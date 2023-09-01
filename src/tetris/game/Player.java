/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.game;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import static tetris.home.GameResult.saveResult;

class Block extends Pane {
    private static final boolean[] special = {true, false, false, false, false, true, true};
    
    private int tetriminoID;
    private int index;
    
    public void changeBlock(int tetriID) {
        tetriminoID = tetriID;
        setRotation(3);
    }
    
    public int getTetriID() {
        return tetriminoID;
    }
    
    public Color getColor() {
        return Tetriminos.tetri[tetriminoID].color;
    }
    
    public Color getBrightColor() {
        return Tetriminos.tetri[tetriminoID].colorBrighter;
    }
    
    public Tetrimino getTetrimino() {
        return Tetriminos.tetri[tetriminoID].get(index);
    }
    
    public Tetrimino getTetrimino(int index) {
        return Tetriminos.tetri[tetriminoID].get(index);
    }
    
    public int getRightRotationID() {
        int result = index + 1;
        // Special cases (I/S/Z)
        if (special[tetriminoID] && result == 5) result = 3;
        else if (result == 7) result = 3;
        return result;
    }

    public int getLeftRotationID() {
        int result = index - 1;
        // Special cases (I/S/Z)
        if (special[tetriminoID] && result == 1) result = 3;
        else if (result == -1) result = 3;
        return result;
    }
    
    public void setRotation(int index) {
        getChildren().clear();
        Tetrimino tetrimino = getTetrimino(this.index = index);
        tetrimino.getData().forEach(point -> {
            Cell cell = new Cell();
            cell.setStroke(Color.rgb(0, 0, 30));
            cell.setColor(getBrightColor());
            cell.setXY(point.x, point.y);
            getChildren().add(cell);
        });
    }
}

class GhostPiece extends Pane {
    int posX, posY;
    
    public void update(int x, int y) {
        Tetrimino tetrimino = Game.player.block.getTetrimino();
        while (GameLogic.downCheck(x, y, tetrimino)) ++y;
        posX = x; posY = y;
        update();
    }
    
    public void update() {
        if (!isVisible()) return;
        getChildren().clear();
        Tetrimino tetrimino = Game.player.block.getTetrimino();
        Color color = Game.player.block.getBrightColor();
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.7);
        for (Pair point: tetrimino.getData()) {
            Cell cell = new Cell();
            cell.setStroke(color);
            cell.setXY(posX + point.x, posY + point.y);
            getChildren().add(cell);
        }
    }
    
    public void toggle() {
        boolean visible = isVisible() ^ true;
        setVisible(visible);
        if (visible) update();
    }
}

class UserData {
    long score = 0;
    int exp = 0;
    int level = 1;
    int line = 0;
    int b2b = -1, b2bMax = 0;
    int[] lineClearCombo = new int[4];
    int perfectCount = 0;
    
    public UserData() {
        Game.rightPane.score.setValue(String.valueOf(score));
        Game.rightPane.line.setValue(String.valueOf(line));
    }
    
    public void increaseScore(int score) {
        Game.rightPane.score.setValue(String.valueOf(this.score += score));
    }
    
    public void increasePerfectClear() {
        ++perfectCount;
        Game.leftPane.comboField.runPerfectClear();
        increaseScore(Config.SCORE_P);
    }
    
    public void increaseExp(int expEarned) {
        this.exp += expEarned;
        int expNeed = Config.getExpNeed(level);
        boolean levelUp = false;
        while (exp >= expNeed) {
            exp -= expNeed;
            expNeed = Config.getExpNeed(++level);
            levelUp = true;
        }
        if (levelUp) Game.auto.changeLevel(level);
        Game.expPane.increaseProgress(exp, expNeed, levelUp);
    }
    
