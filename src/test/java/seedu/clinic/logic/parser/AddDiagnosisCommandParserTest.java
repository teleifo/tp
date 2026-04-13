package seedu.clinic.logic.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.clinic.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.clinic.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.clinic.logic.commands.AddDiagnosisCommand;
import seedu.clinic.logic.parser.exceptions.ParseException;

public class AddDiagnosisCommandParserTest {

    private final AddDiagnosisCommandParser parser = new AddDiagnosisCommandParser();

    private static String withUsage(String message) {
        return message + "\n" + AddDiagnosisCommand.MESSAGE_USAGE;
    }

    @Test
    public void parse_allFieldsPresent_success() throws Exception {
        String userInput = " id/1 desc/Flu vd/2026-03-01 diagnosed/2"
                + " sym/fever med/Paracetamol dose/500mg freq/3 times daily dispensed/4";

        AddDiagnosisCommand command = parser.parse(userInput);
        assertTrue(command.toString().contains("Flu"));
    }

    @Test
    public void parse_missingDescription_throwsSpecificParseException() {
        String userInput = " id/1 vd/2026-03-01 diagnosed/2 sym/fever"
                + " med/Paracetamol dose/500mg freq/3 times daily dispensed/4";
        assertParseFailure(parser, userInput, withUsage(AddDiagnosisCommand.MESSAGE_MISSING_DESCRIPTION));
    }

    @Test
    public void parse_missingPatientId_throwsSpecificParseException() {
        String userInput = " desc/Flu vd/2026-03-01 diagnosed/2"
                + " sym/fever med/Paracetamol dose/500mg freq/3 times daily dispensed/4";

        assertParseFailure(parser, userInput, withUsage(AddDiagnosisCommand.MESSAGE_MISSING_PATIENT_ID));
    }

    @Test
    public void parse_missingVisitDate_throwsSpecificParseException() {
        String userInput = " id/1 desc/Flu diagnosed/2"
                + " sym/fever med/Paracetamol dose/500mg freq/3 times daily dispensed/4";

        assertParseFailure(parser, userInput, withUsage(AddDiagnosisCommand.MESSAGE_MISSING_VISIT_DATE));
    }

    @Test
    public void parse_missingDiagnosedBy_throwsSpecificParseException() {
        String userInput = " id/1 desc/Flu vd/2026-03-01"
                + " sym/fever med/Paracetamol dose/500mg freq/3 times daily dispensed/4";

        assertParseFailure(parser, userInput, withUsage(AddDiagnosisCommand.MESSAGE_MISSING_DOCTOR));
    }

    @Test
    public void parse_noSymptoms_throwsParseException() {
        String userInput = " id/1 desc/Flu vd/2026-03-01 diagnosed/2"
                + " med/Paracetamol dose/500mg freq/3 times daily dispensed/4";
        assertParseFailure(parser, userInput, AddDiagnosisCommand.MESSAGE_MISSING_SYMPTOM);
    }

    @Test
    public void parse_noPrescriptions_throwsParseException() {
        String userInput = " id/1 desc/Flu vd/2026-03-01 diagnosed/2 sym/fever";
        assertParseFailure(parser, userInput, AddDiagnosisCommand.MESSAGE_MISSING_MEDICATION);
    }

    @Test
    public void parse_missingDosage_throwsSpecificParseException() {
        String userInput = " id/1 desc/Flu vd/2026-03-01 diagnosed/2"
                + " sym/fever med/Paracetamol freq/3 times daily dispensed/4";

        assertParseFailure(parser, userInput, AddDiagnosisCommand.MESSAGE_MISSING_MEDICATION_DETAILS);
    }

    @Test
    public void parse_missingFrequency_throwsSpecificParseException() {
        String userInput = " id/1 desc/Flu vd/2026-03-01 diagnosed/2"
                + " sym/fever med/Paracetamol dose/500mg dispensed/4";

        assertParseFailure(parser, userInput, AddDiagnosisCommand.MESSAGE_MISSING_MEDICATION_DETAILS);
    }

