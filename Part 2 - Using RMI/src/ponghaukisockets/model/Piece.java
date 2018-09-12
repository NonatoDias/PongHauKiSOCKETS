/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets.model;

import javafx.scene.shape.Circle;

/**
 *
 * @author Nonato Dias
 */
public class Piece {

    public Circle circle;
    public int position;
    private int initalPosition;
    double initialX;
    double initialY;

    public Piece(Circle circle, int position) {
        this.circle = circle;
        this.position = position;
        this.initalPosition = position;
        this.initialX = circle.localToScene(circle.getBoundsInLocal()).getMinX();
        this.initialY = circle.localToScene(circle.getBoundsInLocal()).getMinY();
        
        //System.out.println("--------------  piece "+ this.initialX+ "   -   "+this.initialY);
    } 

    void toInitialPosition() {
        this.position = this.initalPosition;
    }
}
