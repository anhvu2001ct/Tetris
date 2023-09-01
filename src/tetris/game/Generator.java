/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class Generator {
    private int[] data = new int[35];
    private ArrayDeque<Character> history = new ArrayDeque<>(Arrays.asList('S', 'Z', 'S', 'Z'));
    private ArrayList<Integer> lru = new ArrayList<>();
    private Random rng = new Random();
    
    public Generator() {
        for (int i = 0; i < data.length; ++i) data[i] = i%7;
        lru.ensureCapacity(7);
    }
    
    private boolean firstTime = true;
    public int getTetrimino() {
        // always return I/T/L/J for the first time
        if (firstTime) {
            firstTime = false;
            return rng.nextInt(4);
        }
        
        int index = 0, result = 0;
        for (int i = 0; i < 6; ++i) {
            index = rng.nextInt(data.length);
            result = data[index];
            if (i == 5 || !history.contains(Tetrimino.type[result])) break;
            if (lru.size() > 0) data[index] = lru.get(0);
        }
        
        lru.remove(Integer.valueOf(result));
        lru.add(result);
        data[index] = lru.get(0);
        history.removeFirst();
        history.addLast(Tetrimino.type[result]);
        
        return result;
    }
    
    public int isFailed() {
        for (int piece = 0; piece < 7; ++piece) {
            boolean found = false;
            for (int j = 0; j < 35; ++j) {
                if (data[j] == piece) {
                    found = true;
                    break;
                }
            }
            if (!found && !lru.contains(piece)) return piece;
        }
        return -1;
    }
    
    static void test() {
        int[] testNum = {20, 100, 200, 1000, 2000, 10000, 20000};
        
        for (int t = 0; t < testNum.length; ++t) {
            int num = testNum[t];
            System.out.println("\nTestNum = " + num);
            Generator rng = new Generator();
            Map<Character, Integer> cnt = new HashMap<>();
            long time = 0;
            
            for (int i = 0; i < num; ++i) {
                double now = System.nanoTime();
                char value = Tetrimino.type[rng.getTetrimino()];
                time += System.nanoTime() - now;
                System.out.print(value);
                cnt.put(value, cnt.getOrDefault(value, 0) + 1);
            }
            
            int minF = num, maxF = 0;
            for (Map.Entry<Character, Integer> entry : cnt.entrySet()) {
                minF = Math.min(minF, entry.getValue());
                maxF = Math.max(maxF, entry.getValue());
            }
            
            int piece = rng.isFailed();
            if (piece != -1) {
                System.err.println("Piece " + Tetrimino.type[piece] + " not found!");
                System.err.println(Arrays.toString(rng.data));
                System.err.println(rng.lru);
            }
            System.out.println("\nFrequency: " + cnt);
            System.out.printf("Max - Min = %d (%.2f%%)\n", maxF - minF, 100.0*(maxF - minF)/num);
            System.out.printf("Total time: %.3f | Average time per gen: %.7f\n", time/1e9, time/1e9/num);
        }
    }
}
