package kiroku;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Set of simple utility methods to process data through
 * out the program execution.
 * 
 * @see Validators
 */
public class DataUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Utility to load entries from an external file.
     * <p>
     * If there is no file to fetch from, it skips out.
     * Otherwise, it starts a BufferedReader to read said file.
     * If the entry is valid, it will call 
     * {@link Student#fromStorageFormat(String)}
     * to create a Student object based from the file's data.
     * </p>
     * 
     * @param filename : Entry output file name.
     * @param students : The list storing the entries as objects.
     * @see KirokuController#start()
     * @see Student#fromStorageFormat(String)
     */
    public static void loadFromFile(String filename, List<Student> students) {
        File file = new File(filename);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Student s = Student.fromStorageFormat(line);
                if (s != null) students.add(s);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Utility to save entries to an external file.
     * 
     * @param filename : Entry output file name.
     * @param students : The list storing the entries as objects.
     * @see KirokuController#start()
     * @see Student#toStorageFormat()
     */
    public static void saveToFile(String filename, List<Student> students) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Student s : students) writer.write(s.toStorageFormat() + "\n");
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    public static String promptValidName(KirokuView view) {
        String name;
        do {
            name = view.prompt("\nEnter full name: ");
            if (!Validators.isValidName(name)) {
                view.showMessage("\nName cannot be empty.\n");
            }
        } while (!Validators.isValidName(name));
        return name;
    }

    public static String promptValidRefNum(KirokuView view) {
        String ref;
        do {
            ref = view.prompt("Enter reference number (12 digits): ");
            if (!Validators.isValidRefNum(ref, 12)) {
                view.showMessage("\nInvalid reference number. It must be exactly 12 digits.\n");
            }
        } while (!Validators.isValidRefNum(ref, 12));
        return ref;
    }

    public static ZonedDateTime promptValidDateTime(KirokuView view) {
        String input;
        while (true) {
            input = view.prompt("Enter date and time (DD/MM/YYYY HH:MM): ");
            if (Validators.isValidDateTime(input)) {
                LocalDateTime localDateTime = LocalDateTime.parse(input, formatter);
                return localDateTime.atZone(ZoneId.systemDefault());
            } else {
                view.showMessage("\nInvalid date/time format. Please follow DD/MM/YYYY HH:MM.\n");
            }
        }
    }
}