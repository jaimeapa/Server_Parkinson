package Pojos;

import java.time.LocalDate;
import java.util.LinkedList;

/**
 * Represents a doctor with attributes such as ID, name, surname, date of birth, email,
 * and a list of associated patients.
 */
public class Doctor{
    /**
     * The unique identifier for the doctor.
     */
    private int doctor_id;
    /**
     * The first name of the doctor.
     */
    private String name;
    /**
     * The last name of the doctor.
     */
    private String surname;
    /**
     * The date of birth of the doctor.
     */
    private LocalDate dob;
    /**
     * The email address of the doctor.
     */
    private String email;
    /**
     * The list of patients associated with the doctor.
     */
    private LinkedList<Patient> patients;
    // Constructors

    /**
     * Creates a new doctor with all attributes.
     *
     * @param doctor_id the unique identifier for the doctor.
     * @param name      the first name of the doctor.
     * @param surname   the last name of the doctor.
     * @param dob       the date of birth of the doctor.
     * @param email     the email address of the doctor.
     */
    public Doctor(int doctor_id, String name, String surname, LocalDate dob, String email) {
        this.doctor_id = doctor_id;
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.email = email;
        patients = new LinkedList<>();
    }
    /**
     * Creates a new doctor without specifying the ID.
     *
     * @param name    the first name of the doctor.
     * @param surname the last name of the doctor.
     * @param dob     the date of birth of the doctor.
     * @param email   the email address of the doctor.
     */
    public Doctor(String name, String surname, LocalDate dob, String email) {
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.email = email;
        patients = new LinkedList<>();
    }
    // Getters and Setters

    /**
     * Gets the unique identifier for the doctor.
     *
     * @return the doctor ID.
     */

    public int getDoctor_id() {
        return doctor_id;
    }
    /**
     * Sets the unique identifier for the doctor.
     *
     * @param doctor_id the doctor ID to set.
     */
    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }
    /**
     * Gets the first name of the doctor.
     *
     * @return the doctor's first name.
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the first name of the doctor.
     *
     * @param name the doctor's first name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Gets the last name of the doctor.
     *
     * @return the doctor's last name.
     */
    public String getSurname() {
        return surname;
    }
/**
     * Sets the last name of the doctor.
     *
     * @param surname the doctor's last name to set.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }
    /**
     * Gets the date of birth of the doctor.
     *
     * @return the date of birth.
     */

    public LocalDate getDob() {
        return dob;
    }
    /**
     * Sets the date of birth of the doctor.
     *
     * @param dob the date of birth to set.
     */
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
    /**
     * Gets the email address of the doctor.
     *
     * @return the email address.
     */
    public String getEmail() {
        return email;
    }
    /**
     * Sets the email address of the doctor.
     *
     * @param email the email address to set.
     */

    public void setEmail(String email)  {
        this.email = email;
    }
    /**
     * Gets the list of patients associated with the doctor.
     *
     * @return the list of patients.
     */

    public LinkedList<Patient> getPatients() {
        return patients;
    }
    /**
     * Sets the list of patients associated with the doctor.
     *
     * @param patients the list of patients to set.
     */

    public void setPatients(LinkedList<Patient> patients) {
        this.patients = patients;
    }
    // Override Methods

    /**
     * Returns a string representation of the `Doctor` object.
     *
     * @return a string containing the doctor's details.
     */
    @Override
    public String toString() {
        return "Doctor{" +
                "doctor_id=" + doctor_id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", dob=" + dob +
                ", email='" + email + '\'' +
                '}';
    }
}
