---
layout: page
title: User Guide
---

ClinicBook is a **desktop app for managing contacts, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, ClinicBook can get your contact management tasks done faster than traditional GUI apps.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2526S2-CS2103-T11-3/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your ClinicBook.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar clinicbook.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `find n/NAME_KEYWORDS`, `NAME_KEYWORDS` is a parameter which can be used as `find n/John`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Listing all persons : `list`

Shows a list of all persons in the clinic book.

Format: `list`

* Each card shows both the displayed row number and the person's stable `ID`.
* Use the row number for index-based commands such as `delete`.
* Use the stable `ID` for commands that reference a specific person record, such as `diagnosis`.

### Locating persons by name, phone, or NRIC: `find`

Finds persons who match the supplied name keywords, phone number, and/or patient NRIC.

Format: `find [n/NAME_KEYWORDS] [p/PHONE] [nric/NRIC]`

* At least one of `n/`, `p/`, or `nric/` must be provided.
* Prefixes are required. `find Alice` is invalid; use `find n/Alice`.
* Name search is case-insensitive. e.g `n/hans` will match `Hans`
* The order of the name keywords does not matter. e.g. `n/Hans Bo` will match `Bo Hans`
* Only full words in the name will be matched e.g. `n/Han` will not match `Hans`
* Phone search requires an exact match. e.g. `p/9876` will not match `98765432`
* NRIC search requires an exact valid NRIC and only matches patient entries.
* If multiple prefixes are provided, a person must match all supplied fields.

Examples:
* `find n/John` returns `john` and `John Doe`
* `find p/98765432` returns persons with phone number `98765432`
* `find nric/S1234567D` returns the patient with NRIC `S1234567D`
* `find n/Nadia p/93456789 nric/S1234567D` returns the patient only if all three fields match<br>

  <!-- ![result for 'find alex david'](images/findAlexDavidResult.png) -->

### Deleting a person : `delete`

Deletes the specified person from the clinic book.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the clinic book.
* `find n/Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

### Adding a diagnosis : `diagnosis`

Adds a diagnosis to a patient and validates the referenced doctor and pharmacist by stable person `ID`.

Format:
`diagnosis id/PATIENT_ID desc/DESCRIPTION vd/VISIT_DATE diagnosed/DOCTOR_ID sym/SYMPTOM... med/MEDICATION dose/DOSAGE freq/FREQUENCY dispensed/PHARMACIST_ID`

* `id/`, `diagnosed/`, and `dispensed/` use the stable person `ID` shown on each person card, not the displayed row number.
* `delete` remains index-based. `diagnosis` is ID-based.
* `id/` must refer to a patient, `diagnosed/` must refer to a doctor, and `dispensed/` must refer to a pharmacist.
* `vd/` must be in `yyyy-MM-dd` format.
* At least one `sym/` and one medication block are required.

Example:
`diagnosis id/1 desc/Flu vd/2026-03-01 diagnosed/2 sym/fever sym/cough med/Paracetamol dose/500mg freq/3 times daily dispensed/4`

### Clearing all entries : `clear`

Clears all entries from the clinic book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

ClinicBook data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

ClinicBook data are saved automatically as a JSON file `[JAR file location]/data/clinicbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, ClinicBook will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the ClinicBook to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous ClinicBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Clear** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Diagnosis** | `diagnosis id/PATIENT_ID desc/DESCRIPTION vd/VISIT_DATE diagnosed/DOCTOR_ID sym/SYMPTOM... med/MEDICATION dose/DOSAGE freq/FREQUENCY dispensed/PHARMACIST_ID`<br> e.g., `diagnosis id/1 desc/Flu vd/2026-03-01 diagnosed/2 sym/fever med/Paracetamol dose/500mg freq/3 times daily dispensed/4`
**Find** | `find [n/NAME_KEYWORDS] [p/PHONE] [nric/NRIC]`<br> e.g., `find n/James Jake p/98765432 nric/S1234567D`
**List** | `list`
**Help** | `help`
