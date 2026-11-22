package registration;

import java.util.Set;

public class EligibilityService {
    /** Returns true if the student has completed all prerequisites of the course. */
    public boolean hasPrerequisites(Student student, Course course) {
        throw new UnsupportedOperationException("Member 2");
    }

    /** Returns the missing prerequisite codes (empty set if none). */
    public Set<String> missingPrerequisites(Student student, Course course) {
        throw new UnsupportedOperationException("Member 2");
    }

    /** Decision-table method: eligibility based on invalid input, prerequisites and already-completed courses. */
    public boolean isEligible(Student student, Course course) {
        throw new UnsupportedOperationException("Member 2");
    }
}