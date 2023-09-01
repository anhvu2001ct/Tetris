/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class Auto extends ScheduledService<Void> {
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (Game.player.ghost.posX != Game.player.posX || Game.player.ghost.posY != Game.player.posY) {
                    Platform.runLater(() -> InputEventHandler.softDrop(false));
                }
                return null;
            }
        };
    }

    public void changeLevel(int level) {
        cancel();
        setPeriod(Duration.seconds(Config.getSpeed(level)));
        restart();
        Sound.changeMusic(level);
        Game.rightPane.level.setValue(String.valueOf(level));
    }
    
    public void pause() {
        Game.loop.pause();
        cancel();
        Sound.pause();
    }
    
    public void play() {
        Game.loop.start();
        restart();
        Sound.playMusic();
    }
    
    private double time = 0;
    public void lockDelayCheck(double delta) {
        if (Game.player.ghost.posX == Game.player.posX && Game.player.ghost.posY == Game.player.posY) {
            time += delta;
            if (time >= 0.6) InputEventHandler.hardDrop();
        }
    }
    
    public void resetLockDelay() {
        time = 0;
    }
}
