package seedu.clinic.model.person;

import static seedu.clinic.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.clinic.model.tag.Tag;
/**
 * Represents a Patient in the clinic.
 * A Patient is a Person who receives medical services.
 *
 * TODO: Reintroduce emergency contact support after temporary model simplification.
 * TODO: Implement allergies management
 */
public class Patient extends ContactPerson {
    public static final String ROLE = "Patient";

    private final NRIC nric;
    private final LocalDate dateOfBirth;
    private final Sex sex;
    private final List<Diagnosis> diagnoses = new ArrayList<>();

    /**
     * Every field must be present and not null.
     */
    public Patient(Name name, Phone phone, Email email, Address address, Set<Tag> tags,
            NRIC nric, LocalDate dateOfBirth, Sex sex) {
        super(name, phone, email, address, tags);
        requireAllNonNull(nric, dateOfBirth, sex);
        this.nric = nric;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
    }

    /**
     * Every field must be present and not null.
     */
    public Patient(Name name, Phone phone, Email email, Address address, NRIC nric,
            LocalDate dateOfBirth, Sex sex) {
        this(name, phone, email, address, Collections.emptySet(), nric, dateOfBirth, sex);
    }

    /**
     * Constructs a Patient with ID.
     */
    public Patient(Name name, Phone phone, Email email, Address address, Set<Tag> tags,
            NRIC nric, LocalDate dateOfBirth, Sex sex, int id) {
        super(name, phone, email, address, tags, id);
        requireAllNonNull(nric, dateOfBirth, sex);
        this.nric = nric;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
    }

    /**
     * Constructs a Patient with ID.
     */
    public Patient(Name name, Phone phone, Email email, Address address, NRIC nric,
            LocalDate dateOfBirth, Sex sex, int id) {
        this(name, phone, email, address, Collections.emptySet(), nric, dateOfBirth, sex, id);
    }

    /**
     * Reuses an existing person as the shared identity and contact details for a patient.
     */
    public Patient(Person person, NRIC nric, LocalDate dob, Sex sex) {
        this(person.getName(), person.getPhone(), person.getEmail(), person.getAddress(), person.getTags(),
            nric, dob, sex, person.getId());
    }

    public String getRole() {
        return ROLE;
    }

    public NRIC getNric() {
        return nric;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Sex getSex() {
        return sex;
    }

    /**
     * Returns the age of the patient in years.
     */

    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    /**
     * Adds a diagnosis to the patient's diagnosis list.
     */
    public void addDiagnosis(Diagnosis diagnosis) {
        requireAllNonNull(diagnosis);
        diagnoses.add(diagnosis);
    }

    /**
     * Removes a diagnosis from the patient's diagnosis list.
     */
    public void removeDiagnosis(Diagnosis diagnosis) {
        requireAllNonNull(diagnosis);
        diagnoses.remove(diagnosis);
    }

    public List<Diagnosis> getDiagnoses() {
        return Collections.unmodifiableList(diagnoses);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Patient)) {
            return false;
        }

        Patient otherPatient = (Patient) other;
        return super.equals(otherPatient)
                && nric.equals(otherPatient.nric)
                && dateOfBirth.equals(otherPatient.dateOfBirth)
                && sex.equals(otherPatient.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nric, dateOfBirth, sex);
    }
}
