import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KirokuController {
    private final List<Student> students = new ArrayList<>();
    private final KirokuView view = new KirokuView();
    private static final String FILE_NAME = "lobok.txt";

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
                case 0 -> view.showMessage("Exiting lobok...");
                default -> view.showMessage("Invalid option. Try again.");
            }
        } while (choice != 0);

        DataUtils.saveToFile(FILE_NAME, students);
    }

    private void addStudent() {
        String name = DataUtils.promptValidName(view);
        String ref = DataUtils.promptValidRefNum(view);
        LocalDateTime dateTime = DataUtils.promptValidDateTime(view);
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
        s.setEntryDateTime(DataUtils.promptValidDateTime(view));
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