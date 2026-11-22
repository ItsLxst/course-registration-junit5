package registration;

public class WaitlistService {
    /** Adds the student to the end of the waitlist (duplicates are not allowed). */
    public void addToWaitlist(Student student, Course course) {
        throw new UnsupportedOperationException("Member 5");
    }

    /** Enrolls the student at the head of the waitlist and returns that student's id. Returns null if the waitlist is empty. */
    public String promoteNext(Course course) {
        throw new UnsupportedOperationException("Member 5");
    }

    public int waitlistPosition(Student student, Course course) {
        throw new UnsupportedOperationException("Member 5");
    }
}