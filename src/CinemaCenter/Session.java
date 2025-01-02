package CinemaCenter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SessionSelectionController {

    @FXML
    private TableView<Session> sessionTable;

    @FXML
    private TableColumn<Session, String> dateColumn;

    @FXML
    private TableColumn<Session, String> startTimeColumn;

    @FXML
    private TableColumn<Session, String> hallColumn;

    @FXML
    private TableColumn<Session, Integer> vacantSeatsColumn;

    @FXML
    private Button nextButton;

    private ObservableList<Session> sessionList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // TableView sütunlarını verilerle bağla
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        hallColumn.setCellValueFactory(new PropertyValueFactory<>("hall"));
        vacantSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("vacantSeats"));

        // Seans bilgilerini yükle
        loadSessions();
    }

    private void loadSessions() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT SessionDate, StartTime, Hall, VacantSeats FROM Sessions WHERE MovieID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, SelectedMovie.getMovieID()); // Önceden seçilen filmin ID'si

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sessionList.add(new Session(
                        rs.getString("SessionDate"),
                        rs.getString("StartTime"),
                        rs.getString("Hall"),
                        rs.getInt("VacantSeats")
                ));
            }
            sessionTable.setItems(sessionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNext() {
        // Kullanıcı bir seans seçmiş mi kontrol et
        Session selectedSession = sessionTable.getSelectionModel().getSelectedItem();
        if (selectedSession == null) {
            System.out.println("Please select a session.");
            return;
        }

        // Seçilen seansı kaydet ve bir sonraki aşamaya geç
        SelectedSession.setSession(selectedSession);
        SceneLoader.loadScene("seat_selection.fxml"); // Koltuk seçimi ekranına geç
    }

    @FXML
    private void handleBack() {
        // Geri dön ve arama ekranına geç
        SceneLoader.loadScene("search.fxml");
    }
}
