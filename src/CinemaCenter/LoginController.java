package CinemaCenter;

import javafx.fxml.FXML;	
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void onLoginButtonClick() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // Büyük/küçük harf validasyonu
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Kullanıcı adı ve şifre boş bırakılamaz.");
            return;
        }

        if (!username.equals(username.toLowerCase())) {
            errorLabel.setText("Kullanıcı adı yalnızca küçük harf içermelidir.");
            return;
        }

        // Kullanıcı doğrulama
        String role = UserDAO.authenticate(username, password);

        if (role != null) {
            errorLabel.setText(""); // Hata mesajını temizle
            System.out.println("Giriş Başarılı! Rol: " + role);

            // Role göre sahne değişimi
            switch (role) {
                case "Cashier":
                    loadCashierOperationsScreen();
                    break;
                case "Manager":
                    loadManagerOperationsScreen(); // YENİ
                    break;
                    
                default:
                    errorLabel.setText("Bu role henüz destek sağlanmıyor.");
                    break;
            }
        } else {
            errorLabel.setText("Hatalı kullanıcı adı veya şifre.");
        }
    }

    private void loadCashierOperationsScreen() {
        try {
            // Load Cashier Operations screen
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/CinemaCenter/cashier_operations.fxml"));
            Scene scene = new Scene(loader.load());

            // Get current stage and set the new scene
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Cashier Operations");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Cashier Operations ekranı yüklenirken hata oluştu.");
        }
    }
    
    private void loadManagerOperationsScreen() {
        try {
            // Manager'ın userID'sini al
            int managerId = UserDAO.getUserId(usernameField.getText(), passwordField.getText());
            if (managerId == -1) {
                errorLabel.setText("Kullanıcı bilgileri alınamadı.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CinemaCenter/manager_operations.fxml"));
            Scene scene = new Scene(loader.load());
            
            // ManagerController'a userID'yi ilet
            ManagerController managerController = loader.getController();
            managerController.setManagerUserId(managerId);
            
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Manager Operations");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Manager Operations ekranı yüklenirken hata oluştu.");
        }
    }
    
    
}
