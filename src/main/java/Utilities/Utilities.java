package Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * The {@code Utilities} class provides utility methods for common operations,
 * such as reading input, validating email formats, and parsing dates.
 */
public class Utilities {
    /**
     * Reads a string input from the user with a custom prompt message.
     * If an error occurs while reading, the method prompts the user to try again.
     *
     * @param message the message to display as a prompt to the user
     * @return the input string entered by the user
     */
    public static String readString(String message) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(message);
        try {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("Error al leer la entrada. Intente nuevamente.");
            return readString(message); // vuelve a intentar si ocurre un error
        }
    }
    /**
     * Checks if the provided email string follows a valid email format.
     *
     * @param email the email string to validate
     * @return {@code true} if the email format is valid; {@code false} otherwise
     */
    public static boolean checkEmail(String email){
        Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");
        Matcher mather = pattern.matcher(email);
        if (mather.find() == true) {
            return true;
        } else {
            System.out.println("Please follow the email format: example@example.com");
            return false;
        }
    }
    /**
     * Converts a date string in the format "yyyy-MM-d" into a {@code LocalDate} object.
     *
     * @param d the date string to parse
     * @return the parsed {@code LocalDate} object
     * @throws DateTimeParseException if the date string is not in the expected format
     */
    public static LocalDate stringToDate(String d) {
        LocalDate date = LocalDate.parse(d, DateTimeFormatter.ofPattern("yyyy-MM-d"));
        return date;

    }
}
