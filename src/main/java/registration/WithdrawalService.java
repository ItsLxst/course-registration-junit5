package registration;

/**
 * Member 4 - Course withdrawal.
 */
public class WithdrawalService {
    private final WaitlistService waitlistService;

    public WithdrawalService() {
        this(new WaitlistService());
    }

    public WithdrawalService(WaitlistService waitlistService) {
        this.waitlistService = waitlistService;
    }

    /**
     * Withdraws the student from the course.
     * <ul>
     *   <li>Removes the student from the course roster and the course from the student's enrolled courses.</li>
     *   <li>If the course has a waitlist, the freed seat goes to the next waitlisted student
     *       via {@link WaitlistService#promoteNext(Course)}.</li>
     * </ul>
     *
     * @throws IllegalArgumentException if student or course is null
     * @throws RegistrationException if the student is not enrolled in the course
     *         (a student who is only on the waitlist is not enrolled)
     */
    public void withdraw(Student student, Course course) {
        if (student == null || course == null) {
            throw new IllegalArgumentException("Student and course must not be null");
        }
        if (!course.getEnrolledStudentIds().contains(student.getId())) {
            throw new RegistrationException(
                    "Student " + student.getId() + " is not enrolled in " + course.getCode());
        }

        course.getEnrolledStudentIds().remove(student.getId());
        student.getEnrolledCourses().remove(course.getCode());

        if (!course.getWaitlist().isEmpty()) {
            waitlistService.promoteNext(course);
        }
    }
}
