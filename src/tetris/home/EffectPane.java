/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.home;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import tetris.Tetris;

interface EffectObject {
    public boolean update(double t);
}

class Point {
    Point2D point;
    Point2D velocity;
    
    public Point(double width) {
        point = new Point2D(Tetris.rand(width*0.15, width*0.85), Tetris.randInt(-25, -15));
        int dir = Tetris.randInt(0, 1) == 0 ? -1 : 1;
        velocity = new Point2D(Tetris.rand(10, 40)*dir, Tetris.rand(10, 40));
    }
    
    public void move(double t) {
        point = point.add(velocity.multiply(t));
    }
    
    public double getX() {
        return point.getX();
    }
    
    public double getY() {
        return point.getY();
    }
}

class Bubble extends Circle implements EffectObject {
    private static final double minSize = 5;
    private static final double maxSize = 20;

    Point point = new Point(900);
    double lifeTime = Tetris.rand(1, 8);
    double initOpacity = Tetris.rand(0.25, 0.75);
    double decAmount = initOpacity/lifeTime;
    
    public Bubble() {
        setRadius(Tetris.rand(minSize, maxSize));
        setStroke(Color.WHITESMOKE);
        setBlendMode(BlendMode.ADD);
        setCenterX(point.getX());
        setCenterY(point.getY());
    }
    
    @Override
    public boolean update(double t) {
        double newOpacity = getOpacity() - t * decAmount;
        if (newOpacity <= 0) return false;
        point.move(t);
        setCenterX(point.getX());
        setCenterY(point.getY());
        setOpacity(newOpacity);
        return true;
    }
}

class Sparkle extends ImageView implements EffectObject {
    private static final int minSize = 7;
    private static final int maxSize = 23;
    private static final DropShadow shadow = new DropShadow(10, Color.WHITESMOKE);
    private static final Map<Integer, WritableImage> save = new HashMap<>();
    
    Point point = new Point(900);
    double lifeTime = Tetris.rand(1, 8);
    double initOpacity = Tetris.rand(0.25, 0.75);
    double decAmount = initOpacity/lifeTime;
    
    public Sparkle() {
        int size = Tetris.randInt(minSize, maxSize);
        double radius = size/2.0;
        if (!save.containsKey(size)) {
            WritableImage img = new WritableImage(size, size);
            PixelWriter writer = img.getPixelWriter();
            Point2D center = new Point2D((size-1)/2.0, (size-1)/2.0);
            for (int x = 0; x < size; ++x) {
                for (int y = 0; y < size; ++y) {
                    double distance = center.distance(x, y);
                    double alpha = 0;
                    if (distance < radius) alpha = 1.0 - distance/radius;
                    writer.setColor(x, y, Color.rgb(240, 240, 240, alpha));
                }
            }
            save.put(size, img);
        }
        setX(point.getX());
        setY(point.getY());
        setImage(save.get(size));
        setEffect(shadow);
    }
    
    @Override
    public boolean update(double t) {
        double newOpacity = getOpacity() - t * decAmount;
        if (newOpacity <= 0) return false;
        point.move(t);
        setX(point.getX());
        setY(point.getY());
        setOpacity(newOpacity);
        return true;
    }
}

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class EffectPane extends Pane {
    final int maxParticlesNum = 20;
    
    double time = 0;
    
    private boolean shouldAddMore() {
        if (getChildren().size() == maxParticlesNum) return false;
        return Tetris.RNG.nextDouble() > 0.3;
    }
    
    private void add() {
        Node effect = Tetris.randInt(0, 1) == 0 ? (new Bubble()) : (new Sparkle());
        effect.setCache(true);
        effect.setCacheHint(CacheHint.SPEED);
        Platform.runLater(() -> getChildren().add(effect));
    }
    
    public EffectPane() {
        Home.loop.addEvent(() -> {
            double delta = Home.loop.getElapsedTime();
            time += delta;
            Platform.runLater(() -> {
                Iterator<Node> iter = getChildren().iterator();
                while (iter.hasNext()) {
                    EffectObject o = (EffectObject)iter.next();
                    if (!o.update(delta)) iter.remove();
                }
            });
            if (time > 0.6) {
                if (shouldAddMore()) add();
                time = 0;
            }
        });
    }
}
