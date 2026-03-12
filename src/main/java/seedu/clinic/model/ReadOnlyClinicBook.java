package seedu.clinic.model;

import javafx.collections.ObservableList;
import seedu.clinic.model.person.Person;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyClinicBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

}
