package CinemaCenter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SearchController {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> resultsList;

    private ObservableList<String> movieList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // ListView'i başlat
        resultsList.setItems(movieList);
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim();

        if (searchText.isEmpty()) {
            resultsList.getItems().clear();
            resultsList.getItems().add("Please enter a search keyword.");
            return;
        }

        movieList.clear();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT Title FROM Movies WHERE Title LIKE ? OR Genre LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + searchText + "%");
            stmt.setString(2, "%" + searchText + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                movieList.add(rs.getString("Title"));
            }

            if (movieList.isEmpty()) {
                resultsList.getItems().clear();
                resultsList.getItems().add("No movies found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultsList.getItems().clear();
            resultsList.getItems().add("Error occurred while searching.");
        }
    }

    @FXML
    private void handleBack() {
        try {
            // Login.fxml ekranına geri dön
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) searchField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMovieSelection() {
        String selectedMovie = resultsList.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            showMovieDetails(selectedMovie);
        }
    }

    private void showMovieDetails(String movieTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MovieDetails.fxml"));
            Parent root = loader.load();

            // MovieDetailsController'ı al ve filme ait detayları ayarla
            MovieDetailController controller = loader.getController();
            controller.setMovieTitle(movieTitle);

            Stage stage = (Stage) searchField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
