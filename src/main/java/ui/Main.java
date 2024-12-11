package ui;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import Encryption.EncryptPassword;
import Pojos.Role;
import Pojos.User;
import Utilities.Utilities;
import jdbcs.JDBCManager;
import jdbcs.JDBCRole;
import jdbcs.JDBCUser;

/**
 * The {@code Main} class is the main entry point of the server.
 * It manages client connections through a socket server, user authentication,
 * and the handling of an administration menu for the server.
 */
public class Main {
    /** The number of active clients connected to the server */
    private static int activeClients = 0;
    /** The server's running state */
    private static boolean running = true;

    /**
     * Main method that starts the server and allows clients to connect.
     * It also handles user authentication in a separate thread.
     *
     * @param args The command-line arguments (not used in this case)
     * @throws IOException If an error occurs while creating the server socket
     */
    public static void main(String[] args) throws IOException {
        JDBCManager manager = new JDBCManager();
        ServerSocket serverSocket = new ServerSocket(8000);

        new Thread(() -> logIn(manager, serverSocket)).start();

        try {
            while (running) {
                Socket socket = serverSocket.accept();
                activeClients++;
                System.out.println("Cliente conectado. Clientes activos: " + activeClients);

                new Thread(() -> {
                    try {
                        handleClient(socket, manager);
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
    /**
     * Handles the connection and interactions with a client.
     * It creates an instance of the {@link UI} class to interact with the client.
     *
     * @param socket The client's socket
     */
    private static void handleClient(Socket socket, JDBCManager manager) {
        try {
            UI ui = new UI(socket, manager);
            ui.run();
        } catch (Exception e) {
            System.err.println("Error manejando el cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error cerrando el socket: " + e.getMessage());
            }
        }
    }

    /**
     * Releases the resources of the {@code ServerSocket} when the server shuts down.
     *
     * @param serverSocket The server socket to release
     */
    private static void releaseResources(ServerSocket serverSocket) {
        try {
            if (serverSocket != null) {

                serverSocket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Handles the login process for an administrator using JDBC and encryption utilities.
     * This method facilitates a continuous login process as long as the application is running.
     * It verifies user credentials against a database and, if successful and the user has the
     * "administrator" role, it invokes the administrative menu.
     * @param manager instance for managing database connections.
     * @param serverSocket instance used for server communication.
     */
    private static void logIn(JDBCManager manager, ServerSocket serverSocket) {
        JDBCRole roleManager = new JDBCRole(manager);
        JDBCUser userManager = new JDBCUser(manager, roleManager);
        Role role = new Role("administrator");
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
                    if (u != null && u.getRole().equals(role)) {
                        menuAdmin(serverSocket);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Displays the server's admin menu, where the admin can shut down the server
     * or see the number of active clients connected.
     */
    private static void menuAdmin(ServerSocket serverSocket) {
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
