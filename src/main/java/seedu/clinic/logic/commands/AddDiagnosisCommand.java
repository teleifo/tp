package seedu.clinic.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_DESC;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_DIAGNOSED_BY;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_DISPENSED_BY;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_DOSAGE;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_FREQ;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_MEDICATION;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_SYMPTOM;
import static seedu.clinic.logic.parser.CliSyntax.PREFIX_VISIT_DATE;

import java.util.Optional;

import seedu.clinic.commons.util.ToStringBuilder;
import seedu.clinic.logic.commands.exceptions.CommandException;
import seedu.clinic.model.Model;
import seedu.clinic.model.person.Diagnosis;
import seedu.clinic.model.person.Doctor;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Person;
import seedu.clinic.model.person.Pharmacist;
import seedu.clinic.model.person.Prescription;

/**
 * Adds a diagnosis to a patient.
 */
public class AddDiagnosisCommand extends Command {

    public static final String COMMAND_WORD = "diagnosis";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a diagnosis to a patient. "
            + "Parameters: "
            + PREFIX_ID + "ID "
            + PREFIX_DESC + "DESCRIPTION "
            + PREFIX_VISIT_DATE + "VISIT_DATE "
            + PREFIX_DIAGNOSED_BY + "DIAGNOSED_BY "
            + PREFIX_SYMPTOM + "SYMPTOM... "
            + "[" + PREFIX_MEDICATION + "MEDICATION_NAME "
            + PREFIX_DOSAGE + "DOSAGE "
            + PREFIX_FREQ + "FREQUENCY "
            + PREFIX_DISPENSED_BY + "DISPENSED_BY]... \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ID + "1 "
            + PREFIX_DESC + "Flu "
            + PREFIX_VISIT_DATE + "2026-03-01 "
            + PREFIX_DIAGNOSED_BY + "2 "
            + PREFIX_SYMPTOM + "fever " + PREFIX_SYMPTOM + "cough "
            + PREFIX_MEDICATION + "Paracetamol "
            + PREFIX_DOSAGE + "500mg "
            + PREFIX_FREQ + "3 times daily "
            + PREFIX_DISPENSED_BY + "4";

    public static final String MESSAGE_SUCCESS = "New diagnosis added: %1$s";
    public static final String MESSAGE_INVALID_PATIENT = "The patient ID provided is invalid";
    public static final String MESSAGE_INVALID_DOCTOR = "The doctor ID provided is invalid";
    public static final String MESSAGE_INVALID_PHARMACIST = "The pharmacist ID provided is invalid";

    private final int patientId;
    private final Diagnosis diagnosis;

    /**
     * Creates an AddDiagnosisCommand to add the specified {@code Diagnosis}
     * to the patient with the given stable person ID.
     */
    public AddDiagnosisCommand(int patientId, Diagnosis diagnosis) {
        requireNonNull(diagnosis);
        this.patientId = patientId;
        this.diagnosis = diagnosis;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Optional<Patient> patient = findPersonById(model, patientId, Patient.class);
        if (!patient.isPresent()) {
            throw new CommandException(MESSAGE_INVALID_PATIENT);
        }

        Optional<Doctor> doctor = findPersonById(model, diagnosis.getDiagnosedBy(), Doctor.class);
        if (!doctor.isPresent()) {
            throw new CommandException(MESSAGE_INVALID_DOCTOR);
        }

        for (Prescription pres : diagnosis.getPrescriptions()) {
            Optional<Pharmacist> pharmacist = findPersonById(model, pres.getDispensedBy(), Pharmacist.class);
            if (!pharmacist.isPresent()) {
                throw new CommandException(MESSAGE_INVALID_PHARMACIST);
            }
        }

        model.addDiagnosis(patient.get(), diagnosis);

        return new CommandResult(String.format(MESSAGE_SUCCESS, diagnosis));
    }

    private static <T extends Person> Optional<T> findPersonById(Model model, int id, Class<T> expectedType) {
        return model.getClinicBook().getPersonList().stream()
                .filter(expectedType::isInstance)
                .map(expectedType::cast)
                .filter(person -> person.getId() == id)
                .findFirst();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddDiagnosisCommand)) {
            return false;
        }

        AddDiagnosisCommand otherCommand = (AddDiagnosisCommand) other;
        return patientId == otherCommand.patientId
                && diagnosis.equals(otherCommand.diagnosis);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("patientId", patientId)
                .add("diagnosis", diagnosis)
                .toString();
    }
}
