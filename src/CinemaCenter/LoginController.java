package CinemaCenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Alanların boş olup olmadığını kontrol et
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Boş Alan", "Lütfen kullanıcı adı ve şifre giriniz.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Users WHERE BINARY Username = ? AND BINARY Password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                showAlert(Alert.AlertType.INFORMATION, "Giriş Başarılı", "Hoş geldiniz, " + rs.getString("Role") + "!");
                loadSearchScene(); // Başarılı girişte yeni sahneye geç
            } else {
                showAlert(Alert.AlertType.ERROR, "Giriş Başarısız", "Hatalı kullanıcı adı veya şifre.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Hata", "Veritabanı bağlantı hatası: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadSearchScene() {
        try {
            // Yeni sahneyi yükle
            Parent root = FXMLLoader.load(getClass().getResource("search.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow(); // Mevcut pencereyi al
            stage.setScene(new Scene(root, 800, 600)); // Yeni sahne ayarla
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
