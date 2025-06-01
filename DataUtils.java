import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DataUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

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

    public static LocalDateTime promptValidDateTime(KirokuView view) {
        String input;
        while (true) {
            input = view.prompt("Enter date and time (DD/MM/YYYY HH:MM): ");
            if (Validators.isValidDateTime(input)) {
                return LocalDateTime.parse(input, formatter);
            } else {
                view.showMessage("\nInvalid date/time format. Please follow DD/MM/YYYY HH:MM.\n");
            }
        }
    }
}