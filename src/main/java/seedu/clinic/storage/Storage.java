package seedu.clinic.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.clinic.commons.exceptions.DataLoadingException;
import seedu.clinic.model.ReadOnlyClinicBook;
import seedu.clinic.model.ReadOnlyUserPrefs;
import seedu.clinic.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends ClinicBookStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataLoadingException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getClinicBookFilePath();

    @Override
    Optional<ReadOnlyClinicBook> readClinicBook() throws DataLoadingException;

    @Override
    void saveClinicBook(ReadOnlyClinicBook clinicBook) throws IOException;

}
