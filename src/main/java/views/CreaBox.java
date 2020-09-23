package views;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import classes.Event;
import classes.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tornadofx.control.DateTimePicker;
import utils.DatabaseConnection;

public class CreaBox {

	public static void display(User logged, VBox Centrale) {

		DecimalFormat format = new DecimalFormat( "#.0" );

		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Crea");
		
		Label label = new Label();
		label.setText("Inserisci le informazioni del nuovo evento");

		TextField titolo = new TextField ();
		titolo.setPromptText("Inserire titolo");

		TextArea descrizione = new TextArea ();
		descrizione.setPromptText("Inserire descrizione");

		DateTimePicker dateTimePicker = new DateTimePicker();

		ComboBox tipo = new ComboBox ();
		tipo.getItems().addAll(new Event().getEventTypes(new DatabaseConnection().getConnection()));

		TextField partecipanti = new TextField ();
		partecipanti.setPromptText("Inserire numero partecipanti");
		partecipanti.setTextFormatter( new TextFormatter<Object>(c -> {
			if ( c.getControlNewText().isEmpty() )
			{
				return c;
			}

			ParsePosition parsePosition = new ParsePosition( 0 );
			Object object = format.parse( c.getControlNewText(), parsePosition );

			if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() )
			{
				return null;
			}
			else
			{
				return c;
			}
		}));

		Label titolol = new Label("Titolo");
		Label descrizionel = new Label("Descrizione");
		Label tipol = new Label("Tipo");
		Label datetimel = new Label("Data e Ora");
		Label partecipantil = new Label("Numero partecipanti");

		Button yesButton = new Button ("Crea");
		yesButton.setOnAction(e -> {
			if(dateTimePicker.getDateTimeValue().compareTo(LocalDateTime.now()) > 0){
				Event evento = new Event();
				evento.setTitle(titolo.getText());
				evento.setType(tipo.getSelectionModel().getSelectedItem().toString());
				evento.setDescription(descrizione.getText());
				evento.setDateTime(dateTimePicker.getDateTimeValue());
				evento.setNPartecipantiMax(Integer.parseInt(partecipanti.getText()));
				evento.setCreator(logged);
				evento.putEvent(new DatabaseConnection().getConnection());
				window.close();
			}else{
				ConfirmBox.display("Impossibile creare evento", "Impossibile impostare una data che sia precedente alla data odierna");
			}
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
		center.getChildren().addAll(titolol, titolo, descrizionel, descrizione, datetimel, dateTimePicker, tipol, tipo, partecipantil, partecipanti);
		
		HBox bot = new HBox(10);
		bot.setAlignment(Pos.CENTER);
		bot.getChildren().addAll(yesButton, noButton);
		
		BorderPane layout = new BorderPane();
		layout.setTop(top);
		layout.setCenter(center);
		layout.setBottom(bot);

		Scene scene = new Scene(layout, 300, 600);
		window.setScene(scene);
		window.showAndWait();
	}
	
}
