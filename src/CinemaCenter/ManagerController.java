package CinemaCenter;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.*;

public class ManagerController {

    @FXML private VBox productsTabPane;
    @FXML private VBox personnelTabPane;
    @FXML private VBox ticketPricesTabPane;
    @FXML private VBox revenueTabPane;

    // Manager'ın userID'sini saklayacağız (kendini kovamaması için)
    private int managerUserId;

    // Setleyici (LoginController'dan çağırabilirsiniz)
    public void setManagerUserId(int id) {
        this.managerUserId = id;
    }

    // -------------------------
    // 1) ÜRÜN ENVANTERİ
    // -------------------------
    private TableView<ProductRow> productsTable;
    private TextField stockTextField;
    private Button updateStockButton;

    private TextField priceTextField;
    private Button updatePriceButton;

    // -------------------------
    // 2) PERSONEL YÖNETİMİ (Users tablosu)
    // -------------------------
    private TableView<UserRow> usersTable;
    private Button hireButton, fireButton, editButton;

    // -------------------------
    // 3) BİLET & İNDİRİM FİYATLARI
    // -------------------------
    private TableView<TicketPriceRow> ticketPricesTable;
    private Button updateTicketPriceButton;
    private TextField basePriceField;
    private TextField ageDiscountRateField;

    // -------------------------
    // 4) GELİR & VERGİ
    // -------------------------
    private Label totalRevenueLabel;
    private Label totalTaxLabel;
    private Button refreshRevenueButton;

