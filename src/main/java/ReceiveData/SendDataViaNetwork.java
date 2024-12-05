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

        }
    }

    public static void sendStrings(String message, Socket socket) throws IOException {

        //System.out.println("Connection established... sending text");
        //DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(message);
        //releaseResources(dataOutputStream);
        //releaseResourcesForString(printWriter,socket);

    }
    public static void sendInt(Integer message,  Socket socket) throws IOException{
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

    public static void sendPatient(Patient patient, Socket socket)
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

    public static void sendDoctor(Doctor doctor, Socket socket)
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

    public static void sendInterpretation(Interpretation interpretation, Socket socket) throws IOException{
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

    public static void sendUser(User u, Socket socket) throws IOException
    {
        //DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(u.getEmail());
        byte[] password = u.getPassword();

        dataOutputStream.writeUTF(new String(password));
        dataOutputStream.writeUTF(u.getRole().toString());
        //releaseResources(dataOutputStream);
    }

    public static void releaseResources(){
        try {
            dataOutputStream.close();
        } catch (IOException ex) {
            //Logger.getLogger(SendBinaryDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }


    private static void releaseResourcesForString(PrintWriter printWriter, Socket socket) {
        printWriter.close();
 
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static void releaseResourcesInt(DataOutputStream dataOutputStream, OutputStream outputStream){
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
    private static void releaseResourcesForPatient(ObjectOutputStream objectOutputStream) {
        try {
            objectOutputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
