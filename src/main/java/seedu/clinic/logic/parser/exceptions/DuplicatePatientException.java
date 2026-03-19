package seedu.clinic.model.person.exceptions;

/**
 * Signals that the AddPatientCommand cannot be executed because there's a patient of the same name.
 */
public class DuplicatePatientException extends RuntimeException {}