import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class startPageController implements Initializable{

    @FXML
    private ComboBox<level> cmbDifficulty;
    @FXML
    private ComboBox<gridSize> cmbGrid;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnExit;
    @FXML
    private Label lblError;

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
        Object objectGrid = null;
        Object objectLevel = null;

            objectGrid = this.cmbGrid.getValue();
            objectLevel = this.cmbDifficulty.getValue();


        if(!(objectGrid == null) && !(objectLevel == null)) {
            stage.close();
            try {
                minesweeperApp.startGame((((gridSize) this.cmbGrid.getValue()).toString()), (((level) this.cmbDifficulty.getValue()).toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            lblError.setText("Choose level and grid size first!!");
        }

    }
}
