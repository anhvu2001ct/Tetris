/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

import java.io.File;
import java.util.ArrayDeque;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import static tetris.Tetris.RNG;

class TheMusic {
    public static double volume = 1;

    private final MediaPlayer mp;

    public TheMusic(String src) {
        mp = new MediaPlayer(new Media((new File("music/"+src)).toURI().toString()));
        mp.setCycleCount(MediaPlayer.INDEFINITE);
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

class TheSound extends Service<Void> {
    public static double volume = 1;
    
    private final Media src;
    private ArrayDeque<MediaPlayer> li = new ArrayDeque<>();
    private int count = 0;
    
    public TheSound(String soundSrc) {
        src = new Media((new File("music/sound/"+soundSrc)).toURI().toString());
    }
    
    public void play() {
        restart();
    }
    
    private void play(MediaPlayer mp) {
        mp.setVolume(volume);
        mp.seek(Duration.ZERO);
        mp.play();
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (volume == 0) return null;
                if (count == 0) {
                    MediaPlayer mp = new MediaPlayer(src);
                    mp.setOnEndOfMedia(() -> ++count);
                    li.add(mp);
                    play(mp);
                } else {
                    MediaPlayer mp = li.pollFirst();
                    li.add(mp);
                    --count;
                    play(mp);
                }
                return null;
            }
        };
    }
}

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class Sound {
    private static final TheMusic[] MUSIC = {
        new TheMusic("slow1.mp3"),
        new TheMusic("slow.mp3"),
        new TheMusic("fast.mp3")
    };
    
    public static final TheSound MOVE_SOUND = new TheSound("move.wav");
    public static final TheSound STUCK_SOUND = new TheSound("stuck.wav");
    public static final TheSound ROTATE_SOUND = new TheSound("rotate.wav");
    public static final TheSound DROP_SOUND = new TheSound("drop.wav");
    public static final TheSound HOLD_SOUND = new TheSound("hold.wav");

    public static final TheSound[] SCORING_SOUND = {
        new TheSound("score1.mp3"),
        new TheSound("score2.mp3")
    };
    
    public static final TheSound[] TETRIS_SOUND = {
        new TheSound("tetris1.wav"),
        new TheSound("tetris2.wav")
    };
    
    private static TheMusic getMusic(int level) {
        if (level >= 7) return MUSIC[2];
        else if (level >= 4) return MUSIC[1];
        return MUSIC[0];
    }
    
    private static TheMusic currentMusic = null;
    public static void changeMusic(int level) {
        TheMusic music = getMusic(level);
        if (currentMusic != music) reset(music);
    }
    
    public static void reset(TheMusic music) {
        if (currentMusic != null) currentMusic.stop();
        currentMusic = music;
        setMusicVolume(TheMusic.volume);
        playMusic();
    }
    
    public static void playMusic() {
        if (currentMusic != null) currentMusic.play();
    }
    
    public static void playSound(TheSound sound) {
        if (sound != null) sound.play();
    }
    
    public static void playSound(TheSound[] sounds) {
        if (sounds == null || sounds.length == 0) return;
        sounds[RNG.nextInt(sounds.length)].play();
    }
    
    public static void pause() {
        if (currentMusic != null) currentMusic.pause();
    }
    
    public static void setMusicVolume(double volume) {
        TheMusic.volume = volume;
        if (currentMusic != null) currentMusic.setVolume(volume);
    }
    
    public static void setSoundVolume(double volume) {
        TheSound.volume = volume;
    }
}
