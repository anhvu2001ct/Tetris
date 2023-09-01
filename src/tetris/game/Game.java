/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

import javafx.animation.FadeTransition;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import static tetris.Tetris.switchToHome;

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class Game {
    static final StackPane root = new StackPane();
    static final HBox mainPane = new HBox();
    static final PausePane pausePane = new PausePane();
    static final GameOverPane goPane = new GameOverPane();
    static final GameLoop loop = new GameLoop();
    static final Board board = new Board();
    static final Auto auto = new Auto();
    static final LeftPane leftPane = new LeftPane();
    static final RightPane rightPane = new RightPane();
    static final ExpPane expPane = new ExpPane();
    static Player player;
    static boolean isOver;

    public static Scene getScene(boolean newGame) {
        Scene scene = root.getScene();
        if (scene == null) scene = new Scene(root);
        if (newGame) {
            initialize();
            startNewGame();
        }
        return scene;
    }
    
    private static void startNewGame() {
        FadeTransition fade = new FadeTransition(Duration.millis(1500), root);
        fade.setFromValue(0);
        fade.setToValue(1);
        InputEventHandler.unInit();
        mainPane.toFront();
        pausePane.changeBorders();
        pausePane.forceUnPaused();
        expPane.reset();
        root.requestFocus();
        Sound.reset(null);
        fade.setOnFinished(__ -> {
            auto.changeLevel(player.data.level);
            loop.start();
            pausePane.unchangeBorders();
            InputEventHandler.init();
        });
        fade.play();
    }

    private static void gameUpdate() {
        auto.lockDelayCheck(loop.getElapsedTime());
        Effect.checkClearEffect();
        InputEventHandler.triggerKeysPressed();
    }

    private static Service<Void> onGameOver;
    private static void gameOver() {
        isOver = true;
        goPane.show();
        auto.pause();
        if (onGameOver == null) {
            onGameOver = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            player.data.save();
                            Thread.sleep(750);
                            switchToHome(true);
                            return null;
                        }
                    };
                }
            };
        }
        onGameOver.restart();
    }

    private static Pane createBorder() {
        Pane pane = new Pane();
        pane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        pane.setMinWidth(4);
        HBox.setMargin(pane, new Insets(0, 5, 0, 5));
        pane.getStyleClass().add("main-border-normal");
        return pane;
    }

    private static boolean isInitialized = false;
    public static void initialize() {
        if (!isInitialized) {
//            Generator.test();
            isInitialized = true;
            root.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
            root.getStylesheets().add("style/game.css");
            pausePane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
            mainPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
            mainPane.getChildren().addAll(leftPane, createBorder(), board, expPane, rightPane);
            mainPane.getStyleClass().add("bg-color");
            root.getChildren().addAll(new StackPane(pausePane, mainPane), goPane);
            loop.setGameUpdate(Game::gameUpdate);
            loop.setGameOver(Game::gameOver);
            Tetriminos.init();
            player = new Player();
        }
        isOver = false;
        goPane.hide();
        board.clear();
        player.reset();
        auto.resetLockDelay();
    }
}
