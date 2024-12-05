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
    private static ArrayList<Integer> patientSymptoms;

    public UserMenu(Socket socket, JDBCManager manager){
        this.socket = socket;
        this.manager = manager;
        this.patientManager = new JDBCPatient(manager);
        this.roleManager = new JDBCRole(manager);
        this.userManager = new JDBCUser(manager, roleManager);
        this.symptomsManager = new JDBCSymptoms(manager);
        this.doctorManager = new JDBCDoctor(manager);
        this.interpretationManager = new JDBCInterpretation(manager);
    }

    @Override
    public void run() {
        try{
            patientSymptoms = new ArrayList<>();

            System.out.println("Socket accepted");

            int message = ReceiveDataViaNetwork.receiveInt(socket);
            if(message == 1){
                patientMenu();
            } else if (message == 2) {
                doctorMenu();

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void patientMenu() throws IOException
    {
        boolean menu = true;

        while(menu){
            int option = ReceiveDataViaNetwork.receiveInt(socket);
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
        Patient patient = ReceiveDataViaNetwork.recievePatient(socket);
        User u = ReceiveDataViaNetwork.recieveUser(socket);
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
            SendDataViaNetwork.sendStrings("SUCCESS", socket);
            SendDataViaNetwork.sendPatient(patient, socket);
            Doctor doctor = doctorManager.getDoctorFromId(doctor_id);
            SendDataViaNetwork.sendDoctor(doctor, socket);
            clientPatientMenu(patient);
        }else{
            SendDataViaNetwork.sendStrings("ERROR", socket);
        }
    }

    private static void patientLogIn() throws IOException {
        User u = ReceiveDataViaNetwork.recieveUser(socket);
        User user = userManager.checkPassword(u.getEmail(), new String(u.getPassword()));

        if(user != null){
            Patient patient = patientManager.getPatientFromUser(userManager.getIdFromEmail(u.getEmail()));
            System.out.println(patient.toString());
            SendDataViaNetwork.sendPatient(patient, socket);
            Doctor doctor = doctorManager.getDoctorFromId(patient.getDoctor_id());
            SendDataViaNetwork.sendDoctor(doctor, socket);
            clientPatientMenu(patient);
        }else{
            Patient patient = new Patient( "name", "surname", LocalDate.of(1,1,1), "email");
            SendDataViaNetwork.sendPatient(patient, socket);
        }
    }

    private static void doctorMenu() throws IOException {
        boolean menu = true;

        while(menu){
            int option = ReceiveDataViaNetwork.receiveInt(socket);
            switch (option) {
                case 1: { // Registrar nuevo doctor
                    Doctor doctor = ReceiveDataViaNetwork.receiveDoctor(socket);
                    User u = ReceiveDataViaNetwork.recieveUser(socket);
                    System.out.println(u.toString());

                    // Agregar al usuario y al doctor
                    userManager.addUser(u.getEmail(), new String(u.getPassword()), 2); // Role ID 2 para doctores
                    Integer user_id = userManager.getIdFromEmail(u.getEmail());
                    doctorManager.addDoctor(doctor.getName(), doctor.getSurname(), doctor.getDob(), u.getEmail(),user_id);

                    clientDoctorMenu(doctor); // Redirigir al menú del doctor
                    break;
                }
                case 2: { // Log in como doctor
                    User u = ReceiveDataViaNetwork.recieveUser(socket);
                    User user = userManager.checkPassword(u.getEmail(), new String(u.getPassword()));

                    if (user != null) {
                        Doctor doctor = doctorManager.getDoctorFromUser(user.getId());
                        System.out.println(doctor.toString());
                        SendDataViaNetwork.sendDoctor(doctor, socket);
                        clientDoctorMenu(doctor); // Redirigir al menú del doctor
                    } else {
                        Doctor doctor = new Doctor("name", "surname", LocalDate.of(1, 1, 1), "email");
                        SendDataViaNetwork.sendDoctor(doctor, socket);
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
            int option = ReceiveDataViaNetwork.receiveInt(socket);
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
        SendDataViaNetwork.sendInt(size,socket);
        if(size > 0) {
            for (Patient patientSelected : patients) {
                SendDataViaNetwork.sendPatient(patientSelected, socket);
                System.out.println("Patient " + patientSelected.getPatient_id() + " sent");
            }
            int patientId = ReceiveDataViaNetwork.receiveInt(socket);
            patient = patients.get(patientId); //hay que hacer un getPatientFromId para pacientes que ya han grabado datos y otro para los que no
        }
        if (patient != null) {
            SendDataViaNetwork.sendPatient(patient, socket);
        }
    }

    private static void makeAnInterpretation(Doctor doctor_logedIn) throws IOException {
        List<Patient> patients = patientManager.getPatientsByDoctorId(doctor_logedIn.getDoctor_id());
        int length = patients.size();
        SendDataViaNetwork.sendInt(length,socket);
        if(length > 0) {
            for (Patient patient2 : patients) {
                SendDataViaNetwork.sendPatient(patient2, socket);
            }
            int patientId2 = ReceiveDataViaNetwork.receiveInt(socket);
            Patient patient2 = patients.get(patientId2);
            LinkedList<Interpretation> interpretations = interpretationManager.getInterpretationsFromPatient_Id(patient2.getPatient_id());
            if (interpretations.isEmpty()) {
                SendDataViaNetwork.sendStrings("ERROR", socket);
            } else {
                SendDataViaNetwork.sendStrings("OKAY", socket);
                SendDataViaNetwork.sendInt(interpretations.size(), socket);
                for (Interpretation interpretation : interpretations) {
                    SendDataViaNetwork.sendInterpretation(interpretation, socket);
                }
                int interpretationIndex = ReceiveDataViaNetwork.receiveInt(socket);
                Interpretation interpretation = interpretations.get(interpretationIndex);
                if (interpretation != null) {
                    SendDataViaNetwork.sendInterpretation(interpretation, socket);
                    LinkedList<Symptoms> symptoms =interpretationManager.getSymptomsFromInterpretation(interpretationIndex);
                    int size_symptoms = symptoms.size();
                    SendDataViaNetwork.sendInt(size_symptoms,socket);
                    if (size_symptoms > 0) {
                        for (Symptoms symptom : symptoms) {
                            SendDataViaNetwork.sendStrings(symptom.getName(), socket);
                        }
                    }
                }
                String interpretation2 = ReceiveDataViaNetwork.receiveString(socket);
                interpretation.setInterpretation(interpretation2);
                interpretationManager.setInterpretation(interpretation2, interpretation.getId());
                System.out.println(interpretation.toString());
            }
        }
    }

    public static void clientPatientMenu(Patient patient_logedIn) throws IOException {
        int option;
        boolean menu = true;

        while(menu){
            option = ReceiveDataViaNetwork.receiveInt(socket);
            switch(option){
                case 1:{
                    readSymptoms();
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
                    recieveInterpretationAndlogOut();
                    break;
                }
                default:
                    System.out.println("That number is not an option, try again");
                    break;
            }

        }
    }

    private static void readSymptoms() throws IOException {
        ArrayList<Symptoms> symptoms = symptomsManager.readSymptoms();
        for(int i = 0; i < symptoms.size(); i++)
        {
            SendDataViaNetwork.sendStrings(symptoms.get(i).getName(), socket);
        }
        SendDataViaNetwork.sendStrings("stop", socket);
        SendDataViaNetwork.sendStrings("Type the numbers corresponding to the symptoms you have (To stop adding symptoms type '0'): ", socket);

        int symptomId = 1;
        while(symptomId != 0){
            symptomId = ReceiveDataViaNetwork.receiveInt(socket);
            if(symptomId != 0) {
                System.out.println("Symptoms ids: " + symptomId);
                patientSymptoms.add(symptomId);
            }
        }
        SendDataViaNetwork.sendStrings("Your symptoms have been recorded correctly!", socket);
    }

    private static void seeYourReports(Patient patient_logedIn) throws IOException {
        LinkedList<Interpretation> allInterpretations = interpretationManager.getInterpretationsFromPatient_Id(patient_logedIn.getPatient_id());
        int length = allInterpretations.size();
        SendDataViaNetwork.sendInt(length, socket);
        LinkedList<Symptoms> allSymptoms = new LinkedList<>();
        int lengthSymptom;
        for(int i=0; i < length; i++){
            lengthSymptom = 0;
            SendDataViaNetwork.sendInterpretation(allInterpretations.get(i), socket);
            allSymptoms = interpretationManager.getSymptomsFromInterpretation(allInterpretations.get(i).getId());
            if(!allSymptoms.isEmpty()){
                lengthSymptom = allSymptoms.size();
            }
            SendDataViaNetwork.sendInt(lengthSymptom,socket);
            if(lengthSymptom != 0){
                for(int j=0; j < lengthSymptom; j++){
                    SendDataViaNetwork.sendStrings(allSymptoms.get(j).getName(), socket);
                    System.out.println("Sent: " + allSymptoms.get(j).getName());
                }
            }
        }
        System.out.println(ReceiveDataViaNetwork.receiveString(socket));
    }

    private static void recieveInterpretationAndlogOut() throws IOException {
        Interpretation interpretation = ReceiveDataViaNetwork.recieveInterpretation(socket);

        if(interpretation != null) {
            SendDataViaNetwork.sendStrings("OK", socket);
            if(interpretation.getSignalEDA().getValues().isEmpty() && interpretation.getSignalEMG().getValues().isEmpty()
                    && patientSymptoms.isEmpty()) {
                System.out.println("The report is empty, not added");
            }else{
                interpretationManager.addInterpretation(interpretation);
                int interpretation_id = interpretationManager.getId(interpretation.getDate(), interpretation.getPatient_id());
                for(int i = 0; i < patientSymptoms.size(); i++){
                    interpretationManager.assignSymtomsToInterpretation(interpretation_id, patientSymptoms.get(i));
                }
            }
        }else{
            SendDataViaNetwork.sendStrings("NOTOKAY", socket);
        }
        System.out.println(interpretation);
    }

}
