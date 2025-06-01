import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * The class that handles data being displayed to the user.
 * <p>
 * Bridged by {@link KirokuController}.
 * </p>
 */
public class KirokuView {
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