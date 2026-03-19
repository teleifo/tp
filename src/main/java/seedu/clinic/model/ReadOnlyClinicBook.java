package seedu.clinic.model;

import javafx.collections.ObservableList;
import seedu.clinic.model.person.Diagnosis;
import seedu.clinic.model.person.Person;
import seedu.clinic.model.person.Patient;

/**
 * Unmodifiable view of an clinic book
 */
public interface ReadOnlyClinicBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the patients list.
     */
    ObservableList<Patient> getPatientList();

}
