package CinemaCenter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Utility class for managing scene loading and navigation in JavaFX application.
 */
public class NavigationUtil {
    
    /**
     * Loads a new scene from the specified FXML file.
     *
     * @param fxmlPath Path to the FXML file to be loaded
     * @param title Window title for the scene
     * @param <T> Type of the controller class
     * @return Controller instance of the loaded scene
     */
    public static <T> T loadScene(String fxmlPath, String title) {
        return loadScene(fxmlPath, title, null);
    }

    /**
     * Loads a new scene from the specified FXML file and initializes the controller with data.
     *
     * @param fxmlPath Path to the FXML file to be loaded
     * @param title Window title for the scene
     * @param data Data to be passed to the controller
     * @param <T> Type of the controller class
     * @return Controller instance of the loaded scene
     */
    public static <T> T loadScene(String fxmlPath, String title, Object data) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(fxmlPath));
            Parent root = loader.load();
            
            T controller = loader.getController();
            
            if (data != null && controller instanceof DataInitializable) {
                ((DataInitializable) controller).initData(data);
            }
            
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
            
            return controller;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
