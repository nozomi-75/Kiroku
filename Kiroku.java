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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Kiroku {
    public static void main(String[] args) {
        KirokuController controller = new KirokuController();
        controller.start();
    }
}

class KirokuController {
    private final List<Student> students = new ArrayList<>();
    private final KirokuView view = new KirokuView();
    private static final String FILE_NAME = "logbook.csv";

    public void start() {
        DataUtils.loadFromFile(FILE_NAME, students);

        int choice;
        do {
            view.showMenu();
            choice = Validators.parseMenuChoice(view.prompt(""));

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> displayStudents();
                case 3 -> editStudent();
                case 4 -> deleteStudent();
                case 5 -> searchStudents();
                case 0 -> view.showMessage("Exiting Kiroku...");
                default -> view.showMessage("Invalid option. Try again.");
            }
        } while (choice != 0);

        DataUtils.saveToFile(FILE_NAME, students);
    }

    private void addStudent() {
        String name = DataUtils.promptValidName(view);
        String ref = DataUtils.promptValidRefNum(view);
        ZonedDateTime dateTime = DataUtils.promptValidDateTime(view);
        students.add(new Student(name, ref, dateTime));
        view.showMessage("\nStudent entry added.");
    }

    private void displayStudents() {
        if (isListEmpty("\nNo students logged.")) return;
        int i = 1;
        for (Student s : students) view.showStudent(s, i++);
    }

    private void editStudent() {
        if (isListEmpty("\nNo students to edit.")) return;
        displayStudents();
        int index = view.promptInt("\nSelect student number to edit: ", 1, students.size()) - 1;
        Student s = students.get(index);
        s.setFullName(DataUtils.promptValidName(view));

        ZonedDateTime dateTime = DataUtils.promptValidDateTime(view);
        s.setEntryDateTime(dateTime);
        view.showMessage("\nStudent updated.");
    }

    private void deleteStudent() {
        if (isListEmpty("\nNo students to delete.")) return;
        displayStudents();
        int index = view.promptInt("\nSelect student number to delete: ", 1, students.size()) - 1;
        if (view.confirm("Are you sure you want to delete \"" + students.get(index).getFullName() + "\"?")) {
            students.remove(index);
            view.showMessage("\nStudent deleted.");
        } else {
            view.showMessage("\nDeletion cancelled.");
        }
    }

    private void searchStudents() {
        if (isListEmpty("\nNo students to search.")) return;
        int mode = view.promptInt("\nSearch by: 1) Name  2) Ref #: ", 1, 2);
        String query = view.prompt("\nEnter search term: ").toLowerCase();
        List<Student> results = SearchUtils.search(students, query, mode == 1);
        if (results.isEmpty()) {
            view.showMessage("\nNo matching results.");
        } else {
            int i = 1;
            for (Student s : results) view.showStudent(s, i++);
        }
    }

    public boolean isListEmpty(String message) {
        if (students.isEmpty()) {
            view.showMessage(message);
            return true;
        }
        return false;
    }
}

class KirokuView {
    private final Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        System.out.println("\n--- Student Logbook Menu ---");
        System.out.println("1. Add new student entry");
        System.out.println("2. View all entries");
        System.out.println("3. Edit existing entry");
        System.out.println("4. Delete existing entry");
        System.out.println("5. Search entries");
        System.out.println("0. Exit");
        System.out.print("\nEnter your choice: ");
    }

    public void showStudent(Student s, int index) {
        String formattedDate = s.getEntryDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        System.out.printf("%d. Name: %s | Ref: %s | Date: %s%n", index, s.getFullName(), s.getReferenceNumber(), formattedDate);
    }

    public void showMessage(String msg) {
        System.out.println(msg);
    }

    public String prompt(String label) {
        System.out.print(label);
        return scanner.nextLine().trim();
    }

    public int promptInt(String label, int min, int max) {
        while (true) {
            try {
                System.out.print(label);
                int val = Integer.parseInt(scanner.nextLine());
                if (val >= min && val <= max) return val;
                System.out.printf("\nPlease enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid number. Please try again.\n");
            }
        }
    }

    public boolean confirm(String question) {
        String input = prompt(question + " (y/n): ").toLowerCase();
        return input.equals("y") || input.equals("yes");
    }
}

class DataUtils {
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

class SearchUtils {
    public static List<Student> search(List<Student> students, String query, boolean byName) {
        List<Student> results = new ArrayList<>();
        for (Student s : students) {
            if (byName && s.getFullName().toLowerCase().contains(query)) {
                results.add(s);
            } else if (!byName && s.getReferenceNumber().contains(query)) {
                results.add(s);
            }
        }
        return results;
    }
}

class Validators {
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

class Student {
    private String fullName;
    private String referenceNumber;
    private ZonedDateTime entryDateTime;

    private static DateTimeFormatter dateFormatting = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm z");

    public Student(String fullName, String referenceNumber, ZonedDateTime entryDateTime) {
        this.fullName = fullName;
        this.referenceNumber = referenceNumber;
        this.entryDateTime = entryDateTime;
    }

    public String getFullName() { return fullName; }
    public String getReferenceNumber() { return referenceNumber; }
    public ZonedDateTime getEntryDateTime() { return entryDateTime; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEntryDateTime(ZonedDateTime entryDateTime) { this.entryDateTime = entryDateTime; }

    public String toStorageFormat() {
        return fullName + "," + referenceNumber + "," + entryDateTime.format(dateFormatting);
    }

    public static Student fromStorageFormat(String entry) {
        String[] parts = entry.split(",");
        if (parts.length != 3) return null;
        ZonedDateTime dateTime = ZonedDateTime.parse(parts[2], dateFormatting);
        return new Student(parts[0], parts[1], dateTime);
    }
}