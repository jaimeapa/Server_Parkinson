package ui;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicInteger;

import jdbcs.JDBCManager;

public class Main {
    private static ServerSocket serverSocket;
    private static Socket socket;
    private static BufferedReader bufferedReader;
    private static DataInputStream dataInputStream;
    private static ObjectInputStream objectInputStream;
    private static AtomicInteger activeClients = new AtomicInteger(0);
    private static boolean running = true;


    public static void main(String args[]) throws IOException, EOFException{
        JDBCManager manager = new JDBCManager();
        serverSocket = new ServerSocket(8000);

        /*try {
            while (true) {
                socket = serverSocket.accept();
                new Thread(new UserMenu(socket, manager)).start();
            }
        }finally {
            releaseResources(socket, serverSocket);
        }*/
        new Thread(() -> menuAdmin()).start();
        try {
            while (running) {
                socket = serverSocket.accept(); // Acepta conexiones
                activeClients.incrementAndGet(); // Incrementa el contador de clientes
                System.out.println("Cliente conectado. Clientes activos: " + activeClients.get());

                // Maneja al cliente en un hilo separado
                new Thread(() -> {
                    try {
                        new UserMenu(socket, manager).run(); // Simula la gestión del cliente
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // Reduce el contador al desconectarse el cliente
                        activeClients.decrementAndGet();
                        System.out.println("Cliente desconectado. Clientes activos: " + activeClients.get());
                    }
                }).start();
            }
        } finally {
            releaseResources(socket, serverSocket);
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
                    while (activeClients.get() > 0) {
                        System.out.println("Esperando desconexión de clientes activos: " + activeClients.get());
                        Thread.sleep(2000); // Espera 2 segundos antes de volver a comprobar
                    }
                    System.out.println("No hay clientes conectados. Apagando servidor...");
                    running = false;
                    serverSocket.close();
                    break;
                } else if (opcion == 2) {
                    System.out.println("Clientes activos actualmente: " + activeClients.get());
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
            socket.close();
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
