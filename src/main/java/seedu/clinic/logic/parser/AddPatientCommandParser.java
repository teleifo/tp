package seedu.clinic.logic.parser;

import static seedu.clinic.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_NRIC;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_DOB;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_SEX;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_ALLERGIES;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_EMERGENCY_CONTACT;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_ADDRESS;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

import seedu.clinic.logic.commands.AddPatientCommand;
import seedu.clinic.logic.parser.exceptions.ParseException;
import seedu.clinic.model.person.Address;
import seedu.clinic.model.person.Email;
import seedu.clinic.model.person.Name;
import seedu.clinic.model.person.NRIC;
import seedu.clinic.model.person.Sex;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Person;
import seedu.clinic.model.person.Phone;
import seedu.clinic.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddPatientCommand object.
 */
public class AddPatientCommandParser implements Parser<AddPatientCommand> {

    private static final DateTimeFormatter DOB_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String MESSAGE_INVALID_DOB = "DOB must be in dd-MM-yyyy format.";
    private static final String MESSAGE_INVALID_SEX = "Sex must be one of: MALE, FEMALE, INTERSEX.";
    private static final String MESSAGE_EMPTY_EMERGENCY_CONTACT = "Emergency contact cannot be blank.";

    public AddPatientCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(args,
                PREFIX_NAME, PREFIX_NRIC, PREFIX_DOB,
                PREFIX_SEX, PREFIX_ALLERGIES, PREFIX_EMERGENCY_CONTACT,
                PREFIX_EMAIL, PREFIX_PHONE, PREFIX_ADDRESS);

        if (!arePrefixesPresent(argMultimap,
                PREFIX_NAME, PREFIX_NRIC, PREFIX_DOB,
                PREFIX_SEX, PREFIX_ALLERGIES, PREFIX_EMERGENCY_CONTACT,
                PREFIX_EMAIL, PREFIX_PHONE, PREFIX_ADDRESS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(
                        MESSAGE_INVALID_COMMAND_FORMAT,
                        AddPatientCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_NAME, PREFIX_NRIC, PREFIX_EMAIL, PREFIX_PHONE);

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        NRIC nric = parseNric(argMultimap.getValue(PREFIX_NRIC).get());
        LocalDate dob = parseDob(argMultimap.getValue(PREFIX_DOB).get());
        Sex sex = parseSex(argMultimap.getValue(PREFIX_SEX).get());

        String emergencyContact = parseEmergencyContact(argMultimap.getValue(PREFIX_EMERGENCY_CONTACT).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());

        List<String> allergyValues = argMultimap.getAllValues(PREFIX_ALLERGIES);
        Set<Tag> allergies = ParserUtil.parseTags(allergyValues);

        Person person = new Person(name, phone, email, address, allergies);
        Patient patient = new Patient(person, nric, dob, sex, emergencyContact);
        return new AddPatientCommand(patient);
    }

    private static NRIC parseNric(String nricInput) throws ParseException {
        String normalized = nricInput.trim().toUpperCase(Locale.ROOT);
        if (!NRIC.isValidNric(normalized)) {
                throw new ParseException(NRIC.MESSAGE_CONSTRAINTS);
        }
        return new NRIC(normalized);
    }

    private static LocalDate parseDob(String dobInput) throws ParseException {
        try {
            return LocalDate.parse(dobInput.trim(), DOB_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ParseException(MESSAGE_INVALID_DOB, e);
        }
    }

    private static Sex parseSex(String sexInput) throws ParseException {
        String normalized = sexInput.trim().toUpperCase(Locale.ROOT);
        try {
            return Sex.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            throw new ParseException(MESSAGE_INVALID_SEX, e);
        }
    }

    // TODO: Verify this works
    private static String parseEmergencyContact(String emergencyContactInput) throws ParseException {
        String trimmed = emergencyContactInput.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(MESSAGE_EMPTY_EMERGENCY_CONTACT);
        }
        return trimmed;
    }

    /**
     * Returns true if all prefixes contain values.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes)
                .allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}