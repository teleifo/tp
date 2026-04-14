package seedu.clinic.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_ORDERED_BY;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_TEST_NAME;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_TEST_TYPE;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_VISIT_DATE;

import java.util.Optional;

import seedu.clinic.commons.util.ToStringBuilder;
import seedu.clinic.logic.commands.exceptions.CommandException;
import seedu.clinic.model.Model;
import seedu.clinic.model.person.Doctor;
import seedu.clinic.model.person.LabTest;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Person;

/**
 * Orders a lab or imaging test for a patient.
 */
public class OrderTestCommand extends Command {

    public static final String COMMAND_WORD = "order-test";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Orders a lab or imaging test for a patient. "
            + "Parameters: "
            + PREFIX_ID + "PATIENT_ID "
            + PREFIX_TEST_NAME + "TEST_NAME "
            + PREFIX_TEST_TYPE + "TEST_TYPE "
            + PREFIX_VISIT_DATE + "ORDERED_DATE "
            + PREFIX_ORDERED_BY + "DOCTOR_ID\n"
            + "TEST_TYPE must be 'LAB' or 'IMAGING'.\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ID + "1 "
            + PREFIX_TEST_NAME + "Complete Blood Count "
            + PREFIX_TEST_TYPE + "LAB "
            + PREFIX_VISIT_DATE + "2026-04-08 "
            + PREFIX_ORDERED_BY + "2";

    public static final String MESSAGE_SUCCESS = "New lab/imaging test ordered: %1$s";
    public static final String MESSAGE_INVALID_PATIENT = "The patient ID provided is invalid";
    public static final String MESSAGE_INVALID_DOCTOR = "The doctor ID provided is invalid";

    private final int patientId;
    private final LabTest labTest;

    /**
     * Creates an OrderTestCommand to order the specified {@code LabTest}
     * for the patient with the given person ID.
     */
    public OrderTestCommand(int patientId, LabTest labTest) {
        requireNonNull(labTest);
        this.patientId = patientId;
        this.labTest = labTest;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Optional<Patient> patient = findPersonById(model, patientId, Patient.class);
        if (!patient.isPresent()) {
            throw new CommandException(MESSAGE_INVALID_PATIENT);
        }

        Optional<Doctor> doctor = findPersonById(model, labTest.getOrderedBy(), Doctor.class);
        if (!doctor.isPresent()) {
            throw new CommandException(MESSAGE_INVALID_DOCTOR);
        }

        model.addLabTest(patient.get(), labTest);

        return new CommandResult(String.format(MESSAGE_SUCCESS, labTest));
    }

    private static <T extends Person> Optional<T> findPersonById(Model model, int id, Class<T> expectedType) {
        return model.getClinicBook().getPersonList().stream()
                .filter(expectedType::isInstance)
                .map(expectedType::cast)
                .filter(person -> person.getId() == id)
                .findFirst();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof OrderTestCommand)) {
            return false;
        }

        OrderTestCommand otherCommand = (OrderTestCommand) other;
        return patientId == otherCommand.patientId
                && labTest.equals(otherCommand.labTest);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("patientId", patientId)
                .add("labTest", labTest)
                .toString();
    }
}
