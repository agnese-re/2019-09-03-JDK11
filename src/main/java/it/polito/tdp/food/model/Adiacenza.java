package it.polito.tdp.food.model;

public class Adiacenza {

	private String tipoPortion1;
	private String tipoPortion2;
	private int pesoArco;
	
	public Adiacenza(String tipoPortion1, String tipoPortion2, int pesoArco) {
		super();
		this.tipoPortion1 = tipoPortion1;
		this.tipoPortion2 = tipoPortion2;
		this.pesoArco = pesoArco;
	}

	public String getTipoPortion1() {
		return tipoPortion1;
	}

	public void setTipoPortion1(String tipoPortion1) {
		this.tipoPortion1 = tipoPortion1;
	}

	public String getTipoPortion2() {
		return tipoPortion2;
	}

	public void setTipoPortion2(String tipoPortion2) {
		this.tipoPortion2 = tipoPortion2;
	}

	public int getPesoArco() {
		return pesoArco;
	}

	public void setPesoArco(int pesoArco) {
		this.pesoArco = pesoArco;
	}

	@Override
	public String toString() {
		return "Adiacenza [tipoPortion1=" + tipoPortion1 + ", tipoPortion2=" + tipoPortion2 + ", pesoArco=" + pesoArco
				+ "]";
	}
	
}
