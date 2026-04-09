package seedu.clinic.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import seedu.clinic.commons.core.LogsCenter;
import seedu.clinic.commons.util.ToStringBuilder;
import seedu.clinic.model.Model;
import seedu.clinic.model.person.Diagnosis;
import seedu.clinic.model.person.LabTest;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.PatientHasNricPredicate;
import seedu.clinic.model.person.Prescription;

/**
 * Finds a patient by NRIC to support medical history retrieval workflows.
 */
public class GetHistoryCommand extends Command {

    public static final String COMMAND_WORD = "get-history";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds patient record(s) by NRIC for medical history retrieval.\n"
            + "Parameters: nric/NRIC\n"
            + "Example: " + COMMAND_WORD + " nric/S1234567D";

    public static final String MESSAGE_NO_PATIENT_FOUND = "No patient found with NRIC %s.";

    private static final Logger logger = LogsCenter.getLogger(GetHistoryCommand.class);

    private final String nric;
    private final PatientHasNricPredicate predicate;

    /**
     * Creates a command that finds patient records by NRIC.
     */
    public GetHistoryCommand(String nric) {
        requireNonNull(nric);
        this.nric = nric;
        this.predicate = new PatientHasNricPredicate(nric);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);

        List<Patient> matchedPatients = model.getFilteredPersonList().stream()
                .filter(Patient.class::isInstance)
                .map(Patient.class::cast)
                .collect(Collectors.toList());

        // key: the system property name to look up. def: the fallback default value if that property is not set
        String actor = System.getProperty("SYSTEM", "unknown-user");
        logger.info(String.format("AUDIT access-history user=%s nric=%s matches=%d",
            actor,
            nric,
            matchedPatients.size()));

        if (matchedPatients.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_NO_PATIENT_FOUND, nric));
        }

        return new CommandResult(formatMedicalHistory(matchedPatients));
    }

    private String formatMedicalHistory(List<Patient> matchedPatients) {
        return matchedPatients.stream()
                .map(this::formatPatientHistory)
                .collect(Collectors.joining("\n\n"));
    }

    private String formatPatientHistory(Patient patient) {
        StringBuilder result = new StringBuilder();
        result.append(formatPatientHeader(patient));
        result.append(System.lineSeparator());
        result.append(formatDiagnosesList(patient.getDiagnoses()));
        result.append(System.lineSeparator());
        result.append(formatLabTestsList(patient.getLabTests()));
        return result.toString();
    }

    private String formatPatientHeader(Patient patient) {
        StringBuilder header = new StringBuilder();
        header.append(String.format("Medical history for %s (NRIC: %s)", patient.getName(), patient.getNric().value));
        header.append(System.lineSeparator());
        header.append(String.format("Date of birth: %s", patient.getDateOfBirth()));
        return header.toString();
    }

    private String formatDiagnosesList(List<Diagnosis> diagnoses) {
        if (diagnoses.isEmpty()) {
            return "Diagnoses: none recorded.";
        }

        StringBuilder diagnosesSection = new StringBuilder("Diagnoses:");
        for (int index = 0; index < diagnoses.size(); index++) {
            diagnosesSection.append(System.lineSeparator());
            diagnosesSection.append(formatSingleDiagnosis(index + 1, diagnoses.get(index)));
        }
        return diagnosesSection.toString();
    }

    private String formatSingleDiagnosis(int index, Diagnosis diagnosis) {
        StringBuilder diag = new StringBuilder();
        diag.append(String.format("  %d. %s (Visit date: %s, Diagnosed by ID: %d)",
                index, diagnosis.getDescription(), diagnosis.getVisitDate(), diagnosis.getDiagnosedBy()));
        diag.append(System.lineSeparator());
        diag.append(formatSymptoms(diagnosis.getSymptoms()));
        diag.append(System.lineSeparator());
        diag.append(formatPrescriptionsSection(diagnosis.getPrescriptions()));
        return diag.toString();
    }

    private String formatSymptoms(List<String> symptoms) {
        if (symptoms.isEmpty()) {
            return "     Symptoms: none recorded.";
        }
        return "     Symptoms: " + String.join(", ", symptoms);
    }

    private String formatPrescriptionsSection(List<Prescription> prescriptions) {
        if (prescriptions.isEmpty()) {
            return "     Prescriptions: none recorded.";
        }

        StringBuilder section = new StringBuilder("     Prescriptions:");
        for (Prescription p : prescriptions) {
            section.append(System.lineSeparator()).append("       - ").append(formatPrescription(p));
        }
        return section.toString();
    }

    private String formatPrescription(Prescription prescription) {
        String prescribedBy = prescription.getPrescribedBy() == 0
            ? "N/A"
            : String.valueOf(prescription.getPrescribedBy());

        return String.format("%s, dosage: %s, frequency: %s, prescribed by ID: %s, dispensed by ID: %d",
                prescription.getMedicationName(),
                prescription.getDosage(),
                prescription.getFrequency(),
                prescribedBy,
                prescription.getDispensedBy());
    }

    private String formatLabTestsList(List<LabTest> labTests) {
        if (labTests.isEmpty()) {
            return "Lab/Imaging Tests: none ordered.";
        }

        StringBuilder section = new StringBuilder("Lab/Imaging Tests:");
        for (int index = 0; index < labTests.size(); index++) {
            section.append(System.lineSeparator());
            section.append(formatSingleLabTest(index + 1, labTests.get(index)));
        }
        return section.toString();
    }

    private String formatSingleLabTest(int index, LabTest labTest) {
        return String.format("  %d. [%s] %s (Ordered date: %s, Ordered by ID: %d)",
                index, labTest.getTestType(), labTest.getTestName(),
                labTest.getOrderedDate(), labTest.getOrderedBy());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof GetHistoryCommand)) {
            return false;
        }

        GetHistoryCommand otherCommand = (GetHistoryCommand) other;
        return nric.equals(otherCommand.nric);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nric", nric)
                .toString();
    }
}
