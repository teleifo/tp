package seedu.clinic.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.clinic.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.clinic.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.clinic.testutil.Assert.assertThrows;
import static seedu.clinic.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.clinic.logic.commands.AddDiagnosisCommand;
import seedu.clinic.logic.commands.ClearCommand;
import seedu.clinic.logic.commands.DeleteCommand;
import seedu.clinic.logic.commands.ExitCommand;
import seedu.clinic.logic.commands.FindCommand;
import seedu.clinic.logic.commands.GetHistoryCommand;
import seedu.clinic.logic.commands.HelpCommand;
import seedu.clinic.logic.commands.ListCommand;
import seedu.clinic.logic.commands.OrderTestCommand;
import seedu.clinic.logic.parser.exceptions.ParseException;
import seedu.clinic.model.person.NRIC;
import seedu.clinic.model.person.PersonMatchesFindCriteriaPredicate;
import seedu.clinic.model.person.Phone;

public class ClinicBookParserTest {

    private final ClinicBookParser parser = new ClinicBookParser();

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " n/" + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new PersonMatchesFindCriteriaPredicate(
                keywords, Optional.empty(), Optional.empty())), command);
    }

    @Test
    public void parseCommand_findByPhone() throws Exception {
        FindCommand command = (FindCommand) parser.parseCommand(FindCommand.COMMAND_WORD + " p/98765432");
        assertEquals(new FindCommand(new PersonMatchesFindCriteriaPredicate(List.of(),
                Optional.of(new Phone("98765432")), Optional.empty())), command);
    }

    @Test
    public void parseCommand_findByNric() throws Exception {
        FindCommand command = (FindCommand) parser.parseCommand(FindCommand.COMMAND_WORD + " nric/S1234567D");
        assertEquals(new FindCommand(new PersonMatchesFindCriteriaPredicate(List.of(),
                Optional.empty(), Optional.of(new NRIC("S1234567D")))), command);
    }

    @Test
    public void parseCommand_getHistory() throws Exception {
        GetHistoryCommand command = (GetHistoryCommand) parser.parseCommand(
                GetHistoryCommand.COMMAND_WORD + " nric/t1234567z");
        assertEquals(new GetHistoryCommand("T1234567Z"), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_addDiagnosis() throws Exception {
        String args = " id/1 desc/Flu vd/2026-03-01 diagnosed/2"
                + " sym/fever med/Paracetamol dose/500mg freq/daily dispensed/4";
        assertTrue(parser.parseCommand(AddDiagnosisCommand.COMMAND_WORD + args)
                instanceof AddDiagnosisCommand);
    }

    @Test
    public void parseCommand_orderTest() throws Exception {
        String args = " id/1 test/Chest X-Ray testtype/IMAGING vd/2026-04-08 ordered/2";
        assertTrue(parser.parseCommand(OrderTestCommand.COMMAND_WORD + args)
            instanceof OrderTestCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }

    @Test
    public void parseCommand_legacyAddCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand(
                "add n/John Doe p/98765432 e/john@example.com a/Clementi"));
    }

    @Test
    public void parseCommand_legacyEditCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand(
                "edit 1 n/John Doe"));
    }
}
