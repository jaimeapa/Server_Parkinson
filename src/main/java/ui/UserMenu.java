package ui;

import Pojos.Patient;
import ReceiveData.ReceiveStringsViaNetwork;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import jdbcs.JDBCManager;
import jdbcs.JDBCPatient;

public class UserMenu implements Runnable{
    private static Socket socket;
    private static BufferedReader bufferedReader;
    private static DataInputStream dataInputStream;
    private static ObjectInputStream objectInputStream;
    private static JDBCManager manager;
    private static JDBCPatient patientManager;

    public UserMenu(Socket socket, JDBCManager manager){
        this.socket = socket;
        this.manager = manager;
        this.patientManager = new JDBCPatient(manager);
    }

    @Override
    public void run() {
        try{
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //InputStream inputStream = socket.getInputStream();
            dataInputStream = new DataInputStream(socket.getInputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            System.out.println("Socket accepted");

            int message = ReceiveStringsViaNetwork.receiveInt(socket, dataInputStream);
            if(message == 1){
                String option = ReceiveStringsViaNetwork.receiveString(socket, bufferedReader);
                if(option.equalsIgnoreCase("REGISTER")){
                    Patient patient = ReceiveStringsViaNetwork.recievePatient(socket, objectInputStream);

                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        releaseResources(dataInputStream, objectInputStream);
    }
    private static void releaseResources(DataInputStream dataInputStream, ObjectInputStream objectInputStream){
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


    }
}
