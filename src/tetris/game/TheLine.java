/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

import javafx.scene.layout.Pane;

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class TheLine extends Pane {
    public TheLine() {
        setPrefHeight(Config.SIZE);
        for (int i = 0; i < Config.WIDTH; ++i) {
            Cell cell = new Cell();
            cell.setX(i * Config.SIZE);
            getChildren().add(cell);
        }
    }
    
    public Cell get(int column) {
        return (Cell)getChildren().get(column);
    }
    
    public boolean isClear() {
        for (int i = 0; i < Config.WIDTH; ++i) {
            if (get(i).isEmpty()) return false;
        }
        return true;
    }
}
