package seedu.clinic.logic.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.clinic.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.clinic.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.clinic.logic.commands.OrderTestCommand;
import seedu.clinic.logic.parser.exceptions.ParseException;
import seedu.clinic.model.person.LabTest.TestType;

public class OrderTestCommandParserTest {

    private final OrderTestCommandParser parser = new OrderTestCommandParser();

    @Test
    public void parse_allFieldsPresentLabTest_success() throws Exception {
        String userInput = " id/1 test/Complete Blood Count testtype/LAB vd/2026-04-08 ordered/2";

        OrderTestCommand command = parser.parse(userInput);
        assertTrue(command.toString().contains("Complete Blood Count"));
        assertTrue(command.toString().contains(TestType.LAB.name()));
    }

    @Test
    public void parse_allFieldsPresentImagingTest_success() throws Exception {
        String userInput = " id/1 test/Chest X-Ray testtype/IMAGING vd/2026-04-08 ordered/2";

        OrderTestCommand command = parser.parse(userInput);
        assertTrue(command.toString().contains("Chest X-Ray"));
        assertTrue(command.toString().contains(TestType.IMAGING.name()));
    }

    @Test
    public void parse_testTypeCaseInsensitive_success() throws Exception {
        String userInput = " id/1 test/Blood Test testtype/lab vd/2026-04-08 ordered/2";

        OrderTestCommand command = parser.parse(userInput);
        assertTrue(command.toString().contains(TestType.LAB.name()));
    }

    @Test
    public void parse_missingId_throwsParseException() {
        String userInput = " test/Complete Blood Count testtype/LAB vd/2026-04-08 ordered/2";
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, OrderTestCommand.MESSAGE_USAGE), () ->
                        parser.parse(userInput));
    }

    @Test
    public void parse_missingTestName_throwsParseException() {
        String userInput = " id/1 testtype/LAB vd/2026-04-08 ordered/2";
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, OrderTestCommand.MESSAGE_USAGE), () ->
                        parser.parse(userInput));
    }

    @Test
    public void parse_missingTestType_throwsParseException() {
        String userInput = " id/1 test/Complete Blood Count vd/2026-04-08 ordered/2";
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, OrderTestCommand.MESSAGE_USAGE), () ->
                        parser.parse(userInput));
    }

    @Test
    public void parse_missingDate_throwsParseException() {
        String userInput = " id/1 test/Complete Blood Count testtype/LAB ordered/2";
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, OrderTestCommand.MESSAGE_USAGE), () ->
                        parser.parse(userInput));
    }

    @Test
    public void parse_missingOrderedBy_throwsParseException() {
        String userInput = " id/1 test/Complete Blood Count testtype/LAB vd/2026-04-08";
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, OrderTestCommand.MESSAGE_USAGE), () ->
                        parser.parse(userInput));
    }

    @Test
    public void parse_invalidTestType_throwsParseException() {
        String userInput = " id/1 test/Complete Blood Count testtype/UNKNOWN vd/2026-04-08 ordered/2";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_duplicatePrefixes_throwsParseException() {
        String userInput = " id/1 id/2 test/Complete Blood Count testtype/LAB vd/2026-04-08 ordered/2";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}
