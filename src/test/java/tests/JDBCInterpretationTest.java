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

        boolean result = interpretationManager.addInterpretation(interpretation);


        assertTrue(result, "La interpretación debería haberse añadido correctamente");
    }



    @Test
    void getInterpretationsFromPatient_Id() {
        LocalDate date = LocalDate.now();
        Interpretation interpretation = new Interpretation(date, "Regular check-up required.");
        interpretationManager.addInterpretation(interpretation);

        LinkedList<Interpretation> interpretations = interpretationManager.getInterpretationsFromPatient_Id(1);

        assertNotNull(interpretations, "Interpretations should not be null.");
        assertFalse(interpretations.isEmpty(), "There should be at least one interpretation for the patient.");
        assertEquals("Regular check-up required.", interpretations.getLast().getInterpretation(),
                "The last interpretation should match the inserted value.");
    }

    @Test
    void getInterpretationsFromDoctor_Id() {
        LocalDate date = LocalDate.now();
        Interpretation interpretation = new Interpretation(date, "Patient requires therapy.");
        interpretationManager.addInterpretation(interpretation);

        LinkedList<Interpretation> interpretations = interpretationManager.getInterpretationsFromDoctor_Id(2);

        assertNotNull(interpretations, "Interpretations should not be null.");
        assertFalse(interpretations.isEmpty(), "There should be at least one interpretation for the doctor.");
        assertEquals("Patient requires therapy.", interpretations.getLast().getInterpretation(),
                "The last interpretation should match the inserted value.");
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

            LocalDate date = LocalDate.now();
            Interpretation interpretation =new Interpretation(date,"patient is improving.");
            //int interpretation_id = interpretationManager.getId()
            interpretationManager.assignSymtomsToInterpretation(patient_id, 1);
            interpretationManager.assignSymtomsToInterpretation(patient_id, 2);

            ArrayList<String> symptoms = symptomManager.getSymptomsForPatient(patient_id);
            assertEquals(2, symptoms.size());
            assertTrue(symptoms.contains("Fever"));
            assertTrue(symptoms.contains("Headache"));
        }
    }

