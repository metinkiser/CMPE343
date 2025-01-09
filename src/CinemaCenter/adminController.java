package CinemaCenter;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalTime;

public class AdminController {

    @FXML
    private ComboBox<String> adminActionComboBox;

    @FXML
    private VBox contentArea;  

    @FXML
    private Button saveButton; 

    // Film ekleme/güncelleme bileşenleri
    private TextField titleField;
    private ComboBox<String> genreComboBox;
    private TextArea summaryField;
    private TextField posterPathField;

    // Seans yönetimi bileşenleri
    private DatePicker sessionDatePicker;
    private ComboBox<String> hallComboBox;
    private ComboBox<LocalTime> startTimeComboBox;
    private ComboBox<LocalTime> endTimeComboBox;
    private TextField vacantSeatsField;

    @FXML
    public void initialize() {
        adminActionComboBox.getItems().addAll(
            "Yeni Film Ekle",
            "Film Güncelle",
            "Seans Yönetimi",
            "Bilet İptal & İade"
        );

        saveButton.setVisible(false); 
    }

    @FXML
    void handleAdminAction(ActionEvent event) {
        String selectedAction = adminActionComboBox.getValue();
        if (selectedAction == null) {
            return;
        }

        contentArea.getChildren().clear();
        saveButton.setVisible(true);

        switch (selectedAction) {
            case "Yeni Film Ekle":
                setupMovieAddForm();
                break;
            case "Film Güncelle":
                setupMovieUpdateForm();
                break;
            case "Seans Yönetimi":
                setupSessionManagementForm();
                break;
            case "Bilet İptal & İade":
                setupTicketCancelForm();
                break;
        }
    }

    /** ✅ Yeni Film Ekleme Formu */
    private void setupMovieAddForm() {
        titleField = new TextField();
        genreComboBox = new ComboBox<>();
        summaryField = new TextArea();
        posterPathField = new TextField();

        genreComboBox.getItems().addAll("Action", "Comedy", "Drama", "Horror", "Sci-Fi", "Romance", "Animation", "Crime");

        contentArea.getChildren().addAll(
            new Label("Film Adı:"), titleField,
            new Label("Tür:"), genreComboBox,
            new Label("Özet:"), summaryField,
            new Label("Afiş Yolu:"), posterPathField
        );

        saveButton.setText("Filmi Ekle");
    }

    /** ✅ Film Güncelleme Formu */
    private void setupMovieUpdateForm() {
        titleField = new TextField();
        genreComboBox = new ComboBox<>();
        summaryField = new TextArea();
        posterPathField = new TextField();

        genreComboBox.getItems().addAll("Action", "Comedy", "Drama", "Horror", "Sci-Fi", "Romance", "Animation", "Crime");

        contentArea.getChildren().addAll(
            new Label("Film Adı:"), titleField,
            new Label("Yeni Tür:"), genreComboBox,
            new Label("Yeni Özet:"), summaryField,
            new Label("Yeni Afiş Yolu:"), posterPathField
        );

        saveButton.setText("Filmi Güncelle");
    }

    /** ✅ Seans Yönetimi Formu */
    private void setupSessionManagementForm() {
        titleField = new TextField();
        sessionDatePicker = new DatePicker();
        hallComboBox = new ComboBox<>();
        startTimeComboBox = new ComboBox<>();
        endTimeComboBox = new ComboBox<>();
        vacantSeatsField = new TextField();

        hallComboBox.getItems().addAll("Hall_A", "Hall_B");
        startTimeComboBox.getItems().addAll(LocalTime.of(10, 0), LocalTime.of(14, 0), LocalTime.of(18, 0));
        endTimeComboBox.getItems().addAll(LocalTime.of(12, 0), LocalTime.of(16, 0), LocalTime.of(20, 0));

        contentArea.getChildren().addAll(
            new Label("Film Adı:"), titleField,
            new Label("Seans Tarihi:"), sessionDatePicker,
            new Label("Salon Seç:"), hallComboBox,
            new Label("Başlangıç Saati:"), startTimeComboBox,
            new Label("Bitiş Saati:"), endTimeComboBox,
            new Label("Boş Koltuk Sayısı:"), vacantSeatsField
        );

        saveButton.setText("Seans Ekle");
    }

