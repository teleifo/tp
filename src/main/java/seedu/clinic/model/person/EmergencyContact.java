package seedu.clinic.model.person;

import static seedu.clinic.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.clinic.commons.util.ToStringBuilder;

/**
 * Represents an emergency contact for a patient.
 *
 * TODO: NOT CURRENTLY INTEGRATED
 */
public class EmergencyContact {

    private final ContactPerson contactPerson;
    private final String relationship;

    /**
     * Constructs an EmergencyContact with required fields.
     */
    public EmergencyContact(ContactPerson contactPerson, String relationship) {
        requireAllNonNull(contactPerson, relationship);
        this.contactPerson = contactPerson;
        this.relationship = relationship;
    }

    public ContactPerson getContactPerson() {
        return contactPerson;
    }

    public String getRelationship() {
        return relationship;
    }

    /**
     * Sets the emergency email by updating the contact person's email.
     */
    public void setEmergencyEmail(Email email) {
        // Note: ContactPerson is immutable, so replacement required
        requireAllNonNull(email);
    }

    /**
     * Sets the emergency phone by updating the contact person's phone.
     */
    public void setEmergencyPhone(Phone phone) {
        // Note: ContactPerson is immutable, so replacement required
        requireAllNonNull(phone);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EmergencyContact)) {
            return false;
        }

        EmergencyContact otherEmergencyContact = (EmergencyContact) other;
        return contactPerson.equals(otherEmergencyContact.contactPerson)
                && relationship.equals(otherEmergencyContact.relationship);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactPerson, relationship);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("contactPerson", contactPerson)
                .add("relationship", relationship)
                .toString();
    }
}
