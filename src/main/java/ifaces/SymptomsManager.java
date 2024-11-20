package ifaces;

import Pojos.Symptoms;

import java.util.ArrayList;

public interface SymptomsManager {
    public ArrayList<Symptoms> readSymptoms();
    public int getSymptomsLength();
    public void addSymptom(Symptoms symptom);
}
