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
/**
 * The {@code UI} class implements the {@code Runnable} interface and handles the interaction
 * with clients through network communication. It manages the user interface for different types of users
 * (patients and doctors) based on messages received from the client.
 * It processes login, registration, and handles the main menu options for both patients and doctors.
 * This class runs on a separate thread for each client connection.
 */
public class UI implements Runnable{
    /**
     * The socket used for the communication between the client and server.
     * This socket facilitates the transmission and reception of data between
     * the client application and the server. It is used for network
     * communication to ensure that the client can send and receive necessary
     * data, such as symptoms, reports, and interpretations, to/from the server.
     */
    private Socket socket;
    /** The database manager */
    private JDBCManager manager;

    /**
     * Constructs a new {@code UI} instance with the provided socket and database manager.
     *
     * @param socket The socket for communication with the client.
     * @param manager The {@code JDBCManager} instance used to interact with the database.
     */
    public UI(Socket socket, JDBCManager manager){
        this.socket = socket;
        this.manager = manager;
    }
    /**
     * Starts the user interface for the client, handling either a patient or doctor login.
     * It determines the type of user based on the initial message received from the client and
     * routes the user to the appropriate menu.
     * This method is executed on a separate thread when the {@code UI} class is run.
     */
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
    /**
     * Displays the patient menu and handles patient-related operations such as registration
     * and login. Routes the client to the appropriate submenu based on user input.
     *
     * @param recieveDataViaNetwork The object used to receive data from the client.
     * @param sendDataViaNetwork The object used to send data to the client.
     * @param userManager The manager for user-related operations.
     * @param patientManager The manager for patient-related operations.
     * @param doctorManager The manager for doctor-related operations.
     * @param symptomsManager The manager for symptoms-related operations.
     * @param interpretationManager The manager for interpretation-related operations.
     * @param socket The socket for network communication with the client.
     * @throws IOException If an error occurs during communication with the client.
     */
    private static void patientMenu(ReceiveDataViaNetwork recieveDataViaNetwork, SendDataViaNetwork sendDataViaNetwork, UserManager userManager, PatientManager patientManager, DoctorManager doctorManager, SymptomsManager symptomsManager, InterpretationManager interpretationManager, Socket socket) throws IOException
    {
        try {
            boolean menu = true;

            while (menu) {
                int option = recieveDataViaNetwork.receiveInt();
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
                        System.out.println("Patient disconnected");
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
    /**
     * Handles the patient registration process. Registers the user, assigns them to a doctor,
     * and sends the relevant information back to the client.
     *
     * @param recieveDataViaNetwork The object used to receive data from the client.
     * @param sendDataViaNetwork The object used to send data to the client.
     * @param userManager The manager for user-related operations.
     * @param doctorManager The manager for doctor-related operations.
     * @param patientManager The manager for patient-related operations.
     * @param symptomsManager The manager for symptoms-related operations.
     * @param interpretationManager The manager for interpretation-related operations.
     * @param socket The socket for network communication with the client.
     */
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
                    doctor_id = doctors.get(0).getDoctor_id();
                } else if (doctors.size() > 1) {
                    doctor_id = doctors.get(0).getDoctor_id();
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
                    patient.setPatient_id(patientManager.getId(patient.getName()));
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
    /**
     * Handles the patient login process by verifying credentials, fetching patient data, and directing the user to the patient menu.
     *
     * @param recieveDataViaNetwork the object used to receive data from the network
     * @param sendDataViaNetwork the object used to send data to the network
     * @param userManager the user manager for managing user data
     * @param patientManager the patient manager for managing patient data
     * @param doctorManager the doctor manager for managing doctor data
     * @param symptomsManager the symptoms manager for managing symptoms data
     * @param interpretationManager the interpretation manager for managing interpretation data
     * @param socket the socket connection to the client
     */
    private static void patientLogIn(ReceiveDataViaNetwork recieveDataViaNetwork, SendDataViaNetwork sendDataViaNetwork, UserManager userManager, PatientManager patientManager, DoctorManager doctorManager, SymptomsManager symptomsManager, InterpretationManager interpretationManager, Socket socket) {
        try {
            sendDataViaNetwork.sendStrings("Patient LOG IN");
            String message = recieveDataViaNetwork.receiveString();
            System.out.println(message);
            Role role = new Role("patient");
            if (message.equals("OK")) {
                User u = recieveDataViaNetwork.recieveUser();
                User user = userManager.checkPassword(u.getEmail(), new String(u.getPassword()));

                if (user != null && user.getRole().getName().equals(role.getName())) {
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
    /**
     * Displays the doctor menu and handles navigation through it based on user input. Provides options for doctor registration, login, or exiting.
     *
     * @param recieveDataViaNetwork the object used to receive data from the network
     * @param sendDataViaNetwork the object used to send data to the network
     * @param userManager the user manager for managing user data
     * @param doctorManager the doctor manager for managing doctor data
     * @param patientManager the patient manager for managing patient data
     * @param interpretationManager the interpretation manager for managing interpretation data
     * @param symptomsManager the symptoms manager for managing symptoms data
     * @param socket the socket connection to the client
     */
    private static void doctorMenu(ReceiveDataViaNetwork recieveDataViaNetwork, SendDataViaNetwork sendDataViaNetwork, UserManager userManager, DoctorManager doctorManager, PatientManager patientManager, InterpretationManager interpretationManager, SymptomsManager symptomsManager, Socket socket) {
        try {
            boolean menu = true;
            while (menu) {
                int option = recieveDataViaNetwork.receiveInt();
                switch (option) {
                    case 1: {
                        doctorRegister(recieveDataViaNetwork, sendDataViaNetwork, userManager, doctorManager, patientManager, symptomsManager, interpretationManager, socket);
                        break;
                    }
                    case 2: {
                        doctorLogIn(recieveDataViaNetwork, sendDataViaNetwork, userManager, doctorManager, patientManager, interpretationManager, symptomsManager, socket);
                        break;
                    }
                    case 3: {
                        menu = false;
                        System.out.println("Doctor disconnected");
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
    /**
     * Registers a new doctor by receiving doctor and user data from the client, storing it in the system, and navigating to the doctor menu.
     *
     * @param recieveDataViaNetwork the object used to receive data from the network
     * @param sendDataViaNetwork the object used to send data to the network
     * @param userManager the user manager for managing user data
     * @param doctorManager the doctor manager for managing doctor data
     * @param patientManager the patient manager for managing patient data
     * @param symptomsManager the symptoms manager for managing symptoms data
     * @param interpretationManager the interpretation manager for managing interpretation data
     * @param socket the socket connection to the client
     */
    private static void doctorRegister(ReceiveDataViaNetwork recieveDataViaNetwork, SendDataViaNetwork sendDataViaNetwork, UserManager userManager, DoctorManager doctorManager, PatientManager patientManager, SymptomsManager symptomsManager, InterpretationManager interpretationManager, Socket socket){

        String message = recieveDataViaNetwork.receiveString();
        if (message.equals("OK")) {
            Doctor doctor = recieveDataViaNetwork.receiveDoctor();
            User u = recieveDataViaNetwork.recieveUser();
            System.out.println(u.toString());
            userManager.addUser(u.getEmail(), new String(u.getPassword()), 2);
            int user_id = userManager.getIdFromEmail(u.getEmail());
            doctorManager.addDoctor(doctor.getName(), doctor.getSurname(), doctor.getDob(), u.getEmail(), user_id);
            doctor.setDoctor_id(doctorManager.getId(doctor.getName()));
            clientDoctorMenu(doctor, recieveDataViaNetwork, sendDataViaNetwork, patientManager, interpretationManager, symptomsManager, socket);
        }
    }
    /**
     * Handles the doctor login process by verifying credentials, fetching doctor data, and directing the user to the doctor menu.
     *
     * @param recieveDataViaNetwork the object used to receive data from the network
     * @param sendDataViaNetwork the object used to send data to the network
     * @param userManager the user manager for managing user data
     * @param doctorManager the doctor manager for managing doctor data
     * @param patientManager the patient manager for managing patient data
     * @param interpretationManager the interpretation manager for managing interpretation data
     * @param symptomsManager the symptoms manager for managing symptoms data
     * @param socket the socket connection to the client
     */
    private static void doctorLogIn(ReceiveDataViaNetwork recieveDataViaNetwork,SendDataViaNetwork sendDataViaNetwork, UserManager userManager, DoctorManager doctorManager, PatientManager patientManager, InterpretationManager interpretationManager, SymptomsManager symptomsManager, Socket socket) {
        try {
            String message = recieveDataViaNetwork.receiveString();
            System.out.println(message);
            Role role = new Role("doctor");
            if (message.equals("OK")) {
                User u = recieveDataViaNetwork.recieveUser();
                User user = userManager.checkPassword(u.getEmail(), new String(u.getPassword()));

                if (user != null && user.getRole().getName().equals(role.getName())) {
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
    /**
     * Displays the doctor menu with options to view patient details, interpret data, or log out.
     *
     * @param doctor_logedIn the currently logged-in doctor
     * @param recieveDataViaNetwork the object used to receive data from the network
     * @param sendDataViaNetwork the object used to send data to the network
     * @param patientManager the patient manager for managing patient data
     * @param interpretationManager the interpretation manager for managing interpretation data
     * @param symptomsManager the symptoms manager for managing symptoms data
     * @param socket the socket connection to the client
     */
    private static void clientDoctorMenu(Doctor doctor_logedIn, ReceiveDataViaNetwork recieveDataViaNetwork, SendDataViaNetwork sendDataViaNetwork, PatientManager patientManager, InterpretationManager interpretationManager, SymptomsManager symptomsManager, Socket socket) {
        try {
            boolean menu = true;
            while (menu) {
                int option = recieveDataViaNetwork.receiveInt();
                switch (option) {
                    case 1:
                        viewDetailsOfPatient(doctor_logedIn, recieveDataViaNetwork, sendDataViaNetwork, patientManager, socket);
                        break;
                    case 2:
                        makeAnInterpretation(doctor_logedIn, recieveDataViaNetwork, sendDataViaNetwork, patientManager, interpretationManager, symptomsManager, socket);
                        break;
                    case 3:
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
    /**
     * Displays the list of patients associated with the logged-in doctor and allows the doctor to view details of a selected patient.
     *
     * @param doctor_logedIn the currently logged-in doctor
     * @param recieveDataViaNetwork the object used to receive data from the network
     * @param sendDataViaNetwork the object used to send data to the network
     * @param patientManager the patient manager for managing patient data
     * @param socket the socket connection to the client
     */
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
                patient = patients.get(patientId);
            }
            if (patient != null) {
                sendDataViaNetwork.sendPatient(patient);
            }
        }catch(IOException e){
            System.out.println("Error or client disconnected");
            releaseResources(recieveDataViaNetwork,sendDataViaNetwork,socket);
        }
    }
    /**
     * Handles the process of interpreting data sent by a patient, and allows the doctor to send a response to the patient.
     *
     * @param doctor_logedIn the currently logged-in doctor
     * @param recieveDataViaNetwork the object used to receive data from the network
     * @param sendDataViaNetwork the object used to send data to the network
     * @param patientManager the patient manager for managing patient data
     * @param interpretationManager the interpretation manager for managing interpretation data
     * @param symptomsManager the symptoms manager for managing symptoms data
     * @param socket the socket connection to the client
     */
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
    /**
     * Displays the patient menu and allows the patient to perform different actions like entering symptoms, viewing reports, and logging out.
     *
     * @param patient_logedIn the currently logged-in patient
     * @param recieveDataViaNetwork the object used to receive data from the network
     * @param sendDataViaNetwork the object used to send data to the network
     * @param symptomsManager the manager for handling symptom data
     * @param interpretationManager the manager for handling interpretation data
     * @param socket the socket connection to the client
     */
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
    /**
     * Allows the patient to select symptoms from a list and records the selected symptoms.
     *
     * @param recieveDataViaNetwork the object used to receive data from the network
     * @param sendDataViaNetwork the object used to send data to the network
     * @param symptomsManager the manager for handling symptom data
     * @param socket the socket connection to the client
     * @return a list of symptom IDs selected by the patient
     */
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
    /**
     * Displays the reports of the logged-in patient, including interpretations and associated symptoms.
     *
     * @param patient_logedIn the currently logged-in patient
     * @param recieveDataViaNetwork the object used to receive data from the network
     * @param sendDataViaNetwork the object used to send data to the network
     * @param interpretationManager the manager for handling interpretation data
     * @param symptomsManager the manager for handling symptom data
     * @param socket the socket connection to the client
     */
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
    /**
     * Receives the interpretation data from the patient and logs out the patient, updating the interpretation data.
     *
     * @param patientSymptomsID the list of symptoms IDs recorded by the patient
     * @param recieveDataViaNetwork the object used to receive data from the network
     * @param sendDataViaNetwork the object used to send data to the network
     * @param interpretationManager the manager for handling interpretation data
     * @param socket the socket connection to the client
     */
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
    /**
     * Releases the resources associated with the network connection, closing the socket and other necessary streams.
     *
     * @param recieveDataViaNetwork the object used to receive data from the network
     * @param sendDataViaNetwork the object used to send data to the network
     * @param socket the socket connection to the client
     */
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
