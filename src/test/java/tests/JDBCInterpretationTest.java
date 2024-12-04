package tests;

import Pojos.*;
import jdbcs.*;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JDBCInterpretationTest {
    private static JDBCManager manager;
    private static JDBCUser userManager;
    private static JDBCPatient patientManager;
    private static JDBCSymptoms symptomManager;
    private static JDBCRole roleManager;
    private static JDBCDoctor doctorManager;
    private static JDBCInterpretation interpretationManager;

    @BeforeAll
    static void beforeAll() {
        manager = new JDBCManager();
        roleManager = new JDBCRole(manager);
        userManager = new JDBCUser(manager, roleManager);
        patientManager = new JDBCPatient(manager);
        doctorManager = new JDBCDoctor(manager);
        symptomManager = new JDBCSymptoms(manager);
        interpretationManager = new JDBCInterpretation(manager);
        try {
            manager.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void afterAll() {
        if (manager != null) {
            try {
                manager.getConnection().setAutoCommit(true);
                manager.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @BeforeEach
    void setUp() throws SQLException {
        manager.clearTables(); // Assuming this clears test-related data
        assertNotNull(interpretationManager);
    }

    @AfterEach
    void tearDown() {
        if (manager.getConnection() != null) {
            try {
                manager.getConnection().rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void AddInterpretation() {
        Role role = new Role(1,"patient");
        Role role2 = new Role(2, "doctor");

        User u = new User("juan.perez@example.com", "password".getBytes(), role);
        User u2 = new User("dr.garcia@example.com", "password".getBytes(), role2);

        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        userManager.addUser(u2.getEmail(), new String(u2.getPassword()), u2.getRole().getId());

        LocalDate dob = LocalDate.of(1993, 8, 25);
        LocalDate dob2 = LocalDate.of(1990, 6, 2);

        int id = userManager.getIdFromEmail("juan.perez@example.com");
        int id2= userManager.getIdFromEmail("dr.garcia@example.com");

        doctorManager.addDoctor("Dr","garcia", dob2, "dr.garcia@example.com", id2);
        int doctor_id =doctorManager.getId("Dr");

        patientManager.addPatient("Juan", "Perez", dob, "juan.perez@example.com", doctor_id, id);
        int patient_id= patientManager.getId("Juan");

        Signal signalEMG = new Signal(Arrays.asList(1, 2, 3, 4, 5), Signal.SignalType.EMG);
        Signal signalEDA = new Signal(Arrays.asList(6, 7, 8, 9, 10), Signal.SignalType.EDA);

        Interpretation interpretation = new Interpretation(LocalDate.now(),"Interpretación de prueba",  signalEMG,  signalEDA, patient_id,  doctor_id,"Observaciones adicionales");                      // Fecha

         interpretationManager.addInterpretation(interpretation);

    }



    @Test
    void getInterpretationsFromPatient_Id() {
        Role role = new Role(1, "patient");
        Role role2 = new Role(2, "doctor");

        User u = new User("juan.perez@example.com", "password".getBytes(), role);
        User u2 = new User("dr.garcia@example.com", "password".getBytes(), role2);

        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        userManager.addUser(u2.getEmail(), new String(u2.getPassword()), u2.getRole().getId());

        LocalDate dob = LocalDate.of(1993, 8, 25);
        LocalDate dob2 = LocalDate.of(1990, 6, 2);

        int id = userManager.getIdFromEmail("juan.perez@example.com");
        int id2 = userManager.getIdFromEmail("dr.garcia@example.com");

        doctorManager.addDoctor("Dr", "Garcia", dob2, "dr.garcia@example.com", id2);
        int doctor_id = doctorManager.getId("Dr");

        patientManager.addPatient("Juan", "Perez", dob, "juan.perez@example.com", doctor_id, id);
        int patient_id = patientManager.getId("Juan");

        Signal signalEMG = new Signal(Arrays.asList(1, 2, 3, 4, 5), Signal.SignalType.EMG);
        Signal signalEDA = new Signal(Arrays.asList(6, 7, 8, 9, 10), Signal.SignalType.EDA);

        // Insertar una interpretación para el paciente
        Interpretation interpretation = new Interpretation(LocalDate.now(), "Interpretación de prueba", signalEMG, signalEDA, patient_id, doctor_id, "Observaciones adicionales");
        interpretationManager.addInterpretation(interpretation);

        // Ejecutar el método que se va a probar
        LinkedList<Interpretation> interpretations = interpretationManager.getInterpretationsFromPatient_Id(patient_id);

        // Verificar los resultados
        assertNotNull(interpretations, "La lista de interpretaciones no debe ser nula.");
        assertEquals(1, interpretations.size(), "Debe haber una interpretación.");
        Interpretation result = interpretations.get(0);
        assertEquals("Interpretación de prueba", result.getInterpretation(), "La interpretación debe coincidir.");
        assertEquals(patient_id, result.getPatient_id(), "El ID del paciente debe coincidir.");
        assertEquals(doctor_id, result.getDoctor_id(), "El ID del doctor debe coincidir.");
    }

    @Test
    void getInterpretationsFromDoctor_Id() {
        Role role = new Role(1, "patient");
        Role role2 = new Role(2, "doctor");

        User u = new User("juan.perez@example.com", "password".getBytes(), role);
        User u2 = new User("dr.garcia@example.com", "password".getBytes(), role2);

        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        userManager.addUser(u2.getEmail(), new String(u2.getPassword()), u2.getRole().getId());

        LocalDate dob = LocalDate.of(1993, 8, 25);
        LocalDate dob2 = LocalDate.of(1990, 6, 2);

        int id = userManager.getIdFromEmail("juan.perez@example.com");
        int id2 = userManager.getIdFromEmail("dr.garcia@example.com");

        doctorManager.addDoctor("Dr", "Garcia", dob2, "dr.garcia@example.com", id2);
        int doctor_id = doctorManager.getId("Dr");

        patientManager.addPatient("Juan", "Perez", dob, "juan.perez@example.com", doctor_id, id);
        int patient_id = patientManager.getId("Juan");

        Signal signalEMG = new Signal(Arrays.asList(1, 2, 3, 4, 5), Signal.SignalType.EMG);
        Signal signalEDA = new Signal(Arrays.asList(6, 7, 8, 9, 10), Signal.SignalType.EDA);

        // Insertar una interpretación para el paciente
        Interpretation interpretation = new Interpretation(LocalDate.now(), "Interpretación de prueba", signalEMG, signalEDA, patient_id, doctor_id, "Observaciones adicionales");
        interpretationManager.addInterpretation(interpretation);

        // Ejecutar el método que se va a probar
        LinkedList<Interpretation> interpretations = interpretationManager.getInterpretationsFromDoctor_Id(doctor_id);

        // Verificar los resultados
        assertNotNull(interpretations, "La lista de interpretaciones no debe ser nula.");
        assertEquals(1, interpretations.size(), "Debe haber una interpretación.");
        Interpretation result = interpretations.get(0);
        assertEquals("Interpretación de prueba", result.getInterpretation(), "La interpretación debe coincidir.");
        assertEquals(patient_id, result.getPatient_id(), "El ID del paciente debe coincidir.");
        assertEquals(doctor_id, result.getDoctor_id(), "El ID del doctor debe coincidir.");
    }


    @Test
    void assignSymtomsToInterpretation() {

            Role role = new Role(1, "patient");
            Role role2 = new Role(2, "doctor");

            User u = new User("leo.messi@example.com", "password".getBytes(), role);
            User u2 = new User("vini.junior@example.com", "password".getBytes(), role2);

            userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
            userManager.addUser(u2.getEmail(), new String(u2.getPassword()), u2.getRole().getId());
            Symptoms symptom1 = new Symptoms(1, "Fever");
            Symptoms symptom2 = new Symptoms(2, "Headache");
            symptomManager.addSymptom(symptom1);
            symptomManager.addSymptom(symptom2);


            LocalDate dob = LocalDate.of(2000, 1, 1);
            LocalDate dob2 = LocalDate.of(2004, 1, 2);
            int id = userManager.getIdFromEmail("leo.messi@example.com");
            int id2 = userManager.getIdFromEmail("vini.junior@example.com");

            doctorManager.addDoctor("Vini", "Junior", dob2, "vini.junior@example.com", id2);
            int doctor_id = doctorManager.getId("Vini");
            patientManager.addPatient("Leo", "Messi", dob, "leo.messi@example.com", doctor_id, id);
            int patient_id = patientManager.getId("Leo");

            Signal signalEMG = new Signal(Arrays.asList(1, 2, 3, 4, 5), Signal.SignalType.EMG);
            Signal signalEDA = new Signal(Arrays.asList(6, 7, 8, 9, 10), Signal.SignalType.EDA);

            LocalDate date = LocalDate.now();

            Interpretation interpretation =new Interpretation(date,"patient is improving.",signalEMG,signalEDA,patient_id,doctor_id,"Nice job");
            interpretationManager.addInterpretation(interpretation);
            System.out.println(interpretation);
            int interpretation_id = interpretationManager.getId(date,patient_id);
            interpretationManager.assignSymtomsToInterpretation(interpretation_id, 1);
            interpretationManager.assignSymtomsToInterpretation(interpretation_id, 2);



        }
    @Test
    void getId() {
        Role role = new Role(1, "patient");
        Role role2 = new Role(2, "doctor");
        User u = new User("Ibai.llanos@example.com", "password".getBytes(), role);
        User u2 = new User("Ale.galan@example.com", "password".getBytes(), role2);

        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        userManager.addUser(u2.getEmail(), new String(u2.getPassword()), u2.getRole().getId());

        LocalDate dob = LocalDate.of(1990, 1, 1);
        LocalDate dob2 = LocalDate.of(1996, 6, 2);

        int id = userManager.getIdFromEmail("Ibai.llanos@example.com");
        int id2 = userManager.getIdFromEmail("Ale.galan@example.com");

        doctorManager.addDoctor("Ale","Galan", dob, "Ale.galan@example.com", id2);
        int doctor_id = doctorManager.getId("Ale");

        patientManager.addPatient("Ibai", "Llanos", dob2, "Ibai.llanos@example.com", doctor_id,  id);
        int patient_id = patientManager.getId("Ibai");

        Signal signalEMG = new Signal(Arrays.asList(1, 2, 3, 4, 5), Signal.SignalType.EMG);
        Signal signalEDA = new Signal(Arrays.asList(6, 7, 8, 9, 10), Signal.SignalType.EDA);

        LocalDate date = LocalDate.now();

        Interpretation interpretation =new Interpretation(date,"patient is improving.",signalEMG,signalEDA,patient_id,doctor_id,"Nice job");
        interpretationManager.addInterpretation(interpretation);
        int interpretation_id = interpretationManager.getId(date,patient_id);
        System.out.println(interpretation_id);
        assertTrue(interpretation_id > 0);
    }
    @Test
    void getSymptomsFromInterpretation() {
        Role role = new Role(1, "patient");
        Role role2 = new Role(2, "doctor");

        User u = new User("leo.messi@example.com", "password".getBytes(), role);
        User u2 = new User("vini.junior@example.com", "password".getBytes(), role2);

        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        userManager.addUser(u2.getEmail(), new String(u2.getPassword()), u2.getRole().getId());
        Symptoms symptom1 = new Symptoms(1, "Fever");
        Symptoms symptom2 = new Symptoms(2, "Headache");
        symptomManager.addSymptom(symptom1);
        symptomManager.addSymptom(symptom2);


        LocalDate dob = LocalDate.of(2000, 1, 1);
        LocalDate dob2 = LocalDate.of(2004, 1, 2);
        int id = userManager.getIdFromEmail("leo.messi@example.com");
        int id2 = userManager.getIdFromEmail("vini.junior@example.com");

        doctorManager.addDoctor("Vini", "Junior", dob2, "vini.junior@example.com", id2);
        int doctor_id = doctorManager.getId("Vini");
        patientManager.addPatient("Leo", "Messi", dob, "leo.messi@example.com", doctor_id, id);
        int patient_id = patientManager.getId("Leo");

        Signal signalEMG = new Signal(Arrays.asList(1, 2, 3, 4, 5), Signal.SignalType.EMG);
        Signal signalEDA = new Signal(Arrays.asList(6, 7, 8, 9, 10), Signal.SignalType.EDA);

        LocalDate date = LocalDate.now();

        Interpretation interpretation =new Interpretation(date,"patient is improving.",signalEMG,signalEDA,patient_id,doctor_id,"Nice job");
        interpretationManager.addInterpretation(interpretation);
        System.out.println(interpretation);
        int interpretation_id = interpretationManager.getId(date,patient_id);
        interpretationManager.assignSymtomsToInterpretation(interpretation_id, 1);
        interpretationManager.assignSymtomsToInterpretation(interpretation_id, 2);
        LinkedList<Symptoms> symptoms = interpretationManager.getSymptomsFromInterpretation(interpretation_id);
        System.out.println(symptoms);



    }
    @Test
    void  getInterpretationFromId(){
        Role role = new Role(1, "patient");
        Role role2 = new Role(2, "doctor");
        User u = new User("Ibai.llanos@example.com", "password".getBytes(), role);
        User u2 = new User("Ale.galan@example.com", "password".getBytes(), role2);

        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        userManager.addUser(u2.getEmail(), new String(u2.getPassword()), u2.getRole().getId());

        LocalDate dob = LocalDate.of(1990, 1, 1);
        LocalDate dob2 = LocalDate.of(1996, 6, 2);

        int id = userManager.getIdFromEmail("Ibai.llanos@example.com");
        int id2 = userManager.getIdFromEmail("Ale.galan@example.com");

        doctorManager.addDoctor("Ale","Galan", dob, "Ale.galan@example.com", id2);
        int doctor_id = doctorManager.getId("Ale");

        patientManager.addPatient("Ibai", "Llanos", dob2, "Ibai.llanos@example.com", doctor_id,  id);
        int patient_id = patientManager.getId("Ibai");

        Signal signalEMG = new Signal(Arrays.asList(1, 2, 3, 4, 5), Signal.SignalType.EMG);
        Signal signalEDA = new Signal(Arrays.asList(6, 7, 8, 9, 10), Signal.SignalType.EDA);

        LocalDate date = LocalDate.now();

        Interpretation interpretation = new Interpretation(date,"patient is improving.",signalEMG,signalEDA,patient_id,doctor_id,"Nice job");
        interpretationManager.addInterpretation(interpretation);
        int interpretation_id = interpretationManager.getId(date,patient_id);
        Interpretation interpretation1 = interpretationManager.getInterpretationFromId(interpretation_id);
        System.out.println(interpretation1);
    }
}

