package seedu.clinic.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.clinic.storage.JsonAdaptedPatient.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.clinic.testutil.Assert.assertThrows;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.clinic.commons.exceptions.IllegalValueException;
import seedu.clinic.model.person.Diagnosis;
import seedu.clinic.model.person.NRIC;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Person;
import seedu.clinic.model.person.Sex;
import seedu.clinic.model.tag.Tag;
import seedu.clinic.testutil.PersonBuilder;

public class JsonAdaptedPatientTest {
    private static final int VALID_ID = 1;
    private static final String VALID_NAME = "Alice Patient";
    private static final String VALID_PHONE = "94351253";
    private static final String VALID_EMAIL = "alice@example.com";
    private static final String VALID_ADDRESS = "123, Jurong West Ave 6, #08-111";
    private static final List<JsonAdaptedTag> VALID_ALLERGIES = List.of(new JsonAdaptedTag("shellfish"));
    private static final String VALID_NRIC = "S1166846A";
    private static final String VALID_DATE_OF_BIRTH = "2000-01-02";
    private static final String VALID_SEX = Sex.FEMALE.name();

    @Test
    public void toModelType_validPatientDetails_returnsPatient() throws Exception {
        Diagnosis diagnosis = new Diagnosis("Flu", LocalDate.of(2024, 1, 2), 3);
        diagnosis.addSymptom("cough");

        JsonAdaptedPatient patient = new JsonAdaptedPatient(
                VALID_ID,
                VALID_NAME,
                VALID_PHONE,
                VALID_EMAIL,
                VALID_ADDRESS,
                VALID_ALLERGIES,
                VALID_NRIC,
                VALID_DATE_OF_BIRTH,
                VALID_SEX,
                List.of(new JsonAdaptedDiagnosis(diagnosis)));

        Patient modelPatient = patient.toModelType();
        assertEquals(VALID_ID, modelPatient.getId());
        assertEquals(VALID_NAME, modelPatient.getName().fullName);
        assertEquals(VALID_PHONE, modelPatient.getPhone().value);
        assertEquals(VALID_EMAIL, modelPatient.getEmail().value);
        assertEquals(VALID_ADDRESS, modelPatient.getAddress().value);
        assertEquals(VALID_NRIC, modelPatient.getNric().value);
        assertEquals(LocalDate.parse(VALID_DATE_OF_BIRTH), modelPatient.getDateOfBirth());
        assertEquals(Sex.FEMALE, modelPatient.getSex());
        assertEquals(1, modelPatient.getDiagnoses().size());
        assertEquals("Flu", modelPatient.getDiagnoses().get(0).getDescription());
        assertEquals(1, modelPatient.getDiagnoses().get(0).getSymptoms().size());
        assertEquals("cough", modelPatient.getDiagnoses().get(0).getSymptoms().get(0));
    }

