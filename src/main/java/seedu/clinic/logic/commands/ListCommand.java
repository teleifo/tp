package seedu.clinic.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.clinic.model.Model.PREDICATE_SHOW_ALL_DOCTORS;
import static seedu.clinic.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.clinic.model.Model.PREDICATE_SHOW_ALL_PHARMACISTS;

import seedu.clinic.model.Model;

/**
 * Lists all persons in the clinic book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all persons";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.updateFilteredDoctorList(PREDICATE_SHOW_ALL_DOCTORS);
        model.updateFilteredPharmacistList(PREDICATE_SHOW_ALL_PHARMACISTS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
