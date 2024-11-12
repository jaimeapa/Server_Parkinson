package Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utilities {

    // BufferedReader para leer desde la consola
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    // Constructor privado para prevenir instanciación
    private Utilities() {
        throw new UnsupportedOperationException("Esta es una clase utilitaria y no puede ser instanciada");
    }

    /**
     * Lee un entero desde la consola, mostrando un mensaje y validando la entrada.
     *
     * @param message El mensaje que se mostrará antes de la entrada
     * @return El entero leído
     */
    public static int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(reader.readLine());
            } catch (NumberFormatException | IOException e) {
                System.out.println("Error: Ingrese un número entero válido.");
            }
        }
    }

    /**
     * Lee un float desde la consola, mostrando un mensaje y validando la entrada.
     *
     * @param message El mensaje que se mostrará antes de la entrada
     * @return El float leído
     */
    public static float readFloat(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Float.parseFloat(reader.readLine());
            } catch (NumberFormatException | IOException e) {
                System.out.println("Error: Ingrese un número decimal válido (float).");
            }
        }
    }

    /**
     * Lee un double desde la consola, mostrando un mensaje y validando la entrada.
     *
     * @param message El mensaje que se mostrará antes de la entrada
     * @return El double leído
     */
    public static double readDouble(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Double.parseDouble(reader.readLine());
            } catch (NumberFormatException | IOException e) {
                System.out.println("Error: Ingrese un número decimal válido (double).");
            }
        }
    }

    /**
     * Lee un boolean desde la consola, mostrando un mensaje y validando la entrada.
     * Acepta "true" o "false" como valores válidos.
     *
     * @param message El mensaje que se mostrará antes de la entrada
     * @return El valor booleano leído
     */
    public static boolean readBoolean(String message) {
        while (true) {
            try {
                System.out.print(message);
                String input = reader.readLine().trim().toLowerCase();
                if (input.equals("true")) {
                    return true;
                } else if (input.equals("false")) {
                    return false;
                } else {
                    System.out.println("Error: Ingrese 'true' o 'false'.");
                }
            } catch (IOException e) {
                System.out.println("Error al leer la entrada. Intente nuevamente.");
            }
        }
    }

    /**
     * Lee una cadena de texto desde la consola, mostrando un mensaje.
     *
     * @param message El mensaje que se mostrará antes de la entrada
     * @return La cadena de texto ingresada
     */
    public static String readString(String message) {
        System.out.print(message);
        try {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("Error al leer la entrada. Intente nuevamente.");
            return readString(message); // vuelve a intentar si ocurre un error
        }
    }

    public static LocalDate stringToDate(String d) {
        LocalDate date = LocalDate.parse(d, DateTimeFormatter.ofPattern("yyyy-MM-d"));
        return date;

    }
}
