/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

class KeyTrigger {
    private static final double firstDelay = 0.2;
    private static final double betweenDelay = 0.05;
    
    boolean firstTime = true;
    long lastTick;
    Runnable cb;
    
    public KeyTrigger(Runnable callback) {
        lastTick = System.nanoTime();
        cb = callback;
    }
    
    public void trigger() {
        long now = System.nanoTime();
        double elapsed = (now - lastTick)/1e9;
        if (firstTime) {
            if (elapsed < firstDelay) return;
            firstTime = false;
        }
        if (elapsed > betweenDelay) {
            cb.run();
            lastTick = now;
        }
    }
}

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class InputEventHandler {
    static void moveLeft() {
        if (GameLogic.leftCheck(Game.player.posX, Game.player.posY, Game.player.block.getTetrimino())) {
            Game.player.moveX(-1);
            Game.player.updateGhost();
            Sound.playSound(Sound.MOVE_SOUND);
        }
    }
    
    static void moveRight() {
        if (GameLogic.rightCheck(Game.player.posX, Game.player.posY, Game.player.block.getTetrimino())) {
            Game.player.moveX(1);
            Game.player.updateGhost();
            Sound.playSound(Sound.MOVE_SOUND);
        }
    }
    
    static void softDrop(boolean isUserMove) {
        if (GameLogic.downCheck(Game.player.posX, Game.player.posY, Game.player.block.getTetrimino())) {
            Game.auto.resetLockDelay();
            Game.player.moveY(1);
            if (isUserMove) {
                Game.player.data.increaseScore(1);
                Sound.playSound(Sound.MOVE_SOUND);
            }
        }
    }
    
    static void fastDrop() {
        if (GameLogic.downCheck(Game.player.posX, Game.player.posY, Game.player.block.getTetrimino())) {
            Game.auto.resetLockDelay();
            int delta = Game.player.ghost.posY - Game.player.posY;
            Game.player.moveY(delta);
            Game.player.data.increaseScore(delta);
            Sound.playSound(Sound.MOVE_SOUND);
        } else Sound.playSound(Sound.STUCK_SOUND);
    }
    
    static void hardDrop() {
        Game.auto.resetLockDelay();
        int x = Game.player.ghost.posX, y = Game.player.ghost.posY;
        int newX, newY, minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        boolean gameOver = false;
        for (Pair point: Game.player.block.getTetrimino().getData()) {
            newX = x + point.x;
            newY = Grid.getHFromY(y + point.y);
            if (newY >= Config.HEIGHT) {
                gameOver = true;
                continue;
            }
            Cell cell = Game.board.grid.get(newY).get(newX);
            cell.setColor(Game.player.block.getColor());
            cell.set();
            minY = Math.min(minY, newY);
            maxY = Math.max(maxY, newY);
        }
        if (gameOver) {
            Game.loop.over();
            return;
        }
        Sound.playSound(Sound.DROP_SOUND);
        Game.player.data.increaseScore(2 * (y - Game.player.posY));
        Effect.makeDropEffect(x, y, Game.player.block.getTetrimino());
        GameLogic.lineClear(minY, maxY);
        Game.player.changeTetrimino();
        Game.player.holder.enable();
    }
    
    static void rotateLeft() {
        int nextRotationID = Game.player.block.getLeftRotationID();
        Pair pair = GameLogic.getLeftRotation(Game.player.posX, Game.player.posY, Game.player.block.getTetrimino(nextRotationID));
        if (pair == null) {
            Sound.playSound(Sound.STUCK_SOUND);
            return;
        }
        Sound.playSound(Sound.ROTATE_SOUND);
        Game.auto.resetLockDelay();
        Game.player.moveX(pair.x);
        Game.player.moveY(pair.y);
        Game.player.block.setRotation(nextRotationID);
        Game.player.updateGhost();
    }
    
    static void rotateRight() {
        int nextRotationID = Game.player.block.getRightRotationID();
        Pair pair = GameLogic.getRightRotation(Game.player.posX, Game.player.posY, Game.player.block.getTetrimino(nextRotationID));
        if (pair == null) {
            Sound.playSound(Sound.STUCK_SOUND);
            return;
        }
        Sound.playSound(Sound.ROTATE_SOUND);
        Game.auto.resetLockDelay();
        Game.player.moveX(pair.x);
        Game.player.moveY(pair.y);
        Game.player.block.setRotation(nextRotationID);
        Game.player.updateGhost();
    }
    
    static void holdPiece() {
        if (Game.player.holder.available) {
            Game.player.holder.hold();
            Sound.playSound(Sound.HOLD_SOUND);
        } else Sound.playSound(Sound.STUCK_SOUND);
    }
    
    static void togglePause() {
        if (Game.pausePane.isPaused()) Game.pausePane.resume();
        else Game.pausePane.pause();
        Game.root.requestFocus();
    }
    
    static void toggleHidden() {
        Game.board.grid.setVisible(Game.board.grid.isVisible() ^ true);
    }
    
    private static HashMap<KeyCode, KeyTrigger> keysPressed = new HashMap<>();
    private static HashSet<KeyCode> autoTriggerKeys = new HashSet<>(Arrays.asList(KeyCode.LEFT, KeyCode.RIGHT, KeyCode.DOWN));
    static void triggerKeysPressed() {
        if (Game.pausePane.isPaused()) return;
        keysPressed.forEach((__, kt) -> {
            if (kt != null) kt.trigger();
        });
    }
    
    private static Runnable getEvent(KeyCode code) {
        switch (code) {
            case LEFT:
                return InputEventHandler::moveLeft;
            case RIGHT:
                return InputEventHandler::moveRight;
            case UP:
                return InputEventHandler::rotateRight;
            case DOWN:
                return () -> softDrop(true); 
            case SPACE:
                return InputEventHandler::hardDrop;
            case X:
                return InputEventHandler::fastDrop;
            case C:
                return InputEventHandler::holdPiece;
            case Z:
                return InputEventHandler::rotateLeft;
            case ESCAPE:
                return InputEventHandler::togglePause;
            case H:
                return InputEventHandler::toggleHidden;
        }
        return null;
    }
    
    static void unInit() {
        Scene scene = Game.root.getScene();
        scene.setOnKeyPressed(null);
        scene.setOnKeyReleased(null);
    }
    
    static void init() {
        Scene scene = Game.root.getScene();
        scene.setOnKeyReleased(key -> keysPressed.remove(key.getCode()));
        scene.setOnKeyPressed(key -> {
            if (Game.pausePane.isLocked() || Game.isOver) return;
            KeyCode code = key.getCode();
            Runnable event = getEvent(code);
            if (keysPressed.containsKey(code)) return;
            if (autoTriggerKeys.contains(code)) keysPressed.put(code, new KeyTrigger(event));
            else keysPressed.put(code, null);
            if (!Game.pausePane.isPaused() || code == KeyCode.ESCAPE)
                if (event != null) event.run();
        });
    }
}
