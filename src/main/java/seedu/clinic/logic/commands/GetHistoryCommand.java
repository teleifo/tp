package seedu.clinic.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import seedu.clinic.commons.core.LogsCenter;
import seedu.clinic.commons.util.ToStringBuilder;
import seedu.clinic.model.Model;
import seedu.clinic.model.person.Diagnosis;
import seedu.clinic.model.person.LabTest;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.PatientHasNricPredicate;
import seedu.clinic.model.person.Person;
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

        return new CommandResult(formatMedicalHistory(matchedPatients, getPersonNameMap(model)));
    }

    private Map<Integer, String> getPersonNameMap(Model model) {
        Map<Integer, String> personNameMap = new HashMap<>();
        for (Person person : model.getClinicBook().getPersonList()) {
            personNameMap.put(person.getId(), person.getName().toString());
        }
        return personNameMap;
    }

    private String formatMedicalHistory(List<Patient> matchedPatients, Map<Integer, String> personNameMap) {
        return matchedPatients.stream()
                .map(patient -> formatPatientHistory(patient, personNameMap))
                .collect(Collectors.joining("\n\n"));
    }

    private String formatPatientHistory(Patient patient, Map<Integer, String> personNameMap) {
        StringBuilder result = new StringBuilder();
        result.append(formatPatientHeader(patient));
        result.append(System.lineSeparator());
        result.append(formatDiagnosesList(patient.getDiagnoses(), personNameMap));
        result.append(System.lineSeparator());
        result.append(formatLabTestsList(patient.getLabTests(), personNameMap));
        return result.toString();
    }

    private String formatPatientHeader(Patient patient) {
        StringBuilder header = new StringBuilder();
        header.append(String.format("Medical history for %s (NRIC: %s)", patient.getName(), patient.getNric().value));
        header.append("\n");
        header.append(String.format("Date of birth: %s", patient.getDateOfBirth()));
        return header.toString();
    }

    private String formatDiagnosesList(List<Diagnosis> diagnoses, Map<Integer, String> personNameMap) {
        if (diagnoses.isEmpty()) {
            return "Diagnoses: none recorded.";
        }

        StringBuilder diagnosesSection = new StringBuilder("Diagnoses:");
        for (int index = 0; index < diagnoses.size(); index++) {
            diagnosesSection.append(System.lineSeparator());
            diagnosesSection.append(formatSingleDiagnosis(index + 1, diagnoses.get(index), personNameMap));
        }
        return diagnosesSection.toString();
    }

    private String formatSingleDiagnosis(int index, Diagnosis diagnosis, Map<Integer, String> personNameMap) {
        StringBuilder diag = new StringBuilder();
        diag.append(String.format("  %d. %s (Visit date: %s, Diagnosed by: %s)",
                index, diagnosis.getDescription(), diagnosis.getVisitDate(),
                formatPersonReference(diagnosis.getDiagnosedBy(), personNameMap)));
        diag.append(System.lineSeparator());
        diag.append(formatSymptoms(diagnosis.getSymptoms()));
        diag.append(System.lineSeparator());
        diag.append(formatPrescriptionsSection(diagnosis.getPrescriptions(), personNameMap));
        return diag.toString();
    }

    private String formatSymptoms(List<String> symptoms) {
        if (symptoms.isEmpty()) {
            return "     Symptoms: none recorded.";
        }
        return "     Symptoms: " + String.join(", ", symptoms);
    }

    private String formatPrescriptionsSection(List<Prescription> prescriptions, Map<Integer, String> personNameMap) {
        if (prescriptions.isEmpty()) {
            return "     Prescriptions: none recorded.";
        }

        StringBuilder section = new StringBuilder("     Prescriptions:");
        for (Prescription p : prescriptions) {
            section.append(System.lineSeparator()).append("       - ").append(formatPrescription(p, personNameMap));
        }
        return section.toString();
    }

    private String formatPrescription(Prescription prescription, Map<Integer, String> personNameMap) {
        String prescribedBy = prescription.getPrescribedBy() == 0
            ? "N/A"
            : formatPersonReference(prescription.getPrescribedBy(), personNameMap);
        String dispensedBy = formatPersonReference(prescription.getDispensedBy(), personNameMap);

        return String.format("%s, dosage: %s, frequency: %s, prescribed by: %s, dispensed by: %s",
                prescription.getMedicationName(),
                prescription.getDosage(),
                prescription.getFrequency(),
                prescribedBy,
                dispensedBy);
    }

    private String formatLabTestsList(List<LabTest> labTests, Map<Integer, String> personNameMap) {
        if (labTests.isEmpty()) {
            return "Lab/Imaging Tests: none ordered.";
        }

        StringBuilder section = new StringBuilder("Lab/Imaging Tests:");
        for (int index = 0; index < labTests.size(); index++) {
            section.append(System.lineSeparator());
            section.append(formatSingleLabTest(index + 1, labTests.get(index), personNameMap));
        }
        return section.toString();
    }

    private String formatSingleLabTest(int index, LabTest labTest, Map<Integer, String> personNameMap) {
        return String.format("  %d. [%s] %s (Ordered date: %s, Ordered by: %s)",
                index, labTest.getTestType(), labTest.getTestName(),
                labTest.getOrderedDate(), formatPersonReference(labTest.getOrderedBy(), personNameMap));
    }

    private String formatPersonReference(int personId, Map<Integer, String> personNameMap) {
        String personName = personNameMap.get(personId);
        if (personName == null) {
            return String.format("ID %d (name unavailable)", personId);
        }
        return String.format("%s (ID: %d)", personName, personId);
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
