import java.util.ArrayList;
import java.util.Scanner;

public class Kiroku {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Student> students = new ArrayList<>();
        int choice;
        
        do {
            System.out.println("\n--- Student Logbook Menu ---");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Edit Student");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); 
            
            switch (choice) {
                case 1:
                System.out.print("Enter full name: ");
                String name = scanner.nextLine();
                System.out.print("Enter date (e.g Jan 20,2025): ");
                String date = scanner.nextLine();
                students.add(new Student(name, date));
                System.out.println("Student added.");
                break;
                
                case 2:
                if (students.isEmpty()) {
                    System.out.println("No students logged yet.");
                } else {
                    System.out.println("\n------ Logged Students ------");
                    for (int i = 0; i < students.size(); i++) {
                        students.get(i).displayInfo(i + 1);
                    }
                }
                break;
                
                case 3:
                if (students.isEmpty()) {
                    System.out.println("No students to edit.");
                } else {
                    System.out.println("\n------ Select a Student to Edit------");
                    for (int i = 0; i < students.size(); i++) {
                        students.get(i).displayInfo(i + 1);
                    }
                    
                    System.out.print("Enter student number to edit: ");
                    int index = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    
                    if (index < 1 || index > students.size()) {
                        System.out.println("Invalid student number.");
                    } else {
                        Student student = students.get(index - 1);
                        System.out.print("Enter new full name: ");
                        String newName = scanner.nextLine();
                        System.out.print("Enter new date: ");
                        String newDate = scanner.nextLine();
                        
                        student.setFullName(newName);
                        student.setDate(newDate);
                        System.out.println("Student updated.");
                    }
                }
                break;
                
                case 0:
                System.out.println("Exiting logbook...");
                break;
                
                default:
                System.out.println("Invalid choice. Please try again.");
            }
            
        } while (choice != 0);
        
        scanner.close();
    }
}