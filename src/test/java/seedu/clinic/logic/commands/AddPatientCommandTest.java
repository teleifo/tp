package seedu.clinic.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.clinic.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.clinic.commons.core.GuiSettings;
import seedu.clinic.logic.commands.exceptions.CommandException;
import seedu.clinic.model.ClinicBook;
import seedu.clinic.model.Model;
import seedu.clinic.model.ReadOnlyClinicBook;
import seedu.clinic.model.ReadOnlyUserPrefs;
import seedu.clinic.model.person.Diagnosis;
import seedu.clinic.model.person.NRIC;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Person;
import seedu.clinic.model.person.Sex;
import seedu.clinic.testutil.PersonBuilder;

public class AddPatientCommandTest {

    @Test
    public void constructor_nullPatient_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddPatientCommand(null));
    }

    @Test
    public void execute_patientAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPatientAdded modelStub = new ModelStubAcceptingPatientAdded();
        Patient validPatient = createPatient("Amy Bee", "S1234567D");

        CommandResult commandResult = new AddPatientCommand(validPatient).execute(modelStub);

        assertEquals(String.format(AddPatientCommand.MESSAGE_SUCCESS, validPatient), commandResult.getFeedbackToUser());
        assertEquals(1, modelStub.patientsAdded.size());
        assertEquals(validPatient, modelStub.patientsAdded.get(0));
    }

    @Test
    public void execute_duplicatePatientNric_throwsCommandException() {
        Patient existingPatient = createPatient("Amy Bee", "S1234567D");
        AddPatientCommand addPatientCommand = new AddPatientCommand(createPatient("Amy Tan", "S1234567D"));
        ModelStub modelStub = new ModelStubWithPatient(existingPatient);

        assertThrows(CommandException.class, AddPatientCommand.MESSAGE_DUPLICATE_PATIENT, () ->
            addPatientCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Patient alicePatient = createPatient("Alice Pauline", "T1234567J");
        Patient bobPatient = createPatient("Bob Choo", "S1234567D");
        AddPatientCommand addAliceCommand = new AddPatientCommand(alicePatient);
        AddPatientCommand addBobCommand = new AddPatientCommand(bobPatient);

        assertTrue(addAliceCommand.equals(addAliceCommand));

        AddPatientCommand addAliceCommandCopy = new AddPatientCommand(alicePatient);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        assertFalse(addAliceCommand.equals(1));
        assertFalse(addAliceCommand.equals(null));
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        Patient patient = createPatient("Alice Pauline", "T1234567J");
        AddPatientCommand addPatientCommand = new AddPatientCommand(patient);
        String expected = AddPatientCommand.class.getCanonicalName() + "{Patient=" + patient + "}";
        assertEquals(expected, addPatientCommand.toString());
    }

    private Patient createPatient(String name, String nric) {
        Person person = new PersonBuilder()
                .withId(0)
                .withName(name)
                .build();
        return new Patient(person, Collections.emptySet(), new NRIC(nric), LocalDate.of(1990, 1, 1), Sex.FEMALE);
    }

    /**
     * A default model stub that has all methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getClinicBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setClinicBookFilePath(Path clinicBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setClinicBook(ReadOnlyClinicBook clinicBook) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyClinicBook getClinicBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addDiagnosis(Patient target, Diagnosis diagnosis) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A model stub that contains one existing patient.
     */
    private class ModelStubWithPatient extends ModelStub {
        private final ClinicBook clinicBook;

        ModelStubWithPatient(Patient patient) {
            requireNonNull(patient);
            clinicBook = new ClinicBook();
            clinicBook.addPerson(patient);
        }

        @Override
        public ReadOnlyClinicBook getClinicBook() {
            return clinicBook;
        }
    }

    /**
     * A model stub that accepts a patient add.
     */
    private class ModelStubAcceptingPatientAdded extends ModelStub {
        final ArrayList<Person> patientsAdded = new ArrayList<>();
        private final ClinicBook clinicBook = new ClinicBook();

        @Override
        public void addPerson(Person patient) {
            requireNonNull(patient);
            patientsAdded.add(patient);
            clinicBook.addPerson(patient);
        }

        @Override
        public ReadOnlyClinicBook getClinicBook() {
            return clinicBook;
        }
    }
}
