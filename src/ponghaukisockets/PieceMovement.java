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
    private static boolean isAnyMoving = false;
    private static int delay_ = 1500;
    
    public static void moveCircle(Circle circ){
        moveVertically(circ);
    }
    
    public static void moveToCenter(Circle circ){
        if(isAnyMoving == true) return; isAnyMoving = true;//só uma peça movimenta por vez 
        
        double x = circ.localToScene(circ.getBoundsInLocal()).getMinX();
        double y = circ.localToScene(circ.getBoundsInLocal()).getMinY();
        
        TranslateTransition translateTransition = new TranslateTransition();  
        translateTransition.setDuration(Duration.millis(delay_)); 
        translateTransition.setNode(circ);
        
        translateTransition.setByX(x > centerGemeX ? -207 : +207);
        translateTransition.setByY(y > centerGemeY ? -160 : +160);
        translateTransition.setAutoReverse(false); 
        translateTransition.setOnFinished((e)->{
            isAnyMoving = false;
        });
        
        translateTransition.play();
    }
    
    public static void moveVertically(Circle circ){
        if(isAnyMoving == true) return; isAnyMoving = true;//só uma peça movimenta por vez 
        
        double y = circ.localToScene(circ.getBoundsInLocal()).getMinY();
        
        TranslateTransition translateTransition = new TranslateTransition();  
        translateTransition.setDuration(Duration.millis(delay_)); 
        translateTransition.setNode(circ);
        translateTransition.setByY(y > centerGemeY ? -318 : +318);
        translateTransition.setAutoReverse(false); translateTransition.setOnFinished((e)->{
            isAnyMoving = false;
        });
        
        translateTransition.play();
    }
    
    public static void moveHorizontally(Circle circ){
        if(isAnyMoving == true) return; isAnyMoving = true;//só uma peça movimenta por vez 
        
        double x = circ.localToScene(circ.getBoundsInLocal()).getMinX();
        double y = circ.localToScene(circ.getBoundsInLocal()).getMinY();
        if(y < centerGemeY){
            return;
        }
        TranslateTransition translateTransition = new TranslateTransition();  
        translateTransition.setDuration(Duration.millis(delay_)); 
        translateTransition.setNode(circ);
        translateTransition.setByX(x > centerGemeX ? -412 : +412);
        translateTransition.setAutoReverse(false); 
        translateTransition.setOnFinished((e)->{
            isAnyMoving = false;
        });
        translateTransition.play();
    }
}
