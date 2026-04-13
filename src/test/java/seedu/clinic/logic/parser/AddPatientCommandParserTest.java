package seedu.clinic.logic.parser;

import static seedu.clinic.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.clinic.logic.Messages.getErrorMessageForDuplicatePrefixes;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_NRIC;
import static seedu.clinic.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.clinic.testutil.Assert.assertThrows;

import java.lang.reflect.Field;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.clinic.logic.commands.AddPatientCommand;
import seedu.clinic.logic.commands.Command;
import seedu.clinic.logic.parser.exceptions.ParseException;
import seedu.clinic.model.person.NRIC;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Sex;
import seedu.clinic.model.tag.Tag;

public class AddPatientCommandParserTest {

    private static final String VALID_ARGS_WITH_ALLERGIES = " n/Alice Patient"
            + " nric/s1234567d"
            + " dob/1992-04-12"
            + " sex/female"
            + " allergy/peanut"
            + " allergy/shellfish"
            + " e/alice@example.com"
            + " p/94351253"
            + " a/123, Jurong West Ave 6, #08-111";

    private static final String VALID_ARGS_NO_ALLERGIES = " n/Alice Patient"
            + " nric/S1234567D"
            + " dob/1992-04-12"
            + " sex/MALE"
            + " e/alice@example.com"
            + " p/94351253"
            + " a/123, Jurong West Ave 6, #08-111";

    private static final String VALID_ARGS_WITH_FORMATTED_PHONE = " n/John Doe"
            + " nric/S7510505C"
            + " dob/1990-01-01"
            + " sex/MALE"
            + " allergy/Penicillin"
            + " allergy/Shellfish"
            + " e/johnd@example.com"
            + " p/1234 5678 (HP) 1111-3333 (Office)"
            + " a/123 Clementi Ave 3, #04-12";

    private static final String VALID_ARGS_WITH_FOREIGN_FIN = " n/John Doe"
            + " nric/F0515994Q"
            + " dob/1912-01-01"
            + " sex/Male"
            + " allergy/G6PD"
            + " allergy/Shellfish"
            + " e/john@gmail.com"
            + " p/90010000"
            + " a/123 Marina Terrace";

    private final AddPatientCommandParser parser = new AddPatientCommandParser();

    @Test
    public void parse_allFieldsPresent_success() throws Exception {
        Command parsedCommand = parser.parse(VALID_ARGS_WITH_ALLERGIES);
        Patient patient = extractPatient((AddPatientCommand) parsedCommand);

        org.junit.jupiter.api.Assertions.assertEquals("Alice Patient", patient.getName().fullName);
        org.junit.jupiter.api.Assertions.assertEquals("S1234567D", patient.getNric().value);
        org.junit.jupiter.api.Assertions.assertEquals(LocalDate.of(1992, 4, 12), patient.getDateOfBirth());
        org.junit.jupiter.api.Assertions.assertEquals(Sex.FEMALE, patient.getSex());
        org.junit.jupiter.api.Assertions.assertEquals(2, patient.getAllergies().size());
        org.junit.jupiter.api.Assertions.assertTrue(patient.getAllergies().contains(new Tag("peanut")));
        org.junit.jupiter.api.Assertions.assertTrue(patient.getAllergies().contains(new Tag("shellfish")));
    }

    @Test
    public void parse_optionalAllergiesMissing_success() throws Exception {
        Command parsedCommand = parser.parse(VALID_ARGS_NO_ALLERGIES);
        Patient patient = extractPatient((AddPatientCommand) parsedCommand);

        org.junit.jupiter.api.Assertions.assertEquals(Sex.MALE, patient.getSex());
        org.junit.jupiter.api.Assertions.assertTrue(patient.getAllergies().isEmpty());
    }

    @Test
    public void parse_formattedPhone_success() throws Exception {
        Command parsedCommand = parser.parse(VALID_ARGS_WITH_FORMATTED_PHONE);
        Patient patient = extractPatient((AddPatientCommand) parsedCommand);

        org.junit.jupiter.api.Assertions.assertEquals("1234 5678 (HP) 1111-3333 (Office)",
                patient.getPhone().value);
    }

    @Test
    public void parse_foreignFin_success() throws Exception {
        Command parsedCommand = parser.parse(VALID_ARGS_WITH_FOREIGN_FIN);
        Patient patient = extractPatient((AddPatientCommand) parsedCommand);

        org.junit.jupiter.api.Assertions.assertEquals("F0515994Q", patient.getNric().value);
    }

