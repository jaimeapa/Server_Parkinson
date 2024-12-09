package ui;

import Pojos.*;
import ReceiveData.ReceiveDataViaNetwork;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import ReceiveData.SendDataViaNetwork;
import ifaces.*;
import jdbcs.*;

public class UI implements Runnable{
    private Socket socket;
    private JDBCManager manager;


    public UI(Socket socket, JDBCManager manager){
        this.socket = socket;
        this.manager = manager;
    }

    @Override
    public void run() {
        JDBCPatient patientManager = new JDBCPatient(manager);
        JDBCRole roleManager = new JDBCRole(manager);
        JDBCUser userManager = new JDBCUser(manager, roleManager);
        JDBCSymptoms symptomsManager = new JDBCSymptoms(manager);
        JDBCDoctor doctorManager = new JDBCDoctor(manager);
        JDBCInterpretation interpretationManager = new JDBCInterpretation(manager);
        ReceiveDataViaNetwork recieveDataViaNetwork = null;
        SendDataViaNetwork sendDataViaNetwork = null;

        try{
            recieveDataViaNetwork = new ReceiveDataViaNetwork(socket);
            sendDataViaNetwork = new SendDataViaNetwork(socket);
            System.out.println("Socket accepted");

            int message = recieveDataViaNetwork.receiveInt();
            if(message == 1){
                sendDataViaNetwork.sendStrings("PATIENT");
                patientMenu(recieveDataViaNetwork, sendDataViaNetwork, userManager, patientManager, doctorManager, symptomsManager, interpretationManager, socket);
            } else if (message == 2) {
                sendDataViaNetwork.sendStrings("DOCTOR");
                doctorMenu(recieveDataViaNetwork, sendDataViaNetwork, userManager, doctorManager, patientManager, interpretationManager, symptomsManager, socket);

            }else{
                sendDataViaNetwork.sendStrings("ERROR");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally{
            releaseResources(recieveDataViaNetwork, sendDataViaNetwork, socket);
        }

    }

    private static void patientMenu(ReceiveDataViaNetwork recieveDataViaNetwork, SendDataViaNetwork sendDataViaNetwork, UserManager userManager, PatientManager patientManager, DoctorManager doctorManager, SymptomsManager symptomsManager, InterpretationManager interpretationManager, Socket socket) throws IOException
    {
        try {
            boolean menu = true;

            while (menu) {
                int option = recieveDataViaNetwork.receiveInt();
                if (option == 3) {
                    System.out.println("Patient disconnected");
                } else System.out.println("option " + option);
                switch (option) {
                    case 1: {
                        patientRegister(recieveDataViaNetwork, sendDataViaNetwork, userManager, doctorManager, patientManager, symptomsManager, interpretationManager, socket);
                        break;
                    }
                    case 2: {
                        patientLogIn(recieveDataViaNetwork, sendDataViaNetwork, userManager, patientManager, doctorManager, symptomsManager, interpretationManager, socket);
                        break;
                    }
                    case 3: {
                        menu = false;
                        break;
                    }
                    default: {
                        System.out.println("That number is not an option, try again");
                        break;
                    }
                }
            }
        }catch(IOException e){
            System.out.println("Error or client disconnected");
            releaseResources(recieveDataViaNetwork,sendDataViaNetwork,socket);
        }
    }

    private static void patientRegister(ReceiveDataViaNetwork recieveDataViaNetwork, SendDataViaNetwork sendDataViaNetwork, UserManager userManager, DoctorManager doctorManager, PatientManager patientManager, SymptomsManager symptomsManager, InterpretationManager interpretationManager, Socket socket)  {
        try {
            String message = recieveDataViaNetwork.receiveString();
            if (message.equals("OK")) {
                Patient patient = recieveDataViaNetwork.recievePatient();
                User u = recieveDataViaNetwork.recieveUser();
                System.out.println(u.toString());
                userManager.addUser(u.getEmail(), new String(u.getPassword()), 1);
                int user_id = userManager.getIdFromEmail(u.getEmail());
                ArrayList<Doctor> doctors = doctorManager.readDoctors();
                int doctor_id = 0;
                if (doctors.size() == 1) {
                    // Si solo hay un doctor, selecciona directamente su ID
                    doctor_id = doctors.get(0).getDoctor_id();
                } else if (doctors.size() > 1) {
                    doctor_id = doctors.get(0).getDoctor_id(); // Por defecto el primer doctor
                    int minPatients = patientManager.getPatientsByDoctorId(doctor_id).size();

                    for (int i = 1; i < doctors.size(); i++) {
                        int currentDoctorId = doctors.get(i).getDoctor_id();
                        int currentPatientCount = patientManager.getPatientsByDoctorId(currentDoctorId).size();

                        if (currentPatientCount < minPatients) {
                            doctor_id = currentDoctorId;
                            minPatients = currentPatientCount;
                        }
                    }
                }
                if (!doctors.isEmpty()) {
                    patientManager.addPatient(patient.getName(), patient.getSurname(), patient.getDob(), patient.getEmail(), doctor_id, user_id);
                    sendDataViaNetwork.sendStrings("SUCCESS");
                    sendDataViaNetwork.sendPatient(patient);
                    Doctor doctor = doctorManager.getDoctorFromId(doctor_id);
                    sendDataViaNetwork.sendDoctor(doctor);
                    clientPatientMenu(patient, recieveDataViaNetwork, sendDataViaNetwork, symptomsManager, interpretationManager, socket);
                } else {
                    sendDataViaNetwork.sendStrings("ERROR");
                }
            } else {
                System.out.println("Error in register");
            }
        }catch(IOException e){
            System.out.println("Error or client disconnected");
            releaseResources(recieveDataViaNetwork,sendDataViaNetwork,socket);
        }
    }

    private static void patientLogIn(ReceiveDataViaNetwork recieveDataViaNetwork, SendDataViaNetwork sendDataViaNetwork, UserManager userManager, PatientManager patientManager, DoctorManager doctorManager, SymptomsManager symptomsManager, InterpretationManager interpretationManager, Socket socket) {
        try {
            sendDataViaNetwork.sendStrings("Patient LOG IN");
            String message = recieveDataViaNetwork.receiveString();
            System.out.println(message);
            if (message.equals("OK")) {
                User u = recieveDataViaNetwork.recieveUser();
                User user = userManager.checkPassword(u.getEmail(), new String(u.getPassword()));

                if (user != null) {
                    sendDataViaNetwork.sendStrings("OK");
                    Patient patient = patientManager.getPatientFromUser(userManager.getIdFromEmail(u.getEmail()));
                    System.out.println(patient.toString());
                    sendDataViaNetwork.sendPatient(patient);
                    Doctor doctor = doctorManager.getDoctorFromId(patient.getDoctor_id());
                    sendDataViaNetwork.sendDoctor(doctor);
                    clientPatientMenu(patient, recieveDataViaNetwork, sendDataViaNetwork, symptomsManager, interpretationManager, socket);
                } else {
                    sendDataViaNetwork.sendStrings("ERROR");
                }
            } else {
                System.out.println("Error in log in");
            }
        }catch(IOException e){
            System.out.println("Error or client disconnected");
            releaseResources(recieveDataViaNetwork,sendDataViaNetwork,socket);
        }
    }

    private static void doctorMenu(ReceiveDataViaNetwork recieveDataViaNetwork, SendDataViaNetwork sendDataViaNetwork, UserManager userManager, DoctorManager doctorManager, PatientManager patientManager, InterpretationManager interpretationManager, SymptomsManager symptomsManager, Socket socket) {
        try {
            boolean menu = true;
            while (menu) {
                int option = recieveDataViaNetwork.receiveInt();
                if (option == 3) {
                    System.out.println("Doctor disconnected");
                } else System.out.println("option " + option);
                switch (option) {
                    case 1: { // Registrar nuevo doctor
                        doctorRegister(recieveDataViaNetwork, sendDataViaNetwork, userManager, doctorManager, patientManager, symptomsManager, interpretationManager, socket);
                        break;
                    }
                    case 2: { // Log in como doctor
                        doctorLogIn(recieveDataViaNetwork, sendDataViaNetwork, userManager, doctorManager, patientManager, interpretationManager, symptomsManager, socket);
                        break;
                    }
                    case 3: { // Salir
                        menu = false;
                        break;
                    }
                    default: {
                        System.out.println("That number is not an option, try again");
                        break;
                    }
                }
            }
        }catch(IOException e){
            System.out.println("Error or client disconnected");
            releaseResources(recieveDataViaNetwork,sendDataViaNetwork,socket);
        }
    }
    private static void doctorRegister(ReceiveDataViaNetwork recieveDataViaNetwork, SendDataViaNetwork sendDataViaNetwork, UserManager userManager, DoctorManager doctorManager, PatientManager patientManager, SymptomsManager symptomsManager, InterpretationManager interpretationManager, Socket socket){

        String message = recieveDataViaNetwork.receiveString();
        if (message.equals("OK")) {
            Doctor doctor = recieveDataViaNetwork.receiveDoctor();
            User u = recieveDataViaNetwork.recieveUser();
            System.out.println(u.toString());
            userManager.addUser(u.getEmail(), new String(u.getPassword()), 2);
            int user_id = userManager.getIdFromEmail(u.getEmail());
            doctorManager.addDoctor(doctor.getName(), doctor.getSurname(), doctor.getDob(), u.getEmail(), user_id);
            clientDoctorMenu(doctor, recieveDataViaNetwork, sendDataViaNetwork, patientManager, interpretationManager, symptomsManager, socket);
        }
    }
    private static void doctorLogIn(ReceiveDataViaNetwork recieveDataViaNetwork,SendDataViaNetwork sendDataViaNetwork, UserManager userManager, DoctorManager doctorManager, PatientManager patientManager, InterpretationManager interpretationManager, SymptomsManager symptomsManager, Socket socket) {
        try {
            String message = recieveDataViaNetwork.receiveString();
            System.out.println(message);
            if (message.equals("OK")) {
                User u = recieveDataViaNetwork.recieveUser();
                User user = userManager.checkPassword(u.getEmail(), new String(u.getPassword()));

                if (user != null) {
                    sendDataViaNetwork.sendStrings("OK");
                    Doctor doctor = doctorManager.getDoctorFromUser(user.getId());
                    System.out.println(doctor.toString());
                    sendDataViaNetwork.sendDoctor(doctor);
                    clientDoctorMenu(doctor, recieveDataViaNetwork, sendDataViaNetwork, patientManager, interpretationManager, symptomsManager, socket); // Redirigir al menú del doctor
                } else {
                    sendDataViaNetwork.sendStrings("ERROR");
                }
            } else {
                System.out.println("Error in log in");
            }
        }catch(IOException e){
            System.out.println("Error or client disconnected");
            releaseResources(recieveDataViaNetwork,sendDataViaNetwork,socket);
        }
    }

    private static void clientDoctorMenu(Doctor doctor_logedIn, ReceiveDataViaNetwork recieveDataViaNetwork, SendDataViaNetwork sendDataViaNetwork, PatientManager patientManager, InterpretationManager interpretationManager, SymptomsManager symptomsManager, Socket socket) {
        try {
            boolean menu = true;
            while (menu) {
                int option = recieveDataViaNetwork.receiveInt();
                switch (option) {
                    case 1: // Mostrar lista de pacientes y elegir uno para ver detalles
                        viewDetailsOfPatient(doctor_logedIn, recieveDataViaNetwork, sendDataViaNetwork, patientManager, socket);
                        break;
                    case 2: // Interpretar datos enviados por el paciente y devolver una respuesta
                        makeAnInterpretation(doctor_logedIn, recieveDataViaNetwork, sendDataViaNetwork, patientManager, interpretationManager, symptomsManager, socket);
                        break;
                    case 3: // Log out
                        menu = false;
                        break;
                    default:
                        System.out.println("That number is not an option, try again");
                        break;
                }
            }
        }catch(IOException e){
            System.out.println("Error or client disconnected");
            releaseResources(recieveDataViaNetwork,sendDataViaNetwork,socket);
        }
    }

    private static void viewDetailsOfPatient(Doctor doctor_logedIn, ReceiveDataViaNetwork recieveDataViaNetwork, SendDataViaNetwork sendDataViaNetwork, PatientManager patientManager, Socket socket) {
        try {
            Patient patient = null;
            List<Patient> patients = patientManager.getPatientsByDoctorId(doctor_logedIn.getDoctor_id());
            int size = patients.size();
            sendDataViaNetwork.sendInt(size);
            if (size > 0) {
                for (Patient patientSelected : patients) {
                    sendDataViaNetwork.sendPatient(patientSelected);
                    System.out.println("Patient " + patientSelected.getPatient_id() + " sent");
                }
                int patientId = recieveDataViaNetwork.receiveInt();
                patient = patients.get(patientId); //hay que hacer un getPatientFromId para pacientes que ya han grabado datos y otro para los que no
            }
            if (patient != null) {
                sendDataViaNetwork.sendPatient(patient);
            }
        }catch(IOException e){
            System.out.println("Error or client disconnected");
            releaseResources(recieveDataViaNetwork,sendDataViaNetwork,socket);
        }
    }

    private static void makeAnInterpretation(Doctor doctor_logedIn, ReceiveDataViaNetwork recieveDataViaNetwork, SendDataViaNetwork sendDataViaNetwork, PatientManager patientManager, InterpretationManager interpretationManager, SymptomsManager symptomsManager, Socket socket) {
        try {
            List<Patient> patients = patientManager.getPatientsByDoctorId(doctor_logedIn.getDoctor_id());
            int length = patients.size();
            sendDataViaNetwork.sendInt(length);
            if (length > 0) {
                for (Patient patient2 : patients) {
                    sendDataViaNetwork.sendPatient(patient2);
                }
                int patientId2 = recieveDataViaNetwork.receiveInt();
                Patient patient2 = patients.get(patientId2);
                LinkedList<Interpretation> interpretations = interpretationManager.getInterpretationsFromPatient_Id(patient2.getPatient_id());
                if (interpretations.isEmpty()) {
                    sendDataViaNetwork.sendStrings("ERROR");
                } else {
                    sendDataViaNetwork.sendStrings("OKAY");
                    sendDataViaNetwork.sendInt(interpretations.size());
                    for (Interpretation interpretation : interpretations) {
                        sendDataViaNetwork.sendInterpretation(interpretation);
                    }
                    int interpretationIndex = recieveDataViaNetwork.receiveInt();
                    Interpretation interpretation = interpretations.get(interpretationIndex);
                    if (interpretation != null) {
                        sendDataViaNetwork.sendInterpretation(interpretation);
                        LinkedList<Symptoms> symptoms = symptomsManager.getSymptomsFromInterpretation(interpretation.getId());
                        System.out.println(symptoms);
                        int size_symptoms = symptoms.size();
                        sendDataViaNetwork.sendInt(size_symptoms);
                        if (size_symptoms > 0) {
                            for (Symptoms symptom : symptoms) {
                                sendDataViaNetwork.sendStrings(symptom.getName());
                            }
                        }
                    }
                    String interpretation2 = recieveDataViaNetwork.receiveString();
                    if (interpretation != null) {
                        interpretation.setInterpretation(interpretation2);
                    }
                    interpretationManager.setInterpretation(interpretation2, interpretation.getId());
                    System.out.println(interpretation.toString());
                }
            }
        }catch(IOException e){
            System.out.println("Error or client disconnected");
            releaseResources(recieveDataViaNetwork,sendDataViaNetwork,socket);
        }
    }

    public static void clientPatientMenu(Patient patient_logedIn, ReceiveDataViaNetwork recieveDataViaNetwork, SendDataViaNetwork sendDataViaNetwork, SymptomsManager symptomsManager, InterpretationManager interpretationManager, Socket socket) {
        try {
            int option;
            boolean menu = true;
            ArrayList<Integer> patientSymptomsID = new ArrayList<>();
            while (menu) {
                option = recieveDataViaNetwork.receiveInt();
                switch (option) {
                    case 1: {
                        patientSymptomsID = readSymptoms(recieveDataViaNetwork, sendDataViaNetwork, symptomsManager, socket);
                        break;
                    }
                    case 4: {
                        seeYourReports(patient_logedIn, recieveDataViaNetwork, sendDataViaNetwork, interpretationManager, symptomsManager, socket);
                        break;
                    }
                    case 5: {
                        menu = false;
                        recieveInterpretationAndlogOut(patientSymptomsID, recieveDataViaNetwork, sendDataViaNetwork, interpretationManager, socket);
                        break;
                    }
                    default:
                        System.out.println("That number is not an option, try again");
                        break;
                }

            }
        }catch(IOException e){
            System.out.println("Error or client disconnected");
            releaseResources(recieveDataViaNetwork,sendDataViaNetwork,socket);
        }
    }

    private static ArrayList<Integer> readSymptoms(ReceiveDataViaNetwork recieveDataViaNetwork, SendDataViaNetwork sendDataViaNetwork, SymptomsManager symptomsManager, Socket socket) {
        try {
            ArrayList<Integer> patientSymptomsID = new ArrayList<>();
            ArrayList<Symptoms> symptoms = symptomsManager.readSymptoms();
            for (int i = 0; i < symptoms.size(); i++) {
                sendDataViaNetwork.sendStrings(symptoms.get(i).getName());
            }
            sendDataViaNetwork.sendStrings("stop");
            sendDataViaNetwork.sendStrings("Type the numbers corresponding to the symptoms you have (To stop adding symptoms type '0'): ");

            int symptomId = 1;
            while (symptomId != 0) {
                symptomId = recieveDataViaNetwork.receiveInt();
                if (symptomId != 0) {
                    System.out.println("Symptoms ids: " + symptomId);
                    patientSymptomsID.add(symptomId);
                }
            }
            sendDataViaNetwork.sendStrings("Your symptoms have been recorded correctly!");
            return patientSymptomsID;
        }catch(IOException e){
            System.out.println("Error or client disconnected");
            releaseResources(recieveDataViaNetwork,sendDataViaNetwork,socket);
            return null;
        }
    }

    private static void seeYourReports(Patient patient_logedIn, ReceiveDataViaNetwork recieveDataViaNetwork, SendDataViaNetwork sendDataViaNetwork, InterpretationManager interpretationManager, SymptomsManager symptomsManager, Socket socket) {
        try {
            LinkedList<Interpretation> allInterpretations = interpretationManager.getInterpretationsFromPatient_Id(patient_logedIn.getPatient_id());
            int length = allInterpretations.size();
            sendDataViaNetwork.sendInt(length);
            LinkedList<Symptoms> allSymptoms;
            int lengthSymptom;
            for (int i = 0; i < length; i++) {
                lengthSymptom = 0;
                sendDataViaNetwork.sendInterpretation(allInterpretations.get(i));
                allSymptoms = symptomsManager.getSymptomsFromInterpretation(allInterpretations.get(i).getId());
                if (!allSymptoms.isEmpty()) {
                    lengthSymptom = allSymptoms.size();
                }
                sendDataViaNetwork.sendInt(lengthSymptom);
                if (lengthSymptom != 0) {
                    for (int j = 0; j < lengthSymptom; j++) {
                        sendDataViaNetwork.sendStrings(allSymptoms.get(j).getName());
                    }
                }
            }
            System.out.println(recieveDataViaNetwork.receiveString());
        }catch(IOException e){
            System.out.println("Error or client disconnected");
            releaseResources(recieveDataViaNetwork,sendDataViaNetwork,socket);
        }
    }

    private static void recieveInterpretationAndlogOut(ArrayList<Integer> patientSymptomsID, ReceiveDataViaNetwork recieveDataViaNetwork, SendDataViaNetwork sendDataViaNetwork, InterpretationManager interpretationManager, Socket socket)  {
        try {
            Interpretation interpretation = recieveDataViaNetwork.recieveInterpretation();

            if (interpretation != null) {
                sendDataViaNetwork.sendStrings("OK");
                if (interpretation.getSignalEDA().getValues().isEmpty() && interpretation.getSignalEMG().getValues().isEmpty()
                        && patientSymptomsID.isEmpty()) {
                    System.out.println("The report is empty, not added");
                } else {
                    interpretationManager.addInterpretation(interpretation);
                    int interpretation_id = interpretationManager.getId(interpretation.getDate(), interpretation.getPatient_id());
                    for (int i = 0; i < patientSymptomsID.size(); i++) {
                        System.out.println(patientSymptomsID.get(i));
                        interpretationManager.assignSymtomsToInterpretation(interpretation_id, patientSymptomsID.get(i));
                    }
                }
            } else {
                sendDataViaNetwork.sendStrings("NOTOKAY");
            }
            System.out.println(interpretation);
        }catch(IOException e){
            System.out.println("Error or client disconnected");
            releaseResources(recieveDataViaNetwork,sendDataViaNetwork,socket);
        }
    }

    private static void releaseResources(ReceiveDataViaNetwork recieveDataViaNetwork, SendDataViaNetwork sendDataViaNetwork, Socket socket){
        if(sendDataViaNetwork != null && recieveDataViaNetwork != null) {
            sendDataViaNetwork.releaseResources();
            recieveDataViaNetwork.releaseResources();
        }
        try {
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
