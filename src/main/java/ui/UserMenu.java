package ui;

import Pojos.Patient;
import ReceiveData.ReceiveDataViaNetwork;

import java.io.*;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.logging.Level;
import java.util.logging.Logger;

import ReceiveData.SendDataViaNetwork;
import jdbcs.JDBCManager;
import jdbcs.JDBCPatient;

public class UserMenu implements Runnable{
    private static Socket socket;
    private static BufferedReader bufferedReader;
    private static DataInputStream dataInputStream;
    private static ObjectInputStream objectInputStream;
    private static DataOutputStream dataOutputStream;
    private static ObjectOutputStream objectOutputStream;
    private static PrintWriter printWriter;
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
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            printWriter = new PrintWriter(socket.getOutputStream(), true);

            String option;

            System.out.println("Socket accepted");

            int message = ReceiveDataViaNetwork.receiveInt(socket, dataInputStream);
            if(message == 1){
                /*option = ReceiveStringsViaNetwork.receiveString(socket, bufferedReader);
                //System.out.println(option);
                if(option.equals("REGISTER")){
                    Patient patient = ReceiveStringsViaNetwork.recievePatient(socket, objectInputStream);
                    //System.out.println(patient.toString());
                }*/
                patientMenu();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        releaseResources(dataOutputStream, objectOutputStream, dataInputStream, objectInputStream, bufferedReader, printWriter);
    }

    private static void patientMenu() throws IOException
    {

        boolean menu = true;

        while(menu){
            int option = ReceiveDataViaNetwork.receiveInt(socket, dataInputStream);
            System.out.println("patient menu: " + option);
            switch (option) {
                case 1 : {
                    Patient patient = ReceiveDataViaNetwork.recievePatient(socket, dataInputStream);
                    clientMenu(patient);
                    break;
                }
                case 2 :{

                    break;
                }
                case 3 :{
                    menu=false;
                }
                default:{
                    System.out.println("That number is not an option, try again");
                    break;
                }
            }
        }
    }

    public static void clientMenu(Patient patient_logedIn) throws IOException {

        int option;
        boolean menu = true;
        while(menu){
            option = ReceiveDataViaNetwork.receiveInt(socket, dataInputStream);
            System.out.println("client menu: " + option);
            switch(option){
                case 1:{
                    SendDataViaNetwork.sendStrings("Type the numbers corresponding to the symptoms you have (To stop adding symptoms type '0'): ", printWriter);
                    System.out.println("Lol");
                    break;
                }
                case 2:{
                    break;
                }
                case 3:{
                    /*switch(bitalinoMenu())
                    {
                        case 1:
                        {
                            break;
                        }
                        case 2:
                        {
                            break;
                        }
                    }
                    break;*/
                }
                case 4:{
                    menu = false;
                    //System.exit(0);
                    break;
                }

            }

        }
    }

    private static void releaseResources(DataOutputStream dataOutputStream, ObjectOutputStream objectOutputStream, DataInputStream dataInputStream, ObjectInputStream objectInputStream, BufferedReader bufferedReader, PrintWriter printWriter){
        try {
            objectOutputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            dataOutputStream.close();
        } catch (IOException ex) {
            //Logger.getLogger(SendBinaryDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
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
            bufferedReader.close();
        } catch (IOException ex) {
            //Logger.getLogger(ReceiveStringsViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
        printWriter.close();
    }
}
