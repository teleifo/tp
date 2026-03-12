package seedu.clinic.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.clinic.commons.exceptions.DataLoadingException;
import seedu.clinic.model.ReadOnlyClinicBook;

/**
 * Represents a storage for {@link seedu.clinic.model.ClinicBook}.
 */
public interface ClinicBookStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getClinicBookFilePath();

    /**
     * Returns ClinicBook data as a {@link ReadOnlyClinicBook}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if loading the data from storage failed.
     */
    Optional<ReadOnlyClinicBook> readClinicBook() throws DataLoadingException;

    /**
     * @see #getClinicBookFilePath()
     */
    Optional<ReadOnlyClinicBook> readClinicBook(Path filePath) throws DataLoadingException;

    /**
     * Saves the given {@link ReadOnlyClinicBook} to the storage.
     * @param clinicBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveClinicBook(ReadOnlyClinicBook clinicBook) throws IOException;

    /**
     * @see #saveClinicBook(ReadOnlyClinicBook)
     */
    void saveClinicBook(ReadOnlyClinicBook clinicBook, Path filePath) throws IOException;

}
