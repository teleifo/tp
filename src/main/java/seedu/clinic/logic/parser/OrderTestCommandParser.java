package seedu.clinic.logic.parser;

import static seedu.clinic.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_ORDERED_BY;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_TEST_NAME;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_TEST_TYPE;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_VISIT_DATE;

import java.time.LocalDate;
import java.util.stream.Stream;

import seedu.clinic.logic.commands.OrderTestCommand;
import seedu.clinic.logic.parser.exceptions.ParseException;
import seedu.clinic.model.person.LabTest;
import seedu.clinic.model.person.LabTest.TestType;

/**
 * Parses input arguments and creates a new OrderTestCommand object.
 */
public class OrderTestCommandParser implements Parser<OrderTestCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the OrderTestCommand
     * and returns an OrderTestCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public OrderTestCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args,
                        PREFIX_ID, PREFIX_TEST_NAME, PREFIX_TEST_TYPE,
                        PREFIX_VISIT_DATE, PREFIX_ORDERED_BY);

        if (!arePrefixesPresent(argMultimap,
                PREFIX_ID, PREFIX_TEST_NAME, PREFIX_TEST_TYPE, PREFIX_VISIT_DATE, PREFIX_ORDERED_BY)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    OrderTestCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_ID, PREFIX_TEST_NAME, PREFIX_TEST_TYPE, PREFIX_VISIT_DATE, PREFIX_ORDERED_BY);

        int patientId = ParserUtil.parsePersonId(argMultimap.getValue(PREFIX_ID).get());
        String testName = argMultimap.getValue(PREFIX_TEST_NAME).get().trim();
        String testTypeStr = argMultimap.getValue(PREFIX_TEST_TYPE).get().trim().toUpperCase();
        LocalDate orderedDate = ParserUtil.parseDate(argMultimap.getValue(PREFIX_VISIT_DATE).get());
        int orderedBy = ParserUtil.parsePersonId(argMultimap.getValue(PREFIX_ORDERED_BY).get());

        if (!LabTest.isValidTestName(testName)) {
            throw new ParseException(LabTest.MESSAGE_CONSTRAINTS);
        }

        if (!TestType.isValidTestType(testTypeStr)) {
            throw new ParseException(LabTest.MESSAGE_TYPE_CONSTRAINTS);
        }
        TestType testType = TestType.valueOf(testTypeStr);

        LabTest labTest = new LabTest(testName, testType, orderedBy, orderedDate);

        return new OrderTestCommand(patientId, labTest);
    }

    /**
     * Returns true if all prefixes contain values.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes)
                .allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
