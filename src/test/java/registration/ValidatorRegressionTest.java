package registration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Member 1 - Regression test for defect D-01.
 *
 * Defect: the first version of Validator checked digits with Character.isDigit(c).
 * That method returns true for any Unicode digit, so an ID typed on an Arabic keyboard
 * (٢٠٢٣١٢٣٤ = 20231234) was accepted, even though the rule is ASCII digits 0-9 only.
 *
 * With the defect present both tests fail. After the fix (isAsciiDigit) both pass.
 * Evidence: docs/member1/regression-before-fix.png and regression-after-fix.png
 */
class ValidatorRegressionTest {

    private final Validator validator = new Validator();

    /** Expected: false. "٢٠٢٣١٢٣٤" has 8 characters but they are not ASCII digits. */
    @Test
    void studentIdWithArabicIndicDigitsIsRejected() {
        assertFalse(validator.isValidStudentId("٢٠٢٣١٢٣٤"));
    }

    /** Expected: false. "SWE٤٣١" has the right shape but the digits are not ASCII. */
    @Test
    void courseCodeWithArabicIndicDigitsIsRejected() {
        assertFalse(validator.isValidCourseCode("SWE٤٣١"));
    }
}
