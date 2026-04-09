package seedu.clinic.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.clinic.logic.commands.exceptions.CommandException;
import seedu.clinic.model.Model;
import seedu.clinic.model.person.Person;

/**
 * Base add command with reusable duplicate contact-field warning logic.
 */
public abstract class AddPersonWithDuplicateWarningCommand<T extends Person>
        extends Command implements ConfirmableCommand {
    public static final String MESSAGE_DUPLICATE_WARNING = "Warning: existing %s(s) with the same %s were found. "
            + "Press Enter again to continue adding anyway OR key-in 'list' to get the original list.";
    public static final String MESSAGE_DUPLICATE_REJECT = "Rejected: an existing %s already has the same name, "
            + "phone number, and email address. Matching %s is shown below.";

    private final T personToAdd;
    private boolean isConfirmed;

    protected AddPersonWithDuplicateWarningCommand(T personToAdd) {
        requireNonNull(personToAdd);
        this.personToAdd = personToAdd;
    }

    protected abstract Class<T> getPersonType();

    protected abstract String getPersonLabel();

    protected abstract String getSuccessMessage();

    protected String getDuplicateWarningMessage() {
        return MESSAGE_DUPLICATE_WARNING;
    }

    protected String getDuplicateRejectMessage() {
        return MESSAGE_DUPLICATE_REJECT;
    }

    /**
     * Hook for subclasses to decide whether exact contact-field duplicates should be rejected.
     */
    protected boolean shouldRejectExactDuplicate() {
        return true;
    }

    /**
     * Hook for subclasses to reject duplicates outside contact-field checks.
     */
    protected void validateAdditionalConstraints(Model model) throws CommandException {
        requireNonNull(model);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        validateAdditionalConstraints(model);

        DuplicatePersonFieldsMatch<T> duplicateMatch =
                DuplicatePersonFieldsMatch.find(model, personToAdd, getPersonType());

        if (shouldRejectExactDuplicate() && duplicateMatch.hasExactDuplicate()) {
            model.updateFilteredPersonList(duplicateMatch.asExactDuplicatePredicate());
            throw new CommandException(String.format(getDuplicateRejectMessage(), getPersonLabel(), getPersonLabel()));
        }

        if (duplicateMatch.hasAnyMatch() && !isConfirmed) {
            model.updateFilteredPersonList(duplicateMatch.asPredicate());
            return new CommandResult(String.format(getDuplicateWarningMessage(),
                            getPersonLabel(), duplicateMatch.getMatchingFieldSummary()),
                    false, false, true);
        }

        model.addPerson(personToAdd);
        return new CommandResult(String.format(getSuccessMessage(), personToAdd));
    }

    @Override
    public void confirm() {
        isConfirmed = true;
    }
}
