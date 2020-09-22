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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.DatabaseConnection;

import javax.xml.crypto.Data;

public class GiudicaBox {

	//static Event nuovoEvento;

	public static int display(User user, Event event, String title, String message) {
		DecimalFormat format = new DecimalFormat( "#.0" );

		int idRestituito = 0;
		
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		
		Label label = new Label();
		label.setText(message);

		TextField voto = new TextField();
		voto.setPromptText("Voto per l'evento da 1 a 4");
		voto.setTextFormatter( new TextFormatter<Object>(c -> {
			String newText = c.getControlNewText();

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
				if(0 <= (Integer.parseInt(c.getText())) && (Integer.parseInt(c.getText()) <= 5))
					if (newText.length() > 1) {
						return null;
					} else {
						return c;
					}
				else
					return null;
			}
		}));
		
		Label votol = new Label("Voto");

		Button yesButton = new Button ("Conferma");
		yesButton.setOnAction(e -> {
			user.setReview(new DatabaseConnection().getConnection(), event, Integer.parseInt(voto.getText()));
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
		center.getChildren().addAll(votol, voto);
		
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