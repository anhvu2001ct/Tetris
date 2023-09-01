/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

import java.util.function.Function;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

class CustomCheckBox extends HBox {
   Function<Boolean, Void> trigger;

    public CustomCheckBox(String text, boolean defaultValue, Function<Boolean, Void> cb) {
        trigger = cb;
        Label textLabel = new Label(text);
        textLabel.setMinWidth(200);
        textLabel.getStyleClass().addAll("paused-item-text", "paused-checkbox-text");
        CheckBox checkBox = new CheckBox();
        checkBox.getStyleClass().add("paused-checkbox");
        checkBox.setSelected(defaultValue);
        checkBox.selectedProperty().addListener((ov, oldVal, newVal) -> trigger.apply(newVal));
        getChildren().addAll(textLabel, checkBox);
        setSpacing(20);
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(0, 0, 0, 20));
    }
}

class VolumePane extends HBox {
    static Stop[] sliderColor = {new Stop(0, Color.RED), new Stop(1, Color.web("#25cf4c"))};
    Pane sliderContainer;
    Rectangle rectangle;
    DropShadow shadow = new DropShadow();
    Function<Double, Void> trigger;
    
    private void valueChange(double rate, double width) {
        Color color = PausePane.getColorByRate(rate, sliderColor);
        rectangle.setWidth(rate * width);
        rectangle.setFill(color.brighter());
        shadow.setColor(color);
    }
    
