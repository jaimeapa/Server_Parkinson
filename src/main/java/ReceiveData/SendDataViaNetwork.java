package ReceiveData;

import Pojos.Doctor;
import Pojos.Interpretation;
import Pojos.Patient;
import Pojos.User;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * The `SendDataViaNetwork` class provides methods to send data over a network socket.
 * It supports sending different types of data such as strings, integers, and objects like
 * `Doctor`, `Patient`, `Interpretation`, and `User`.
 */
public class SendDataViaNetwork {
    // Fields

    /** Output stream to send data over the network socket. */
    private  DataOutputStream dataOutputStream;
    // Constructor

    /**
     * Constructs a `SendDataViaNetwork` object with a given network socket.
     *
     * @param socket the network socket to send data through.
     */
    public SendDataViaNetwork(Socket socket){
        try {
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }catch (IOException ex){
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // Methods to Send Data

    /**
     * Sends a string message over the network.
     *
     * @param message the string message to send.
     * @throws IOException if an I/O error occurs during sending.
     */
    public void sendStrings(String message) throws IOException {

        try {
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
        } catch (IOException e) {
            System.err.println("Error send String ");
        }
    }
    /**
     * Sends an integer message over the network.
     *
     * @param message the integer message to send.
     */
    public void sendInt(Integer message){
        try{
            dataOutputStream.writeInt(message);
        }catch (IOException ex){
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Sends a `Patient` object over the network.
     *
     * @param patient the `Patient` object to send.
     */
    public void sendPatient(Patient patient)
    {
        try {
            dataOutputStream.writeInt(patient.getPatient_id());
            dataOutputStream.writeUTF(patient.getName());
            dataOutputStream.writeUTF(patient.getSurname());
            dataOutputStream.writeUTF(patient.getDob().toString());
            dataOutputStream.writeUTF(patient.getEmail());
        } catch (IOException ex) {
            System.out.println("Unable to write the objects on the server.");
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Sends a `Doctor` object over the network.
     *
     * @param doctor the `Doctor` object to send.
     */
    public void sendDoctor(Doctor doctor)
    {
        try {
             dataOutputStream.writeInt(doctor.getDoctor_id());
            dataOutputStream.writeUTF(doctor.getName());
            dataOutputStream.writeUTF(doctor.getSurname());
            dataOutputStream.writeUTF(doctor.getDob().toString());
            dataOutputStream.writeUTF(doctor.getEmail());
        } catch (IOException ex) {
            System.out.println("Unable to write the objects on the server.");
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Sends an `Interpretation` object over the network.
     *
     * @param interpretation the `Interpretation` object to send.
     * @throws IOException if an I/O error occurs during sending.
     */
    public void sendInterpretation(Interpretation interpretation) throws IOException{
        dataOutputStream.writeUTF(interpretation.getDate().toString());
        dataOutputStream.writeInt(interpretation.getDoctor_id());
        dataOutputStream.writeUTF(interpretation.getSignalEMG().valuesToString());
        dataOutputStream.writeInt(interpretation.getPatient_id());
        dataOutputStream.writeUTF(interpretation.getSignalEDA().valuesToString());
        dataOutputStream.writeUTF(interpretation.getObservation());
        dataOutputStream.writeUTF(interpretation.getInterpretation());
    }
    /**
     * Sends a `User` object over the network.
     *
     * @param u the `User` object to send.
     * @throws IOException if an I/O error occurs during sending.
     */
    public void sendUser(User u) throws IOException
    {
        //DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(u.getEmail());
        byte[] password = u.getPassword();

        dataOutputStream.writeUTF(new String(password));
        dataOutputStream.writeUTF(u.getRole().toString());
        //releaseResources(dataOutputStream);
    }
    // Resource Management

    /**
     * Releases resources by closing the output stream.
     */
    public void releaseResources(){
        try {
            dataOutputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
