package seedu.clinic.storage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.clinic.commons.exceptions.IllegalValueException;
import seedu.clinic.model.person.Diagnosis;
import seedu.clinic.model.person.NRIC;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Person;
import seedu.clinic.model.person.Sex;

/**
 * Jackson-friendly version of Patient.
 */
class JsonAdaptedPatient extends JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Patient's %s field is missing!";

    private final String nric;
    private final String dateOfBirth;
    private final String sex;
    private final List<JsonAdaptedDiagnosis> diagnoses = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPatient} with the given patient details.
     */
    @JsonCreator
    public JsonAdaptedPatient(@JsonProperty("id") int id, @JsonProperty("name") String name,
                  @JsonProperty("phone") String phone, @JsonProperty("email") String email,
                  @JsonProperty("address") String address, @JsonProperty("tags") List<JsonAdaptedTag> tags,
                  @JsonProperty("nric") String nric,
                  @JsonProperty("dateOfBirth") String dateOfBirth,
                  @JsonProperty("sex") String sex,
                  @JsonProperty("diagnoses") List<JsonAdaptedDiagnosis> diagnoses) {
        super(id, name, phone, email, address, tags);
        this.nric = nric;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        if (diagnoses != null) {
            this.diagnoses.addAll(diagnoses);
        }
    }

    /**
     * Converts a given {@code Patient} into this class for Jackson use.
     */
    public JsonAdaptedPatient(Patient source) {
        super(source.getId(), source.getName().fullName, source.getPhone().value,
                source.getEmail().value, source.getAddress().value,
                new ArrayList<>(source.getTags().stream()
                        .map(JsonAdaptedTag::new)
                        .collect(Collectors.toList())));

        nric = source.getNric().value;
        dateOfBirth = source.getDateOfBirth().toString();
        sex = source.getSex().name();

        diagnoses.addAll(source.getDiagnoses().stream()
                .map(JsonAdaptedDiagnosis::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted patient object into the model's {@code Patient} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public Patient toModelType() throws IllegalValueException {
        Person person = super.toModelType();

        if (nric == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "NRIC"));
        }
        if (!NRIC.isValidNric(nric)) {
            throw new IllegalValueException(NRIC.MESSAGE_CONSTRAINTS);
        }
        final NRIC modelNric = new NRIC(nric);

        if (dateOfBirth == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "dateOfBirth"));
        }
        final LocalDate modelDob;
        try {
            modelDob = LocalDate.parse(dateOfBirth);
        } catch (RuntimeException e) {
            throw new IllegalValueException("Patient's dateOfBirth is not a valid date!");
        }

        if (sex == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "sex"));
        }
        final Sex modelSex;
        try {
            modelSex = Sex.valueOf(sex.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException("Patient's sex is invalid!");
        }

        final List<Diagnosis> modelDiagnoses = new ArrayList<>();
        for (JsonAdaptedDiagnosis d : diagnoses) {
            modelDiagnoses.add(d.toModelType());
        }

        Patient patient = new Patient(person, modelNric, modelDob, modelSex);
        modelDiagnoses.forEach(patient::addDiagnosis);

        return patient;
    }
}
