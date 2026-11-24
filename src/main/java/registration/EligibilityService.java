package registration;

import java.util.HashSet;
import java.util.Set;

public class EligibilityService {
    /** Returns true if the student has completed all prerequisites of the course. */
    public boolean hasPrerequisites(Student student, Course course) {
        return missingPrerequisites(student, course).isEmpty();
    }

    /** Returns the missing prerequisite codes (empty set if none). */
    public Set<String> missingPrerequisites(Student student, Course course) {
        Set<String> missing = new HashSet<>(course.getPrerequisites());
        missing.removeAll(student.getCompletedCourses());
        return missing;
    }

    /** Decision-table method: eligibility based on invalid input, prerequisites and already-completed courses. */
    public boolean isEligible(Student student, Course course) {
        if (student == null || course == null) {
            return false;
        }
        if (student.getCompletedCourses().contains(course.getCode())) {
            return false;
        }
        return hasPrerequisites(student, course);
    }
}