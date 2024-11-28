package ui;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicInteger;

import Pojos.User;
import Utilities.Utilities;
import jdbcs.JDBCManager;
import jdbcs.JDBCRole;
import jdbcs.JDBCUser;

public class Main {
    private static ServerSocket serverSocket;
    private static Socket socket;
    private static JDBCManager manager;
    private static BufferedReader bufferedReader;
    private static DataInputStream dataInputStream;
    private static ObjectInputStream objectInputStream;
    private static int activeClients = 0;
    private static boolean running = true;


    public static void main(String args[]) throws IOException, EOFException{
        manager = new JDBCManager();
        serverSocket = new ServerSocket(8000);
        running = true;
        /*try {
            while (true) {
                socket = serverSocket.accept();
                new Thread(new UserMenu(socket, manager)).start();
            }
        }finally {
            releaseResources(socket, serverSocket);
        }*/
        new Thread(() -> logIn()).start();
        try {
            while (running) {
                socket = serverSocket.accept(); // Acepta conexiones
                activeClients++;// Incrementa el contador de clientes
                System.out.println("Cliente conectado. Clientes activos: " + activeClients);

                // Maneja al cliente en un hilo separado
                new Thread(() -> {
                    try {
                        new UserMenu(socket, manager).run(); // Simula la gestión del cliente
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // Reduce el contador al desconectarse el cliente
                        activeClients--;
                        System.out.println("Cliente desconectado. Clientes activos: " + activeClients);
                    }
                }).start();
            }
        } finally {
            releaseResources(socket, serverSocket);
            System.exit(0);
        }
    }

    private static void logIn(){
        JDBCRole role = new JDBCRole(manager);
        JDBCUser userManager = new JDBCUser(manager, role);
        User u = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while(running){
                System.out.println("\n\n      LOG IN\n");
                System.out.println("email: ");
                String email;
                do {
                    email = reader.readLine();
                }while(!Utilities.checkEmail(email));
                System.out.println("password: ");
                String password = reader.readLine();
                u = userManager.checkPassword(email,password);
                if(u!=null){
                    menuAdmin();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private static void menuAdmin(){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (running) {
                System.out.println("=== MENÚ DEL SERVIDOR ===");
                System.out.println("1. Apagar el servidor");
                System.out.println("2. Ver clientes conectados");
                System.out.print("Seleccione una opción: \n");

                String input = reader.readLine(); // Lee la entrada del usuario
                int opcion;
                try {
                    opcion = Integer.parseInt(input); // Convierte la entrada en un número
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, ingrese un número válido.");
                    continue;
                }

                if (opcion == 1) {
                    System.out.println("Apagando servidor. Esperando a que no haya clientes conectados...");
                    while (activeClients > 0) {
                        System.out.println("Esperando desconexión de clientes activos: " + activeClients);
                        Thread.sleep(2000); // Espera 2 segundos antes de volver a comprobar
                    }
                    System.out.println("No hay clientes conectados. Apagando servidor...");
                    running = false;
                    releaseResources(socket, serverSocket);
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

    private static void releaseResources(Socket socket, ServerSocket serverSocket){

        try {
            if(socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            //Logger.getLogger(ReceiveBinaryDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        try {
            serverSocket.close();
        } catch (IOException ex) {
            //Logger.getLogger(ReceiveBinaryDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
}
