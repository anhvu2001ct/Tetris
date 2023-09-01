/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

import java.util.ArrayList;

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class Tetrimino {
    public static final char[] type = {'I', 'T', 'L', 'J', 'O', 'S', 'Z'};

    private boolean[][] shape;
    private ArrayList<Pair> data = new ArrayList<>();
    private int size;
    
    public void update(int size, boolean[][] shape) {
        this.shape = shape;
        this.size = size;
        data.clear();
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (shape[i][j]) data.add(new Pair(i, j));
            }
        }
    }
    
    public ArrayList<Pair> getData() { return data; }
    
    public void print() {
        System.out.println(data);
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                System.out.print(shape[i][j] ? 1 : 0);
            }
            System.out.println("");
        }
    }
    
    static void rotateLeft(Tetrimino src, Tetrimino des) {
        des.data.clear();
        des.shape = new boolean[4][4];
        int size = (des.size = src.size);
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                des.shape[i][j] = src.shape[j][size-i-1];
                if (des.shape[i][j]) des.data.add(new Pair(i, j));
            }
        }
    }

    static void rotateRight(Tetrimino src, Tetrimino des) {
        des.data.clear();
        des.shape = new boolean[4][4];
        int size = (des.size = src.size);
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                des.shape[i][j] = src.shape[size-j-1][i];
                if (des.shape[i][j]) des.data.add(new Pair(i, j));
            }
        }
    }
}
