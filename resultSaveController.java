import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;

public class resultSaveController implements Initializable{

    @FXML
    private TextField txtName;

    @FXML
    private Label lblResult, lblAlert;

    @FXML
    private Button btnOK;

    private String result = String.valueOf(MinesweeperApp.finalResult);
    private String dbTableName = MinesweeperApp.tableName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblResult.setText(result);
    }

    Connection connection;

    @FXML
    private void commitChanges(ActionEvent event){
        Stage stage = (Stage)this.btnOK.getScene().getWindow();

        String playerName = txtName.getText();

        if(!playerName.equals(null)){
            executeSQLInsert(playerName);
        }else{
            lblAlert.setText("Name mustn't be empty");
        }

        stage.close();

        Stage newStage = new Stage();
        MinesweeperMain minesweeperMain = new MinesweeperMain();
        try {
            minesweeperMain.start(newStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeSQLInsert(String playerName){
        Statement stmt = null;

        String sql = "INSERT INTO " + dbTableName + " VALUES ('" + playerName + "', '" + result + "')";
//        System.out.println(sql);
        try {
            this.connection = dbConnection.getConnection();

            stmt = connection.createStatement();
            stmt.executeUpdate(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
