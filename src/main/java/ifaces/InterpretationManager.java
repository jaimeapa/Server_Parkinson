package ifaces;

import Pojos.Interpretation;


import java.time.LocalDate;
import java.util.LinkedList;

public interface InterpretationManager {
    void addInterpretation(Interpretation interpretation);
    LinkedList<Interpretation> getInterpretationsFromPatient_Id(Integer id);
    LinkedList<Interpretation> getInterpretationsFromDoctor_Id(Integer id);
    void assignSymtomsToInterpretation(int interpretationId, int symptomId);
    int getId(LocalDate date, int patient_id);
    Interpretation getInterpretationFromId(Integer id);
    void setInterpretation(String interpretation, int interpretation_id);
}
