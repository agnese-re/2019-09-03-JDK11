package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private Graph<String,DefaultWeightedEdge> grafo;
	private FoodDao dao;
	
	// variabili per algoritmo ricorsivo
	private List<String> soluzioneBest;
	private int pesoCamminoMassimo;
	
	public Model() {
		dao = new FoodDao();
	}
	
	public String creaGrafo(double calories) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, dao.getAllVertici(calories));
		
		for(Adiacenza a: dao.getArchi(calories))
			Graphs.addEdgeWithVertices(this.grafo, a.getTipoPortion1(), 
					a.getTipoPortion2(), a.getPesoArco());
		
		// STAMPA SU CONSOLE A FINE DI DEBUG
		System.out.format("Grafo creato con %d vertici e %d archi",
				grafo.vertexSet().size(), grafo.edgeSet().size());
		
		// STAMPA PER INTERFACCIA
		String message = String.format("Grafo creato (%d vertici, %d archi)", 
				grafo.vertexSet().size(), grafo.edgeSet().size());
		
		return message;
		
	}
	
	public List<String> getAllVertici() {
		List<String> vertici = new ArrayList<String>(this.grafo.vertexSet());
		Collections.sort(vertici);
		return vertici;
	}
	
	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	/* RAGGIUNGIBILI DAL TIPO DI PORZIONE SCELTO DALL'UTENTE */
	public List<String> getComponenteConnessa(String tipoPorzione) {
		ConnectivityInspector<String,DefaultWeightedEdge> ci =
				new ConnectivityInspector<>(this.grafo);
		List<String> compConnesse = new ArrayList<String>(ci.connectedSetOf(tipoPorzione));
		return compConnesse;
	}
	
	/* DIRETTAMENTE CONNESSI CON IL TIPO DI PORZIONE SCELTA DALL'UTENTE */
	public List<String> getVicini(String tipoPorzione) {
		List<String> vicini = Graphs.neighborListOf(this.grafo, tipoPorzione);
		return vicini;
	}
	
	/* ok se considero solo i vicini. Con componente connessa NON ho arco diretto 
	 	-> NullPointerException */
	public int getPesoArco(String tipoPorzione, String connesso) {
		DefaultWeightedEdge edge = this.grafo.getEdge(tipoPorzione, connesso);
		return (int)this.grafo.getEdgeWeight(edge);
	}
	
	public List<PorzioneAdiacente> getPorzioniAdiacenti(String tipoPorzione) {
		List<String> vicini = Graphs.neighborListOf(this.grafo, tipoPorzione);
		List<PorzioneAdiacente> result = new ArrayList<PorzioneAdiacente>();
		for(String vicino: vicini) {
			DefaultWeightedEdge edge = this.grafo.getEdge(tipoPorzione, vicino);
			int peso = (int)this.grafo.getEdgeWeight(edge);
			result.add(new PorzioneAdiacente(vicino,peso));
		}
		Collections.sort(result); 	// in ordine di correlazione crescente (peso dell'arco)
		return result;
	}
	
	/* cammino = sequenza di vertici (tipi di porzione) */
	/* Riempio una lista di stringhe 'parziale' con i vertici costituenti un ipotetico 
	 	cammino. Il primo vertice sicuramente presente nella lista e' il tipo di porzio-
	 	ne scelto dall'utente. Il cammino deve essere di lunghezza ESATTAMENTE PARI A N,
	 	deve essere semplice, non contenere cicli o vertici ripetuti */
	public List<String> calcolaCamminoMassimo(int passi, String tipoPorzione) {
		
		this.soluzioneBest = new ArrayList<String>();	// diversa ogni volta che cambio N (passi)
		this.pesoCamminoMassimo = 0;
		
		List<String> parziale = new ArrayList<String>();
		parziale.add(tipoPorzione);
		cercaCammino(parziale,passi);
		
		return soluzioneBest;
		
	}
	
	private void cercaCammino(List<String> parziale, int passi) {
		// CASI TERMINALI
		if(parziale.size() == passi) {
			// potrebbe essere soluzione migliore
			int pesoCamminoAttuale = calcolaPesoCammino(parziale);
			if(pesoCamminoAttuale > pesoCamminoMassimo) {
				pesoCamminoMassimo = pesoCamminoAttuale;
				soluzioneBest = new ArrayList<String>(parziale);	// ATTENZIONE! NON COPIARE IL RIFERIMENTO
			}
			return;		// esco sia che la condizione dell'if sia verificata che non
		}
		// ALGORITMO RICORSIVO
		List<String> vicini = Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1));
		for(String vicino: vicini) 
			if(!parziale.contains(vicino)) {	// vicini non ancora contenuti nel cammino -> altrimenti LOOP -> NO VERTICI RIPETUTI, NO CICLI
				parziale.add(vicino);
				cercaCammino(parziale,passi);
				parziale.remove(parziale.size()-1);
		}
		
	}
	
	public int calcolaPesoCammino(List<String> parziale) {
		int pesoCammino = 0;
		
		for(int indice = 0; indice < parziale.size()-1; indice++) {
			String sourceVertex = parziale.get(indice);
			String destVertex = parziale.get(indice+1);
			DefaultWeightedEdge edge = this.grafo.getEdge(sourceVertex, destVertex);
			pesoCammino += this.grafo.getEdgeWeight(edge);
		}
		
		return pesoCammino;
	}
	
	public boolean grafoCreato() {
		if(this.grafo == null)
			return false;
		return true;
	}
}
