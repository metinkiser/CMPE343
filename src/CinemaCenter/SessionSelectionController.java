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

/**
 * Controller class for the session selection screen.
 * Handles the display of movie sessions and user interactions for selecting a specific screening.
 */
public class SessionSelectionController implements DataInitializable {

    @FXML private ImageView posterImageView;
    @FXML private Label titleLabel;
    @FXML private Label genreLabel;
    @FXML private Label summaryLabel;
    @FXML private Button backButton;
    @FXML private TableView<Session> sessionTableView;
    @FXML private TableColumn<Session, String> dateColumn;
    @FXML private TableColumn<Session, String> hallColumn;
    @FXML private TableColumn<Session, String> startTimeColumn;
    @FXML private TableColumn<Session, String> endTimeColumn;
    @FXML private TableColumn<Session, Integer> vacantSeatsColumn;
    @FXML private Button nextButton;

    private Movie selectedMovie;
    private Session selectedSession;

    /**
     * Initializes the controller class. This method is automatically called after the FXML file has been loaded.
     * Sets up table columns and selection listeners.
     */
    @FXML
    public void initialize() {
        dateColumn.setCellValueFactory(data -> data.getValue().dateProperty());
        hallColumn.setCellValueFactory(data -> data.getValue().hallProperty());
        startTimeColumn.setCellValueFactory(data -> data.getValue().startTimeProperty());
        endTimeColumn.setCellValueFactory(data -> data.getValue().endTimeProperty());
        vacantSeatsColumn.setCellValueFactory(data -> data.getValue().vacantSeatsProperty().asObject());

        sessionTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedSession = newValue;
            nextButton.setDisable(newValue == null);
        });
    }

    /**
     * Sets the selected movie and updates the UI with its details.
     * @param movie The movie object to display details for
     */
    public void setSelectedMovie(Movie movie) {
        this.selectedMovie = movie;
        titleLabel.setText(movie.getTitle());
        genreLabel.setText("Genre: " + movie.getGenre());
        summaryLabel.setText(movie.getSummary());
        posterImageView.setImage(new Image(new java.io.File(movie.getPosterPath()).toURI().toString()));
        loadSessions();
    }

    /**
     * Loads all available sessions for the selected movie from the database.
     */
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
                String startTime = rs.getString("StartTime");
                String endTime = rs.getString("EndTime");
                
                Session session = new Session(
                        rs.getInt("SessionID"),
                        rs.getString("Hall"),
                        rs.getDate("SessionDate").toString(),
                        startTime,
                        endTime,
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

    /**
     * Handles the back button click event.
     * Returns to the cashier operations screen.
     */
    @FXML
    public void onBackButtonClick() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
        NavigationUtil.loadScene("cashier_operations.fxml", "Search Movies");
    }

    /**
     * Handles the next button click event.
     * Proceeds to the seat selection screen if a session is selected.
     */
    @FXML
    public void onNextButtonClick() {
        selectedSession = sessionTableView.getSelectionModel().getSelectedItem();
        if (selectedSession == null) {
            showAlert("Warning", "Please select a session.");
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
            stage.setTitle("Seat Selection - " + selectedMovie.getTitle());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while loading the seat selection screen.");
        }
    }

    /**
     * Displays an alert dialog with the specified title and content.
     * @param title The title of the alert dialog
     * @param content The message to display in the alert dialog
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Initializes the controller with data passed from the previous screen.
     * @param data The Movie object to be displayed
     */
    @Override
    public void initData(Object data) {
        if (data instanceof Movie) {
            setSelectedMovie((Movie) data);
        }
    }
}
