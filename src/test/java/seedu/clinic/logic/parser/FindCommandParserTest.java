package seedu.clinic.logic.parser;

import static seedu.clinic.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.clinic.logic.Messages.getErrorMessageForDuplicatePrefixes;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_NRIC;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.clinic.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.clinic.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.clinic.logic.commands.FindCommand;
import seedu.clinic.model.person.NRIC;
import seedu.clinic.model.person.PersonMatchesFindCriteriaPredicate;
import seedu.clinic.model.person.Phone;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    /*
     * Invalid equivalence partitions for command shape:
     *   - empty string
     *   - whitespace-only input
     *   - missing search prefix
     *   - non-empty preamble before the prefixed criterion
     */

    @Test
    public void parse_emptyString_throwsParseException() {
        // Boundary value: empty string
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_whitespaceOnlyArguments_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingSearchPrefix_throwsParseException() {
        assertParseFailure(parser, "Alice Bob",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonEmptyPreamble_throwsParseException() {
        assertParseFailure(parser, " Alice n/Bob",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleSearchPrefixes_throwsParseException() {
        // EP: name prefix combined with phone prefix
        assertParseFailure(parser, " n/Alice p/98765432",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        // EP: name prefix combined with NRIC prefix
        assertParseFailure(parser, " n/Alice nric/S1234567D",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        // EP: phone prefix combined with NRIC prefix
        assertParseFailure(parser, " p/98765432 nric/S1234567D",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicateSearchPrefix_throwsParseException() {
        // EP: duplicate name prefix
        assertParseFailure(parser, " n/Alice n/Bob", getErrorMessageForDuplicatePrefixes(PREFIX_NAME));
        // EP: duplicate phone prefix
        assertParseFailure(parser, " p/12345678 p/98765432", getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));
        // EP: duplicate NRIC prefix
        assertParseFailure(parser, " nric/S1234567D nric/T1234567J",
                getErrorMessageForDuplicatePrefixes(PREFIX_NRIC));
    }

    /*
     * Invalid equivalence partitions for prefixed values:
     *   - blank name keyword input
     *   - malformed phone value
     *   - invalid NRIC value
     *
     * The tests below cover one invalid value partition at a time.
     */

    @Test
    public void parse_blankNameValue_throwsParseException() {
        assertParseFailure(parser, " n/   ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_malformedPhoneValue_throwsParseException() {
        assertParseFailure(parser, " p/abc", Phone.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_formatInvalidNricValue_throwsParseException() {
        assertParseFailure(parser, " nric/S1234A67D", NRIC.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_validNameCriterion_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonMatchesFindCriteriaPredicate(Arrays.asList("Alice", "Bob"),
                        Optional.empty(), Optional.empty()));
        // EP: standard name keywords
        assertParseSuccess(parser, " n/Alice Bob", expectedFindCommand);
        // Boundary case: name keywords with irregular whitespace
        assertParseSuccess(parser, " \n n/Alice \n \t Bob  \t", expectedFindCommand);
    }

    @Test
    public void parse_validPhoneCriterion_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesFindCriteriaPredicate(Collections.emptyList(),
                        Optional.of(new Phone("98765432")), Optional.empty()));
        assertParseSuccess(parser, " p/98765432", expectedFindCommand);
    }

    @Test
    public void parse_validNricCriterion_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(new PersonMatchesFindCriteriaPredicate(
                Collections.emptyList(), Optional.empty(), Optional.of(new NRIC("S1234567D"))));
        assertParseSuccess(parser, " nric/s1234567d", expectedFindCommand);
    }
}
