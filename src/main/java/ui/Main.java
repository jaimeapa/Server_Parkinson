package ui;

import POJOS.Patient;
import jdbcs.JDBCManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import ReceiveData.ReceiveStringsViaNetwork;

public class Main {
    public static void main(String args[]) throws IOException{
        //JDBCManager manager = new JDBCManager();
        ServerSocket serverSocket = new ServerSocket(8000);
        Socket socket = serverSocket.accept();
        System.out.println("Socket accepted");
        int message = ReceiveStringsViaNetwork.receiveInt(socket);
        System.out.println(message);
        releaseResources(socket, serverSocket);
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
