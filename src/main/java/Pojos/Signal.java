package Pojos;

import java.util.LinkedList;
import java.util.List;

/**
 * The `Signal` class represents a signal with associated values and its type (EMG or EDA).
 * It provides methods to convert signal values between `String` and `List<Integer>` formats
 * and manage the signal's type.
 */
public class Signal {

    /** List of integer values representing the signal data. */
    private List<Integer> values;

    /** Type of the signal (e.g., EMG or EDA). */
    private SignalType signalType;

    /** Enumeration for signal types. */
    public enum SignalType {
        /**
         * Represents an EMG (electromyography) signal.
         */
        EMG,

        /**
         * Represents an EDA (electrodermal activity) signal.
         */
        EDA
    }

    /**
     * Constructs a `Signal` with a specified type and an empty list of values.
     *
     * @param signalType the type of the signal (EMG or EDA).
     */
    public Signal(SignalType signalType) {
        this.values = new LinkedList<>();
        this.signalType = signalType;
    }

    /**
     * Constructs a `Signal` with specified values and a type.
     *
     * @param values     the list of values representing the signal data.
     * @param signalType the type of the signal (EMG or EDA).
     */
    public Signal(List<Integer> values, SignalType signalType) {
        this.values = values;
        this.signalType = signalType;
    }


    /**
     * Gets the list of values representing the signal data.
     *
     * @return the list of signal values.
     */
    public List<Integer> getValues() {
        return values;
    }

    /**
     * Sets the signal values for an EMG signal using a string representation.
     *
     * @param stringEMG the string containing EMG values separated by spaces.
     */
    public void setValuesEMG(String stringEMG) {
        this.values = stringToValues(stringEMG);
    }

    /**
     * Sets the signal values for an EDA signal using a string representation.
     *
     * @param stringEDA the string containing EDA values separated by spaces.
     */
    public void setValuesEDA(String stringEDA) {
        this.values = stringToValues(stringEDA);
    }

    /**
     * Gets the type of the signal (EMG or EDA).
     *
     * @return the signal type.
     */
    public SignalType getSignalType() {
        return signalType;
    }

    /**
     * Sets the type of the signal.
     *
     * @param signalType the type of the signal to set.
     */
    public void setSignalType(SignalType signalType) {
        this.signalType = signalType;
    }

    // Utility Methods

    /**
     * Converts a string of space-separated integers into a list of values.
     *
     * @param str the string containing signal values separated by spaces.
     * @return the list of integer values.
     */
    public List<Integer> stringToValues(String str) {
        values.clear(); // Clear the existing values before adding new ones.
        String[] tokens = str.split(" "); // Split the string by spaces.

        for (String token : tokens) {
            try {
                values.add(Integer.parseInt(token)); // Convert each token to an integer and add it to the list.
            } catch (NumberFormatException e) {
                // Handle errors if a token is not a valid integer.
                System.out.println("Error converting value: " + token);
            }
        }

        return values;
    }

    /**
     * Converts the list of signal values into a space-separated string.
     *
     * @return the string representation of the signal values.
     */
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

    // Override Methods

    /**
     * Returns a string representation of the `Signal` object.
     *
     * <p>The representation includes the signal's values.</p>
     *
     * @return a string representation of the `Signal` object.
     */
    @Override
    public String toString() {
        return "Signal{" +
                "values=" + values +
                '}';
    }
}



