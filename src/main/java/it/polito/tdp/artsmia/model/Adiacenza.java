package it.polito.tdp.artsmia.model;

public class Adiacenza {

	private ArtObject primo;
	private ArtObject secondo;
	private Integer peso;

	public Adiacenza(ArtObject primo, ArtObject secondo, Integer peso) {
		super();
		this.primo = primo;
		this.secondo = secondo;
		this.peso = peso;
	}

	public ArtObject getPrimo() {
		return primo;
	}

	public void setPrimo(ArtObject primo) {
		this.primo = primo;
	}

	public ArtObject getSecondo() {
		return secondo;
	}

	public void setSecondo(ArtObject secondo) {
		this.secondo = secondo;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

}