    @Test
    public void toModelType_invalidNric_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(
                VALID_ID,
                VALID_NAME,
                VALID_PHONE,
                VALID_EMAIL,
                VALID_ADDRESS,
                VALID_ALLERGIES,
                "INVALID",
                VALID_DATE_OF_BIRTH,
                VALID_SEX,
                List.of());
        assertThrows(IllegalValueException.class, NRIC.MESSAGE_CONSTRAINTS, patient::toModelType);
    }

    @Test
    public void toModelType_nullNric_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(
                VALID_ID,
                VALID_NAME,
                VALID_PHONE,
                VALID_EMAIL,
                VALID_ADDRESS,
                VALID_ALLERGIES,
                null,
                VALID_DATE_OF_BIRTH,
                VALID_SEX,
                List.of());
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "NRIC");
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    @Test
    public void toModelType_blankSex_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(
                VALID_ID,
                VALID_NAME,
                VALID_PHONE,
                VALID_EMAIL,
                VALID_ADDRESS,
                VALID_ALLERGIES,
                VALID_NRIC,
                VALID_DATE_OF_BIRTH,
                "  ",
                List.of());
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "sex");
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    @Test
    public void toModelType_nullSex_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(
                VALID_ID,
                VALID_NAME,
                VALID_PHONE,
                VALID_EMAIL,
                VALID_ADDRESS,
                VALID_ALLERGIES,
                VALID_NRIC,
                VALID_DATE_OF_BIRTH,
                null,
                List.of());
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "sex");
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    @Test
    public void toModelType_invalidSex_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(
                VALID_ID,
                VALID_NAME,
                VALID_PHONE,
                VALID_EMAIL,
                VALID_ADDRESS,
                VALID_ALLERGIES,
                VALID_NRIC,
                VALID_DATE_OF_BIRTH,
                "unknown",
                List.of());
        assertThrows(IllegalValueException.class, "Patient's sex is invalid!", patient::toModelType);
    }

    @Test
    public void toModelType_lowercaseSex_success() throws Exception {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(
                VALID_ID,
                VALID_NAME,
                VALID_PHONE,
                VALID_EMAIL,
                VALID_ADDRESS,
                VALID_ALLERGIES,
                VALID_NRIC,
                VALID_DATE_OF_BIRTH,
                "female",
                List.of());
        Patient modelPatient = patient.toModelType();
        assertEquals(Sex.FEMALE, modelPatient.getSex());
    }

    @Test
    public void toModelType_nullDateOfBirth_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(
                VALID_ID,
                VALID_NAME,
                VALID_PHONE,
                VALID_EMAIL,
                VALID_ADDRESS,
                VALID_ALLERGIES,
                VALID_NRIC,
                null,
                VALID_SEX,
                List.of());
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "dateOfBirth");
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    @Test
    public void toModelType_invalidDateOfBirth_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(
                VALID_ID,
                VALID_NAME,
                VALID_PHONE,
                VALID_EMAIL,
                VALID_ADDRESS,
                VALID_ALLERGIES,
                VALID_NRIC,
                "2000-13-40",
                VALID_SEX,
                List.of());
        assertThrows(IllegalValueException.class, "Patient's dateOfBirth is not a valid date!", patient::toModelType);
    }

    @Test
    public void toModelType_nullDiagnoses_successWithEmptyDiagnoses() throws Exception {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(
                VALID_ID,
                VALID_NAME,
                VALID_PHONE,
                VALID_EMAIL,
                VALID_ADDRESS,
                VALID_ALLERGIES,
                VALID_NRIC,
                VALID_DATE_OF_BIRTH,
                VALID_SEX,
                null);
        Patient modelPatient = patient.toModelType();
        assertTrue(modelPatient.getDiagnoses().isEmpty());
    }

    @Test
    public void toModelType_sexWithSpacesAndMixedCase_success() throws Exception {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(
                VALID_ID,
                VALID_NAME,
                VALID_PHONE,
                VALID_EMAIL,
                VALID_ADDRESS,
                VALID_ALLERGIES,
                VALID_NRIC,
                VALID_DATE_OF_BIRTH,
                "  inTerSex  ",
                List.of());
        Patient modelPatient = patient.toModelType();
        assertEquals(Sex.INTERSEX, modelPatient.getSex());
    }

    @Test
    public void toModelType_invalidDiagnosis_throwsIllegalValueException() {
        JsonAdaptedDiagnosis invalidDiagnosis = new JsonAdaptedDiagnosis(
                null,
                "2024-01-02",
                3,
                List.of(),
                List.of());
        JsonAdaptedPatient patient = new JsonAdaptedPatient(
                VALID_ID,
                VALID_NAME,
                VALID_PHONE,
                VALID_EMAIL,
                VALID_ADDRESS,
                VALID_ALLERGIES,
                VALID_NRIC,
                VALID_DATE_OF_BIRTH,
                VALID_SEX,
                List.of(invalidDiagnosis));
        String expectedMessage = String.format(JsonAdaptedDiagnosis.MISSING_FIELD_MESSAGE_FORMAT, "description");
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    @Test
    public void toModelType_nullAllergies_successWithEmptyTags() throws Exception {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(
                VALID_ID,
                VALID_NAME,
                VALID_PHONE,
                VALID_EMAIL,
                VALID_ADDRESS,
                null,
                VALID_NRIC,
                VALID_DATE_OF_BIRTH,
                VALID_SEX,
                List.of());
        Patient modelPatient = patient.toModelType();
        assertTrue(modelPatient.getAllergies().isEmpty());
    }

    @Test
    public void toModelType_fromPatientSource_success() throws Exception {
        Person person = new PersonBuilder()
                .withId(9)
                .withName(VALID_NAME)
                .withPhone(VALID_PHONE)
                .withEmail(VALID_EMAIL)
                .withAddress(VALID_ADDRESS)
                .build();
        Patient source = new Patient(person,
                Set.of(new Tag("shellfish")),
                new NRIC(VALID_NRIC),
                LocalDate.parse(VALID_DATE_OF_BIRTH),
                Sex.INTERSEX);
        source.addDiagnosis(new Diagnosis("Cold", LocalDate.of(2024, 2, 2), 4));

        JsonAdaptedPatient adapted = new JsonAdaptedPatient(source);
        Patient modelPatient = adapted.toModelType();

        assertEquals(source.getId(), modelPatient.getId());
        assertEquals(source.getName(), modelPatient.getName());
        assertEquals(source.getNric(), modelPatient.getNric());
        assertEquals(source.getDateOfBirth(), modelPatient.getDateOfBirth());
        assertEquals(source.getSex(), modelPatient.getSex());
        assertEquals(1, modelPatient.getDiagnoses().size());
        assertEquals("Cold", modelPatient.getDiagnoses().get(0).getDescription());
    }

    @Test
    public void toModelType_nullDiagnosisEntry_throwsNullPointerException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(
                VALID_ID,
                VALID_NAME,
                VALID_PHONE,
                VALID_EMAIL,
                VALID_ADDRESS,
                VALID_ALLERGIES,
                VALID_NRIC,
                VALID_DATE_OF_BIRTH,
                VALID_SEX,
                                Arrays.asList((JsonAdaptedDiagnosis) null));
        assertThrows(NullPointerException.class, patient::toModelType);
    }
}
