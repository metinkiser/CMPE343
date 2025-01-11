package CinemaCenter;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.sql.*;
import java.util.*;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

public class SeatSelectionController {
    @FXML private Label movieInfoLabel;
    @FXML private Label sessionInfoLabel;
    @FXML private GridPane seatGrid;
    @FXML private ListView<String> cartListView;
    @FXML private Label totalPriceLabel;
    
    private Session selectedSession;
    private Map<String, Button> seatButtons = new HashMap<>();
    private Set<String> selectedSeats = new HashSet<>();
    private double basePrice;
    private ObservableList<String> cartItems = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        cartListView.setItems(cartItems);
        updateTotalPrice();
    }
    
    public void setSelectedSession(Session session) {
        this.selectedSession = session;
        loadSessionInfo();
        loadBasePrice();
        createSeatLayout();
        loadOccupiedSeats();
    }
    
    private void loadSessionInfo() {
        Movie movie = selectedSession.getMovie();
        movieInfoLabel.setText(movie.getTitle());
        sessionInfoLabel.setText(String.format("Hall: %s | Date: %s | Time: %s",
            selectedSession.getHall(),
            selectedSession.getDate(),
            selectedSession.getStartTime()));
    }
    
    private void loadBasePrice() {
        String query = "SELECT BasePrice FROM TicketPrices WHERE MovieID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, selectedSession.getMovie().getMovieID());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                basePrice = rs.getDouble("BasePrice");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void createSeatLayout() {
        String query = "SELECT * FROM Seats WHERE HallID = ? ORDER BY SeatRow, SeatColumn";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, selectedSession.getHall());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String seatNumber = rs.getString("SeatNumber");
                int row = rs.getString("SeatRow").charAt(0) - 'A';
                int col = rs.getInt("SeatColumn") - 1;
                
                Button seatButton = new Button(seatNumber);
                seatButton.setStyle("-fx-background-color: lightgreen;");
                seatButton.setPrefSize(50, 50);
                
                seatButton.setOnAction(e -> onSeatClick(seatNumber, seatButton));
                seatButtons.put(seatNumber, seatButton);
                seatGrid.add(seatButton, col, row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadOccupiedSeats() {
        String query = "SELECT s.SeatNumber FROM Tickets t " +
                      "JOIN Seats s ON t.SeatID = s.SeatID " +
                      "WHERE t.SessionID = ?";
                      
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, selectedSession.getSessionID());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String seatNumber = rs.getString("SeatNumber");
                Button button = seatButtons.get(seatNumber);
                if (button != null) {
                    button.setStyle("-fx-background-color: red;");
                    button.setDisable(true);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void onSeatClick(String seatNumber, Button seatButton) {
        if (selectedSeats.contains(seatNumber)) {
            selectedSeats.remove(seatNumber);
            seatButton.setStyle("-fx-background-color: lightgreen;");
            cartItems.remove("Seat " + seatNumber + " - " + String.format("%.2f TL", basePrice));
        } else {
            selectedSeats.add(seatNumber);
            seatButton.setStyle("-fx-background-color: yellow;");
            cartItems.add("Seat " + seatNumber + " - " + String.format("%.2f TL", basePrice));
        }
        updateTotalPrice();
    }
    
    private void updateTotalPrice() {
        double total = selectedSeats.size() * basePrice;
        totalPriceLabel.setText(String.format("%.2f TL", total));
    }
    
    @FXML
    private void onBackButtonClick() {
        try {
            // Mevcut pencereyi kapat
            Stage currentStage = (Stage) movieInfoLabel.getScene().getWindow();
            currentStage.close();
            
            // Yeni pencereyi aç ve film bilgisini aktar
            NavigationUtil.loadScene(
                "SelectSession.fxml",
                "Session Selection",
                selectedSession.getMovie()
            );
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not return to session selection screen.");
        }
    }
    
    @FXML
    private void onNextButtonClick() {
        if (selectedSeats.isEmpty()) {
            showAlert("Warning", "Please select at least one seat.");
            return;
        }
        
        try {
            // NavigationUtil kullanarak geçiş yapalım
            CustomerDetailsController controller = NavigationUtil.loadScene(
                "CustomerDetails.fxml",
                "Customer Details & Products",
                new Object[]{selectedSession, selectedSeats, basePrice}
            );
            
            if (controller == null) {
                throw new IOException("Could not load CustomerDetailsController");
            }
            
            // Mevcut pencereyi kapatalım
            ((Stage) movieInfoLabel.getScene().getWindow()).close();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error details: " + e.getMessage());
            showAlert("Error", "Could not proceed to customer details screen.");
        }
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}