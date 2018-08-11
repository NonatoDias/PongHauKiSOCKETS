/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

/**
 *
 * @author Nonato Dias
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private AnchorPane an_message;
    @FXML
    private AnchorPane an_content;

    @FXML
    private JFXTextField jfxTf_message;

    @FXML
    private Circle circuloAmareloA;

    @FXML
    private Circle circuloAzulA;

    @FXML
    private Circle circuloAzulB;

    @FXML
    private Circle circuloAmareloB;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                
        PieceMap pieceMap = new PieceMap();
        pieceMap.setPieceblueA(new Piece(circuloAzulA, 1));
        pieceMap.setPieceblueB(new Piece(circuloAzulB, 2));
        pieceMap.setPieceYellowA(new Piece(circuloAmareloA, 3));
        pieceMap.setPieceYellowB(new Piece(circuloAmareloB, 4));
        
        //Events
        circuloAzulA.setOnMouseClicked((e)->{
            pieceMap.moveBlueA();
        });
        
        circuloAzulB.setOnMouseClicked((e)->{
            pieceMap.moveBlueB();
        });
        
        circuloAmareloA.setOnMouseClicked((e)->{
            pieceMap.moveYellowA();
        });
        
        circuloAmareloB.setOnMouseClicked((e)->{
            pieceMap.moveYellowB();
        });
    }   
    
    public void circleClick(){
        
    }
}
