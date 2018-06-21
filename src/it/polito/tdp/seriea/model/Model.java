package it.polito.tdp.seriea.model;

import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private List<Season> stagioni;
	private SeasonIdMap stagioniMap;
	private List<Team> squadre;
	private List<Team> squadreDaStagione;
	private TeamIdMap squadreMap;
	private List<Match> matches;
	private MatchIdMap matchesIdMap;
	private SerieADAO dao;
	
	private SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge> graph;
	// vertici: insieme delle squadre che hanno giocato nella stagione indicata
	// archi: risultato della partita
	
	// peso dell'arco
	// +1 se TeamA batte TeamB
	// 0 pareggio
	// -1 se TeamB batte TeamA
	
	
	public Model() {
		stagioniMap = new SeasonIdMap();
		squadreMap = new TeamIdMap();
		matchesIdMap = new MatchIdMap();
		squadreDaStagione = new ArrayList<>();
		
		dao = new SerieADAO();
		
		stagioni = dao.listSeasons(stagioniMap);
		squadre = dao.listTeams(squadreMap);
			
		graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
	}
	
	public void createGraph(Season stagione) {
		stagioniMap = new SeasonIdMap();
		squadreMap = new TeamIdMap();
		matchesIdMap = new MatchIdMap();
		squadreDaStagione = new ArrayList<>();
		
		dao = new SerieADAO();
		
		stagioni = dao.listSeasons(stagioniMap);
		squadre = dao.listTeams(squadreMap);
			
		graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		matches = dao.listMatchesFromSeason(stagioniMap, squadreMap, matchesIdMap, stagione);
		
		for(Match m : matches) {
			squadreDaStagione.add(m.getHomeTeam());
			squadreDaStagione.add(m.getAwayTeam());
		}
		
		// aggiunta vertici
		Graphs.addAllVertices(graph, this.squadreDaStagione);
	
		// aggiunta archi
		for(Match m : matches) {
			Team teamHome = m.getHomeTeam();
			Team teamAway = m.getAwayTeam();
			
			int peso=0;
			
			if(!teamHome.equals(teamAway)) {
				if(m.getFtr().equals("H")) {
					// vince home
					peso = 1;
					teamHome.setPunteggio(3);
				} else if(m.getFtr().equals("A")) {
					teamAway.setPunteggio(3);
					peso = -1;
				} else {
					teamHome.setPunteggio(1);
					teamAway.setPunteggio(1);
				}
				Graphs.addEdge(graph, teamHome, teamAway, peso);
			}
		}
		
	}
	
	public List<Team> getClassifica() {
		List<Team> classifica = new LinkedList<Team>(graph.vertexSet());
		// 3 punti alle vittorie, 0 alle sconfitte, 1 ai pareggi
		
		for(DefaultWeightedEdge e: graph.edgeSet()) {
			Team home = graph.getEdgeSource(e);
			Team away = graph.getEdgeTarget(e);
			switch ((int) graph.getEdgeWeight(e)) {
			case +1:
				home.setPunteggio(3);
				break;
			case -1:
				away.setPunteggio(3);
				break;
			case 0: 
				home.setPunteggio(1);
				away.setPunteggio(1);
				break;
			}
		}
		
		Collections.sort(classifica, new Comparator<Team>() {
			
			@Override
			public int compare(Team t1, Team t2) {
				return -(t1.getPunteggio() - t2.getPunteggio());
			}
		});
		
		return classifica;
	}
	
	public List<Season> getListaStagioni() {
		if(this.stagioni == null)  {
			return new ArrayList<Season>();
		}
		return this.stagioni;
	}

	
	public List<Team> getListaSquadre() {
		if(this.squadre== null) {
			return new ArrayList<Team>();
		}
		return this.squadre;
	}
}
