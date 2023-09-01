/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.home;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import javafx.application.Platform;

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class GameResult implements Serializable, Comparable<GameResult> {
    private static final long serialVersionUID = 1L;
    
    public static class IO {
        public static ArrayList<GameResult> loadData(String fileName) {
            ArrayList<GameResult> result;
            try {
                ObjectInputStream reader = new ObjectInputStream(new FileInputStream(fileName));
                result = (ArrayList<GameResult>)reader.readObject();
            } catch(Exception e) {
                if (!(e instanceof FileNotFoundException)) System.out.println("Error loading data: " + e.getMessage());
                result = new ArrayList<GameResult>();
            }
            return result;
        }

        public static void saveData() {
            try {
                ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream("data.txt"));
                writer.writeObject(dashboard);
            } catch (Exception e) {
                System.out.println("Error saving data: " + e.getMessage());
            }
        }
    }
    
    static final ArrayList<GameResult> dashboard = IO.loadData("data.txt");
    static GameResult currentGame;
    
    String name;
    String dateTime;
    long score;
    int level;
    int totalLineClear;
    int maxB2B;
    int totalPerfectClear;
    int[] lineClearCombo;
    
    public static void saveResult(long score, int level, int line, int b2b, int perfect, int[] combo) {
        currentGame.dateTime = LocalDateTime.now().toString();
        currentGame.score = score;
        currentGame.level = level;
        currentGame.totalLineClear = line;
        currentGame.maxB2B = b2b;
        currentGame.totalPerfectClear = perfect;
        currentGame.lineClearCombo = combo;
        dashboard.add(currentGame);
        Collections.sort(dashboard);
        Platform.runLater(() -> {
            Home.statistics.playerPane.update();
            Home.statistics.dbPane.update();
        });
        IO.saveData();
    }
    
    public static void newGame(String playerName) {
        currentGame = new GameResult();
        currentGame.name = playerName;
    }
    
    public LocalDateTime getDateTime() {
        return LocalDateTime.parse(dateTime);
    }
    
    @Override
    public int compareTo(GameResult o) {
        return Long.compare(o.score, score);
    }
    
    @Override
    public String toString() {
        return String.format("Player('%s', '%s', score=%d, level=%d, line=%d, b2bMax=%d, perfect=%d, single=%d, double=%d, triple=%d, tetris=%d)",
                name, dateTime, score, level, totalLineClear, maxB2B, totalPerfectClear, lineClearCombo[0], lineClearCombo[1], lineClearCombo[2], lineClearCombo[3]);
    }
}
