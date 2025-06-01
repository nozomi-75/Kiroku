package kiroku;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A Plain Old Java Object to represent student data.
 * @see KirokuController
 */
public class Student {
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

    /**
     * A helper method to write the input to an external file.
     * @return The student log entry with name, ref.no., and date.
     * @see DataUtils#saveToFile(String, java.util.List)
     */
    public String toStorageFormat() {
        return fullName + "," + referenceNumber + "," + entryDateTime.format(dateFormatting);
    }

    /**
     * A helper method to load entries from an external file.
     * @param entry : The full student entry detail.
     * @return Student object, if there are any to load.
     */
    public static Student fromStorageFormat(String entry) {
        String[] parts = entry.split(",");
        if (parts.length != 3) return null;
        ZonedDateTime dateTime = ZonedDateTime.parse(parts[2], dateFormatting);
        return new Student(parts[0], parts[1], dateTime);
    }
}