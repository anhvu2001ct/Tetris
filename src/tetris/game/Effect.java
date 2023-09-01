/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import tetris.Tetris;

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class Effect {
    private static int count = 0;
    
    static void checkClearEffect() {
        if (count == 0 && Game.board.effectField.getChildren().size() > 0) {
            Game.board.clearEffect();
        }
    }
    
    private static void removeEffect(ActionEvent __) {
        --count;
    }
    
    private static ParallelTransition createCellAnimation(Cell cell) {
        Duration duration = Duration.millis(Tetris.rand(500, 750));
        TranslateTransition translate = new TranslateTransition(duration, cell);;
        translate.setByX(1.5*Tetris.rand(-Config.SIZE, Config.SIZE));
        translate.setByY(-Tetris.rand(Config.SIZE * 1.6, Config.SIZE * 3.2));
        translate.setInterpolator(Interpolator.EASE_OUT);
        
        RotateTransition rotate = new RotateTransition(duration, cell);
        rotate.setByAngle(Tetris.rand(-90, 90));
        translate.setInterpolator(Interpolator.EASE_OUT);
        
        FadeTransition fade = new FadeTransition(duration, cell);
        fade.setFromValue(0.75);
        fade.setToValue(0);
        fade.setInterpolator(Interpolator.EASE_IN);
        return new ParallelTransition(translate, rotate, fade);
    }
    
    private static void makeRowEffect(int row) {
        TheLine line = Game.board.grid.get(row);
        for (int i = 0; i < Config.WIDTH; ++i) {
            Cell cell = new Cell();
            cell.setColor(line.get(i).getColor().brighter());
            cell.setXY(i, (Config.HEIGHT - row - 1));
            ParallelTransition animation = createCellAnimation(cell);
            animation.setOnFinished(Effect::removeEffect);
            Game.board.addEffect(cell);
            ++count;
            animation.play();
        }
    }
    
    static void makeClearEffect(ArrayList<Integer> rows) {
        int lines = rows.size();
        for (int i = lines-1; i >= 0; --i) makeRowEffect(rows.get(i));
    }
    
    private static ParallelTransition createBubbleAnimation(Circle circle) {
        Duration duration = Duration.millis(Tetris.rand(700, 800));
        TranslateTransition translate = new TranslateTransition(duration, circle);
//        translate.setByX(0.1*rand(-Config.SIZE, Config.SIZE));
        translate.setByY(-Tetris.rand(Config.SIZE * 0.5, Config.SIZE * 2));
        translate.setInterpolator(Interpolator.EASE_OUT);
        
        FadeTransition fade = new FadeTransition(duration, circle);
        fade.setFromValue(0.75);
        fade.setToValue(0);
        fade.setInterpolator(Interpolator.EASE_BOTH);
        return new ParallelTransition(translate, fade);
    }
    
    static void makeDropEffect(int x, int y, Tetrimino tetrimino) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (Pair point: tetrimino.getData()) {
            map.put(point.x, Math.min(map.getOrDefault(point.x, Integer.MAX_VALUE), point.y));
        }
        map.forEach((dataX, dataY) -> {
            int newX = (x + dataX) * Config.SIZE;
            int newY = (y + dataY) * Config.SIZE;
            for (int i = Tetris.randInt(2, 3); i > 0; --i) {
                Circle circle = new Circle(newX + Tetris.rand(0, Config.SIZE), newY, Config.SIZE*Tetris.rand(0.15, 0.3), Color.TRANSPARENT);
                circle.setStroke(Color.WHITE);
                ParallelTransition animation = createBubbleAnimation(circle);
                animation.setOnFinished(Effect::removeEffect);
                Game.board.addEffect(circle);
                ++count;
                animation.play();
            }
        });
    }
}
