using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SchedulingPrototype
{
    class Overlap : IComparable
    {
        public Overlap(params Overlappable[] overlappables)
        {
            if (overlappables.ElementAtOrDefault(1) == null)
                throw new InvalidOperationException("Cannot create an overlap of less than two overlappables.");

            m_overlappables = new SortedSet<Overlappable>(overlappables);
            m_overlapTimes = CalculateOverlaps();
            m_overlapMinutes = m_overlapTimes.Sum(over => (over.End - over.Start).TotalMinutes);
        }

        public Overlap(Overlap other, params Overlappable[] overlappables)
        {
            m_overlappables = new SortedSet<Overlappable>(overlappables);
            m_overlapTimes = new List<StartEndTime>(other.OverlapTimes);
            foreach (Overlappable over in other.Overlappables)
                m_overlappables.Add(over);
            m_overlapMinutes = m_overlapTimes.Sum(over => (over.End - over.Start).TotalMinutes);
        }

        private IList<StartEndTime> CalculateOverlaps()
        {
            return CalculateOverlaps(new List<StartEndTime>(Overlappables.First().Availability));
        }

        private IList<StartEndTime> CalculateOverlaps(IList<StartEndTime> startingAvailability)
        {
            IList<StartEndTime> combinedAvailability = startingAvailability;

            foreach (Overlappable over in Overlappables)
            {
                IEnumerator<StartEndTime> availabilityEnum = over.Availability.GetEnumerator();
                foreach (StartEndTime combinedAvail in combinedAvailability.ToArray())
                {
                    while (availabilityEnum.Current.End <= combinedAvail.Start)
                        if (!availabilityEnum.MoveNext())
                            return combinedAvailability;

                    combinedAvailability.Remove(combinedAvail);
                    while (availabilityEnum.Current.Start < combinedAvail.End)
                    {
                        if (availabilityEnum.Current.Start > combinedAvail.Start)
                        {
                            if (availabilityEnum.Current.End < combinedAvail.End)
                                combinedAvailability.Add(new StartEndTime { Start = availabilityEnum.Current.Start, End = availabilityEnum.Current.End });
                            else
                                combinedAvailability.Add(new StartEndTime { Start = availabilityEnum.Current.Start, End = combinedAvail.End });
                        }
                        else if (availabilityEnum.Current.End < combinedAvail.End)
                        {
                            combinedAvailability.Add(new StartEndTime { Start = combinedAvail.Start, End = availabilityEnum.Current.End });
                        }
                        else
                        {
                            combinedAvailability.Add(combinedAvail);
                        }
                        if (!availabilityEnum.MoveNext())
                            return combinedAvailability;
                    }
                }
            }

            return combinedAvailability;
        }

        public override bool Equals(object obj)
        {
            Overlap other = obj as Overlap;
            if (other == null)
                return OverlapMinutes == other.OverlapMinutes;
            else
                return base.Equals(obj);
        }

        public override int GetHashCode()
        {
            int hash = 17;
            hash = hash * 31 + m_period.GetHashCode();
            foreach (Overlappable over in Overlappables)
                hash = hash * 31 + over.GetHashCode();
            return hash;
        }

        public int CompareTo(object obj)
        {
            Overlap other = obj as Overlap;
            if (other != null)
            {
                int nAvailabilityDifference = OverlapMinutes.CompareTo(other.OverlapMinutes);
                if (nAvailabilityDifference != 0)
                    return nAvailabilityDifference;
                else
                    return string.Concat(Overlappables.Select(over => over.Name)).CompareTo(string.Concat(other.Overlappables.Select(over => over.Name)));
            }
            else if (obj == null)
            {
                return 1;
            }
            else
            {
                throw new ArgumentException();
            }
        }

        public SortedSet<Overlappable> Overlappables
        {
            get { return m_overlappables; }
        }

        public IEnumerable<StartEndTime> OverlapTimes
        {
            get { return m_overlapTimes; }
        }

        public double OverlapMinutes
        {
            get { return m_overlapMinutes; }
        }

        private SortedSet<Overlappable> m_overlappables;
        private StartEndTime m_period;
        private IList<StartEndTime> m_overlapTimes;
        private double m_overlapMinutes;
    }
}
