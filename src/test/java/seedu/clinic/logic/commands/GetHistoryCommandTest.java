package seedu.clinic.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.clinic.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.clinic.model.ClinicBook;
import seedu.clinic.model.Model;
import seedu.clinic.model.ModelManager;
import seedu.clinic.model.UserPrefs;
import seedu.clinic.model.person.Address;
import seedu.clinic.model.person.Diagnosis;
import seedu.clinic.model.person.Email;
import seedu.clinic.model.person.NRIC;
import seedu.clinic.model.person.Name;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Person;
import seedu.clinic.model.person.Phone;
import seedu.clinic.model.person.Prescription;
import seedu.clinic.model.person.Sex;

/**
 * Contains integration tests (interaction with the Model) for {@code GetHistoryCommand}.
 */
public class GetHistoryCommandTest {

    @Test
    public void equals() {
        GetHistoryCommand firstCommand = new GetHistoryCommand("S1234567D");
        GetHistoryCommand secondCommand = new GetHistoryCommand("T1234567J");

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(new GetHistoryCommand("S1234567D")));
        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(secondCommand));
    }

    @Test
    public void execute_matchingPatient_foundSinglePatient() {
        Model model = createModelWithSampleRecords();
        Model expectedModel = new ModelManager(model.getClinicBook(), new UserPrefs());

        GetHistoryCommand command = new GetHistoryCommand("S1234567D");
        command.execute(expectedModel);

        String expectedMessage = "Medical history for Alice Tan (NRIC: S1234567D)\n"
                + "Date of birth: 1990-01-01\n"
                + "Diagnoses:\n"
            + "  1. Hypertension (Visit date: 2024-05-20, Diagnosed by ID: 3)\n"
                + "     Symptoms: headache, dizziness\n"
                + "     Prescriptions:\n"
            + "       - Amlodipine, dosage: 5mg, frequency: once daily, prescribed by ID: N/A, "
            + "dispensed by ID: 4\n"
            + "Lab/Imaging Tests: none ordered.";
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleDiagnoses_showsMostRecentFirst() {
        Model model = createModelWithDiagnosesOutOfOrder();
        Model expectedModel = new ModelManager(model.getClinicBook(), new UserPrefs());

        GetHistoryCommand command = new GetHistoryCommand("S1234567D");
        command.execute(expectedModel);

        String lineSep = "\n";
        String expectedMessage = "Medical history for Alice Tan (NRIC: S1234567D)" + lineSep
                + "Date of birth: 1990-01-01" + lineSep
                + "Diagnoses:" + lineSep
                + "  1. Flu (Visit date: 2027-03-01, Diagnosed by ID: 2)" + lineSep
                + "     Symptoms: fever, cough" + lineSep
                + "     Prescriptions:" + lineSep
                + "       - Paracetamol, dosage: 500mg, frequency: 3 times daily, prescribed by ID: N/A, "
                + "dispensed by ID: 4" + lineSep
                + "  2. Flu (Visit date: 2026-03-01, Diagnosed by ID: 2)" + lineSep
                + "     Symptoms: fever, cough" + lineSep
                + "     Prescriptions:" + lineSep
                + "       - Paracetamol, dosage: 500mg, frequency: 3 times daily, prescribed by ID: N/A, "
                + "dispensed by ID: 4" + lineSep
                + "  3. Flu (Visit date: 2025-03-01, Diagnosed by ID: 2)" + lineSep
                + "     Symptoms: fever, cough" + lineSep
                + "     Prescriptions:" + lineSep
                + "       - Paracetamol, dosage: 500mg, frequency: 3 times daily, prescribed by ID: N/A, "
                + "dispensed by ID: 4" + lineSep
                + "Lab/Imaging Tests: none ordered.";
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_nonMatchingPatient_foundNoPatient() {
        Model model = createModelWithSampleRecords();
        Model expectedModel = new ModelManager(model.getClinicBook(), new UserPrefs());

        GetHistoryCommand command = new GetHistoryCommand("T0000000A");
        command.execute(expectedModel);

        String expectedMessage = String.format(GetHistoryCommand.MESSAGE_NO_PATIENT_FOUND, "T0000000A");
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    private static Model createModelWithSampleRecords() {
        ClinicBook clinicBook = new ClinicBook();
        Patient alice = new Patient(
                new Name("Alice Tan"),
                new Phone("91234567"),
                new Email("alice@example.com"),
                new Address("123 Clementi Rd"),
                Set.of(),
                new NRIC("S1234567D"),
                LocalDate.of(1990, 1, 1),
                Sex.FEMALE,
                1);

        Diagnosis diagnosis = new Diagnosis("Hypertension", LocalDate.of(2024, 5, 20), 3);
        diagnosis.addSymptom("headache");
        diagnosis.addSymptom("dizziness");
        diagnosis.addPrescription(new Prescription("Amlodipine", "5mg", "once daily", 4));
        alice.addDiagnosis(diagnosis);

        clinicBook.addPerson(alice);
        clinicBook.addPerson(new Patient(
                new Name("Bob Lee"),
                new Phone("92345678"),
                new Email("bob@example.com"),
                new Address("456 Jurong West St"),
                Set.of(),
                new NRIC("T1234567J"),
                LocalDate.of(1988, 6, 15),
                Sex.MALE,
                2));
        clinicBook.addPerson(new Person(
                new Name("Carl Helper"),
                new Phone("93456789"),
                new Email("carl@example.com"),
                new Address("789 Yishun Ave"),
                9));

        return new ModelManager(clinicBook, new UserPrefs());
    }

    private static Model createModelWithDiagnosesOutOfOrder() {
        ClinicBook clinicBook = new ClinicBook();
        Patient alice = new Patient(
                new Name("Alice Tan"),
                new Phone("91234567"),
                new Email("alice@example.com"),
                new Address("123 Clementi Rd"),
                Set.of(),
                new NRIC("S1234567D"),
                LocalDate.of(1990, 1, 1),
                Sex.FEMALE,
                1);

        alice.addDiagnosis(createDiagnosis(LocalDate.of(2026, 3, 1)));
        alice.addDiagnosis(createDiagnosis(LocalDate.of(2027, 3, 1)));
        alice.addDiagnosis(createDiagnosis(LocalDate.of(2025, 3, 1)));

        clinicBook.addPerson(alice);
        return new ModelManager(clinicBook, new UserPrefs());
    }

    private static Diagnosis createDiagnosis(LocalDate visitDate) {
        Diagnosis diagnosis = new Diagnosis("Flu", visitDate, 2);
        diagnosis.addSymptom("fever");
        diagnosis.addSymptom("cough");
        diagnosis.addPrescription(new Prescription("Paracetamol", "500mg", "3 times daily", 4));
        return diagnosis;
    }
}
