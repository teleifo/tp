package seedu.clinic.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.clinic.commons.util.ToStringBuilder;
import seedu.clinic.model.person.Diagnosis;
import seedu.clinic.model.person.Doctor;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Person;
import seedu.clinic.model.person.Pharmacist;
import seedu.clinic.model.person.UniquePersonList;

/**
 * Wraps all data at the clinicbook level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class ClinicBook implements ReadOnlyClinicBook {

    private final UniquePersonList<Person> persons;
    private final UniquePersonList<Patient> patients;
    private final UniquePersonList<Pharmacist> pharmacists;
    private final UniquePersonList<Doctor> doctors;
    // id counter for Patient
    private int nextId = 1;
    private int nextPatientId = 1;
    private int nextStaffId = 1;
    // id counter for Staff;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList<Person>();
        patients = new UniquePersonList<Patient>();
        pharmacists = new UniquePersonList<Pharmacist>();
        doctors = new UniquePersonList<Doctor>();
    }

    public ClinicBook() {}

    /**
     * Creates an ClinicBook using the Persons in the {@code toBeCopied}
     */
    public ClinicBook(ReadOnlyClinicBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        for (Person p : persons) {
            assignIdIfMissing(p);
        }
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the patient list with {@code patients}.
     */
    public void setPatients(List<Patient> patients) {
        for (Patient p : patients) {
            if (p.getId() == 0) {
                p.setId(getNextPatientId());
            }
        }
        this.patients.setPersons(patients);
    }

    /**
     * Replaces the contents of the doctor list with {@code doctors}.
     * {@code doctors} must not contain duplicate doctors.
     */
    public void setDoctors(List<Doctor> doctors) {
        for (Doctor d : doctors) {
            if (d.getId() == 0) {
                d.setId(getNextStaffId());
            }
        }
        this.doctors.setPersons(doctors);
    }

    /**
     * Replaces the contents of the doctor list with {@code pharmacist}.
     * {@code pharmacist} must not contain duplicate pharmacist.
     */
    public void setPharmacists(List<Pharmacist> pharmacists) {
        for (Pharmacist p : pharmacists) {
            if (p.getId() == 0) {
                p.setId(getNextStaffId());
            }
        }
        this.pharmacists.setPersons(pharmacists);
    }

    /**
     * Resets the existing data of this {@code ClinicBook} with {@code newData}.
     */
    public void resetData(ReadOnlyClinicBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setPatients(newData.getPatientList());
        setPharmacists(newData.getPharmacistList());
        setDoctors(newData.getDoctorList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the clinic book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Returns true if a patient with the same identity as {@code patient} exists in the clinic book.
     */
    public boolean hasPatient(Patient patient) {
        requireNonNull(patient);
        return patients.contains(patient);
    }

    /**
     * Returns true if a doctor with the same identity as {@code doctor} exists in the clinic book.
     */
    public boolean hasDoctor(Doctor doctor) {
        requireNonNull(doctor);
        return doctors.contains(doctor);
    }

    /**
     * Returns true if a pharmacist with the same identity as {@code pharmacist} exists in the clinic book.
     */
    public boolean hasPharmacist(Pharmacist pharmacist) {
        requireNonNull(pharmacist);
        return pharmacists.contains(pharmacist);
    }

    /**
     * Adds a person to clinic book.
     * The person must not already exist in clinic book.
     */
    public void addPerson(Person p) {
        // If ID is 0 (default), assign a new one
        assignIdIfMissing(p);
        persons.add(p);
    }

    /**
     * Adds a Patient to clinic book.
     * The patient must not already exist in clinic book.
     */
    public void addPatient(Patient p) {
        requireNonNull(p);
        if (p.getId() == 0) {
            p.setId(getNextPatientId());
        }
        patients.add(p);
    }

    /**
     * Adds a doctor to clinic book.
     * The doctor must not already exist in clinic book.
     */
    public void addDoctor(Doctor d) {
        // If ID is 0 (default), assign a new one
        if (d.getId() == 0) {
            d.setId(getNextStaffId());
        }
        doctors.add(d);
    }

    /**
     * Adds a pharmacist to clinic book.
     * The pharmacist must not already exist in clinic book.
     */
    public void addPharmacist(Pharmacist p) {
        // If ID is 0 (default), assign a new one
        if (p.getId() == 0) {
            p = new Pharmacist(p.getName(), p.getPhone(), p.getEmail(), getNextStaffId());
        }
        pharmacists.add(p);
    }

    /**
     * Returns the next available ID and increments the counter
     */
    public int getNextId() {
        int maxId = persons.stream()
                .mapToInt(Person::getId)
                .max()
                .orElse(0);
        nextId = maxId + 1;
        return nextId++;
    }

    /**
     * Returns the next available patient ID and increments the counter
     */
    public int getNextPatientId() {
        int maxId = patients.stream()
                .mapToInt(Person::getId)
                .max()
                .orElse(0);
        nextPatientId = maxId + 1;
        return nextPatientId++;
    }

    /**
     * Returns the next available Doctor ID and increments the counter
     */
    public int getNextStaffId() {
        int currentStaffCount = (int) (doctors.stream().count() + pharmacists.stream().count());
        if (nextStaffId <= currentStaffCount) {
            nextStaffId = currentStaffCount + 1;
        }
        return nextStaffId++;
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in clinic book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in clinic book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        assignIdIfMissing(editedPerson);
        persons.setPerson(target, editedPerson);
    }

    private void assignIdIfMissing(Person person) {
        if (person.getId() == 0) {
            person.setId(getNextId());
        }
    }

    /**
     * Replaces the given patient {@code target} in the list with {@code editedPatient}.
     * {@code target} must exist in clinic book.
     * The person identity of {@code editedPatient} must not be the same as another existing patient in clinic book.
     */
    public void setPatient(Patient target, Patient editedPatient) {
        requireNonNull(editedPatient);

        assignIdIfMissing(editedPatient);
        patients.setPerson(target, editedPatient);
    }

    /**
     * Replaces the given doctor {@code target} in the list with {@code editedDoctor}.
     * {@code target} must exist in clinic book.
     * The doctor identity of {@code editedDoctor} must not be the same as another existing doctor in clinic book.
     */
    public void setDoctor(Doctor target, Doctor editedDoctor) {
        requireNonNull(editedDoctor);

        // assign new patient id if editedPerson has no id
        if (editedDoctor.getId() == 0) {
            editedDoctor.setId(getNextStaffId());
        }
        doctors.setPerson(target, editedDoctor);
    }

    /**
     * Replaces the given pharmacist {@code target} in the list with {@code editedPharmacist}.
     * {@code target} must exist in clinic book.
        * The pharmacist identity of {@code editedPharmacist} must not be the same as another existing
        * pharmacist in clinic book.
     */
    public void setPharmacist(Pharmacist target, Pharmacist editedPharmacist) {
        requireNonNull(editedPharmacist);

        // assign new patient id if editedPerson has no id
        if (editedPharmacist.getId() == 0) {
            editedPharmacist.setId(getNextStaffId());
        }
        pharmacists.setPerson(target, editedPharmacist);
    }

    /**
     * Removes {@code key} from this {@code ClinicBook}.
     * {@code key} must exist in clinic book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    /**
     * Removes {@code key} from this {@code ClinicBook}.
     * {@code key} must exist in clinic book.
     */
    public void removePatient(Patient key) {
        patients.remove(key);
    }

    /**
     * Removes {@code key} from this {@code ClinicBook}.
     * {@code key} must exist in clinic book.
     */
    public void removeDoctor(Doctor key) {
        doctors.remove(key);
    }

    /**
     * Removes {@code key} from this {@code ClinicBook}.
     * {@code key} must exist in clinic book.
     */
    public void removePharmacist(Pharmacist key) {
        pharmacists.remove(key);
    }

    /**
     * Adds a diagnosis to clinic book.
     */
    public void addDiagnosis(Patient target, Diagnosis diagnosis) {
        requireNonNull(target);
        requireNonNull(diagnosis);

        Patient editedPatient = new Patient(
            target.getName(),
            target.getPhone(),
            target.getEmail(),
            target.getAddress(),
            target.getTags(),
            target.getNric(),
            target.getDateOfBirth(),
            target.getSex(),
            target.getId());

        target.getDiagnoses().forEach(editedPatient::addDiagnosis);
        editedPatient.addDiagnosis(diagnosis);

        patients.setPerson(target, editedPatient);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .add("patients", patients)
                .add("doctors", doctors)
                .add("pharmacists", pharmacists)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Patient> getPatientList() {
        return patients.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Doctor> getDoctorList() {
        return doctors.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Pharmacist> getPharmacistList() {
        return pharmacists.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ClinicBook)) {
            return false;
        }

        ClinicBook otherClinicBook = (ClinicBook) other;
        return persons.equals(otherClinicBook.persons)
                && patients.equals(otherClinicBook.patients)
                && doctors.equals(otherClinicBook.doctors)
                && pharmacists.equals(otherClinicBook.pharmacists);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
