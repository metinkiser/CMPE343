package cinemaCenter;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class productCancellationController implements Initializable {

    @FXML private ComboBox<Product> productComboBox;
    @FXML private TextField productQtyField;
    @FXML private Label lblRefundAmount;

    private List<Product> productList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // DB'den tüm ürünleri çekip ComboBox'a ekleyelim
        productList = AdminDAO.getAllProducts(); 
        productComboBox.getItems().addAll(productList);

        // ComboBox'ta seçili ürünü metne çevirme
        productComboBox.setConverter(new ProductConverter(productComboBox));
    }

    @FXML
    void updateRefundAmount() {
        Product selected = productComboBox.getValue();
        if (selected == null) {
            lblRefundAmount.setText("0.00");
            return;
        }

        String qtyStr = productQtyField.getText().trim();
        if (qtyStr.isEmpty()) {
            lblRefundAmount.setText("0.00");
            return;
        }

        try {
            int qty = Integer.parseInt(qtyStr);
            if (qty < 0) {
                lblRefundAmount.setText("0.00");
                return;
            }
            double total = selected.getPrice() * qty;
            lblRefundAmount.setText(String.format("%.2f", total));
        } catch (NumberFormatException e) {
            lblRefundAmount.setText("0.00");
        }
    }

    @FXML
    void processProductCancellation(ActionEvent event) {
        Product selected = productComboBox.getValue();
        if (selected == null) {
            showAlert("Warning", "Please select a product to cancel.", Alert.AlertType.WARNING);
            return;
        }

        String qtyStr = productQtyField.getText().trim();
        if (qtyStr.isEmpty()) {
            showAlert("Warning", "Please enter a quantity to cancel.", Alert.AlertType.WARNING);
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(qtyStr);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid quantity format!", Alert.AlertType.ERROR);
            return;
        }

        if (qty <= 0) {
            showAlert("Warning", "Quantity must be > 0", Alert.AlertType.WARNING);
            return;
        }

        boolean success = AdminDAO.cancelProduct(selected.getProductID(), qty);
        if (success) {
            double refund = selected.getPrice() * qty;
            showAlert("Success",
                      "Product cancellation completed! Refund Amount = " + String.format("%.2f", refund),
                      Alert.AlertType.INFORMATION);
            closeWindow();
        } else {
            showAlert("Error", "Error or product not found / insufficient stock during cancellation.", Alert.AlertType.ERROR);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) productQtyField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
