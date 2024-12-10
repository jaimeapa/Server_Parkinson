package ifaces;


import Pojos.Doctor;

import java.time.LocalDate;
import java.util.ArrayList;

public interface DoctorManager {
    ArrayList<Doctor> readDoctors();
    void addDoctor(String name, String surname, LocalDate dob, String email, int user_id);
    int getId(String name);
    Doctor getDoctorFromUser(int user_id);
    Doctor getDoctorFromId(Integer id);

}
