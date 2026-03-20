package seedu.clinic.model;

import javafx.collections.ObservableList;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Doctor;
import seedu.clinic.model.person.Person;

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

    /**
     * Returns an unmodifiable view of the doctor list.
     * This list will not contain any duplicate doctors.
     */
    ObservableList<Doctor> getDoctorList();
}
