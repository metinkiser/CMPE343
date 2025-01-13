package CinemaCenter;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.*;
import javafx.stage.Stage;
import java.sql.*;
import java.util.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import javafx.scene.control.Alert;

// iText ile ilgili importlar (iText 7 core + layout)
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.geom.PageSize;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Controller class for managing customer details, ticket purchases and product sales.
 * Handles customer information forms, product selection, cart management and invoice generation.
 */
public class CustomerDetailsController implements DataInitializable {

    @FXML private Label movieInfoLabel;
    @FXML private Label sessionInfoLabel;
    @FXML private VBox customerFormsContainer;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private ListView<Product> productsListView;
    @FXML private Spinner<Integer> quantitySpinner;
    @FXML private ListView<String> cartListView;
    @FXML private Label subtotalLabel;
    @FXML private Label taxLabel;
    @FXML private Label totalLabel;
    
    private Session selectedSession;
    private Set<String> selectedSeats; // Seat numbers
    private List<CustomerForm> customerForms = new ArrayList<>();
    private ObservableList<String> cartItems = FXCollections.observableArrayList();
    private Map<String, Product> products = new HashMap<>();
    private double baseTicketPrice;
    private double subtotal = 0;
    private double tax = 0;
    
    // Satın alınan ürünleri takip etmek için Map (Ürün ve Miktar)
    private Map<Product, Integer> purchasedProducts = new HashMap<>();

    // EKLENDİ: AgeDiscountRate'i tablodan okuyacağız
    private double ageDiscountRate = 0.20; // default fallback

    private double ticketTaxRate = 0.20; // default fallback
    private double productTaxRate = 0.10; // default fallback

    // -------------------------------------------------------
    // İç sınıflar
    // -------------------------------------------------------
    
    /**
     * Inner class representing a customer form panel.
     * Contains input fields for customer details and seat information.
     */
    class CustomerForm extends GridPane {
        private TextField firstNameField;
        private TextField lastNameField;
        private TextField ageField;
        private CheckBox ageDiscountCheckBox;
        private String seatNumber;
        
        public CustomerForm(String seatNumber) {
            this.seatNumber = seatNumber;
            setupForm();
        }
        
