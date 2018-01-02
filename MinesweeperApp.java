import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.ArrayList;

public class MinesweeperApp {

    Scene scene;

    private int tileSize = 30;
    private int width;
    private int height = 600;
    private static final int INFO_BOX_HEIGHT = 45;
    private int bombsInField;
    private int X_FIELDS;
    private int Y_FIELDS;

    private Field[][] grid;
    private Label textLabel = new Label();
    private Label label;
    Stage window;


    public Parent createContent(){
        Pane root = new Pane();
        root.setPrefSize(width,height);

        textLabel.setText("Remaining Bombs:");
        textLabel.setFont(Font.font("Arial",FontWeight.BOLD,30 ));
        textLabel.setTextFill(Color.BLACK);

        label = new Label();
        label.setText(String.valueOf(bombsInField));
//        label.setPrefSize(30,30);
        label.setFont(Font.font("Arial",FontWeight.BOLD,30 ));
        label.setTextFill(Color.RED);

        HBox infoBox = new HBox(40);
        infoBox.setPrefWidth(width);
        infoBox.setPrefHeight(INFO_BOX_HEIGHT);
        infoBox.setPadding(new Insets(3));
        infoBox.getChildren().addAll(textLabel, label);
        root.getChildren().add(infoBox);

//        create gridf
        boolean isGridPropelySet = false;
        while(!isGridPropelySet) {
            double remainingBombs = bombsInField;
            double remainingFields = X_FIELDS*Y_FIELDS;

            for (int y = 0; y < Y_FIELDS; y++) {
                for (int x = 0; x < X_FIELDS; x++) {
//                    System.out.println(remainingBombs/remainingFields);
                    boolean shouldBombBePlacedHere;
                    if(remainingBombs>0){
                        shouldBombBePlacedHere = Math.random() < remainingBombs/remainingFields;
                    }else{
                        shouldBombBePlacedHere = false;
                    }


                    Field tile = new Field(x, y, shouldBombBePlacedHere);

                    if(shouldBombBePlacedHere){
                        remainingBombs--;
                    }
                    remainingFields--;

                    grid[x][y] = tile;
                    root.getChildren().addAll(tile);
                }
            }
            if(remainingBombs == 0){
                isGridPropelySet = true;
            }
        }
        for(int y=0; y < Y_FIELDS; y++){
            for(int x=0; x < X_FIELDS; x++){
                Field tile = grid[x][y];
//                set bombs
                if(!tile.hasBomb) {
                    long bombs = getNeighbours(tile).stream().filter(t -> t.hasBomb).count();

                    if(bombs >0) tile.text.setText(String.valueOf(bombs));

                }

            }
        }

        return root;
    }

    public void continueGame(){

        scene.setRoot(createContent());
    }


    private List<Field> getNeighbours(Field tile){
        List<Field> neighbours = new ArrayList<Field>();

        int[] points = new int[] {
                -1, -1,
                -1, 0,
                -1, 1,
                0, -1,
                0, 1,
                1, -1,
                1, 0,
                1, 1
        };

        for(int i = 0; i < points.length; i++){
            int dx = points[i];
            int dy = points[++i];

            int newX = tile.x + dx;
            int newY = tile.y + dy;

            if(isValidCorner(newX, newY)){
                neighbours.add(grid[newX][newY]);
            }
        }


        return neighbours;
    }

    private boolean isValidCorner(int x, int y){

        if(x >= 0 && x < X_FIELDS && y >=0 && y < Y_FIELDS) return true;

        return false;
    }

    private class Field extends StackPane {

        public int x,y;
        private boolean hasBomb;
        private boolean isOpened = false;
        private static final char RED_COLOR_SYMBOL = 'R';
        private static final char BLACK_COLOR_SYMBOL = 'B';


        private Rectangle border = new Rectangle(tileSize-2, tileSize-2);
        private Text text = new Text();


