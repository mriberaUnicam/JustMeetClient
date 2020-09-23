package views;

import classes.Event;
import classes.User;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import utils.DatabaseConnection;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TabellaPrincipale{
	private final static int dataSize = 10_023;
	private final static int rowsPerPage = 1000;

	private TableView<Event> table;
	private List<Event> data;
	private User logged;
	private int type;

	private TableView<Event> createTable() {

		TableView<Event> table = new TableView<>();

		TableColumn<Event, Integer> column1 = new TableColumn<>("Id");
		column1.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getId()).asObject());
		column1.setPrefWidth(150);

		TableColumn<Event, String> column2 = new TableColumn<>("Title");
		column2.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTitle()));
		column2.setPrefWidth(250);

		TableColumn<Event, String> column3 = new TableColumn<>("Giorno e data");
		column3.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDateTime().toString().replace("T", " ")));
		column3.setPrefWidth(250);

		TableColumn<Event, String> column4 = new TableColumn<>("Partecipanti");
		column4.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNPartecipanti()+"/"+param.getValue().getNPartecipantiMax()));
		column4.setPrefWidth(250);

		table.getColumns().addAll(column1, column2, column3, column4);

		return table;
	}

	private Node createPage(int pageIndex) {

		int fromIndex = pageIndex * rowsPerPage;
		int toIndex = Math.min(fromIndex + rowsPerPage, data.size());
		table.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));
		switch (type) {
			case 1:
				table.setRowFactory(tv -> {
					TableRow<Event> row = new TableRow<>();
					row.setOnMouseClicked(event -> {
						Boolean answer = ConfirmBox.display(row.getItem().getTitle(), "Ti vuoi registrare al seguente evento delle ore "+row.getItem().getDateTime().toString().replace("T", " ")+"\n\nDESCRIZIONE\n\n"+row.getItem().getDescription());
						if(answer){
							logged.userJoinEvent(new DatabaseConnection().getConnection(), row.getItem());
							data = row.getItem().getAvailableEvents(new DatabaseConnection().getConnection(), logged);
							this.refresh(pageIndex);
						}
					});
					return row;
				});
				break;
			case 2:
				table.setRowFactory(tv -> {
					TableRow<Event> row = new TableRow<>();
					row.setOnMouseClicked(event -> {
						Boolean answer = ConfirmBox.display(row.getItem().getTitle(), "Sicuro di voler uscire dal seguente evento?");
						if(answer){
							logged.userLeaveEvent(new DatabaseConnection().getConnection(), row.getItem());
							data = row.getItem().getUserSignedEvent(new DatabaseConnection().getConnection(), logged);
							this.refresh(pageIndex);
						}
					});
					return row;
				});
				break;
			case 3:
				table.setRowFactory(tv -> {
					TableRow<Event> row = new TableRow<>();
					row.setOnMouseClicked(event -> {
						Boolean answer = ConfirmBox.display(row.getItem().getTitle(), "Sicuro di voler ELIMINARE il seguente evento?");
						if(answer){
							row.getItem().deleteEvent(new DatabaseConnection().getConnection(), row.getItem().getId());
							data = row.getItem().getUserCreatedEvents(logged, new DatabaseConnection().getConnection());
							this.refresh(pageIndex);
						}
					});
					return row;
				});
				break;
			case 4:
				table.setRowFactory(tv -> {
					TableRow<Event> row = new TableRow<>();
					row.setOnMouseClicked(event -> {
						Boolean answer = ConfirmBox.display(row.getItem().getTitle(), "Sicuro di voler ELIMINARE il seguente evento?");
						if(answer){
							row.getItem().deleteEvent(new DatabaseConnection().getConnection(), row.getItem().getId());
							data = row.getItem().getAllActiveEvents(new DatabaseConnection().getConnection());
							this.refresh(pageIndex);
						}
					});
					return row;
				});
				break;
			case 5:
				table.setRowFactory(tv -> {
					TableRow<Event> row = new TableRow<>();
					row.setOnMouseClicked(event -> {
						ModificaBox.display(row.getItem());
						data = row.getItem().getUserCreatedEvents(logged, new DatabaseConnection().getConnection());
						this.refresh(pageIndex);
					});
					return row;
				});
				break;
			case 6:
				table.setRowFactory(tv -> {
					TableRow<Event> row = new TableRow<>();
					row.setOnMouseClicked(event -> {
						GiudicaBox.display(logged, row.getItem(), "Votazione", "Inserire il voto che si vuole dare a 0 a 5");
						data = row.getItem().getUserCreatedEvents(logged, new DatabaseConnection().getConnection());
						this.refresh(pageIndex);
					});
					return row;
				});
				break;
		}
		return new BorderPane(table);
	}

	/**
	 * Visualizza la tabella generale con i dati passati
	 *
	 * @param events
	 * @param user
	 * @param type 1 = partecipa, 2 = esci, 3 = Elimina, 4 = Elimina moderatore, 5 = Modifica evento, 6 = Valuta
	 */
	public Node display(ArrayList<Event> events, User user, int type) {
		data = events;
		table = createTable();
		logged = user;
		this.type = type;

		Pagination pagination = new Pagination((data.size() / rowsPerPage + 1), 0);
		pagination.setPageFactory(this::createPage);

		return new BorderPane(pagination);
	}

	public void refresh(int pageIndex){
		int fromIndex = pageIndex * rowsPerPage;
		int toIndex = Math.min(fromIndex + rowsPerPage, data.size());
		table.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));
	}

}
