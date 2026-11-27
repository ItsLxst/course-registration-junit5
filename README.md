# Course Registration System (SWE431 JUnit 5 group project)

A small course registration system in Java 17, tested with JUnit 5.

## Requirements

- Java 17 or newer: `java -version`
- Maven 3.8 or newer: `mvn -v`

IntelliJ IDEA also works without installing Maven: open the folder, let it import `pom.xml`, then right-click `src/test/java` and choose Run 'All Tests'.

## Run the tests

```
mvn clean test
```

The coverage report (JaCoCo) is written to `target/site/jacoco/index.html`. Open it in a browser.

Run one test class:

```
mvn test -Dtest=ValidatorTest
```

## Project areas

| Member | Name | Area | Code | Tests |
|---|---|---|---|---|
| 1 | Yasser Aji | Student ID and course code validation | `Validator` | `ValidatorTest`, `ValidatorRegressionTest` |
| 2 | Göknil Bilge | Enrollment eligibility and prerequisites | `EligibilityService` | `EligibilityServiceTest` |
| 3 | Deo Gratias| Course capacity and duplicate enrollment | `CapacityService` | |
| 4 | Furkan Kındığılı | Course withdrawal | `WithdrawalService` | `WithdrawalServiceTest` |
| 5 |  Mohammad Alnajjar| Waitlist and promotion | `WaitlistService` | |

Test design for each area is in `docs/memberN/`.

## Shared rules

- Student ID: exactly 8 digits, like `20231234`
- Course code: 3 uppercase letters then 3 digits, like `SWE431`
- Null or missing input throws `IllegalArgumentException` (exception: `EligibilityService.isEligible` returns `false` instead)
- A rule broken during registration (full course, not enrolled, and so on) throws `RegistrationException`
