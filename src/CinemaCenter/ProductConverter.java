package cinemaCenter;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class ProductConverter extends StringConverter<Product> {

    private ComboBox<Product> comboBox;

    public ProductConverter(ComboBox<Product> comboBox) {
        this.comboBox = comboBox;
    }

    @Override
    public String toString(Product object) {
        if (object == null) return "";
        return object.getName(); 
        // İsterseniz: return object.getName() + " - " + object.getPrice() + "₺";
    }

    @Override
    public Product fromString(String string) {
        // Normalde pek kullanılmaz, 
        // comboBox selection üzerinden setValue yapılıyor
        return comboBox.getItems().stream()
                .filter(p -> p.getName().equals(string))
                .findFirst()
                .orElse(null);
    }
}
