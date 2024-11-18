package ui;

import Pojos.Patient;
import ReceiveData.ReceiveDataViaNetwork;

import java.io.*;
import java.net.Socket;

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
        releaseResources(dataInputStream, objectInputStream);
    }

    private static void patientMenu() throws IOException
    {
        int option = ReceiveDataViaNetwork.receiveInt(socket, dataInputStream);
        //System.out.println(option);
        while(true){
            switch (option) {
                case 1 : {
                    Patient patient = ReceiveDataViaNetwork.recievePatient(socket, objectInputStream);
                    break;
                }
                case 2 :{

                    break;
                }
                case 3 :{

                }
                default:{
                    System.out.println("That number is not an option, try again");
                    break;
                }
            }
        }
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
