package Pojos;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * The `Interpretation` class represents a medical interpretation of signals and symptoms
 * related to a patient. It provides methods to analyze signals for Parkinson's disease
 * and manage associated symptoms and observations.
 */
public class Interpretation {
    /**
     * The unique identifier for the interpretation.
     */
    private int id;
    /**
     * The date of the interpretation.
     */
    private LocalDate date;
    /**
     * The ID of the patient associated with the interpretation.
     */
    private int patient_id;
    /**
     * The ID of the doctor who provided the interpretation.
     */
    private int doctor_id;
    /**
     * The EMG signal associated with the interpretation.
     */
    private Signal signalEMG;
    /**
     * The EDA signal associated with the interpretation.
     */
    private Signal signalEDA;
    /**
     * The textual explanation of the interpretation.
     */
    private String interpretation;
    /**
     * The list of symptoms associated with the interpretation.
     */
    private List<Symptoms> symptoms;
    /**
     * Additional observations related to the interpretation.
     */
    private String observation;
    // Constructors

    /**
     * Constructs a new `Interpretation` with all attributes specified.
     *
     * @param id             the unique identifier for the interpretation.
     * @param date           the date of the interpretation.
     * @param interpretation the textual explanation of the interpretation.
     * @param signalEMG      the EMG signal associated with the interpretation.
     * @param signalEDA      the EDA signal associated with the interpretation.
     * @param patient_id     the ID of the patient.
     * @param doctor_id      the ID of the doctor.
     * @param observation    additional observations related to the interpretation.
     */
    public Interpretation(int id, LocalDate date, String interpretation, Signal signalEMG, Signal signalEDA, int patient_id, int doctor_id, String observation) {
        this.id = id;
        this.date = date;
        this.interpretation = interpretation;
        this.symptoms = new LinkedList<>();
        this.signalEMG = signalEMG;
        this.signalEDA = signalEDA;
        this.patient_id = patient_id;
        this.doctor_id = doctor_id;
        this.observation = observation;
    }

    /**
     * Constructs a new `Interpretation` without an ID.
     *
     * @param date           the date of the interpretation.
     * @param interpretation the textual explanation of the interpretation.
     * @param signalEMG      the EMG signal associated with the interpretation.
     * @param signalEDA      the EDA signal associated with the interpretation.
     * @param patient_id     the ID of the patient.
     * @param doctor_id      the ID of the doctor.
     * @param observation    additional observations related to the interpretation.
     */

    public Interpretation(LocalDate date, String interpretation, Signal signalEMG, Signal signalEDA, int patient_id, int doctor_id, String observation) {
        this.date = date;
        this.interpretation = interpretation;
        this.symptoms = new LinkedList<>();
        this.signalEMG = signalEMG;
        this.signalEDA = signalEDA;
        this.patient_id = patient_id;
        this.doctor_id = doctor_id;
        this.observation = observation;
    }

    /**
     * Constructs a basic `Interpretation` with only the date and interpretation.
     *
     * @param date           the date of the interpretation.
     * @param interpretation the textual explanation of the interpretation.
     */

    public Interpretation(LocalDate date, String interpretation) {
        this.date = date;
        this.interpretation = interpretation;
    }

    /**
     * Gets the unique identifier for the interpretation.
     *
     * @return the unique identifier (ID).
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the interpretation.
     *
     * @param id the unique identifier to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the date of the interpretation.
     *
     * @return the date of the interpretation.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of the interpretation.
     *
     * @param date the date to set.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the patient ID associated with the interpretation.
     *
     * @return the patient ID.
     */
    public int getPatient_id() {
        return patient_id;
    }

    /**
     * Sets the patient ID associated with the interpretation.
     *
     * @param patient_id the patient ID to set.
     */
    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    /**
     * Gets the doctor ID associated with the interpretation.
     *
     * @return the doctor ID.
     */
    public int getDoctor_id() {
        return doctor_id;
    }

    /**
     * Sets the doctor ID associated with the interpretation.
     *
     * @param doctor_id the doctor ID to set.
     */
    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    /**
     * Gets the textual interpretation.
     *
     * @return the interpretation text.
     */
    public String getInterpretation() {
        return interpretation;
    }

    /**
     * Sets the textual interpretation.
     *
     * @param interpretation the interpretation text to set.
     */
    public void setInterpretation(String interpretation) {
        this.interpretation = interpretation;
    }

    /**
     * Gets the list of symptoms associated with the interpretation.
     *
     * @return the list of symptoms.
     */
    public List<Symptoms> getSymptoms() {
        return symptoms;
    }

    /**
     * Sets the list of symptoms associated with the interpretation.
     *
     * @param symptoms the list of symptoms to set.
     */
    public void setSymptoms(List<Symptoms> symptoms) {
        this.symptoms = symptoms;
    }

    /**
     * Gets additional observations related to the interpretation.
     *
     * @return the observation text.
     */
    public String getObservation() {
        return observation;
    }

    /**
     * Sets additional observations related to the interpretation.
     *
     * @param observation the observation text to set.
     */
    public void setObservation(String observation) {
        this.observation = observation;
    }

    /**
     * Gets the EMG (Electromyogram) signal associated with the interpretation.
     *
     * @return the EMG signal.
     */
    public Signal getSignalEMG() {
        return signalEMG;
    }

    /**
     * Sets the EMG (Electromyogram) signal associated with the interpretation.
     *
     * @param signalEMG the EMG signal to set.
     */
    public void setSignalEMG(Signal signalEMG) {
        this.signalEMG = signalEMG;
    }

    /**
     * Gets the EDA (Electrodermal Activity) signal associated with the interpretation.
     *
     * @return the EDA signal.
     */
    public Signal getSignalEDA() {
        return signalEDA;
    }

    /**
     * Sets the EDA (Electrodermal Activity) signal associated with the interpretation.
     *
     * @param signalEDA the EDA signal to set.
     */
    public void setSignalEDA(Signal signalEDA) {
        this.signalEDA = signalEDA;
    }

    /**
     * Returns a string representation of the `Interpretation` object.
     *
     * <p>The representation includes the date, interpretation text, associated signals,
     * symptoms, and any additional observations.</p>
     *
     * @return a string representation of the `Interpretation` object.
     */
    @Override
    public String toString() {
        return "Interpretation{" +
                "date=" + date +
                ", interpretation='" + interpretation + '\'' +
                ", signalEMG='" + signalEMG + '\'' +
                ", signalEDA='" + signalEDA + '\'' +
                ", symptoms=" + symptoms +
                ", observation='" + observation + '\'' +
                '}';
    }
}



