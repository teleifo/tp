package seedu.clinic.logic.commands;

import static seedu.clinic.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.clinic.testutil.TypicalPersons.getTypicalClinicBook;

import org.junit.jupiter.api.Test;

import seedu.clinic.model.ClinicBook;
import seedu.clinic.model.Model;
import seedu.clinic.model.ModelManager;
import seedu.clinic.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyClinicBook_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyClinicBook_success() {
        Model model = new ModelManager(getTypicalClinicBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalClinicBook(), new UserPrefs());
        expectedModel.setClinicBook(new ClinicBook());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
