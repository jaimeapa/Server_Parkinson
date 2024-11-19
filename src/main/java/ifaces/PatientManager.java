package ifaces;

import Pojos.Patient;

import java.time.LocalDate;
import java.util.ArrayList;

public interface PatientManager {
    public ArrayList<Patient> readPatients();
    public void addPatient(String name, String surname, LocalDate dob, String email);
    public int getId(String name);
    public Integer emailToId(String email);
    public Patient getPatientFromUser(int user_id);
    public Patient getPatientFromId(Integer id);
    public Patient getPatientFromEmail(String email);
    public void removePatientById (Integer id);
}
