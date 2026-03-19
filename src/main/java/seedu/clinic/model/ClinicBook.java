package seedu.clinic.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import seedu.clinic.commons.util.ToStringBuilder;
import seedu.clinic.model.person.Person;
import seedu.clinic.model.person.Doctor;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.UniquePersonList;

/**
 * Wraps all data at the clinicbook level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class ClinicBook implements ReadOnlyClinicBook {

    private final UniquePersonList persons;
    private final UniquePersonList patients;
    // id counter for Patient
    private int nextId = 1;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        patients = new UniquePersonList();
    }

    public ClinicBook() {}

    /**
     * Creates an ClinicBook using the Persons in the {@code toBeCopied}
     */
    public ClinicBook(ReadOnlyClinicBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        for (Person p : persons) {
            if (p.getId() == 0) {
                p.setId(getNextId());
            }
        }
        this.persons.setPersons(persons);
    }

    /**
     * Resets the existing data of this {@code ClinicBook} with {@code newData}.
     */
    public void resetData(ReadOnlyClinicBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setPatients(newData.getPatientList());
    }

    /**
     * Replaces the contents of the patient list with {@code patients}.
     */
    public void setPatients(List<Patient> patients) {
        requireNonNull(patients);
        List<Person> personsToSet = new ArrayList<>(patients);
        this.patients.setPersons(personsToSet);
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the clinic book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to clinic book.
     * The person must not already exist in clinic book.
     */
    public void addPerson(Person p) {
        // If ID is 0 (default), assign a new one
        if (p.getId() == 0) {
            p.setId(getNextId());
        }
        persons.add(p);
    }

    /**
     * Adds a Patient to clinic book.
     * The patient must not already exist in clinic book.
     */
    public void addPatient(Patient patient) {
        requireNonNull(patient);
        if (patient.getId() == 0) {
            patient.setId(getNextId());
        }
        persons.add(patient);
        patients.add(patient);
    }

    /**
     * Returns the next available ID and increments the counter
     */
    public int getNextId() {
        int maxId = persons.stream()
                .mapToInt(Person::getId)
                .max()
                .orElse(0);
        nextId = maxId + 1;
        return nextId++;
    }


    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in clinic book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in clinic book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        // assign new patient id if editedPerson has no id
        if (editedPerson.getId() == 0) {
            editedPerson.setId(getNextId());
        }
        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code ClinicBook}.
     * {@code key} must exist in clinic book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Patient> getPatientList() {
        return patients.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ClinicBook)) {
            return false;
        }

        ClinicBook otherClinicBook = (ClinicBook) other;
        return persons.equals(otherClinicBook.persons);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
