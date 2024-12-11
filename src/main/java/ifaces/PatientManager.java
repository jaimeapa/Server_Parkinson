package ifaces;

import Pojos.Patient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The PatientManager interface provides methods for managing patient-related data.
 * It allows reading a list of patients, adding new patients, and retrieving or removing patients by various identifiers.
 */
public interface PatientManager {

    /**
     * Reads the list of all registered patients.
     *
     * @return an ArrayList containing all patients.
     */
    ArrayList<Patient> readPatients();

    /**
     * Adds a new patient to the system.
     *
     * @param name the first name of the patient.
     * @param surname the last name of the patient.
     * @param dob the date of birth of the patient.
     * @param email the email address of the patient.
     * @param doctor_id the ID of the doctor associated with the patient.
     * @param user_id the unique user ID associated with the patient.
     */
    void addPatient(String name, String surname, LocalDate dob, String email, int doctor_id, int user_id);

    /**
     * Retrieves the unique ID of a patient based on their name.
     *
     * @param name the name of the patient.
     * @return the unique ID associated with the patient.
     */
    int getId(String name);

    /**
     * Retrieves the unique ID of a patient based on their email address.
     *
     * @param email the email address of the patient.
     * @return the unique ID associated with the email, or null if not found.
     */
    Integer emailToId(String email);

    /**
     * Retrieves a patient based on the associated user ID.
     *
     * @param user_id the unique user ID associated with the patient.
     * @return the Patient object associated with the specified user ID.
     */
    Patient getPatientFromUser(int user_id);

    /**
     * Retrieves a patient based on their unique patient ID.
     *
     * @param id the unique patient ID.
     * @return the Patient object associated with the specified patient ID.
     */
    Patient getPatientFromId(Integer id);

    /**
     * Retrieves a patient based on their email address.
     *
     * @param email the email address of the patient.
     * @return the Patient object associated with the specified email.
     */
    Patient getPatientFromEmail(String email);

    /**
     * Removes a patient from the system based on their unique ID.
     *
     * @param id the unique patient ID.
     */
    void removePatientById(Integer id);

    /**
     * Retrieves all patients associated with a specific doctor ID.
     *
     * @param doctor_id the unique doctor ID.
     * @return a List of Patient objects associated with the specified doctor ID.
     */
    List<Patient> getPatientsByDoctorId(int doctor_id);
}