    /** ✅ Bilet İptal ve İade Formu */
    private void setupTicketCancelForm() {
        TextField ticketIdField = new TextField();
        contentArea.getChildren().addAll(
            new Label("Bilet No:"), ticketIdField
        );

        saveButton.setText("Bileti İptal Et");
    }

    /** ✅ Seansı Kaydetme */
    @FXML
    void handleSave(ActionEvent event) {
        String selectedAction = adminActionComboBox.getValue();
        if (selectedAction == null) {
            System.out.println("Lütfen bir işlem seçin!");
            return;
        }

        switch (selectedAction) {
            case "Yeni Film Ekle":
                saveNewMovie();
                break;
            case "Film Güncelle":
                updateMovie();
                break;
            case "Seans Yönetimi":
                saveSession();
                break;
        }
    }

    /** ✅ Yeni Filmi Veritabanına Kaydet */
    private void saveNewMovie() {
        String movieTitle = titleField.getText().trim();
        String movieGenre = genreComboBox.getValue();
        String movieSummary = summaryField.getText().trim();
        String posterPath = posterPathField.getText().trim();

        if (movieTitle.isEmpty() || movieGenre == null || movieSummary.isEmpty() || posterPath.isEmpty()) {
            System.out.println("Tüm alanları doldurun!");
            return;
        }

        boolean success = AdminDAO.addMovie(movieTitle, movieGenre, movieSummary, posterPath);
        if (success) System.out.println("Film başarıyla eklendi!");
        else System.out.println("Film eklenirken hata oluştu.");
    }

    /** ✅ Filmi Güncelleme */
    private void updateMovie() {
        String movieTitle = titleField.getText().trim();
        String newGenre = genreComboBox.getValue();
        String newSummary = summaryField.getText().trim();
        String newPoster = posterPathField.getText().trim();

        if (movieTitle.isEmpty()) {
            System.out.println("Film adını giriniz!");
            return;
        }

        boolean success = AdminDAO.updateMovie(movieTitle, newGenre, newSummary, newPoster);
        if (success) System.out.println("Film başarıyla güncellendi!");
        else System.out.println("Film güncellenirken hata oluştu.");
    }

    /** ✅ Yeni Seansı Veritabanına Kaydet */
    private void saveSession() {
        String movieTitle = titleField.getText().trim();
        LocalDate sessionDate = sessionDatePicker.getValue();
        String hall = hallComboBox.getValue();
        LocalTime startTime = startTimeComboBox.getValue();
        LocalTime endTime = endTimeComboBox.getValue();
        
        int vacantSeats;
        try {
            vacantSeats = Integer.parseInt(vacantSeatsField.getText().trim());
        } catch (NumberFormatException e) {
            System.out.println("Boş koltuk sayısı geçerli bir sayı olmalıdır!");
            return;
        }

        if (movieTitle.isEmpty() || sessionDate == null || hall == null || startTime == null || endTime == null) {
            System.out.println("Lütfen tüm alanları doldurun!");
            return;
        }

        // **Önce MovieID'yi al**
        int movieId = AdminDAO.getMovieIdByTitle(movieTitle);
        if (movieId == -1) {
            System.out.println("Bu isimde bir film bulunamadı!");
            return;
        }

        // **Doğru parametreleri kullanarak çağır**
        boolean success = AdminDAO.addSession(movieId, sessionDate, hall, startTime, endTime, vacantSeats);
        if (success) System.out.println("Seans başarıyla eklendi!");
        else System.out.println("Seans eklenirken hata oluştu.");
    }

}
