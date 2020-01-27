
package controllers;

import implement.DatabaseReadWrite;
import java.io.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utilities.DBConnection;
import utilities.DateTimeLocale;

/**
 *
 * @author Blake Thompson
 */
public class LoginMenuController implements Initializable {
    Stage stage;
    Parent scene;

    public static String currentUser;
    public static final String logfile = "src/files/logfile.txt";
    FileWriter fwriter;
    PrintWriter writeLog;
    Alert alert;
    int loginAttempts = 0;
    ResourceBundle rb;
    
    @FXML
    private Label usernameLabel;

    @FXML
    private TextField usernameText;

    @FXML
    private Label passwordLabel;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Button submitButton;

    @FXML
    private Button quitButton;

    @FXML
    void onActionExitProgram(ActionEvent event) throws SQLException, Exception {
        DBConnection.closeConnection();
        System.exit(0);
    }

    @FXML
    void onActionLogin(ActionEvent event) throws SQLException, IOException, Exception {
        // loop to the top of the list if not already there
        try (ResultSet userList = DatabaseReadWrite.getAllRecords("user")) {
            // loop to the top of the list if not already there
            userList.beforeFirst();
            // scan through user table results
            while (userList.next()) {
                // look for a username and password match, go to MainMenu.fxml if match
                if (usernameText.getText().equals(userList.getString("userName"))
                        && passwordText.getText().equals(userList.getString("password"))) {
                    writeLog.println("Login by " + usernameText.getText() + " " + DateTimeLocale.getCurrentUTC() + " UTC");
                    writeLog.close();
                    
                    MainMenuController.loginFlag = true;
                    currentUser = usernameText.getText();
                    
                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
                // display incorrect login info and reset text fields
                else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(rb.getString("denied"));
                    alert.setContentText(rb.getString("incorrect"));
                    alert.showAndWait();
                }
                
                loginAttempts++;
                usernameText.setText("");
                passwordText.setText("");
            }
        }
        
        // check if this is the third failed attempt, lockout if so
        if (loginAttempts == 3) {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(rb.getString("locked"));
            alert.setContentText(rb.getString("attempts"));
            alert.showAndWait();
            
            writeLog.println("System locked from " + DateTimeLocale.myLocale.getDisplayCountry()
                    + " " + DateTimeLocale.getCurrentUTC() + " UTC");
            writeLog.close();
            
            DBConnection.closeConnection();
            System.exit(0);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // use ResourceBundle for login messages
        this.rb = rb;
        
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        submitButton.setText(rb.getString("submit"));
        quitButton.setText(rb.getString("quit"));
        
        // create new file object
        try {
            File file = new File(logfile);
            fwriter = new FileWriter(file, true);    
            writeLog = new PrintWriter(fwriter);
        }
        catch (IOException ex) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("file"));
            alert.setContentText(rb.getString("error") + ex.getMessage());
            alert.showAndWait();
        }
        
        // connect to the database
        try {    
            DBConnection.makeConnection();
        } catch (Exception ex) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("unreachable"));
            alert.setContentText(rb.getString("error") + ex.getMessage());
            alert.showAndWait();

            System.exit(0);
        }
    }
}
