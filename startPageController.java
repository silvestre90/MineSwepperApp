import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class startPageController implements Initializable{

    @FXML
    private ComboBox<level> cmbDifficulty;
    @FXML
    private ComboBox<gridSize> cmbGrid;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnResults;
    @FXML
    private Button btnExit;
    @FXML
    private Label lblError;

    public static String levelValue;
    public static String gridValue;

    Connection connection;

    private static final String defaultValue = "Open List";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.cmbDifficulty.setItems(FXCollections.observableArrayList(level.values()));
        this.cmbGrid.setItems(FXCollections.observableArrayList(gridSize.values()));
    }

    @FXML
    public void exitGame(ActionEvent event){
        Stage stage = (Stage)this.btnExit.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void startMinesweeper(ActionEvent event){
        Stage stage = (Stage)this.btnExit.getScene().getWindow();

        MinesweeperApp minesweeperApp = new MinesweeperApp();



        if(!isLevelOrGridEmpty()) {
            stage.close();
            try {
                minesweeperApp.startGame((((gridSize) this.cmbGrid.getValue()).toString()), (((level) this.cmbDifficulty.getValue()).toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            setLblErrorText();
        }

    }


    public void showBestResultsStage(Stage primaryStage, ResultSet resultSet) throws Exception {
        Parent root = (Parent) FXMLLoader.load(getClass().getResource("bestResults.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Best results");
        primaryStage.show();
    }

    private boolean isLevelOrGridEmpty(){
        Object objectGrid = null;
        Object objectLevel = null;

        objectGrid = this.cmbGrid.getValue();
        objectLevel = this.cmbDifficulty.getValue();

        if(!(objectGrid == null) && !(objectLevel == null)) {
            return false;
        }

       return true;
    }

    @FXML
    public void showBestResults(ActionEvent event) throws Exception{

        PreparedStatement pr = null;
        ResultSet rs = null;

        if(!isLevelOrGridEmpty()) {

            gridValue = this.cmbGrid.getSelectionModel().getSelectedItem().toString();
            levelValue = this.cmbDifficulty.getSelectionModel().getSelectedItem().toString();

                    Stage stage = (Stage) this.btnExit.getScene().getWindow();
                    stage.close();
                    showBestResultsStage(new Stage(), rs);
        }else{
            setLblErrorText();
        }

    }

    private void setLblErrorText(){
        lblError.setText("Level or grid empty!");
    }
}
