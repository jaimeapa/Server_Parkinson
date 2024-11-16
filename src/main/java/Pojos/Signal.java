package Pojos;

import java.time.LocalDate;
import java.util.List;

public class Signal {
    private List<Integer> valuesEMG;
    private String patientName;
    private LocalDate beginDate;
    private String EMGFilename;

    public Signal(List<Integer> valuesEMG, String patientName, LocalDate beginDate, String EMGFilename) {
        this.valuesEMG = valuesEMG;
        this.patientName = patientName;
        this.beginDate = beginDate;
        this.EMGFilename = EMGFilename;
    }

    public List<Integer> getValuesEMG() {
        return valuesEMG;
    }

    public void setValuesEMG(List<Integer> valuesEMG) {
        this.valuesEMG = valuesEMG;
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

    public String getEMGFilename() {
        return EMGFilename;
    }

    public void setEMGFilename(String EMGFilename) {
        this.EMGFilename = EMGFilename;
    }

    @Override
    public String toString() {
        return "Signal{" +
                "valuesEMG=" + valuesEMG +
                ", patientName='" + patientName + '\'' +
                ", beginDate=" + beginDate +
                ", EMGFilename='" + EMGFilename + '\'' +
                '}';
    }
}


