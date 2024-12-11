package ifaces;


import Pojos.Doctor;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The DoctorManager interface provides methods for managing doctor-related data.
 * It allows reading a list of doctors, adding a new doctor, and retrieving doctor information by various identifiers.
 */
public interface DoctorManager {

    /**
     * Reads the list of all registered doctors.
     *
     * @return an ArrayList containing all doctors.
     */
    ArrayList<Doctor> readDoctors();

    /**
     * Adds a new doctor to the system.
     *
     * @param name the first name of the doctor.
     * @param surname the last name of the doctor.
     * @param dob the date of birth of the doctor.
     * @param email the email address of the doctor.
     * @param user_id the unique user ID associated with the doctor.
     */
    void addDoctor(String name, String surname, LocalDate dob, String email, int user_id);

    /**
     * Retrieves the unique ID of a doctor based on their name.
     *
     * @param name the name of the doctor.
     * @return the unique ID associated with the doctor.
     */
    int getId(String name);

    /**
     * Retrieves a doctor based on the associated user ID.
     *
     * @param user_id the unique user ID associated with the doctor.
     * @return the Doctor object associated with the specified user ID.
     */
    Doctor getDoctorFromUser(int user_id);

    /**
     * Retrieves a doctor based on their unique doctor ID.
     *
     * @param id the unique doctor ID.
     * @return the Doctor object associated with the specified doctor ID.
     */
    Doctor getDoctorFromId(Integer id);
}

