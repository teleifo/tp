package seedu.clinic.model.util;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.clinic.model.ClinicBook;
import seedu.clinic.model.ReadOnlyClinicBook;
import seedu.clinic.model.person.Address;
import seedu.clinic.model.person.Doctor;
import seedu.clinic.model.person.Email;
import seedu.clinic.model.person.NRIC;
import seedu.clinic.model.person.Name;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Person;
import seedu.clinic.model.person.Pharmacist;
import seedu.clinic.model.person.Phone;
import seedu.clinic.model.person.Sex;
import seedu.clinic.model.tag.Tag;

/**
 * Contains utility methods for populating {@code ClinicBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Patient(new Name("Alex Yeoh"), new Phone("91234567"), new Email("alexyeoh@gmail.com"),
                new Address("123 Clementi Ave 3, #04-12"), Collections.emptySet(),
                new NRIC("S1234567D"), LocalDate.parse("1990-01-01"), Sex.MALE),
            new Doctor(new Name("Tan Wei Ming"), new Phone("87654321"), new Email("drtan@gmail.com")),
            new Patient(new Name("Jane Lim"), new Phone("92345678"), new Email("jane@gmail.com"),
                new Address("45 Bedok North St 1, #02-34"), Collections.emptySet(),
                new NRIC("S1786425D"), LocalDate.parse("1993-07-08"), Sex.FEMALE),
            new Pharmacist(new Name("Lee Mei"), new Phone("98765432"), new Email("leemei@gmail.com"))
        };
    }

    public static ReadOnlyClinicBook getSampleClinicBook() {
        ClinicBook sampleAb = new ClinicBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
