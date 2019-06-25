package it.polito.tdp.extflightdelays.model;

public class Evento implements Comparable<Evento> {
	
	private int turista;
	private String statoPartenza;
	private int giorno;
	
	public Evento(int turista, String statoPartenza, int giorno) {
		this.turista = turista;
		this.statoPartenza=statoPartenza;
		this.giorno=giorno;
	}

	@Override
	public int compareTo(Evento other) {
		
		return this.giorno-other.getGiorno();
	}

	public int getTurista() {
		return turista;
	}

	public String getStatoPartenza() {
		return statoPartenza;
	}

	public int getGiorno() {
		return giorno;
	}
	
	

}
