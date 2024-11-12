package POJOS;

import java.time.LocalDate;
import java.util.LinkedList;

public class Patient {
    private int patient_id;
    private String name;
    private String surname;
    private LocalDate dob;
    private String email;
    private Signal Signal;
    private int hospital_id;
    private LinkedList<String> symptoms;
    private LinkedList<Integer> values;

    public Patient(int patient_id, String name, String surname, LocalDate dob, String email, int hospital_id, LinkedList<String> symptoms) {
        this.patient_id = patient_id;
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.email = email;
        this.hospital_id = hospital_id;
        this.symptoms = symptoms;
        this.values = new LinkedList<Integer>();
    }

    public Patient(int patient_id, String name, String surname, LocalDate dob, String email) {
        this.patient_id = patient_id;
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.email = email;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
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

    public POJOS.Signal getSignal() {
        return Signal;
    }
    public void setSignal(POJOS.Signal signal) {
        Signal = signal;
    }

    public int getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(int hospital_id) {
        this.hospital_id = hospital_id;
    }

    public LinkedList<String> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(LinkedList<String> symptoms) {
        this.symptoms = symptoms;
    }

    public LinkedList<Integer> getValues() {
        return values;
    }

    public void setValues(LinkedList<Integer> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patient_id=" + patient_id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", dob=" + dob +
                ", email='" + email + '\'' +
                ", Signal=" + Signal +
                ", hospital_id=" + hospital_id +
                ", symptoms=" + symptoms +
                ", values=" + values +
                '}';
    }
}
