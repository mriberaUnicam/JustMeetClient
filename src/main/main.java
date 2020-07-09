package main;

import main.classes.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        List<User> utenti = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        User user;

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

                        for(int i = 0; i < utenti.size(); i++ ){
                            if (utenti.get(i).getUsername().equals(user.getUsername()))
                                if(utenti.get(i).getPassword().equals(user.getPassword())) {
                                user = utenti.get(i);
                            }
                        }

                        if (user.getEmail() != null)
                            logged = user;
                        else {
                            user = null;
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

                        utenti.add(user);
                        System.out.println("Registazione completata");
                        logged = user;
                        break;
                    default:
                        System.out.println("Opzione errata");
                }
            }
            while (logged != null) {
                System.out.println("Benvenuto in JustMeet " + logged.getUsername());
                System.out.println("1 - Crea evento");
                System.out.println("2 - Effettua il logout");
                System.out.print("Scegli: ");
                choise = in.nextInt();
                switch (choise) {
                    case 1:
                        System.out.println("Creazione nuovo evento");
                        break;
                    case 2:
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
