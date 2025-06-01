public class Student {
    private String fullName;
    private String date;

    public Student(String fullName, String date) {
        this.fullName = fullName;
        this.date = date;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDate() {
        return date;
    }

    public void setFullName(String newFullName) {
        this.fullName = newFullName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void displayInfo(int index) {
        System.out.println(
            index + ". Name: " +
            fullName + ", Date: " + date
        );
    }
}
