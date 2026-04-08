package seedu.clinic.model.person;

import static seedu.clinic.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.clinic.commons.util.ToStringBuilder;

/**
 * Represents a Person in clinic book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 *
 * TODO: Remove Address field and move to Patient subclass
 * TODO: Implement automatic ID formatting using ID_FORMAT
 */
public class Person {

    // May change to Person
    public static final String ROLE = "Unknown";
    // TODO: Move this to Staff/Patient subclasses
    private static final int DEFAULT_ID = 0;
    // TODO: Implement ID_FORMAT usage in automatic ID assignment (e.g., P001, P002)
    // This format will be used when generating IDs from DEFAULT_ID or similar constants
    private static final String ID_FORMAT = "P%03d";

    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Address address;
    private int id;

    /**
     * Every field must be present and not null.
     * ID will be assigned by ClinicBook
     */
    public Person(Name name, Phone phone, Email email, Address address, int id) {
        requireAllNonNull(name, phone, email, address);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.id = id;
    }

    /**
     * Constructor for Person with explicit address and automatic ID assignment.
     */
    public Person(Name name, Phone phone, Email email, Address address) {
        this(name, phone, email, address, DEFAULT_ID);
    }

    /**
     * Every field must be present and not null.
     * ID will be assigned by ClinicBook
     */
    public Person(Name name, Phone phone, Email email, int id) {
        this(name, phone, email, new Address("N/A"), id);
    }

    /**
     * Constructor for Person with automatic ID assignment.
     */
    public Person(Name name, Phone phone, Email email) {
        this(name, phone, email, DEFAULT_ID);
    }

    public String getRole() {
        return ROLE;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns true if both persons have the same persisted ID.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getId() == getId();
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;

        return id == otherPerson.id
                && name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(id, name, phone, email, address);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("id", id)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .toString();
    }

}
