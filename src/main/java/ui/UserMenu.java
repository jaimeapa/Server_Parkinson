package ui;

import Pojos.*;
import ReceiveData.ReceiveDataViaNetwork;
import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ReceiveData.SendDataViaNetwork;
import jdbcs.*;

public class UserMenu implements Runnable{
    private static Socket socket;
    private static JDBCManager manager;
    private static JDBCPatient patientManager;
    private static JDBCUser userManager;
    private static JDBCRole roleManager;
    private static JDBCSymptoms symptomsManager;
    private static JDBCDoctor doctorManager;
    private static JDBCInterpretation interpretationManager;
    private static ReceiveDataViaNetwork recieveDataViaNetwork;
    private static SendDataViaNetwork sendDataViaNetwork;

    public UserMenu(Socket socket, JDBCManager manager){
        this.socket = socket;
        this.manager = manager;
    }

    @Override
    public void run() {
        patientManager = new JDBCPatient(manager);
        roleManager = new JDBCRole(manager);
        userManager = new JDBCUser(manager, roleManager);
        symptomsManager = new JDBCSymptoms(manager);
        doctorManager = new JDBCDoctor(manager);
        interpretationManager = new JDBCInterpretation(manager);

        try{
            recieveDataViaNetwork = new ReceiveDataViaNetwork(socket);
            sendDataViaNetwork = new SendDataViaNetwork(socket);
            //patientSymptoms = new ArrayList<>();

            System.out.println("Socket accepted");

            int message = recieveDataViaNetwork.receiveInt();
            if(message == 1){
                patientMenu();
            } else if (message == 2) {
                doctorMenu();

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        releaseResources();
    }

    private static void patientMenu() throws IOException
    {
        boolean menu = true;

        while(menu){
            int option = recieveDataViaNetwork.receiveInt();
            switch (option) {
                case 1 : {
                    patientRegister();
                    break;
                }
                case 2 :{
                    patientLogIn();
                    break;
                }
                case 3 :{
                    menu=false;
                    break;
                }
                default:{
                    System.out.println("That number is not an option, try again");
                    break;
                }
            }
        }
    }

    private static void patientRegister() throws IOException {
        Patient patient = recieveDataViaNetwork.recievePatient();
        User u = recieveDataViaNetwork.recieveUser();
        System.out.println(u.toString());
        userManager.addUser(u.getEmail(), new String(u.getPassword()), 1);
        Integer user_id = userManager.getIdFromEmail(u.getEmail());
        ArrayList<Doctor> doctors = doctorManager.readDoctors();
        int doctor_id = 0;
        if (doctors.size() == 1) {
            // Si solo hay un doctor, selecciona directamente su ID
            doctor_id = doctors.get(0).getDoctor_id();
        } else if(doctors.size() > 1) {
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
        if(!doctors.isEmpty()) {
            patientManager.addPatient(patient.getName(), patient.getSurname(), patient.getDob(), patient.getEmail(), doctor_id, user_id);
            sendDataViaNetwork.sendStrings("SUCCESS");
            sendDataViaNetwork.sendPatient(patient);
            Doctor doctor = doctorManager.getDoctorFromId(doctor_id);
            sendDataViaNetwork.sendDoctor(doctor);
            clientPatientMenu(patient);
        }else{
            sendDataViaNetwork.sendStrings("ERROR");
        }
    }

    private static void patientLogIn() throws IOException {
        User u = recieveDataViaNetwork.recieveUser();
        User user = userManager.checkPassword(u.getEmail(), new String(u.getPassword()));

        if(user != null){
            Patient patient = patientManager.getPatientFromUser(userManager.getIdFromEmail(u.getEmail()));
            System.out.println(patient.toString());
            sendDataViaNetwork.sendPatient(patient);
            Doctor doctor = doctorManager.getDoctorFromId(patient.getDoctor_id());
            sendDataViaNetwork.sendDoctor(doctor);
            clientPatientMenu(patient);
        }else{
            Patient patient = new Patient( "name", "surname", LocalDate.of(1,1,1), "email");
            sendDataViaNetwork.sendPatient(patient);
        }
    }

    private static void doctorMenu() throws IOException {
        boolean menu = true;

        while(menu){
            int option = recieveDataViaNetwork.receiveInt();
            switch (option) {
                case 1: { // Registrar nuevo doctor
                    Doctor doctor = recieveDataViaNetwork.receiveDoctor();
                    User u = recieveDataViaNetwork.recieveUser();
                    System.out.println(u.toString());

                    // Agregar al usuario y al doctor
                    userManager.addUser(u.getEmail(), new String(u.getPassword()), 2); // Role ID 2 para doctores
                    Integer user_id = userManager.getIdFromEmail(u.getEmail());
                    doctorManager.addDoctor(doctor.getName(), doctor.getSurname(), doctor.getDob(), u.getEmail(),user_id);

                    clientDoctorMenu(doctor); // Redirigir al menú del doctor
                    break;
                }
                case 2: { // Log in como doctor
                    User u = recieveDataViaNetwork.recieveUser();
                    User user = userManager.checkPassword(u.getEmail(), new String(u.getPassword()));

                    if (user != null) {
                        Doctor doctor = doctorManager.getDoctorFromUser(user.getId());
                        System.out.println(doctor.toString());
                        sendDataViaNetwork.sendDoctor(doctor);
                        clientDoctorMenu(doctor); // Redirigir al menú del doctor
                    } else {
                        Doctor doctor = new Doctor("name", "surname", LocalDate.of(1, 1, 1), "email");
                        sendDataViaNetwork.sendDoctor(doctor);
                    }

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
    }

    private static void clientDoctorMenu(Doctor doctor_logedIn) throws IOException {
        boolean menu = true;
        while (menu) {
            int option = recieveDataViaNetwork.receiveInt();
            Patient patient = null;
            switch (option) {
                case 1: // Mostrar lista de pacientes y elegir uno para ver detalles
                    viewDetailsOfPatient(doctor_logedIn);
                    break;
                case 2: // Interpretar datos enviados por el paciente y devolver una respuesta
                    makeAnInterpretation(doctor_logedIn);
                    break;
                case 3: // Log out
                    menu = false;
                    break;
                default:
                    System.out.println("That number is not an option, try again");
                    break;
            }
        }
    }

    private static void viewDetailsOfPatient(Doctor doctor_logedIn) throws IOException {
        Patient patient = null;
        List<Patient> patients = patientManager.getPatientsByDoctorId(doctor_logedIn.getDoctor_id());
        int size = patients.size();
        sendDataViaNetwork.sendInt(size);
        if(size > 0) {
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
    }

    private static void makeAnInterpretation(Doctor doctor_logedIn) throws IOException {
        List<Patient> patients = patientManager.getPatientsByDoctorId(doctor_logedIn.getDoctor_id());
        int length = patients.size();
        sendDataViaNetwork.sendInt(length);
        if(length > 0) {
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
                    LinkedList<Symptoms> symptoms =interpretationManager.getSymptomsFromInterpretation(interpretationIndex);
                    int size_symptoms = symptoms.size();
                    sendDataViaNetwork.sendInt(size_symptoms);
                    if (size_symptoms > 0) {
                        for (Symptoms symptom : symptoms) {
                            sendDataViaNetwork.sendStrings(symptom.getName());
                        }
                    }
                }
                String interpretation2 = recieveDataViaNetwork.receiveString();
                interpretation.setInterpretation(interpretation2);
                interpretationManager.setInterpretation(interpretation2, interpretation.getId());
                System.out.println(interpretation.toString());
            }
        }
    }

    public static void clientPatientMenu(Patient patient_logedIn) throws IOException {
        int option;
        boolean menu = true;
        ArrayList<Integer> patientSymptomsID = new ArrayList<>();
        while(menu){
            option = recieveDataViaNetwork.receiveInt();
            switch(option){
                case 1:{
                    patientSymptomsID =  readSymptoms();
                    break;
                }
                case 2:{
                    break;
                }
                case 3:{
                    break;
                }
                case 4:{
                    seeYourReports(patient_logedIn);
                    break;
                }
                case 5:{
                    menu = false;
                    recieveInterpretationAndlogOut(patientSymptomsID);
                    break;
                }
                default:
                    System.out.println("That number is not an option, try again");
                    break;
            }

        }
    }

    private static ArrayList<Integer> readSymptoms() throws IOException {
        ArrayList<Integer> patientSymptomsID = new ArrayList<>();
        ArrayList<Symptoms> symptoms = symptomsManager.readSymptoms();
        for(int i = 0; i < symptoms.size(); i++)
        {
            sendDataViaNetwork.sendStrings(symptoms.get(i).getName());
        }
        sendDataViaNetwork.sendStrings("stop");
        sendDataViaNetwork.sendStrings("Type the numbers corresponding to the symptoms you have (To stop adding symptoms type '0'): ");

        int symptomId = 1;
        while(symptomId != 0){
            symptomId = recieveDataViaNetwork.receiveInt();
            if(symptomId != 0) {
                System.out.println("Symptoms ids: " + symptomId);
                patientSymptomsID.add(symptomId);
            }
        }
        sendDataViaNetwork.sendStrings("Your symptoms have been recorded correctly!");
        return patientSymptomsID;
    }

    private static void seeYourReports(Patient patient_logedIn) throws IOException {
        LinkedList<Interpretation> allInterpretations = interpretationManager.getInterpretationsFromPatient_Id(patient_logedIn.getPatient_id());
        int length = allInterpretations.size();
        sendDataViaNetwork.sendInt(length);
        LinkedList<Symptoms> allSymptoms = new LinkedList<>();
        int lengthSymptom;
        for(int i=0; i < length; i++){
            lengthSymptom = 0;
            sendDataViaNetwork.sendInterpretation(allInterpretations.get(i));
            allSymptoms = interpretationManager.getSymptomsFromInterpretation(allInterpretations.get(i).getId());
            if(!allSymptoms.isEmpty()){
                lengthSymptom = allSymptoms.size();
            }
            sendDataViaNetwork.sendInt(lengthSymptom);
            if(lengthSymptom != 0){
                for(int j=0; j < lengthSymptom; j++){
                    sendDataViaNetwork.sendStrings(allSymptoms.get(j).getName());
                    System.out.println("Sent: " + allSymptoms.get(j).getName());
                }
            }
        }
        System.out.println(recieveDataViaNetwork.receiveString());
    }

    private static void recieveInterpretationAndlogOut(ArrayList<Integer> patientSymptomsID) throws IOException {
        Interpretation interpretation = recieveDataViaNetwork.recieveInterpretation();

        if(interpretation != null) {
            sendDataViaNetwork.sendStrings("OK");
            if(interpretation.getSignalEDA().getValues().isEmpty() && interpretation.getSignalEMG().getValues().isEmpty()
                    && patientSymptomsID.isEmpty()) {
                System.out.println("The report is empty, not added");
            }else{
                interpretationManager.addInterpretation(interpretation);
                int interpretation_id = interpretationManager.getId(interpretation.getDate(), interpretation.getPatient_id());
                for(int i = 0; i < patientSymptomsID.size(); i++){
                    System.out.println(patientSymptomsID.get(i));
                    interpretationManager.assignSymtomsToInterpretation(interpretation_id, patientSymptomsID.get(i));
                }
            }
        }else{
            sendDataViaNetwork.sendStrings("NOTOKAY");
        }
        System.out.println(interpretation);
    }

    private static void releaseResources(){
        sendDataViaNetwork.releaseResources();
        recieveDataViaNetwork.releaseResources();
    }
}
