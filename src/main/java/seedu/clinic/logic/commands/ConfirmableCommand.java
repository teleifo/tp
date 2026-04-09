package seedu.clinic.logic.commands;

/**
 * Represents a command that may be re-executed after explicit confirmation.
 */
public interface ConfirmableCommand {

    /**
     * Marks the command as confirmed.
     */
    void confirm();
}
