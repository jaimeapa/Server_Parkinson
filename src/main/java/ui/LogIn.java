package ui;

import java.io.IOException;
import java.io.IOException;
import java.io.IOException;
import POJOS.User;
import UserManager.JPAUserManager;

public class LogIn {
    public LogIn() {
    }

    public static void main(String[] args) throws NumberFormatException, IOException {
        JPAUserManager userMan = new JPAUserManager();

        while(true) {
            System.out.println("\nLog-in menu: ");
            System.out.println(" 1. Access as a client");
            System.out.println(" 2. Access as the server");
            System.out.println(" 0. Exit program");
            int option = Utilities.readInteger("Option: ");
            switch (option) {
                case 0:
                    System.out.println("Program terminated.");
                    return;
                case 1:
                    ClientMenu.clientMenu();
                    break;
                case 2:
                    System.out.println("\nIntroduce:");
                    String username = Utilities.readString(" -Username: ");
                    String password = Utilities.readString(" -Password: ");
                    User user = JPAUserManager.logIn(username, password);
                    if (user != null) {
                        if (user.getRole().getName().equals("server")) {
                            ServerMenu.serverMenu(userMan, user);
                        } else {
                            System.out.println(" ERROR: Wrong username or password.");
                        }
                    }
                    break;
                default:
                    System.out.println(" ERROR: This option is not valid.");
            }
        }
    }
}
