/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class Cell extends Rectangle {
    private boolean available;
    private Color color;
    
    public Cell() {
        setWidth(Config.SIZE);
        setHeight(Config.SIZE);
        setStrokeWidth(Math.ceil(Config.SIZE * 0.05));
        setArcHeight(Config.SIZE * 0.4);
        setArcWidth(Config.SIZE * 0.4);
        clear();
    }
    
    public void setXY(int x, int y) {
        setX(x * Config.SIZE);
        setY(y * Config.SIZE);
    }
    
    public void set() {
        available = false;
        setStroke(Color.rgb(0, 0, 30));
        setFill(color);
    }
    
    public void clear() {
        available = true;
        setFill(null);
        setStroke(null);
    }
    
    public void setColor(Color color) { setFill(this.color = color); }
    public Color getColor() { return color; }
    
    public boolean isEmpty() { return available; }
}
