package seedu.clinic.model.person;

/**
 * Represents a staff member in the clinic.
 */
public abstract class Staff extends ContactPerson {

    /**
     * Constructs a Staff with the given name, phone, and email.
     * ID will be assigned by {@code ClinicBook}.
     */
    public Staff(Name name, Phone phone, Email email) {
        super(name, phone, email);
    }

    /**
     * Constructs a Staff with the given name, phone, email, and explicit global person ID.
     */
    public Staff(Name name, Phone phone, Email email, int id) {
        super(name, phone, email, id);
    }
}
