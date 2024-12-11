package ifaces;

import Pojos.Interpretation;


import java.time.LocalDate;
import java.util.LinkedList;

/**
 * The InterpretationManager interface provides methods for managing interpretations
 * and their relationships to patients, doctors, and symptoms.
 */
public interface InterpretationManager {

    /**
     * Adds a new interpretation to the system.
     *
     * @param interpretation the Interpretation object to be added.
     */
    void addInterpretation(Interpretation interpretation);

    /**
     * Retrieves all interpretations associated with a specific patient ID.
     *
     * @param id the unique patient ID.
     * @return a LinkedList of Interpretation objects associated with the patient.
     */
    LinkedList<Interpretation> getInterpretationsFromPatient_Id(Integer id);

    /**
     * Retrieves all interpretations associated with a specific doctor ID.
     *
     * @param id the unique doctor ID.
     * @return a LinkedList of Interpretation objects associated with the doctor.
     */
    LinkedList<Interpretation> getInterpretationsFromDoctor_Id(Integer id);

    /**
     * Assigns a symptom to a specific interpretation.
     *
     * @param interpretationId the unique ID of the interpretation.
     * @param symptomId the unique ID of the symptom.
     */
    void assignSymtomsToInterpretation(int interpretationId, int symptomId);

    /**
     * Retrieves the unique ID of an interpretation based on its date and patient ID.
     *
     * @param date the date of the interpretation.
     * @param patient_id the unique patient ID associated with the interpretation.
     * @return the unique ID of the interpretation.
     */
    int getId(LocalDate date, int patient_id);

    /**
     * Retrieves an interpretation based on its unique ID.
     *
     * @param id the unique ID of the interpretation.
     * @return the Interpretation object associated with the specified ID.
     */
    Interpretation getInterpretationFromId(Integer id);

    /**
     * Updates the interpretation text for a specific interpretation.
     *
     * @param interpretation the new interpretation text.
     * @param interpretation_id the unique ID of the interpretation to be updated.
     */
    void setInterpretation(String interpretation, int interpretation_id);
}