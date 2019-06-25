package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {

	private ExtFlightDelaysDAO dao;
	private List<String> states;
	private Graph<String, DefaultWeightedEdge> grafo;
	private Map<Integer, Airport> airportMap; // ordino gli aeroporti per id
	private Simulazione simulatore;

	public Model() {
		this.dao = new ExtFlightDelaysDAO();
		grafo = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		simulatore = new Simulazione();

		states = new ArrayList<String>(dao.loadAllStates());
		// System.out.println(states.size());
		airportMap = new HashMap<Integer, Airport>();
		for (Airport a : dao.loadAllAirports()) {
			airportMap.put(a.getId(), a);
		}
	}

	public List<String> getStates() {
		Collections.sort(states);
		return states;
	}

	public void creaGrafo() {

		// aggiungo i vertici
		Graphs.addAllVertices(this.grafo, states);

		// aggiungo vertici e peso
		for (String s1 : this.grafo.vertexSet()) {
			for (String s2 : this.grafo.vertexSet()) {
				int f = 0;
				f = dao.getNFlight(s1, s2);
				if (f > 0) {
					Graphs.addEdgeWithVertices(this.grafo, s1, s2, f);
				}
			}
		}
	}

	public Graph<String, DefaultWeightedEdge> getGrafo() {
		return this.grafo;
	}

	public List<String> getVicini(String state) {
		List<String> vicini = new ArrayList<String>();
		for(DefaultWeightedEdge e : grafo.outgoingEdgesOf(state)) {
				String s = grafo.getEdgeWeight(e) + " - " + grafo.getEdgeTarget(e);
				vicini.add(s);
		}
		Collections.sort(vicini);
		return vicini;
	}

	public Map<String, Integer> simula(int t, int g, String state) {
		
		simulatore.init(t,g,state, grafo);
		simulatore.run();
		return simulatore.getnTuristiPerStato();
	}

}
