package seedu.clinic.model.person;

import static seedu.clinic.commons.util.AppUtil.checkArgument;
import static seedu.clinic.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import seedu.clinic.commons.util.ToStringBuilder;

/**
 * Represents a medical diagnosis when a Doctor sees a Patient.
 *
 * TODO: Change to visitDate:LocalDateTime
 * TODO: Implement generateMedicalCertificate() for medical certificates
 */
public class Diagnosis {

    public static final String MESSAGE_CONSTRAINTS =
            "Diagnosis descriptions should not be blank";
    public static final String VALIDATION_REGEX = "[^\\s].*";

    private final String description;
    private final LocalDate visitDate;
    private final Doctor diagnosedBy;
    private final List<String> symptoms = new ArrayList<>();
    private final List<Prescription> prescriptions = new ArrayList<>();

    /**
     * Constructs a {@code Diagnosis}.
     *
     * If no visit date is provided, the current date is used as the visit date.
     * 
     * @param diagnosis A valid diagnosis description.
     * @param diagnosedBy Doctor who gave this diagnosis.
     */
    public Diagnosis(String diagnosis, Doctor diagnosedBy) {
        this(diagnosis, LocalDate.now(), diagnosedBy);
    }

    /**
     * Constructs a {@code Diagnosis} with a visit date.
     *
     * @param diagnosis A valid diagnosis description.
     * @param visitDate Date of the visit associated with the diagnosis.
     * @param diagnosedBy Doctor who gave this diagnosis.
     */
    public Diagnosis(String diagnosis, LocalDate visitDate, Doctor diagnosedBy) {
        requireAllNonNull(diagnosis, visitDate, diagnosedBy);
        checkArgument(isValidDiagnosis(diagnosis), MESSAGE_CONSTRAINTS);
        description = diagnosis;
        this.visitDate = visitDate;
        this.diagnosedBy = diagnosedBy;
    }

    /**
     * Returns true if a given string is a valid diagnosis description.
     */
    public static boolean isValidDiagnosis(String test) {
        requireAllNonNull(test);
        return test.matches(VALIDATION_REGEX);
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public Doctor getDiagnosedBy() {
        return diagnosedBy;
    }

    /**
     * Returns the diagnosis description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Adds a symptom for this diagnosis.
     */
    public void addSymptom(String symptom) {
        requireAllNonNull(symptom);
        checkArgument(isValidDiagnosis(symptom), MESSAGE_CONSTRAINTS);
        symptoms.add(symptom);
    }

    /**
     * Removes a symptom from this diagnosis.
     */
    public void removeSymptom(String symptom) {
        requireAllNonNull(symptom);
        symptoms.remove(symptom);
    }

    public List<String> getSymptoms() {
        return Collections.unmodifiableList(symptoms);
    }

    /**
     * Adds a prescription to this diagnosis.
     */
    public void addPrescription(Prescription prescription) {
        requireAllNonNull(prescription);
        prescriptions.add(prescription);
    }

    /**
     * Removes a prescription from this diagnosis.
     */
    public void removePrescription(Prescription prescription) {
        requireAllNonNull(prescription);
        prescriptions.remove(prescription);
    }

    public List<Prescription> getPrescriptions() {
        return Collections.unmodifiableList(prescriptions);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("description", description)
                .add("visitDate", visitDate)
                .add("diagnosedBy", diagnosedBy)
                .add("symptoms", symptoms)
                .add("prescriptions", prescriptions)
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Diagnosis)) {
            return false;
        }

        Diagnosis otherDiagnosis = (Diagnosis) other;
        return description.equals(otherDiagnosis.description)
                && Objects.equals(visitDate, otherDiagnosis.visitDate)
                && diagnosedBy.equals(otherDiagnosis.diagnosedBy)
                && symptoms.equals(otherDiagnosis.symptoms)
                && prescriptions.equals(otherDiagnosis.prescriptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, visitDate, diagnosedBy, symptoms, prescriptions);
    }
}
