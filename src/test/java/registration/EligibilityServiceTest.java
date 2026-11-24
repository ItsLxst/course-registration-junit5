package registration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EligibilityServiceTest {
    private final EligibilityService service = new EligibilityService();

    @Test
    void eligibleWhenAllPrerequisitesCompleted() {
        Student student = new Student("20231234", "Ayse");
        student.getCompletedCourses().add("SWE101");
        Course course = new Course("SWE201", 30);
        course.getPrerequisites().add("SWE101");

        assertTrue(service.isEligible(student, course));
    }

    @Test
    void notEligibleWhenPrerequisiteMissing() {
        Student student = new Student("20231234", "Ayse");
        Course course = new Course("SWE201", 30);
        course.getPrerequisites().add("SWE101");

        assertFalse(service.isEligible(student, course));
        assertTrue(service.missingPrerequisites(student, course).contains("SWE101"));
    }

    @Test
    void eligibleWhenCourseHasNoPrerequisites() {
        Student student = new Student("20231234", "Ayse");
        Course course = new Course("SWE101", 30);

        assertTrue(service.isEligible(student, course));
    }

    @Test
    void notEligibleWhenStudentIsNull() {
        Course course = new Course("SWE101", 30);

        assertFalse(service.isEligible(null, course));
    }

    @Test
    void notEligibleWhenCourseAlreadyCompleted() {
        Student student = new Student("20231234", "Ayse");
        student.getCompletedCourses().add("SWE201");
        Course course = new Course("SWE201", 30);

        assertFalse(service.isEligible(student, course));
    }

}