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
    private Circle pecaAmareloA;

    @FXML
    private Circle pecaAzulA;

    @FXML
    private Circle pecaAzulB;

    @FXML
    private Circle pecaAmareloB;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        pecaAzulA.setOnMouseClicked((e)->{
            PieceMovement.moveCircle(pecaAzulA);
        });
        
        pecaAzulB.setOnMouseClicked((e)->{
            PieceMovement.moveCircle(pecaAzulB);
        });
        
        pecaAmareloA.setOnMouseClicked((e)->{
            PieceMovement.moveCircle(pecaAmareloA);
        });
        
        pecaAmareloB.setOnMouseClicked((e)->{
            PieceMovement.moveCircle(pecaAmareloB);
        });
        
    }   
    
    public void circleClick(){
        PieceMovement.moveCircle(pecaAmareloB);
    }
}