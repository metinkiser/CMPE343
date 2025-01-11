package cinemaCenter;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

public class adminController {

    // ==== Movie Toggle and Radio Buttons ====
    @FXML private ToggleGroup movieToggleGroup;  // FXML'de <fx:define> ile tanımlandı
    @FXML private RadioButton rbAddMovie;
    @FXML private RadioButton rbUpdateMovie;
    @FXML private VBox vboxAddMovie;
    @FXML private VBox vboxUpdateMovie;

    // Cancellation & Refund
    @FXML private Button cancelProcessButton;
    @FXML private ComboBox<String> cancelTypeComboBox;
    @FXML private HBox productCancelBox;
    @FXML private ComboBox<String> productComboBox; 
    @FXML private TextField productQtyField;
    @FXML private HBox ticketCancelBox;
    @FXML private TextField ticketIdField;

    // ==== Movie Add ====
    @FXML private TextField titleField;
    @FXML private ComboBox<String> genreComboBox;
    @FXML private TextArea summaryField;
    @FXML private TextField posterPathField;

    // ==== Movie Update ====
    @FXML private TextField updateMovieTitleField;
    @FXML private ComboBox<String> updateFieldComboBox;
    @FXML private TextField updateFieldValue;
    @FXML private ComboBox<String> genreUpdateComboBox;
    @FXML private Label newValueLabel;

    // ==== Session Management ====
    @FXML private TextField sessionMovieTitleField;
    @FXML private TableView<SessionData> sessionTable;
    @FXML private TableColumn<SessionData, Integer> sessionIdColumn;
    @FXML private TableColumn<SessionData, String> hallColumn;
    @FXML private TableColumn<SessionData, LocalDate> dateColumn;
    @FXML private TableColumn<SessionData, LocalTime> startTimeColumn;
    @FXML private TableColumn<SessionData, LocalTime> endTimeColumn;

