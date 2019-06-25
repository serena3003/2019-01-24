package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;


public class Simulazione {
	
	private PriorityQueue<Evento> queue;
	private int nTuristi;
	private int nGiorni;
	private Graph<String, DefaultWeightedEdge> grafo;
	
	private Map<String, Integer> nTuristiPerStato;

	public Simulazione() {
		queue = new PriorityQueue<Evento>();
		nTuristiPerStato = new HashMap<String, Integer>();
	}

	public void init(int t, int g, String state, Graph<String, DefaultWeightedEdge> grafo) {
		
		nTuristi = t; 
		nGiorni = g;
		this.grafo=grafo;
		
		for(String s : grafo.vertexSet()) {
			if(s.equals(state)) {
				nTuristiPerStato.put(state, t);
			} else {
				nTuristiPerStato.put(s,0);
			}
		}
		
		//pulisco la coda
		queue.clear();
		
		//aggiungo gli eventi iniziali -> ho t turisti nello stato state che vogliono partire il primo giorno
		for(int i = 0; i<nTuristi; i++) {
			queue.add(new Evento(i+1,state, 0));
		}
	}

	public void run() {
		while(!queue.isEmpty()) {
			Evento e = queue.poll();
			
			if(e.getGiorno()!=nGiorni) {
				int turisti = e.getTurista();
				String vecchioStato = e.getStatoPartenza();
				String nuovoStato = getProbabilitaVolo(e.getStatoPartenza());
				
				//aggiungo l'evento alla coda
				queue.add(new Evento(turisti, nuovoStato, e.getGiorno()+1));
				//aggiorno la mappa
				nTuristiPerStato.put(nuovoStato, nTuristiPerStato.get(nuovoStato)+1);
				nTuristiPerStato.put(vecchioStato, nTuristiPerStato.get(vecchioStato)-1);
			}
			
		}
		
	}
	
	public Map<String, Integer> getnTuristiPerStato() {
		return nTuristiPerStato;
	}
	
	public String getProbabilitaVolo(String state){
			
			double probTot = 0.0;
			String res = null;
			double probStato = 0.0;
			
			//calcolo la somma di tutti i pesi
			for(DefaultWeightedEdge e : grafo.outgoingEdgesOf(state)) {
				probTot = probTot + grafo.getEdgeWeight(e);
			}
			
			for(DefaultWeightedEdge e : grafo.outgoingEdgesOf(state)) {
				//se la probabilita dello stato e' maggiore del massimo trovato, aggiorno
				if((grafo.getEdgeWeight(e)/probTot) > probStato) {
					res = grafo.getEdgeTarget(e);
					probStato = grafo.getEdgeWeight(e)/probTot;
				}
			}
			System.out.println(state + " - " + res);
			return res;
	}

}
