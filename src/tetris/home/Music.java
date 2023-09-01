/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.home;

import java.io.File;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

class TheMusic {
    public static double volume = 0.5;

    private final MediaPlayer mp;
    private Runnable callback;

    public TheMusic(String src) {
        mp = new MediaPlayer(new Media(new File("music/"+src).toURI().toString()));
        mp.setCycleCount(MediaPlayer.INDEFINITE);
    }
    
    public void setOnFinished(Runnable cb) {
        mp.setOnEndOfMedia(cb);
    }
    
    public void setVolume(double volume) {
        mp.setVolume(volume);
    }
    
    public void pause() {
        mp.pause();
    }
    
    public void stop() {
        mp.stop();
    }
    
    public void play() {
        mp.play();
    }
}

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class Music {
    private static TheMusic currentMusic;
    private static final TheMusic[] MUSICS = {
        new TheMusic("home1.mp3"),
        new TheMusic("home2.mp3"),
        new TheMusic("home3.mp3")
    };
    
    public static void init() {
        for (int i = 0; i < MUSICS.length; ++i) {
            int next = (i+1) % MUSICS.length;
            MUSICS[i].setOnFinished(() -> {
                if (currentMusic != null) currentMusic.stop();
                currentMusic = MUSICS[next];
                setVolume(TheMusic.volume);
                currentMusic.play();
            });
        }
        
        KeyCodeCombination increase = new KeyCodeCombination(KeyCode.EQUALS, KeyCodeCombination.CONTROL_DOWN);
        KeyCodeCombination decrease = new KeyCodeCombination(KeyCode.MINUS, KeyCodeCombination.CONTROL_DOWN);
        Home.root.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (increase.match(event)) {
                setVolume(Math.min(1, TheMusic.volume + 0.1));
            } else if (decrease.match(event)) {
                setVolume(Math.max(0, TheMusic.volume - 0.1));
            }
        });
        
        currentMusic = MUSICS[0];
        setVolume(TheMusic.volume);
        play();
    }
    
    public static void play() {
        if (currentMusic != null) currentMusic.play();
    }
    
    public static void pause() {
        if (currentMusic != null) currentMusic.pause();
    }
    
    public static void setVolume(double volume) {
        TheMusic.volume = volume;
        if (currentMusic != null) currentMusic.setVolume(volume);
    }
}
