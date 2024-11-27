package ifaces;

import Pojos.Patient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface PatientManager {
    public ArrayList<Patient> readPatients();
    public void addPatient(String name, String surname, LocalDate dob, String email, int user_id);
    public int getId(String name);
    public Integer emailToId(String email);
    public Patient getPatientFromUser(int user_id);
    public Patient getPatientFromId(Integer id);
    public Patient getPatientFromEmail(String email);
    public void removePatientById (Integer id);
    public void assignSymtomsToPatient(int patientId, int symptomId);
    public List<Patient> getPatientsByDoctorId(int doctor_id);
}
