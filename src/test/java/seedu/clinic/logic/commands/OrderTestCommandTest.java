package seedu.clinic.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.clinic.logic.commands.exceptions.CommandException;
import seedu.clinic.model.ClinicBook;
import seedu.clinic.model.Model;
import seedu.clinic.model.ModelManager;
import seedu.clinic.model.UserPrefs;
import seedu.clinic.model.person.Address;
import seedu.clinic.model.person.Doctor;
import seedu.clinic.model.person.Email;
import seedu.clinic.model.person.LabTest;
import seedu.clinic.model.person.LabTest.TestType;
import seedu.clinic.model.person.NRIC;
import seedu.clinic.model.person.Name;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Phone;
import seedu.clinic.model.person.Sex;

public class OrderTestCommandTest {

    private static final int PATIENT_ID = 1;
    private static final int DOCTOR_ID = 2;

    @Test
    public void execute_validLabTest_success() throws Exception {
        Model model = createModelWithPatientAndDoctor();
        LabTest labTest = new LabTest("Complete Blood Count", TestType.LAB, DOCTOR_ID,
                LocalDate.of(2026, 4, 8));
        OrderTestCommand command = new OrderTestCommand(PATIENT_ID, labTest);

        CommandResult result = command.execute(model);

        assertEquals(String.format(OrderTestCommand.MESSAGE_SUCCESS, labTest), result.getFeedbackToUser());
        Patient patient = model.getFilteredPersonList().stream()
                .filter(Patient.class::isInstance)
                .map(Patient.class::cast)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected a patient in the filtered list"));
        assertEquals(1, patient.getLabTests().size());
        assertEquals(labTest, patient.getLabTests().get(0));
    }

    @Test
    public void execute_validImagingTest_success() throws Exception {
        Model model = createModelWithPatientAndDoctor();
        LabTest labTest = new LabTest("Chest X-Ray", TestType.IMAGING, DOCTOR_ID,
                LocalDate.of(2026, 4, 8));
        OrderTestCommand command = new OrderTestCommand(PATIENT_ID, labTest);

        CommandResult result = command.execute(model);

        assertEquals(String.format(OrderTestCommand.MESSAGE_SUCCESS, labTest), result.getFeedbackToUser());
    }

    @Test
    public void execute_invalidPatient_throwsCommandException() {
        Model model = createModelWithPatientAndDoctor();
        LabTest labTest = new LabTest("Complete Blood Count", TestType.LAB, DOCTOR_ID,
                LocalDate.of(2026, 4, 8));
        OrderTestCommand command = new OrderTestCommand(99, labTest);

        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(OrderTestCommand.MESSAGE_INVALID_PATIENT, exception.getMessage());
    }

    @Test
    public void execute_invalidDoctor_throwsCommandException() {
        Model model = createModelWithPatientOnly();
        LabTest labTest = new LabTest("Complete Blood Count", TestType.LAB, DOCTOR_ID,
                LocalDate.of(2026, 4, 8));
        OrderTestCommand command = new OrderTestCommand(PATIENT_ID, labTest);

        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(OrderTestCommand.MESSAGE_INVALID_DOCTOR, exception.getMessage());
    }

    @Test
    public void equals() {
        LabTest labTest = new LabTest("Complete Blood Count", TestType.LAB, DOCTOR_ID,
                LocalDate.of(2026, 4, 8));
        OrderTestCommand command = new OrderTestCommand(PATIENT_ID, labTest);

        assertTrue(command.equals(command));
        assertTrue(command.equals(new OrderTestCommand(PATIENT_ID, labTest)));
    }

    private static Model createModelWithPatientAndDoctor() {
        ClinicBook clinicBook = new ClinicBook();
        clinicBook.addPerson(new Patient(
                new Name("Patient One"),
                new Phone("91234567"),
                new Email("patient@example.com"),
                new Address("1 Street"),
                Set.of(),
                new NRIC("S1166846A"),
                LocalDate.of(2000, 1, 1),
                Sex.FEMALE,
                PATIENT_ID));
        clinicBook.addPerson(new Doctor(
                new Name("Doctor One"),
                new Phone("92345678"),
                new Email("doctor@example.com"),
                DOCTOR_ID));
        return new ModelManager(clinicBook, new UserPrefs());
    }

    private static Model createModelWithPatientOnly() {
        ClinicBook clinicBook = new ClinicBook();
        clinicBook.addPerson(new Patient(
                new Name("Patient One"),
                new Phone("91234567"),
                new Email("patient@example.com"),
                new Address("1 Street"),
                Set.of(),
                new NRIC("S1166846A"),
                LocalDate.of(2000, 1, 1),
                Sex.FEMALE,
                PATIENT_ID));
        return new ModelManager(clinicBook, new UserPrefs());
    }
}
