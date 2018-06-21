package it.polito.tdp.seriea.db;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.seriea.model.Match;
import it.polito.tdp.seriea.model.MatchIdMap;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.SeasonIdMap;
import it.polito.tdp.seriea.model.Team;
import it.polito.tdp.seriea.model.TeamIdMap;

public class SerieADAO {
	
	public List<Season> listSeasons(SeasonIdMap stagioniMap) {
		String sql = "SELECT season, description FROM seasons" ;
		
		List<Season> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(stagioniMap.get(new Season(res.getInt("season"), res.getString("description")))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Team> listTeams(TeamIdMap squadreMap) {
		String sql = "SELECT team FROM teams" ;
		
		List<Team> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(squadreMap.get(new Team(res.getString("team")))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<Match> listMatchesFromSeason(SeasonIdMap stagioniMap, TeamIdMap squadreMap, MatchIdMap matchesIdMap, Season season) {
		String sql = "select * from matches where season=?";
		
		List<Match> list = new ArrayList<>();
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, season.getSeason());
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				Team teamA = squadreMap.get(res.getString("HomeTeam"));
				Team teamB = squadreMap.get(res.getString("AwayTeam"));

				Match m = new Match(res.getInt("match_id"), season, teamA, teamB, res.getString("FTR"));
				list.add(matchesIdMap.get(m)) ;
				
				teamA.getMatches().add(matchesIdMap.get(m));
				teamB.getMatches().add(matchesIdMap.get(m));
				season.getMatches().add(matchesIdMap.get(m));
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
}
