package kiroku;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for searching log entries.
 * @see KirokuController
 */
public class SearchUtils {
    /**
     * Helper method to search log entries.
     * @see KirokuController#searchStudents()
     * 
     * @param students : The list of student objects to read from.
     * @param query : String to search from the list.
     * @param byName : Identify whether search should be against name.
     * 
     * @return results : List of entries that contains the query.
     */
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