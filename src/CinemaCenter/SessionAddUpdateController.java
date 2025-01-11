package cinemaCenter;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalTime;

public class SessionAddUpdateController {

    @FXML private Label lblTitle;
    @FXML private ComboBox<String> hallComboBox;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<LocalTime> startTimeComboBox;
    @FXML private ComboBox<LocalTime> endTimeComboBox;

    private boolean isUpdateMode = false;
    private int movieId;           // Hangi filme session ekleyeceğimizi bilmek için
    private SessionData existingSession; // Update modunda dolacak

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
        endTimeComboBox.getItems().addAll(
                LocalTime.of(12, 0),
                LocalTime.of(14, 0),
                LocalTime.of(16, 0),
                LocalTime.of(18, 0),
                LocalTime.of(19, 30),
                LocalTime.of(20, 30),
                LocalTime.of(22, 0),
                LocalTime.of(23, 30)
        );
    }

    /**
     * Bu metot, AdminController'dan çağrılırken parametreleri ayarlar.
     * @param movieId  session'ların ait olduğu movieId
     * @param session  update modunda güncellenecek session, add modunda null
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

            // Alanları doldur
            hallComboBox.setValue(session.getHall());
            datePicker.setValue(session.getSessionDate());
            startTimeComboBox.setValue(session.getStartTime());
            endTimeComboBox.setValue(session.getEndTime());
        }
    }

    @FXML
    void onSaveButtonClick() {
        String hall = hallComboBox.getValue();
        LocalDate date = datePicker.getValue();
        LocalTime start = startTimeComboBox.getValue();
        LocalTime end = endTimeComboBox.getValue();

        if (hall == null || date == null || start == null || end == null) {
            showAlert("Warning", "Please fill all fields.", Alert.AlertType.WARNING);
            return;
        }

        if (!isUpdateMode) {
            // Add session
            boolean success = AdminDAO.addSession(movieId, date, hall, start, end);
            if (success) {
                showAlert("Success", "Session added successfully!", Alert.AlertType.INFORMATION);
                closeWindow();
            } else {
                showAlert("Error", "Error while adding session.", Alert.AlertType.ERROR);
            }
        } else {
            // Update session
            int sessionId = existingSession.getSessionID();

            // Bilet satılmış mı kontrolü AdminDAO'da var ama normalde orada yapılır
            boolean hasSold = AdminDAO.hasSoldTickets(sessionId);
            if (hasSold) {
                showAlert("Warning", "Cannot update session because tickets have been sold!", Alert.AlertType.WARNING);
                return;
            }

            boolean success = AdminDAO.updateSessionAllFields(sessionId, date, hall, start, end);
            if (success) {
                showAlert("Success", "Session updated successfully!", Alert.AlertType.INFORMATION);
                closeWindow();
            } else {
                showAlert("Error", "Error while updating session.", Alert.AlertType.ERROR);
            }
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) hallComboBox.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