    @Test
    public void parse_missingDispensedBy_throwsSpecificParseException() {
        String userInput = " id/1 desc/Flu vd/2026-03-01 diagnosed/2"
                + " sym/fever med/Paracetamol dose/500mg freq/3 times daily";

        assertParseFailure(parser, userInput, AddDiagnosisCommand.MESSAGE_MISSING_MEDICATION_DETAILS);
    }

    @Test
    public void parse_duplicatePrefixes_throwsParseException() {
        String userInput = " id/1 id/2 desc/Flu vd/2026-03-01 diagnosed/2"
                + " sym/fever med/Paracetamol dose/500mg freq/3 times daily dispensed/4";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_emptyDescription_throwsFriendlyParseException() {
        String userInput = " id/1 desc/   vd/2026-03-01 diagnosed/2"
                + " sym/fever med/Paracetamol dose/500mg freq/3 times daily dispensed/4";

        assertParseFailure(parser, userInput, AddDiagnosisCommand.MESSAGE_EMPTY_DESCRIPTION);
    }

    @Test
    public void parse_emptySymptom_throwsFriendlyParseException() {
        String userInput = " id/1 desc/Flu vd/2026-03-01 diagnosed/2"
                + " sym/   med/Paracetamol dose/500mg freq/3 times daily dispensed/4";

        assertParseFailure(parser, userInput, AddDiagnosisCommand.MESSAGE_EMPTY_SYMPTOM);
    }

    @Test
    public void parse_zeroPatientId_throwsFriendlyParseException() {
        String userInput = " id/0 desc/Flu vd/2026-03-01 diagnosed/2"
                + " sym/fever med/Paracetamol dose/500mg freq/3 times daily dispensed/4";

        assertParseFailure(parser, userInput, AddDiagnosisCommand.MESSAGE_INVALID_PATIENT);
    }

    @Test
    public void parse_negativePatientId_throwsFriendlyParseException() {
        String userInput = " id/-1 desc/Flu vd/2026-03-01 diagnosed/2"
                + " sym/fever med/Paracetamol dose/500mg freq/3 times daily dispensed/4";

        assertParseFailure(parser, userInput, AddDiagnosisCommand.MESSAGE_INVALID_PATIENT);
    }

    @Test
    public void parse_nonPositiveDoctorId_throwsFriendlyParseException() {
        String userInput = " id/1 desc/Flu vd/2026-03-01 diagnosed/0"
                + " sym/fever med/Paracetamol dose/500mg freq/3 times daily dispensed/4";

        assertParseFailure(parser, userInput, AddDiagnosisCommand.MESSAGE_INVALID_DOCTOR);
    }

    @Test
    public void parse_nonPositivePharmacistId_throwsFriendlyParseException() {
        String userInput = " id/1 desc/Flu vd/2026-03-01 diagnosed/2"
                + " sym/fever med/Paracetamol dose/500mg freq/3 times daily dispensed/-4";

        assertParseFailure(parser, userInput, AddDiagnosisCommand.MESSAGE_INVALID_PHARMACIST);
    }

    @Test
    public void parse_frequencyContainingSlash_throwsFriendlyParseException() {
        String userInput = " id/1 desc/Flu vd/2026-03-01 diagnosed/2"
                + " sym/fever med/Paracetamol dose/500mg freq/1/day dispensed/4";

        assertParseFailure(parser, userInput, AddDiagnosisCommand.MESSAGE_INVALID_FREQUENCY_SLASH);
    }

    @Test
    public void parse_frequencyContainingDosePrefix_throwsFriendlyParseException() {
        String userInput = " id/1 desc/Flu vd/2026-03-01 diagnosed/2"
                + " sym/fever med/Paracetamol dose/500mg freq/1 dose/day dispensed/4";

        assertParseFailure(parser, userInput, AddDiagnosisCommand.MESSAGE_MISSING_MEDICATION_DETAILS);
    }
}
