package seedu.clinic.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.clinic.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.clinic.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.clinic.testutil.TypicalPersons.ALICE;
import static seedu.clinic.testutil.TypicalPersons.BENSON;
import static seedu.clinic.testutil.TypicalPersons.CARL;
import static seedu.clinic.testutil.TypicalPersons.ELLE;
import static seedu.clinic.testutil.TypicalPersons.FIONA;
import static seedu.clinic.testutil.TypicalPersons.getTypicalClinicBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.clinic.model.Model;
import seedu.clinic.model.ModelManager;
import seedu.clinic.model.UserPrefs;
import seedu.clinic.model.person.PersonMatchesFindCriteriaPredicate;
import seedu.clinic.model.person.Phone;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalClinicBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalClinicBook(), new UserPrefs());

    @Test
    public void equals() {
        PersonMatchesFindCriteriaPredicate firstPredicate =
                new PersonMatchesFindCriteriaPredicate(Collections.singletonList("first"), Optional.empty());
        PersonMatchesFindCriteriaPredicate secondPredicate =
                new PersonMatchesFindCriteriaPredicate(Collections.singletonList("second"), Optional.empty());

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_noMatches_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        PersonMatchesFindCriteriaPredicate predicate =
                new PersonMatchesFindCriteriaPredicate(Collections.singletonList("nomatch"), Optional.empty());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_nameKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        PersonMatchesFindCriteriaPredicate predicate =
                new PersonMatchesFindCriteriaPredicate(Arrays.asList("Kurz", "Elle", "Kunz"), Optional.empty());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void execute_phoneNumber_onePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PersonMatchesFindCriteriaPredicate predicate =
                new PersonMatchesFindCriteriaPredicate(Collections.emptyList(), Optional.of(new Phone("98765432")));
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(BENSON), model.getFilteredPersonList());
    }

    @Test
    public void execute_nameOrPhone_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        PersonMatchesFindCriteriaPredicate predicate = new PersonMatchesFindCriteriaPredicate(
                Collections.singletonList("Alice"), Optional.of(new Phone("9482427")));
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        PersonMatchesFindCriteriaPredicate predicate = new PersonMatchesFindCriteriaPredicate(
                Arrays.asList("keyword"), Optional.of(new Phone("94351253")));
        FindCommand findCommand = new FindCommand(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }
}
