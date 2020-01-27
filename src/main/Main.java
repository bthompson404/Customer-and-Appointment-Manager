
package main;

import java.io.IOException;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Blake Thompson
 */
public class Main extends Application {
    
    static Stage stage;
    
    @Override
    public void start(Stage stage) {
        
        this.stage = stage;

        ResourceBundle rb = ResourceBundle.getBundle("files/rb");
        Parent main = null;
        
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginMenu.fxml"));
            loader.setResources(rb);
            main = loader.load();
            
            Scene scene = new Scene(main);
            
            stage.setScene(scene);
            
            stage.show();
            
        } 
        catch (IOException ignored) {
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
