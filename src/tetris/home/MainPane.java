/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

class Modal extends StackPane {
    StackPane container = new StackPane();
    H2PPane h2pPane = new H2PPane();
    AboutPane aboutPane = new AboutPane();
    private boolean animating = false;
    
    public Modal() {
        container.getChildren().addAll(h2pPane, aboutPane);
        container.getStyleClass().add("main-modal-container");
        getChildren().add(container);
        getStyleClass().add("main-modal");
        setVisible(false);
        
        container.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> event.consume());
        addEventHandler(MouseEvent.MOUSE_CLICKED, __ -> hide());
    }
    
    public void openH2P() {
        aboutPane.setVisible(false);
        h2pPane.setVisible(true);
        show();
    }
    
    public void openAbout() {
        h2pPane.setVisible(false);
        aboutPane.setVisible(true);
        show();
    }
    
    private void allowAnimating() { animating = false; }
    
    public void show() {
        if (animating) return;
        animating = true;
        TranslateTransition translate = new TranslateTransition(Duration.millis(600), container);
        translate.setFromY(-this.getHeight());
        translate.setToY(0);
        translate.setOnFinished(__ -> allowAnimating());
        setVisible(true);
        translate.play();
    }
    
    public void hide() {
        if (animating) return;
        animating = true;
        TranslateTransition translate = new TranslateTransition(Duration.millis(600), container);
        translate.setByY(-this.getHeight());
        translate.setOnFinished(__ -> {
            allowAnimating();
            setVisible(false);
        });
        translate.play();
    }
}

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class MainPane extends StackPane {
    MainLeft left = new MainLeft();
    MainRight right = new MainRight();
    Modal modal = new Modal();
    

    public MainPane() {
        getChildren().addAll(new HBox(left, right), modal);
    }
    
    private KeyCodeCombination comb = new KeyCodeCombination(KeyCode.U, KeyCodeCombination.CONTROL_DOWN, KeyCodeCombination.ALT_DOWN);
    private Service<Void> onImportData;
    private long currentTime = -1;
    public void enableImportData() {
        if (onImportData == null) {
            onImportData = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            HashSet<String> current = new HashSet<>();
                            GameResult.dashboard.forEach(result -> current.add(result.toString()));
                            ArrayList<GameResult> newData = GameResult.IO.loadData("ext.txt");
                            int count = 0;
                            for (GameResult newResult: newData) {
                                System.out.println(newResult);
                                if (!current.contains(newResult.toString())) {
                                    GameResult.dashboard.add(newResult);
                                    ++count;
                                }
                            }
                            if (count > 0) {
                                System.out.println("Found " + count + " new data. Please wait...");
                                Collections.sort(GameResult.dashboard);
                                GameResult.IO.saveData();
                                Platform.runLater(() -> {
                                    Home.statistics.dbPane.update();
                                    System.out.println("New data imported!");
                                });
                            } else System.out.println("No new data found.");
                            return null;
                        }
                    };
                }
            };
        }
        getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (comb.match(event)) {
                long now = System.nanoTime();
                if (currentTime != -1 && (now - currentTime)/1e9 < 3) {
                    System.out.println("Please wait 3 seconds before importing new data!");
                    return;
                }
                currentTime = now;
                onImportData.restart();
            }
        });
    }
}
