package seedu.clinic.model.person.exceptions;

/**
 * Signals that the operation will result in duplicate Patients (Patients are considered duplicates if they have the same nric). 
 * */
public class DuplicatePatientException extends RuntimeException {
    public DuplicatePatientException() {
        super("Operation would result in duplicate patients");
    }
}