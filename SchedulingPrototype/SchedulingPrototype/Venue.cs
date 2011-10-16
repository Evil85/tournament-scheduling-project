using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SchedulingPrototype
{
    class Venue : Overlappable
    {
        public Venue(string name, IEnumerable<StartEndTime> availability)
            : base(name, availability)
        {
        }
    }
}
