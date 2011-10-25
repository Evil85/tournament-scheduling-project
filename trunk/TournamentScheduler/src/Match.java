
public class Match {
	
	/**
	 * Represents a match between two players at a court during a time slot.
	 * @param court
	 * @param team1
	 * @param team2
	 * @param timeSlot
	 */
	public Match(Court court, PlayerGroup team1, PlayerGroup team2, TimeSpan timeSlot)
	{
		setCourt(court);
		setGroup1(team1);
		setGroup2(team2);
		setTimeSlot(timeSlot);
	}
	
	public String toString()
    {
        return String.format("%s vs. %s at %s, %s", getGroup1().getName(), getGroup2().getName(), getCourt().getName(), getTimeSlot());
    }
	
	/**
	 * Determines whether or not this match is possible, given the time constraints.
	 * @return
	 */
	public Boolean Possible()
	{
		TimeSpan[] tsArray = new TimeSpan[0];
		return getTimeSlot().Within(getCourt().getAvailability().toArray(tsArray))&&
				getTimeSlot().Within(getGroup1().getAvailability().toArray(tsArray)) &&
				getTimeSlot().Within(getGroup2().getAvailability().toArray(tsArray));
	}

	public Court getCourt() {
		return m_court;
	}

	private void setCourt(Court m_court) {
		this.m_court = m_court;
	}

	public PlayerGroup getGroup1() {
		return m_group1;
	}

	private void setGroup1(PlayerGroup m_group1) {
		this.m_group1 = m_group1;
	}

	public PlayerGroup getGroup2() {
		return m_group2;
	}

	private void setGroup2(PlayerGroup m_group2) {
		this.m_group2 = m_group2;
	}

	public TimeSpan getTimeSlot() {
		return m_timeSlot;
	}

	private void setTimeSlot(TimeSpan m_timeSlot) {
		this.m_timeSlot = m_timeSlot;
	}

	private Court m_court;
	private PlayerGroup m_group1;
	private PlayerGroup m_group2;
	private TimeSpan m_timeSlot;
	
}
