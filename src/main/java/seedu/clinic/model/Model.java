package seedu.clinic.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.clinic.commons.core.GuiSettings;
import seedu.clinic.model.person.Doctor;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

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
     * Deletes the given person.
     * The person must exist in clinic book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in clinic book.
     */
    void addPerson(Person person);

    /**
     * Adds the given patient.
     * {@code patient} must not already exist in clinic book.
     */
    void addPatient(Patient patient);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in clinic book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in clinic book.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);
}
