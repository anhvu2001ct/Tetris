/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

class TetriminoPane extends Pane {
    public TetriminoPane() {
        setPrefWidth(Config.SIZE * 6);
    }
    
    public void setTetrimino(Tetrimino tetrimino, Color color) {
        getChildren().clear();
        List<Pair> list = tetrimino.getData();
        int minH = list.get(0).y, maxH = list.get(list.size()-1).y;
        setPrefHeight(Config.SIZE * (maxH - minH + 1));
        for (Pair point: list) {
            Cell cell = new Cell();
            cell.setStroke(Color.rgb(0, 0, 30));
            cell.setColor(color);
            cell.setXY(point.x + 1, point.y - minH);
            getChildren().add(cell);
        }
    }
}

class UserScore extends HBox {
    private Label text;
    private Label value;
    
    public UserScore(String text) {
        setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        this.text = new Label(text);
        this.value = new Label("0");
        this.text.setMinWidth(75);
        this.value.setMinWidth(110);
        this.value.setWrapText(true);
        this.text.getStyleClass().add("right-scoring-text");
        this.value.getStyleClass().add("right-scoring-text");
        getChildren().addAll(this.text, this.value);
    }
    
    public void setValue(String value) {
        this.value.setText(value);
    }
}

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class RightPane extends VBox {
    private VBox nextField;
    private VBox scoreField;
    UserScore level, score, line;
    
    public RightPane() {
        setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        getChildren().addAll(createNextField(), createScoreField());
    }
    
    public int countNextTetri() {
        return nextField.getChildren().size()-1;
    }
    
    public void setNextTetrimino(int idx, Tetrimino tetrimino, Color color) {
        TetriminoPane pane = (TetriminoPane)nextField.getChildren().get(idx+1);
        pane.setTetrimino(tetrimino, color);
    }
    
    private HBox createNextField() {
        Label text = new Label("Next");
        text.getStyleClass().add("right-next-text");
        nextField = new VBox(text);
        nextField.setAlignment(Pos.TOP_CENTER);
        nextField.setSpacing(Config.SIZE * 0.8);
        nextField.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        for (int i = 0; i < 4; ++i) nextField.getChildren().add(new TetriminoPane());
        
        Pane tmp = new Pane(), tmp2 = new Pane();
        HBox.setHgrow(tmp, Priority.ALWAYS);
        HBox.setHgrow(tmp2, Priority.ALWAYS);
        return new HBox(tmp, nextField, tmp2);
    }
    
    private VBox createScoreField() {
        level = new UserScore("Level");
        level.setValue("1");
        score = new UserScore("Score");
        line = new UserScore("Lines");
        scoreField = new VBox(level, score, line);
        scoreField.setSpacing(20);
        VBox.setVgrow(scoreField, Priority.ALWAYS);
        scoreField.setPadding(new Insets(Config.SIZE, 10, 0, Config.SIZE));
        scoreField.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        return scoreField;
    }
}
