package seedu.clinic.testutil;

import seedu.clinic.model.ClinicBook;
import seedu.clinic.model.person.Person;

/**
 * A utility class to help with building ClinicBook objects.
 * Example usage: <br>
 *     {@code ClinicBook ab = new ClinicBookBuilder().withPerson("John", "Doe").build();}
 */
public class ClinicBookBuilder {

    private ClinicBook clinicBook;

    public ClinicBookBuilder() {
        clinicBook = new ClinicBook();
    }

    public ClinicBookBuilder(ClinicBook clinicBook) {
        this.clinicBook = clinicBook;
    }

    /**
     * Adds a new {@code Person} to the {@code ClinicBook} that we are building.
     */
    public ClinicBookBuilder withPerson(Person person) {
        clinicBook.addPerson(person);
        return this;
    }

    public ClinicBook build() {
        return clinicBook;
    }
}
