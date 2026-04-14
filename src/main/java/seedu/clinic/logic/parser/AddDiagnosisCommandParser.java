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

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    AddDiagnosisCommand.MESSAGE_USAGE));
        }

        String patientIdRaw = getRequiredValue(argMultimap, PREFIX_ID, AddDiagnosisCommand.MESSAGE_MISSING_PATIENT_ID);
        String descriptionRaw = getRequiredValue(argMultimap, PREFIX_DESC,
                AddDiagnosisCommand.MESSAGE_MISSING_DESCRIPTION);
        String visitDateRaw = getRequiredValue(argMultimap, PREFIX_VISIT_DATE,
                AddDiagnosisCommand.MESSAGE_MISSING_VISIT_DATE);
        String diagnosedByRaw = getRequiredValue(argMultimap, PREFIX_DIAGNOSED_BY,
                AddDiagnosisCommand.MESSAGE_MISSING_DOCTOR);

        if (argMultimap.getAllValues(PREFIX_SYMPTOM).isEmpty()) {
            throw new ParseException(AddDiagnosisCommand.MESSAGE_MISSING_SYMPTOM);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_ID, PREFIX_DESC, PREFIX_VISIT_DATE, PREFIX_DIAGNOSED_BY);

        int patientId = parsePositivePersonId(patientIdRaw,
                AddDiagnosisCommand.MESSAGE_INVALID_PATIENT);
        String description = descriptionRaw.trim();
        if (description.isEmpty()) {
            throw new ParseException(AddDiagnosisCommand.MESSAGE_EMPTY_DESCRIPTION);
        }
        LocalDate visitDate = ParserUtil.parseDate(visitDateRaw);
        if (visitDate.isAfter(LocalDate.now())) {
            throw new ParseException(AddDiagnosisCommand.MESSAGE_FUTURE_VISIT_DATE);
        }
        int diagnosedById = parsePositivePersonId(diagnosedByRaw,
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
            throw new ParseException(AddDiagnosisCommand.MESSAGE_MISSING_MEDICATION);
        }

        if (dosages.size() > prescriptionCount && frequencies.size() == prescriptionCount) {
            int firstFreqPos = args.indexOf(" freq/");
            int firstDosePos = args.indexOf(" dose/");

            if (firstFreqPos != -1 && firstDosePos != -1 && firstDosePos < firstFreqPos) {
                java.util.regex.Pattern unitDosePattern = java.util.regex.Pattern.compile(" freq/\\S+ dose/");
                java.util.regex.Matcher matcher = unitDosePattern.matcher(args);

                java.util.List<Integer> freqsWithUnitDose = new java.util.ArrayList<>();
                while (matcher.find()) {
                    String before = args.substring(0, matcher.start());
                    int freqIdx = countMatches(before, " freq/");
                    freqsWithUnitDose.add(freqIdx);
                }

                java.util.List<Integer> dosePositions = new java.util.ArrayList<>();
                int pos = 0;
                while ((pos = args.indexOf(" dose/", pos)) >= 0) {
                    dosePositions.add(pos);
                    pos++;
                }

                java.util.List<Integer> freqPositions = new java.util.ArrayList<>();
                pos = 0;
                while ((pos = args.indexOf(" freq/", pos)) >= 0) {
                    freqPositions.add(pos);
                    pos++;
                }

                java.util.List<Integer> dosIndicesToRemove = new java.util.ArrayList<>();
                for (int freqIdx : freqsWithUnitDose) {
                    int dosCount = 0;
                    for (int dosePos : dosePositions) {
                        if (dosePos < freqPositions.get(freqIdx)) {
                            dosCount++;
                        }
                    }

                    if (dosCount < dosages.size()) {
                        frequencies.set(freqIdx, frequencies.get(freqIdx) + " dose/" + dosages.get(dosCount));
                        dosIndicesToRemove.add(dosCount);
                    }
                }

                java.util.Collections.sort(dosIndicesToRemove, java.util.Collections.reverseOrder());
                for (int idx : dosIndicesToRemove) {
                    dosages.remove(idx);
                }
            }
        }

        if (dosages.size() != prescriptionCount || frequencies.size() != prescriptionCount
                || dispensedByList.size() != prescriptionCount) {
            throw new ParseException(AddDiagnosisCommand.MESSAGE_MISSING_MEDICATION_DETAILS);
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

    private static String getRequiredValue(ArgumentMultimap argumentMultimap, Prefix prefix, String errorMsg)
            throws ParseException {
        return argumentMultimap.getValue(prefix).orElseThrow(() -> new ParseException(
                String.format(errorMsg + "\n" + AddDiagnosisCommand.MESSAGE_USAGE)));
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

    /**
     * Counts the number of non-overlapping occurrences of a given pattern in a string.
     */
    private static int countMatches(String text, String pattern) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(pattern, index)) >= 0) {
            count++;
            index += pattern.length();
        }
        return count;
    }
}
