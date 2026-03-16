package seedu.clinic.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.clinic.commons.util.AppUtil.checkArgument;

/**
 * Represents a Singapore NRIC in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidNric(String)}
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class NRIC {

    public static final String MESSAGE_CONSTRAINTS =
            "NRICs should start with S or T, followed by 7 digits, and end with a valid checksum letter";
    public static final String VALIDATION_REGEX = "[ST]\\d{7}[A-Z]";
    private static final int[] WEIGHTS = {2, 7, 6, 5, 4, 3, 2};
    private static final String CHECKSUM_CHARACTERS = "JZIHGFEDCBA";

    public final String value;

    /**
     * Constructs a {@code NRIC}.
     *
     * @param nric A valid NRIC.
     */
    public NRIC(String nric) {
        requireNonNull(nric);
        checkArgument(isValidNric(nric), MESSAGE_CONSTRAINTS);
        value = nric;
    }

    /**
     * Returns true if a given string is a valid Singapore NRIC.
     */
    public static boolean isValidNric(String test) {
        if (!test.matches(VALIDATION_REGEX)) {
            return false;
        }

        int offset = test.charAt(0) == 'T' ? 4 : 0;
        int weightedSum = offset;
        for (int index = 0; index < WEIGHTS.length; index++) {
            int digit = Character.getNumericValue(test.charAt(index + 1));
            weightedSum += digit * WEIGHTS[index];
        }

        char expectedChecksum = CHECKSUM_CHARACTERS.charAt(weightedSum % 11);
        return test.charAt(test.length() - 1) == expectedChecksum;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof NRIC)) {
            return false;
        }

        NRIC otherNric = (NRIC) other;
        return value.equals(otherNric.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
