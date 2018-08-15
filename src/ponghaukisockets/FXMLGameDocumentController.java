/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.Socket;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.animation.TranslateTransition;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

/**
 *
 * @author Nonato Dias
 */
public class FXMLGameDocumentController implements Initializable {
    
    private ClientSocket socket;
    
    private PieceMap pieceMap;
    
    @FXML
    private AnchorPane an_message;
    @FXML
    private AnchorPane an_content;
    
    @FXML
    private StackPane dialogStackPane;

    @FXML
    private JFXTextField jfxTf_message;
    
    @FXML
    private TextFlow msgTextFlow;

    @FXML
    private Circle circuloAmareloA;

    @FXML
    private Circle circuloAzulA;

    @FXML
    private Circle circuloAzulB;

    @FXML
    private Circle circuloAmareloB;
    
    @FXML
    private JFXButton btnStart;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Conexao
        socket = new ClientSocket();
              
        //Peças do jogo
        pieceMap = new PieceMap();
        pieceMap.setPieceblueA(new Piece(circuloAzulA, 1));
        pieceMap.setPieceblueB(new Piece(circuloAzulB, 2));
        pieceMap.setPieceYellowA(new Piece(circuloAmareloA, 3));
        pieceMap.setPieceYellowB(new Piece(circuloAmareloB, 4));
        
        //Events
        addEventsToTheView();
        
        //run
        showDialog("Arguardando comunicação com o servidor ...");
        //addMessageBlue("Teste com texto azul");
        //addMessageYellow("Teste com texto amarelo");
        socket.connect();
    }   
    
    public void circleClick(){
        
    }
    
    public void addMessageBlue(String msg){
        addMessage(msg, Paint.valueOf("#1e90ff"));
    }
    
    public void addMessageYellow(String msg){
       addMessage(msg, Paint.valueOf("#c3c310"));
    }
    
    public void addMessage(String msg, Paint value){
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Text dth = new Text(df.format(new Date()).toString() +" -- ");
        dth.setFont(Font.font("Helvetica", FontPosture.REGULAR, 16));
        
        Text text1 = new Text(msg+"\n");
        text1.setFill(value);
        text1.setFont(Font.font("Helvetica", FontPosture.REGULAR, 20));
        msgTextFlow.getChildren().addAll(dth, text1);
    }
    
    private void showDialog(String text){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Atenção"));
        content.setBody(new Text(text));
        
        JFXDialog dialog = new JFXDialog(dialogStackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton btn = new JFXButton("OK");
        //btn.setButtonType(com.jfoenix.controls.JFXButton.ButtonType.RAISED);
	//btn.setPrefHeight(32);
        
        btn.setOnAction((e)->{
            dialog.close();
        });
        content.setActions(btn);
        
        dialog.show();
    }

    private void addEventsToTheView() {
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
        
        jfxTf_message.setOnKeyPressed((e)->{
            if(e.getCode().equals(KeyCode.ENTER)){
                addMessageBlue(jfxTf_message.getText());
                jfxTf_message.setText("");
            }
        });
        
        dialogStackPane.setOnMouseClicked((e)->{
            showDialog("Arguardando comunicação com o servidor ...");
        });
        
        btnStart.setOnAction((e)->{
            
        });
        
        socket.setOnConnect(()->{
            dialogStackPane.setVisible(false);
            return 1;
        });
    }
}
