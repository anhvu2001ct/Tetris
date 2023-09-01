/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

class ProgressTransition extends Transition {
    private Rectangle node;
    private double oldYPos;
    private double oldHeight, newHeight;
    
    public ProgressTransition(Duration duration, Rectangle node) {
        setCycleDuration(duration);
        this.node = node;
    }
    
    public void setHeight(double newHeight) {
        oldHeight = node.getHeight();
        oldYPos = node.getY();
        this.newHeight = newHeight;
    }
    
    @Override
    protected void interpolate(double frac) {
        double value = (newHeight - oldHeight)*frac;
        node.setHeight(oldHeight + value);
        node.setY(oldYPos - value);
    }
}

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class ExpPane extends Pane {
    private int topWidth = 0;
    private int bottomWidth = 3;
    private int leftWidth = 2;
    private int rightWidth = 2;
    
    private final Border NORMAL = new Border(new BorderStroke(Color.rgb(32, 178, 170), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(topWidth, rightWidth, bottomWidth, leftWidth)));
    private final Border PAUSED = new Border(new BorderStroke(Color.rgb(205, 40, 40), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(topWidth, rightWidth, bottomWidth, leftWidth)));

    Rectangle progress = new Rectangle();
    ProgressTransition pt = new ProgressTransition(Duration.millis(700), progress);
    
    public double getInnerWidth() {
        return 9;
    }
    
    public double getInnerHeight() {
        return getHeight() - topWidth - bottomWidth;
    }
    
    public void changeState(boolean state) {
        getStyleClass().clear();
        if (state) {
            progress.setFill(Color.rgb(0, 230, 184));
            getStyleClass().add("main-expbar-normal");
            setBorder(NORMAL);
        } else {
            progress.setFill(Color.rgb(232, 118, 118));
            getStyleClass().add("main-expbar-paused");
            setBorder(PAUSED);
        }
    }
    
    public void increaseProgress(int currentExp, int expNeed, boolean reset) {
        pt.stop();
        double newHeight = 1.0*currentExp / expNeed * getInnerHeight();
        pt.setHeight(newHeight);
        pt.play();
    }
    
    public void reset() {
        progress.setY(getHeight() - bottomWidth);
        progress.setHeight(0);
    }
    
    public ExpPane() {
        pt.setInterpolator(Interpolator.EASE_OUT);

        heightProperty().addListener(__ -> {
            progress.setX(leftWidth);
            progress.setWidth(getInnerWidth());
            progress.setY(getHeight() - bottomWidth);
        });
        
        setPrefSize(getInnerWidth() + leftWidth + rightWidth, Config.HEIGHT * Config.SIZE);
        HBox.setMargin(this, new Insets(0, 5, 0, 5));
        changeState(true);
        getChildren().add(progress);
    }
}