    public VolumePane(String text, Function<Double, Void> cb) {
        trigger = cb;
        Label textLabel = new Label(text);
        textLabel.setMinWidth(180);
        textLabel.getStyleClass().add("paused-item-text");

        Slider slider = new Slider(0, 1, 1);
        slider.setMinWidth(170);
        slider.getStyleClass().add("paused-volume-slider");
        
        Rectangle maxRect = new Rectangle(0, 0, 0, 0);
        maxRect.getStyleClass().add("paused-volume-full");
        
        rectangle = new Rectangle(0, 0, 0, 0);
        slider.widthProperty().addListener(__ -> {
            Double rate = Double.valueOf(1);
            valueChange(rate, slider.getWidth());
            maxRect.setWidth(slider.getWidth());
            trigger.apply(rate);
        });
        maxRect.heightProperty().bind(slider.heightProperty());
        maxRect.arcHeightProperty().bind(rectangle.heightProperty());
        maxRect.arcWidthProperty().bind(rectangle.arcHeightProperty());
        rectangle.heightProperty().bind(slider.heightProperty());
        rectangle.arcHeightProperty().bind(rectangle.heightProperty());
        rectangle.arcWidthProperty().bind(rectangle.arcHeightProperty());
        slider.valueProperty().addListener((ov, oldVal, newVal) -> {
            Double val = newVal.doubleValue();
            valueChange(val, slider.getWidth());
            trigger.apply(val);
        });

        Pane tmp = new Pane(), tmp2 = new Pane();
        VBox.setVgrow(tmp, Priority.ALWAYS);
        VBox.setVgrow(tmp2, Priority.ALWAYS);
        sliderContainer = new Pane(maxRect, rectangle, slider);
        sliderContainer.setEffect(shadow);

        getChildren().addAll(textLabel, new VBox(tmp, sliderContainer, tmp2));
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(0, 0, 0, 20));
    }
}

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class PausePane extends HBox {
    class CustomButton extends Button {
        String style;

        public CustomButton(String text, String styleClass) {
            super(text);
            style = "paused-button-" + styleClass;
            getStyleClass().addAll("paused-button", style);
        }

        public void changeStyle(String styleClass) {
            getStyleClass().remove(style);
            style = "paused-button-" + styleClass;
            getStyleClass().add(style);
        }
    }

    private VBox pane = new VBox();
    private boolean showed = false;
    private boolean locked = false;

    public PausePane() {
      Pane tmp = new Pane(), tmp2 = new Pane();
      HBox.setHgrow(tmp, Priority.ALWAYS);
      HBox.setHgrow(tmp2, Priority.ALWAYS);
      getStyleClass().add("bg-color");
      createPane();
      getChildren().addAll(tmp, pane, tmp2);
    }
    
    public boolean isPaused() {
        return showed;
    }
    
    public boolean isLocked() {
        return locked;
    }
    
    public void forceUnPaused() {
        showed = false;
    }
    
    private void toggle() {
        showed ^= true;
        toggleLock();
    }
    
    private void toggleLock() {
        locked ^= true;
    }
    
    private static Color getColorByRate(Color left, Color right, double rate) {
        return Color.color(left.getRed() * rate + right.getRed() * (1.0-rate),
                         left.getGreen() * rate + right.getGreen() * (1.0-rate),
                         left.getBlue() * rate + right.getBlue() * (1.0-rate));
    }
    
    public static Color getColorByRate(double rate, Stop... stops) {
        int bound = 0;
        for (; bound < stops.length; ++bound) {
            if (rate <= stops[bound].getOffset()) break;
        }
        Stop left = stops[Math.max(0, bound-1)], right = stops[Math.min(bound, stops.length-1)];
        if (left == right) return left.getColor();
        return getColorByRate(left.getColor(), right.getColor(), (right.getOffset() - rate) / (right.getOffset() - left.getOffset()));
    }
    
    private CustomButton homeButton;
    private void createPane() {
        pane.setPrefSize(400, Region.USE_COMPUTED_SIZE);
        pane.setPadding(new Insets(20, 0, 20, 0));
        pane.setSpacing(20);
        Label pauseText = new Label("Game paused");
        pauseText.getStyleClass().add("paused-pause-text");
        StackPane pauseTextContainer = new StackPane(pauseText);
        VBox.setMargin(pauseTextContainer, new Insets(0, 0, 30, 0));
        pane.getChildren().add(pauseTextContainer);
        
        CustomButton resumeButton = new CustomButton("Resume", "primary");
        resumeButton.setOnAction(__ -> PausedEventHandler.resumeButtonClicked(resumeButton));
        homeButton = new CustomButton("Home", "secondary");
        homeButton.setOnAction(__ -> PausedEventHandler.homeButtonClicked(homeButton));
        pane.getChildren().addAll(new StackPane(resumeButton), new StackPane(homeButton));
        
        CustomCheckBox ghostCheckBox = new CustomCheckBox("Enable Ghost-piece", true, PausedEventHandler::ghostPieceToggled);
        VBox.setMargin(ghostCheckBox, new Insets(30, 0, 0, 0));
        pane.getChildren().add(ghostCheckBox);
        
        CustomCheckBox gridCheckBox = new CustomCheckBox("Show grid", false, PausedEventHandler::gridShowToggled);
        VBox.setMargin(gridCheckBox, new Insets(5, 0, 0, 0));
        pane.getChildren().add(gridCheckBox);
        
        VolumePane musicVolume = new VolumePane("Music volume", PausedEventHandler::musicVolumeChanged);
        VBox.setMargin(musicVolume, new Insets(10, 0, 0, 0));
        VolumePane soundVolume = new VolumePane("SoundFX volume", PausedEventHandler::soundVolumeChanged);
        pane.getChildren().addAll(musicVolume, soundVolume);
    }
    
    private int[] borders = {1};
    public void changeBorders() {
        for (int i = 0; i < borders.length; ++i) {
            Pane border = (Pane)Game.mainPane.getChildren().get(borders[i]);
            border.getStyleClass().clear();
            border.getStyleClass().add("main-border-paused");
        }
        Game.expPane.changeState(false);
    }
    
    public void unchangeBorders() {
        for (int i = 0; i < borders.length; ++i) {
            Pane border = (Pane)Game.mainPane.getChildren().get(borders[i]);
            border.getStyleClass().clear();
            border.getStyleClass().add("main-border-normal");
        }
        Game.expPane.changeState(true);
    }
    
    public void pause() {
        if (locked || isPaused()) return;
        toggle();
        Game.auto.pause();
        toFront();
        changeBorders();
        pane.setTranslateY(-pane.getHeight());
        homeButton.changeStyle("secondary");
        pane.setEffect(new GaussianBlur(5));
        FadeTransition fade = new FadeTransition(Duration.millis(500), this);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setOnFinished(__ -> {
            TranslateTransition translate = new TranslateTransition(Duration.millis(250), pane);
            translate.setFromY((-pane.getHeight()));
            translate.setToY(0);
            translate.setInterpolator(Interpolator.EASE_OUT);
            translate.setOnFinished(___ -> {
                pane.setEffect(null);
                toggleLock();
            });
            translate.play();
        });
        fade.play();
    }
    
    public void resume() {
        if (locked || !isPaused()) return;
        toggle();
        FadeTransition fade = new FadeTransition(Duration.millis(800), this);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(__ -> {
            Game.auto.play();
            unchangeBorders();
            toggleLock();
            toBack();
        });
        fade.play();
    }
}

