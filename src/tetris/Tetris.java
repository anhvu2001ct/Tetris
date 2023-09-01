/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import java.util.Random;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import tetris.game.Game;
import tetris.home.Home;

/**
 * <b>Disclaimer:</b> I only do the coding part in this private project.
 * I do not owned any images or sounds, musics. This project is only for
 * my self-study. I do not have any purposes to public this project or make it
 * commercially.
 * <br><i>All the credit goes to the respective owners.</i>
 * 
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class Tetris extends Application {
    public static final Random RNG = new Random();
    private static Stage stage;
    
    public static double rand(double min, double max) {
        return min + RNG.nextDouble() * (max - min);
    }
    
    public static int randInt(int min, int max) {
        return min + RNG.nextInt(max - min + 1);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("PROJECT:   T E T R I S");
        primaryStage.getIcons().add(new Image("img/icon.png"));
        primaryStage.setScene(Home.getScene());
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
        stage = primaryStage;
    }
    
    private static void runSwitchAnimation(Runnable cb) {
        Node root = stage.getScene().getRoot();
        FadeTransition fade = new FadeTransition(Duration.millis(750), root);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(__ -> {
            cb.run();
            root.setOpacity(1);
        });
        fade.play();
    }
    
    public static void switchToHome(boolean gameOver) {
        runSwitchAnimation(() -> {
            Home.returnHome(gameOver);
            stage.setScene(Home.getScene());
        });
    }
    
    public static void switchToGame(boolean newGame) {
        runSwitchAnimation(() -> stage.setScene(Game.getScene(newGame)));
    }

    /**
     * Start the project
     */
    public static void main(String[] args) {
        launch(args);
    }
}
