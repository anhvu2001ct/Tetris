/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

import javafx.scene.paint.Color;

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class Tetriminos {
    public static Tetriminos[] tetri = new Tetriminos[7];
    
    private Tetrimino[] data = new Tetrimino[7];
    public final Color color, colorBrighter;

    public Tetriminos(Color color) {
        this.color = color.darker();
        this.colorBrighter = this.color.brighter();
    }
    
    public Tetrimino get(int index) { return data[index]; }
    
    public void update(int size, boolean[][] shape) {
        for (int i = 0; i < 7; ++i) data[i] = new Tetrimino();
        data[3].update(size, shape);
        for (int i = 4; i < 7; ++i) Tetrimino.rotateRight(data[i-1], data[i]);
        for (int i = 2; i >= 0; --i) Tetrimino.rotateLeft(data[i+1], data[i]);
    }
    
    public void print() {
        System.out.println(color);
        data[3].print();

        System.out.println("--- Rotate Left ---");
        for (int i = 2; i >= 0; --i) {
            data[i].print();
            System.out.println("");
        }

        System.out.println("--- Rotate Right ---");
        for (int i = 4; i < 7; ++i) {
            data[i].print();
            System.out.println("");
        }
    }

    static void init() {
        // I shape
        tetri[0] = new Tetriminos(Color.CYAN);
        tetri[0].update(4, new boolean[][] {
            {false, false, false, false},
            {true, true, true, true},
            {false, false, false, false},
            {false, false, false, false}
        });
        
        // T shape
        tetri[1] = new Tetriminos(Color.rgb(165, 105, 189));
        tetri[1].update(3, new boolean[][] {
            {false, true, false, false},
            {true, true, true, false},
            {false, false, false, false},
            {false, false, false, false}
        });
        
        // L shape
        tetri[2] = new Tetriminos(Color.rgb(235, 152, 78));
        tetri[2].update(3, new boolean[][] {
            {false, false, true, false},
            {true, true, true, false},
            {false, false, false, false},
            {false, false, false, false}
        });
        
        // J shape
        tetri[3] = new Tetriminos(Color.rgb(15, 95, 200));
        tetri[3].update(3, new boolean[][] {
            {true, false, false, false},
            {true, true, true, false},
            {false, false, false, false},
            {false, false, false, false}
        });

        // O shape
        tetri[4] = new Tetriminos(Color.YELLOW);
        tetri[4].update(4, new boolean[][] {
            {false, false, false, false},
            {false, true, true, false},
            {false, true, true, false},
            {false, false, false, false}
        });
        
        // S shape
        tetri[5] = new Tetriminos(Color.rgb(130, 224, 170));
        tetri[5].update(3, new boolean[][] {
            {false, true, true, false},
            {true, true, false, false},
            {false, false, false, false},
            {false, false, false, false}
        });

        // Z shape
        tetri[6] = new Tetriminos(Color.RED);
        tetri[6].update(3, new boolean[][] {
            {true, true, false, false},
            {false, true, true, false},
            {false, false, false, false},
            {false, false, false, false}
        });
    }
}
