/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.home;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;

class MenuItem extends StackPane {
    Polygon pane;
    Label text;
    
    public MenuItem(String name, Runnable cb) {
        pane = new Polygon(0, 0, 150, 0, 150, 50, 0, 50, -35, 25);
//        pane = new Polygon(0, 0, 150, 0, 150, 50, 0, 50, -15, 37.5, 0, 25, -15, 12.5);
        pane.getStyleClass().add("main-right-item-box");
        pane.setTranslateX(5);
       
        text = new Label(name);
        text.getStyleClass().add("main-right-item-text");

        getChildren().addAll(pane, text);
        setAlignment(Pos.CENTER_RIGHT);
        getStyleClass().add("main-right-item-pane");
        setOnMouseClicked(event -> cb.run());
    }
}

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class MainRight extends HBox {
    MenuItem continueGame;
    
    public MainRight() {
        Pane tmp = new Pane();
        HBox.setHgrow(tmp, Priority.ALWAYS);
        
        continueGame = new MenuItem("Continue", MenuEventHandler::continueGame);
        continueGame.setDisable(true);

        VBox menuContainer = new VBox(continueGame,
                new MenuItem("New Game", MenuEventHandler::playNewGame),
                new MenuItem("Statistics", MenuEventHandler::openStatisticsPane),
                new MenuItem("How to play", MenuEventHandler::openH2P),
                new MenuItem("About", MenuEventHandler::openAbout),
                new MenuItem("Quit", MenuEventHandler::quitGame));
        menuContainer.setAlignment(Pos.CENTER_RIGHT);
        menuContainer.setSpacing(5);
        
        getChildren().addAll(tmp, menuContainer);
        
        HBox.setHgrow(this, Priority.ALWAYS);
    }
}
