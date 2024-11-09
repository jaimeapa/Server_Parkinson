package ReceiveData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiveStringsViaNetwork {

    public static String recieveString(ServerSocket serverSocket) throws IOException {

        //ServerSocket serverSocket = new ServerSocket(9000);
        Socket socket = serverSocket.accept();
        System.out.println("Connection client created");
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
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
}