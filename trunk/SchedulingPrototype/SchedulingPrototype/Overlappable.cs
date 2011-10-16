using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SchedulingPrototype
{
    class Overlappable : IComparable
    {
        public Overlappable(string name, IEnumerable<StartEndTime> availability)
        {
            m_name = name;
            m_availability = availability;
            m_availabilityMinutes = Availability.Sum(o => (o.End - o.Start).TotalMinutes);
        }

        public override bool Equals(object obj)
        {
            Overlappable other = obj as Overlappable;
            if (other == null)
                return Name == other.Name && Availability.Equals(other.Availability);
            else
                return object.Equals(this, obj);
        }

        public override int GetHashCode()
        {
            int hash = 17;
            hash = hash * 31 + Name.GetHashCode();
            foreach (StartEndTime t in Availability)
                hash = hash * 31 + t.GetHashCode();
            return hash;
        }

        public int CompareTo(object obj)
        {
            Overlappable other = obj as Overlappable;
            if (other != null)
            {
                int nAvailabilityDifference = AvailabilityMinutes.CompareTo(other.AvailabilityMinutes);
                if (nAvailabilityDifference != 0)
                    return nAvailabilityDifference;
                else
                    return Name.CompareTo(other.Name);
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

        public string Name
        {
            get { return m_name; }
        }

        public IEnumerable<StartEndTime> Availability
        {
            get { return m_availability; }
        }

        public double AvailabilityMinutes
        {
            get { return m_availabilityMinutes; }
        }

        private string m_name;
        private IEnumerable<StartEndTime> m_availability;
        private double m_availabilityMinutes;
    }
}
