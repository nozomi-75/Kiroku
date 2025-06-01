import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Kiroku {
    private static Scanner scanner;
    private static ArrayList<Student> students;
    private static int choice;
    private static String name;
    private static String refNum;
    private static String date;
    
    // external file for all records
    private static final String FILE_NAME = "logbook.txt";
    private static final String INDEX_MESSAGE = "\nEnter student number to ";
    
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        students = new ArrayList<>();
        
        loadFromFile();
        
        do {
            initialPrompt();
            
            switch (choice) {
                case 1 -> caseOne();
                case 2 -> caseTwo();
                case 3 -> caseThree();
                case 4 -> caseFour();
                case 0 -> System.out.println("\nExiting logbook...\n");
                default -> System.out.println("\nInvalid choice. Please try again.\n");
            }
        } while (choice != 0);
        
        scanner.close();
    }
    
    private static void initialPrompt() {
        System.out.println("\n--- Student Logbook Menu ---");
        System.out.println("1. Add new student entry");
        System.out.println("2. View all entries");
        System.out.println("3. Edit existing entry");
        System.out.println("4. Delete existing entry");
        System.out.println("0. Exit program");
        System.out.print("\nEnter your choice: ");
        
        choice = scanner.nextInt();
        scanner.nextLine();
    }
    
    private static void caseOne() {
        String name = getNameInput();
        String refNum = getRefNumInput();
        String date = getDateInput();

        students.add(new Student(name, refNum, date));
        saveToFile();
        System.out.println("\nStudent entry added.");
    }
    
    private static void caseTwo() {
        if (students.isEmpty()) {
            System.out.println("\nNo students logged yet.");
        } else {
            System.out.println("\n------ Logged Students ------");
            getStudentEntries();
        }
    }
    
    private static void caseThree() {
        if (isListEmpty("No entries to edit.")) return;
        System.out.println("\n------ Select a Student to Edit ------");
        getStudentEntries();

        int index = getValidStudentIndex(INDEX_MESSAGE + "edit: ");
        if (index == -1) return;

        Student student = students.get(index);
        String newName = getNameInput();
        String newDate = getDateInput();

        student.setFullName(newName);
        student.setDate(newDate);
        saveToFile();

        System.out.println("\nStudent updated.");
    }

    private static void caseFour() {
        if (isListEmpty("No entries to manage.")) return;
        System.out.println("\n------ Select a Student to Delete ------");
        getStudentEntries();

        int index = getValidStudentIndex(INDEX_MESSAGE + "delete: ");
        Student toDelete = students.get(index);

        System.out.print("Are you sure you want to delete \"" + toDelete.getFullName() + "\"? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            students.remove(index);
            saveToFile();
            System.out.println("\nStudent deleted.");
        } else {
            System.out.println("\nDeletion cancelled.");
        }
    }

    private static String getNameInput() {
        do {
            System.out.print("Enter full name: ");
            name = scanner.nextLine().trim();
            if (!isValidName(name)) {
                System.out.println("\nPlease enter student name.");
            } 
        } while (!isValidName(name));
        return name;
    }

    private static String getRefNumInput() {
        int refNumLength = 12;

        do {
            System.out.println("Enter ref. no.: ");
            refNum = scanner.nextLine().trim();
            if (!isValidRefNum(refNum, refNumLength)) {
                System.out.println("\nPlease enter correct ref. no.");
            }
        } while (!isValidRefNum(refNum, refNumLength));
        return refNum;
    }

    private static String getDateInput() {
        do {
            System.out.print("Enter date (DD/MM/YYYY): ");
            date = scanner.nextLine();
            if (!isValidDate(date)) {
                System.out.println("\nInvalid date format. Please try again.");
            }
        } while (!isValidDate(date));
        return date;
    }

    private static int getValidStudentIndex(String message) {
        int index = -1;

        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();

            try {
                index = Integer.parseInt(input);
                if (index >=1 && index <= students.size()) {
                    return index - 1;
                } else {
                    System.out.println("Invalid number. Please enter a number between 1 and " + students.size() + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid numeric student number.");
            }
        }
    }
    
    private static void getStudentEntries() {
        for (int i = 0; i < students.size(); i++) {
            students.get(i).displayInfo(i + 1);
        }
    }
    
    private static void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Student s : students) {
                // write student name and date separated by pipe
                writer.write(
                    s.getFullName() + "|" + 
                    s.getRefNum() + "|" +
                    s.getDate());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("\nError saving to file: " + e.getMessage());
        }
    }
    
    private static void loadFromFile() {
        File file = new File(FILE_NAME);
        // if file does not exist, exit from this method
        if (!file.exists()) return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            
            // while read lines from reader are not null
            while ((line = reader.readLine()) != null) {
                // split the line into two parts from the pipe
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    // re-add extracted name and date
                    students.add(new Student(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("\nError reading from file: " + e.getMessage());
        }
    }

    private static boolean isListEmpty(String message) {
        if (students.isEmpty()) {
            System.out.println(message);
            return true;
        }
        return false;
    }

    private static boolean isValidName(String name) {
        return (name != null && !name.trim().isEmpty());
    }

    private static boolean isValidRefNum(String refNum, int length) {
        return ((refNum != null) && (!refNum.trim().isEmpty()) && (refNum.length() == length) && refNum.matches("\\d+"));
    }

    private static boolean isValidDate(String input) {
        DateTimeFormatter dateFormatting = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            LocalDate.parse(input, dateFormatting);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}