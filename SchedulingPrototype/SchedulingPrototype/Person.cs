using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SchedulingPrototype
{
    class Person : Overlappable
    {
        public Person(string name, IEnumerable<StartEndTime> availability)
            : base(name, availability)
        {
        }

        public override string ToString()
        {
            return string.Format("Name: {0}; Availability: {1}", Name, string.Join(", ", Availability));
        }
    }
}
