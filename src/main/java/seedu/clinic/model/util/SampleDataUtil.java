package seedu.clinic.model.util;

import java.time.LocalDate;
import java.util.Arrays;
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
import seedu.clinic.model.tag.Tag;

/**
 * Contains utility methods for populating {@code ClinicBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Patient(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"), getTagSet("friends"),
                new NRIC("S1166846A"), LocalDate.parse("2002-12-25"), "98765432"),
            new Doctor(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com")),
            new Patient(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), getTagSet("neighbours"),
                new NRIC("S1786425D"), LocalDate.parse("2002-04-01"), "97654321"),
            new Pharmacist(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com")),
            new Pharmacist(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com")),
            new Doctor(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"))
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
