/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import it.polito.tdp.food.model.Model;
import it.polito.tdp.food.model.PorzioneAdiacente;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtCalorie"
    private TextField txtCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="txtPassi"
    private TextField txtPassi; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCorrelate"
    private Button btnCorrelate; // Value injected by FXMLLoader

    @FXML // fx:id="btnCammino"
    private Button btnCammino; // Value injected by FXMLLoader

    @FXML // fx:id="boxPorzioni"
    private ComboBox<String> boxPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    private TableColumn<PorzioneAdiacente, Integer> colCorrelazione;

    @FXML
    private TableColumn<PorzioneAdiacente, String> colPorzioneAdiacenteName;

    @FXML
    private TableView<PorzioneAdiacente> tblPorzioniAdiacenti;

    @FXML
    void doCammino(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Cerco cammino peso massimo...\n");
    	
    	String tipoPorzione = this.boxPorzioni.getValue();
    	int passi;
    	
    	if(tipoPorzione == null) 
    		txtResult.setText("Scegli un 'Tipo di Porzione' dal menu' a tendina");
    	else {
	    	try {
	    		passi = Integer.parseInt(this.txtPassi.getText());
	    		
	    		List<String> cammino = this.model.calcolaCamminoMassimo(passi, tipoPorzione);
	    		txtResult.appendText("Cammino di peso pari a " + this.model.calcolaPesoCammino(cammino) + "\n");
	    		int i = 1;
	    		for(String s: cammino) {
	    			txtResult.appendText("\n" + i + ") " + s);
	    			i++;
	    		}
	    	} catch(NumberFormatException nfe) {
	    		txtResult.setText("Inserire un valore numerico intero nel campo 'Passi'.\n" +
	    				"Non sono ammessi numeri decimali, caratteri o stringhe");
	    	}
    	}
    	
    }

    @FXML
    void doCorrelate(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Cerco porzioni correlate...");
    	
    	/* grafo creato? Se riesce a cliccare sul bottone 'Correlate' significa che
    	 	la comboBox e' stata riempita, quindi che il grafo e' stato creato */
    	String tipoPorzione = this.boxPorzioni.getValue();
    	if(tipoPorzione == null) {
    		txtResult.setText("Scegli un 'Tipo di Porzione' dal menu' a tendina");
    		return;
    	} else {
    		this.btnCammino.setDisable(false); 	// attivo il bottone -> tipo porzione e' stato scelto
    		List<String> result = this.model.getVicini(tipoPorzione);
    		if(result.size() == 0)
    			txtResult.setText("NON ci sono porzioni correlate a " + tipoPorzione);
    		else {
    			/* Preparo righe tabella per ospitare le porzioni adiacenti */
    			this.tblPorzioniAdiacenti.setItems(FXCollections.observableArrayList
    					(this.model.getPorzioniAdiacenti(tipoPorzione)));
    			for(String s: result) 
    				txtResult.appendText("\n" + s + " " + this.model.getPesoArco(tipoPorzione, s)); 
    		}
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();	// pulisco area di testo
    	this.boxPorzioni.getItems().clear(); 	// pulisco la comboBox
    	
    	txtResult.appendText("Creazione grafo...");
    	double calorie;
    	try {
    		calorie = Double.parseDouble(this.txtCalorie.getText());
    		String msg = model.creaGrafo(calorie);
    		this.boxPorzioni.getItems().addAll(model.getAllVertici());
    		this.btnCorrelate.setDisable(false); 	// attivo il bottone -> serve l'id del bottone
    		
    		/* se creaGrafo fosse di tipo void */
    		txtResult.appendText(" Grafo creato!\n");
    		txtResult.appendText("# VERTICI: " + model.getNVertici() + "\n");
    		txtResult.appendText("# ARCHI: " + model.getNArchi() + "\n");
    		
    		/* se creaGrafo restituisce una stringa */
    		txtResult.appendText("\n" + msg);;
    	} catch(NumberFormatException nfe) {
    		txtResult.setText("Inserire un valore numerico nel campo 'Calorie'.\n" +
    				"Non sono ammessi caratteri o stringhe");
    		return;
    	}
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtCalorie != null : "fx:id=\"txtCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtPassi != null : "fx:id=\"txtPassi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCorrelate != null : "fx:id=\"btnCorrelate\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCammino != null : "fx:id=\"btnCammino\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxPorzioni != null : "fx:id=\"boxPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert colCorrelazione != null : "fx:id=\"colCorrelazione\" was not injected: check your FXML file 'Food.fxml'.";
        assert colPorzioneAdiacenteName != null : "fx:id=\"colPorzioneAdiacenteName\" was not injected: check your FXML file 'Food.fxml'.";
        assert tblPorzioniAdiacenti != null : "fx:id=\"tblPorzioniAdiacenti\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";

        this.colPorzioneAdiacenteName.setCellValueFactory(new PropertyValueFactory<PorzioneAdiacente,String>("adiacente"));
        this.colCorrelazione.setCellValueFactory(new PropertyValueFactory<PorzioneAdiacente,Integer>("correlazione"));
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
