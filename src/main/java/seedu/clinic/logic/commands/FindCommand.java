package seedu.clinic.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.clinic.commons.util.ToStringBuilder;
import seedu.clinic.logic.Messages;
import seedu.clinic.model.Model;
import seedu.clinic.model.person.PersonMatchesFindCriteriaPredicate;

/**
 * Finds and lists all persons in clinic book who match the search using exactly one of:
 * name keywords, phone number, or patient NRIC.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons who match the supplied "
            + "name keywords, phone number, or patient NRIC, and displays them as a list with index numbers.\n"
            + "Parameters: n/NAME_KEYWORDS | p/PHONE | nric/NRIC\n"
            + "Examples: " + COMMAND_WORD + " n/alice bob, " + COMMAND_WORD + " p/98765432, "
            + COMMAND_WORD + " nric/S1234567D";

    private final PersonMatchesFindCriteriaPredicate predicate;

    public FindCommand(PersonMatchesFindCriteriaPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
