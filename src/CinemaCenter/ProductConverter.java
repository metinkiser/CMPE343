package cinemaCenter;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 * A converter for Product objects so the ComboBox displays only the product name.
 */
public class ProductConverter extends StringConverter<Product> {

    private ComboBox<Product> comboBox;

    /**
     * Constructor that receives the ComboBox reference.
     * @param comboBox The ComboBox<Product>
     */
    public ProductConverter(ComboBox<Product> comboBox) {
        this.comboBox = comboBox;
    }

    @Override
    public String toString(Product object) {
        if (object == null) return "";
        return object.getName();
    }

    @Override
    public Product fromString(String string) {
        return comboBox.getItems().stream()
                .filter(p -> p.getName().equals(string))
                .findFirst()
                .orElse(null);
    }
}
