package ifaces;


import Pojos.Doctor;

import java.time.LocalDate;
import java.util.ArrayList;

public interface DoctorManager {
    public ArrayList<Doctor> readDoctors();
    public void addDoctor(String name, String surname, LocalDate dob, String email, int user_id);
    public int getId(String name);
    public Integer emailToId(String email);
    public Doctor getDoctorFromUser(int user_id);
    public Doctor getDoctorFromId(Integer id);
    public Doctor getDoctorFromEmail(String email);
    public void removeDoctorById (Integer id);
}
