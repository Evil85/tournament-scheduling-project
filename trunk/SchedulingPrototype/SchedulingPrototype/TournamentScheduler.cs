using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SchedulingPrototype
{
    class TournamentScheduler
    {
        protected static bool Conflict(Match potentialMatch, IList<Match> existingMatches)
        {
            return existingMatches.Any(mat =>
                {
                    if (potentialMatch.TimeSlot.Overlaps(mat.TimeSlot))
                    {
                        if (potentialMatch.Person1 == mat.Person1 ||
                            potentialMatch.Person1 == mat.Person2 ||
                            potentialMatch.Person2 == mat.Person1 ||
                            potentialMatch.Person2 == mat.Person2 ||
                            potentialMatch.Place == mat.Place)
                            return true;
                    }
                    return false;
                });
        }

        protected static bool Adjacency(Match potentialMatch, IList<Match> existingMatches, TimeSpan adjacencyThreshold)
        {
            StartEndTime expandedTimeSlot = potentialMatch.TimeSlot;
            expandedTimeSlot.Start -= adjacencyThreshold;
            expandedTimeSlot.End += adjacencyThreshold;
            potentialMatch.TimeSlot = expandedTimeSlot;

            return existingMatches.Any(mat =>
            {
                if (potentialMatch.TimeSlot.Overlaps(mat.TimeSlot))
                {
                    if (potentialMatch.Person1 == mat.Person1 ||
                        potentialMatch.Person1 == mat.Person2 ||
                        potentialMatch.Person2 == mat.Person1 ||
                        potentialMatch.Person2 == mat.Person2)
                        return true;
                }
                return false;
            });
        }
    }
}
