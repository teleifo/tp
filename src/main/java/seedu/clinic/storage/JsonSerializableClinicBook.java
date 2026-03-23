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
import seedu.clinic.model.person.Person;

/**
 * An Immutable ClinicBook that is serializable to JSON format.
 */
@JsonRootName(value = "clinicbook")
class JsonSerializableClinicBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableClinicBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableClinicBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons) {
        if (persons != null) {
            this.persons.addAll(persons);
        }
    }

    /**
     * Converts a given {@code ReadOnlyClinicBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableClinicBook}.
     */
    public JsonSerializableClinicBook(ReadOnlyClinicBook source) {
        persons.addAll(source.getPersonList().stream()
                .map(person -> {
                    if (person instanceof seedu.clinic.model.person.Patient) {
                        return new JsonAdaptedPatient((seedu.clinic.model.person.Patient) person);
                    }
                    if (person instanceof seedu.clinic.model.person.Doctor) {
                        return new JsonAdaptedDoctor((seedu.clinic.model.person.Doctor) person);
                    }
                    if (person instanceof seedu.clinic.model.person.Pharmacist) {
                        return new JsonAdaptedPharmacist((seedu.clinic.model.person.Pharmacist) person);
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
        return clinicBook;
    }
}
