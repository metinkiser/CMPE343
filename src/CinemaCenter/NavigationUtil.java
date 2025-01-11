package CinemaCenter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NavigationUtil {
    public static <T> T loadScene(String fxmlPath, String title) {
        return loadScene(fxmlPath, title, null);
    }

    public static <T> T loadScene(String fxmlPath, String title, Object data) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(fxmlPath));
            Parent root = loader.load();
            
            T controller = loader.getController();
            
            // Controller'ın data setter metodunu çağır
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
