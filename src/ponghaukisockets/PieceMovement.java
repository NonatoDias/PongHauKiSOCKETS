/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;

import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 *
 * @author Nonato Dias
 */
public class PieceMovement {
    
    public static final int CENTER  = 0;
    
    private static double centerGemeX = 337;
    private static double centerGemeY = 325;
    
    public static void moveCircle(Circle circ){
        moveToCenter(circ);
    }
    
    private static void moveToCenter(Circle circ){
        double x = circ.localToScene(circ.getBoundsInLocal()).getMinX();
        double y = circ.localToScene(circ.getBoundsInLocal()).getMinY();
        
        TranslateTransition translateTransition = new TranslateTransition();  
        translateTransition.setDuration(Duration.millis(2100)); 
        translateTransition.setNode(circ);
        
        translateTransition.setToX(x > centerGemeX ? -207 : +207);
        translateTransition.setToY(y > centerGemeY ? -160 : +160);
        translateTransition.setOnFinished((e)->{
            //callback
        });
        
        translateTransition.play();
        
    }
}
