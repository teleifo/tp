package seedu.clinic.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.clinic.commons.exceptions.IllegalValueException;
import seedu.clinic.model.ClinicBook;
import seedu.clinic.model.ReadOnlyClinicBook;
import seedu.clinic.model.person.Doctor;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Person;
import seedu.clinic.model.person.Pharmacist;

/**
 * An Immutable ClinicBook that is serializable to JSON format.
 */
@JsonRootName(value = "clinicbook")
class JsonSerializableClinicBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATE_DOCTOR = "Doctors list contains duplicate doctor(s).";
    public static final String MESSAGE_DUPLICATE_PHARMACIST = "Pharmacists list contains duplicate pharmacist(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedDoctor> doctors = new ArrayList<>();
    private final List<JsonAdaptedPharmacist> pharmacists = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableClinicBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableClinicBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
                                      @JsonProperty("doctors") List<JsonAdaptedDoctor> doctors,
                                      @JsonProperty("pharmacists") List<JsonAdaptedPharmacist> pharmacists) {
        this.persons.addAll(persons);
        this.doctors.addAll(doctors);
        this.pharmacists.addAll(pharmacists);
    }

    /**
     * Converts a given {@code ReadOnlyClinicBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableClinicBook}.
     */
    public JsonSerializableClinicBook(ReadOnlyClinicBook source) {
        doctors.addAll(source.getDoctorList().stream().map(JsonAdaptedDoctor::new).collect(Collectors.toList()));
        pharmacists.addAll(source.getPharmacistList().stream().map(JsonAdaptedDoctor::new).collect(Collectors.toList()));
        persons.addAll(source.getPersonList().stream()
                .map(person -> {
                    if (person instanceof Patient) {
                        return new JsonAdaptedPatient((Patient) person);
                    }
                    if (person instanceof Doctor) {
                        return new JsonAdaptedDoctor((Doctor) person);
                    }
                    if (person instanceof Pharmacist) {
                        return new JsonAdaptedPharmacist((Pharmacist) person);
                    }
                    return new JsonAdaptedPerson(person);
                })
                .collect(Collectors.toList()));
    }

    /**
     * Converts this clinic book into the model's {@code ClinicBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public ClinicBook toModelType() throws IllegalValueException {
        ClinicBook clinicBook = new ClinicBook();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (clinicBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            clinicBook.addPerson(person);
        }

        for (JsonAdaptedDoctor jsonAdaptedDoctor : doctors) {
            Doctor doctor = jsonAdaptedDoctor.toModelType();
            if (clinicBook.hasDoctor(doctor)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_DOCTOR);
            }
            clinicBook.addDoctor(doctor);
        }

        for (JsonAdaptedPharmacist jsonAdaptedPharmacist : pharmacists) {
            Pharmacist pharmacist = jsonAdaptedPharmacist.toModelType();
            if (clinicBook.hasPharmacist(pharmacist)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PHARMACIST);
            }
            clinicBook.addPharmacist(pharmacist);
        }
        return clinicBook;
    }

}