    public void increaseLines(int lines) {
        int expEarned = 0;
        if (lines == 4) {
            Sound.playSound(Sound.TETRIS_SOUND);
            b2bMax = Math.max(b2bMax, ++b2b);
            if (b2b > 0) {
                Game.leftPane.comboField.runB2B(b2b);
                increaseScore(level * Config.SCORE[3]);
                expEarned += Config.LINES[3];
            }
        } else b2b = -1;
        ++lineClearCombo[lines-1];
        Game.leftPane.comboField.runLineClear(Config.CLEARS[lines-1]);
        Game.rightPane.line.setValue(String.valueOf(line += lines));
        expEarned += Config.LINES[lines-1];
        increaseExp(expEarned);
    }
    
    public void save() {
        saveResult(score, level, line, b2bMax, perfectCount, lineClearCombo);
    }
}

class Holder {
    int tetriminoID = -1;
    boolean available = true;
    
    public Holder() {
        Game.leftPane.clearHolder();
    }
    
    public void enable() {
        available = true;
        if (tetriminoID != -1) {
            Game.leftPane.setHolder(Tetriminos.tetri[tetriminoID].get(3), Tetriminos.tetri[tetriminoID].colorBrighter);
        }
    }
    
    public void hold() {
        available = false;
        Block block = Game.player.block;
        Game.leftPane.setHolder(block.getTetrimino(3), Color.rgb(190, 190, 190));
        int currentID = block.getTetriID();
        if (tetriminoID != -1) Game.player.changeTetrimino(tetriminoID);
        else Game.player.changeTetrimino();
        tetriminoID = currentID;
    }
}

class TetriContainer {
    private Generator rng = new Generator();
    int[] tetriIDs;
    
    public TetriContainer() {
        tetriIDs = new int[Game.rightPane.countNextTetri()];
        for (int i = 0; i < tetriIDs.length; ++i) {
            setNextTetrimino(i, rng.getTetrimino());
        }
    }
    
    public int getNextTetrimino() {
        int id = tetriIDs[0];
        for (int i = 1; i < tetriIDs.length; ++i) setNextTetrimino(i-1, tetriIDs[i]);
        setNextTetrimino(tetriIDs.length-1, rng.getTetrimino());
        return id;
    }
    
    private void setNextTetrimino(int idx, int tetriID) {
        tetriIDs[idx] = tetriID;
        Game.rightPane.setNextTetrimino(idx, Tetriminos.tetri[tetriID].get(3), Tetriminos.tetri[tetriID].colorBrighter);
    }
}

/**
 *
 * @author Anh Vu Nguyen {@literal <nganhvu>}
 */
public class Player {
    Block block = new Block();
    GhostPiece ghost = new GhostPiece();
    Holder holder;
    TetriContainer nextTetri;
    UserData data;
    int posX, posY;
    
    public Player() {
        Game.board.addTetrimino(ghost);
        Game.board.addTetrimino(block);
    }
    
    public void reset() {
        holder = new Holder();
        nextTetri = new TetriContainer();
        data = new UserData();
        changeTetrimino();
    }
    
    public void changeTetrimino() {
//        changeTetrimino(0);
        changeTetrimino(nextTetri.getNextTetrimino());
    }
    
    public void changeTetrimino(int tetriID) {
        block.changeBlock(tetriID);
        block.setLayoutY((posY = -3) * Config.SIZE);
        block.setLayoutX((posX = (Config.WIDTH - 4)/2) * Config.SIZE);
        updateGhost();
    }
    
    public void lineClear(int lineNum, boolean isPerfectClear) {
        Sound.playSound(Sound.SCORING_SOUND);
        data.increaseScore(Config.SCORE[lineNum-1] * data.level);
        data.increaseLines(lineNum);
        if (isPerfectClear) data.increasePerfectClear();
    }
    
    public void updateGhost() {
        ghost.update(posX, posY);
    }
    
    public void moveX(int x) {
        block.setLayoutX((posX += x) * Config.SIZE);
    }
    
    public void moveY(int y) {
        block.setLayoutY((posY += y) * Config.SIZE);
    }
}
