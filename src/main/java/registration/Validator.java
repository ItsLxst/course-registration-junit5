package registration;

/**
 * Member 1 - Student ID and course code validation.
 *
 * <ul>
 *   <li>Student ID: exactly 8 ASCII digits, like 20231234.</li>
 *   <li>Course code: exactly 3 uppercase ASCII letters followed by 3 ASCII digits, like SWE431.</li>
 * </ul>
 *
 * Missing input (null, empty or only spaces) is a programming error and throws
 * {@link IllegalArgumentException}, the same way {@link WithdrawalService} treats null input.
 * Input that is present but in the wrong format returns false.
 */
public class Validator {

    public static final int STUDENT_ID_LENGTH = 8;
    public static final int COURSE_LETTERS = 3;
    public static final int COURSE_DIGITS = 3;

    /** Std no: 8 numbers (like 20231234). */
    public boolean isValidStudentId(String studentId) {
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("Student ID must not be empty");
        }
        if (studentId.length() != STUDENT_ID_LENGTH) {
            return false;
        }
        for (int i = 0; i < studentId.length(); i++) {
            if (!isAsciiDigit(studentId.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /** Subject code: 3 capital letter + 3 number (like SWE431). */
    public boolean isValidCourseCode(String courseCode) {
        if (courseCode == null || courseCode.isBlank()) {                 // D1
            throw new IllegalArgumentException("Course code must not be empty");
        }
        if (courseCode.length() != COURSE_LETTERS + COURSE_DIGITS) {     // D2
            return false;
        }
        for (int i = 0; i < COURSE_LETTERS; i++) {                       // D3
            if (!isUpperAsciiLetter(courseCode.charAt(i))) {              // D4
                return false;
            }
        }
        for (int i = COURSE_LETTERS; i < courseCode.length(); i++) {     // D5
            if (!isAsciiDigit(courseCode.charAt(i))) {                    // D6
                return false;
            }
        }
        return true;
    }

    // Character.isDigit() is not used on purpose: it also accepts non-ASCII digits
    // such as Arabic-Indic digits (defect D-01, see ValidatorRegressionTest).
    private static boolean isAsciiDigit(char c) {
        return c >= '0' && c <= '9';
    }

    // Character.isUpperCase() is not used either: it accepts letters like Ç and Ş.
    private static boolean isUpperAsciiLetter(char c) {
        return c >= 'A' && c <= 'Z';
    }
}
