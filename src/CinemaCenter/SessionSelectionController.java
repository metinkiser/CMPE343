package CinemaCenter;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class SessionSelectionController implements DataInitializable {

    @FXML
    private ImageView posterImageView;

    @FXML
    private Label titleLabel;

    @FXML
    private Label genreLabel;

    @FXML
    private Label summaryLabel;
    
    @FXML
    private Button backButton;

    @FXML
    private TableView<Session> sessionTableView;

    @FXML
    private TableColumn<Session, String> dateColumn;

    @FXML
    private TableColumn<Session, String> hallColumn;

    @FXML
    private TableColumn<Session, String> startTimeColumn;

    @FXML
    private TableColumn<Session, String> endTimeColumn;

    @FXML
    private TableColumn<Session, Integer> vacantSeatsColumn;

    @FXML
    private Button nextButton;

    private Movie selectedMovie;
    private Session selectedSession;

    @FXML
    public void initialize() {
        // Set up table columns
        dateColumn.setCellValueFactory(data -> data.getValue().dateProperty());
        hallColumn.setCellValueFactory(data -> data.getValue().hallProperty());
        startTimeColumn.setCellValueFactory(data -> data.getValue().startTimeProperty());
        endTimeColumn.setCellValueFactory(data -> data.getValue().endTimeProperty());
        vacantSeatsColumn.setCellValueFactory(data -> data.getValue().vacantSeatsProperty().asObject());

        // Disable Next button until a session is selected
        sessionTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedSession = newValue;
            nextButton.setDisable(newValue == null);
        });
    }

    public void setSelectedMovie(Movie movie) {
        this.selectedMovie = movie;
        
        // Display movie details
        titleLabel.setText(movie.getTitle());
        genreLabel.setText("Genre: " + movie.getGenre());
        summaryLabel.setText(movie.getSummary());
        posterImageView.setImage(new Image(new java.io.File(movie.getPosterPath()).toURI().toString()));
        
        // Load sessions immediately when movie is set
        loadSessions();
    }

    private void loadSessions() {
        sessionTableView.getItems().clear();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT s.*, m.* FROM Sessions s " +
                     "JOIN Movies m ON s.MovieID = m.MovieID " +
                     "WHERE s.MovieID = ?")) {

            stmt.setInt(1, selectedMovie.getMovieID());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                // Time değerlerini String olarak alıyoruz
                String startTime = rs.getString("StartTime");
                String endTime = rs.getString("EndTime");
                
                Session session = new Session(
                        rs.getInt("SessionID"),
                        rs.getString("Hall"),
                        rs.getDate("SessionDate").toString(),
                        startTime,  // String olarak kullanıyoruz
                        endTime,    // String olarak kullanıyoruz
                        rs.getInt("VacantSeats")
                );
                
                Movie movie = new Movie(
                    rs.getInt("MovieID"),
                    rs.getString("Title"),
                    rs.getString("Genre"),
                    rs.getString("Summary"),
                    rs.getString("PosterPath")
                );
                session.setMovie(movie);
                
                sessionTableView.getItems().add(session);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load sessions.");
        }
    }

    @FXML
    public void onBackButtonClick() {
        // Mevcut pencereyi kapat
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
        
        // Yeni pencereyi aç
        NavigationUtil.loadScene("cashier_operations.fxml", "Search Movies");
    }

    @FXML
    public void onNextButtonClick() {
        selectedSession = sessionTableView.getSelectionModel().getSelectedItem();
        if (selectedSession == null) {
            showAlert("Uyarı", "Lütfen bir seans seçiniz.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SeatSelection.fxml"));
            Parent root = loader.load();
            
            selectedSession.setMovie(selectedMovie);
            
            SeatSelectionController seatController = loader.getController();
            seatController.setSelectedSession(selectedSession);
            
            Stage stage = (Stage) nextButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Koltuk Seçimi - " + selectedMovie.getTitle());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Hata", "Koltuk seçimi ekranı yüklenirken bir hata oluştu.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void initData(Object data) {
        if (data instanceof Movie) {
            setSelectedMovie((Movie) data);
        }
    }
}
