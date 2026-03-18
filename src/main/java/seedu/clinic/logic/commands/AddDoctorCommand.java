package seedu.clinic.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.clinic.logic.commands.exceptions.CommandException;
import seedu.clinic.model.Model;
import seedu.clinic.model.person.Doctor;

/**
 * Adds a doctor to the clinic book.
 */
public class AddDoctorCommand extends Command {

    public static final String COMMAND_WORD = "add-doc";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a doctor to the clinic book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_TAG + "senior "
            + PREFIX_TAG + "surgeon";

    public static final String MESSAGE_SUCCESS = "New doctor added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This doctor already exists in the address book";

    private final Doctor toAdd;

    /**
     * Creates an AddDoctorCommand to add the specified {@code Doctor}
     */
    public AddDoctorCommand(Doctor doctor) {
        requireNonNull(doctor);
        toAdd = doctor;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddDoctorCommand // instanceof handles nulls
                && toAdd.equals(((AddDoctorCommand) other).toAdd));
    }
}
