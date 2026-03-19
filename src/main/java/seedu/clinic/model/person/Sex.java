package seedu.clinic.model.person;

/**
 * Represents the biological sex of a patient.
 *
 * TODO:NOT CURRENTLY INTEGRATED
 */
public enum Sex {
    MALE("Male"),
    FEMALE("Female"),
    INTERSEX("Intersex");

    private final String displayName;

    Sex(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
