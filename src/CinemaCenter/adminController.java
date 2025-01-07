package CinemaCenter;



import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;

public class AdminController {

    @FXML
    private ComboBox<String> adminActionComboBox;

    @FXML
    private VBox contentArea;  

    @FXML
    private Button saveButton; 

    private TextField titleField;
    private TextField posterField;
    private ComboBox<String> genreComboBox;
    private TextArea summaryField;
    private TextField updateMovieIdField;
    private TextField programField;
    private TextField ticketIdField;

    @FXML
    public void initialize() {
        adminActionComboBox.getItems().addAll(
            "Yeni Film Ekle",
            "Film Güncelle",
            "Program Güncelle",
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
            case "Program Güncelle":
                setupProgramUpdateForm();
                break;
            case "Bilet İptal & İade":
                setupTicketCancelForm();
                break;
        }
    }

    private void setupMovieAddForm() {
        titleField = new TextField();
        posterField = new TextField();
        summaryField = new TextArea();
        genreComboBox = new ComboBox<>();

        genreComboBox.getItems().addAll("Action", "Comedy", "Drama", "Horror", "Sci-Fi", "Romance", "Animation", "Crime");

        contentArea.getChildren().addAll(
            new Label("Film Adı:"), titleField,
            new Label("Tür:"), genreComboBox,
            new Label("Özet:"), summaryField,
            new Label("Afiş Yolu:"), posterField
        );

        saveButton.setText("Filmi Ekle");
    }

    private void setupMovieUpdateForm() {
        updateMovieIdField = new TextField();
        titleField = new TextField();
        genreComboBox = new ComboBox<>();
        summaryField = new TextArea();
        posterField = new TextField();

        genreComboBox.getItems().addAll("Action", "Comedy", "Drama", "Horror", "Sci-Fi", "Romance", "Animation", "Crime");

        contentArea.getChildren().addAll(
            new Label("Film ID:"), updateMovieIdField,
            new Label("Yeni Film Adı:"), titleField,
            new Label("Yeni Tür:"), genreComboBox,
            new Label("Yeni Özet:"), summaryField,
            new Label("Yeni Afiş Yolu:"), posterField
        );

        saveButton.setText("Filmi Güncelle");
    }

    private void setupProgramUpdateForm() {
        programField = new TextField();
        contentArea.getChildren().addAll(
            new Label("Yeni Program Bilgisi:"), programField
        );
        saveButton.setText("Programı Güncelle");
    }

    private void setupTicketCancelForm() {
        ticketIdField = new TextField();
        contentArea.getChildren().addAll(
            new Label("Bilet No:"), ticketIdField
        );
        saveButton.setText("Bileti İptal Et");
    }

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
            case "Program Güncelle":
                updateProgram();
                break;
            case "Bilet İptal & İade":
                cancelTicket();
                break;
        }
    }

    private void saveNewMovie() {
        String movieTitle = titleField.getText().trim();
        String moviePoster = posterField.getText().trim();
        String movieGenre = genreComboBox.getValue();
        String movieSummary = summaryField.getText().trim();

        if (movieTitle.isEmpty() || moviePoster.isEmpty() || movieGenre == null || movieSummary.isEmpty()) {
            System.out.println("Tüm alanları doldurun!");
            return;
        }

        boolean success = MovieDAO.addMovie(movieTitle, movieGenre, movieSummary, moviePoster);
        if (success) System.out.println("Film başarıyla eklendi!");
        else System.out.println("Film eklenirken hata oluştu.");
    }

    private void updateMovie() {
        int movieId;
        try {
            movieId = Integer.parseInt(updateMovieIdField.getText().trim());
        } catch (NumberFormatException e) {
            System.out.println("Geçerli bir Film ID giriniz!");
            return;
        }

        String newTitle = titleField.getText().trim();
        String newGenre = genreComboBox.getValue();
        String newSummary = summaryField.getText().trim();
        String newPoster = posterField.getText().trim();

        boolean success = MovieDAO.updateMovie(movieId, newTitle, newGenre, newSummary, newPoster);
        if (success) System.out.println("Film başarıyla güncellendi!");
        else System.out.println("Film güncellenirken hata oluştu.");
    }

    private void updateProgram() {
        System.out.println("Program güncellendi!");
    }

    private void cancelTicket() {
        System.out.println("Bilet iptal edildi!");
    }
}  
