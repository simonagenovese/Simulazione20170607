package it.polito.tdp.seriea.model;

import java.util.*;

public class Team {
	
	private String team ;
	private List<Match> matches;
	private int punteggio;

	public Team(String team) {
		super();
		this.team = team;
		punteggio = 0;
		matches = new ArrayList<>();
	}

	/**
	 * @return the team
	 */
	public String getTeam() {
		return team;
	}
	
	public List<Match> getMatches() {
		return matches;
	}

	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

	/**
	 * @param team the team to set
	 */
	public void setTeam(String team) {
		this.team = team;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return team;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((team == null) ? 0 : team.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		if (team == null) {
			if (other.team != null)
				return false;
		} else if (!team.equals(other.team))
			return false;
		return true;
	}

	public void setPunteggio(int punteggio) {
		this.punteggio+=punteggio;
	}
	
	public int getPunteggio() {
		return this.punteggio;
	}
	
	
	

}
