package seedu.clinic.model;

import java.nio.file.Path;

import seedu.clinic.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getClinicBookFilePath();

}
