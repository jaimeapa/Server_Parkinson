package Pojos;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import Pojos.Signal.SignalType;

public class Interpretation {
    private int id;
    private LocalDate date;
    private int patient_id;
    private int doctor_id;
    private Signal signalEMG;
    private Signal signalEDA;
    private String interpretation;
    private List<Symptoms> symptoms;
    private String observation;
    public static final int samplingrate = 100;


    public Interpretation(int id, LocalDate date,String interpretation,Signal signalEMG, Signal signalEDA, int patient_id, int doctor_id) {
        this.id = id;
        this.date = date;
        this.interpretation = interpretation;
        this.symptoms = new LinkedList<>();
        this.signalEMG = signalEMG;
        this.signalEDA = signalEDA;
        this.patient_id = patient_id;
        this.doctor_id = doctor_id;
    }

    public Interpretation(LocalDate date, String interpretation) {
        this.date = date;
        this.interpretation = interpretation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getInterpretation() {
        return interpretation;
    }

    public List<Symptoms> getSymptoms() {
        return symptoms;
    }

    public String getObservation() {
        return observation;
    }

    public void setSymptoms(List<Symptoms> symptoms) {
        this.symptoms = symptoms;
    }

    public void setInterpretation(String interpretation) {
        this.interpretation = interpretation;
    }

    public Signal getSignalEMG() {
        return signalEMG;
    }

    public void setSignalEMG(Signal signalEMG) {
        this.signalEMG = signalEMG;
    }

    public Signal getSignalEDA() {
        return signalEDA;
    }

    public void setSignalEDA(Signal signalEDA) {
        this.signalEDA = signalEDA;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String analyzeBitalinoData(Signal signal) {
        List<Integer> filteredValues = new LinkedList<>();
        for (Integer value : signal.getValues()) {
            if (value >= 50 && value <= 900) {
                filteredValues.add(value);
            }
        }
        if (filteredValues.isEmpty()) {
            return "No valid data to analyze.";
        }
        if (signal.getSignalType() == SignalType.EMG) {
            return analyzeEMGForParkinson(filteredValues);
        } else if (signal.getSignalType() == SignalType.EDA) {
            return analyzeEDAForParkinson(filteredValues);
        } else {
            return "Unknown signal type.";
        }
    }

    private double convertRawValue(int rawValue) {
        return (rawValue * 3.3) / (Math.pow(2, 10) - 1);
    }

    private String analyzeEMGForParkinson(List<Integer> emgValues) {
        double total = 0;
        double max = 0;

        for (int value : emgValues) {
            double convertedValue = convertRawValue(value);
            total += convertedValue;
            if (convertedValue > max) {
                max = convertedValue;
            }
        }

        double average = 0;
        if (!emgValues.isEmpty()) {
            average = total / emgValues.size();
        }
        String observation;
        if (max > 500) {
            observation = "High muscle activity detected. This might indicate tremors, which are commonly associated with Parkinson's disease.";
        } else if (average < 80) {
            observation = "Low average muscle activity detected. This could indicate rigidity or bradykinesia, typical symptoms of Parkinson's disease.";
        } else if (average > 300 && max > 400) {
            observation = "Fluctuations in muscle activity detected. This might indicate dyskinesia, which can occur in advanced Parkinson's cases or as a side effect of medication.";
        } else {
            observation = "Muscle activity appears normal. No signs of Parkinson's disease detected in this observation.";
        }

        return "EMG Parkinson Analysis:\n" +
                "Average Amplitude: " + String.format("%.2f µV", average) + "\n" +
                "Max Amplitude: " + String.format("%.2f µV", max) + "\n" +
                "Observation: " + observation + "\n";
    }


    private String analyzeEDAForParkinson(List<Integer> edaValues) {
        double total = 0;
        double max = 0;

        for (int value : edaValues) {
            double convertedValue = convertRawValue(value);
            total += convertedValue;
            if (convertedValue > max) {
                max = convertedValue;
            }
        }

        double average = 0;
        if (!edaValues.isEmpty()) {
            average = total / edaValues.size();
        }

        String observation;
        if (average < 0.8) {
            observation = "Reduced autonomic response detected. This might indicate autonomic dysfunction, which is common in Parkinson's disease.";
        } else if (max > 15) {
            observation = "High autonomic response detected. This could be due to stress or anxiety, which are often associated with Parkinson's disease.";
        } else if (average >= 0.8 && average <= 1.5) {
            observation = "Borderline autonomic activity detected. This might indicate mild autonomic irregularities.";
        } else {
            observation = "Normal autonomic activity. No significant autonomic dysfunction detected.";
        }

        return "EDA Parkinson Analysis:\n" +
                "Average Conductance: " + String.format("%.2f µS", average) + "\n" +
                "Max Conductance: " + String.format("%.2f µS", max) + "\n" +
                "Observation: " + observation + "\n";
    }


    @Override
    public String toString() {
        return "Interpretation{" +
                "date=" + date +
                ", interpretation='" + interpretation + '\'' +
                ", symptoms=" + symptoms +
                ", observation='" + observation + '\'' +
                '}';
    }
}
