/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class Pair {
    public final int x, y;

    Pair(int y, int x) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
