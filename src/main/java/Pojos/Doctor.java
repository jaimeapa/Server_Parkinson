package Pojos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Doctor implements Serializable {
    private int doctor_id;
    private String name;
    private String surname;
    private LocalDate dob;
    private String email;

    public Doctor(int doctor_id, String name, String surname, LocalDate dob, String email) {
        this.doctor_id = doctor_id;
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.email = email;
    }
    public Doctor(String name, String surname, LocalDate dob, String email) {
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.email = email;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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
