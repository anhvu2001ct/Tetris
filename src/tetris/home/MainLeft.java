/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.home;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import static tetris.Tetris.RNG;

class TetrisText extends HBox {
    String text = "TETRIS";
    Text[] letters = new Text[text.length()];
    int index = -1;
    double time = 0;
    Color color;
    
    private void nextColor() {
        color = Color.rgb(RNG.nextInt(256), RNG.nextInt(256), RNG.nextInt(256), 0.75).brighter();
    }
    
    public TetrisText() {
        getStyleClass().add("main-left-tetris");
        setAlignment(Pos.CENTER);
        for (int i = 0; i < text.length(); ++i) {
            letters[i] = new Text(String.valueOf(text.charAt(i)));
            getChildren().add(letters[i]);
        }
        setSpacing(10);
        VBox.setMargin(this, new Insets(0, 0, 10, 0));
        
        Home.loop.addEvent(() -> {
            time += Home.loop.getElapsedTime();
            if (time > 1) {
                int prev = index;
                index = (index + 1) % text.length();
                if (index == 0) nextColor();
                if (prev != -1) letters[prev].setStroke(null);
                letters[index].setFill(color);
                letters[index].setStroke(color);
                time = 0;
            }
        });
    }
}

class PlayerNameInput extends HBox {
    private final int maxCharNum = 15;
    
    private TextField field = new TextField();
    private boolean state = false;
    
    public PlayerNameInput() {
        field.getStyleClass().addAll("main-left-name", "main-left-name-fail");
        field.setAlignment(Pos.CENTER);
        field.setPromptText("Player Name");
        getChildren().add(field);
        setAlignment(Pos.CENTER);
        enableValidation();
    }
    
    public boolean isReady() {
        return state;
    }
    
    public String getName() {
        return field.getText();
    }
    
    public void askForInput() {
        ScaleTransition scale = new ScaleTransition(Duration.millis(300), field);
        scale.setFromX(1); scale.setToX(1.3);
        scale.setFromY(1); scale.setToY(1.3);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
        Platform.runLater(() -> field.requestFocus());
    }
    
    private void changeState(boolean newState) {
        if (newState == state) return;
        String success = "main-left-name-success", fail = "main-left-name-fail";
        field.getStyleClass().remove(state ? success : fail);
        state = newState;
        field.getStyleClass().add(state ? success : fail);
    }
    
    private void enableValidation() {
        field.textProperty().addListener((osv, o, n) -> {
            n = n.replaceAll("\\s", "");
            if (n.isEmpty()) changeState(false);
            else {
                changeState(true);
                if (n.length() > maxCharNum) n = n.substring(0, maxCharNum);
            }
            field.setText(n);
        });
    }
}

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class MainLeft extends VBox {
    PlayerNameInput playerNameInput = new PlayerNameInput();
    
    public MainLeft() {
        setPrefWidth(450);
        setAlignment(Pos.CENTER);
        
        Label projectText = new Label("P R O J E C T:");
        projectText.getStyleClass().add("main-left-project");
        
        Label volumeRemaider = new Label("Press Ctrl +/- to change volume");
        volumeRemaider.getStyleClass().add("main-left-volume");
        volumeRemaider.setTranslateY(15);
        
        getChildren().addAll(projectText, new TetrisText(), playerNameInput, volumeRemaider);
    }
}
