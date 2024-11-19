package ReceiveData;

import Pojos.Patient;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiveDataViaNetwork {

    public static String receiveString(Socket socket, BufferedReader bufferedReader) throws IOException {

        String line = "";
        String information = "";
        information = bufferedReader.readLine();
        System.out.println(information);
        return information;
    }

    public static Patient recievePatient(Socket socket, DataInputStream dataInputStream){
        //InputStream inputStream = null;
        //ObjectInputStream objectInputStream = null;
        Patient patient = null;

        try {
            //Object tmp;
            String name = dataInputStream.readUTF();
            String surname = dataInputStream.readUTF();
            String date = dataInputStream.readUTF();
            String email = dataInputStream.readUTF();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dob = LocalDate.parse(date, formatter);
            patient = new Patient(name,surname,dob,email);

            //patient = (Patient) objectInputStream.readObject();
        } catch (EOFException ex) {
            System.out.println("All data have been correctly read.");
        } catch (IOException  ex) {
            System.out.println("Unable to read from the client.");
            ex.printStackTrace();
            //Logger.getLogger(ReceiveClientViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(patient != null){
            System.out.println(patient.toString()   );
        }
        return patient;
    }
    public static int receiveInt(Socket socket, DataInputStream dataInputStream) throws IOException{
        //InputStream inputStream = socket.getInputStream();
        //DataInputStream dataInputStream = new DataInputStream(inputStream);
        int message = 10;
        try{
            message = dataInputStream.readInt();
        }catch(IOException ex){
            ex.printStackTrace();
        }

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
    private static void releaseResources(BufferedReader bufferedReader) {
        try {
            bufferedReader.close();
        } catch (IOException ex) {
            Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
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