package CinemaCenter;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import java.sql.*;
import java.util.*;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

public class ManagerController {

    @FXML private VBox productsTabPane;
    @FXML private VBox personnelTabPane;
    @FXML private VBox ticketPricesTabPane;
    @FXML private VBox taxRatesTabPane;      // YENİ: fxml'de ekledik
    @FXML private VBox revenueTabPane;

    private int managerUserId; // Manager kendi userID'si

    public void setManagerUserId(int id) {
        this.managerUserId = id;
    }

    // (1) Products
    private TableView<ProductRow> productsTable;
    private TextField stockTextField;
    private Button updateStockButton;
    private TextField priceTextField;
    private Button updatePriceButton;

    // (2) Personnel
    private TableView<UserRow> usersTable;
    private Button hireButton, fireButton, editButton;

    // (3) TicketPrices
    private TableView<TicketPriceRow> ticketPricesTable;
    private Button updateTicketPriceButton;
    private TextField basePriceField;
    private TextField ageDiscountRateField;

    // (4) Tax Rates
    private TableView<TaxRateRow> taxRatesTable;
    private Button updateTaxButton;

    // (5) Revenue & Tax
    private Label totalRevenueLabel;
    private Label totalTaxLabel;
    private Button refreshRevenueButton;

    @FXML
    public void initialize() {
        setupProductsTab();
        setupPersonnelTab();
        setupTicketPricesTab();
        setupTaxRatesTab();    // YENİ
        setupRevenueTab();
    }

    // ------------------------------------------------------------
    // (1) Products & Stock Tab
    // ------------------------------------------------------------
    private void setupProductsTab() {
        productsTable = new TableView<>();
        TableColumn<ProductRow, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("productId"));

