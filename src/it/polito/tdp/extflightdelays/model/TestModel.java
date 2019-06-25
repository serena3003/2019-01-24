package it.polito.tdp.extflightdelays.model;

import java.util.Map;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		
		model.creaGrafo();
		System.out.println("Grafo creato di " + model.getGrafo().vertexSet().size() + " vertici e "+ model.getGrafo().edgeSet().size() + " archi");
		
		Map<String, Integer> nTuristi = model.simula(15, 15, "NM");

		for(String s : nTuristi.keySet()) {
			if(nTuristi.get(s)!=0)
				System.out.println(s + " - " + nTuristi.get(s)+"\n");
    	}
	}

}
