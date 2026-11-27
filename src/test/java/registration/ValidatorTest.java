package registration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Member 1 - Student ID and course code validation tests.
 *
 * Techniques: equivalence partitioning, boundary value analysis on lengths
 * (one shorter, exact, one longer), exception assertions for missing input, and basis path
 * tests for isValidCourseCode (CFG in docs/member1).
 */
class ValidatorTest {

    private final Validator validator = new Validator();

    // ---------- Student ID: valid, invalid and boundary values ----------

    /** Expected: only exactly 8 ASCII digits is valid. Length is tested at 7, 8 and 9. */
    @ParameterizedTest(name = "[{index}] \"{0}\" -> {1} ({2})")
    @CsvSource(delimiter = '|', textBlock = """
            20231234    | true  | valid: 8 digits
            00000000    | true  | valid: all zeros is still 8 digits
            99999999    | true  | valid: all nines
            2023123     | false | boundary: 7 digits
            202312345   | false | boundary: 9 digits
            2           | false | 1 digit
            2023123A    | false | letter at the end
            A0231234    | false | letter at the start
            2023-234    | false | symbol inside
            '2023 234'  | false | space inside
            ' 2023123'  | false | 8 characters but starts with a space
            """)
    void studentIdFormat(String id, boolean expected, String reason) {
        assertEquals(expected, validator.isValidStudentId(id), reason);
    }

    /** Expected: missing ID is a programming error, so IllegalArgumentException. 8 spaces has the right length but is still missing. */
    @ParameterizedTest(name = "[{index}] \"{0}\" throws")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "        "})
    void missingStudentIdThrows(String id) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.isValidStudentId(id));
        assertEquals("Student ID must not be empty", ex.getMessage());
    }

    // ---------- Course code: valid, invalid and boundary values ----------

    /** Expected: exactly 3 uppercase letters + 3 digits. Each part is tested one shorter and one longer. */
    @ParameterizedTest(name = "[{index}] \"{0}\" -> {1} ({2})")
    @CsvSource(delimiter = '|', textBlock = """
            SWE431   | true  | valid
            AAA000   | true  | valid: lowest letters and digits
            ZZZ999   | true  | valid: highest letters and digits
            SW431    | false | boundary: 2 letters
            SWEN431  | false | boundary: 4 letters
            SWE43    | false | boundary: 2 digits
            SWE4310  | false | boundary: 4 digits
            swe431   | false | lowercase
            Swe431   | false | mixed case
            431SWE   | false | digits before letters
            SWE4A1   | false | letter inside the digit part
            S3E431   | false | digit inside the letter part
            SWE-43   | false | symbol, right length
            'SWE 43' | false | space, right length
            ÇSE431   | false | Turkish letter is not A-Z
            """)
    void courseCodeFormat(String code, boolean expected, String reason) {
        assertEquals(expected, validator.isValidCourseCode(code), reason);
    }

    /** Expected: missing code throws IllegalArgumentException. */
    @ParameterizedTest(name = "[{index}] \"{0}\" throws")
    @NullAndEmptySource
    @ValueSource(strings = {"      "})
    void missingCourseCodeThrows(String code) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.isValidCourseCode(code));
        assertEquals("Course code must not be empty", ex.getMessage());
    }

    // ---------- Basis paths of isValidCourseCode ----------
    // CFG: docs/member1/cfg-isValidCourseCode.png, V(G) = 7.
    // Paths P5 (skip the letter loop) and P6 (skip the digit loop) are infeasible,
    // because D2 guarantees length 6, so both loops always run. 5 feasible paths -> 5 tests.

    /** P0 baseline: every check passes, returns true. */
    @Test
    void basisP0ValidCodeReturnsTrue() {
        assertTrue(validator.isValidCourseCode("SWE431"));
    }

    /** P1 flip D1, path 1-2: null input throws. */
    @Test
    void basisP1NullThrows() {
        assertThrows(IllegalArgumentException.class, () -> validator.isValidCourseCode(null));
    }

    /** P2 flip D2, path 1-3-4: wrong length returns false before any loop runs. */
    @Test
    void basisP2WrongLengthReturnsFalse() {
        assertFalse(validator.isValidCourseCode("SWE43"));
    }

    /** P3 flip D4, path 1-3-5-6-7-8: first character is not an uppercase letter. */
    @Test
    void basisP3BadLetterReturnsFalse() {
        assertFalse(validator.isValidCourseCode("sWE431"));
    }

    /** P4 flip D6: the first digit position holds a letter. */
    @Test
    void basisP4BadDigitReturnsFalse() {
        assertFalse(validator.isValidCourseCode("SWEA31"));
    }

    // ---------- Use with the shared model ----------

    /** Expected: the IDs and codes used in the other members' tests pass validation, so the suite uses valid data. */
    @Test
    void sampleDataUsedByTheTeamIsValid() {
        Student ayse = new Student("20231234", "Ayse");
        Course course = new Course("SWE431", 3);

        assertTrue(validator.isValidStudentId(ayse.getId()));
        assertTrue(validator.isValidCourseCode(course.getCode()));
    }
}
