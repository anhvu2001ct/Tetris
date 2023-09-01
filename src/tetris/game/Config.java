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
public class Config {
    public static final int         WIDTH   = 10;
    public static final int         HEIGHT  = 20;
    public static final int         SIZE    = 27;
    public static final int[]       SCORE   = {100, 300, 500, 800};
    public static final int         SCORE_P = 1000;
    public static final int[]       LINES   = {1, 3, 5, 8};
    public static final String[]    CLEARS  = {"SINGLE", "DOUBLE", "TRIPLE", "TETRIS"};
    
    public static int getExpNeed(int level) {
        return 13 + 5*level + 3*(level-1)*(level-2)/2;
    }
    
    public static double getSpeed(int level) {
        return Math.pow(0.8 - (level - 2)*0.006, level-1);
    }
}
