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
    private static BufferedReader bufferedReader;
    private static DataInputStream dataInputStream;
    private static ObjectInputStream objectInputStream;
    private static DataOutputStream dataOutputStream;
    private static ObjectOutputStream objectOutputStream;
    private static PrintWriter printWriter;
    private static JDBCManager manager;
    private static JDBCPatient patientManager;
    private static JDBCUser userManager;
    private static JDBCRole roleManager;
    private static JDBCSymptoms symptomsManager;
    private static Patient patient;
    private static JDBCDoctor doctorManager;
    private static Doctor doctor;
    private static JDBCInterpretation interpretationManager;
    private static Interpretation interpretation;
    private static ArrayList<Integer> patientSymptoms;
    private static List<Patient> patients;

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
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataInputStream = new DataInputStream(socket.getInputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            patientSymptoms = new ArrayList<>();

            System.out.println("Socket accepted");

            int message = ReceiveDataViaNetwork.receiveInt(dataInputStream);
            if(message == 1){
                patientMenu();
            } else if (message == 2) {
                doctorMenu();

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        releaseResources(dataOutputStream, objectOutputStream, dataInputStream, objectInputStream, bufferedReader, printWriter);
    }

    private static void patientMenu() throws IOException
    {

        boolean menu = true;

        while(menu){
            int option = ReceiveDataViaNetwork.receiveInt(dataInputStream);
            switch (option) {
                case 1 : {
                    patient = ReceiveDataViaNetwork.recievePatient( dataInputStream);
                    User u = ReceiveDataViaNetwork.recieveUser(dataInputStream);
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
                        SendDataViaNetwork.sendStrings("SUCCESS", dataOutputStream);
                        SendDataViaNetwork.sendPatient(patient, dataOutputStream);
                        Doctor doctor = doctorManager.getDoctorFromId(doctor_id);
                        SendDataViaNetwork.sendDoctor(doctor, dataOutputStream);
                        clientPatientMenu(patient);
                    }else{
                        SendDataViaNetwork.sendStrings("ERROR", dataOutputStream);
                    }


                    break;
                }
                case 2 :{
                    User u = ReceiveDataViaNetwork.recieveUser(dataInputStream);
                    User user = userManager.checkPassword(u.getEmail(), new String(u.getPassword()));

                    if(user != null){
                        patient = patientManager.getPatientFromUser(userManager.getIdFromEmail(u.getEmail()));
                        System.out.println(patient.toString());
                        SendDataViaNetwork.sendPatient(patient, dataOutputStream);
                        Doctor doctor = doctorManager.getDoctorFromId(patient.getDoctor_id());
                        SendDataViaNetwork.sendDoctor(doctor, dataOutputStream);
                        clientPatientMenu(patient);
                    }else{
                        patient = new Patient( "name", "surname", LocalDate.of(1,1,1), "email");
                        SendDataViaNetwork.sendPatient(patient, dataOutputStream);
                    }


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
    private static void doctorMenu() throws IOException {

        boolean menu = true;

        while(menu){
            int option = ReceiveDataViaNetwork.receiveInt(dataInputStream);
            switch (option) {
                case 1: { // Registrar nuevo doctor
                    doctor = ReceiveDataViaNetwork.receiveDoctor(dataInputStream);
                    User u = ReceiveDataViaNetwork.recieveUser(dataInputStream);
                    System.out.println(u.toString());

                    // Agregar al usuario y al doctor
                    userManager.addUser(u.getEmail(), new String(u.getPassword()), 2); // Role ID 2 para doctores
                    Integer user_id = userManager.getIdFromEmail(u.getEmail());
                    doctorManager.addDoctor(doctor.getName(), doctor.getSurname(), doctor.getDob(), u.getEmail(),user_id);

                    clientDoctorMenu(doctor); // Redirigir al menú del doctor
                    break;
                }
                case 2: { // Log in como doctor
                    User u = ReceiveDataViaNetwork.recieveUser(dataInputStream);
                    User user = userManager.checkPassword(u.getEmail(), new String(u.getPassword()));

                    if (user != null) {
                        doctor = doctorManager.getDoctorFromUser(user.getId());
                        System.out.println(doctor.toString());
                        SendDataViaNetwork.sendDoctor(doctor, dataOutputStream);
                        clientDoctorMenu(doctor); // Redirigir al menú del doctor
                    } else {
                        doctor = new Doctor("name", "surname", LocalDate.of(1, 1, 1), "email");
                        SendDataViaNetwork.sendDoctor(doctor, dataOutputStream);
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
            int option = ReceiveDataViaNetwork.receiveInt(dataInputStream);
            Patient patient = null;
            switch (option) {
                case 1: // Mostrar lista de pacientes y elegir uno para ver detalles
                    patients = patientManager.getPatientsByDoctorId(doctor_logedIn.getDoctor_id());
                    int size = patients.size();
                    SendDataViaNetwork.sendInt(size,dataOutputStream);
                    if(size > 0) {
                        for (Patient patientSelected : patients) {
                            SendDataViaNetwork.sendPatient(patientSelected, dataOutputStream);
                            System.out.println("Patient " + patientSelected.getPatient_id() + " sent");
                        }
                        int patientId = ReceiveDataViaNetwork.receiveInt(dataInputStream);
                        patient = patients.get(patientId); //hay que hacer un getPatientFromId para pacientes que ya han grabado datos y otro para los que no
                    }
                    if (patient != null) {
                        SendDataViaNetwork.sendPatient(patient, dataOutputStream);
                    }
                    break;
                case 2: // Interpretar datos enviados por el paciente y devolver una respuesta
                    patients = patientManager.getPatientsByDoctorId(doctor_logedIn.getDoctor_id());
                    int length = patients.size();
                    SendDataViaNetwork.sendInt(length,dataOutputStream);
                    if(length > 0) {
                        for (Patient patient2 : patients) {
                            SendDataViaNetwork.sendPatient(patient2, dataOutputStream);
                        }
                        int patientId2 = ReceiveDataViaNetwork.receiveInt(dataInputStream);
                        Patient patient2 = patients.get(patientId2);
                        LinkedList<Interpretation> interpretations = interpretationManager.getInterpretationsFromPatient_Id(patient2.getPatient_id());
                        if (interpretations.isEmpty()) {
                            SendDataViaNetwork.sendStrings("ERROR", dataOutputStream);
                        } else {
                            SendDataViaNetwork.sendStrings("OKAY", dataOutputStream);
                            SendDataViaNetwork.sendInt(interpretations.size(), dataOutputStream);
                            //List<Interpretation> interpretations = interpretationManager.getInterpretationsFromPatient_Id(patient2.getPatient_id());
                            for (Interpretation interpretation : interpretations) {
                                SendDataViaNetwork.sendInterpretation(interpretation, dataOutputStream);
                            }
                            int interpretationId = ReceiveDataViaNetwork.receiveInt(dataInputStream);
                            Interpretation interpretation = interpretations.get(interpretationId);
                            if (interpretation != null) {
                                SendDataViaNetwork.sendInterpretation(interpretation, dataOutputStream);
                                LinkedList<Symptoms> symptoms = interpretationManager.getSymptomsFromInterpretation(interpretation.getId());
                                int size_symptoms = symptoms.size();
                                SendDataViaNetwork.sendInt(size_symptoms,dataOutputStream);
                                if (size_symptoms > 0) {
                                    for (Symptoms symptom : symptoms) {
                                        SendDataViaNetwork.sendStrings(symptom.getName(), dataOutputStream);
                                    }
                                }
                            }
                            String interpretation2 = ReceiveDataViaNetwork.receiveString(dataInputStream);
                            interpretation.setInterpretation(interpretation2);
                            interpretationManager.setInterpretation(interpretation2, interpretation.getId());
                            System.out.println(interpretation.toString());
                        }
                    }
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

    public static void clientPatientMenu(Patient patient_logedIn) throws IOException {

        int option;
        boolean menu = true;

        while(menu){
            option = ReceiveDataViaNetwork.receiveInt(dataInputStream);
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
                    interpretation = ReceiveDataViaNetwork.recieveInterpretation(dataInputStream);

                    if(interpretation != null) {
                        SendDataViaNetwork.sendStrings("OK", dataOutputStream);
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
                        SendDataViaNetwork.sendStrings("NOTOKAY", dataOutputStream);
                    }
                    System.out.println(interpretation);
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
            SendDataViaNetwork.sendStrings(symptoms.get(i).getName(), dataOutputStream);
        }
        SendDataViaNetwork.sendStrings("stop", dataOutputStream);
        SendDataViaNetwork.sendStrings("Type the numbers corresponding to the symptoms you have (To stop adding symptoms type '0'): ", dataOutputStream);

        int symptomId = 1;
        while(symptomId != 0){
            symptomId = ReceiveDataViaNetwork.receiveInt(dataInputStream);
            if(symptomId != 0) {
                System.out.println("Symptoms ids: " + symptomId);
                patientSymptoms.add(symptomId);
            }
        }
        SendDataViaNetwork.sendStrings("Your symptoms have been recorded correctly!", dataOutputStream);
    }

    private static void seeYourReports(Patient patient_logedIn) throws IOException {
        LinkedList<Interpretation> allInterpretations = interpretationManager.getInterpretationsFromPatient_Id(patient_logedIn.getPatient_id());
        int length = allInterpretations.size();
        SendDataViaNetwork.sendInt(length, dataOutputStream);
        LinkedList<Symptoms> allSymptoms = new LinkedList<>();
        int lengthSymptom;
        for(int i=0; i < length; i++){
            lengthSymptom = 0;
            SendDataViaNetwork.sendInterpretation(allInterpretations.get(i), dataOutputStream);
            allSymptoms = interpretationManager.getSymptomsFromInterpretation(allInterpretations.get(i).getId());
            if(!allSymptoms.isEmpty()){
                lengthSymptom = allSymptoms.size();
            }
            SendDataViaNetwork.sendInt(lengthSymptom,dataOutputStream);
            if(lengthSymptom != 0){
                for(int j=0; j < lengthSymptom; j++){
                    SendDataViaNetwork.sendStrings(allSymptoms.get(j).getName(), dataOutputStream);
                    System.out.println("Sent: " + allSymptoms.get(j).getName());
                }
            }
        }
        System.out.println(ReceiveDataViaNetwork.receiveString(dataInputStream));
    }

    private static void releaseResources(DataOutputStream dataOutputStream, ObjectOutputStream objectOutputStream, DataInputStream dataInputStream, ObjectInputStream objectInputStream, BufferedReader bufferedReader, PrintWriter printWriter){
        try {
            objectOutputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            dataOutputStream.close();
        } catch (IOException ex) {
            //Logger.getLogger(SendBinaryDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        try {
            objectInputStream.close();
        } catch (IOException ex) {
            //Logger.getLogger(ReceiveClientViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        try {
            dataInputStream.close();
        } catch (IOException ex) {
            //Logger.getLogger(ReceiveBinaryDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        try {
            bufferedReader.close();
        } catch (IOException ex) {
            //Logger.getLogger(ReceiveStringsViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
        printWriter.close();
    }

}
