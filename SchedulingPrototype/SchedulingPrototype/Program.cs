using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Diagnostics;

namespace SchedulingPrototype
{
    class Program
    {
        static void Main(string[] args)
        {
            Stopwatch stopwatch = new Stopwatch();
            stopwatch.Start();

            //StartEndTime period = new StartEndTime { Start = new DateTime(2011, 10, 9), End = new DateTime(2011, 10, 10).AddMinutes(-1) };
            StartEndTime period = new StartEndTime { Start = new DateTime(2011, 10, 9, 1, 0, 0), End = new DateTime(2011, 11, 9, 10, 0, 0) };
            TimeSpan matchLength = new TimeSpan(1, 0, 0);

            Venue venue1 = new Venue("venue1", new StartEndTime[]
            {
                new StartEndTime{Start = new DateTime(2011, 10, 9), End = new DateTime(2011, 10, 10)}
            });

            SortedSet<Person> people = RandomPeople(20, period);

            stopwatch.Stop();
            Console.WriteLine("Generated random tournament in {0}ms.\n", stopwatch.ElapsedMilliseconds);
            PrintRoster(people);
            stopwatch.Restart();

            IEnumerable<Match> matches = RoundRobinScheduler.CreateSchedule(matchLength, new Venue[] {venue1}, people);

            stopwatch.Stop();
            Console.WriteLine("Created Round Robin schedule in {0}ms.\n", stopwatch.ElapsedMilliseconds);

            PrintSchedule(matches);

            Console.ReadKey();
        }

        private static void PrintRoster(IEnumerable<Person> people)
        {
            Console.WriteLine("Roster:");
            foreach (Person pers in people)
                Console.WriteLine(pers);
            Console.WriteLine();
        }

        private static void PrintSchedule(IEnumerable<Match> matches)
        {
            Console.WriteLine("Schedule: ({0} matches)", matches.Count());
            foreach (Match mat in matches)
                Console.WriteLine(mat);
            Console.WriteLine();
        }

        private static IEnumerable<StartEndTime> RandomAvailability(StartEndTime period, bool bAtLeastoneTimeSpan, int nMaxTimeSpans, TimeSpan minimum, TimeSpan maximum)
        {
            int nTimeSpans = s_generator.Next(bAtLeastoneTimeSpan ? 1 : 0, nMaxTimeSpans);

            int nMinMinutes = (int)minimum.TotalMinutes;
            int nMaxMinutes = (int)maximum.TotalMinutes;
            int nPeriodMinutes = (int)(period.End - period.Start).TotalMinutes;

            IList<StartEndTime> availability = new List<StartEndTime>();
            for (int i = 0; i < nTimeSpans; i++)
            {
                TimeSpan span = new TimeSpan(0, s_generator.Next(nMinMinutes, nMaxMinutes), 0);
                TimeSpan offset = new TimeSpan(0, s_generator.Next(nPeriodMinutes - (int)span.TotalMinutes), 0);
                StartEndTime avail = new StartEndTime { Start = period.Start + offset, End = period.Start + offset + span };
                if (!availability.Any(a => a.Overlaps(avail)))
                    availability.Add(avail);
            }

            return availability;
        }

        private static IEnumerable<StartEndTime> InverseAvailability(StartEndTime period, params StartEndTime[] availability)
        {
            if (availability.Any())
            {
                DateTime currentTime = period.Start;
                foreach (StartEndTime avail in availability)
                {
                    if (avail.Start > currentTime)
                        yield return new StartEndTime { Start = currentTime, End = avail.Start.AddMinutes(-1) };
                    currentTime = avail.End > currentTime ? avail.End : currentTime;
                }

                if (currentTime < period.End)
                    yield return new StartEndTime { Start = currentTime, End = period.End };
            }
            else
            {
                yield return period;
            }
        }

        private static SortedSet<Person> RandomPeople(int nPeople, StartEndTime period)
        {
            SortedSet<Person> people = new SortedSet<Person>();
            TimeSpan minimum = new TimeSpan(1, 0, 0);
            TimeSpan maximum = new TimeSpan(4, 0, 0);
            for (int i = 0; i < nPeople; i++)
                people.Add(new Person("person" + i, InverseAvailability(period, RandomAvailability(period, false, 2, minimum, maximum).ToArray())));
            return people;
        }

        private static Random s_generator = new Random();
    }
}
