package ui;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import Encryption.EncryptPassword;
import Pojos.User;
import Utilities.Utilities;
import jdbcs.JDBCManager;
import jdbcs.JDBCRole;
import jdbcs.JDBCUser;

public class Main {
    //private static ServerSocket serverSocket;
    private static int activeClients = 0;
    private static JDBCManager manager;
    private static boolean running = true;
    private static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        manager = new JDBCManager();
        serverSocket = new ServerSocket(8000);
        //running = true;
        LinkedList<UserMenu> userMenus = new LinkedList<>();
        // Hilo para la administración del servidor
        new Thread(Main::logIn).start();

        try {
            while (running) {
                Socket socket = serverSocket.accept();
                activeClients++;
                System.out.println("Cliente conectado. Clientes activos: " + activeClients);

                new Thread(() -> {
                    try {
                        handleClient(socket);
                    } catch (Exception e) {

                    } finally {
                        activeClients--;
                    }
                }).start();
            }
        } finally {
            System.out.println("Cerrando el ServerSocket");
            releaseResources(serverSocket);
            System.exit(0);
        }
    }

    private static void handleClient(Socket socket) {
        try {
            UserMenu userMenu = new UserMenu(socket, manager);
            userMenu.run();
        } catch (Exception e) {
            System.err.println("Error manejando el cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error cerrando el socket: " + e.getMessage());
            }
            //activeClients.decrementAndGet();
        }
    }


    private static void releaseResources(ServerSocket serverSocket) {
        try {
            if (serverSocket != null) {

                serverSocket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static int getActiveClients() {
        return activeClients;
    }

    private static void logIn() {
        JDBCRole role = new JDBCRole(manager);
        JDBCUser userManager = new JDBCUser(manager, role);
        //running = true;
        try {
            while (running) {
                System.out.println("\n\n      LOG IN\n");
                String email;
                do {
                    email = Utilities.readString("Email: ");
                } while (!Utilities.checkEmail(email));

                String psw = Utilities.readString("Enter your password: ");
                byte[] password = EncryptPassword.encryptPassword(psw);

                if (password != null) {
                    User u = userManager.checkPassword(email, new String(password));
                    if (u != null) {
                        System.out.println(u.toString());
                        menuAdmin();
                        //running = false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void menuAdmin() {
        //running = true;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (running) {
                System.out.println("=== MENÚ DEL SERVIDOR ===");
                System.out.println("1. Apagar el servidor");
                System.out.println("2. Ver clientes conectados");
                System.out.print("Seleccione una opción: \n");

                String input = reader.readLine();
                int opcion;
                try {
                    opcion = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, ingrese un número válido.");
                    continue;
                }

                if (opcion == 1) {
                    System.out.println("Apagando servidor...");
                    while (activeClients > 0) {
                        System.out.println("Esperando desconexión de clientes activos: " + activeClients);
                        Thread.sleep(2000);
                    }
                    running = false;
                    releaseResources(serverSocket);
                } else if (opcion == 2) {
                    System.out.println("Clientes activos actualmente: " + activeClients);
                } else {
                    System.out.println("Opción no válida. Intente nuevamente.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
