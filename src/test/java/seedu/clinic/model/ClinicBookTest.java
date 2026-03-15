package seedu.clinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.clinic.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.clinic.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.clinic.testutil.Assert.assertThrows;
import static seedu.clinic.testutil.TypicalPersons.ALICE;
import static seedu.clinic.testutil.TypicalPersons.getTypicalClinicBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.clinic.model.person.Person;
import seedu.clinic.model.person.exceptions.DuplicatePersonException;
import seedu.clinic.testutil.PersonBuilder;

public class ClinicBookTest {

    private final ClinicBook clinicBook = new ClinicBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), clinicBook.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> clinicBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyClinicBook_replacesData() {
        ClinicBook newData = getTypicalClinicBook();
        clinicBook.resetData(newData);
        assertEquals(newData, clinicBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        ClinicBookStub newData = new ClinicBookStub(newPersons);

        assertThrows(DuplicatePersonException.class, () -> clinicBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> clinicBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInClinicBook_returnsFalse() {
        assertFalse(clinicBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInClinicBook_returnsTrue() {
        clinicBook.addPerson(ALICE);
        assertTrue(clinicBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInClinicBook_returnsTrue() {
        clinicBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(clinicBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> clinicBook.getPersonList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = ClinicBook.class.getCanonicalName() + "{persons=" + clinicBook.getPersonList() + "}";
        assertEquals(expected, clinicBook.toString());
    }

    /**
     * A stub ReadOnlyClinicBook whose persons list can violate interface constraints.
     */
    private static class ClinicBookStub implements ReadOnlyClinicBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();

        ClinicBookStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }
    }

}
