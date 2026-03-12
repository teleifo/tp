package seedu.address.model.person;

import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a patient in the address book.
 */
public class Patient extends Person {

	/**
	 * Every field must be present and not null.
	 */
	public Patient(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
		super(name, phone, email, address, tags);
	}
}
