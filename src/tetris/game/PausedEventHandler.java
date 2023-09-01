/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

import tetris.game.PausePane.CustomButton;
import static tetris.Tetris.switchToHome;

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class PausedEventHandler {
    static void resumeButtonClicked(CustomButton button) {
        if (Game.pausePane.isLocked()) return;
        InputEventHandler.togglePause();
    }
    
    static void homeButtonClicked(CustomButton button) {
        if (Game.pausePane.isLocked()) return;
        if (button.style.endsWith("secondary")) {
            button.changeStyle("primary");
            return;
        }
        switchToHome(false);
    }
    
    static Void musicVolumeChanged(double value) {
        Sound.setMusicVolume(value);
        return null;
    }
    
    static Void soundVolumeChanged(double value) {
        Sound.setSoundVolume(value);
        return null;
    }
    
    static Void ghostPieceToggled(boolean value) {
        Game.player.ghost.toggle();
        return null;
    }
    
    static Void gridShowToggled(boolean value) {
        Game.board.boardGrid.toggle();
        return null;
    }
}
