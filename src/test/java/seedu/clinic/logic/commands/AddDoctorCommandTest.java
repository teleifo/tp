package seedu.clinic.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.clinic.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.clinic.logic.commands.exceptions.CommandException;
import seedu.clinic.model.Model;
import seedu.clinic.model.ModelManager;
import seedu.clinic.model.person.Doctor;
import seedu.clinic.testutil.DoctorBuilder;

public class AddDoctorCommandTest {

    @Test
    public void execute_partialContactDuplicate_returnsWarning() throws Exception {
        Model model = new ModelManager();
        Doctor existingDoctor = new DoctorBuilder().build();
        model.addPerson(existingDoctor);
        Doctor doctorToAdd = new DoctorBuilder()
                .withName("Dr Bob Tan")
                .withEmail("bob@example.com")
                .build();

        CommandResult commandResult = new AddDoctorCommand(doctorToAdd).execute(model);

        assertEquals(String.format(AddPersonWithDuplicateWarningCommand.MESSAGE_DUPLICATE_WARNING,
                "doctor", "phone number"),
                commandResult.getFeedbackToUser());
        assertTrue(commandResult.isRequireConfirmation());
        assertEquals(1, model.getFilteredPersonList().size());
        assertEquals(existingDoctor, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_exactContactDuplicate_throwsCommandException() {
        Model model = new ModelManager();
        Doctor existingDoctor = new DoctorBuilder().build();
        model.addPerson(existingDoctor);
        Doctor doctorToAdd = new DoctorBuilder(existingDoctor).build();

        assertThrows(CommandException.class,
                String.format(AddPersonWithDuplicateWarningCommand.MESSAGE_DUPLICATE_REJECT, "doctor", "doctor"), ()
                        -> new AddDoctorCommand(doctorToAdd).execute(model));
        assertEquals(1, model.getFilteredPersonList().size());
        assertEquals(existingDoctor, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_exactContactDuplicate_filtersOnlyExactMatches() {
        Model model = new ModelManager();
        Doctor exactMatch = new DoctorBuilder().build();
        Doctor partialMatch = new DoctorBuilder()
                .withName("Dr Bob Tan")
                .withPhone(exactMatch.getPhone().value)
                .withEmail("partial@example.com")
                .build();
        model.addPerson(exactMatch);
        model.addPerson(partialMatch);
        Doctor doctorToAdd = new DoctorBuilder(exactMatch).build();

        assertThrows(CommandException.class,
                String.format(AddPersonWithDuplicateWarningCommand.MESSAGE_DUPLICATE_REJECT, "doctor", "doctor"), ()
                        -> new AddDoctorCommand(doctorToAdd).execute(model));
        assertEquals(1, model.getFilteredPersonList().size());
        assertEquals(exactMatch, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_exactContactDuplicateWithDifferentNameCase_throwsCommandException() {
        Model model = new ModelManager();
        Doctor existingDoctor = new DoctorBuilder().build();
        model.addPerson(existingDoctor);
        Doctor doctorToAdd = new DoctorBuilder(existingDoctor)
                .withName(existingDoctor.getName().fullName.toLowerCase())
                .build();

        assertThrows(CommandException.class,
                String.format(AddPersonWithDuplicateWarningCommand.MESSAGE_DUPLICATE_REJECT, "doctor", "doctor"), ()
                        -> new AddDoctorCommand(doctorToAdd).execute(model));
        assertEquals(1, model.getFilteredPersonList().size());
        assertEquals(existingDoctor, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_exactContactDuplicateWithDifferentEmailCase_throwsCommandException() {
        Model model = new ModelManager();
        Doctor existingDoctor = new DoctorBuilder().build();
        model.addPerson(existingDoctor);
        Doctor doctorToAdd = new DoctorBuilder(existingDoctor)
                .withEmail(existingDoctor.getEmail().value.toUpperCase())
                .build();

        assertThrows(CommandException.class,
                String.format(AddPersonWithDuplicateWarningCommand.MESSAGE_DUPLICATE_REJECT, "doctor", "doctor"), ()
                        -> new AddDoctorCommand(doctorToAdd).execute(model));
        assertEquals(1, model.getFilteredPersonList().size());
        assertEquals(existingDoctor, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_confirmedAfterWarning_addSuccessful() throws Exception {
        Model model = new ModelManager();
        model.addPerson(new DoctorBuilder().build());
        Doctor doctorToAdd = new DoctorBuilder()
                .withName("Dr Bob Tan")
                .withEmail("bob@example.com")
                .build();
        AddDoctorCommand addDoctorCommand = new AddDoctorCommand(doctorToAdd);

        addDoctorCommand.execute(model);
        addDoctorCommand.confirm();
        CommandResult commandResult = addDoctorCommand.execute(model);

        assertEquals(String.format(AddDoctorCommand.MESSAGE_SUCCESS, doctorToAdd), commandResult.getFeedbackToUser());
        assertEquals(2, model.getClinicBook().getPersonList().size());
    }
}
