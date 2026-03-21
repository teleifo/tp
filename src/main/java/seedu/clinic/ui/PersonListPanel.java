package seedu.clinic.ui;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.clinic.commons.core.LogsCenter;
import seedu.clinic.model.person.Doctor;
import seedu.clinic.model.person.Patient;
import seedu.clinic.model.person.Person;
import seedu.clinic.model.person.Pharmacist;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private ListView<Person> personListView;

    private ObservableList<Person> combinedList = FXCollections.observableArrayList();

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(
            ObservableList<Person> personList,
            ObservableList<Patient> patientList,
            ObservableList<Pharmacist> pharmacistList,
            ObservableList<Doctor> doctorList) {
        super(FXML);

        combinedList = createCombinedList(personList, patientList, pharmacistList, doctorList);
        personListView.setItems(combinedList);
        personListView.setCellFactory(listView -> new PersonListViewCell());
    }

    static ObservableList<Person> createCombinedList(ObservableList<Person> personList,
                                                     ObservableList<Patient> patientList,
                                                     ObservableList<Pharmacist> pharmacistList,
                                                     ObservableList<Doctor> doctorList) {

        requireNonNull(personList);
        requireNonNull(patientList);
        requireNonNull(doctorList);
        requireNonNull(pharmacistList);

        ObservableList<Person> combinedList = FXCollections.observableArrayList();
        Runnable refreshCombinedList = () -> {
            combinedList.setAll(personList);
            combinedList.addAll(patientList);
            combinedList.addAll(doctorList);
            combinedList.addAll(pharmacistList);
        };

        ListChangeListener<Person> personListListener = change -> refreshCombinedList.run();
        ListChangeListener<Patient> patientListListener = change -> refreshCombinedList.run();
        ListChangeListener<Doctor> doctorListListener = change -> refreshCombinedList.run();
        ListChangeListener<Pharmacist> pharmacistListListener = change -> refreshCombinedList.run();

        personList.addListener(personListListener);
        patientList.addListener(patientListListener);
        doctorList.addListener(doctorListListener);
        pharmacistList.addListener(pharmacistListListener);
        refreshCombinedList.run();
        return combinedList;
    }


    /**
    * Custom {@code ListCell} that displays the graphics of a {@code Person},
    * {@code Patient}, {@code Doctor}, or {@code Pharmacist}
     * using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(person, getIndex() + 1).getRoot());
            }
        }
    }

}
