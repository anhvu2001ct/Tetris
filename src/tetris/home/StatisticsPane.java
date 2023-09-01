/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.home;

import java.util.List;
import javafx.animation.Transition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

class PlayerScorePane extends VBox {
    Label header, score, level, line, b2b, perfect;
    Label singleCnt, doubleCnt, tripleCnt, tetrisCnt;
    
    public PlayerScorePane() {
        setPrefWidth(300);
        header      = createHeader();
        score       = createItem("Score");
        level       = createItem("Level");
        line        = createItem("Total Line Cleared");
        b2b         = createItem("Maximum B2B Combo");
        perfect     = createItem("Total Perfect Clear");
        singleCnt   = createItem("Total Single");
        doubleCnt   = createItem("Total Double");
        tripleCnt   = createItem("Total Triple");
        tetrisCnt   = createItem("Total Tetris");
        getStyleClass().add("pane-shadow");
    }
    
    private Label createHeader() {
        Label label = new Label("No game played");
        label.getStyleClass().add("statistic-player-header-text");
        
        StackPane header = new StackPane(label);
        header.getStyleClass().add("statistic-player-header");
        
        getChildren().add(header);
        return label;
    }
    
    private Label createItem(String name) {
        Label labelName = new Label(name);
        labelName.getStyleClass().add("statistic-player-item-name");
        
        Label labelScore = new Label("N/A");
        labelScore.getStyleClass().add("statistic-db-item-score");
        
        HBox result = new HBox(labelName, labelScore);
        result.setAlignment(Pos.CENTER_LEFT);
        result.getStyleClass().add("statistic-player-item");
        getChildren().add(result);
        return labelScore;
    }
    
    public void update() {
        GameResult result = GameResult.currentGame;
        header.setText(String.format("%s (#%d)", result.name, GameResult.dashboard.indexOf(result)+1));
        score.setText(String.valueOf(result.score));
        level.setText(String.valueOf(result.level));
        line.setText(String.valueOf(result.totalLineClear));
        b2b.setText(String.valueOf(result.maxB2B));
        perfect.setText(String.valueOf(result.totalPerfectClear));
        singleCnt.setText(String.valueOf(result.lineClearCombo[0]));
        doubleCnt.setText(String.valueOf(result.lineClearCombo[1]));
        tripleCnt.setText(String.valueOf(result.lineClearCombo[2]));
        tetrisCnt.setText(String.valueOf(result.lineClearCombo[3]));
    }
}

class ScrollTransition extends Transition {
    Pane node;
    Rectangle clip;
    
    public ScrollTransition(Duration duration, Pane node, Rectangle clip) {
        setCycleDuration(duration);
        this.node = node;
        this.clip = clip;
    }
    
    @Override
    protected void interpolate(double frac) {
        clip.setHeight(frac * node.getHeight());
    }
}

class DashBoardPane extends Pane {
    private int maxUser = 10;
    
    Rectangle clip = new Rectangle();
    VBox container = new VBox();
    
    public DashBoardPane() {
        createHeader();
        for (int i = 0; i < maxUser; ++i) container.getChildren().add(new Pane());
        container.setPrefWidth(350);
        clip.setWidth(container.getPrefWidth());
        setClip(clip);
        setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        getChildren().add(container);
        getStyleClass().add("pane-shadow");
        update();
    }
    
    private ScrollTransition animation = new ScrollTransition(Duration.millis(1250), this, clip);
    public void runAnimation() {
        animation.play();
    }
    
    public void hide() {
        clip.setHeight(0);
    }
    
    public void update() {
        List<GameResult> li = GameResult.dashboard;
        for (int i = 0; i < maxUser; ++i) {
            if (i < li.size()) changeItem(i, li.get(i).name, String.valueOf(li.get(i).score), li.get(i)==GameResult.currentGame);
            else changeItem(i, " ", "N/A", false);
        }
    }
    
    private void changeItem(int index, String name, String score, boolean isPlayer) {
        Label labelIndex = new Label("#" + (index+1));
        labelIndex.getStyleClass().add("statistic-db-item-index");

        Label labelName = new Label(name);
        labelName.getStyleClass().add("statistic-db-item-name");
        
        Label labelScore = new Label(score);
        labelScore.getStyleClass().add("statistic-db-item-score");
        
        HBox result = new HBox(labelIndex, labelName, labelScore);
        result.setAlignment(Pos.CENTER_LEFT);
        result.getStyleClass().add(isPlayer ? "statistic-db-item-special" : "statistic-db-item");
        container.getChildren().set(index+1, result);
    }
    
    private void createHeader() {
        Label label = new Label("Dashboard");
        label.getStyleClass().add("statistic-db-header-text");
        
        StackPane header = new StackPane(label);
        header.getStyleClass().add("statistic-db-header");
        
        container.getChildren().add(header);
    }
}

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class StatisticsPane extends HBox {
    PlayerScorePane playerPane = new PlayerScorePane();
    DashBoardPane dbPane = new DashBoardPane();
    
    public StatisticsPane() {
        Button button = new Button("");
        button.setLayoutX(10);
        button.setLayoutY(10);
        button.setOnAction(__ -> Home.changeBG(Home.mainBG, () -> {}));
        button.getStyleClass().add("statistic-left-back");
        
        Pane leftPane = new Pane(button);
        leftPane.setPrefWidth(70);
        
        HBox rightPane = new HBox(playerPane, dbPane);
        rightPane.setSpacing(60);
        rightPane.setAlignment(Pos.CENTER);
        VBox rightPaneContainer = new VBox(rightPane);
        rightPaneContainer.setAlignment(Pos.CENTER);
//        rightPaneContainer.setStyle("-fx-background-color: white");
        HBox.setHgrow(rightPaneContainer, Priority.ALWAYS);
        
        getChildren().addAll(leftPane, rightPaneContainer);
        setVisible(false);
    }    
}
