/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.home;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javax.imageio.ImageIO;

class HomeLoop extends ScheduledService<Void> {
    private ArrayList<Runnable> events = new ArrayList<Runnable>();
    private long current = -1;
    private double delta = 0;
    
    public HomeLoop() {
        setPeriod(Duration.millis(20));
    }
    
    public void addEvent(Runnable event) {
        events.add(event);
    }
    
    public double getElapsedTime() {
        return delta;
    }
    
    private void calcDelta() {
        long now = System.nanoTime();
        if (current != -1) delta = (now - current)/1e9;
        current = now;
    }
    
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                calcDelta();
                events.forEach(event -> event.run());
                return null;
            }
        };
    }
}

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class Home {
    static final StackPane root = new StackPane();
    static final HomeLoop loop = new HomeLoop();
    static final MainPane main = new MainPane();
    static final StatisticsPane statistics = new StatisticsPane();
    static final EffectPane effects = new EffectPane();
    
    public static Scene getScene() {
        Scene scene = root.getScene();
        if (scene == null) {
            scene = new Scene(root);
            initialize();
        }
        return scene;
    }
    
    static Pane mainBG = new Pane();
    static Pane statisticBG = new Pane();
    static void changeBG(Pane toBG, Runnable cb) {
        Pane fromBG = (toBG == mainBG) ? statisticBG : mainBG;
        Pane fromPane = (fromBG == mainBG) ? main : statistics;
        Pane toPane = (toBG == mainBG) ? main : statistics;
        
        fromBG.toFront();
        FadeTransition fadeFrom = new FadeTransition(Duration.millis(1000), fromBG);
        fadeFrom.setFromValue(1);
        fadeFrom.setToValue(0);

        FadeTransition fadeTo = new FadeTransition(Duration.millis(1000), toBG);
        fadeTo.setFromValue(0.3);
        fadeTo.setToValue(1);
        
        FadeTransition fadeFromPane = new FadeTransition(Duration.millis(1000), fromPane);
        fadeFromPane.setFromValue(1);
        fadeFromPane.setToValue(0);
        
        if (toPane == statistics) statistics.dbPane.hide();
        toPane.toFront();
        toPane.setVisible(true);
        FadeTransition fadeToPane = new FadeTransition(Duration.millis(1000), toPane);
        fadeToPane.setFromValue(0.3);
        fadeToPane.setToValue(1);
        
        ParallelTransition animation = new ParallelTransition(fadeFrom, fadeTo, fadeFromPane, fadeToPane);
        animation.setOnFinished(__ -> {
            toBG.toFront();
            fromBG.setOpacity(1);
            fromPane.setVisible(false);
            fromPane.setOpacity(1);
            cb.run();
        });
        animation.play();
        root.requestFocus();
    }
    
    static void initialize() {
        root.setPrefSize(900, 600);
        root.getStylesheets().add("style/home.css");
        mainBG.getStyleClass().addAll("home-bg", "home-bg1");
        statisticBG.getStyleClass().addAll("home-bg", "home-bg2");
        StackPane bgContainer = new StackPane(statisticBG, mainBG);
        root.getChildren().addAll(bgContainer, effects, statistics, main);
        root.setOnMouseClicked(event -> root.requestFocus());
        root.requestFocus();
        main.enableImportData();
        Music.init();
        loop.restart();
    }
    
    static void pause() {
        loop.cancel();
        Music.pause();
    }
    
    public static void returnHome(boolean gameEnd) {
        loop.restart();
        Music.play();
        main.right.continueGame.setDisable(gameEnd);
        if (gameEnd) {
            main.setVisible(false);
            statistics.setVisible(true);
            statistics.dbPane.hide();
            statisticBG.toFront();
            statistics.toFront();
            statistics.dbPane.runAnimation();
        }
    }
}
