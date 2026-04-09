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
 */
public class Patient extends Person {
    public static final String ROLE = "Patient";

    private final Set<Tag> allergies;
    private final NRIC nric;
    private final LocalDate dateOfBirth;
    private final Sex sex;
    private final List<Diagnosis> diagnoses = new ArrayList<>();
    private final List<LabTest> labTests = new ArrayList<>();

    /**
     * Every field must be present and not null.
     */
    public Patient(Name name, Phone phone, Email email, Address address, Set<Tag> allergies,
            NRIC nric, LocalDate dateOfBirth, Sex sex) {
        super(name, phone, email, address);
        requireAllNonNull(nric, dateOfBirth, sex);
        this.nric = nric;
        this.allergies = allergies;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
    }

    /**
     * Constructs a Patient with ID.
     */
    public Patient(Name name, Phone phone, Email email, Address address, Set<Tag> allergies,
            NRIC nric, LocalDate dateOfBirth, Sex sex, int id) {
        super(name, phone, email, address, id);
        requireAllNonNull(nric, dateOfBirth, sex);
        this.nric = nric;
        this.allergies = allergies;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
    }

    /**
     * Reuses an existing person as the shared identity and contact details for a patient.
     */
    public Patient(Person person, Set<Tag> allergies, NRIC nric, LocalDate dob, Sex sex) {
        this(person.getName(), person.getPhone(), person.getEmail(), person.getAddress(), allergies,
            nric, dob, sex, person.getId());
    }


    public String getRole() {
        return ROLE;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getAllergies() {
        return Collections.unmodifiableSet(allergies);
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

    /**
     * Adds an allergy to the patient's allergy set.
     */
    public void addAllergy(Tag allergy) {
        requireAllNonNull(allergy);
        allergies.add(allergy);
    }

    /**
     * Removes an allergy from the patient's allergy set.
     */
    public void removeAllergy(Tag allergy) {
        requireAllNonNull(allergy);
        allergies.remove(allergy);
    }


    public List<Diagnosis> getDiagnoses() {
        return Collections.unmodifiableList(diagnoses);
    }

    /**
     * Adds a lab test to the patient's lab test list.
     */
    public void addLabTest(LabTest labTest) {
        requireAllNonNull(labTest);
        labTests.add(labTest);
    }

    /**
     * Removes a lab test from the patient's lab test list.
     */
    public void removeLabTest(LabTest labTest) {
        requireAllNonNull(labTest);
        labTests.remove(labTest);
    }

    public List<LabTest> getLabTests() {
        return Collections.unmodifiableList(labTests);
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
                && allergies.equals(otherPatient.allergies)
                && sex.equals(otherPatient.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nric, dateOfBirth, sex, allergies);
    }
}
