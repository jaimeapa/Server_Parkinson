package ifaces;

import Pojos.Symptoms;

import java.util.ArrayList;
import java.util.LinkedList;

public interface SymptomsManager {
    public ArrayList<Symptoms> readSymptoms();
    public int getSymptomsLength();
    public void addSymptom(Symptoms symptom);
    LinkedList<Symptoms> getSymptomsFromInterpretation(int interpretation_id);
}
