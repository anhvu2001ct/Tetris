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
public class GameLogic {
    static boolean leftCheck(int x, int y, Tetrimino tetrimino) {
        int newX, newY;
        for (Pair point: tetrimino.getData()) {
            newX = x - 1 + point.x;
            newY = Grid.getHFromY(y + point.y);
            if (newX < 0 || !isEmpty(newY, newX)) return false;
        }
        return true;
    }
    
    static boolean rightCheck(int x, int y, Tetrimino tetrimino) {
        int newX, newY;
        for (Pair point: tetrimino.getData()) {
            newX = x + 1 + point.x;
            newY = Grid.getHFromY(y + point.y);
            if (newX >= Config.WIDTH || !isEmpty(newY, newX)) return false;
        }
        return true;
    }
    
    static boolean downCheck(int x, int y, Tetrimino tetrimino) {
        int newX, newY;
        for (Pair point: tetrimino.getData()) {
            newX = x + point.x;
            newY = Grid.getHFromY(y + 1 + point.y);
            if (newY < 0 || !isEmpty(newY, newX)) return false;
        }
        return true;
    }

    private static int[] leftDx = {0, -1, 1, 0, -1, 1, -2, 2, 0};
    private static int[] leftDy = {0, 0, 0, -1, -1, -1, 0, 0, -2};
    static Pair getLeftRotation(int x, int y, Tetrimino tetrimino) {
        int newX, newY;
        for (int i = 0; i < leftDx.length; ++i) {
            boolean found = true;
            for (Pair point: tetrimino.getData()) {
                newX = x + leftDx[i] + point.x;
                newY = Grid.getHFromY(y + leftDy[i] + point.y);
                if (newX < 0 || newX >= Config.WIDTH || newY < 0 || !isEmpty(newY, newX)) {
                    found = false;
                    break;
                }
            }
            if (found) return new Pair(leftDy[i], leftDx[i]);
        }
        return null;
    }
    
    private static int[] rightDx = {0, 1, -1, 0, 1, -1, 2, -2, 0};
    private static int[] rightDy = leftDy;
    static Pair getRightRotation(int x, int y, Tetrimino tetrimino) {
        int newX, newY;
        for (int i = 0; i < rightDx.length; ++i) {
            boolean found = true;
            for (Pair point: tetrimino.getData()) {
                newX = x + rightDx[i] + point.x;
                newY = Grid.getHFromY(y + rightDy[i] + point.y);
                if (newX < 0 || newX >= Config.WIDTH || newY < 0 || !isEmpty(newY, newX)) {
                    found = false;
                    break;
                }
            }
            if (found) return new Pair(rightDy[i], rightDx[i]);
        }
        return null;
    }
    
    private static ArrayList<Integer> lines = new ArrayList<>(4);
    static void lineClear(int minY, int maxY) {
        lines.clear();
        for (int i = minY; i <= maxY; ++i) {
            if (Game.board.grid.get(i).isClear()) lines.add(i);
        }
        int cnt = lines.size();
        if (cnt > 0) {
            Effect.makeClearEffect(lines);
            for (int i = 0; i < cnt; ++i) Game.board.grid.remove(lines.get(i) - i);
            Game.player.lineClear(cnt, Game.board.grid.getMaxH() == 0);
        }
    }
    
    static boolean isEmpty(int row, int column) {
        if (row >= Game.board.grid.getMaxH()) return true;
        return Game.board.grid.get(row).get(column).isEmpty();
    }
}
