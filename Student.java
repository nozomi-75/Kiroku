import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A Plain Old Java Object to represent student data.
 * @see KirokuController
 */
public class Student {
    private String fullName;
    private String referenceNumber;
    private LocalDateTime entryDateTime;

    public Student(String fullName, String referenceNumber, LocalDateTime entryDateTime) {
        this.fullName = fullName;
        this.referenceNumber = referenceNumber;
        this.entryDateTime = entryDateTime;
    }

    public String getFullName() { return fullName; }
    public String getReferenceNumber() { return referenceNumber; }
    public LocalDateTime getEntryDateTime() { return entryDateTime; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEntryDateTime(LocalDateTime entryDateTime) { this.entryDateTime = entryDateTime; }

    /**
     * A helper method to write the input to an external file.
     * @return The student log entry with name, ref.no., and date.
     * @see DataUtils#saveToFile(String, java.util.List)
     */
    public String toStorageFormat() {
        return fullName + "," + referenceNumber + "," + entryDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    /**
     * A helper method to load entries from an external file.
     * @param entry : The full student entry detail.
     * @return Student object, if there are any to load.
     */
    public static Student fromStorageFormat(String entry) {
        String[] parts = entry.split(",");
        if (parts.length != 3) return null;
        LocalDateTime dateTime = LocalDateTime.parse(parts[2], DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        return new Student(parts[0], parts[1], dateTime);
    }
}