    @Test
    public void parse_missingRequiredPrefix_failure() {
        String missingSex = " n/Alice Patient"
                + " nric/S1234567D"
                + " dob/1992-04-12"
                + " e/alice@example.com"
                + " p/94351253"
                + " a/123, Jurong West Ave 6, #08-111";

        assertParseFailure(parser, missingSex,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPatientCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonEmptyPreamble_failure() {
        String nonEmptyPreamble = "junk" + VALID_ARGS_NO_ALLERGIES;

        assertParseFailure(parser, nonEmptyPreamble,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPatientCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicateNricPrefix_failure() {
        String duplicateNric = " n/Alice Patient"
                + " nric/S1234567D"
                + " nric/S7654321D"
                + " dob/1992-04-12"
                + " sex/FEMALE"
                + " e/alice@example.com"
                + " p/94351253"
                + " a/123, Jurong West Ave 6, #08-111";

        assertParseFailure(parser, duplicateNric, getErrorMessageForDuplicatePrefixes(PREFIX_NRIC));
    }

    @Test
    public void parse_invalidNric_failure() {
        String invalidNric = " n/Alice Patient"
                + " nric/INVALID"
                + " dob/1992-04-12"
                + " sex/FEMALE"
                + " e/alice@example.com"
                + " p/94351253"
                + " a/123, Jurong West Ave 6, #08-111";

        assertParseFailure(parser, invalidNric, NRIC.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidDob_failure() {
        String invalidDob = " n/Alice Patient"
                + " nric/S1234567D"
                + " dob/12-04-1992"
                + " sex/FEMALE"
                + " e/alice@example.com"
                + " p/94351253"
                + " a/123, Jurong West Ave 6, #08-111";

        assertParseFailure(parser, invalidDob,
                "DOB must be a valid date in yyyy-MM-dd format and cannot be in the future.");
    }

    @Test
    public void parse_invalidChronologicalDob_failure() {
        String invalidChronologicalDob = " n/Alice Patient"
                + " nric/S1234567D"
                + " dob/2023-02-30"
                + " sex/FEMALE"
                + " e/alice@example.com"
                + " p/94351253"
                + " a/123, Jurong West Ave 6, #08-111";

        assertParseFailure(parser, invalidChronologicalDob,
                "DOB must be a valid date in yyyy-MM-dd format and cannot be in the future.");
    }

    @Test
    public void parse_futureDob_failure() {
        String futureDob = " n/Alice Patient"
                + " nric/S1234567D"
                + " dob/2099-01-01"
                + " sex/FEMALE"
                + " e/alice@example.com"
                + " p/94351253"
                + " a/123, Jurong West Ave 6, #08-111";

        assertParseFailure(parser, futureDob,
                "DOB must be a valid date in yyyy-MM-dd format and cannot be in the future.");
    }

    @Test
    public void parse_invalidSex_failure() {
        String invalidSex = " n/Alice Patient"
                + " nric/S1234567D"
                + " dob/1992-04-12"
                + " sex/unknown"
                + " e/alice@example.com"
                + " p/94351253"
                + " a/123, Jurong West Ave 6, #08-111";

        assertParseFailure(parser, invalidSex, "Sex must be one of: MALE, FEMALE, INTERSEX.");
    }

    @Test
    public void parse_invalidAllergyTag_failure() {
        String invalidAllergy = " n/Alice Patient"
                + " nric/S1234567D"
                + " dob/1992-04-12"
                + " sex/FEMALE"
                + " allergy/bad*tag"
                + " e/alice@example.com"
                + " p/94351253"
                + " a/123, Jurong West Ave 6, #08-111";

        assertParseFailure(parser, invalidAllergy, Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_mistypedAllergyPrefix_failure() {
        String mistypedAllergyPrefix = " n/John Doe"
                + " nric/S7630902G"
                + " dob/1990-01-01"
                + " sex/MALE"
                + " allergy/Penicillin all/Shellfish"
                + " e/johnd@example.com"
                + " p/91234567"
                + " a/123 Clementi Ave 3, #04-12";

        assertParseFailure(parser, mistypedAllergyPrefix, Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_blankSex_failure() {
        String blankSex = " n/Alice Patient"
                + " nric/S1234567D"
                + " dob/1992-04-12"
                + " sex/   "
                + " e/alice@example.com"
                + " p/94351253"
                + " a/123, Jurong West Ave 6, #08-111";

        assertThrows(ParseException.class,
            "Sex must be one of: MALE, FEMALE, INTERSEX.", () -> parser.parse(blankSex));
    }

    private Patient extractPatient(AddPatientCommand command) throws Exception {
        Field field = AddPatientCommand.class.getDeclaredField("newPatient");
        field.setAccessible(true);
        return (Patient) field.get(command);
    }
}
