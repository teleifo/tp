package seedu.clinic.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.clinic.model.ClinicBook;

public class PatientTest {

    @Test
    public void constructor_andGetters_success() {
        Patient patient = new Patient(
                new Name("Alice Patient"),
                new Phone("91234567"),
                new Email("alice@example.com"),
                new Address("1 Street"),
                new NRIC("S1166846A"),
                LocalDate.of(2000, 1, 1),
                Sex.FEMALE,
                5);

        assertEquals("S1166846A", patient.getNric().value);
        assertEquals(LocalDate.of(2000, 1, 1), patient.getDateOfBirth());
        assertEquals(Sex.FEMALE, patient.getSex());
    }

    @Test
    public void getAge_sameDayBirth_returnsExpectedYears() {
        LocalDate dob = LocalDate.now().minusYears(1);
        Patient patient = new Patient(
                new Name("Alice Patient"),
                new Phone("91234567"),
                new Email("alice@example.com"),
                new Address("1 Street"),
                new NRIC("S1166846A"),
                dob,
                Sex.FEMALE,
                6);

        assertEquals(1, patient.getAge());
    }

    @Test
    public void addAndRemoveDiagnosis_success() {
        Patient patient = new Patient(
                new Name("Alice Patient"),
                new Phone("91234567"),
                new Email("alice@example.com"),
                new Address("1 Street"),
                new NRIC("S1166846A"),
                LocalDate.of(2000, 1, 1),
                Sex.FEMALE,
                7);
        Diagnosis diagnosis = new Diagnosis("Flu", 2);

        patient.addDiagnosis(diagnosis);
        assertEquals(1, patient.getDiagnoses().size());

        patient.removeDiagnosis(diagnosis);
        assertTrue(patient.getDiagnoses().isEmpty());
    }

    @Test
    public void equalsAndHashCode() {
        Patient first = new Patient(
                new Name("Alice Patient"),
                new Phone("91234567"),
                new Email("alice@example.com"),
                new Address("1 Street"),
                new NRIC("S1166846A"),
                LocalDate.of(2000, 1, 1),
                Sex.FEMALE,
                8);
        Patient second = new Patient(
                new Name("Alice Patient"),
                new Phone("91234567"),
                new Email("alice@example.com"),
                new Address("1 Street"),
                new NRIC("S1166846A"),
                LocalDate.of(2000, 1, 1),
                Sex.FEMALE,
                8);

        assertTrue(first.equals(first));
        assertFalse(first.equals(null));
        assertFalse(first.equals(5));
        assertTrue(first.equals(second));
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void constructorFromPerson_setsId() {
        Person person = new Person(
                new Name("Base Person"),
                new Phone("91234567"),
                new Email("base@example.com"),
                new Address("1 Street"),
                Set.of(),
                10);

        Patient patient = new Patient(person, new NRIC("S1166846A"), LocalDate.of(2000, 1, 1), Sex.FEMALE);
        assertEquals(10, patient.getId());
    }

    @Test
    public void constructor_withoutExplicitId_remainsUnassignedUntilAddedToClinicBook() {
        Patient patient = new Patient(
                new Name("Auto Id"),
                new Phone("90001234"),
                new Email("auto@example.com"),
                new Address("2 Street"),
                new NRIC("S1166846A"),
                LocalDate.of(2001, 2, 3),
                Sex.MALE);

        assertEquals(0, patient.getId());

        ClinicBook clinicBook = new ClinicBook();
        clinicBook.addPerson(patient);

        assertTrue(patient.getId() > 0);
    }

    @Test
    public void addDiagnosis_null_throwsNullPointerException() {
        Patient patient = new Patient(
                new Name("Alice Patient"),
                new Phone("91234567"),
                new Email("alice@example.com"),
                new Address("1 Street"),
                new NRIC("S1166846A"),
                LocalDate.of(2000, 1, 1),
                Sex.FEMALE,
                11);

        assertThrows(NullPointerException.class, () -> patient.addDiagnosis(null));
    }

    @Test
    public void removeDiagnosis_null_throwsNullPointerException() {
        Patient patient = new Patient(
                new Name("Alice Patient"),
                new Phone("91234567"),
                new Email("alice@example.com"),
                new Address("1 Street"),
                new NRIC("S1166846A"),
                LocalDate.of(2000, 1, 1),
                Sex.FEMALE,
                12);

        assertThrows(NullPointerException.class, () -> patient.removeDiagnosis(null));
    }

    @Test
    public void getDiagnoses_modifyReturnedList_throwsUnsupportedOperationException() {
        Patient patient = new Patient(
                new Name("Alice Patient"),
                new Phone("91234567"),
                new Email("alice@example.com"),
                new Address("1 Street"),
                new NRIC("S1166846A"),
                LocalDate.of(2000, 1, 1),
                Sex.FEMALE,
                13);

        assertThrows(UnsupportedOperationException.class, () -> patient.getDiagnoses().add(new Diagnosis("Flu", 2)));
    }
}
