package it.polito.tdp.food.model;

public class PorzioneAdiacente implements Comparable<PorzioneAdiacente> {

	private String adiacente;
	private int correlazione;	// peso dell'arco tra una porzione scelta e adiacente
	
	public PorzioneAdiacente(String adiacente, int correlazione) {
		super();
		this.adiacente = adiacente;
		this.correlazione = correlazione;
	}

	public String getAdiacente() {
		return adiacente;
	}

	public void setAdiacente(String adiacente) {
		this.adiacente = adiacente;
	}

	public int getCorrelazione() {
		return correlazione;
	}

	public void setCorrelazione(int correlazione) {
		this.correlazione = correlazione;
	}

	@Override
	public int compareTo(PorzioneAdiacente o) {
		return this.getCorrelazione()-o.getCorrelazione();
	}
	
	
}
