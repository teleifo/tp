package seedu.clinic.logic.commands;

import static seedu.clinic.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.clinic.commons.util.ToStringBuilder;
import seedu.clinic.model.person.Doctor;

/**
 * Adds a doctor to the clinic book.
 */
public class AddDoctorCommand extends AddPersonWithDuplicateWarningCommand<Doctor> {

    public static final String COMMAND_WORD = "add-doctor";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a doctor to the clinic book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            //+ "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com ";

    public static final String MESSAGE_SUCCESS = "New doctor added: %1$s";


    private final Doctor toAdd;

    /**
     * Creates an AddDoctorCommand to add the specified {@code Doctor}
     */
    public AddDoctorCommand(Doctor doctor) {
        super(doctor);
        toAdd = doctor;
    }

    @Override
    protected Class<Doctor> getPersonType() {
        return Doctor.class;
    }

    @Override
    protected String getPersonLabel() {
        return "doctor";
    }

    @Override
    protected String getSuccessMessage() {
        return MESSAGE_SUCCESS;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddDoctorCommand)) {
            return false;
        }

        AddDoctorCommand otherAddCommand = (AddDoctorCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
