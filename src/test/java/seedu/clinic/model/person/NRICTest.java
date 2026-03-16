package seedu.clinic.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.clinic.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NRICTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new NRIC(null));
    }

    @Test
    public void constructor_invalidNric_throwsIllegalArgumentException() {
        String invalidNric = "";
        assertThrows(IllegalArgumentException.class, () -> new NRIC(invalidNric));
    }

    @Test
    public void isValidNric() {
        assertThrows(NullPointerException.class, () -> NRIC.isValidNric(null));

        assertFalse(NRIC.isValidNric(""));
        assertFalse(NRIC.isValidNric(" "));
        assertFalse(NRIC.isValidNric("F1234567D"));
        assertFalse(NRIC.isValidNric("S123456D"));
        assertFalse(NRIC.isValidNric("S1234A67D"));
        assertFalse(NRIC.isValidNric("S1234567A"));
        assertFalse(NRIC.isValidNric("s1234567d"));

        assertTrue(NRIC.isValidNric("S1234567D"));
        assertTrue(NRIC.isValidNric("T1234567J"));
    }

    @Test
    public void equals() {
        NRIC nric = new NRIC("S1234567D");

        assertTrue(nric.equals(new NRIC("S1234567D")));
        assertTrue(nric.equals(nric));
        assertFalse(nric.equals(null));
        assertFalse(nric.equals(5.0f));
        assertFalse(nric.equals(new NRIC("T1234567J")));
    }
}
