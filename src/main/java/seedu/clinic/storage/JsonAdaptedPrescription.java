package seedu.clinic.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// import seedu.clinic.commons.exceptions.IllegalValueException;
// TODO: Enable after PR #52 merges
// Currently omitted to avoid dependency on unmerged classes
//import seedu.clinic.model.person.Pharmacist;
//import seedu.clinic.model.person.Prescription;

/**
 * Jackson-friendly version of {@link `Prescription`}.
 */
class JsonAdaptedPrescription {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Prescription's %s field is missing!";

    private final String medicationName;
    private final String dosage;
    private final String frequency;
    private final String dispensedBy;

    /**
     * Constructs a {@code JsonAdaptedPrescription} with the given prescription details.
     */
    @JsonCreator
    public JsonAdaptedPrescription(@JsonProperty("medicationName") String medicationName,
                                   @JsonProperty("dosage") String dosage,
                                   @JsonProperty("frequency") String frequency,
                                   @JsonProperty("dispensedBy") String dispensedBy) {
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.dispensedBy = dispensedBy;
    }

    // /**
    //  * Converts a given {@code Prescription} into this class for Jackson use.
    //  */
    // TODO: Enable after PR #52 merges
    // Currently omitted to avoid dependency on unmerged classes
//    public JsonAdaptedPrescription(Prescription source) {
//        medicationName = source.getMedicationName();
//        dosage = source.getDosage();
//        frequency = source.getFrequency();
//        dispensedBy = source.getDispensedBy().getName();
//    }

    // /**
    //  * Converts this Jackson-friendly adapted prescription object into the model's {@code Prescription} object.
    //  *
    //  * @throws IllegalValueException if there were any data constraints violated.
    //  */
    // TODO: Enable after PR #52 merges
    // Currently omitted to avoid dependency on unmerged classes
//    public Prescription toModelType() throws IllegalValueException {
//        if (medicationName == null) {
//            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "medicationName"));
//        }
//        if (dosage == null) {
//            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "dosage"));
//        }
//        if (frequency == null) {
//            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "frequency"));
//        }
//        if (dispensedBy == null) {
//            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "dispensedBy"));
//        }
//
//        final Pharmacist modelPharmacist = new Pharmacist(dispensedBy);
//
//        return new Prescription(medicationName, dosage, frequency, modelPharmacist);
//    }
}
