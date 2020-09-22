package views;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import classes.Event;
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

import javax.xml.crypto.Data;
import javax.xml.soap.Text;

public class ModificaBox {

	public static void display(Event evento) {
		DecimalFormat format = new DecimalFormat( "#.0" );

		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Modifica");
		
		Label label = new Label();
		label.setText("Inserire informazioni modifica evento");

		TextField titolo = new TextField ();
		titolo.setText(evento.getTitle());
		TextArea descrizione = new TextArea();
		descrizione.setText(evento.getDescription());
		DateTimePicker dateTimePicker = new DateTimePicker();
		dateTimePicker.setDateTimeValue(evento.getDateTime());
		TextField tipo = new TextField ();
		tipo.setText(evento.getType());
		TextField partecipanti = new TextField ();
		partecipanti.setText(String.valueOf(evento.getNPartecipantiMax()));
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
		Label datal = new Label("Data");
		Label partecipantil = new Label("Numero partecipanti");
		
		Button yesButton = new Button ("Modifica");
		yesButton.setOnAction(e -> {
			evento.setTitle(titolo.getText());
			evento.setDescription(descrizione.getText());
			evento.setType(tipo.getText());
			evento.setDateTime(dateTimePicker.getDateTimeValue());
			evento.setNPartecipantiMax(Integer.parseInt(partecipanti.getText()));
			evento.updateEvent(new DatabaseConnection().getConnection());
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
		center.getChildren().addAll(titolol, titolo, descrizionel, descrizione, tipol, tipo, datal, dateTimePicker, partecipantil, partecipanti);
		
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