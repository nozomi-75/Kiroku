import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public String toStorageFormat() {
        return fullName + "|" + referenceNumber + "|" + entryDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public static Student fromStorageFormat(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 3) return null;
        LocalDateTime dateTime = LocalDateTime.parse(parts[2], DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        return new Student(parts[0], parts[1], dateTime);
    }
}