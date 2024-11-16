package ui;

import Pojos.Patient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import ReceiveData.ReceiveStringsViaNetwork;
import jdbcs.JDBCManager;
import jdbcs.JDBCPatient;

public class Main {
    private static ServerSocket serverSocket;
    private static Socket socket;
    private static BufferedReader bufferedReader;
    private static DataInputStream dataInputStream;
    private static ObjectInputStream objectInputStream;


    public static void main(String args[]) throws IOException, EOFException{
        JDBCManager manager = new JDBCManager();
        serverSocket = new ServerSocket(8000);
        //socket = serverSocket.accept();
        /*Thread userMenuThread = new Thread(new UserMenu(socket));
        userMenuThread.start();

        // Wait for the UserMenu thread to complete before closing resources
        try {
            userMenuThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        try {
            while (true) {
                socket = serverSocket.accept();
                new Thread(new UserMenu(socket, manager)).start();
            }
        }finally {
            releaseResources(socket, serverSocket);
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
