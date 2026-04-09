package seedu.clinic.logic.commands;

import static seedu.clinic.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.clinic.commons.util.ToStringBuilder;
import seedu.clinic.model.person.Pharmacist;

/**
 * Adds a pharmacist to clinic book.
 */
public class AddPharmacistCommand extends AddPersonWithDuplicateWarningCommand<Pharmacist> {

    public static final String COMMAND_WORD = "add-pharmacist";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a pharmacist to clinicbook. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_PHONE + "PHONE "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_EMAIL + "john@gmail.com "
            + PREFIX_PHONE + "90010000 ";

    public static final String MESSAGE_SUCCESS = "New pharmacist added: %1$s";


    private final Pharmacist newPharmacist;

    /**
     * Creates an AddPharmacistCommand to add the specified {@code Pharmacist}
     */
    public AddPharmacistCommand(Pharmacist pharmacist) {
        super(pharmacist);
        newPharmacist = pharmacist;
    }

    @Override
    protected Class<Pharmacist> getPersonType() {
        return Pharmacist.class;
    }

    @Override
    protected String getPersonLabel() {
        return "pharmacist";
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
        if (!(other instanceof AddPharmacistCommand)) {
            return false;
        }

        AddPharmacistCommand otherAddCommand = (AddPharmacistCommand) other;
        return newPharmacist.equals(otherAddCommand.newPharmacist);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("Pharmacist", newPharmacist)
                .toString();
    }
}
