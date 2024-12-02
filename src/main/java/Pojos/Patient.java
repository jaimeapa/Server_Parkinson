package Pojos;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Patient implements Serializable {
    private static final long serialVersionUID = 4092297860583387711L;
    private int patient_id;
    private String name;
    private String surname;
    private LocalDate dob;
    private String email;
    private int doctor_id;
    private Signal Signal;
    private LinkedList<Symptoms>  symptoms;
    private LinkedList<Integer> values_EDA;
    private LinkedList<Integer> values_EMG;
    private LinkedList<Interpretation> interpretations;
    public static final int samplingrate = 100;

    public Patient(int patient_id, String name, String surname, LocalDate dob, String email, int doctor_id, LinkedList<Symptoms>  symptoms, LinkedList<Interpretation> interpretations) {
        this.patient_id = patient_id;
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.email = email;
        this.doctor_id = doctor_id;
        this.symptoms = new LinkedList<>();
        this.values_EDA = new LinkedList<>();
        this.values_EMG = new LinkedList<>();
        this.interpretations= new LinkedList<>();
    }

    public Patient(String name, String surname, LocalDate dob, String email) {
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.email = email;
    }
    public Patient(int patient_id, String name, String surname, LocalDate dob, String email) {
        this.patient_id = patient_id;
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.email = email;
    }

    public Patient(int patient_id, String name, String surname, LocalDate dob, String email, int doctor_id) {
        this.patient_id = patient_id;
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.email = email;
        this.doctor_id = doctor_id;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public Pojos.Signal getSignal() {
        return Signal;
    }

    public LinkedList<Symptoms> getSymptoms() {
        return symptoms;
    }

    public LinkedList<Integer> getValues_EDA() {
        return values_EDA;
    }

    public LinkedList<Integer> getValues_EMG() {
        return values_EMG;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public LinkedList<Interpretation> getInterpretations() {
        return interpretations;
    }

    public void setInterpretations(LinkedList<Interpretation> interpretations) {
        this.interpretations = interpretations;
    }

    public void setEmail(String email) throws NotBoundException {
        Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");
        Matcher mather = pattern.matcher(email);
        if (mather.find() == true) {
            this.email = email;
        } else {
            throw new NotBoundException("Not valid email");
        }
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public void setSignal(Pojos.Signal signal) {
        Signal = signal;
    }

    public void setSymptoms(LinkedList<Symptoms> symptoms) {
        this.symptoms = symptoms;
    }

    public void setValues_EDA(LinkedList<Integer> values_EDA) {
        this.values_EDA = values_EDA;
    }

    public void setValues_EMG(LinkedList<Integer> values_EMG) {
        this.values_EMG = values_EMG;
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
                ", symptoms=" + symptoms +
                ", values_EDA=" + values_EDA +
                ", values_EMG=" + values_EMG +
                '}';
    }
}
