package registration;

import java.util.HashSet;
import java.util.Set;

public class Student {
    private final String id;
    private final String name;
    private final Set<String> completedCourses = new HashSet<>();
    private final Set<String> enrolledCourses = new HashSet<>();

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Set<String> getCompletedCourses() { return completedCourses; }
    public Set<String> getEnrolledCourses() { return enrolledCourses; }
}