package seedu.clinic.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Person;

/**
 * A UI component that displays information for a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on ClinicBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label role;
    @FXML
    private Label name;
    @FXML
    private Label rowNumber;
    @FXML
    private Label personIdLabel;
    @FXML
    private Label nric;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane allergies;
    @FXML
    private FlowPane sex;

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;

        rowNumber.setText("Index " + displayedIndex + ".");
        personIdLabel.setText("(ID: " + person.getId() + ")");
        role.setText(person.getRole());
        name.setText(person.getName().fullName);

<<<<<<< HEAD
        name.setWrapText(true);
        name.setMinWidth(0);
        keepFullyVisible(role);
        keepFullyVisible(personIdLabel);
        if (person instanceof Patient patient) {
            nric.setText("NRIC: " + patient.getNric().value);
=======
        allowWrapping(name);
        HBox.setHgrow(name, Priority.ALWAYS);
        keepFullyVisible(role);
        if (person instanceof Patient) {
            nric.setText("NRIC: " + ((Patient) person).getNric().value);
>>>>>>> 190629d9592f95fbb7d2d17141f9a1694bbbaa2d
            address.setText(person.getAddress().value);
            patient.getAllergies().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> allergies.getChildren().add(new Label(tag.tagName)));
            sex.getChildren().add(new Label(patient.getSex().getDisplayName()));
        } else {
            nric.setManaged(false);
            nric.setVisible(false);
            address.setManaged(false);
            address.setVisible(false);
            sex.setManaged(false);
            sex.setVisible(false);
        }
        phone.setText(person.getPhone().value);
        email.setText(person.getEmail().value);
        role.setText(person.getRole());
        allowWrapping(address);
        allowWrapping(email);
<<<<<<< HEAD
=======

        if (person instanceof Patient patient) {
            patient.getAllergies().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        }
>>>>>>> 190629d9592f95fbb7d2d17141f9a1694bbbaa2d
    }

    private static void allowWrapping(Label label) {
        label.setWrapText(true);
        label.setMinWidth(0);
        label.setMaxWidth(Double.MAX_VALUE);
    }

    private static void keepFullyVisible(Label label) {
        label.setMinWidth(Region.USE_PREF_SIZE);
    }
}
