package registration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Member 4 - Course withdrawal tests.
 *
 * Students are put into courses directly through the Course/Student collections
 * (not through CapacityService.enroll), so these tests depend only on WithdrawalService.
 */
class WithdrawalServiceTest {

    private WithdrawalService service;
    private Course course;
    private Student ayse;
    private Student mehmet;

    @BeforeEach
    void setUp() {
        service = new WithdrawalService();
        course = new Course("SWE431", 3);
        ayse = new Student("20231234", "Ayse");
        mehmet = new Student("20235678", "Mehmet");
    }

    /** Enrolls the student on both sides: course roster and student record. */
    private static void enrollDirectly(Student student, Course course) {
        course.getEnrolledStudentIds().add(student.getId());
        student.getEnrolledCourses().add(course.getCode());
    }

    /** Test double: records promoteNext calls instead of using Member 5's real implementation. */
    private static class RecordingWaitlistService extends WaitlistService {
        final List<Course> promotedCourses = new ArrayList<>();

        @Override
        public String promoteNext(Course course) {
            promotedCourses.add(course);
            return null;
        }
    }

    // ---------- Valid ----------

    /** Expected: only the withdrawing student loses the seat; other students stay enrolled. */
    @Test
    void withdrawRemovesStudentFromCourse() {
        enrollDirectly(ayse, course);
        enrollDirectly(mehmet, course);

        service.withdraw(ayse, course);

        assertFalse(course.getEnrolledStudentIds().contains(ayse.getId()));
        assertTrue(course.getEnrolledStudentIds().contains(mehmet.getId()));
        assertEquals(1, course.getEnrolledStudentIds().size());
    }

    // ---------- Invalid ----------

    /** Expected: RegistrationException (contract in the Javadoc) and no change to the roster. */
    @Test
    void withdrawThrowsWhenStudentNotEnrolled() {
        enrollDirectly(mehmet, course);

        RegistrationException ex = assertThrows(RegistrationException.class,
                () -> service.withdraw(ayse, course));

        assertTrue(ex.getMessage().contains(ayse.getId()));
        assertEquals(1, course.getEnrolledStudentIds().size());
        assertTrue(course.getEnrolledStudentIds().contains(mehmet.getId()));
    }

    /** Expected: after the first withdrawal the student is no longer enrolled, so a second one must fail. */
    @Test
    void secondWithdrawalOfSameStudentThrows() {
        enrollDirectly(ayse, course);
        service.withdraw(ayse, course);

        assertThrows(RegistrationException.class, () -> service.withdraw(ayse, course));
        assertTrue(course.getEnrolledStudentIds().isEmpty());
    }

    /** Expected: a waitlisted student is not enrolled, so withdraw fails and the waitlist is untouched. */
    @Test
    void waitlistedStudentCannotWithdraw() {
        enrollDirectly(mehmet, course);
        course.getWaitlist().addLast(ayse.getId());

        assertThrows(RegistrationException.class, () -> service.withdraw(ayse, course));

        assertEquals(1, course.getWaitlist().size());
        assertEquals(ayse.getId(), course.getWaitlist().peekFirst());
        assertEquals(1, course.getEnrolledStudentIds().size());
    }

    /** Expected: null input is a programming error -> IllegalArgumentException, state unchanged. */
    @Test
    void withdrawRejectsNullInput() {
        enrollDirectly(ayse, course);

        assertThrows(IllegalArgumentException.class, () -> service.withdraw(null, course));
        assertThrows(IllegalArgumentException.class, () -> service.withdraw(ayse, null));

        assertTrue(course.getEnrolledStudentIds().contains(ayse.getId()));
    }

    // ---------- Boundary ----------

    /** Boundary 1 -> 0: withdrawing the only student leaves an empty roster, the course itself remains. */
    @Test
    void withdrawingLastStudentLeavesCourseEmpty() {
        enrollDirectly(ayse, course);

        service.withdraw(ayse, course);

        assertTrue(course.getEnrolledStudentIds().isEmpty());
        assertEquals(3, course.getCapacity());
    }

    /** Boundary full -> capacity-1: withdrawing from a full course frees exactly one seat. */
    @Test
    void withdrawFromFullCourseFreesExactlyOneSeat() {
        Student can = new Student("20239999", "Can");
        enrollDirectly(ayse, course);
        enrollDirectly(mehmet, course);
        enrollDirectly(can, course);
        assertEquals(course.getCapacity(), course.getEnrolledStudentIds().size()); // precondition: full

        service.withdraw(mehmet, course);

        assertEquals(course.getCapacity() - 1, course.getEnrolledStudentIds().size());
        assertFalse(course.getEnrolledStudentIds().contains(mehmet.getId()));
    }

    // ---------- Waitlist interaction (Member 5) ----------

    /** Expected: a freed seat with a non-empty waitlist triggers exactly one promotion for this course. */
    @Test
    void withdrawPromotesNextStudentWhenWaitlistNotEmpty() {
        RecordingWaitlistService waitlist = new RecordingWaitlistService();
        WithdrawalService serviceWithWaitlist = new WithdrawalService(waitlist);
        enrollDirectly(ayse, course);
        course.getWaitlist().addLast(mehmet.getId());

        serviceWithWaitlist.withdraw(ayse, course);

        assertEquals(List.of(course), waitlist.promotedCourses);
    }

    /** Expected: with an empty waitlist there is nobody to promote, so promoteNext is not called. */
    @Test
    void withdrawDoesNotPromoteWhenWaitlistEmpty() {
        RecordingWaitlistService waitlist = new RecordingWaitlistService();
        WithdrawalService serviceWithWaitlist = new WithdrawalService(waitlist);
        enrollDirectly(ayse, course);

        serviceWithWaitlist.withdraw(ayse, course);

        assertTrue(waitlist.promotedCourses.isEmpty());
    }

    // ---------- Regression ----------

    /**
     * Regression test.
     * Defect: withdraw() removed the student from the course roster but did not remove the course
     * from the student's own enrolledCourses, so the two records disagreed after withdrawal.
     * With the defect present this test fails; after the fix it passes.
     */
    @Test
    void regression_withdrawAlsoUpdatesStudentRecord() {
        Course other = new Course("SWE432", 30);
        enrollDirectly(ayse, course);
        enrollDirectly(ayse, other);

        service.withdraw(ayse, course);

        assertFalse(ayse.getEnrolledCourses().contains("SWE431"));
        assertTrue(ayse.getEnrolledCourses().contains("SWE432"));
    }
}
