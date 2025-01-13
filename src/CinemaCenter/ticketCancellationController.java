package cinemaCenter;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class ticketCancellationController {

    @FXML
    private TextField ticketIdField;

    @FXML
    void processTicketCancellation(ActionEvent event) {
        String ticketIdStr = ticketIdField.getText().trim();
        if (ticketIdStr.isEmpty()) {
            showAlert("Warning", "Please enter a valid ticket ID.", Alert.AlertType.WARNING);
            return;
        }

        try {
            int ticketId = Integer.parseInt(ticketIdStr);
            boolean success = AdminDAO.cancelTicket(ticketId);
            if (success) {
                showAlert("Success", "Ticket cancelled successfully!", Alert.AlertType.INFORMATION);
                closeWindow();
            } else {
                showAlert("Error", "Error or ticket not found during cancellation.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid ticket ID format!", Alert.AlertType.ERROR);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) ticketIdField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
