package seedu.clinic.storage;

// import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
// import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// import seedu.clinic.commons.exceptions.IllegalValueException;
// TODO: Enable after PR #52 merges
// Currently omitted to avoid dependency on unmerged classes
//import seedu.clinic.model.person.Diagnosis;
//import seedu.clinic.model.person.Doctor;
//import seedu.clinic.model.person.Prescription;

/**
 * Jackson-friendly version of {@link `Diagnosis`}.
 */
class JsonAdaptedDiagnosis {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Diagnosis's %s field is missing!";

    private final String description;
    private final String visitDate;
    private final String diagnosedBy;
    private final List<String> symptoms = new ArrayList<>();
    private final List<JsonAdaptedPrescription> prescriptions = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedDiagnosis} with the given diagnosis details.
     */
    @JsonCreator
    public JsonAdaptedDiagnosis(@JsonProperty("description") String description,
                                @JsonProperty("visitDate") String visitDate,
                                @JsonProperty("diagnosedBy") String diagnosedBy,
                                @JsonProperty("symptoms") List<String> symptoms,
                                @JsonProperty("prescriptions") List<JsonAdaptedPrescription> prescriptions) {

        this.description = description;
        this.visitDate = visitDate;
        this.diagnosedBy = diagnosedBy;

        if (symptoms != null) {
            this.symptoms.addAll(symptoms);
        }
        if (prescriptions != null) {
            this.prescriptions.addAll(prescriptions);
        }
    }

    // /**
    //  * Converts a given {@code Diagnosis} into this class for Jackson use.
    //  */
    // TODO: Enable after PR #52 merges
    // Currently omitted to avoid dependency on unmerged classes
//    public JsonAdaptedDiagnosis(Diagnosis source) {
//        description = source.getDescription();
//        visitDate = source.getVisitDate().toString();
//        diagnosedBy = source.getDiagnosedBy().getName();
//
//        symptoms.addAll(source.getSymptoms());
//
//        prescriptions.addAll(source.getPrescriptions().stream()
//                .map(JsonAdaptedPrescription::new)
//                .collect(Collectors.toList()));
//    }

    // /**
    //  * Converts this Jackson-friendly adapted diagnosis object into the model's {@code Diagnosis} object.
    //  *
    //  * @throws IllegalValueException if there were any data constraints violated.
    //  */
    // TODO: Enable after PR #52 merges
    // Currently omitted to avoid dependency on unmerged classes
//    public Diagnosis toModelType() throws IllegalValueException {
//        if (description == null) {
//            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "description"));
//        }
//
//        if (visitDate == null) {
//            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "visitDate"));
//        }
//        final LocalDate modelVisitDate = LocalDate.parse(visitDate);
//
//        if (diagnosedBy == null) {
//            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "diagnosedBy"));
//        }
//        final Doctor modelDoctor = new Doctor(diagnosedBy); // assumes constructor exists
//
//        final List<Prescription> modelPrescriptions = new ArrayList<>();
//        for (JsonAdaptedPrescription p : prescriptions) {
//            modelPrescriptions.add(p.toModelType());
//        }
//
//        return new Diagnosis(description, modelVisitDate, modelDoctor, symptoms, modelPrescriptions);
//    }
*/

}
