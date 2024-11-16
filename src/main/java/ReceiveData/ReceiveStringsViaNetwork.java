package ReceiveData;

import Pojos.Patient;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiveStringsViaNetwork {

    public static String receiveString(Socket socket, BufferedReader bufferedReader) throws IOException {

        //ServerSocket serverSocket = new ServerSocket(9000);
        //Socket socket = serverSocket.accept();
        System.out.println("Connection client created");
        //BufferedReader bufferedReader = new BufferedReader(
          //      new InputStreamReader(socket.getInputStream()));
        System.out.println("Text Received:\n");
        String line;
        String information = "";
        while ((line = bufferedReader.readLine()) != null) {
            if (line.toLowerCase().contains("stop")) {
                System.out.println("Stopping the server");
                releaseResources(bufferedReader, socket);
                System.exit(0);
            }
            information = information + line + "\n";
            //System.out.println(line);
        }
        return information;
    }

    public static Patient recievePatient(Socket socket, ObjectInputStream objectInputStream){
        //InputStream inputStream = null;
        //ObjectInputStream objectInputStream = null;
        Patient patient = null;

        /*try {
            inputStream = socket.getInputStream();
            System.out.println("Connection from the direction " + socket.getInetAddress());
        } catch (IOException ex) {
            System.out.println("It was not possible to start the server. Fatal error.");
            ex.printStackTrace();
        }*/
        try {
            //objectInputStream = new ObjectInputStream(inputStream);
            /*Object tmp;
            while ((tmp = objectInputStream.readObject()) != null) {
                patient = (Patient) tmp;
                System.out.println(patient.toString());
            }*/
            patient = (Patient) objectInputStream.readObject();
            if (patient != null) {
                System.out.println("Received patient: " + patient.toString());
            }
        } catch (EOFException ex) {
            System.out.println("All data have been correctly read.");
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Unable to read from the client.");
            //Logger.getLogger(ReceiveClientViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        } /*finally {
            releasePatientResources(objectInputStream);
        }*/
        return patient;
    }
    public static int receiveInt(Socket socket, DataInputStream dataInputStream) throws IOException{
        //InputStream inputStream = socket.getInputStream();
        //DataInputStream dataInputStream = new DataInputStream(inputStream);
        int message = dataInputStream.readInt();
        //releaseResources2(dataInputStream);
        return message;
    }

    private static void releaseResources2(DataInputStream dataInputStream){
        try {
            dataInputStream.close();
        } catch (IOException ex) {
            //Logger.getLogger(ReceiveBinaryDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

    }
    private static void releaseResources(BufferedReader bufferedReader,
                                         Socket socket) {
        try {
            bufferedReader.close();
        } catch (IOException ex) {
            Logger.getLogger(ReceiveStringsViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ReceiveStringsViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static void releasePatientResources(ObjectInputStream objectInputStream){
        try {
            objectInputStream.close();
        } catch (IOException ex) {
            //Logger.getLogger(ReceiveClientViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
}