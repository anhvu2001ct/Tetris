/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.home;

import javafx.application.Platform;
import static tetris.Tetris.switchToGame;

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class MenuEventHandler {
    public static void continueGame() {
        Home.pause();
        switchToGame(false);
    }
    
    public static void playNewGame() {
        if (Home.main.left.playerNameInput.isReady()) {
            Home.pause();
            GameResult.newGame(Home.main.left.playerNameInput.getName());
            Home.statistics.playerPane.header.setText("Game In Progress");
            switchToGame(true);
        } else Home.main.left.playerNameInput.askForInput();
    }
    
    static boolean tmp = true;
    public static void openStatisticsPane() {
        Home.changeBG(Home.statisticBG, Home.statistics.dbPane::runAnimation);
    }
    
    public static void openH2P() {
        Home.main.modal.openH2P();
    }
    
    public static void openAbout() {
        Home.main.modal.openAbout();
    }
    
    public static void quitGame() {
        Platform.exit();
    }
}
