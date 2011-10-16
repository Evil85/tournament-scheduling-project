using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SchedulingPrototype
{
    struct Match
    {
        public override string ToString()
        {
            return string.Format("{0} vs. {1} at {2}, {3}", Person1.Name, Person2.Name, Place.Name, TimeSlot);
        }

        public StartEndTime TimeSlot { get; set; }
        public Person Person1 { get; set; }
        public Person Person2 { get; set; }
        public Venue Place { get; set; }
    }
}
