package ui;

import Pojos.Doctor;
import Pojos.Patient;
import Pojos.Symptoms;
import Pojos.User;
import ReceiveData.ReceiveDataViaNetwork;

import java.io.*;
import java.net.Socket;
import java.sql.SQLOutput;
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

    public UserMenu(Socket socket, JDBCManager manager){
        this.socket = socket;
        this.manager = manager;
        this.patientManager = new JDBCPatient(manager);
        this.roleManager = new JDBCRole(manager);
        this.userManager = new JDBCUser(manager, roleManager);
        this.symptomsManager = new JDBCSymptoms(manager);
        this.doctorManager = new JDBCDoctor(manager);
    }

    @Override
    public void run() {
        try{
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //InputStream inputStream = socket.getInputStream();
            dataInputStream = new DataInputStream(socket.getInputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            printWriter = new PrintWriter(socket.getOutputStream(), true);

            String option;

            System.out.println("Socket accepted");

            int message = ReceiveDataViaNetwork.receiveInt(socket, dataInputStream);
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
            int option = ReceiveDataViaNetwork.receiveInt(socket, dataInputStream);
            switch (option) {
                case 1 : {
                    patient = ReceiveDataViaNetwork.recievePatient(socket, dataInputStream);
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
                        SendDataViaNetwork.sendStrings("SUCCESS", printWriter);
                        clientPatientMenu(patient);
                    }else{
                        SendDataViaNetwork.sendStrings("ERROR", printWriter);
                    }


                    break;
                }
                case 2 :{
                    User u = ReceiveDataViaNetwork.recieveUser(dataInputStream);
                    User user = userManager.checkPassword(u.getEmail(), new String(u.getPassword()));

                    if(user != null){
                        patient = patientManager.getPatientFromUser(user.getId());
                        System.out.println(patient.toString());
                        SendDataViaNetwork.sendPatient(patient, dataOutputStream);
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
            int option = ReceiveDataViaNetwork.receiveInt(socket, dataInputStream);
            switch (option) {
                case 1: { // Registrar nuevo doctor
                    doctor = ReceiveDataViaNetwork.receiveDoctor(socket, dataInputStream);
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
            int option = ReceiveDataViaNetwork.receiveInt(socket, dataInputStream);

            switch (option) {
                case 1: // Mostrar lista de pacientes y elegir uno para ver detalles
                    SendDataViaNetwork.sendInt(doctor_logedIn.getPatients().size(),dataOutputStream);
                    List<Patient> patients = patientManager.getPatientsByDoctorId(doctor_logedIn.getDoctor_id());
                    for (Patient patient : patients) {
                        SendDataViaNetwork.sendPatient(patient, dataOutputStream);
                    }
                    int patientId = ReceiveDataViaNetwork.receiveInt(socket, dataInputStream);
                    Patient patient = patientManager.getPatientFromId(patientId); //hay que hacer un getPatientFromId para pacientes que ya han grabado datos y otro para los que no
                    if (patient != null) {
                        SendDataViaNetwork.sendPatient(patient, dataOutputStream);
                    }
                    break;
                case 2: // Interpretar datos enviados por el paciente y devolver una respuesta
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
        ArrayList<Symptoms> symptoms = new ArrayList<>();
        while(menu){
            option = ReceiveDataViaNetwork.receiveInt(socket, dataInputStream);
            switch(option){
                case 1:{
                    symptoms = symptomsManager.readSymptoms();
                    for(int i = 0; i < symptoms.size(); i++)
                    {
                        SendDataViaNetwork.sendStrings(symptoms.get(i).getName(), printWriter);
                    }
                    SendDataViaNetwork.sendStrings("stop", printWriter);
                    SendDataViaNetwork.sendStrings("Type the numbers corresponding to the symptoms you have (To stop adding symptoms type '0'): ", printWriter);

                    int symptomId = 1;
                    while(symptomId != 0){
                        symptomId = ReceiveDataViaNetwork.receiveInt(socket,dataInputStream);
                        if(symptomId != 0) {
                            System.out.println("Symptoms ids: " + symptomId);
                            patientManager.assignSymtomsToPatient(patient.getPatient_id(), symptomId);
                        }
                    }
                    SendDataViaNetwork.sendStrings("Your symptoms have been recorded correctly!", printWriter);
                    //System.out.println("Lol");
                    break;
                }
                case 2:{
                    ReceiveDataViaNetwork.receiveInt(socket,dataInputStream);
                    ReceiveDataViaNetwork.recieveValues(patient_logedIn, dataInputStream);
                    System.out.println(patient_logedIn.toString());
                    break;
                }
                case 3:{
                    break;
                }
                case 4:{
                    menu = false;
                    //System.exit(0);
                    break;
                }
                default:
                    System.out.println("That number is not an option, try again");
                    break;
            }

        }
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
