package cinemaCenter;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Controller for adding or updating a session.
 * If tickets have been sold for this session, it cannot be updated.
 */
public class SessionAddUpdateController {

    @FXML private Label lblTitle;
    @FXML private ComboBox<String> hallComboBox;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<LocalTime> startTimeComboBox;
    @FXML private ComboBox<String> durationComboBox;
    @FXML private Button btnSave;

    private boolean isUpdateMode = false;
    private int movieId;
    private SessionData existingSession;

    /**
     * Called by FXML loader. Initializes combobox data (hall, times, duration).
     */
    @FXML
    public void initialize() {
        hallComboBox.getItems().addAll("Hall_A", "Hall_B");
        startTimeComboBox.getItems().addAll(
                LocalTime.of(10, 0),
                LocalTime.of(13, 0),
                LocalTime.of(14, 0),
                LocalTime.of(16, 30),
                LocalTime.of(18, 0),
                LocalTime.of(19, 0),
                LocalTime.of(20, 0),
                LocalTime.of(21, 30)
        );
        durationComboBox.getItems().addAll("2 hours", "2.5 hours", "3 hours");
        durationComboBox.setValue("2 hours");
    }

    /**
     * Prepares data for either add mode (session=null) or update mode.
     * @param movieId The associated movie ID
     * @param session The existing session, or null if adding
     */
    public void setData(int movieId, SessionData session) {
        this.movieId = movieId;
        if (session == null) {
            // Add mode
            isUpdateMode = false;
            lblTitle.setText("Add New Session");
        } else {
            // Update mode
            isUpdateMode = true;
            existingSession = session;
            lblTitle.setText("Update Session (ID: " + session.getSessionID() + ")");

            hallComboBox.setValue(session.getHall());
            datePicker.setValue(session.getSessionDate());
            startTimeComboBox.setValue(session.getStartTime());
        }
    }

    /**
     * Handles the Save button click. Either adds or updates a session.
     */
    @FXML
    void onSaveButtonClick() {
        String hall = hallComboBox.getValue();
        LocalDate date = datePicker.getValue();
        LocalTime start = startTimeComboBox.getValue();
        String selectedDuration = durationComboBox.getValue();

        if (hall == null || date == null || start == null || selectedDuration == null) {
            showAlert("Warning", "Please fill all fields.", Alert.AlertType.WARNING);
            return;
        }

        // Calculate end time based on duration
        LocalTime end = calcEndTime(start, selectedDuration);

        if (!isUpdateMode) {
            // Add session
            boolean success = AdminDAO.addSession(movieId, date, hall, start, end);
            if (!success) {
                showAlert("Error", "Session overlap or DB error while adding.", Alert.AlertType.ERROR);
                return;
            }
            showAlert("Success", "Session added successfully!", Alert.AlertType.INFORMATION);
            closeWindow();
        } else {
            // Update session
            int sessionId = existingSession.getSessionID();

            // Check if there are sold tickets. If yes, do not allow update.
            boolean hasSold = AdminDAO.hasSoldTickets(sessionId);
            if (hasSold) {
                showAlert("Warning", "Cannot update this session because tickets have been sold.", Alert.AlertType.WARNING);
                return;
            }

            boolean success = AdminDAO.updateSessionAllFields(sessionId, date, hall, start, end);
            if (!success) {
                showAlert("Error", "Session overlap or DB error while updating.", Alert.AlertType.ERROR);
                return;
            }
            showAlert("Success", "Session updated successfully!", Alert.AlertType.INFORMATION);
            closeWindow();
        }
    }

    /**
     * Calculates end time from a chosen duration (e.g. "2 hours", "2.5 hours", "3 hours").
     */
    private LocalTime calcEndTime(LocalTime startTime, String duration) {
        switch (duration) {
            case "2.5 hours":
                return startTime.plusHours(2).plusMinutes(30);
            case "3 hours":
                return startTime.plusHours(3);
            default:
                // "2 hours"
                return startTime.plusHours(2);
        }
    }

    /**
     * Closes this popup window.
     */
    private void closeWindow() {
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays an Alert dialog.
     */
    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
