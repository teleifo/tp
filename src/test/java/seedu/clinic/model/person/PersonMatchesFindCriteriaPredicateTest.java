package seedu.clinic.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.clinic.testutil.Assert.assertThrows;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.clinic.model.tag.Tag;
import seedu.clinic.testutil.PersonBuilder;

public class PersonMatchesFindCriteriaPredicateTest {

    @Test
    public void equals() {
        PersonMatchesFindCriteriaPredicate firstPredicate = new PersonMatchesFindCriteriaPredicate(
                Collections.singletonList("first"), Optional.empty(), Optional.empty());
        PersonMatchesFindCriteriaPredicate secondPredicate = new PersonMatchesFindCriteriaPredicate(
                Arrays.asList("first", "second"), Optional.of(new Phone("12345678")),
                Optional.of(new NRIC("S1234567D")));
        PersonMatchesFindCriteriaPredicate sameNameDifferentPhone = new PersonMatchesFindCriteriaPredicate(
                Collections.singletonList("first"), Optional.of(new Phone("12345678")), Optional.empty());
        PersonMatchesFindCriteriaPredicate differentNameSamePhone = new PersonMatchesFindCriteriaPredicate(
                Collections.singletonList("first"), Optional.empty(), Optional.of(new NRIC("S1234567D")));

        assertTrue(firstPredicate.equals(firstPredicate));

        PersonMatchesFindCriteriaPredicate firstPredicateCopy = new PersonMatchesFindCriteriaPredicate(
                Collections.singletonList("first"), Optional.empty(), Optional.empty());
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(sameNameDifferentPhone));
        assertFalse(firstPredicate.equals(differentNameSamePhone));
    }

    @Test
    public void constructor_emptyCriteria_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> new PersonMatchesFindCriteriaPredicate(
                Collections.emptyList(), Optional.empty(), Optional.empty()));
    }

    @Test
    public void test_nameCriteriaMatches_returnsTrue() {
        // EP: one keyword matches one full word in the name
        PersonMatchesFindCriteriaPredicate predicate = new PersonMatchesFindCriteriaPredicate(
                Collections.singletonList("Alice"), Optional.empty(), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // EP: multiple keywords all match
        predicate = new PersonMatchesFindCriteriaPredicate(Arrays.asList("Alice", "Bob"),
                Optional.empty(), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // EP: any one keyword may match
        predicate = new PersonMatchesFindCriteriaPredicate(Arrays.asList("Bob", "Carol"),
                Optional.empty(), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Carol").build()));

        // EP: matching is case-insensitive
        predicate = new PersonMatchesFindCriteriaPredicate(Arrays.asList("aLIce", "bOB"),
                Optional.empty(), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    /*
     * Valid equivalence partitions for exact-match criteria:
     *   - phone criterion matches exactly
     *   - NRIC criterion matches exactly on a patient entry
     */

    @Test
    public void test_phoneCriteriaMatches_returnsTrue() {
        PersonMatchesFindCriteriaPredicate predicate = new PersonMatchesFindCriteriaPredicate(
                Collections.emptyList(), Optional.of(new Phone("12345678")), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withPhone("12345678").build()));
    }

    @Test
    public void test_nricCriteriaMatchesPatient_returnsTrue() {
        Patient patient = new Patient(new PersonBuilder().withName("Alice Bob").withPhone("12345678").build(),
                Set.of(new Tag("shellfish")), new NRIC("S1234567D"), LocalDate.of(1992, 4, 12), Sex.FEMALE);
        PersonMatchesFindCriteriaPredicate predicate = new PersonMatchesFindCriteriaPredicate(
                Collections.emptyList(), Optional.empty(), Optional.of(new NRIC("S1234567D")));
        assertTrue(predicate.test(patient));
    }

    /*
     * Invalid equivalence partitions:
     *   - no name keyword matches
     *   - phone criterion does not match exactly
     *   - NRIC criterion is applied to a non-patient
     *   - NRIC criterion does not match the patient's NRIC
     *
     * The tests below cover one invalid predicate partition at a time.
     */

    @Test
    public void test_nameCriteriaDoesNotMatch_returnsFalse() {
        PersonMatchesFindCriteriaPredicate predicate = new PersonMatchesFindCriteriaPredicate(
                Collections.singletonList("Carol"),
                Optional.empty(), Optional.empty());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_phoneCriteriaDoesNotMatch_returnsFalse() {
        PersonMatchesFindCriteriaPredicate predicate = new PersonMatchesFindCriteriaPredicate(Collections.emptyList(),
                Optional.of(new Phone("12345678")), Optional.empty());
        assertFalse(predicate.test(new PersonBuilder().withPhone("87654321").build()));
    }

    @Test
    public void test_nricCriteriaOnNonPatient_doesNotMatch() {
        PersonMatchesFindCriteriaPredicate predicate = new PersonMatchesFindCriteriaPredicate(Collections.emptyList(),
                Optional.empty(), Optional.of(new NRIC("S1234567D")));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_nricCriteriaDoesNotMatchPatient_returnsFalse() {
        Patient patient = new Patient(new PersonBuilder().withName("Alice Bob").withPhone("12345678").build(),
                Collections.emptySet(), new NRIC("T1234567J"), LocalDate.of(1992, 4, 12), Sex.FEMALE);
        PersonMatchesFindCriteriaPredicate predicate = new PersonMatchesFindCriteriaPredicate(Collections.emptyList(),
                Optional.empty(), Optional.of(new NRIC("S1234567D")));
        assertFalse(predicate.test(patient));
    }
}
