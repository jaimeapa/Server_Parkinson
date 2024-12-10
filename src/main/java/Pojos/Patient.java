package Pojos;

import java.time.LocalDate;
import java.util.LinkedList;

/**
 * The `Patient` class represents a patient in the healthcare system, storing
 * personal details and their associated interpretations.
 */
public class Patient {
    // Fields

    /** Unique identifier for the patient. */
    private int patient_id;

    /** First name of the patient. */
    private String name;

    /** Last name of the patient. */
    private String surname;

    /** Date of birth of the patient. */
    private LocalDate dob;

    /** Email address of the patient. */
    private String email;

    /** ID of the doctor assigned to the patient. */
    private int doctor_id;

    /** List of interpretations associated with the patient. */
    private LinkedList<Interpretation> interpretations;

    // Constructors

    /**
     * Constructs a new `Patient` with a unique identifier and personal details.
     *
     * @param patient_id the unique identifier for the patient.
     * @param name       the first name of the patient.
     * @param surname    the last name of the patient.
     * @param dob        the date of birth of the patient.
     * @param email      the email address of the patient.
     */
    public Patient(int patient_id, String name, String surname, LocalDate dob, String email) {
        this.patient_id = patient_id;
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.email = email;
        this.interpretations = new LinkedList<>();
    }

    /**
     * Constructs a new `Patient` with a unique identifier, personal details, and a doctor ID.
     *
     * @param patient_id the unique identifier for the patient.
     * @param name       the first name of the patient.
     * @param surname    the last name of the patient.
     * @param dob        the date of birth of the patient.
     * @param email      the email address of the patient.
     * @param doctor_id  the ID of the doctor assigned to the patient.
     */
    public Patient(int patient_id, String name, String surname, LocalDate dob, String email, int doctor_id) {
        this.patient_id = patient_id;
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.email = email;
        this.doctor_id = doctor_id;
        this.interpretations = new LinkedList<>();
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the patient.
     *
     * @return the patient ID.
     */
    public int getPatient_id() {
        return patient_id;
    }

    /**
     * Sets the unique identifier for the patient.
     *
     * @param patient_id the patient ID to set.
     */
    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    /**
     * Gets the first name of the patient.
     *
     * @return the first name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the first name of the patient.
     *
     * @param name the first name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the last name of the patient.
     *
     * @return the last name.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the last name of the patient.
     *
     * @param surname the last name to set.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Gets the date of birth of the patient.
     *
     * @return the date of birth.
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * Sets the date of birth of the patient.
     *
     * @param dob the date of birth to set.
     */
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    /**
     * Gets the email address of the patient.
     *
     * @return the email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the patient.
     *
     * @param email the email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the doctor ID assigned to the patient.
     *
     * @return the doctor ID.
     */
    public int getDoctor_id() {
        return doctor_id;
    }

    /**
     * Sets the doctor ID assigned to the patient.
     *
     * @param doctor_id the doctor ID to set.
     */
    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    /**
     * Gets the list of interpretations associated with the patient.
     *
     * @return the list of interpretations.
     */
    public LinkedList<Interpretation> getInterpretations() {
        return interpretations;
    }

    /**
     * Sets the list of interpretations associated with the patient.
     *
     * @param interpretations the list of interpretations to set.
     */
    public void setInterpretations(LinkedList<Interpretation> interpretations) {
        this.interpretations = interpretations;
    }

    // Override Methods

    /**
     * Returns a string representation of the `Patient` object.
     *
     * <p>The representation includes the patient's ID, name, surname, date of birth,
     * and email address.</p>
     *
     * @return a string representation of the `Patient` object.
     */
    @Override
    public String toString() {
        return "Patient{" +
                "patient_id=" + patient_id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", dob=" + dob +
                ", email='" + email + '\'' +
                '}';
    }
}

