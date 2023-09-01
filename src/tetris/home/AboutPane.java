/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.home;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author Nguyen Quynh Nhu
 */
public class AboutPane extends VBox {
    public AboutPane() {
        getStylesheets().add("style/about.css");
        
        StackPane stackPane = new StackPane();
        
        Text text = new Text("About");
        stackPane.getChildren().add(text);
        
        Label label = new Label("This game \"PROJECT: Tetris\" is a game made by us during studying subject CSD201 at university because ìn order to complete the subject, we must made a simple application/game using java. So, here it is. Enjoy~");
        Label label5 = new Label("The goal of the game is to move the bricks that slowly fall down a rectangle of size 20 rows x 10 columns (on the screen). Where there are tiles, you cannot move to that position. The player arranges the blocks to fill a horizontal line to score points, and that horizontal line disappears.");
        Label author = new Label("Author");
        Label label2 = new Label("Nguyen Anh Vu");
        Label label3 = new Label("Duong Van Thanh Cong");
        Label label4 = new Label("Nguyen Quynh Nhu");
        
        label.setWrapText(true);
        label5.setWrapText(true);
        label.setTextAlignment(TextAlignment.JUSTIFY);
        label5.setTextAlignment(TextAlignment.JUSTIFY);
        
        text.getStyleClass().add("main-about-title");
        label.getStyleClass().add("main-about-label");
        label5.getStyleClass().add("main-about-label");
        author.getStyleClass().add("main-about-label6");
        label2.getStyleClass().add("main-about-label2");
        label3.getStyleClass().add("main-about-label2");
        label4.getStyleClass().add("main-about-label2");
        
        getChildren().addAll(stackPane, label, label5, author, label2, label3, label4);
    }
    
}