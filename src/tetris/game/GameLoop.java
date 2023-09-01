/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class GameLoop {

    public final int frameRate;
    private int frameCount;
    private long currentTick;
    private double timeElapsed;
    private Timeline loop;
    private Runnable gameUpdate, gameOver;
    
    public GameLoop() {
        this(60);
    }
    
    public GameLoop(int frameRate) {
        this.frameRate = frameRate;
        frameCount = 0;
        timeElapsed = 0;
        loop = new Timeline(new KeyFrame(Duration.millis(1000.0 / frameRate), __ -> update()));
        loop.setCycleCount(Timeline.INDEFINITE);
    }
    
    private void update() {
        long now = System.nanoTime();
        timeElapsed = (now - currentTick)/1e9;
        currentTick = now;
        if (++frameCount > frameRate) frameCount -= frameRate;
        gameUpdate.run();
    }
    
    public void start() {
        currentTick = System.nanoTime();
        loop.play();
    }
    
    public void pause() {
        loop.pause();
    }
    
    public void over() {
        loop.stop();
        gameOver.run();
    }
    
    public void setGameUpdate(Runnable gameUpdate) {
        this.gameUpdate = gameUpdate;
    }
    
    public void setGameOver(Runnable gameOver) {
        this.gameOver = gameOver;
    }

    public int getFPS() { return (int)Math.min(frameRate, Math.round(1.0 / timeElapsed)); }
    public int getFrameCount() { return frameCount; }
    public double getElapsedTime() { return timeElapsed; }
}
