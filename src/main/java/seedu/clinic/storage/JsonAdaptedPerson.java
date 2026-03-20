package seedu.clinic.storage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import seedu.clinic.commons.exceptions.IllegalValueException;
import seedu.clinic.model.person.Address;
import seedu.clinic.model.person.Email;
import seedu.clinic.model.person.NRIC;
import seedu.clinic.model.person.Name;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Person;
import seedu.clinic.model.person.Phone;
import seedu.clinic.model.person.Sex;
import seedu.clinic.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = JsonAdaptedPerson.class, name = "person"),
    @JsonSubTypes.Type(value = JsonAdaptedPatient.class, name = "patient"),
    @JsonSubTypes.Type(value = JsonAdaptedDoctor.class, name = "doctor"),
    @JsonSubTypes.Type(value = JsonAdaptedPharmacist.class, name = "pharmacist")
})
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final int id;
    private final String name;
    private final String nric;
    private final String dateOfBirth;
    private final String sex;
    private final String emergencyContact;
    private final String phone;
    private final String email;
    private final String address;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("id") int id, @JsonProperty("type") String type,
             @JsonProperty("name") String name, @JsonProperty("nric") String nric,
             @JsonProperty("dateOfBirth") String dateOfBirth,
             @JsonProperty("sex") String sex,
             @JsonProperty("emergencyContact") String emergencyContact,
             @JsonProperty("phone") String phone,
             @JsonProperty("email") String email, @JsonProperty("address") String address,
             @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.id = id;
        this.name = name;
        this.nric = nric;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.emergencyContact = emergencyContact;
        this.phone = phone;
        this.email = email;
        this.address = address;
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Compatibility constructor for plain person records in tests.
     */
    public JsonAdaptedPerson(int id, String name, String phone, String email, String address,
            List<JsonAdaptedTag> tags) {
        this(id, null, name, null, null, null, null, phone, email, address, tags);
    }

    /**
     * Compatibility constructor that omits patient sex.
     */
    public JsonAdaptedPerson(int id, String type, String name, String nric, String dateOfBirth,
            String emergencyContact, String phone, String email, String address, List<JsonAdaptedTag> tags) {
        this(id, type, name, nric, dateOfBirth, null, emergencyContact, phone, email, address, tags);
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        id = source.getId();
        name = source.getName().fullName;
        nric = source instanceof Patient ? ((Patient) source).getNric().value : null;
        dateOfBirth = source instanceof Patient ? ((Patient) source).getDateOfBirth().toString() : null;
        sex = source instanceof Patient ? ((Patient) source).getSex().name() : null;
        address = source instanceof Patient ? ((Patient) source).getAddress().value : null;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        if (id == 0) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "id"));
        }
        final int modelId = id;

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<Tag> modelTags = new HashSet<>(personTags);
        Person modelPerson = new Person(modelName, modelPhone, modelEmail, modelAddress, modelTags, modelId);

        if (type == null || TYPE_PERSON.equalsIgnoreCase(type)) {
            return modelPerson;
        }

        if (!TYPE_PATIENT.equalsIgnoreCase(type)) {
            throw new IllegalValueException(String.format(MESSAGE_INVALID_PERSON_TYPE, type));
        }

        if (nric == null) {
            throw new IllegalValueException(String.format(MISSING_PATIENT_FIELD_MESSAGE_FORMAT, "NRIC"));
        }
        String normalizedNric = nric.toUpperCase();
        if (!NRIC.isValidNric(normalizedNric)) {
            throw new IllegalValueException(NRIC.MESSAGE_CONSTRAINTS);
        }

        if (dateOfBirth == null) {
            throw new IllegalValueException(String.format(MISSING_PATIENT_FIELD_MESSAGE_FORMAT, "dateOfBirth"));
        }

        final NRIC modelNric = new NRIC(normalizedNric);
        final LocalDate modelDateOfBirth;
        try {
            modelDateOfBirth = LocalDate.parse(dateOfBirth);
        } catch (RuntimeException e) {
            throw new IllegalValueException("Patient's dateOfBirth is not a valid date!");
        }

        final Sex modelSex;
        if (sex == null) {
            modelSex = Sex.FEMALE;
        } else {
            try {
                modelSex = Sex.valueOf(sex.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalValueException("Patient's sex is invalid!");
            }
        }

        return new Patient(modelPerson, modelNric, modelDateOfBirth, modelSex);
    }

}
