package seedu.clinic.model.person;

import static seedu.clinic.commons.util.AppUtil.checkArgument;
import static seedu.clinic.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.util.Objects;

import seedu.clinic.commons.util.ToStringBuilder;

/**
 * Represents a lab or imaging test ordered for a Patient by a Doctor.
 */
public class LabTest {

    public static final String MESSAGE_CONSTRAINTS =
            "Test name should not be blank";
    public static final String MESSAGE_TYPE_CONSTRAINTS =
            "Test type must be either 'LAB' or 'IMAGING'";
    private static final String VALIDATION_REGEX = "[^\\s].*";

    /**
     * The type of diagnostic test that can be ordered.
     */
    public enum TestType {
        LAB, IMAGING;

        /**
         * Returns true if the given string is a valid test type.
         */
        public static boolean isValidTestType(String value) {
            requireAllNonNull(value);
            try {
                TestType.valueOf(value.trim().toUpperCase());
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
    }

    private final String testName;
    private final TestType testType;
    private final int orderedBy; // doctor ID
    private final LocalDate orderedDate;

    /**
     * Constructs a {@code LabTest} with the current date as the ordered date.
     *
     * @param testName  A valid test name.
     * @param testType  The type of the test (LAB or IMAGING).
     * @param orderedBy The ID of the doctor who ordered the test.
     */
    public LabTest(String testName, TestType testType, int orderedBy) {
        this(testName, testType, orderedBy, LocalDate.now());
    }

    /**
     * Constructs a {@code LabTest}.
     *
     * @param testName    A valid test name.
     * @param testType    The type of the test (LAB or IMAGING).
     * @param orderedBy   The ID of the doctor who ordered the test.
     * @param orderedDate The date the test was ordered.
     */
    public LabTest(String testName, TestType testType, int orderedBy, LocalDate orderedDate) {
        requireAllNonNull(testName, testType, orderedBy, orderedDate);
        checkArgument(isValidTestName(testName), MESSAGE_CONSTRAINTS);
        this.testName = testName;
        this.testType = testType;
        this.orderedBy = orderedBy;
        this.orderedDate = orderedDate;
    }

    /**
     * Returns true if a given string is a valid test name.
     */
    public static boolean isValidTestName(String test) {
        requireAllNonNull(test);
        return test.matches(VALIDATION_REGEX);
    }

    public String getTestName() {
        return testName;
    }

    public TestType getTestType() {
        return testType;
    }

    public int getOrderedBy() {
        return orderedBy;
    }

    public LocalDate getOrderedDate() {
        return orderedDate;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof LabTest)) {
            return false;
        }

        LabTest otherTest = (LabTest) other;
        return testName.equals(otherTest.testName)
                && testType == otherTest.testType
                && orderedBy == otherTest.orderedBy
                && orderedDate.equals(otherTest.orderedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testName, testType, orderedBy, orderedDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("testName", testName)
                .add("testType", testType)
                .add("orderedBy", orderedBy)
                .add("orderedDate", orderedDate)
                .toString();
    }
}
