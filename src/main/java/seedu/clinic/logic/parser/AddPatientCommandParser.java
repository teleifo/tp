package seedu.clinic.logic.parser;

import static seedu.clinic.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_ALLERGIES;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_DOB;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_NRIC;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_SEX;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

import seedu.clinic.logic.commands.AddPatientCommand;
import seedu.clinic.logic.parser.exceptions.ParseException;
import seedu.clinic.model.person.Address;
import seedu.clinic.model.person.Email;
import seedu.clinic.model.person.NRIC;
import seedu.clinic.model.person.Name;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Person;
import seedu.clinic.model.person.Phone;
import seedu.clinic.model.person.Sex;
import seedu.clinic.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddPatientCommand object.
 */
public class AddPatientCommandParser implements Parser<AddPatientCommand> {

    private static final DateTimeFormatter DOB_FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
    private static final String MESSAGE_INVALID_DOB =
            "DOB must be a valid date in yyyy-MM-dd format and cannot be later than today.";
    private static final String MESSAGE_INVALID_SEX = "Sex must be one of: MALE, FEMALE, INTERSEX.";
    private static final String MESSAGE_NRIC_DOB_MISMATCH =
            "NRIC/FIN's first two digits must match the last two digits of the birth year.";
    private static final String MESSAGE_NRIC_CENTURY_MISMATCH =
            "S-prefix NRIC must correspond to a birth year before 2000; "
            + "T-prefix NRIC must correspond to a birth year from 2000 onwards.";

    /**
     * Parses the given {@code String} of arguments and returns an AddPatientCommand object.
     */
    public AddPatientCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args,
                        PREFIX_NAME, PREFIX_NRIC, PREFIX_DOB,
                        PREFIX_SEX, PREFIX_ALLERGIES,
                        PREFIX_EMAIL, PREFIX_PHONE, PREFIX_ADDRESS);

        if (!arePrefixesPresent(argMultimap,
                PREFIX_NAME, PREFIX_NRIC, PREFIX_DOB,
                PREFIX_SEX,
                PREFIX_EMAIL, PREFIX_PHONE, PREFIX_ADDRESS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(
                        MESSAGE_INVALID_COMMAND_FORMAT,
                        AddPatientCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_NRIC, PREFIX_EMAIL, PREFIX_PHONE);

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        NRIC nric = parseNric(argMultimap.getValue(PREFIX_NRIC).get());
        LocalDate dob = parseDob(argMultimap.getValue(PREFIX_DOB).get());
        validateNricDobConsistency(nric, dob);
        Sex sex = parseSex(argMultimap.getValue(PREFIX_SEX).get());

        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());

        List<String> allergyValues = argMultimap.getAllValues(PREFIX_ALLERGIES);
        Set<Tag> allergies = ParserUtil.parseTags(allergyValues);

        Person person = new Person(name, phone, email, address);
        Patient patient = new Patient(person, allergies, nric, dob, sex);
        return new AddPatientCommand(patient);
    }

    private static void validateNricDobConsistency(NRIC nric, LocalDate dob) throws ParseException {
        char prefix = nric.value.charAt(0);
        int birthYear = dob.getYear();
        if (prefix == 'S' && birthYear >= 2000) {
            throw new ParseException(MESSAGE_NRIC_CENTURY_MISMATCH);
        }
        if (prefix == 'T' && birthYear < 2000) {
            throw new ParseException(MESSAGE_NRIC_CENTURY_MISMATCH);
        }
        String nricYearDigits = nric.value.substring(1, 3);
        String dobYearDigits = String.format("%02d", birthYear % 100);
        if (!nricYearDigits.equals(dobYearDigits)) {
            throw new ParseException(MESSAGE_NRIC_DOB_MISMATCH);
        }
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
            LocalDate dob = LocalDate.parse(dobInput.trim(), DOB_FORMATTER);
            if (dob.isAfter(LocalDate.now())) {
                throw new ParseException(MESSAGE_INVALID_DOB);
            }
            return dob;
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

    /**
     * Returns true if all prefixes contain values.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes)
                .allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
