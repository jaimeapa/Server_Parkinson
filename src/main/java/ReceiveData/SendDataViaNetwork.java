package ReceiveData;

import Pojos.Patient;
import Pojos.User;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendDataViaNetwork {

    public static void sendStrings(String message, PrintWriter printWriter) throws IOException {

        //System.out.println("Connection established... sending text");
        printWriter.println(message);
        //printWriter.println("stop");
        //releaseResourcesForString(printWriter,socket);

    }
    public static void sendInt(Integer message,  DataOutputStream dataOutputStream) throws IOException{
        //OutputStream outputStream = socket.getOutputStream();
        //DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        try{
            dataOutputStream.writeInt(message);
        }catch (IOException ex){
            ex.printStackTrace();
        }

        //releaseResourcesInt(dataOutputStream,outputStream);
    }

    public static void sendPatient(Patient patient, DataOutputStream dataOutputStream)
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
            //objectOutputStream = new ObjectOutputStream(outputStream);
            /*objectOutputStream.writeObject(patient);
            objectOutputStream.flush();
            objectOutputStream.reset();*/
            dataOutputStream.writeUTF(patient.getName());
            dataOutputStream.writeUTF(patient.getSurname());
            dataOutputStream.writeUTF(patient.getDob().toString());
            dataOutputStream.writeUTF(patient.getEmail());

        } catch (IOException ex) {
            System.out.println("Unable to write the objects on the server.");
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        } /*finally {
            releaseResourcesForPatient(objectOutputStream);

        }*/

    }

    public static void sendUser(User u, DataOutputStream dataOutputStream) throws IOException
    {
        dataOutputStream.writeUTF(u.getEmail());
        dataOutputStream.writeUTF(new String(u.getPassword()));
        dataOutputStream.writeUTF(u.getRole().toString());
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
