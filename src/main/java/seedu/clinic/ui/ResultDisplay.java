package seedu.clinic.ui;

import static java.util.Objects.requireNonNull;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart<Region> {
    private static final double MIN_HEIGHT = 100;
    private static final double MAX_HEIGHT = 240;
    private static final double LINE_HEIGHT = 24;
    private static final double EXTRA_PADDING = 24;
    private static final int APPROX_CHARS_PER_LINE = 58;

    private static final String FXML = "ResultDisplay.fxml";

    @FXML
    private TextArea resultDisplay;

    /**
     * Constructs a ResultDisplay that loads the FXML layout and enables text wrapping.
     */
    public ResultDisplay() {
        super(FXML);
        resultDisplay.setWrapText(true);
        resultDisplay.setMinHeight(MIN_HEIGHT);
        resultDisplay.setPrefHeight(MIN_HEIGHT);
        resultDisplay.setMaxHeight(MIN_HEIGHT);
        resultDisplay.setPrefRowCount(3);

        StackPane root = (StackPane) getRoot();
        root.setMinHeight(MIN_HEIGHT);
        root.setPrefHeight(MIN_HEIGHT);
        root.setMaxHeight(MIN_HEIGHT);
    }

    public void setFeedbackToUser(String feedbackToUser) {
        requireNonNull(feedbackToUser);
        resultDisplay.setText(feedbackToUser);
        updateHeight(feedbackToUser);
    }

    private void updateHeight(String feedbackToUser) {
        int lineCount = feedbackToUser.lines()
                .mapToInt(this::estimateWrappedLines)
                .sum();
        double targetHeight = Math.max(MIN_HEIGHT,
                Math.min(MAX_HEIGHT, lineCount * LINE_HEIGHT + EXTRA_PADDING));

        resultDisplay.setPrefHeight(targetHeight);
        resultDisplay.setMaxHeight(targetHeight);

        StackPane root = (StackPane) getRoot();
        root.setMinHeight(MIN_HEIGHT);
        root.setPrefHeight(targetHeight);
        root.setMaxHeight(targetHeight);
    }

    private int estimateWrappedLines(String line) {
        return Math.max(1, (int) Math.ceil((double) line.length() / APPROX_CHARS_PER_LINE));
    }

}
