package ifaces;

import Pojos.Symptoms;

import java.util.ArrayList;
import java.util.LinkedList;

public interface SymptomsManager {
    ArrayList<Symptoms> readSymptoms();
    int getSymptomsLength();
    void addSymptom(Symptoms symptom);
    LinkedList<Symptoms> getSymptomsFromInterpretation(int interpretation_id);
}
