package seedu.clinic.logic.parser;

import static seedu.clinic.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_NRIC;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Stream;

import seedu.clinic.commons.core.LogsCenter;
import seedu.clinic.logic.commands.FindCommand;
import seedu.clinic.logic.parser.exceptions.ParseException;
import seedu.clinic.model.person.NRIC;
import seedu.clinic.model.person.PersonMatchesFindCriteriaPredicate;
import seedu.clinic.model.person.Phone;

/**
 * Parses input arguments and creates a new {@code FindCommand} object.
 */
public class FindCommandParser implements Parser<FindCommand> {

    private static final Logger logger = LogsCenter.getLogger(FindCommandParser.class);

    /**
     * Parses the given {@code String} of arguments in the context of the {@code FindCommand}
     * and returns a FindCommand object for execution.
     *
     * @throws ParseException If the user input does not conform to the expected format.
     */
    public FindCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_NRIC);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_NRIC);
        validateCommandShape(argMultimap);

        List<String> nameKeywords = parseNameKeywords(argMultimap);
        Optional<Phone> phone = parsePhone(argMultimap);
        Optional<NRIC> nric = parseNric(argMultimap);

        return new FindCommand(new PersonMatchesFindCriteriaPredicate(nameKeywords, phone, nric));
    }

    /**
     * Validates that the find command uses exactly one prefixed criterion and no preamble text.
     */
    private void validateCommandShape(ArgumentMultimap argMultimap) throws ParseException {
        long presentPrefixCount = Stream.of(PREFIX_NAME, PREFIX_PHONE, PREFIX_NRIC)
                .filter(prefix -> argMultimap.getValue(prefix).isPresent())
                .count();
        boolean hasPreamble = !argMultimap.getPreamble().isEmpty();
        if (presentPrefixCount != 1 || hasPreamble) {
            logger.info(String.format("Rejected find command: presentPrefixes=%d, hasPreamble=%s",
                    presentPrefixCount, hasPreamble));
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
    }

    private List<String> parseNameKeywords(ArgumentMultimap argMultimap) throws ParseException {
        Optional<String> nameInput = argMultimap.getValue(PREFIX_NAME);
        if (nameInput.isEmpty()) {
            return List.of();
        }

        if (nameInput.get().isBlank()) {
            logger.info("Rejected find command: empty name value");
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return Arrays.asList(nameInput.get().split("\\s+"));
    }

    private Optional<Phone> parsePhone(ArgumentMultimap argMultimap) throws ParseException {
        Optional<String> phoneInput = argMultimap.getValue(PREFIX_PHONE);
        if (phoneInput.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(ParserUtil.parsePhone(phoneInput.get()));
    }

    private Optional<NRIC> parseNric(ArgumentMultimap argMultimap) throws ParseException {
        Optional<String> nricInput = argMultimap.getValue(PREFIX_NRIC);
        if (nricInput.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(ParserUtil.parseNric(nricInput.get()));
    }

}
