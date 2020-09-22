package views;

import classes.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tornadofx.control.DateTimePicker;
import utils.DatabaseConnection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CercaBox {

	public static void display(VBox Centrale) {
		
		//IL VBOX CENTRALE è IL BOX DELLA PAGINA CENTRALE LA QUALE DEVE MOSTRARE I RISULTATI
		
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Cerca");
		
		Label label = new Label();
		label.setText("Inserire dati eventi da cercare");

		TextField titolo = new TextField ();
		titolo.setPromptText("Inserire titolo");

		DateTimePicker dateTimePicker = new DateTimePicker();
		dateTimePicker.setDateTimeValue(LocalDateTime.now());

		ComboBox tipo = new ComboBox();
		tipo.getItems().add("");
		tipo.getItems().addAll(new Event().getEventTypes(new DatabaseConnection().getConnection()));

		TextField partecipanti = new TextField ();
		partecipanti.setPromptText("Inserire numero partecipanti");

		Label titolol = new Label("Titolo");
		Label tipol = new Label("Tipo");
		Label datel = new Label("Data");
		
		Button yesButton = new Button ("Cerca");
		yesButton.setOnAction(e -> {
			Event evento = new Event();
			evento.setTitle(titolo.getText());
			if(tipo.getValue()!=null) {
				evento.setType(tipo.getValue().toString());
			}else{
				evento.setType("");
			}
			evento.setDateTime(dateTimePicker.getDateTimeValue());
			Centrale.getChildren().clear();
			Centrale.getChildren().add(new Label("Eventi trovati con i parametri inseriti"));
			Centrale.getChildren().add(new TabellaPrincipale().display(evento.findEvent(new DatabaseConnection().getConnection()), null, 1));
			window.close();
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
		center.getChildren().addAll(titolol, titolo, tipol, tipo, datel, dateTimePicker);
		
		HBox bot = new HBox(10);
		bot.setAlignment(Pos.CENTER);
		bot.getChildren().addAll(yesButton, noButton);
		
		BorderPane layout = new BorderPane();
		layout.setTop(top);
		layout.setCenter(center);
		layout.setBottom(bot);
		
		Scene scene= new Scene(layout, 300, 600);
		window.setScene(scene);
		window.showAndWait();
	}
}