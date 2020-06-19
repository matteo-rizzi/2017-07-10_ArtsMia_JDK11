package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private Map<Integer, ArtObject> idMap;
	private ArtsmiaDAO dao;
	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	private List<ArtObject> best;
	private Integer max;

	public Model() {
		this.dao = new ArtsmiaDAO();
		this.idMap = new HashMap<>();
		this.dao.listObjects(this.idMap);
	}

	public void creaGrafo() {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

		// aggiungo i vertici
		Graphs.addAllVertices(this.grafo, this.idMap.values());

		// aggiungo gli archi
		for (Adiacenza a : this.dao.getAdiacenze(idMap)) {
			Graphs.addEdge(this.grafo, a.getPrimo(), a.getSecondo(), a.getPeso());
		}
	}

	public int nVertici() {
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}

	public boolean oggettoPresente(Integer id) {
		boolean trovato = false;
		if (this.grafo.vertexSet().contains(idMap.get(id)))
			trovato = true;
		return trovato;
	}

	public int componenteConnessa(Integer source) {
		List<ArtObject> connessa = new ArrayList<>();

		GraphIterator<ArtObject, DefaultWeightedEdge> bfv = new BreadthFirstIterator<>(this.grafo,
				this.idMap.get(source));
		while (bfv.hasNext()) {
			connessa.add(bfv.next());
		}

		return connessa.size();
	}

	public List<ArtObject> camminoMassimo(Integer source, Integer lunghezza) {
		this.best = new ArrayList<>();
		this.max = 0;

		List<ArtObject> parziale = new ArrayList<>();
		parziale.add(this.idMap.get(source));

		this.cerca(parziale, lunghezza, this.idMap.get(source).getClassification());

		Collections.sort(best);

		return best;
	}

	// 4318
	private void cerca(List<ArtObject> parziale, Integer lunghezza, String classification) {
		// caso terminale
		if (parziale.size() == lunghezza) {
			if (calcolaPeso(parziale) > max) {
				this.best = new ArrayList<>(parziale);
				max = calcolaPeso(parziale);
				System.out.println(parziale);
			} 
			return;
		}

		// caso intermedio
		for (ArtObject ao : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size() - 1))) {
			if (ao.getClassification().equals(classification) && !parziale.contains(ao) && ao.getClassification() != null) {
				parziale.add(ao);
				this.cerca(parziale, lunghezza, classification);
				parziale.remove(ao);
			}
		}

	}

	private Integer calcolaPeso(List<ArtObject> parziale) {
		int pesoTot = 0;
		for(int i = 0; i< parziale.size() - 1; i++) {
			int peso = (int) this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(i), parziale.get(i + 1)));
			pesoTot += peso;
		}
		return pesoTot;
	}

	public Integer getMax() {
		return max;
	}

}
