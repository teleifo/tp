package seedu.clinic.storage;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.clinic.commons.exceptions.IllegalValueException;
import seedu.clinic.model.person.LabTest;
import seedu.clinic.model.person.LabTest.TestType;

/**
 * Jackson-friendly version of {@link LabTest}.
 */
class JsonAdaptedLabTest {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "LabTest's %s field is missing!";
    private static final String INVALID_TEST_TYPE_MESSAGE = "LabTest's testType is invalid!";

    private final String testName;
    private final String testType;
    private final int orderedBy;
    private final String orderedDate;

    /**
     * Constructs a {@code JsonAdaptedLabTest} with the given lab test details.
     */
    @JsonCreator
    public JsonAdaptedLabTest(@JsonProperty("testName") String testName,
                              @JsonProperty("testType") String testType,
                              @JsonProperty("orderedBy") int orderedBy,
                              @JsonProperty("orderedDate") String orderedDate) {
        this.testName = testName;
        this.testType = testType;
        this.orderedBy = orderedBy;
        this.orderedDate = orderedDate;
    }

    /**
     * Converts a given {@code LabTest} into this class for Jackson use.
     */
    public JsonAdaptedLabTest(LabTest source) {
        testName = source.getTestName();
        testType = source.getTestType().name();
        orderedBy = source.getOrderedBy();
        orderedDate = source.getOrderedDate().toString();
    }

    /**
     * Converts this Jackson-friendly adapted lab test object into the model's {@code LabTest} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public LabTest toModelType() throws IllegalValueException {
        if (testName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "testName"));
        }

        if (testType == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "testType"));
        }

        if (!TestType.isValidTestType(testType)) {
            throw new IllegalValueException(INVALID_TEST_TYPE_MESSAGE);
        }
        final TestType modelTestType = TestType.valueOf(testType.trim().toUpperCase());

        if (orderedBy == 0) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "orderedBy"));
        }

        if (orderedDate == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "orderedDate"));
        }
        final LocalDate modelOrderedDate = LocalDate.parse(orderedDate);

        return new LabTest(testName, modelTestType, orderedBy, modelOrderedDate);
    }
}
