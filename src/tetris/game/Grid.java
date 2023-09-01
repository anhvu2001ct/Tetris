/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class Grid extends VBox {
    public final Pane placeholder = new Pane();
    
    public Grid() {
        setVgrow(placeholder, Priority.ALWAYS);
        getChildren().add(placeholder);
    }
    
    public void clear() {
        getChildren().remove(1, getChildren().size());
    }
    
    public void add(TheLine line) {
        getChildren().add(1, line);
    }
    
    public void remove(int row) {
        getChildren().remove(getMaxH() - row);
    }
    
    public int getMaxH() {
        return getChildren().size() - 1;
    }
    
    public TheLine get(int row) {
        while (getMaxH()-1 < row) add(new TheLine());
        return (TheLine)getChildren().get(getMaxH() - row);
    }
    
    static int getHFromY(int y) {
        return Config.HEIGHT - y - 1;
    }
}
