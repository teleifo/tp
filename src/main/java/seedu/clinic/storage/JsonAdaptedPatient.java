package seedu.clinic.storage;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.clinic.commons.exceptions.IllegalValueException;
import seedu.clinic.model.person.Diagnosis;
import seedu.clinic.model.person.NRIC;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Person;
import seedu.clinic.model.person.Sex;
import seedu.clinic.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Patient}.
 */
class JsonAdaptedPatient extends JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Patient's %s field is missing!";
    private static final String FIELD_NRIC = "NRIC";
    private static final String FIELD_DATE_OF_BIRTH = "dateOfBirth";
    private static final String FIELD_SEX = "sex";
    private static final String INVALID_DATE_OF_BIRTH_MESSAGE = "Patient's dateOfBirth is not a valid date!";
    private static final String INVALID_SEX_MESSAGE = "Patient's sex is invalid!";

    private final String nric;
    private final String dateOfBirth;
    private final String sex;
    private final List<JsonAdaptedTag> allergies = new ArrayList<>();
    private final List<JsonAdaptedDiagnosis> diagnoses = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPatient} with the given patient details.
     */
    @JsonCreator
    public JsonAdaptedPatient(@JsonProperty("id") int id, @JsonProperty("name") String name,
                  @JsonProperty("phone") String phone, @JsonProperty("email") String email,
                  @JsonProperty("address") String address,
                  @JsonProperty("allergies") List<JsonAdaptedTag> allergies,
                  @JsonProperty("nric") String nric,
                  @JsonProperty("dateOfBirth") String dateOfBirth,
                  @JsonProperty("sex") String sex,
                  @JsonProperty("diagnoses") List<JsonAdaptedDiagnosis> diagnoses) {
        super(id, name, phone, email, address);
        this.nric = nric;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        if (allergies != null) {
            this.allergies.addAll(allergies);
        }
        if (diagnoses != null) {
            this.diagnoses.addAll(diagnoses);
        }
    }

    /**
     * Converts a given {@code Patient} into this class for Jackson use.
     */
    public JsonAdaptedPatient(Patient source) {
        super(source.getId(), source.getName().fullName, source.getPhone().value,
                source.getEmail().value, source.getAddress().value);

        allergies.addAll(source.getAllergies().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));

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
    @Override
    public Patient toModelType() throws IllegalValueException {
        Person person = super.toModelType();

        if (nric == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, FIELD_NRIC));
        }

        if (!NRIC.isValidNric(nric)) {
            throw new IllegalValueException(NRIC.MESSAGE_CONSTRAINTS);
        }
        final NRIC modelNric = new NRIC(nric);

        if (dateOfBirth == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, FIELD_DATE_OF_BIRTH));
        }

        final LocalDate modelDob;
        try {
            modelDob = LocalDate.parse(dateOfBirth);
        } catch (DateTimeParseException e) {
            throw new IllegalValueException(INVALID_DATE_OF_BIRTH_MESSAGE);
        }

        if (sex == null || sex.isBlank()) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, FIELD_SEX));
        }

        final Sex modelSex;
        try {
            modelSex = Sex.valueOf(sex.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException(INVALID_SEX_MESSAGE);
        }

        final Set<Tag> modelAllergies = new HashSet<>();
        for (JsonAdaptedTag tag : allergies) {
            modelAllergies.add(tag.toModelType());
        }

        final List<Diagnosis> modelDiagnoses = new ArrayList<>();
        for (JsonAdaptedDiagnosis d : diagnoses) {
            modelDiagnoses.add(d.toModelType());
        }

        Patient patient = new Patient(person, modelAllergies, modelNric, modelDob, modelSex);
        modelDiagnoses.forEach(patient::addDiagnosis);

        return patient;
    }
}
