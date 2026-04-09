package seedu.clinic.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.clinic.commons.util.ToStringBuilder;
import seedu.clinic.model.person.Diagnosis;
import seedu.clinic.model.person.LabTest;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Person;
import seedu.clinic.model.person.UniquePersonList;

/**
 * Wraps all data at the clinicbook level.
 * Duplicates are not allowed (by .isSamePerson comparison).
 */
public class ClinicBook implements ReadOnlyClinicBook {

    private final UniquePersonList<Person> persons;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList<Person>();
    }

    public ClinicBook() {}

    /**
     * Creates an ClinicBook using the Persons in the {@code toBeCopied}
     */
    public ClinicBook(ReadOnlyClinicBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        for (Person p : persons) {
            assignIdIfMissing(p);
        }
        this.persons.setPersons(persons);
    }

    /**
     * Resets the existing data of this {@code ClinicBook} with {@code newData}.
     */
    public void resetData(ReadOnlyClinicBook newData) {
        requireNonNull(newData);
        setPersons(newData.getPersonList());
    }

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
    public void addPerson(Person person) {
        assignIdIfMissing(person);
        persons.add(person);
    }

    /**
     * Returns the next available ID and increments the counter.
     */
    public int getNextId() {
        return persons.stream()
                .mapToInt(Person::getId)
                .max()
                .orElse(0) + 1;
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in clinic book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in clinic book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);
        assignIdIfMissing(editedPerson);
        persons.setPerson(target, editedPerson);
    }

    private void assignIdIfMissing(Person person) {
        if (person.getId() == 0) {
            person.setId(getNextId());
        }
    }

    /**
     * Removes {@code key} from this {@code ClinicBook}.
     * {@code key} must exist in clinic book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    /**
     * Adds a diagnosis to clinic book.
     */
    public void addDiagnosis(Patient target, Diagnosis diagnosis) {
        requireNonNull(target);
        requireNonNull(diagnosis);

        Patient editedPatient = new Patient(
            target.getName(),
            target.getPhone(),
            target.getEmail(),
            target.getAddress(),
            target.getTags(),
            target.getNric(),
            target.getDateOfBirth(),
            target.getSex(),
            target.getId());

        target.getDiagnoses().forEach(editedPatient::addDiagnosis);
        target.getLabTests().forEach(editedPatient::addLabTest);
        editedPatient.addDiagnosis(diagnosis);

        setPerson(target, editedPatient);
    }

    /**
     * Adds a lab test to the target patient in clinic book.
     */
    public void addLabTest(Patient target, LabTest labTest) {
        requireNonNull(target);
        requireNonNull(labTest);

        Patient editedPatient = new Patient(
            target.getName(),
            target.getPhone(),
            target.getEmail(),
            target.getAddress(),
            target.getTags(),
            target.getNric(),
            target.getDateOfBirth(),
            target.getSex(),
            target.getId());

        target.getDiagnoses().forEach(editedPatient::addDiagnosis);
        target.getLabTests().forEach(editedPatient::addLabTest);
        editedPatient.addLabTest(labTest);

        setPerson(target, editedPatient);
    }

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
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

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
