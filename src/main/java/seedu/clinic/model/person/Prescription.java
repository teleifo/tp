package seedu.clinic.model.person;

import static seedu.clinic.commons.util.AppUtil.checkArgument;
import static seedu.clinic.commons.util.CollectionUtil.requireAllNonNull;

import seedu.clinic.commons.util.ToStringBuilder;

/**
 * Represents a prescription for medication.
 *
 * TODO: Add isFulfilled boolean flag for prescription status
 * TODO: Implement dispensePrescription() to mark fulfilled prescriptions
 * TODO: Implement getSummary() method for prescription slip
 */
public class Prescription {

    public static final String MESSAGE_CONSTRAINTS =
            "Prescription fields should not be blank";
    private static final String VALIDATION_REGEX = "[^\\s].*";

    private final String medicationName;
    private final String dosage;
    private final String frequency;
    private final Pharmacist dispensedBy;

    /**
     * Constructs a {@code Prescription}.
     */
    public Prescription(String medicationName, String dosage, String frequency, Pharmacist dispensedBy) {
        requireAllNonNull(medicationName, dosage, frequency, dispensedBy);
        checkArgument(isValidField(medicationName), MESSAGE_CONSTRAINTS);
        checkArgument(isValidField(dosage), MESSAGE_CONSTRAINTS);
        checkArgument(isValidField(frequency), MESSAGE_CONSTRAINTS);
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.dispensedBy = dispensedBy;
    }

    /**
     * Returns true if a given string is a valid prescription field value.
     */
    public static boolean isValidField(String value) {
        requireAllNonNull(value);
        return value.matches(VALIDATION_REGEX);
    }

    public String getMedicationName() {
        return medicationName;
    }

    public String getDosage() {
        return dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public Pharmacist getDispensedBy() {
        return dispensedBy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("medicationName", medicationName)
                .add("dosage", dosage)
                .add("frequency", frequency)
                .add("dispensedBy", dispensedBy)
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Prescription)) {
            return false;
        }

        Prescription otherPrescription = (Prescription) other;
        return medicationName.equals(otherPrescription.medicationName)
                && dosage.equals(otherPrescription.dosage)
                && frequency.equals(otherPrescription.frequency)
                && dispensedBy.equals(otherPrescription.dispensedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(medicationName, dosage, frequency, dispensedBy);
    }
}
