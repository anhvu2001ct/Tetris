/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.home;

import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

class Content1 extends HBox {

    Image im1 = new Image("/img/step1.png");
    ImageView st1 = new ImageView(im1);
    Label label = new Label();

    public Content1() {
        st1.setFitHeight(400);
        st1.setFitWidth(700);
        label.setGraphic(st1);
        setAlignment(Pos.CENTER);
        getChildren().add(label);
    }
}

class Content2 extends HBox {

    Image im2 = new Image("/img/step2.png");
    ImageView st2 = new ImageView(im2);
    Label label = new Label();

    public Content2() {
        st2.setFitHeight(450);
        st2.setFitWidth(700);
        label.setGraphic(st2);
        setAlignment(Pos.CENTER);
        getChildren().add(label);
    }
}

class Content3 extends HBox {

    Image im3 = new Image("/img/step3.png");
    ImageView st3 = new ImageView(im3);
    Label label = new Label();

    public Content3() {
        st3.setFitHeight(450);
        st3.setFitWidth(700);
        label.setGraphic(st3);
        setAlignment(Pos.CENTER);
        getChildren().add(label);
    }
}

class Content4 extends HBox {
    Image im4 = new Image("/img/step4.png");
    ImageView st4 = new ImageView(im4);
    Label label = new Label();

    public Content4() {
        st4.setFitHeight(400);
        st4.setFitWidth(700);
        label.setGraphic(st4);
        setAlignment(Pos.CENTER);
        getChildren().add(label);
    }
}

class Navigator extends HBox {
    Image image = new Image("/img/next.png");
    ImageView next = new ImageView(image);
    Image images = new Image("/img/previous.png");
    ImageView previous = new ImageView(images);
    Button left = new Button("");
    Button right = new Button("");

    public Navigator() {
        previous.setFitHeight(20);
        previous.setFitWidth(50);
        left.getStyleClass().add("button");
        left.setGraphic(previous);

        next.setFitHeight(20);
        next.setFitWidth(50);
        left.setMaxSize(100, 200);
        right.setGraphic(next);
        right.getStyleClass().add("button");
        getChildren().addAll(left, right);
        setAlignment(Pos.CENTER);
        setSpacing(20);
    }
}

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class H2PPane extends VBox {

    StackPane container = new StackPane();
    Rectangle clip = new Rectangle();
    Node[] data = new Node[4];
    Navigator nav = new Navigator();

    public H2PPane() {
        getStylesheets().add("style/h2p.css");

        data[0] = new Content1();
        data[1] = new Content2();
        data[2] = new Content3();
        data[3] = new Content4();

        container.getChildren().addAll(data);
        container.setClip(clip);
        VBox.setVgrow(container, Priority.ALWAYS);
        container.heightProperty().addListener(__ -> {
            clip.setWidth(container.getWidth());
            clip.setHeight(container.getHeight());
        });

        getChildren().addAll(container, nav);
        init();

    }

    int curId = 0;
    boolean animating = false;
    
    void allowAnimating() { animating = false; }

    void changeRight() {
        if (animating) return;
        animating = true;
        int oldId = curId;
        curId = (curId + 1) % data.length;
        
        TranslateTransition oldPane = new TranslateTransition(Duration.millis(500), data[oldId]);
        oldPane.setByX(-container.getWidth());
        
        TranslateTransition newPane = new TranslateTransition(Duration.millis(500), data[curId]);
        newPane.setFromX(container.getWidth());
        newPane.setToX(0);
        data[curId].setVisible(true);
        
        SequentialTransition ani = new SequentialTransition(oldPane, newPane);
        ani.setOnFinished(__ -> {
            data[oldId].setVisible(false);
            allowAnimating();
        });
        ani.play();
    }

    void changeLeft() {
        if (animating) return;
        animating = true;
        int oldId = curId;
        curId = (curId - 1 + data.length) % data.length;
        
        TranslateTransition oldPane = new TranslateTransition(Duration.millis(500), data[oldId]);
        oldPane.setByX(container.getWidth());
        
        TranslateTransition newPane = new TranslateTransition(Duration.millis(500), data[curId]);
        newPane.setFromX(-container.getWidth());
        newPane.setToX(0);
        data[curId].setVisible(true);
        
        SequentialTransition ani = new SequentialTransition(oldPane, newPane);
        ani.setOnFinished(__ -> {
            data[oldId].setVisible(false);
            allowAnimating();
        });
        ani.play();
    }

    void init() {
        for (int i = 0; i < data.length; ++i) data[i].setVisible(i == curId);

        nav.left.setOnAction(__ -> changeLeft());
        nav.right.setOnAction(__ -> changeRight());
    }
}