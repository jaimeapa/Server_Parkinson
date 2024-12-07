package Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

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

    public static LocalDate stringToDate(String d) {
        LocalDate date = LocalDate.parse(d, DateTimeFormatter.ofPattern("yyyy-MM-d"));
        return date;

    }
}
