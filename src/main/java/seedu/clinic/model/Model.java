package seedu.clinic.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.clinic.commons.core.GuiSettings;
import seedu.clinic.model.person.Diagnosis;
import seedu.clinic.model.person.Doctor;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Person;
import seedu.clinic.model.person.Pharmacist;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /** {@code Predicate} that always evaluate to true */
    Predicate<Doctor> PREDICATE_SHOW_ALL_DOCTORS = unused -> true;

    /** {@code Predicate} that always evaluate to true */
    Predicate<Pharmacist> PREDICATE_SHOW_ALL_PHARMACISTS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' clinic book file path.
     */
    Path getClinicBookFilePath();

    /**
     * Sets the user prefs' clinic book file path.
     */
    void setClinicBookFilePath(Path clinicBookFilePath);

    /**
     * Replaces clinic book data with the data in {@code clinicBook}.
     */
    void setClinicBook(ReadOnlyClinicBook clinicBook);

    /** Returns the ClinicBook */
    ReadOnlyClinicBook getClinicBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in clinic book.
     */
    boolean hasPerson(Person person);

    /**
     * Returns true if a doctor with the same identity as {@code doctor} exists in clinic book.
     */
    boolean hasDoctor(Doctor doctor);

    /**
     * Returns true if a pharmacist with the same identity as {@code pharmacist} exists in clinic book.
     */
    boolean hasPharmacist(Pharmacist pharmacist);

    /**
     * Deletes the given person.
     * The person must exist in clinic book.
     */
    void deletePerson(Person target);

    /**
     * Deletes the given doctor.
     * The person must exist in clinic book.
     */
    void deleteDoctor(Doctor target);

    /**
     * Deletes the given pharmacist.
     * The person must exist in clinic book.
     */
    void deletePharmacist(Pharmacist target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in clinic book.
     */
    void addPerson(Person person);

    /**
     * Adds the given doctor.
     * {@code doctor} must not already exist in clinic book.
     */
    void addDoctor(Doctor doctor);

    /**
     * Adds the given pharmacist.
     * {@code pharmacist} must not already exist in clinic book.
     */
    void addPharmacist(Pharmacist pharmacist);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in clinic book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in clinic book.
     */
    void setPerson(Person target, Person editedPerson);

    /**
     * Replaces the given doctor {@code target} with {@code editedDoctor}.
     * {@code target} must exist in clinic book.
     * The person identity of {@code editedDoctor} must not be the same as another existing doctor in clinic book.
     */

    void setDoctor(Doctor target, Doctor editedDoctor);

    /**
     * Replaces the given pharmacist {@code target} with {@code editedPharmacist}.
     * {@code target} must exist in clinic book.
        * The person identity of {@code editedPharmacist} must not be the same as another existing
        * pharmacist in clinic book.
     */
    void setPharmacist(Pharmacist target, Pharmacist editedPharmacist);

    /**
     * Adds the given diagnosis to the target patient.
     * {@code target} must exist in clinic book.
     */
    void addDiagnosis(Patient target, Diagnosis diagnosis);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /** Returns an unmodifiable view of the filtered doctor list */
    ObservableList<Doctor> getFilteredDoctorList();

    /** Returns an unmodifiable view of the filtered pharmacist list */
    ObservableList<Pharmacist> getFilteredPharmacistList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Updates the filter of the filtered doctor list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredDoctorList(Predicate<Doctor> predicate);

    /**
     * Updates the filter of the filtered pharmacist list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPharmacistList(Predicate<Pharmacist> predicate);

    /** Returns an unmodifiable view of the filtered patient list */
    ObservableList<Patient> getFilteredPatientList();
}
