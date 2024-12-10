package ReceiveData;

import Pojos.*;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The `ReceiveDataViaNetwork` class provides methods to receive data from a network socket.
 * It supports receiving different types of data such as strings, integers, and objects like
 * `Doctor`, `Patient`, `Interpretation`, and `User`.
 */
public class ReceiveDataViaNetwork {
    // Fields

    /** Input stream to receive data from the network socket. */
    private DataInputStream dataInputStream;

    // Constructor

    /**
     * Constructs a `ReceiveDataViaNetwork` object with a given network socket.
     *
     * @param socket the network socket to read data from.
     */
    public ReceiveDataViaNetwork(Socket socket){
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
        }catch (IOException ex){
            Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // Methods to Receive Data

    /**
     * Receives a string from the network.
     *
     * @return the received string, or `null` if an error occurs.
     */
    public String receiveString() {
            try {
                return dataInputStream.readUTF();
            } catch (IOException ex) {
                System.err.println("Error recibing String");
                Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
    }
    /**
     * Receives a `Doctor` object from the network.
     *
     * @return the received `Doctor` object, or `null` if an error occurs.
     */
    public Doctor receiveDoctor(){
            Doctor doctor = null;

            try {
                int id = dataInputStream.readInt();
                String name = dataInputStream.readUTF();
                String surname = dataInputStream.readUTF();
                String date = dataInputStream.readUTF();
                String email = dataInputStream.readUTF();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate dob = LocalDate.parse(date, formatter);
                doctor = new Doctor(id, name, surname, dob, email);
            } catch (EOFException ex) {
                System.out.println("All data have been correctly read.");
            } catch (IOException ex) {
                System.out.println("Unable to read from the client.");
                Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            }

            return doctor;
    }
    /**
     * Receives a `Patient` object from the network.
     *
     * @return the received `Patient` object, or `null` if an error occurs.
     */
    public Patient recievePatient(){
            Patient patient = null;

            try {
                int id = dataInputStream.readInt();
                String name = dataInputStream.readUTF();
                String surname = dataInputStream.readUTF();
                String date = dataInputStream.readUTF();
                String email = dataInputStream.readUTF();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate dob = LocalDate.parse(date, formatter);
                patient = new Patient(id, name, surname, dob, email);
            } catch (EOFException ex) {
                System.out.println("All data have been correctly read.");
            } catch (IOException ex) {
                System.out.println("Unable to read from the client.");
                Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            }
            return patient;
    }
    /**
     * Receives an `Interpretation` object from the network.
     *
     * @return the received `Interpretation` object, or `null` if an error occurs.
     */

    public Interpretation recieveInterpretation(){
            Interpretation interpretation = null;


            try {
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
            } catch (EOFException ex) {
                System.out.println("All data have been correctly read.");
            } catch (IOException ex) {
                System.out.println("Unable to read from the client.");
                Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            }
            return interpretation;
    }
    /**
     * Receives an integer from the network.
     *
     * @return the received integer.
     * @throws IOException if an error occurs during reading.
     */
    public int receiveInt() throws IOException{
        return dataInputStream.readInt();
    }
    /**
     * Receives a `User` object from the network.
     *
     * @return the received `User` object, or `null` if an error occurs.
     */
    public User recieveUser()
    {
            User u = null;
            try {
                String email = dataInputStream.readUTF();
                byte[] psw = dataInputStream.readUTF().getBytes();
                String role = dataInputStream.readUTF();
                Role r = new Role(role);
                u = new User(email, psw, r);
            } catch (IOException ex) {
                Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            }
            return u;
    }
    // Resource Management

    /**
     * Releases resources by closing the input stream.
     */
    public void releaseResources(){
        try {
            dataInputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}