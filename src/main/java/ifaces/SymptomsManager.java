package ifaces;

import Pojos.Symptoms;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The SymptomsManager interface provides methods for managing symptoms-related data.
 * It includes functionality for reading symptoms, adding new symptoms, and retrieving symptoms based on interpretation IDs.
 */
public interface SymptomsManager {

    /**
     * Reads the list of all registered symptoms.
     *
     * @return an ArrayList containing all symptoms.
     */
    ArrayList<Symptoms> readSymptoms();

    /**
     * Retrieves the total number of symptoms.
     *
     * @return the length of the symptoms list as an integer.
     */
    int getSymptomsLength();

    /**
     * Adds a new symptom to the system.
     *
     * @param symptom the Symptoms object to be added.
     */
    void addSymptom(Symptoms symptom);

    /**
     * Retrieves all symptoms associated with a specific interpretation ID.
     *
     * @param interpretation_id the unique ID of the interpretation.
     * @return a LinkedList of Symptoms objects associated with the specified interpretation ID.
     */
    LinkedList<Symptoms> getSymptomsFromInterpretation(int interpretation_id);
}