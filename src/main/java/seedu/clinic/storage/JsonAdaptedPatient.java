package seedu.clinic.storage;

//import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
//import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

//import seedu.clinic.commons.exceptions.IllegalValueException;
// TODO: Enable after PR #52 merges
// Currently omitted to avoid dependency on unmerged classes
//import seedu.clinic.model.person.NRIC;
//import seedu.clinic.model.person.Patient;
//import seedu.clinic.model.person.Person;

/**
 * Jackson-friendly version of {@link `Patient`}.
 */
class JsonAdaptedPatient {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Patient's %s field is missing!";

    private final JsonAdaptedPerson person;

    private final String nric;
    private final String dateOfBirth;
    private final String emergencyContact;
    private final List<JsonAdaptedDiagnosis> diagnoses = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPatient} with the given patient details.
     */
    @JsonCreator
    public JsonAdaptedPatient(@JsonProperty("person") JsonAdaptedPerson person,
                              @JsonProperty("nric") String nric,
                              @JsonProperty("dateOfBirth") String dateOfBirth,
                              @JsonProperty("emergencyContact") String emergencyContact,
                              @JsonProperty("diagnoses") List<JsonAdaptedDiagnosis> diagnoses) {

        this.person = person;
        this.nric = nric;
        this.dateOfBirth = dateOfBirth;
        this.emergencyContact = emergencyContact;
    }
/*
    /**
     * Converts a given {@code Patient} into this class for Jackson use.
     */
    // TODO: Enable after PR #52 merges
    // Currently omitted to avoid dependency on unmerged classes
//    public JsonAdaptedPatient(Patient source) {
//        person = new JsonAdaptedPerson(source);
//
//        nric = source.getNric().value;
//        dateOfBirth = source.getDateOfBirth().toString();
//        emergencyContact = source.getEmergencyContact();
//
//        diagnoses.addAll(source.getDiagnoses().stream()
//                .map(JsonAdaptedDiagnosis::new)
//                .collect(Collectors.toList()));
//    }

    /*
    /**
     * Converts this Jackson-friendly adapted patient object into the model's {@code Patient} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    // TODO: Enable after PR #52 merges
    // Currently omitted to avoid dependency on unmerged classes
//    public Patient toModelType() throws IllegalValueException {
//        if (person == null) {
//            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "person"));
//        }
//        final Person modelPerson = person.toModelType();
//
//        if (nric == null) {
//            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "NRIC"));
//        }
//        if (!NRIC.isValidNric(nric)) {
//            throw new IllegalValueException(NRIC.MESSAGE_CONSTRAINTS);
//        }
//        final NRIC modelNric = new NRIC(nric);
//
//        if (dateOfBirth == null) {
//            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "dateOfBirth"));
//        }
//        final LocalDate modelDob = LocalDate.parse(dateOfBirth);
//
//        if (emergencyContact == null) {
//            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "emergencyContact"));
//        }
//
//        final List<Diagnosis> modelDiagnoses = new ArrayList<>();
//        for (JsonAdaptedDiagnosis d : diagnoses) {
//            modelDiagnoses.add(d.toModelType());
//        }
//
//        return new Patient(modelPerson, modelNric, modelDob, emergencyContact, modelDiagnoses);
//    }
}