        private void setupForm() {
            setHgap(10);
            setVgap(5);
            setPadding(new Insets(10));
            
            // Add form title
            Label titleLabel = new Label("Seat " + seatNumber);
            titleLabel.setStyle("-fx-font-weight: bold");
            add(titleLabel, 0, 0, 2, 1);
            
            // Add form fields
            add(new Label("First Name:"), 0, 1);
            firstNameField = new TextField();
            add(firstNameField, 1, 1);
            
            add(new Label("Last Name:"), 0, 2);
            lastNameField = new TextField();
            add(lastNameField, 1, 2);
            
            add(new Label("Age:"), 0, 3);
            ageField = new TextField();
            add(ageField, 1, 3);
            
            ageDiscountCheckBox = new CheckBox("Age Discount");
            add(ageDiscountCheckBox, 0, 4, 2, 1);
            
            // Yaş alanı için sayısal input validasyonu
            ageField.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal.matches("\\d*")) {
                    ageField.setText(newVal.replaceAll("[^\\d]", ""));
                } else if (!newVal.isEmpty()) {
                    try {
                        int age = Integer.parseInt(newVal);
                        if (age <= 0) {
                            ageField.setText(oldVal);
                        } else if (age > 150) {
                            ageField.setText(oldVal);
                        } else {
                            ageDiscountCheckBox.setSelected(age < 18 || age >= 65);
                            CustomerDetailsController.this.updateCart();
                        }
                    } catch (NumberFormatException e) {
                        ageField.setText(oldVal);
                    }
                }
            });
            
            ageDiscountCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> 
                CustomerDetailsController.this.updateCart());
            
            setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5;");
        }
        
        public boolean isValid() {
            if (firstNameField.getText().trim().isEmpty() || 
                lastNameField.getText().trim().isEmpty() || 
                ageField.getText().trim().isEmpty()) {
                return false;
            }
            
            try {
                int age = Integer.parseInt(ageField.getText().trim());
                return age > 0 && age < 150;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        
        public CustomerDetails getCustomerDetails() {
            return new CustomerDetails(
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                Integer.parseInt(ageField.getText().trim()),
                seatNumber,
                ageDiscountCheckBox.isSelected()
            );
        }
    }
    
    /**
     * Inner class representing customer details data structure.
     * Stores customer information including name, age and seat details.
     */
    class CustomerDetails {
        String firstName;
        String lastName;
        int age;
        String seatNumber;
        boolean hasAgeDiscount;
        
        public CustomerDetails(String firstName, String lastName, int age, 
                               String seatNumber, boolean hasAgeDiscount) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.seatNumber = seatNumber;
            this.hasAgeDiscount = hasAgeDiscount;
        }
    }
    
    /**
     * Inner class representing a product in the system.
     * Contains product information including price and stock details.
     */
    class Product {
        int id;
        String name;
        double price;
        int stock;
        String category;
        
        public Product(int id, String name, double price, int stock, String category) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.stock = stock;
            this.category = category;
        }
        
        @Override
        public String toString() {
            return String.format("%s - %.2f TL (Stock: %d)", name, price, stock);
        }
    }
    
    // -------------------------------------------------------
    // initialize
    // -------------------------------------------------------
    
    /**
     * Initializes the controller and sets up UI components.
     * Loads product categories and configures event listeners.
     */
    public void initialize() {
        cartListView.setItems(cartItems);
        
        // Kategori ComboBox'ını doldur
        loadProductCategories();
        
        // Spinner için maksimum değeri ayarla
        SpinnerValueFactory<Integer> valueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        quantitySpinner.setValueFactory(valueFactory);
        
        // Kategori seçildiğinde ürünleri yükle
        categoryComboBox.setOnAction(e -> {
            String selectedCategory = categoryComboBox.getValue();
            if (selectedCategory != null) {
                loadProductsByCategory(selectedCategory);
            }
        });
        
        loadTaxRates(); // Tax rate'leri yükle
    }
    
    private void loadProductCategories() {
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT Category FROM Products")) {
            
            ObservableList<String> categories = FXCollections.observableArrayList();
            while (rs.next()) {
                categories.add(rs.getString("Category"));
            }
            categoryComboBox.setItems(categories);
            
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load product categories.");
        }
    }

    // EKLENDİ: AgeDiscountRate'i load etme
    private void loadAgeDiscountRate() {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "SELECT AgeDiscountRate FROM TicketPrices WHERE MovieID=?")) {
            stmt.setInt(1, selectedSession.getMovie().getMovieID());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                this.ageDiscountRate = rs.getDouble("AgeDiscountRate");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // default 0.20 kalabilir
        }
    }
    
    /**
     * Sets the selected session, seats and base ticket price.
     * Initializes customer forms for each selected seat.
     *
     * @param session The selected movie session
     * @param seats Set of selected seat numbers
     * @param basePrice Base ticket price
     */
    public void setSessionAndSeats(Session session, Set<String> seats, double basePrice) {
        this.selectedSession = session;
        this.selectedSeats = seats;
        this.baseTicketPrice = basePrice;
        
        // EKLENDİ: AgeDiscountRate'i tablodan çekelim
        loadAgeDiscountRate();
        
        // Film ve seans bilgilerini göster
        movieInfoLabel.setText(session.getMovie().getTitle());
        sessionInfoLabel.setText(String.format("Hall: %s | Date: %s | Time: %s",
            session.getHall(), session.getDate(), session.getStartTime()));
        
        // Her koltuk için müşteri formu oluştur
        for (String seat : seats) {
            CustomerForm form = new CustomerForm(seat);
            customerForms.add(form);
            customerFormsContainer.getChildren().add(form);
        }
        
        // Sepeti güncelle
        updateCart();
    }
    
    private void loadProductsByCategory(String category) {
        productsListView.getItems().clear();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM Products WHERE Category = ? AND StockQuantity > 0")) {
            
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();
            
            ObservableList<Product> products = FXCollections.observableArrayList();
            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("ProductID"),
                    rs.getString("Name"),
                    rs.getDouble("Price"),
                    rs.getInt("StockQuantity"),
                    category
                );
                products.add(product);
            }
            productsListView.setItems(products);
            
            // Ürün seçildiğinde spinner maksimum değerini güncelle
            productsListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        SpinnerValueFactory<Integer> vf = 
                            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, newVal.stock, 1);
                        quantitySpinner.setValueFactory(vf);
                    }
                });
            
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load products.");
        }
    }
    
    /**
     * Handles adding selected products to the shopping cart.
     * Performs stock validation and updates cart display.
     */
    @FXML
    private void onAddToCartClick() {
        Product selected = productsListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Uyarı", "Lütfen bir ürün seçin.");
            return;
        }
        
        int quantity = quantitySpinner.getValue();
        
        // Mevcut sepetteki miktar kontrolü
        int currentQuantity = purchasedProducts.getOrDefault(selected, 0);
        int totalQuantity = currentQuantity + quantity;
        
        // Stok kontrolü
        if (totalQuantity > selected.stock) {
            showAlert(Alert.AlertType.WARNING, "Uyarı", 
                String.format("Yeterli stok yok. Mevcut stok: %d, Sepetteki miktar: %d", 
                    selected.stock, currentQuantity));
            return;
        }
        
        // Database'deki stok kontrolü
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT StockQuantity FROM Products WHERE ProductID = ?")) {
            
            stmt.setInt(1, selected.id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int dbStock = rs.getInt("StockQuantity");
                if (totalQuantity > dbStock) {
                    showAlert(Alert.AlertType.WARNING, "Uyarı", 
                        "Bu ürün için yeterli stok bulunmamaktadır.");
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Hata", "Stok kontrolü yapılırken bir hata oluştu.");
            return;
        }
        
        // Ürünü sepete ekle
        purchasedProducts.put(selected, totalQuantity);
        
        // Sepet görünümünü güncelle
        updateCartView(selected, quantity);
        
        // Ürün stoğunu güncelle
        selected.stock -= quantity;
        if (selected.stock == 0) {
            productsListView.getItems().remove(selected);
        } else {
            productsListView.refresh();
            SpinnerValueFactory<Integer> vf = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, selected.stock, 1);
            quantitySpinner.setValueFactory(vf);
        }
    }
    
    private void updateCartView(Product product, int quantity) {
        cartItems.removeIf(item -> item.startsWith(product.name));
        
        int totalQuantity = purchasedProducts.get(product);
        double totalPrice = product.price * totalQuantity;
        
        String cartItem = String.format("%s x%d - %.2f TL", 
            product.name, totalQuantity, totalPrice);
        cartItems.add(cartItem);
        
        updateTotals();
    }
    
    private void updateTotals() {
        subtotal = 0;
        tax = 0;
        
        // Bilet fiyatlarını hesapla
        for (CustomerForm form : customerForms) {
            double ticketPrice = baseTicketPrice;
            if (form.ageDiscountCheckBox.isSelected()) {
                ticketPrice = baseTicketPrice * (1 - ageDiscountRate);
            }
            subtotal += ticketPrice;
            tax += ticketPrice * ticketTaxRate; // Bilet vergisi
        }
        
        // Ürün fiyatlarını ekle
        for (Map.Entry<Product, Integer> entry : purchasedProducts.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double productPrice = product.price * quantity;
            subtotal += productPrice;
            tax += productPrice * productTaxRate; // Ürün vergisi
        }
        
        subtotalLabel.setText(String.format("%.2f TL", subtotal));
        taxLabel.setText(String.format("%.2f TL", tax));
        totalLabel.setText(String.format("%.2f TL", subtotal + tax));
    }
    
    @FXML
    private void onBackButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SeatSelection.fxml"));
            Parent root = loader.load();
            
            SeatSelectionController controller = loader.getController();
            controller.setSelectedSession(selectedSession);
            
            Stage stage = new Stage();
            stage.setTitle("Koltuk Seçimi - " + selectedSession.getMovie().getTitle());
            stage.setScene(new Scene(root));
            stage.show();
            
            closeCurrentWindow();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Hata", "Koltuk seçimi ekranına dönülemedi.");
        }
    }
    
    @FXML
    private void onConfirmButtonClick() {
        for (CustomerForm form : customerForms) {
            if (!form.isValid()) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please fill in all customer details.");
                return;
            }
        }
        List<CustomerDetails> customers = new ArrayList<>();
        for (CustomerForm form : customerForms) {
            customers.add(form.getCustomerDetails());
        }
        saveToDatabase(customers);
    }
    
    // -------------------------------------------------------
    // Veritabanı kaydetme + Fatura oluşturma
    // -------------------------------------------------------
    
    /**
     * Saves purchase information to database and generates invoice.
     * Creates records for tickets, customers and product sales.
     *
     * @param customers List of customer details to be saved
     */
    private void saveToDatabase(List<CustomerDetails> customers) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            String ticketSQL = "INSERT INTO Tickets (SessionID, SeatNumber, SeatID, CustomerID, "
                             + "FirstName, LastName, DiscountApplied, Price, AgeDiscountApplied, HasDiscount) "
                             + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String customerSQL = "INSERT INTO Customers (FirstName, LastName, Age) VALUES (?, ?, ?)";
            String salesSQL    = "INSERT INTO Sales (TicketID, ProductID, Quantity, TotalPrice, SubTotal, TaxAmount) "
                               + "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement customerStmt = conn.prepareStatement(customerSQL, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement ticketStmt   = conn.prepareStatement(ticketSQL, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement salesStmt    = conn.prepareStatement(salesSQL);

            List<Integer> createdTicketIds = new ArrayList<>();
            for (CustomerDetails c : customers) {
                // Customers tablosu
                customerStmt.setString(1, c.firstName);
                customerStmt.setString(2, c.lastName);
                customerStmt.setInt   (3, c.age);
                customerStmt.executeUpdate();

                ResultSet customerRs = customerStmt.getGeneratedKeys();
                int customerId = -1;
                if (customerRs.next()) {
                    customerId = customerRs.getInt(1);
                }

                // seatID bul
                int seatID = getSeatID(conn, selectedSession.getHall(), c.seatNumber);

                // AgeDiscountRate'e göre bilet fiyatı
                double ticketPrice = baseTicketPrice;
                if (c.hasAgeDiscount) {
                    ticketPrice = baseTicketPrice * (1 - ageDiscountRate);
                }

                ticketStmt.setInt   (1, selectedSession.getSessionID());
                ticketStmt.setString(2, c.seatNumber);
                ticketStmt.setInt   (3, seatID);
                ticketStmt.setInt   (4, customerId);
                ticketStmt.setString(5, c.firstName);
                ticketStmt.setString(6, c.lastName);
                ticketStmt.setBoolean(7, c.hasAgeDiscount);
                ticketStmt.setDouble(8, ticketPrice);
                ticketStmt.setBoolean(9, c.hasAgeDiscount);
                ticketStmt.setBoolean(10, c.hasAgeDiscount);

                ticketStmt.executeUpdate();
                ResultSet ticketRs = ticketStmt.getGeneratedKeys();
                int ticketId = -1;
                if (ticketRs.next()) {
                    ticketId = ticketRs.getInt(1);
                    createdTicketIds.add(ticketId);
                }

                // Sales
                for (Map.Entry<Product, Integer> entry : purchasedProducts.entrySet()) {
                    Product product = entry.getKey();
                    int quantity = entry.getValue();
                    double subPrice = product.price * quantity;
                    double subTax   = subPrice * 0.08;

                    salesStmt.setInt   (1, ticketId);
                    salesStmt.setInt   (2, product.id);
                    salesStmt.setInt   (3, quantity);
                    salesStmt.setDouble(4, subPrice + subTax);
                    salesStmt.setDouble(5, subPrice);
                    salesStmt.setDouble(6, subTax);
                    salesStmt.executeUpdate();

                    updateProductStock(conn, product.id, quantity);
                }
                updateVacantSeats(conn, selectedSession.getSessionID(), selectedSeats.size());
            }

            conn.commit();
            generateAndSaveInvoice(customers, createdTicketIds);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Purchase completed successfully!");
            goBackToCashierScreen();

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not complete the purchase.");
        }
    }

    private int getSeatID(Connection conn, String hall, String seatNumber) throws SQLException {
        String seatQuery = "SELECT SeatID FROM Seats WHERE HallID=? AND SeatNumber=?";
        try (PreparedStatement stmt = conn.prepareStatement(seatQuery)) {
            stmt.setString(1, hall);
            stmt.setString(2, seatNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("SeatID");
            }
        }
        return -1; 
    }
    
    /**
     * Generates PDF invoice and saves it to both file system and database.
     *
     * @param customers List of customers included in purchase
     * @param ticketIds List of generated ticket IDs
     */
    private void generateAndSaveInvoice(List<CustomerDetails> customers, List<Integer> ticketIds) {
        double totalAmount = subtotal + tax;
        double subTotal = subtotal;
        double taxAmount = tax;
        
        // PDF'i byte array olarak oluştur
        byte[] pdfData = createInvoicePdf(customers, ticketIds, subTotal, taxAmount, totalAmount);
        
        long timestamp = System.currentTimeMillis();
        String invoiceFileName = "Invoice_" + timestamp + ".pdf";
        String invoiceFilePath = "C:\\invoices\\" + invoiceFileName;
        
        // Dizini oluştur
        try {
            Path dirPath = Paths.get("C:\\invoices");
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not create invoices directory.");
            return;
        }
        
        // Dosyaya kaydet
        try (FileOutputStream fos = new FileOutputStream(invoiceFilePath)) {
            fos.write(pdfData);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not write invoice PDF to disk.");
        }
        
        // Veritabanına kaydet
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "INSERT INTO Invoices (CustomerID, SaleDateTime, TotalAmount, SubTotal, " +
                        "TaxAmount, InvoicePath, InvoiceContent, PaymentMethod, InvoiceNumber) " +
                        "VALUES (?, NOW(), ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            int firstCustomerId = findFirstCustomerId(customers, conn);
            
            stmt.setInt(1, firstCustomerId);
            stmt.setDouble(2, totalAmount);
            stmt.setDouble(3, subTotal);
            stmt.setDouble(4, taxAmount);
            stmt.setString(5, invoiceFilePath);
            stmt.setBytes(6, pdfData);  // PDF içeriğini binary olarak kaydet
            stmt.setString(7, "Cash");
            stmt.setString(8, "INV-" + timestamp);
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not save invoice to DB.");
        }
    }
    
    /**
     * Creates PDF document containing invoice details.
     *
     * @param customers List of customers
     * @param ticketIds List of ticket IDs
     * @param subTotal Purchase subtotal
     * @param taxAmount Total tax amount
     * @param totalAmount Final total amount
     * @return PDF document as byte array
     */
    private byte[] createInvoicePdf(List<CustomerDetails> customers, 
                                  List<Integer> ticketIds,
                                  double subTotal, 
                                  double taxAmount, 
                                  double totalAmount) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            PdfWriter writer   = new PdfWriter(bos);
            PdfDocument pdf    = new PdfDocument(writer);
            Document document  = new Document(pdf, PageSize.A4);
            
            document.add(new Paragraph("INVOICE / FATURA").setFontSize(16).setBold());
            document.add(new Paragraph("Date: " + new java.util.Date().toString()).setFontSize(10));
            document.add(new Paragraph(" "));
            
            // Film ve salon bilgileri
            document.add(new Paragraph("Movie Details / Film Detayları:").setBold());
            document.add(new Paragraph(String.format("Movie / Film: %s", selectedSession.getMovie().getTitle())));
            document.add(new Paragraph(String.format("Hall / Salon: %s", selectedSession.getHall())));
            document.add(new Paragraph(String.format("Date / Tarih: %s", selectedSession.getDate())));
            document.add(new Paragraph(String.format("Time / Saat: %s", selectedSession.getStartTime())));
            document.add(new Paragraph(" "));
            
            // Bilet detayları
            document.add(new Paragraph("Ticket Details / Bilet Detayları:").setBold());
            for (CustomerDetails c : customers) {
                double ticketPrice = baseTicketPrice;
                if (c.hasAgeDiscount) {
                    ticketPrice = baseTicketPrice * (1 - ageDiscountRate);
                }
                
                document.add(new Paragraph(String.format(
                    "Customer / Müşteri: %s %s\n" +
                    "Seat / Koltuk: %s\n" +
                    "Ticket Price / Bilet Fiyatı: %.2f TL%s",
                    c.firstName, c.lastName,
                    c.seatNumber,
                    ticketPrice,
                    c.hasAgeDiscount ? " (Age Discount / Yaş İndirimi)" : ""
                )));
                document.add(new Paragraph(" "));
            }
            
            // Ürün detayları
            if (!purchasedProducts.isEmpty()) {
                document.add(new Paragraph("Products / Ürünler:").setBold());
                for (Map.Entry<Product, Integer> entry : purchasedProducts.entrySet()) {
                    Product p = entry.getKey();
                    int qty   = entry.getValue();
                    double lineTotal = p.price * qty;
                    document.add(new Paragraph(
                        String.format("- %s x%d = %.2f TL", p.name, qty, lineTotal)));
                }
                document.add(new Paragraph(" "));
            }
            
            // Toplam detayları
            document.add(new Paragraph("Payment Details / Ödeme Detayları:").setBold());
            document.add(new Paragraph(String.format("Subtotal / Ara Toplam: %.2f TL", subTotal)));
            document.add(new Paragraph(String.format("Tax / KDV: %.2f TL", taxAmount)));
            document.add(new Paragraph(String.format("Total / Toplam: %.2f TL", totalAmount)));
            
            document.close();
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private int findFirstCustomerId(List<CustomerDetails> customers, Connection conn) {
        if (customers.isEmpty()) return -1;
        CustomerDetails first = customers.get(0);
        String sql = "SELECT CustomerID FROM Customers WHERE FirstName=? AND LastName=? ORDER BY CustomerID DESC LIMIT 1";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, first.firstName);
            st.setString(2, first.lastName);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt("CustomerID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    private void updateProductStock(Connection conn, int productId, int quantity) throws SQLException {
        String updateSQL = "UPDATE Products SET StockQuantity = StockQuantity - ? WHERE ProductID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        }
    }
    
    @SuppressWarnings("unused")
    private void updateVacantSeats(Connection conn, int sessionId, int soldCount) throws SQLException {
        String sql = "UPDATE Sessions SET VacantSeats = VacantSeats - ? WHERE SessionID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, soldCount);
            stmt.setInt(2, sessionId);
            stmt.executeUpdate();
        }
    }
    
    private void closeCurrentWindow() {
        ((Stage) movieInfoLabel.getScene().getWindow()).close();
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void goBackToCashierScreen() {
        NavigationUtil.loadScene("/CinemaCenter/cashier_operations.fxml", "Search Movies");
        closeCurrentWindow();
    }

    private void updateCart() {
        cartItems.clear();
        subtotal = 0;
        
        for (CustomerForm form : customerForms) {
            double ticketPrice = baseTicketPrice;
            if (form.ageDiscountCheckBox.isSelected()) {
                ticketPrice = baseTicketPrice * (1 - ageDiscountRate); 
            }
            String cartItem = String.format("Seat %s - %.2f TL", form.seatNumber, ticketPrice);
            cartItems.add(cartItem);
            subtotal += ticketPrice;
        }
        
        for (Map.Entry<Product, Integer> entry : purchasedProducts.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double lineTotal = product.price * quantity;
            
            String cartItem = String.format("%s x%d - %.2f TL", product.name, quantity, lineTotal);
            cartItems.add(cartItem);
        }
        
        updateTotals();
    }
    
    @Override
    public void initData(Object data) {
        if (data instanceof Object[]) {
            Object[] params = (Object[]) data;
            if (params.length == 3 && params[0] instanceof Session && params[1] instanceof Set && params[2] instanceof Double) {
                Session session = (Session) params[0];
                @SuppressWarnings("unchecked")
                Set<String> seats = (Set<String>) params[1];
                double basePrice = (Double) params[2];
                setSessionAndSeats(session, seats, basePrice);
            }
        }
    }

    private void loadTaxRates() {
        try (Connection conn = DBUtil.getConnection()) {
            // Bilet vergisi
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT Rate FROM TaxRates WHERE TaxType = 'MovieTicket'");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ticketTaxRate = rs.getDouble("Rate") / 100.0;
            }

            // Ürün vergisi
            stmt = conn.prepareStatement(
                "SELECT Rate FROM TaxRates WHERE TaxType = 'Food'");
            rs = stmt.executeQuery();
            if (rs.next()) {
                productTaxRate = rs.getDouble("Rate") / 100.0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // default değerler kalacak
        }
    }

    public byte[] getInvoiceFromDatabase(String invoiceNumber) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT InvoiceContent FROM Invoices WHERE InvoiceNumber = ?")) {
            
            stmt.setString(1, invoiceNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBytes("InvoiceContent");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