        TableColumn<ProductRow, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<ProductRow, Double> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<ProductRow, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));

        productsTable.getColumns().addAll(colId, colName, colPrice, colStock);

        stockTextField = new TextField();
        stockTextField.setPromptText("Stock (+/-)");
        updateStockButton = new Button("Update Stock");
        updateStockButton.setOnAction(e -> onUpdateStockClick());

        priceTextField = new TextField();
        priceTextField.setPromptText("New Price");
        updatePriceButton = new Button("Update Price");
        updatePriceButton.setOnAction(e -> onUpdatePriceClick());

        HBox box = new HBox(10, stockTextField, updateStockButton, priceTextField, updatePriceButton);
        productsTabPane.getChildren().addAll(productsTable, box);

        loadProducts();
    }

    private void loadProducts() {
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT ProductID, Name, Price, StockQuantity FROM Products")) {
            ObservableList<ProductRow> data = FXCollections.observableArrayList();
            while (rs.next()) {
                data.add(new ProductRow(
                    rs.getInt("ProductID"),
                    rs.getString("Name"),
                    rs.getDouble("Price"),
                    rs.getInt("StockQuantity")
                ));
            }
            productsTable.setItems(data);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void onUpdateStockClick() {
        var selected = productsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a product.");
            return;
        }
        String val = stockTextField.getText().trim();
        if (val.isEmpty()) {
            showAlert("Warning", "Please enter a stock value.");
            return;
        }
        try {
            int delta = Integer.parseInt(val);
            int updatedValue = selected.getStockQuantity() + delta;
            if (updatedValue < 0) {
                showAlert("Error", "Stock cannot be negative!");
                return;
            }
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE Products SET StockQuantity=? WHERE ProductID=?")) {
                stmt.setInt(1, updatedValue);
                stmt.setInt(2, selected.getProductId());
                stmt.executeUpdate();
            }
            loadProducts();
            showAlert("Info", "Stock updated successfully.");
        } catch (NumberFormatException ex) {
            showAlert("Error", "Invalid integer for stock!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "DB error updating stock.");
        }
    }

    private void onUpdatePriceClick() {
        var selected = productsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a product.");
            return;
        }
        String val = priceTextField.getText().trim();
        if (val.isEmpty()) {
            showAlert("Warning", "Please enter a price.");
            return;
        }
        try {
            double newPrice = Double.parseDouble(val);
            if (newPrice < 0) {
                showAlert("Error", "Price cannot be negative!");
                return;
            }
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE Products SET Price=? WHERE ProductID=?")) {
                stmt.setDouble(1, newPrice);
                stmt.setInt(2, selected.getProductId());
                stmt.executeUpdate();
            }
            loadProducts();
            showAlert("Info", "Price updated successfully.");
        } catch (NumberFormatException ex) {
            showAlert("Error", "Invalid number for price!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "DB error updating price.");
        }
    }

    // ------------------------------------------------------------
    // (2) PERSONNEL (Users tablosu)
    // ------------------------------------------------------------
    private void setupPersonnelTab() {
        usersTable = new TableView<>();
        TableColumn<UserRow, Integer> colUid = new TableColumn<>("UserID");
        colUid.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<UserRow, String> colFName = new TableColumn<>("FirstName");
        colFName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<UserRow, String> colLName = new TableColumn<>("LastName");
        colLName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<UserRow, String> colUname = new TableColumn<>("Username");
        colUname.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<UserRow, String> colPass = new TableColumn<>("Password");
        colPass.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<UserRow, String> colRole = new TableColumn<>("Role");
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        usersTable.getColumns().addAll(colUid, colFName, colLName, colUname, colPass, colRole);

        hireButton = new Button("Hire");
        hireButton.setOnAction(e -> openHireDialog());

        fireButton = new Button("Fire");
        fireButton.setOnAction(e -> onFireUser());

        editButton = new Button("Edit");
        editButton.setOnAction(e -> openEditDialog());

        personnelTabPane.getChildren().addAll(usersTable, new HBox(10, hireButton, fireButton, editButton));

        loadUsers();
    }

    private void loadUsers() {
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Users")) {
            ObservableList<UserRow> data = FXCollections.observableArrayList();
            while (rs.next()) {
                data.add(new UserRow(
                    rs.getInt("UserID"),
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getString("Username"),
                    rs.getString("Password"),
                    rs.getString("Role")
                ));
            }
            usersTable.setItems(data);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // YENİ: "Hire" için ayrı bir diyalog
    private void openHireDialog() {
        Dialog<UserRow> dialog = new Dialog<>();
        dialog.setTitle("Hire New User");
        dialog.setHeaderText("Enter user details:");

        Label fNameLabel = new Label("First Name:");
        TextField fNameField = new TextField();
        Label lNameLabel = new Label("Last Name:");
        TextField lNameField = new TextField();
        Label unameLabel = new Label("Username:");
        TextField unameField = new TextField();
        Label passLabel = new Label("Password:");
        TextField passField = new TextField();
        Label roleLabel = new Label("Role (Manager/Cashier/Admin):");
        TextField roleField = new TextField();

        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(10));

        gp.add(fNameLabel, 0, 0); gp.add(fNameField, 1, 0);
        gp.add(lNameLabel, 0, 1); gp.add(lNameField, 1, 1);
        gp.add(unameLabel, 0, 2); gp.add(unameField, 1, 2);
        gp.add(passLabel, 0, 3); gp.add(passField, 1, 3);
        gp.add(roleLabel, 0, 4); gp.add(roleField, 1, 4);

        dialog.getDialogPane().setContent(gp);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                // Validasyon
                String fn = fNameField.getText().trim();
                String ln = lNameField.getText().trim();
                String un = unameField.getText().trim();
                String pw = passField.getText().trim();
                String rl = roleField.getText().trim();

                if (fn.isEmpty() || ln.isEmpty() || un.isEmpty() || pw.isEmpty() || rl.isEmpty()) {
                    showAlert("Error", "All fields are required!");
                    return null;
                }
                if (!isValidRole(rl)) {
                    showAlert("Error", "Role must be one of: Manager, Cashier, Admin");
                    return null;
                }
                if (isUsernameUsed(un)) {
                    showAlert("Error", "Username is already used!");
                    return null;
                }

                return new UserRow(-1, fn, ln, un, pw, rl);
            }
            return null;
        });

        var result = dialog.showAndWait();
        if (result.isPresent()) {
            UserRow newUser = result.get();
            // Insert DB
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Users (FirstName, LastName, Username, Password, Role) VALUES (?,?,?,?,?)")) {
                stmt.setString(1, newUser.getFirstName());
                stmt.setString(2, newUser.getLastName());
                stmt.setString(3, newUser.getUsername());
                stmt.setString(4, newUser.getPassword());
                stmt.setString(5, newUser.getRole());
                stmt.executeUpdate();
                loadUsers();
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Error", "Could not hire user.");
            }
        }
    }

    // YENİ: "Edit" için ayrı diyalog
    private void openEditDialog() {
        UserRow selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Select a user to edit!");
            return;
        }
        Dialog<UserRow> dialog = new Dialog<>();
        dialog.setTitle("Edit User");
        dialog.setHeaderText("Update user details:");

        Label fNameLabel = new Label("First Name:");
        TextField fNameField = new TextField(selected.getFirstName());
        Label lNameLabel = new Label("Last Name:");
        TextField lNameField = new TextField(selected.getLastName());
        Label unameLabel = new Label("Username:");
        TextField unameField = new TextField(selected.getUsername());
        Label passLabel = new Label("Password:");
        TextField passField = new TextField(selected.getPassword());
        Label roleLabel = new Label("Role (Manager/Cashier/Admin):");
        TextField roleField = new TextField(selected.getRole());

        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(10));

        gp.add(fNameLabel, 0, 0); gp.add(fNameField, 1, 0);
        gp.add(lNameLabel, 0, 1); gp.add(lNameField, 1, 1);
        gp.add(unameLabel, 0, 2); gp.add(unameField, 1, 2);
        gp.add(passLabel, 0, 3); gp.add(passField, 1, 3);
        gp.add(roleLabel, 0, 4); gp.add(roleField, 1, 4);

        dialog.getDialogPane().setContent(gp);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                String fn = fNameField.getText().trim();
                String ln = lNameField.getText().trim();
                String un = unameField.getText().trim();
                String pw = passField.getText().trim();
                String rl = roleField.getText().trim();

                if (fn.isEmpty() || ln.isEmpty() || un.isEmpty() || pw.isEmpty() || rl.isEmpty()) {
                    showAlert("Error", "All fields are required!");
                    return null;
                }
                if (!isValidRole(rl)) {
                    showAlert("Error", "Invalid role!");
                    return null;
                }
                // If changed username => check usage
                if (!un.equalsIgnoreCase(selected.getUsername()) && isUsernameUsed(un)) {
                    showAlert("Error", "Username is already used!");
                    return null;
                }
                // Return updated row
                return new UserRow(selected.getUserId(), fn, ln, un, pw, rl);
            }
            return null;
        });

        var result = dialog.showAndWait();
        if (result.isPresent()) {
            UserRow updated = result.get();
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE Users SET FirstName=?, LastName=?, Username=?, Password=?, Role=? WHERE UserID=?")) {
                stmt.setString(1, updated.getFirstName());
                stmt.setString(2, updated.getLastName());
                stmt.setString(3, updated.getUsername());
                stmt.setString(4, updated.getPassword());
                stmt.setString(5, updated.getRole());
                stmt.setInt(6, updated.getUserId());
                stmt.executeUpdate();
                loadUsers();
                showAlert("Info", "User updated.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Error", "Could not edit user.");
            }
        }
    }

    private void onFireUser() {
        UserRow selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Select a user to fire!");
            return;
        }
        if (selected.getUserId() == this.managerUserId) {
            showAlert("Error", "You cannot fire yourself!");
            return;
        }
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Users WHERE UserID=?")) {
            stmt.setInt(1, selected.getUserId());
            stmt.executeUpdate();
            loadUsers();
            showAlert("Info", "User fired successfully.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Could not fire user.");
        }
    }

    // ------------------------------------------------------------
    // (3) TicketPrices & Discount
    // ------------------------------------------------------------
    private void setupTicketPricesTab() {
        ticketPricesTable = new TableView<>();

        // PriceID kolonu KALDIRILDI. Filmin adını göstereceğiz
        // Yani getMovieTitle() gibi. O yüzden data modelde "movieTitle" ekleyelim
        TableColumn<TicketPriceRow, String> colMovieTitle = new TableColumn<>("Movie Title");
        colMovieTitle.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));

        TableColumn<TicketPriceRow, Double> colBase = new TableColumn<>("BasePrice");
        colBase.setCellValueFactory(new PropertyValueFactory<>("basePrice"));

        TableColumn<TicketPriceRow, Double> colDisc = new TableColumn<>("DiscountedPrice");
        colDisc.setCellValueFactory(new PropertyValueFactory<>("discountedPrice"));

        TableColumn<TicketPriceRow, Double> colAgeDisc = new TableColumn<>("AgeDiscount(%)");
        colAgeDisc.setCellValueFactory(new PropertyValueFactory<>("ageDiscountRate"));

        ticketPricesTable.getColumns().addAll(colMovieTitle, colBase, colDisc, colAgeDisc);

        basePriceField = new TextField();
        basePriceField.setPromptText("BasePrice");
        ageDiscountRateField = new TextField();
        ageDiscountRateField.setPromptText("AgeDiscount % (0~100)");

        updateTicketPriceButton = new Button("Update Selected");
        updateTicketPriceButton.setOnAction(e -> onUpdateTicketPricesClick());

        HBox box = new HBox(10, basePriceField, ageDiscountRateField, updateTicketPriceButton);
        ticketPricesTabPane.getChildren().addAll(ticketPricesTable, box);

        ticketPricesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            // Eğer farklı bir satıra basılırsa field'ları dolduralım veya sıfırlayalım
            if (newV != null) {
                // BasePrice
                basePriceField.setText(String.valueOf(newV.getBasePrice()));
                // AgeDiscountRate %100 formatında varsayalım => newV.getAgeDiscountRate()*100
                double ageRatePercent = newV.getAgeDiscountRate() * 100.0;
                ageDiscountRateField.setText(String.valueOf(ageRatePercent));
            } else {
                basePriceField.clear();
                ageDiscountRateField.clear();
            }
        });

        loadTicketPrices();
    }

    private void loadTicketPrices() {
        String sql = "SELECT tp.PriceID, tp.MovieID, m.Title as MovieTitle, " +
                     "tp.BasePrice, tp.DiscountedPrice, tp.AgeDiscountRate " +
                     "FROM TicketPrices tp " +
                     "JOIN Movies m ON tp.MovieID = m.MovieID";
                 
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            ObservableList<TicketPriceRow> data = FXCollections.observableArrayList();
            while (rs.next()) {
                data.add(new TicketPriceRow(
                    rs.getInt("PriceID"),
                    rs.getInt("MovieID"),
                    rs.getString("MovieTitle"),
                    rs.getDouble("BasePrice"),
                    rs.getDouble("DiscountedPrice"),
                    rs.getDouble("AgeDiscountRate")
                ));
            }
            ticketPricesTable.setItems(data);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void onUpdateTicketPricesClick() {
        TicketPriceRow selected = ticketPricesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Select a row to update!");
            return;
        }
        String bVal = basePriceField.getText().trim();
        String aVal = ageDiscountRateField.getText().trim();  // % cinsinden

        if (bVal.isEmpty() && aVal.isEmpty()) {
            showAlert("Warning", "Enter at least BasePrice or AgeDiscount!");
            return;
        }
        double newBase = selected.getBasePrice();
        double newAgeRate = selected.getAgeDiscountRate(); // 0~1 arası

        try {
            if (!bVal.isEmpty()) {
                newBase = Double.parseDouble(bVal);
                if (newBase < 0) {
                    showAlert("Error", "BasePrice cannot be negative!");
                    return;
                }
            }
            if (!aVal.isEmpty()) {
                double percent = Double.parseDouble(aVal);
                if (percent < 0 || percent > 100) {
                    showAlert("Error", "AgeDiscountRate must be between 0 and 100!");
                    return;
                }
                newAgeRate = percent / 100.0;  // DB'ye 0~1 olarak yazacağız
            }

            double newDiscounted = newBase * (1 - newAgeRate);

            String sql = "UPDATE TicketPrices SET BasePrice=?, AgeDiscountRate=?, DiscountedPrice=? WHERE PriceID=?";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDouble(1, newBase);
                stmt.setDouble(2, newAgeRate);
                stmt.setDouble(3, newDiscounted);
                stmt.setInt(4, selected.getPriceID());
                stmt.executeUpdate();
            }
            loadTicketPrices();
            showAlert("Info", "Ticket price updated.");

        } catch (NumberFormatException ex) {
            showAlert("Error", "Invalid number format!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "DB error updating ticket price.");
        }
    }

    // ------------------------------------------------------------
    // (4) Tax Rates Tab
    // ------------------------------------------------------------
    private void setupTaxRatesTab() {
        taxRatesTable = new TableView<>();
        TableColumn<TaxRateRow, String> colType = new TableColumn<>("TaxType");
        colType.setCellValueFactory(new PropertyValueFactory<>("taxType"));

        TableColumn<TaxRateRow, Double> colRate = new TableColumn<>("Rate(%)");
        colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));

        taxRatesTable.getColumns().addAll(colType, colRate);

        updateTaxButton = new Button("Update Selected Tax");
        updateTaxButton.setOnAction(e -> onUpdateTaxClick());

        taxRatesTabPane.getChildren().addAll(taxRatesTable, updateTaxButton);

        loadTaxRates();
    }

    private void loadTaxRates() {
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM TaxRates")) {
            ObservableList<TaxRateRow> data = FXCollections.observableArrayList();
            while (rs.next()) {
                data.add(new TaxRateRow(rs.getString("TaxType"), rs.getDouble("Rate")));
            }
            taxRatesTable.setItems(data);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void onUpdateTaxClick() {
        TaxRateRow selected = taxRatesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Select a tax row to update!");
            return;
        }
        TextInputDialog td = new TextInputDialog(String.valueOf(selected.getRate()));
        td.setHeaderText("Enter new tax rate (can be >100, but not negative):");
        var result = td.showAndWait();
        if (result.isPresent()) {
            String val = result.get().trim();
            try {
                double newRate = Double.parseDouble(val);
                if (newRate < 0) {
                    showAlert("Error", "Tax rate cannot be negative!");
                    return;
                }
                try (Connection conn = DBUtil.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE TaxRates SET Rate=? WHERE TaxType=?")) {
                    stmt.setDouble(1, newRate);
                    stmt.setString(2, selected.getTaxType());
                    stmt.executeUpdate();
                }
                loadTaxRates();
                showAlert("Info", "Tax rate updated.");
            } catch (NumberFormatException ex) {
                showAlert("Error", "Invalid number format!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Error", "DB error updating tax rate.");
            }
        }
    }

    // ------------------------------------------------------------
    // (5) Revenue & Tax Tab
    // ------------------------------------------------------------
    private void setupRevenueTab() {
        totalRevenueLabel = new Label("Total Revenue: 0");
        totalTaxLabel = new Label("Total Tax: 0");
        refreshRevenueButton = new Button("Refresh");
        refreshRevenueButton.setOnAction(e -> loadRevenueTax());

        revenueTabPane.getChildren().addAll(totalRevenueLabel, totalTaxLabel, refreshRevenueButton);
        loadRevenueTax();
    }

    private void loadRevenueTax() {
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT SUM(SubTotal) as S, SUM(TaxAmount) as T FROM Invoices")) {
            if (rs.next()) {
                double sumSub = rs.getDouble("S");
                double sumTax = rs.getDouble("T");
                totalRevenueLabel.setText(String.format("Total Revenue: %.2f TL", sumSub + sumTax));
                totalTaxLabel.setText(String.format("Total Tax: %.2f TL", sumTax));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // ------------------------------------------------------------
    // Ortak yardımcı metodlar & POJO’lar
    // ------------------------------------------------------------
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private boolean isUsernameUsed(String uname) {
        String sql = "SELECT COUNT(*) FROM Users WHERE LOWER(Username) = LOWER(?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, uname);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isValidRole(String role) {
        return role.equalsIgnoreCase("Manager")
            || role.equalsIgnoreCase("Cashier")
            || role.equalsIgnoreCase("Admin");
    }

    // POJO
    public static class ProductRow {
        private int productId;
        private String name;
        private double price;
        private int stockQuantity;
        public ProductRow(int pid, String n, double p, int s) {
            this.productId = pid; this.name = n; this.price = p; this.stockQuantity = s;
        }
        public int getProductId() { return productId; }
        public String getName() { return name; }
        public double getPrice() { return price; }
        public int getStockQuantity() { return stockQuantity; }
    }

    public static class UserRow {
        private int userId;
        private String firstName;
        private String lastName;
        private String username;
        private String password;
        private String role;
        public UserRow(int uid, String fn, String ln, String un, String pw, String r) {
            this.userId = uid; this.firstName = fn; this.lastName = ln;
            this.username = un; this.password = pw; this.role = r;
        }
        public int getUserId() { return userId; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public String getRole() { return role; }
    }

    public static class TicketPriceRow {
        private int priceID;
        private int movieID;
        private String movieTitle; // YENİ
        private double basePrice;
        private double discountedPrice;
        private double ageDiscountRate;

        public TicketPriceRow(int pid, int mid, String mtitle,
                              double base, double disc, double ageRate) {
            this.priceID = pid;
            this.movieID = mid;
            this.movieTitle = mtitle;
            this.basePrice = base;
            this.discountedPrice = disc;
            this.ageDiscountRate = ageRate;
        }
        public int getPriceID() { return priceID; }
        public int getMovieID() { return movieID; }
        public String getMovieTitle() { return movieTitle; }
        public double getBasePrice() { return basePrice; }
        public double getDiscountedPrice() { return discountedPrice; }
        public double getAgeDiscountRate() { return ageDiscountRate; }
    }

    // YENİ: TaxRateRow
    public static class TaxRateRow {
        private String taxType;
        private double rate;

        public TaxRateRow(String ttype, double r) {
            this.taxType = ttype;
            this.rate = r;
        }
        public String getTaxType() { return taxType; }
        public double getRate() { return rate; }
    }

    @FXML
    private void onLogoutClick() {
        try {
            // Login ekranını yükle
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CinemaCenter/login.fxml"));
            Scene scene = new Scene(loader.load());
            
            // Mevcut pencereyi al ve login ekranını göster
            Stage stage = (Stage) productsTabPane.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load login screen.");
        }
    }
}
