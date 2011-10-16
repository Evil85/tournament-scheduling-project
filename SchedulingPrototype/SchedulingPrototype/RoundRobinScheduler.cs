using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SchedulingPrototype
{
    class RoundRobinScheduler : TournamentScheduler
    {
        public static IEnumerable<Match> CreateSchedule(TimeSpan matchLength, IEnumerable<Venue> venues, SortedSet<Person> people)
        {
            SortedSet<Overlap> overlaps = new SortedSet<Overlap>();
            int i = 0;
            foreach (Person pers in people)
            {
                foreach (Person other in people.Skip(i + 1))
                    overlaps.Add(new Overlap(pers, other));
                i++;
            }

            IList<Match> matches = new List<Match>();
            if (overlaps.Any())
            {
                if (!TryScheduleMatch(matchLength, overlaps, venues, ref matches))
                {
                    Person difficultPerson = (Person)overlaps.First().Overlappables.First();
                    Console.WriteLine("WARNING: skipping {0}.\n", difficultPerson.Name);
                    people.Remove(difficultPerson);
                    return CreateSchedule(matchLength, venues, people);
                }
            }
            else
            {
                Console.WriteLine("WARNING: Could not schedule any matches.\n");
            }
            return matches;
        }

        private static bool TryScheduleMatch(TimeSpan matchLength, SortedSet<Overlap> overlaps, IEnumerable<Venue> venues, ref IList<Match> existingMatches)
        {
            Overlap currentOverlap = overlaps.First();
            overlaps.Remove(currentOverlap);
            IList<Match> fallbackMatches = new List<Match>();

            foreach (Venue ven in venues)
            {
                Overlap venueOverlap = new Overlap(currentOverlap, ven);
                Match potentialMatch = new Match { Person1 = (Person)currentOverlap.Overlappables.First(), Person2 = (Person)currentOverlap.Overlappables.ElementAt(1), Place = ven };
                foreach (StartEndTime window in venueOverlap.OverlapTimes)
                {
                    StartEndTime matchTime = new StartEndTime { Start = window.Start, End = window.Start + matchLength };
                    while (matchTime.End <= window.End)
                    {
                        potentialMatch.TimeSlot = matchTime;
                        if (!Conflict(potentialMatch, existingMatches) && Adjacency(potentialMatch, existingMatches, matchLength))
                            fallbackMatches.Add(potentialMatch);
                        else if (TryAddMatch(potentialMatch, matchLength, overlaps, venues, ref existingMatches))
                            return true;

                        matchTime.Start = matchTime.Start + matchLength;
                        matchTime.End = matchTime.End + matchLength;
                    }
                }
            }

            foreach (Match fallback in fallbackMatches)
                if (TryAddMatch(fallback, matchLength, overlaps, venues, ref existingMatches))
                    return true;

            overlaps.Add(currentOverlap);
            return false;
        }

        private static bool TryAddMatch(Match potentialMatch, TimeSpan matchLength, SortedSet<Overlap> overlaps, IEnumerable<Venue> venues, ref IList<Match> existingMatches)
        {
            if (!Conflict(potentialMatch, existingMatches))
            {
                existingMatches.Add(potentialMatch);

                if (!overlaps.Any() || TryScheduleMatch(matchLength, overlaps, venues, ref existingMatches))
                    return true;
                else
                    existingMatches.Remove(potentialMatch);
            }
            return false;
        }
    }
}
