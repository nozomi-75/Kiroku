import java.util.ArrayList;
import java.util.List;

public class SearchUtils {
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