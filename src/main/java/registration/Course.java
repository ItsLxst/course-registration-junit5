package registration;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class Course {
    private final String code;
    private final int capacity;
    private final Set<String> prerequisites = new HashSet<>();
    private final Set<String> enrolledStudentIds = new LinkedHashSet<>();
    private final Deque<String> waitlist = new ArrayDeque<>();

    public Course(String code, int capacity) {
        this.code = code;
        this.capacity = capacity;
    }

    public String getCode() { return code; }
    public int getCapacity() { return capacity; }
    public Set<String> getPrerequisites() { return prerequisites; }
    public Set<String> getEnrolledStudentIds() { return enrolledStudentIds; }
    public Deque<String> getWaitlist() { return waitlist; }
}