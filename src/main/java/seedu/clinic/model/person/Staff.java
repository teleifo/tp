package seedu.clinic.model.person;

import java.util.Objects;

import seedu.clinic.commons.util.ToStringBuilder;

/**
 * Represents a Staff member in the clinic.
 *
 */
public abstract class Staff extends ContactPerson {

    private static final int DEFAULT_STAFF_ID = 0;
    // have to fix, coz staffId will start with 0 again whenever we re-run the program
    // maybe handled it by the ClinicBook like Patient ID
    private static int nextStaffId = DEFAULT_STAFF_ID + 1;

    private final int staffId;

    /**
     * Constructs a Staff with the given name, phone, email, and staff ID.
     */
    public Staff(Name name, Phone phone, Email email, int staffId) {
        super(name, phone, email);
        this.staffId = staffId;
        if (staffId >= nextStaffId) {
            nextStaffId = staffId + 1;
        }
    }

    /**
     * Constructs a Staff with the given name, phone, and email.
     * Staff ID is set to DEFAULT_STAFF_ID.
     */
    public Staff(Name name, Phone phone, Email email) {
        super(name, phone, email);
        this.staffId = getNextStaffId();
    }

    private static int getNextStaffId() {
        return nextStaffId++;
    }

    public int getStaffId() {
        return staffId;
    }

    @Override
    public boolean isSamePerson(Person otherPerson) {
        return otherPerson instanceof Staff
                && super.isSamePerson(otherPerson)
                && ((Staff) otherPerson).getStaffId() == getStaffId();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Staff)) {
            return false;
        }

        Staff otherStaff = (Staff) other;
        return staffId == otherStaff.staffId
                && getName().equals(otherStaff.getName())
                && getPhone().equals(otherStaff.getPhone())
                && getEmail().equals(otherStaff.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(staffId, getName(), getPhone(), getEmail());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("staffId", staffId)
                .add("name", getName())
                .add("phone", getPhone())
                .add("email", getEmail())
                .toString();
    }
}
