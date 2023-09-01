/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;

class TheGrid extends Pane {
    public TheGrid() {
        getStyleClass().add("main-grid");
        for (int i = 1; i < Config.WIDTH; ++i) {
            Line line = new Line(i * Config.SIZE, 0, i * Config.SIZE, Config.HEIGHT * Config.SIZE);
            getChildren().add(line);
        }
        for (int i = 1; i < Config.HEIGHT; ++i) {
            Line line = new Line(0, i * Config.SIZE, Config.WIDTH * Config.SIZE, i * Config.SIZE);
            getChildren().add(line);
        }
        setVisible(false);
    }
    
    public void toggle() {
        if (isVisible()) setVisible(false);
        else setVisible(true);
    }
}

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class Board extends StackPane {
    final Pane moveField = new Pane();
    final TheGrid boardGrid = new TheGrid();
    final Grid grid = new Grid();
    final Pane effectField = new Pane();
    
    public Board() {
        setPrefSize(Config.WIDTH * Config.SIZE, Config.HEIGHT * Config.SIZE);
        getChildren().addAll(moveField, boardGrid, grid, effectField);
    }
    
    public void clear() {
        grid.clear();
    }
    
    public void addTetrimino(Pane block) {
        moveField.getChildren().add(block);
    }
    
    public void addEffect(Node effect) {
        effectField.getChildren().add(effect);
    }
    
    public void removeEffect(Node effect) {
        effectField.getChildren().remove(effect);
    }
    
    public void clearEffect() {
        effectField.getChildren().clear();
    }
}
