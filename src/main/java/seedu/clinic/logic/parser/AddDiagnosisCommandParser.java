package seedu.clinic.logic.parser;

import static seedu.clinic.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_DESC;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_DIAGNOSED_BY;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_DISPENSED_BY;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_DOSAGE;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_FREQ;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_MEDICATION;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_SYMPTOM;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_VISIT_DATE;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import seedu.clinic.logic.commands.AddDiagnosisCommand;
import seedu.clinic.logic.parser.exceptions.ParseException;
import seedu.clinic.model.person.Diagnosis;
import seedu.clinic.model.person.Prescription;

/**
 * Parses input arguments and creates a new AddDiagnosisCommand object
 */
public class AddDiagnosisCommandParser implements Parser<AddDiagnosisCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddDiagnosisCommand
     * and returns an AddDiagnosisCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddDiagnosisCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args,
                        PREFIX_ID, PREFIX_DESC, PREFIX_VISIT_DATE,
                        PREFIX_DIAGNOSED_BY, PREFIX_SYMPTOM, PREFIX_MEDICATION,
                        PREFIX_DOSAGE, PREFIX_FREQ, PREFIX_DISPENSED_BY);

        if (!arePrefixesPresent(argMultimap,
                PREFIX_ID, PREFIX_DESC, PREFIX_VISIT_DATE, PREFIX_DIAGNOSED_BY)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    AddDiagnosisCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getAllValues(PREFIX_SYMPTOM).isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    AddDiagnosisCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_ID, PREFIX_DESC, PREFIX_VISIT_DATE, PREFIX_DIAGNOSED_BY);

        int patientId = parsePositivePersonId(argMultimap.getValue(PREFIX_ID).get(),
                AddDiagnosisCommand.MESSAGE_INVALID_PATIENT);
        String description = argMultimap.getValue(PREFIX_DESC).get().trim();
        if (description.isEmpty()) {
            throw new ParseException(AddDiagnosisCommand.MESSAGE_EMPTY_DESCRIPTION);
        }
        LocalDate visitDate = ParserUtil.parseDate(argMultimap.getValue(PREFIX_VISIT_DATE).get());
        if (visitDate.isAfter(LocalDate.now())) {
            throw new ParseException(AddDiagnosisCommand.MESSAGE_FUTURE_VISIT_DATE);
        }
        int diagnosedById = parsePositivePersonId(argMultimap.getValue(PREFIX_DIAGNOSED_BY).get(),
                AddDiagnosisCommand.MESSAGE_INVALID_DOCTOR);
        List<String> symptoms = argMultimap.getAllValues(PREFIX_SYMPTOM);
        if (symptoms.stream().map(String::trim).anyMatch(String::isEmpty)) {
            throw new ParseException(AddDiagnosisCommand.MESSAGE_EMPTY_SYMPTOM);
        }

        List<String> medNames = argMultimap.getAllValues(PREFIX_MEDICATION);
        List<String> dosages = argMultimap.getAllValues(PREFIX_DOSAGE);
        List<String> frequencies = argMultimap.getAllValues(PREFIX_FREQ);
        List<Integer> dispensedByList = new ArrayList<>();
        for (String value : argMultimap.getAllValues(PREFIX_DISPENSED_BY)) {
            dispensedByList.add(parsePositivePersonId(value, AddDiagnosisCommand.MESSAGE_INVALID_PHARMACIST));
        }

        int prescriptionCount = medNames.size();

        if (prescriptionCount == 0) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    AddDiagnosisCommand.MESSAGE_USAGE));
        } else if (prescriptionCount > 0) {
            if (IntStream.of(dosages.size(), frequencies.size(), dispensedByList.size())
                    .anyMatch(size -> size != prescriptionCount)) {
                throw new ParseException(String.format(
                        MESSAGE_INVALID_COMMAND_FORMAT,
                        AddDiagnosisCommand.MESSAGE_USAGE));
            }
        }

        List<Prescription> prescriptions = new ArrayList<>();
        for (int i = 0; i < prescriptionCount; i++) {
            prescriptions.add(ParserUtil.parsePrescription(
                    medNames.get(i),
                    dosages.get(i),
                    frequencies.get(i),
                    dispensedByList.get(i)
            ));
        }

        Diagnosis diagnosis = new Diagnosis(description, visitDate, diagnosedById);
        symptoms.forEach(diagnosis::addSymptom);
        prescriptions.forEach(diagnosis::addPrescription);

        return new AddDiagnosisCommand(patientId, diagnosis);
    }

    /**
     * Returns true if all prefixes contain values.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes)
                .allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Parses a diagnosis-related person ID into an integer and returns the parsed person ID.
     * @throws ParseException If the ID is not an integer greater than 0.
     */
    private static int parsePositivePersonId(String rawId, String errorMessage) throws ParseException {
        String trimmedId = rawId.trim();
        try {
            if (Integer.parseInt(trimmedId) <= 0) {
                throw new ParseException(errorMessage);
            }
        } catch (NumberFormatException e) {
            throw new ParseException(errorMessage);
        }

        return ParserUtil.parsePersonId(trimmedId);
    }
}
