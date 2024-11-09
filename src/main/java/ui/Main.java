package ui;

import jdbcs.JDBCManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import ReceiveData.ReceiveStringsViaNetwork;

public class Main {
    public static void main(String args[]) throws IOException{
        JDBCManager manager = new JDBCManager();
        ServerSocket serverSocket = new ServerSocket(8080);



    }
}
