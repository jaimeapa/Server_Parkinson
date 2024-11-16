package ui;

import Pojos.Patient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import ReceiveData.ReceiveStringsViaNetwork;
import jdbcs.JDBCManager;

public class Main {
    public static void main(String args[]) throws IOException, EOFException{
        JDBCManager manager = new JDBCManager();
        ServerSocket serverSocket = new ServerSocket(8000);
        Socket socket = serverSocket.accept();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

        System.out.println("Socket accepted");

        int message = ReceiveStringsViaNetwork.receiveInt(socket, dataInputStream);
        System.out.println(message);
        Patient patient = ReceiveStringsViaNetwork.recievePatient(socket, objectInputStream);
        //System.out.println((patient.toString()));
        releaseResources(socket, serverSocket, dataInputStream, objectInputStream);
    }

    private static void releaseResources(Socket socket, ServerSocket serverSocket, DataInputStream dataInputStream, ObjectInputStream objectInputStream){
        try {
            objectInputStream.close();
        } catch (IOException ex) {
            //Logger.getLogger(ReceiveClientViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        try {
            dataInputStream.close();
        } catch (IOException ex) {
            //Logger.getLogger(ReceiveBinaryDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
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
