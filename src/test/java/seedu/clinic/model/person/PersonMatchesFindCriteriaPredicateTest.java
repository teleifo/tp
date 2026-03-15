package seedu.clinic.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.clinic.testutil.PersonBuilder;

public class PersonMatchesFindCriteriaPredicateTest {

    @Test
    public void equals() {
        PersonMatchesFindCriteriaPredicate firstPredicate = new PersonMatchesFindCriteriaPredicate(
                Collections.singletonList("first"), Optional.empty());
        PersonMatchesFindCriteriaPredicate secondPredicate = new PersonMatchesFindCriteriaPredicate(
                Arrays.asList("first", "second"), Optional.of(new Phone("12345678")));
        PersonMatchesFindCriteriaPredicate sameNameDifferentPhone = new PersonMatchesFindCriteriaPredicate(
                Collections.singletonList("first"), Optional.of(new Phone("12345678")));
        PersonMatchesFindCriteriaPredicate differentNameSamePhone = new PersonMatchesFindCriteriaPredicate(
                Collections.singletonList("second"), Optional.empty());

        assertTrue(firstPredicate.equals(firstPredicate));

        PersonMatchesFindCriteriaPredicate firstPredicateCopy = new PersonMatchesFindCriteriaPredicate(
                Collections.singletonList("first"), Optional.empty());
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(sameNameDifferentPhone));
        assertFalse(firstPredicate.equals(differentNameSamePhone));
    }

    @Test
    public void test_matchesNameKeywords_returnsTrue() {
        PersonMatchesFindCriteriaPredicate predicate = new PersonMatchesFindCriteriaPredicate(
                Collections.singletonList("Alice"), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        predicate = new PersonMatchesFindCriteriaPredicate(Arrays.asList("Alice", "Bob"), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        predicate = new PersonMatchesFindCriteriaPredicate(Arrays.asList("Bob", "Carol"), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Carol").build()));

        predicate = new PersonMatchesFindCriteriaPredicate(Arrays.asList("aLIce", "bOB"), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_matchesPhone_returnsTrue() {
        PersonMatchesFindCriteriaPredicate predicate = new PersonMatchesFindCriteriaPredicate(
                Collections.emptyList(), Optional.of(new Phone("12345678")));
        assertTrue(predicate.test(new PersonBuilder().withPhone("12345678").build()));
    }

    @Test
    public void test_matchesNameOrPhone_returnsTrue() {
        PersonMatchesFindCriteriaPredicate predicate = new PersonMatchesFindCriteriaPredicate(
                Collections.singletonList("Alice"), Optional.of(new Phone("12345678")));
        assertTrue(predicate.test(new PersonBuilder().withName("Carol Danvers").withPhone("12345678").build()));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withPhone("87654321").build()));
    }

    @Test
    public void test_doesNotMatchCriteria_returnsFalse() {
        PersonMatchesFindCriteriaPredicate predicate = new PersonMatchesFindCriteriaPredicate(
                Collections.emptyList(), Optional.empty());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        predicate = new PersonMatchesFindCriteriaPredicate(Collections.singletonList("Carol"), Optional.empty());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        predicate = new PersonMatchesFindCriteriaPredicate(Collections.emptyList(), Optional.of(new Phone("12345678")));
        assertFalse(predicate.test(new PersonBuilder().withPhone("87654321").build()));

        predicate = new PersonMatchesFindCriteriaPredicate(Arrays.asList("Carol"), Optional.of(new Phone("12345678")));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").withPhone("87654321").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        PersonMatchesFindCriteriaPredicate predicate = new PersonMatchesFindCriteriaPredicate(
                keywords, Optional.of(new Phone("12345678")));

        String expected = PersonMatchesFindCriteriaPredicate.class.getCanonicalName()
                + "{nameKeywords=" + keywords + ", phone=Optional[12345678]}";
        assertEquals(expected, predicate.toString());
    }
}
