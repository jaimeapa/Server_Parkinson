package ReceiveData;

import Pojos.Doctor;
import Pojos.Interpretation;
import Pojos.Patient;
import Pojos.User;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendDataViaNetwork {
    private static DataOutputStream dataOutputStream;
    public SendDataViaNetwork(Socket socket){
        try {
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();

        }
    }

    public void sendStrings(String message) throws IOException {

        try {
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
        } catch (IOException e) {
            System.err.println("Error send String ");
        }

    }
    public void sendInt(Integer message){
        //OutputStream outputStream = socket.getOutputStream();
        //DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        try{
            //DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeInt(message);
            //releaseResources(dataOutputStream);
        }catch (IOException ex){
            ex.printStackTrace();
        }

        //releaseResourcesInt(dataOutputStream,outputStream);
    }

    public void sendPatient(Patient patient)
    {
        //OutputStream outputStream = null;
        //ObjectOutputStream objectOutputStream = null;

        /*try {
            //socket = new Socket("localhost", 8080);
            outputStream = socket.getOutputStream();
        } catch (IOException ex) {
            System.out.println("It was not possible to connect to the server.");
            System.exit(-1);
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        try {
            //DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeInt(patient.getPatient_id());
            dataOutputStream.writeUTF(patient.getName());
            dataOutputStream.writeUTF(patient.getSurname());
            dataOutputStream.writeUTF(patient.getDob().toString());
            dataOutputStream.writeUTF(patient.getEmail());
            //releaseResources(dataOutputStream);
        } catch (IOException ex) {
            System.out.println("Unable to write the objects on the server.");
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        } /*finally {
            releaseResourcesForPatient(objectOutputStream);

        }*/

    }

    public void sendDoctor(Doctor doctor)
    {
        //OutputStream outputStream = null;
        //ObjectOutputStream objectOutputStream = null;

        /*try {
            //socket = new Socket("localhost", 8080);
            outputStream = socket.getOutputStream();
        } catch (IOException ex) {
            System.out.println("It was not possible to connect to the server.");
            System.exit(-1);
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        try {
            //DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            //objectOutputStream = new ObjectOutputStream(outputStream);
            /*objectOutputStream.writeObject(patient);
            objectOutputStream.flush();
            objectOutputStream.reset();*/
            dataOutputStream.writeInt(doctor.getDoctor_id());
            dataOutputStream.writeUTF(doctor.getName());
            dataOutputStream.writeUTF(doctor.getSurname());
            dataOutputStream.writeUTF(doctor.getDob().toString());
            dataOutputStream.writeUTF(doctor.getEmail());
            //releaseResources(dataOutputStream);
        } catch (IOException ex) {
            System.out.println("Unable to write the objects on the server.");
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        } /*finally {
            releaseResourcesForPatient(objectOutputStream);

        }*/

    }

    public void sendInterpretation(Interpretation interpretation) throws IOException{
        //DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(interpretation.getDate().toString());
        dataOutputStream.writeInt(interpretation.getDoctor_id());
        dataOutputStream.writeUTF(interpretation.getSignalEMG().valuesToString());
        dataOutputStream.writeInt(interpretation.getPatient_id());
        dataOutputStream.writeUTF(interpretation.getSignalEDA().valuesToString());
        dataOutputStream.writeUTF(interpretation.getObservation());
        dataOutputStream.writeUTF(interpretation.getInterpretation());
        //releaseResources(dataOutputStream);
    }

    public void sendUser(User u) throws IOException
    {
        //DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(u.getEmail());
        byte[] password = u.getPassword();

        dataOutputStream.writeUTF(new String(password));
        dataOutputStream.writeUTF(u.getRole().toString());
        //releaseResources(dataOutputStream);
    }

    public void releaseResources(){
        try {
            dataOutputStream.close();
        } catch (IOException ex) {
            //Logger.getLogger(SendBinaryDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
    private void releaseResourcesInt(DataOutputStream dataOutputStream, OutputStream outputStream){
        try {
            dataOutputStream.close();
        } catch (IOException ex) {
            //Logger.getLogger(SendBinaryDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException ex) {
           // Logger.getLogger(SendBinaryDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
}
