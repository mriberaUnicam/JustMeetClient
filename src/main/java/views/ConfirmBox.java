package views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

	static boolean answer;

	public static boolean display(String title, String message) {
		
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);
		
		Label label = new Label();
		label.setText(message);
		
		Button yesButton = new Button ("Ok");
		yesButton.setOnAction(e -> {
			answer = true;
			window.close();
			
		});
		
		Button noButton = new Button ("Annulla");
		noButton.setOnAction(e -> {
			answer = false;
			window.close();
			
		});		
		
		HBox top = new HBox(10);
		top.setAlignment(Pos.CENTER);
		top.getChildren().addAll(label);
		
		HBox center = new HBox(10);
		center.setAlignment(Pos.CENTER);
		center.getChildren().addAll(yesButton, noButton);
		
		BorderPane layout = new BorderPane();
		layout.setTop(top);
		layout.setCenter(center);

		Scene scene= new Scene(layout);
		window.setScene(scene);
		window.showAndWait();

		return answer;
	}
	
}
