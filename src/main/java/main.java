import classes.Event;
import classes.User;
import utils.DatabaseConnection;
import utils.TimeThread;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;

public class main {

    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        Scanner in = new Scanner(System.in);
        User user;
        Event evento;
        ArrayList<Event> rs;

        int choise = 0;
        User logged = null;

        Timer timeThread = new Timer();
        timeThread.schedule(new TimeThread(), 0, 60000);

        while (choise != 3) {
            while (logged == null) {
                System.out.println("Benvenuto in JustMeet");
                System.out.println("1 - Login");
                System.out.println("2 - Registrati");
                System.out.println("3 - Esci");
                System.out.print("Scegli: ");

                choise = in.nextInt();

                switch (choise) {
                    case 1:
                        user = new User();
                        System.out.println("Insrisci un username:");
                        in.nextLine();
                        user.setUsername(in.nextLine());
                        System.out.println("Insrisci un password:");
                        user.setPassword(in.nextLine());

                        user = user.getUser(db.getConnection());

                        if (user != null)
                            logged = user;
                        else {
                            System.out.println("Username/Password errati");
                        }
                        break;
                    case 2:
                        user = new User();

                        System.out.println("Insrisci un username:");
                        in.nextLine();
                        user.setUsername(in.nextLine());
                        System.out.println("Insrisci un nome:");
                        user.setName(in.nextLine());
                        System.out.println("Insrisci un cognome:");
                        user.setSurname(in.nextLine());
                        System.out.println("Insrisci una email:");
                        user.setEmail(in.nextLine());
                        System.out.println("Insrisci una password:");
                        user.setPassword(in.nextLine());

                        System.out.println("Registazione completata");
                        logged = user;
                        int id = user.putUser(db.getConnection());
                        if(id == -1){
                            logged = null;
                        }else{
                            user.setId(id);
                        }
                        break;
                    case 3:
                        System.exit(0);
                    default:
                        System.out.println("Opzione errata");
                }
            }
            while (logged != null) {
                System.out.println("Benvenuto in JustMeet " + logged.getUsername());
                System.out.println("1 - Crea evento");
                System.out.println("2 - Partecipa ad evento");
                System.out.println("3 - Esci dall'evento");
                System.out.println("4 - Valuta un evento");
                System.out.println("5 - Modifica evento");
                System.out.println("6 - Elimina evento");
                System.out.println("7 - Effettua il logout");
                if(logged.isMod()){
                    System.out.println("8 - Controllo Moderatore");
                }
                System.out.println("Scegli: ");
                choise = in.nextInt();
                switch (choise) {
                    case 1:
                        System.out.println("Creazione nuovo evento");
                        evento = new Event();
                        in.nextLine();
                        System.out.println("Inserisci nome dell'evento:");
                        evento.setTitle(in.nextLine());
                        System.out.println("Inserisci descrizione dell'evento:");
                        evento.setDescription(in.nextLine());
                        System.out.println("Inserisci una data (Formato: DD/MM/AAAA):");
                        String date = in.nextLine();
                        System.out.println("Inserisci un orario (Formato: HH:MM): ");
                        String hours = in.nextLine();
                        evento.setDateTime(
                                LocalDateTime.of(
                                        LocalDate.of(
                                            Integer.parseInt(date.split("/")[2]),
                                                Integer.parseInt(date.split("/")[1]),
                                                    Integer.parseInt(date.split("/")[0])
                                ),
                                        LocalTime.of(
                                                Integer.parseInt(hours.split(":")[0]),
                                                        Integer.parseInt(hours.split(":")[1])
                                        )));
                        System.out.println("Inserisci il tipo dell'evento:");
                        evento.setType(in.nextLine());
                        System.out.println("Inserisci il numero dei partecipanti:");
                        evento.setNPartecipantiMax(in.nextInt());
                        evento.setCreator(logged);
                        evento.putEvent(db.getConnection());
                        break;
                    case 2:
                        System.out.println("Partecipa ad evento");
                        evento = new Event();

                        rs = evento.getAvailableEvents(db.getConnection(), logged);
                        rs.forEach(event -> {
                            System.out.println("" + event.getId() + " - " + event.getTitle());
                        });
                        if(rs.size() == 0) {
                            System.out.println("Non ci sono eventi disponibili\n");
                            break;
                        }else {
                            System.out.println((rs.size()+2) + " - Annulla\n");
                        }
                        System.out.println("Scegli l'id dell'evento a cui partecipare: ");
                        in.nextLine(); //Clear stdin
                        evento.setId(in.nextInt());
                        if(evento.getId()<(rs.size()+2)) {
                            for (int i = 0; i < rs.size(); i++) {
                                if (evento.getId() == rs.get(i).getId()) {
                                    evento = rs.get(i);
                                    break;
                                }
                            }
                            logged.userJoinEvent(db.getConnection(), evento);
                            System.out.println("Registrazione effettuata");
                        }
                        break;
                    case 3:
                        System.out.println("Esci da un evento");
                        evento = new Event();

                        rs = evento.getUserSignedEvent(db.getConnection(), logged);
                        rs.forEach(event -> {
                            System.out.println("" + event.getId() + " - " + event.getTitle());
                        });
                        if(rs.size() == 0) {
                            System.out.println("Non sei registrato a nessun\n");
                            break;
                        }else {
                            System.out.println((rs.size()+2) + " - Annulla\n");
                        }
                        System.out.println("Scegli l'id dell'evento dal quale uscire: ");
                        in.nextLine(); //Clear stdin
                        evento.setId(in.nextInt());
                        if(evento.getId()<(rs.size()+2)) {
                            for (int i = 0; i < rs.size(); i++) {
                                if (evento.getId() == rs.get(i).getId()) {
                                    evento = rs.get(i);
                                    break;
                                }
                            }
                            logged.userLeaveEvent(db.getConnection(), evento);
                            System.out.println("Uscita effettuata");
                        }
                        break;
                    case 4:
                        System.out.println("Valuta un evento");
                        evento = new Event();
                        int valutazione = 0;

                        rs = evento.getUserPastEvents(db.getConnection(), logged);
                        rs.forEach(event -> {
                            System.out.println("" + event.getId() + " - " + event.getTitle());
                        });
                        if(rs.size() == 0) {
                            System.out.println("Non hai partecipato a nessun evento\n");
                            break;
                        }else {
                            System.out.println((rs.size()+2) + " - Annulla\n");
                        }
                        System.out.println("Scegli l'id dell'evento da valutare: ");
                        in.nextLine(); //Clear stdin
                        evento.setId(in.nextInt());
                        if(evento.getId()<(rs.size()+2)) {
                            for (int i = 0; i < rs.size(); i++) {
                                if (evento.getId() == rs.get(i).getId()) {
                                    evento = rs.get(i);
                                    break;
                                }
                            }
                            while(valutazione<1 || valutazione>5){
                                System.out.println("Inserisci valutazione da 1 a 5: ");
                                valutazione = in.nextInt();
                            }
                            logged.setReview(db.getConnection(), evento, valutazione);
                            System.out.println("Valutazione effettuata - Valutazione finale = " + evento.getReview(db.getConnection()));
                        }
                        break;
                    case 5:
                        System.out.println("Modifica evento");
                        evento = new Event();

                        rs = evento.getUserCreatedEvents(logged, db.getConnection());
                        rs.forEach(event -> {
                            System.out.println("" + event.getId() + " - " + event.getTitle());
                        });
                        if(rs.size() == 0){
                            System.out.println("Non hai eventi creati\n");
                            break;
                        }
                        else {
                            System.out.println((rs.size()+2) + " - Annulla\n");
                        }
                        System.out.println("Scegli l'id dell'evento da modificare: ");
                        in.nextLine(); //Clear stdin
                        evento.setId(in.nextInt());
                        for(int i = 0; i < rs.size(); i++) {
                            if(evento.getId() == rs.get(i).getId()){
                                evento = rs.get(i);
                                break;
                            }
                        }
                        in.nextLine();
                        if(evento.getId()<(rs.size()+2)) {
                            System.out.println("Modifica evento premere invio per lasciare invariato");
                            System.out.println("Inserisci nome dell'evento (attuale = " + evento.getTitle() + ") :");
                            String name = in.nextLine();
                            if (!name.equals(""))
                                evento.setTitle(name);
                            System.out.println("Inserisci descrizione dell'evento (attuale = " + evento.getDescription() + ") :");
                            String description = in.nextLine();
                            if (!description.equals(""))
                                evento.setDescription(description);
                            System.out.println("Inserisci una data (attuale = " + evento.getDateTime().toString() + ") (Formato: DD/MM/AAAA):");
                            date = in.nextLine();
                            System.out.println("Inserisci un orario (attuale = " + evento.getDateTime().toString() + ") (Formato: HH:MM): ");
                            hours = in.nextLine();
                            if (!date.equals("") && !hours.equals("")) {
                                LocalDateTime dateTime = LocalDateTime.of(
                                        LocalDate.of(
                                                Integer.parseInt(date.split("/")[2]),
                                                Integer.parseInt(date.split("/")[1]),
                                                Integer.parseInt(date.split("/")[0])
                                        ),
                                        LocalTime.of(
                                                Integer.parseInt(hours.split(":")[0]),
                                                Integer.parseInt(hours.split(":")[1])
                                        ));

                                evento.setDateTime(dateTime);
                            }
                            System.out.println("Inserisci il tipo dell'evento (attuale = " + evento.getType() + ") :");
                            String type = in.nextLine();
                            if (!type.equals(""))
                                evento.setType(type);
                            System.out.println("Inserisci il numero dei partecipanti massimi (attuale = " + evento.getNPartecipanti() + ") :");
                            int npart = in.nextInt();
                            if (evento.getNPartecipanti() != npart)
                                evento.setNPartecipantiMax(npart);
                            evento.updateEvent(db.getConnection());
                        }
                        break;
                    case 6:
                        System.out.println("Elimina evento");
                        evento = new Event();

                        rs = evento.getUserCreatedEvents(logged, db.getConnection());
                        rs.forEach(event -> {
                            System.out.println("" + event.getId() + " - " + event.getTitle());
                        });
                        System.out.println("Scegli l'id dell'evento da eliminare: ");
                        evento.deleteEvent(db.getConnection(), in.nextInt());
                        break;
                    case 7:
                        logged = null;
                        break;
                    case 8:
                        if(logged.isMod()){
                            System.out.println("Elimina evento");
                            evento = new Event();

                            rs = evento.getAllActiveEvents(db.getConnection());
                            rs.forEach(event -> {
                                System.out.println("" + event.getId() + " - " + event.getTitle());
                            });
                            System.out.println("Scegli l'id dell'evento da eliminare: ");
                            evento.deleteEvent(db.getConnection(), in.nextInt());
                        }else{
                            System.out.println("Opzione errata");
                        }
                        break;
                    default:
                        System.out.println("Opzione errata");
                        break;
                }
            }
        }
    }
}
