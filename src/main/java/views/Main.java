package views;

import classes.Event;
import classes.User;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import utils.DatabaseConnection;
import utils.TimeThread;

import java.util.Timer;

public class Main extends Application {

	DatabaseConnection db = new DatabaseConnection();

	Button login;
	Button registrazione;
	Button chiudi;
	Button cerca;
	Button crea;
	Button modifica;
	Button elimina;
	Button partecipa;
	Button info;
	Button logout;
	Button ritirati;
	Button vota;
	Button partecipante;
	
	Stage window;
	
	Scene scelta;
	Scene scenaPrin;
	
	static User utenteLoggato; //Varabile per memorizzare l'utente loggato nella sessione in corso

	/**
	 * Questo metodo così chiamato va dentro Application
	 * e va a settare tutti i parametri per l'esecuzione
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
		Timer timeThread = new Timer();
		timeThread.schedule(new TimeThread(), 0, 60000);
	}

	@Override
	public void start(Stage primaryStage) throws Exception{
		
		window = primaryStage; //Ci riferiamo a primaryStage tramite window
		
		Label presentazione = new Label("Benvenuto su JustMeet!");

		//parte principale che va a lavorare come programma vero e proprio
		
		window.setTitle("JustMeet");
		
		window.setOnCloseRequest(e ->{ 
			e.consume();//consuma la funzione prima che possa davvero chiudere comunque il ptogramma
						//cos� verr� lanciato solo il metodo closeProg
			closeProg();//fa in modo che se il programma viene chiuso con la X viene comunque chiamato il metodo di chiusura
		});
		
		//Bottoni e funzionalit� di login
		login = new Button("Login");
		login.setOnAction(e ->{
			window.setScene(this.getLoginScene());
		});

		registrazione= new Button("Registrazione");
		registrazione.setOnAction(e ->{
			window.setScene(this.getSignupScene());
		});

		chiudi = new Button("Chiudi");
		chiudi.setOnAction(e -> closeProg());

		info = new Button("Clicca qui per informazioni");
		info.setOnAction(e ->{
			AlertBox.display("Informazioni", "Tutte le tue informazioni verranno gestite e conosciute solo dagli sviluppatori e proprietari del programma.\n"
			+"In caso di reclami, richiesta informazioni e segnalazioni scrivere una mail all'indirizzo veronica.sentini@studenti.unicam.it, \n oppure all'indirizzo"
			+ " marco.ribera@studenti.unicam.it");
		});

		VBox layoutSce = new VBox(20);
		layoutSce.getChildren().addAll(presentazione, registrazione, login, info, chiudi);
		layoutSce.setAlignment(Pos.CENTER);
		scelta = new Scene(layoutSce, 300, 500);
				
		window.setScene(scelta);
		window.show();
		
		//Creazione di una sezione dove mettere i bottoni in cima al programma
		HBox topMenu = new HBox(10);
		
		VBox centerBox = new VBox();
		centerBox.setAlignment(Pos.CENTER);
		
		cerca = new Button("Cerca");
		cerca.setOnAction(e -> CercaBox.display(centerBox));
		
		crea = new Button("Crea");
		crea.setOnAction(e -> {
			centerBox.getChildren().clear();
			window.hide();
			CreaBox.display(utenteLoggato, centerBox);
			window.show();
		});
		
		ritirati = new Button("Ritirati");
		ritirati.setOnAction(e -> {
			centerBox.getChildren().clear();
			centerBox.getChildren().add(new Label("Ritirati da un evento al quale partecipi"));
			centerBox.getChildren().add(new TabellaPrincipale().display(new Event().getUserSignedEvent(new DatabaseConnection().getConnection(), utenteLoggato),  utenteLoggato, 2));
		});

		modifica = new Button("Modifica");
		modifica.setOnAction(e -> {
			centerBox.getChildren().clear();
			centerBox.getChildren().add(new Label("Modifica un evento da te creato"));
			centerBox.getChildren().add(new TabellaPrincipale().display(new Event().getUserCreatedEvents(utenteLoggato, new DatabaseConnection().getConnection()),  utenteLoggato, 5));
		});
		
		elimina = new Button("Elimina");
		elimina.setOnAction(e ->{
			centerBox.getChildren().clear();
			if(utenteLoggato.isMod()){
				centerBox.getChildren().add(new Label("MODALITA' MODERATORE\nElimina un evento dalla lista"));
				centerBox.getChildren().add(new TabellaPrincipale().display(new Event().getAllActiveEvents(new DatabaseConnection().getConnection()), utenteLoggato, 4));
			} else {
				centerBox.getChildren().add(new Label("Elimina un evento da te creato"));
				centerBox.getChildren().add(new TabellaPrincipale().display(new Event().getUserCreatedEvents(utenteLoggato, new DatabaseConnection().getConnection()), utenteLoggato, 3));
			}
		});
		
		partecipa = new Button("Partecipa");
		partecipa.setOnAction(e -> {
			centerBox.getChildren().clear();
			centerBox.getChildren().add(new Label("Partecipa ad un evento dalla lista"));
			centerBox.getChildren().add(new TabellaPrincipale().display(new Event().getAvailableEvents(new DatabaseConnection().getConnection(), utenteLoggato), utenteLoggato, 1));
		});

		vota = new Button("Vota");
		vota.setOnAction(e -> {
					centerBox.getChildren().clear();
					centerBox.getChildren().add(new Label("Giudica un evento dalla lista"));
					centerBox.getChildren().add(new TabellaPrincipale().display(new Event().getUserPastEvents(new DatabaseConnection().getConnection(), utenteLoggato), utenteLoggato, 6));
				});

		topMenu.getChildren().addAll(cerca, crea, modifica, elimina, partecipa, ritirati, vota);
		
		HBox botMenu = new HBox();

		logout = new Button("Logout");
		logout.setOnAction(e -> {
			utenteLoggato = null;
			window.setScene(scelta);
		});

		botMenu.getChildren().add(logout);
		botMenu.setAlignment(Pos.CENTER_RIGHT);
		
		BorderPane principale = new BorderPane();
		principale.setTop(topMenu);
		principale.setCenter(centerBox);
		principale.setBottom(botMenu);
		
		scenaPrin = new Scene (principale, 1024, 768);
	}
		
	//Metodi per i bottoni presenti nel layout di base	
	private void closeProg() {
		Boolean answer = ConfirmBox.display("Uscita", "Sei sicuro di voler uscire?");
		if (answer) {
			window.close();
		}
	}

	private Scene getLoginScene(){
		Scene accesso;

		Button accedi = new Button("Accedi");
		Button indietro = new Button("Indietro");
		Label opzione = new Label("Benvenuto nella sezione Login");

		HBox layoutLogT = new HBox(10);
		VBox layoutLogC = new VBox(10);
		HBox layoutLogB = new HBox(10);

		Label username1l = new Label("Username");
		TextField username1 = new TextField ();
		username1.setPromptText("Inserire Username");

		Label password1l = new Label("Password");
		PasswordField password1 = new PasswordField();

		layoutLogT.setAlignment(Pos.CENTER);
		layoutLogT.getChildren().addAll(opzione);
		layoutLogC.setAlignment(Pos.CENTER_LEFT);
		layoutLogC.getChildren().addAll(username1l, username1, password1l, password1);
		layoutLogB.setAlignment(Pos.CENTER);
		layoutLogB.getChildren().addAll(accedi, indietro);
		BorderPane layoutLog = new BorderPane();
		layoutLog.setTop(layoutLogT);
		layoutLog.setCenter(layoutLogC);
		layoutLog.setBottom(layoutLogB);
		accesso = new Scene(layoutLog, 300, 250);

		accedi.setOnAction(e -> {
			User utente = new User();
			utente.setUsername(username1.getText());
			utente.setPassword(password1.getText());
			utente = utente.getUser(db.getConnection());

			if (utente != null){
				utenteLoggato = utente;
				window.setScene(scenaPrin);
			}else {
				AlertBox.display("Errore","Username o Password errati");
			}
		});

		indietro.setOnAction(e ->{
			window.setScene(scelta);
		});

		return accesso;
	}

	private Scene getSignupScene(){
		Scene registrare;

		Button indietro = new Button("Indietro");
		Button registra = new Button("Registra");

		HBox layoutLogTR = new HBox(10);
		VBox layoutLogCR = new VBox(10);
		HBox layoutLogBR = new HBox(10);

		Label username2l = new Label("Username");
		TextField username2 = new TextField ();
		username2.setPromptText("Inserire Username");

		Label nomel = new Label("Nome");
		TextField nome = new TextField ();
		nome.setPromptText("Inserire Nome");

		Label cognomel = new Label("Cognome");
		TextField cognome = new TextField ();
		cognome.setPromptText("Inserire Cognome");

		Label emaill = new Label("Email");
		TextField email = new TextField ();
		email.setPromptText("prova@test.it");

		Label password2l = new Label("Password");
		PasswordField password2 = new PasswordField();

		layoutLogTR.setAlignment(Pos.CENTER);
		Label opzione = new Label("Benvenuto nella sezione registrazione");
		layoutLogTR.getChildren().addAll(opzione);
		layoutLogCR.setAlignment(Pos.CENTER_LEFT);
		layoutLogCR.getChildren().addAll(username2l, username2, nomel, nome, cognomel, cognome, emaill, email, password2l, password2);
		layoutLogBR.setAlignment(Pos.CENTER);
		layoutLogBR.getChildren().addAll(registra, indietro);
		BorderPane layoutR = new BorderPane();
		layoutR.setTop(layoutLogTR);
		layoutR.setCenter(layoutLogCR);
		layoutR.setBottom(layoutLogBR);
		registrare = new Scene(layoutR, 300, 500);

		registra.setOnAction(e -> {
			User utente = new User();
			utente.setUsername(username2.getText());
			utente.setName(nome.getText());
			utente.setSurname(cognome.getText());
			utente.setEmail(email.getText());
			utente.setPassword(password2.getText());
			utente.setId(utente.putUser(db.getConnection()));
			utenteLoggato = utente;
			window.setScene(scenaPrin);
		});

		indietro.setOnAction(e ->{
			window.setScene(scelta);
		});

		return registrare;
	}
}