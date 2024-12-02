package Pojos;

import java.time.LocalDate;
import java.util.List;

public class Signal {
    private List<Integer> values;
    private String patientName;
    private LocalDate beginDate;
    private String filename;
    private SignalType signalType;
    public static final int samplingrate = 100;

    public enum SignalType {
        EMG,
        EDA
    }

    public Signal(List<Integer> values, String patientName, LocalDate beginDate, String Filename) {
        this.values = values;
        this.patientName = patientName;
        this.beginDate = beginDate;
        this.filename = Filename;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValuesEMG(List<Integer> valuesEMG) {
        this.values = values;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String Filename) {
        this.filename = Filename;
    }

    public SignalType getSignalType() {
        return signalType;
    }

    public void setSignalType(SignalType signalType) {
        this.signalType = signalType;
    }

    @Override
    public String toString() {
        return "Signal{" +
                "values=" + values +
                ", patientName='" + patientName + '\'' +
                ", beginDate=" + beginDate +
                ", Filename='" + filename + '\'' +
                '}';
    }
}


