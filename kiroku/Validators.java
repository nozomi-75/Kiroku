package kiroku;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Set of simple methods to validate user input.
 * @see KirokuController#start()
 * @see DataUtils#promptValidName(KirokuView)
 * @see DataUtils#promptValidRefNum(KirokuView)
 * @see DataUtils#promptValidDateTime(KirokuView)
 */
public class Validators {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isValidRefNum(String ref, int length) {
        return ref != null && ref.matches("\\d{" + length + "}");
    }

    public static boolean isValidDateTime(String input) {
        try {
            LocalDateTime.parse(input, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static int parseMenuChoice(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}