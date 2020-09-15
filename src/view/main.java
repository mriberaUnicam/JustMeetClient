package view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class main extends Application{

    //window.setScene

    Button login;
    Button signup;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JustMeet");
        login = new Button();
        login.setText("Login");
        login.setOnAction(e -> {System.out.println("Login");});

        signup = new Button();
        signup.setText("Signup");
        signup.setOnAction(e -> {System.out.println("Signup");});

        VBox layout = new VBox(20);
        layout.getChildren().add(login);
        layout.getChildren().add(signup);

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}