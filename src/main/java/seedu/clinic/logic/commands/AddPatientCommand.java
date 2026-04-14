package seedu.clinic.logic.commands;

import static seedu.clinic.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_ALLERGIES;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_DOB;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_NRIC;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_SEX;

import seedu.clinic.commons.util.ToStringBuilder;
import seedu.clinic.logic.commands.exceptions.CommandException;
import seedu.clinic.model.Model;
import seedu.clinic.model.person.Patient;

/**
 * Adds a patient to clinic book.
 */
public class AddPatientCommand extends AddPersonWithDuplicateWarningCommand<Patient> {

    public static final String COMMAND_WORD = "add-patient";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a patient to clinicbook. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_NRIC + "NRIC "
            + PREFIX_DOB + "DOB "
            + PREFIX_SEX + "SEX "
            + "[" + PREFIX_ALLERGIES + "ALLERGY]... "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_ADDRESS + "ADDRESS "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_NRIC + "S1234567D "
            + PREFIX_DOB + "01-01-1912 "
            + PREFIX_SEX + "Male "
            + PREFIX_ALLERGIES + "G6PD " + PREFIX_ALLERGIES + "Shellfish "
            + PREFIX_EMAIL + "john@gmail.com "
            + PREFIX_PHONE + "90010000 "
            + PREFIX_ADDRESS + "123 Marina Terrace ";

    public static final String MESSAGE_SUCCESS = "New patient added: %1$s";
    public static final String MESSAGE_DUPLICATE_PATIENT = "This patient's NRIC/FIN already exists in clinic book";

    private final Patient newPatient;

    /**
     * Creates an AddPatientCommand to add the specified {@code Patient}
     */
    public AddPatientCommand(Patient patient) {
        super(patient);
        newPatient = patient;
    }

    @Override
    protected void validateAdditionalConstraints(Model model) throws CommandException {
        boolean hasDuplicateNric = model.getClinicBook().getPersonList().stream()
                .filter(Patient.class::isInstance)
                .map(Patient.class::cast)
                .anyMatch(existingPatient -> existingPatient.getNric().equals(newPatient.getNric()));

        if (hasDuplicateNric) {
            throw new CommandException(MESSAGE_DUPLICATE_PATIENT);
        }
    }

    @Override
    protected Class<Patient> getPersonType() {
        return Patient.class;
    }

    @Override
    protected String getPersonLabel() {
        return "patient";
    }

    @Override
    protected String getSuccessMessage() {
        return MESSAGE_SUCCESS;
    }

    @Override
    protected boolean shouldRejectExactDuplicate() {
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddPatientCommand)) {
            return false;
        }

        AddPatientCommand otherAddCommand = (AddPatientCommand) other;
        return newPatient.equals(otherAddCommand.newPatient);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("Patient", newPatient)
                .toString();
    }
}
