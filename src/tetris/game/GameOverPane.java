/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class GameOverPane extends VBox {
    FadeTransition fade;
    
    public GameOverPane() {
        Text over = new Text("Game Over");
        VBox.setMargin(over, new Insets(100, 0, 0, 0));
        over.getStyleClass().add("main-go-gameover");
        
        Text wait = new Text("Please wait...");
        wait.getStyleClass().add("main-go-wait");
                
        getStyleClass().add("main-go-pane");
        setAlignment(Pos.TOP_CENTER);
        getChildren().addAll(over, wait);
        
        fade = new FadeTransition(Duration.millis(500), wait);
        fade.setFromValue(1);
        fade.setToValue(0.3);
        fade.setCycleCount(Transition.INDEFINITE);
        fade.setAutoReverse(true);
    }
    
    void show() {
        fade.play();
        setVisible(true);
    }
    
    void hide() {
        fade.stop();
        setVisible(false);
    }
}
