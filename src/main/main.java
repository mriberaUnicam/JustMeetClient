package main;

import main.classes.Event;
import main.classes.User;
import main.utils.DatabaseConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        Scanner in = new Scanner(System.in);
        User user;
        Event evento;

        int choise = 0;
        User logged = null;

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
                System.out.println("2 - Modifica evento");
                System.out.println("3 - Elimina evento");
                System.out.println("4 - Effettua il logout");
                System.out.print("Scegli: ");
                choise = in.nextInt();
                switch (choise) {
                    case 1:
                        System.out.println("Creazione nuovo evento");
                        evento = new Event();
                        System.out.println("Inserisci nome dell'evento:");
                        evento.setTitle(in.nextLine());
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
                        evento.setType(1);
                        System.out.println("Inserisci il numero dei partecipanti:");
                        evento.setNPartecipanti(in.nextInt());
                        evento.setCreator(logged);
                        evento.putEvent(db.getConnection());
                        break;
                    case 2:
                        System.out.println("Modifica evento");
                        evento = new Event();

                        ArrayList<Event> rs = evento.getUserCreatedEvents(logged, db.getConnection());
                        rs.forEach(event -> {
                            System.out.println("" + event.getId() + " - " + event.getTitle());
                        });
                        if(rs.size() == 0){
                            System.out.println("Non hai eventi creati\n");
                            break;
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
                        System.out.println("Modifica evento premere invio per lasciare invariato");
                        System.out.println("Inserisci nome dell'evento (attuale = "+evento.getTitle()+") :");
                        String name = in.nextLine();
                        if(evento.getTitle().equals(name));
                            evento.setTitle(name);
                        System.out.println("Inserisci una data (attuale = "+evento.getDateTime().toString()+") (Formato: DD/MM/AAAA):");
                        date = in.nextLine();
                        System.out.println("Inserisci un orario (attuale = "+evento.getDateTime().toString()+") (Formato: HH:MM): ");
                        hours = in.nextLine();
                            if(!date.equals("") && !hours.equals("")) {
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
                        System.out.println("Inserisci il tipo dell'evento (attuale = "+evento.getType()+") :");
                        if(evento.getType()!=1)
                            evento.setType(1);
                        System.out.println("Inserisci il numero dei partecipanti (attuale = "+evento.getNPartecipanti()+") :");
                        int npart = in.nextInt();
                        if(evento.getNPartecipanti() != npart)
                            evento.setNPartecipanti(npart);
                        evento.updateEvent(db.getConnection());
                        break;
                    case 3:
                        System.out.println("Elimina evento");
                        evento = new Event();

                        rs = evento.getUserCreatedEvents(logged, db.getConnection());
                        rs.forEach(event -> {
                            System.out.println("" + event.getId() + " - " + event.getTitle());
                        });
                        System.out.println("Scegli l'id dell'evento da eliminare: ");
                        evento.deleteEvent(db.getConnection(), in.nextInt());
                        break;
                    case 4:
                        logged = null;
                        break;
                    default:
                        System.out.println("Opzione errata");
                        break;
                }
            }
        }
    }
}
