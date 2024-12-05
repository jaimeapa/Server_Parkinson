package Pojos;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Signal {
    private List<Integer> values;
    private String filename;
    private SignalType signalType;
    public static final int samplingrate = 100;

    public enum SignalType {
        EMG,
        EDA
    }

    public Signal(SignalType signalType) {
        this.values = new LinkedList<>();
        this.signalType = signalType;
    }

    public Signal(List<Integer> values, SignalType signalType) {
        this.values = values;
        this.signalType = signalType;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValuesEMG(String stringEMG) {
        this.values = stringToValues(stringEMG);
    }
    public void setValuesEDA(String stringEDA) {
        this.values = stringToValues(stringEDA);
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

    public  List<Integer> stringToValues(String str) {
        values.clear(); // Limpiamos la lista antes de agregar nuevos valores.
        String[] tokens = str.split(" "); // Dividimos el String por el espacio.

        int size = tokens.length;
        if(size>2) {
            for (int i = 0; i < size; i++) {
                try {
                    values.add(Integer.parseInt(tokens[i])); // Convertimos cada fragmento a Integer y lo agregamos a la LinkedList.
                } catch (NumberFormatException e) {
                    // Manejo de error si algún valor no es un Integer válido.
                    System.out.println("Error al convertir el valor: " + tokens[i]);
                }
            }
        }

        return values;
    }
    public String valuesToString() {
        StringBuilder message = new StringBuilder();
        String separator = " ";

        for (int i = 0; i < values.size(); i++) {
            message.append(values.get(i));
            if (i < values.size() - 1) {
                message.append(separator);
            }
        }

        return message.toString();
    }

    @Override
    public String toString() {
        return "Signal{" +
                "values=" + values +
                ", Filename='" + filename + '\'' +
                '}';
    }
}


