package ifaces;

import Pojos.Patient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface PatientManager {
    ArrayList<Patient> readPatients();
    void addPatient(String name, String surname, LocalDate dob, String email, int doctor_id, int user_id);
    int getId(String name);
    Integer emailToId(String email);
    Patient getPatientFromUser(int user_id);
    Patient getPatientFromId(Integer id);
    Patient getPatientFromEmail(String email);
    void removePatientById (Integer id);
    List<Patient> getPatientsByDoctorId(int doctor_id);
}
