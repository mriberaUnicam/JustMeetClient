package views;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class IdBox {

	//static Event nuovoEvento;

	public static int display(String title, String message, String logged) {
		
		int idRestituito = 0;
		
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		
		Label label = new Label();
		label.setText(message);
		
		TextField codice = new TextField();
		codice.setPromptText("Codice dell'evento da modificare");
		//se o non corrispondente dare errore con alertBox
		
		Label codicel = new Label("Codice Evento");

		
		Button yesButton = new Button ("Conferma");
		yesButton.setOnAction(e -> {
			
			//nell'alert box aggiungere il titolo dell'idBox
			//aggiungere una confirm box per sicurezza
			//assegnare l'id alla variabile idRestituito
			
		});
		
		Button noButton = new Button ("Annulla");
		noButton.setOnAction(e -> {
			
			window.close();
			
		});		
		
		HBox top = new HBox(10);
		top.setAlignment(Pos.CENTER);
		top.getChildren().addAll(label);
		
		VBox center = new VBox(10);
		center.setAlignment(Pos.CENTER_LEFT);
		center.getChildren().addAll(codicel, codice);
		
		HBox bot = new HBox(10);
		bot.setAlignment(Pos.CENTER);
		bot.getChildren().addAll(yesButton, noButton);
		
		BorderPane layout = new BorderPane();
		layout.setTop(top);
		layout.setCenter(center);
		layout.setBottom(bot);
		
		
		Scene scene= new Scene(layout, 300, 200);
		window.setScene(scene);
		window.showAndWait();
		
		return idRestituito;
		
	}
}