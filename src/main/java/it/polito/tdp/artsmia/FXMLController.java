package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ChoiceBox<Integer> boxLUN;

	@FXML
	private Button btnCalcolaComponenteConnessa;

	@FXML
	private Button btnCercaOggetti;

	@FXML
	private Button btnAnalizzaOggetti;

	@FXML
	private TextField txtObjectId;

	@FXML
	private TextArea txtResult;

	@FXML
	void doAnalizzaOggetti(ActionEvent event) {
		this.txtResult.clear();

		this.model.creaGrafo();
		this.txtResult.appendText("Grafo creato!\n");
		this.txtResult.appendText("# VERTICI: " + this.model.nVertici() + "\n");
		this.txtResult.appendText("# ARCHI: " + this.model.nArchi() + "\n");
	}

	@FXML
	void doCalcolaComponenteConnessa(ActionEvent event) {
		this.txtResult.clear();
		this.boxLUN.getItems().clear();
		try {
			Integer id;
			try {
				id = Integer.parseInt(this.txtObjectId.getText());
			} catch (NumberFormatException e) {
				this.txtResult
						.appendText("Errore! Devi selezionare un valore intero rappresentante l'id dell'oggetto!\n");
				return;
			}
			if (!this.model.oggettoPresente(id)) {
				this.txtResult.appendText("Errore! Non corrisponde alcun oggetto all'id selezionato!\n");
				return;
			}
			Integer classification = this.model.componenteConnessa(id);
			this.txtResult.appendText((String.format("La componente connessa che contiene il vertice %d ha %d vertici", id, classification)));
			if(classification > 1) {
				List<Integer> daAggiungere = new ArrayList<>();
				for(int i = 2; i <= classification; i++) {
					daAggiungere.add(i);
				}
				this.boxLUN.getItems().addAll(daAggiungere);
			}
		} catch (RuntimeException e) {
			this.txtResult.appendText("Errore! Per cercare la componente connessa dell'oggetto corrispondente all'id selezionato devi prima creare il grafo!\n");
			return;
		}
	

	}

	@FXML
	void doCercaOggetti(ActionEvent event) {
		this.txtResult.clear();
		try {
			if(this.boxLUN.getItems().size() == 0) {
				this.txtResult.appendText("Errore! Per cercare il cammino massimo devi cercare prima la componente connessa di un oggetto con componente connessa di dimensione maggiore di 1!\n");
				return;
			}
			else if(this.boxLUN.getValue() == null) {
				this.txtResult.appendText("Errore! Per cercare il cammino massimo devi selezionare la lunghezza dall'apposito menu a tendina!\n");
				return;
			}
			
			Integer lunghezza = this.boxLUN.getValue();
			
			Integer id;
			try {
				id = Integer.parseInt(this.txtObjectId.getText());
			} catch (NumberFormatException e) {
				this.txtResult
						.appendText("Errore! Devi selezionare un valore intero rappresentante l'id dell'oggetto!\n");
				return;
			}
			if (!this.model.oggettoPresente(id)) {
				this.txtResult.appendText("Errore! Non corrisponde alcun oggetto all'id selezionato!\n");
				return;
			}
			
			List<ArtObject> best = this.model.camminoMassimo(id, lunghezza);
			this.txtResult.appendText(String.format("Il cammino massimo a partire dall'oggetto con id %d è:\n", id));
			for(ArtObject ao : best) {
				this.txtResult.appendText(ao.toString() + "\n");
			}
			this.txtResult.appendText("Il peso totale è: " + this.model.getMax() + "\n");
					
		} catch (RuntimeException e) {
			this.txtResult.appendText("Errore! Per cercare la componente connessa dell'oggetto corrispondente all'id selezionato devi prima creare il grafo!\n");
			return;
		}
		
	}

	@FXML
	void initialize() {
		assert boxLUN != null : "fx:id=\"boxLUN\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnCalcolaComponenteConnessa != null : "fx:id=\"btnCalcolaComponenteConnessa\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnCercaOggetti != null : "fx:id=\"btnCercaOggetti\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnAnalizzaOggetti != null : "fx:id=\"btnAnalizzaOggetti\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtObjectId != null : "fx:id=\"txtObjectId\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

	}

	public void setModel(Model model) {
		this.model = model;
	}
}
