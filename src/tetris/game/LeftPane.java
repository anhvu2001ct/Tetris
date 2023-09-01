/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

class TypingTransition extends Transition {
    Text node;
    String orgText;
    int current = -1;

    public TypingTransition(Duration duration, Text node) {
        setCycleDuration(duration);
        this.node = node;
        orgText = node.getText();
    }
    
    @Override
    protected void interpolate(double frac) {
        int tmp = (int)Math.round(frac * orgText.length());
        if (current != tmp) {
            current = tmp;
            node.setText(orgText.substring(0, current));
        }
    }
}

class ComboEffect extends AnchorPane {
    public ComboEffect() {
        getChildren().addAll(makeB2BContainer(), makeLineClearContainer(), makePerfectClearContainer());
    }

    private Text backToBack = new Text("B-2-B");
    private Text b2bCombo = new Text("");
    private HBox makeB2BContainer() {
        Font font = Font.font("Cambria", FontWeight.BOLD, 30);
        backToBack.setFont(font); b2bCombo.setFont(font);
        backToBack.setOpacity(0); b2bCombo.setOpacity(0);
        backToBack.setCache(true); backToBack.setCacheHint(CacheHint.SPEED);
        b2bCombo.setCache(true); b2bCombo.setCacheHint(CacheHint.SPEED);
        HBox b2bContainer = new HBox(backToBack, b2bCombo);
        b2bContainer.setSpacing(10);
        b2bContainer.setAlignment(Pos.CENTER);
        AnchorPane.setTopAnchor(b2bContainer, 10.0);
        AnchorPane.setLeftAnchor(b2bContainer, 0.0);
        AnchorPane.setRightAnchor(b2bContainer, 0.0);
        return b2bContainer;
    }
    
    private Text lineClear = new Text("");
    private StackPane makeLineClearContainer() {
        lineClear.setFont(Font.font("VNI-Fato", FontWeight.BOLD, 20));
        lineClear.setOpacity(0);
        lineClear.setFill(Color.WHITE);
        lineClear.setCache(true); lineClear.setCacheHint(CacheHint.SPEED);
        StackPane result = new StackPane(lineClear);
        AnchorPane.setTopAnchor(result, 40.0);
        AnchorPane.setLeftAnchor(result, 0.0);
        AnchorPane.setRightAnchor(result, 0.0);
        return result;
    }
    
    private Text perfectClear = new Text("PERFECT");
    private StackPane makePerfectClearContainer() {
        perfectClear.setFont(Font.font("Baskerville Old Face", FontWeight.BOLD, 30));
        perfectClear.setOpacity(0);
        perfectClear.setCache(true); perfectClear.setCacheHint(CacheHint.SPEED);
        perfectClear.setStyle("-fx-fill: linear-gradient(to right, rgba(255,0,0,1) 0%, rgba(255,154,0,1) 10%, rgba(208,222,33,1) 20%, rgba(79,220,74,1) 30%, rgba(63,218,216,1) 40%, rgba(47,201,226,1) 50%, rgba(28,127,238,1) 60%, rgba(95,21,242,1) 70%, rgba(186,12,248,1) 80%, rgba(251,7,217,1) 90%, rgba(255,0,0,1) 100%);");
        StackPane result = new StackPane(perfectClear);
        AnchorPane.setTopAnchor(result, 70.0);
        AnchorPane.setLeftAnchor(result, 0.0);
        AnchorPane.setRightAnchor(result, 0.0);
        return result;
    }
    
    private ParallelTransition b2bEffect;
    public void runB2B(int num) {
        if (b2bEffect == null) {
            Duration duration =  Duration.millis(2000);
            FadeTransition fade = new FadeTransition(duration, backToBack);
            fade.setFromValue(1); fade.setToValue(0);
            FillTransition fill = new FillTransition(duration, backToBack, Color.WHITE, Color.YELLOW);
            fill.setInterpolator(Interpolator.EASE_OUT);
            
            FadeTransition fade2 = new FadeTransition(duration, b2bCombo);
            fade2.setFromValue(1); fade2.setToValue(0);
            FillTransition fill2 = new FillTransition(duration, b2bCombo, Color.WHITE, Color.RED);
            fill2.setInterpolator(Interpolator.EASE_OUT);
            b2bEffect = new ParallelTransition(fade, fill, fade2, fill2);
        }
        b2bCombo.setText("x" + num);
        b2bEffect.playFromStart();
    }
    
    private SequentialTransition lineClearEffect;
    public void runLineClear(String value) {
        if (lineClearEffect == null) {
            Duration duration =  Duration.millis(1500);
            FadeTransition fade = new FadeTransition(duration, lineClear);
            fade.setFromValue(1); fade.setToValue(0);
            ScaleTransition scale = new ScaleTransition(duration, lineClear);
            scale.setByX(1);
            scale.setByY(1);
            fade.setInterpolator(Interpolator.EASE_IN);
            lineClearEffect = new SequentialTransition(new ParallelTransition(fade, scale));
        }
        lineClear.setText(String.valueOf(value));
        lineClearEffect.playFromStart();
    }
    
    private SequentialTransition perfectClearEffect;
    public void runPerfectClear() {
        if (perfectClearEffect == null) {
            Duration duration =  Duration.millis(1500);
            TypingTransition typing = new TypingTransition(duration, perfectClear);
            FadeTransition fade = new FadeTransition(duration.multiply(0.5), perfectClear);
            fade.setFromValue(1); fade.setToValue(0.3);
            fade.setAutoReverse(true);
            fade.setCycleCount(2);
            FadeTransition fade2 = new FadeTransition(duration, perfectClear);
            fade2.setFromValue(1); fade2.setToValue(0);
            ScaleTransition scale = new ScaleTransition(duration, perfectClear);
            scale.setByX(1);
            perfectClearEffect = new SequentialTransition(new ParallelTransition(fade, typing), new ParallelTransition(fade2, scale));
        }
        perfectClearEffect.playFromStart();
    }
}

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class LeftPane extends VBox {
    private Pane holder;
    ComboEffect comboField;
    
    public LeftPane() {
        setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        getChildren().addAll(createHolder(), createComboField());
    }
    
    private HBox createHolder() {
        Label holdText = new Label("Hold (C)");
        holdText.getStyleClass().add("left-hold-text");
        
        holder = new Pane();

        VBox pane = new VBox(holdText, holder);
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setPrefSize(Config.SIZE * 6, Config.SIZE * 5);

        Pane tmp = new Pane(), tmp2 = new Pane();
        HBox.setHgrow(tmp, Priority.ALWAYS);
        HBox.setHgrow(tmp2, Priority.ALWAYS);
        return new HBox(tmp, pane, tmp2);
    }
    
    public void clearHolder() {
        holder.getChildren().clear();
    }
    
    public void setHolder(Tetrimino tetrimino, Color color) {
        holder.getChildren().clear();
        if (tetrimino == null) return;
        for (Pair point: tetrimino.getData()) {
            Cell cell = new Cell();
            cell.setStroke(Color.rgb(0, 0, 30));
            cell.setColor(color);
            cell.setXY(point.x + 1, point.y + 1);
            holder.getChildren().add(cell);
        }
    }
    
    private ComboEffect createComboField() {
        comboField = new ComboEffect();
        comboField.setPrefSize(200, Region.USE_COMPUTED_SIZE);
        VBox.setVgrow(comboField, Priority.ALWAYS);
        return comboField;
    }
}
