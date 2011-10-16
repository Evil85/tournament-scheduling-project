using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SchedulingPrototype
{
    struct StartEndTime
    {
        public override string ToString()
        {
            string strStartDate = Start.ToShortDateString();
            string strEndDate = End.ToShortDateString();
            if (strEndDate == strStartDate)
                return string.Format("{0} {1}-{2}", strStartDate, Start.ToShortTimeString(), End.ToShortTimeString());
            else
                return string.Format("{0} {1}-{2} {3}", strStartDate, Start.ToShortTimeString(), strEndDate, End.ToShortTimeString());
        }

        public bool Overlaps(StartEndTime other)
        {
            return (other.Start <= Start && other.End >= End) ||
                (other.Start > Start && other.Start < End) ||
                (other.End < End && other.End > Start);
        }

        public DateTime Start { get; set; }
        public DateTime End { get; set; }
    }
}