        public Field(int x, int y, boolean hasBomb){
            this.x = x;
            this.y = y;
            this.hasBomb = hasBomb;


            border.setStroke(Color.LIGHTGRAY);
            border.setFill(Color.DARKBLUE);
            text.setFill(Color.BLACK);
            text.setFont(Font.font(18));
            text.setText(hasBomb ? "X": "");
            text.setVisible(false);

            getChildren().addAll(border, text);

            setTranslateX(x* tileSize);
            setTranslateY(y* tileSize + INFO_BOX_HEIGHT);



            this.setOnMouseClicked((new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    MouseButton button = event.getButton();
                    if(button==MouseButton.PRIMARY){
                        if(!isFieldFlaggedWithBomb()){
                            open();
                        }
                    }else if(button==MouseButton.SECONDARY){
                        if(!isOpened) {
                            if (!isFieldFlaggedWithBomb()) {
                                changeFieldColor(RED_COLOR_SYMBOL);
                                changeDisplayedBombNumber(true);
                            } else {
                                changeFieldColor(BLACK_COLOR_SYMBOL);
                                changeDisplayedBombNumber(false);
                            }
                        }

                    }
                }
            }));

        }

        public void changeDisplayedBombNumber(boolean isDecreased){
            int currentBombAmount = Integer.parseInt( label.getText());

            if(isDecreased){
                if(currentBombAmount >0) {
                    label.setText(String.valueOf(currentBombAmount - 1));
                }
            } else{
                if(currentBombAmount < bombsInField) {
                    label.setText(String.valueOf(currentBombAmount + 1));
                }
            }

        }

        public boolean isFieldFlaggedWithBomb(){
            if(border.getFill() == Color.RED){
                return true;
            }else return false;
        }

        public void changeFieldColor(char colorSymbol){

            if(colorSymbol == RED_COLOR_SYMBOL){
                border.setFill(Color.RED);
            }else border.setFill(Color.BLACK);


        }

        public void display(String title, String message){
           Stage alertWindow = new Stage();


//        zablokuj interakcje z innymi okienkami
            alertWindow.initModality(Modality.APPLICATION_MODAL);
            alertWindow.setTitle(title);
            alertWindow.setMinWidth(350);
            alertWindow.setMaxHeight(200);
            alertWindow.setResizable(false);


            Label label = new Label();
            label.setText(message);
            Button continueButton = new Button("Play again");
            Button closeButton = new Button("Quit title");

            continueButton.setPrefSize(100,50);
            continueButton.setOnAction(e -> {
                alertWindow.close();
                scene.setRoot(createContent());
            });

            closeButton.setPrefSize(100,50);
            closeButton.setOnAction(e -> {
                alertWindow.close();
                window.close();
            });



            VBox layout = new VBox(10);
            HBox buttonContainer = new HBox(10);
            buttonContainer.setAlignment(Pos.CENTER);
            buttonContainer.getChildren().addAll(continueButton,closeButton);
            layout.getChildren().addAll(label, buttonContainer);
            layout.setAlignment(Pos.CENTER);

            Scene scene = new Scene(layout);
            alertWindow.setScene(scene);
            alertWindow.showAndWait();

        }


        public void open(){



            if(hasBomb){
               display("Game lost", "Snapp, you lost! Do you want to continue?");
            }
            else if(!isOpened){
                text.setVisible(true);
                isOpened = true;
                border.setFill(null);


                if (text.getText().isEmpty()){
                    getNeighbours(this).forEach(Field::open);
                }
            }
        }

    }




    public void startGame(String gridDimensions, String difficulty) throws Exception {

        int gridSize = 0;
        this.bombsInField = 0;
        switch (gridDimensions){
            case "TWENTY":
                gridSize = 20;
                break;
            case "TWENTY_FIVE":
                gridSize = 25;
                this.tileSize = 25;
                break;
            case "THIRTY":
                gridSize = 30;
                this.tileSize = 21;
                break;
        }

        switch (difficulty){
            case "EASY":
                this.bombsInField = (int)(0.1*(gridSize*gridSize));
                break;
            case "MEDIUM":
                this.bombsInField = (int)(0.18*(gridSize*gridSize));
                break;
            case "HARD":
                this.bombsInField = (int)(0.26*(gridSize*gridSize));
                break;
        }

        X_FIELDS = gridSize;
        Y_FIELDS = gridSize;
        grid = new Field[X_FIELDS][Y_FIELDS];
        this.width  = gridSize*this.tileSize;
        this.height = gridSize*this.tileSize+INFO_BOX_HEIGHT;

        window = new Stage();
        scene = new Scene(createContent());
        window.setScene(scene);
        window.setTitle("Minesweeper");
        window.show();
    }



}