    @FXML
    public void initialize() {
        setupProductsTab();
        setupPersonnelTab();
        setupTicketPricesTab();
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
        ProductRow selected = productsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a product");
            return;
        }
        String stockVal = stockTextField.getText().trim();
        if (stockVal.isEmpty()) {
            showAlert("Warning", "Please enter a stock value.");
            return;
        }
        try {
            int delta = Integer.parseInt(stockVal);
            int updatedValue = selected.getStockQuantity() + delta;
            if (updatedValue < 0) {
                showAlert("Error", "Stock cannot be negative!");
                return;
            }
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE Products SET StockQuantity=? WHERE ProductID=?")) {
                stmt.setInt(1, updatedValue);
                stmt.setInt(2, selected.getProductId());
                stmt.executeUpdate();
            }
            loadProducts();
            showAlert("Info", "Stock updated successfully.");
        } catch (NumberFormatException ex) {
            showAlert("Error", "Invalid number format for stock!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Could not update stock.");
        }
    }

    private void onUpdatePriceClick() {
        ProductRow selected = productsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a product");
            return;
        }
        String priceVal = priceTextField.getText().trim();
        if (priceVal.isEmpty()) {
            showAlert("Warning", "Please enter a price.");
            return;
        }
        try {
            double newPrice = Double.parseDouble(priceVal);
            if (newPrice < 0) {
                showAlert("Error", "Price cannot be negative!");
                return;
            }
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE Products SET Price=? WHERE ProductID=?")) {
                stmt.setDouble(1, newPrice);
                stmt.setInt(2, selected.getProductId());
                stmt.executeUpdate();
            }
            loadProducts();
            showAlert("Info", "Price updated successfully.");
        } catch (NumberFormatException ex) {
            showAlert("Error", "Invalid number format for price!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Could not update price.");
        }
    }

    // ------------------------------------------------------------
    // (2) Personnel (Users tablosu)
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
        hireButton.setOnAction(e -> onHireUser());

        fireButton = new Button("Fire");
        fireButton.setOnAction(e -> onFireUser());

        editButton = new Button("Edit");
        editButton.setOnAction(e -> onEditUser());

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

    private void onHireUser() {
        TextInputDialog td = new TextInputDialog();
        td.setHeaderText("Enter new user info (format: firstName,lastName,username,password,role)");
        Optional<String> result = td.showAndWait();
        if (result.isPresent()) {
            String[] arr = result.get().split(",");
            if (arr.length < 5) {
                showAlert("Error", "Please enter 5 fields!");
                return;
            }
            String fName = arr[0].trim();
            String lName = arr[1].trim();
            String uname = arr[2].trim();
            String pass  = arr[3].trim();
            String role  = arr[4].trim();

            // ROL VALIDASYON
            if (!isValidRole(role)) {
                showAlert("Error", "Role must be one of: Manager, Cashier, Admin");
                return;
            }

            // USERNAME case-insensitive check
            if (isUsernameUsed(uname)) {
                showAlert("Error", "Username is already used!");
                return;
            }

            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Users (FirstName, LastName, Username, Password, Role) VALUES (?,?,?,?,?)")) {
                stmt.setString(1, fName);
                stmt.setString(2, lName);
                stmt.setString(3, uname);
                stmt.setString(4, pass);
                stmt.setString(5, role);
                stmt.executeUpdate();
                loadUsers();
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Error", "Could not hire user.");
            }
        }
    }

    private void onFireUser() {
        UserRow selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Select a user to fire!");
            return;
        }
        // Manager kendini kovamasın
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

    private void onEditUser() {
        UserRow selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Select a user to edit!");
            return;
        }
        TextInputDialog td = new TextInputDialog(String.format("%s,%s,%s,%s,%s",
            selected.getFirstName(), selected.getLastName(), selected.getUsername(), 
            selected.getPassword(), selected.getRole()));
        td.setHeaderText("Edit user (format: firstName,lastName,username,password,role)");
        Optional<String> result = td.showAndWait();
        if (result.isPresent()) {
            String[] arr = result.get().split(",");
            if (arr.length < 5) {
                showAlert("Error", "Please enter 5 fields!");
                return;
            }
            String fName = arr[0].trim();
            String lName = arr[1].trim();
            String uname = arr[2].trim();
            String pass  = arr[3].trim();
            String role  = arr[4].trim();

            if (!isValidRole(role)) {
                showAlert("Error", "Role must be one of: Manager, Cashier, Admin");
                return;
            }

            // username case-insensitive check
            // ama eğer selected oldUserName ile ayni ise problem değil
            // ama farkli bir username girmişse check
            if (!uname.equalsIgnoreCase(selected.getUsername()) && isUsernameUsed(uname)) {
                showAlert("Error", "Username is already used!");
                return;
            }

            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE Users SET FirstName=?, LastName=?, Username=?, Password=?, Role=? WHERE UserID=?")) {
                stmt.setString(1, fName);
                stmt.setString(2, lName);
                stmt.setString(3, uname);
                stmt.setString(4, pass);
                stmt.setString(5, role);
                stmt.setInt(6, selected.getUserId());
                stmt.executeUpdate();
                loadUsers();
                showAlert("Info", "User updated.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Error", "Could not edit user.");
            }
        }
    }

    // username’in kullanılıp kullanılmadığını case-insensitive kontrol
    private boolean isUsernameUsed(String uname) {
        // Tüm user tablosunda LOWER(Username)=LOWER(?) var mı?
        String sql = "SELECT COUNT(*) FROM Users WHERE LOWER(Username) = LOWER(?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, uname);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return (count > 0);
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

    // ------------------------------------------------------------
    // (3) Ticket Prices & Discount Tab
    // ------------------------------------------------------------
    private void setupTicketPricesTab() {
        ticketPricesTable = new TableView<>();

        TableColumn<TicketPriceRow, Integer> colPriceId = new TableColumn<>("PriceID");
        colPriceId.setCellValueFactory(new PropertyValueFactory<>("priceID"));

        TableColumn<TicketPriceRow, Integer> colMovieId = new TableColumn<>("MovieID");
        colMovieId.setCellValueFactory(new PropertyValueFactory<>("movieID"));

        TableColumn<TicketPriceRow, Double> colBase = new TableColumn<>("BasePrice");
        colBase.setCellValueFactory(new PropertyValueFactory<>("basePrice"));

        TableColumn<TicketPriceRow, Double> colDisc = new TableColumn<>("DiscountedPrice");
        colDisc.setCellValueFactory(new PropertyValueFactory<>("discountedPrice"));

        TableColumn<TicketPriceRow, Double> colAgeDisc = new TableColumn<>("AgeDiscountRate");
        colAgeDisc.setCellValueFactory(new PropertyValueFactory<>("ageDiscountRate"));

        ticketPricesTable.getColumns().addAll(colPriceId, colMovieId, colBase, colDisc, colAgeDisc);

        basePriceField = new TextField();
        basePriceField.setPromptText("BasePrice");
        ageDiscountRateField = new TextField();
        ageDiscountRateField.setPromptText("AgeDiscountRate (0~1)");

        updateTicketPriceButton = new Button("Update Selected");
        updateTicketPriceButton.setOnAction(e -> onUpdateTicketPricesClick());

        HBox box = new HBox(10, basePriceField, ageDiscountRateField, updateTicketPriceButton);
        ticketPricesTabPane.getChildren().addAll(ticketPricesTable, box);

        loadTicketPrices();
    }

    private void loadTicketPrices() {
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM TicketPrices")) {
            ObservableList<TicketPriceRow> data = FXCollections.observableArrayList();
            while (rs.next()) {
                data.add(new TicketPriceRow(
                    rs.getInt("PriceID"),
                    rs.getInt("MovieID"),
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

    // Sadece BasePrice ve AgeDiscountRate alıyoruz, DiscountedPrice otomatik hesaplıyoruz
    private void onUpdateTicketPricesClick() {
        TicketPriceRow selected = ticketPricesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Select a row to update.");
            return;
        }
        String bVal = basePriceField.getText().trim();
        String aVal = ageDiscountRateField.getText().trim();

        if (bVal.isEmpty() && aVal.isEmpty()) {
            showAlert("Warning", "Enter at least one field (BasePrice or AgeDiscountRate) to update!");
            return;
        }

        try {
            double newBase = selected.getBasePrice();
            if (!bVal.isEmpty()) {
                newBase = Double.parseDouble(bVal);
                if (newBase < 0) {
                    showAlert("Error", "BasePrice cannot be negative!");
                    return;
                }
            }
            double newAgeRate = selected.getAgeDiscountRate();
            if (!aVal.isEmpty()) {
                newAgeRate = Double.parseDouble(aVal);
                if (newAgeRate < 0 || newAgeRate > 1) {
                    showAlert("Error", "AgeDiscountRate must be between 0 and 1!");
                    return;
                }
            }
            // DiscountedPrice = newBase * (1 - newAgeRate)
            double newDiscounted = newBase * (1 - newAgeRate);

            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE TicketPrices SET BasePrice=?, AgeDiscountRate=?, DiscountedPrice=? WHERE PriceID=?")) {
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
    // (4) Revenue & Tax Tab
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
    // Yardımcı metodlar & POJO’lar
    // ------------------------------------------------------------
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

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
        private double basePrice;
        private double discountedPrice;
        private double ageDiscountRate;
        public TicketPriceRow(int pid, int mid, double b, double d, double a) {
            this.priceID = pid; this.movieID = mid;
            this.basePrice = b; this.discountedPrice = d; this.ageDiscountRate = a;
        }
        public int getPriceID() { return priceID; }
        public int getMovieID() { return movieID; }
        public double getBasePrice() { return basePrice; }
        public double getDiscountedPrice() { return discountedPrice; }
        public double getAgeDiscountRate() { return ageDiscountRate; }
    }
}
