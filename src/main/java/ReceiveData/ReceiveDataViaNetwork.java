package ReceiveData;

import Pojos.*;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiveDataViaNetwork {
    private static DataInputStream dataInputStream;

    public ReceiveDataViaNetwork(Socket socket){
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
        }catch (IOException e){

        }
    }

    public static String receiveString(Socket socket) throws IOException {
        //DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        String information = "";
        information = dataInputStream.readUTF();
        //releaseResources(dataInputStream);
        return information;
    }

    public static Doctor receiveDoctor(Socket socket){
        //InputStream inputStream = null;
        //ObjectInputStream objectInputStream = null;
        Doctor doctor = null;

        try {
            //DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            int id = dataInputStream.readInt();
            String name = dataInputStream.readUTF();
            String surname = dataInputStream.readUTF();
            String date = dataInputStream.readUTF();
            String email = dataInputStream.readUTF();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dob = LocalDate.parse(date, formatter);
            doctor = new Doctor(id,name,surname,dob,email);
            //releaseResources(dataInputStream);
            //patient = (Patient) objectInputStream.readObject();
        } catch (EOFException ex) {
            System.out.println("All data have been correctly read.");
        } catch (IOException  ex) {
            System.out.println("Unable to read from the client.");
            ex.printStackTrace();
            //Logger.getLogger(ReceiveClientViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }

        return doctor;
    }

    public static Patient recievePatient(Socket socket){
        //InputStream inputStream = null;
        //ObjectInputStream objectInputStream = null;
        Patient patient = null;

        try {
            //DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            int id = dataInputStream.readInt();
            String name = dataInputStream.readUTF();
            String surname = dataInputStream.readUTF();
            String date = dataInputStream.readUTF();
            String email = dataInputStream.readUTF();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dob = LocalDate.parse(date, formatter);
            patient = new Patient(id,name,surname,dob,email);
            //releaseResources(dataInputStream);
            //patient = (Patient) objectInputStream.readObject();
        } catch (EOFException ex) {
            System.out.println("All data have been correctly read.");
        } catch (IOException  ex) {
            System.out.println("Unable to read from the client.");
            ex.printStackTrace();
            //Logger.getLogger(ReceiveClientViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }

        return patient;
    }

    public static Interpretation recieveInterpretation(Socket socket){
        //InputStream inputStream = null;
        //ObjectInputStream objectInputStream = null;
        Interpretation interpretation = null;


        try {
            //Object tmp;
            //DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String stringDate = dataInputStream.readUTF();
            int doctor_id = dataInputStream.readInt();
            String stringEMG = dataInputStream.readUTF();
            int patient_id = dataInputStream.readInt();
            String stringEDA = dataInputStream.readUTF();
            String observation = dataInputStream.readUTF();
            String interpretation1 = dataInputStream.readUTF();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(stringDate, formatter);
            Signal signalEMG = new Signal(Signal.SignalType.EMG);
            signalEMG.setValuesEMG(stringEMG);
            Signal signalEDA = new Signal(Signal.SignalType.EDA);
            signalEDA.setValuesEDA(stringEDA);
            interpretation = new Interpretation(date, interpretation1, signalEMG, signalEDA, patient_id, doctor_id, observation);
            //releaseResources(dataInputStream);
            //patient = (Patient) objectInputStream.readObject();
        } catch (EOFException ex) {
            System.out.println("All data have been correctly read.");
        } catch (IOException  ex) {
            System.out.println("Unable to read from the client.");
            ex.printStackTrace();
            //Logger.getLogger(ReceiveClientViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }

        return interpretation;
    }

    public static int receiveInt(Socket socket) throws IOException{
        //InputStream inputStream = socket.getInputStream();
        //DataInputStream dataInputStream = new DataInputStream(inputStream);
        int message = 10;
        try{
            //DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            message = dataInputStream.readInt();
            //releaseResources(dataInputStream);
        }catch(IOException ex){
            ex.printStackTrace();
        }

        return message;
    }

    public static User recieveUser(Socket socket)
    {
        User u = null;
        try{
            //DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String email = dataInputStream.readUTF();
            byte[] psw = dataInputStream.readUTF().getBytes();
            String role = dataInputStream.readUTF();
            Role r = new Role(role);
            u = new User(email,psw,r);
            //releaseResources(dataInputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
        return u;
    }
    /*public static void recieveValues(Patient patient, DataInputStream dataInputStream) throws IOException{
        LinkedList<Integer> values = new LinkedList<>();
        String signalType = dataInputStream.readUTF();
        if(signalType.equals("EMG")) {
            Integer value = 0;
            do {
                value = dataInputStream.readInt();
                if (value != -1) {
                    values.add(value);
                }
            } while (value != -1);
            patient.setValues_EMG(values);
        }else {
            if (signalType.equals("EDA")) {
                Integer value = 0;
                do {
                    value = dataInputStream.readInt();
                    if (value != -1) {
                        values.add(value);
                    }
                } while (value != -1);
                patient.setValues_EDA(values);
            }
        }
    }*/


    public static void releaseResources(){
        try {
            dataInputStream.close();
        } catch (IOException ex) {
            //Logger.getLogger(SendBinaryDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

}