    @FXML
    public void initialize() {
        // ==== Movie ToggleGroup ====
        movieToggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == rbAddMovie) {
                vboxAddMovie.setVisible(true);
                vboxAddMovie.setManaged(true);
                vboxUpdateMovie.setVisible(false);
                vboxUpdateMovie.setManaged(false);
            } else if (newVal == rbUpdateMovie) {
                vboxAddMovie.setVisible(false);
                vboxAddMovie.setManaged(false);
                vboxUpdateMovie.setVisible(true);
                vboxUpdateMovie.setManaged(true);
            }
        });
        rbAddMovie.setSelected(true);

        // ==== Populate Genre Combobox'lar ====
        genreComboBox.getItems().addAll("Action", "Comedy", "Drama", "Horror", "Sci-Fi", "Romance", "Animation", "Crime");
        updateFieldComboBox.getItems().addAll("Title", "Genre", "Summary", "PosterPath");
        genreUpdateComboBox.getItems().addAll("Action", "Comedy", "Drama", "Horror", "Sci-Fi", "Romance", "Animation", "Crime");

        // ==== Session Table Columns ====
        sessionIdColumn.setCellValueFactory(new PropertyValueFactory<>("sessionID"));
        hallColumn.setCellValueFactory(new PropertyValueFactory<>("hall"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        // ==== Field update combo listener ====
        updateFieldComboBox.setOnAction(e -> handleUpdateFieldSelection());

        // ==== Cancellation & Refund Type ====
        cancelTypeComboBox.getItems().addAll("Ticket", "Product");
        cancelTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if ("Ticket".equalsIgnoreCase(newVal)) {
                ticketCancelBox.setVisible(true);
                ticketCancelBox.setManaged(true);
                productCancelBox.setVisible(false);
                productCancelBox.setManaged(false);
            } else if ("Product".equalsIgnoreCase(newVal)) {
                productCancelBox.setVisible(true);
                productCancelBox.setManaged(true);
                ticketCancelBox.setVisible(false);
                ticketCancelBox.setManaged(false);
                // Product listesi yükle
                loadProductNamesIntoCombo();
            } else {
                ticketCancelBox.setVisible(false);
                ticketCancelBox.setManaged(false);
                productCancelBox.setVisible(false);
                productCancelBox.setManaged(false);
            }
        });
    }

    private void loadProductNamesIntoCombo() {
        productComboBox.getItems().clear();
        List<Product> allProducts = AdminDAO.getAllProducts(); 
        List<String> productNames = allProducts.stream()
                                               .map(Product::getName)
                                               .collect(Collectors.toList());
        productComboBox.getItems().addAll(productNames);
    }

    private void handleUpdateFieldSelection() {
        String selectedField = updateFieldComboBox.getValue();
        if ("Genre".equals(selectedField)) {
            updateFieldValue.setVisible(false);
            genreUpdateComboBox.setVisible(true);
            newValueLabel.setText("New Genre:");
        } else {
            updateFieldValue.setVisible(true);
            genreUpdateComboBox.setVisible(false);
            newValueLabel.setText("New Value:");
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ========================== MOVIE OPERATIONS ==========================
    @FXML
    void saveNewMovieAction(ActionEvent event) {
        String movieTitle = titleField.getText().trim();
        String movieGenre = genreComboBox.getValue();
        String movieSummary = summaryField.getText().trim();
        String userInputPoster = posterPathField.getText().trim();
        String posterPath = "C:/moviePosters/" + userInputPoster + ".jpg";

        if (movieTitle.isEmpty() || movieGenre == null || movieSummary.isEmpty() || userInputPoster.isEmpty()) {
            showAlert("Warning", "Please fill all fields to add a movie!", Alert.AlertType.WARNING);
            return;
        }

        boolean success = AdminDAO.addMovie(movieTitle, movieGenre, movieSummary, posterPath);
        if (success) {
            showAlert("Success", "Movie added successfully!", Alert.AlertType.INFORMATION);
            // Temizleme
            titleField.clear();
            summaryField.clear();
            posterPathField.clear();
            genreComboBox.getSelectionModel().clearSelection();
        } else {
            showAlert("Error", "Error while adding the movie or it already exists.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void updateMovieAction(ActionEvent event) {
        String movieTitle = updateMovieTitleField.getText().trim();
        String selectedField = updateFieldComboBox.getValue();
        if (movieTitle.isEmpty() || selectedField == null) {
            showAlert("Warning", "Please fill all fields to update a movie!", Alert.AlertType.WARNING);
            return;
        }

        String newValue;
        if ("Genre".equals(selectedField)) {
            newValue = genreUpdateComboBox.getValue();
            if (newValue == null || newValue.isEmpty()) {
                showAlert("Warning", "Please select a genre!", Alert.AlertType.WARNING);
                return;
            }
        } else {
            newValue = updateFieldValue.getText().trim();
            if ("PosterPath".equals(selectedField)) {
                newValue = "C:/moviePosters/" + newValue + ".jpg";
            }
            if (newValue.isEmpty()) {
                showAlert("Warning", "Please type the new value!", Alert.AlertType.WARNING);
                return;
            }
        }

        int movieId = AdminDAO.getMovieIdByTitle(movieTitle);
        if (movieId == -1) {
            showAlert("Error", "Movie not found for the given title!", Alert.AlertType.ERROR);
            return;
        }

        boolean success = AdminDAO.updateMovieField(movieId, selectedField, newValue);
        if (success) {
            showAlert("Success", "Movie updated successfully!", Alert.AlertType.INFORMATION);
            // Temizleme
            updateMovieTitleField.clear();
            updateFieldValue.clear();
            updateFieldComboBox.getSelectionModel().clearSelection();
            genreUpdateComboBox.getSelectionModel().clearSelection();
        } else {
            showAlert("Error", "Error while updating the movie.", Alert.AlertType.ERROR);
        }
    }

    // ========================== SESSION MANAGEMENT ==========================
    @FXML
    void loadSessionsForMovieAction(ActionEvent event) {
        String movieTitle = sessionMovieTitleField.getText().trim();
        if (movieTitle.isEmpty()) {
            showAlert("Warning", "Please enter a movie title to load sessions.", Alert.AlertType.WARNING);
            return;
        }

        int movieId = AdminDAO.getMovieIdByTitle(movieTitle);
        if (movieId == -1) {
            showAlert("Error", "Movie not found for the given title!", Alert.AlertType.ERROR);
            return;
        }

        // Load session list
        List<SessionData> sessionList = AdminDAO.getSessionsByMovie(movieId);
        sessionTable.getItems().clear();
        sessionTable.getItems().addAll(sessionList);
    }

    /**
     * "Add New Session" butonu -> yeni popup açar
     */
    @FXML
    void showAddSessionPopup(ActionEvent event) {
        // movieTitleField'den MovieID buluyoruz
        String movieTitle = sessionMovieTitleField.getText().trim();
        if (movieTitle.isEmpty()) {
            showAlert("Warning", "Please enter a Movie Title before adding a session!", Alert.AlertType.WARNING);
            return;
        }
        int movieId = AdminDAO.getMovieIdByTitle(movieTitle);
        if (movieId == -1) {
            showAlert("Error", "Movie not found for the given title!", Alert.AlertType.ERROR);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cinemaCenter/sessionAddUpdate.fxml"));
            Parent root = loader.load();

            SessionAddUpdateController controller = loader.getController();
            // session parametresi null -> add mode
            controller.setData(movieId, null);

            Stage popup = new Stage();
            popup.setTitle("Add New Session");
            popup.setScene(new Scene(root));
            popup.showAndWait();

            // Pencere kapandıktan sonra tabloyu yenilemek istersek:
            loadSessionsForMovieAction(null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * "Update Selected Session" butonu -> tablo seçili itemi popup'a taşı
     */
    @FXML
    void showUpdateSessionPopup(ActionEvent event) {
        SessionData selected = sessionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a session from the table to update!", Alert.AlertType.WARNING);
            return;
        }
        // Movie Title -> Movie ID
        String movieTitle = sessionMovieTitleField.getText().trim();
        int movieId = AdminDAO.getMovieIdByTitle(movieTitle);
        if (movieId == -1) {
            showAlert("Error", "Movie not found for the given title!", Alert.AlertType.ERROR);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cinemaCenter/sessionAddUpdate.fxml"));
            Parent root = loader.load();

            SessionAddUpdateController controller = loader.getController();
            // session parametresine tablo seçili itemi ver -> update mode
            controller.setData(movieId, selected);

            Stage popup = new Stage();
            popup.setTitle("Update Session");
            popup.setScene(new Scene(root));
            popup.showAndWait();

            // Pencere kapandıktan sonra tabloyu yenilemek istersek:
            loadSessionsForMovieAction(null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteSessionAction(ActionEvent event) {
        SessionData selected = sessionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a session from the table to delete!", Alert.AlertType.WARNING);
            return;
        }
        int sessionId = selected.getSessionID();

        boolean hasSold = AdminDAO.hasSoldTickets(sessionId);
        if (hasSold) {
            showAlert("Warning", "Cannot delete a session that has sold tickets!", Alert.AlertType.WARNING);
            return;
        }

        boolean success = AdminDAO.deleteSession(sessionId);
        if (success) {
            showAlert("Success", "Session deleted successfully!", Alert.AlertType.INFORMATION);
            sessionTable.getItems().remove(selected);
        } else {
            showAlert("Error", "Error while deleting session.", Alert.AlertType.ERROR);
        }
    }

    // ========================== CANCELLATION & REFUND ==========================
    @FXML
    void processCancellationAction(ActionEvent event) {
        String cancelType = cancelTypeComboBox.getValue();

        if (cancelType == null || cancelType.isEmpty()) {
            showAlert("Warning", "Please select a cancellation type (Ticket or Product).", Alert.AlertType.WARNING);
            return;
        }

        if ("Ticket".equalsIgnoreCase(cancelType)) {
            // Bilet iptal
            String ticketId = ticketIdField.getText().trim();
            if (ticketId.isEmpty()) {
                showAlert("Warning", "Please enter a valid ticket ID.", Alert.AlertType.WARNING);
                return;
            }
            boolean success = AdminDAO.cancelTicket(Integer.parseInt(ticketId));
            if (success) {
                showAlert("Success", "Ticket cancelled successfully!", Alert.AlertType.INFORMATION);
                ticketIdField.clear();
            } else {
                showAlert("Error", "Error or ticket not found during cancellation.", Alert.AlertType.ERROR);
            }
        } else if ("Product".equalsIgnoreCase(cancelType)) {
            // Ürün iptal
            String selectedProduct = productComboBox.getValue();
            String qtyStr = productQtyField.getText().trim();

            if (selectedProduct == null || selectedProduct.isEmpty() || qtyStr.isEmpty()) {
                showAlert("Warning", "Please select a product and enter quantity.", Alert.AlertType.WARNING);
                return;
            }

            int qty;
            try {
                qty = Integer.parseInt(qtyStr);
            } catch (NumberFormatException e) {
                showAlert("Warning", "Quantity must be a numeric value.", Alert.AlertType.WARNING);
                return;
            }
            if (qty <= 0) {
                showAlert("Warning", "Quantity must be > 0.", Alert.AlertType.WARNING);
                return;
            }

            int productId = AdminDAO.getProductIdByName(selectedProduct);
            if (productId == -1) {
                showAlert("Error", "Product not found in database!", Alert.AlertType.ERROR);
                return;
            }

            boolean success = AdminDAO.cancelProduct(productId, qty);
            if (success) {
                showAlert("Success", "Product cancellation processed successfully!", Alert.AlertType.INFORMATION);
                productComboBox.getSelectionModel().clearSelection();
                productQtyField.clear();
            } else {
                showAlert("Error", "Error while cancelling the product.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cinemaCenter/login.fxml"));
            Parent loginRoot = loader.load();
            Stage stage = (Stage) rbAddMovie.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
