package seedu.clinic.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.clinic.commons.core.LogsCenter;
import seedu.clinic.commons.exceptions.DataLoadingException;
import seedu.clinic.commons.exceptions.IllegalValueException;
import seedu.clinic.commons.util.FileUtil;
import seedu.clinic.commons.util.JsonUtil;
import seedu.clinic.model.ReadOnlyClinicBook;

/**
 * A class to access ClinicBook data stored as a json file on the hard disk.
 */
public class JsonClinicBookStorage implements ClinicBookStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonClinicBookStorage.class);

    private Path filePath;

    public JsonClinicBookStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getClinicBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyClinicBook> readClinicBook() throws DataLoadingException {
        return readClinicBook(filePath);
    }

    /**
     * Similar to {@link #readClinicBook()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    public Optional<ReadOnlyClinicBook> readClinicBook(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableClinicBook> jsonClinicBook = JsonUtil.readJsonFile(
                filePath, JsonSerializableClinicBook.class);
        if (!jsonClinicBook.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonClinicBook.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    @Override
    public void saveClinicBook(ReadOnlyClinicBook clinicBook) throws IOException {
        saveClinicBook(clinicBook, filePath);
    }

    /**
     * Similar to {@link #saveClinicBook(ReadOnlyClinicBook)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveClinicBook(ReadOnlyClinicBook clinicBook, Path filePath) throws IOException {
        requireNonNull(clinicBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableClinicBook(clinicBook), filePath);
    }

}
