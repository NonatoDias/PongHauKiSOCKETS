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
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Nonato Dias
 */
public class FXMLGameDocumentController implements Initializable {
    private PieceMap pieceMap;
    private String player = ""; //"PLAYER_BLUE" or "PLAYER_YELLOW"
    private String whoDidLastMove = "PLAYER_YELLOW";//Começa com azul
    
    @FXML
    private JFXDialog dialog;
    
    @FXML
    private JFXDialog dialogAlert;
    
    @FXML
    private JFXButton btnDialogOK;
    
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
    private JFXButton btnQuit;
    
    @FXML
    private Label labelGameTitle;
    
    @FXML
    private Label labelGameStatus;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Peças do jogo
        pieceMap = new PieceMap();
        pieceMap.setPieceblueA(new Piece(circuloAzulA, 1));
        pieceMap.setPieceblueB(new Piece(circuloAzulB, 2));
        pieceMap.setPieceYellowA(new Piece(circuloAmareloA, 3));
        pieceMap.setPieceYellowB(new Piece(circuloAmareloB, 4));
  
        //Events
        circuloAzulA.setOnMouseClicked((e)->{
            if(player.equals("PLAYER_BLUE") && !player.equals(whoDidLastMove)){
                //sendMovimentToServer("BLUE_A");
            }else{
                
            }
        });
        circuloAzulB.setOnMouseClicked((e)->{
            if(player.equals("PLAYER_BLUE") && !player.equals(whoDidLastMove)){
                //sendMovimentToServer("BLUE_B");
            }else{
                
            }
        });
        circuloAmareloA.setOnMouseClicked((e)->{
            if(player.equals("PLAYER_YELLOW") && !player.equals(whoDidLastMove)){
                //sendMovimentToServer("YELLOW_A");
            }else{
                
            }
        });
        circuloAmareloB.setOnMouseClicked((e)->{
            if(player.equals("PLAYER_YELLOW") && !player.equals(whoDidLastMove)){
                //sendMovimentToServer("YELLOW_B");
            }else{
                
            }
        });
        jfxTf_message.setOnKeyPressed((e)->{
            if(e.getCode().equals(KeyCode.ENTER)){
                String colorPlayer = "#1e90ff";
                try {
                    //addMessageToTheServer(jfxTf_message.getText()+"&amp;"+player);
                    ChatRemoteInterface Inv =  (ChatRemoteInterface)Naming.lookup("//localhost/chatMethodsRef");
                    Inv.writeMessage(jfxTf_message.getText());
                } catch (Exception ex) {
                    
                } 
                jfxTf_message.setText("");
            }
        });
        
        dialogStackPane.setOnMouseClicked((e)->{
            //showDialogHost();
        });
        
        btnQuit.setOnAction((e)->{
            quitGame();
        });
        
        //run
        startGame();
        //showDialogHost();
    }   
    
    
    public void startGame(){
        msgTextFlow.getChildren().setAll(new Text(""));
    }
    
    public void restartGame(){        
        Parent game = null;
        try {
            game = FXMLLoader.load(getClass().getResource("FXMLGameDocument.fxml"));
        } catch (IOException ex) {
            //log("ERROR: "+ex.toString());
        }
        Stage stage2 = new Stage();
        Scene scene2 = new Scene(game);
        stage2.setScene(scene2);
        stage2.setResizable(false);
        stage2.show();
        
        Stage stage = (Stage) btnQuit.getScene().getWindow();
        stage.close();
    }
    
    public void initThreadServer() throws IOException{
        
    }
    
    public void initClientAndCloseDialog(){
        
    }
    
    /**
     * Trata as mensagens
     * @param msg 
     */
    public void resolveMessages(String msg){
        /*String dataFrom = ProtocolCONFIG.getDataFromMessage(msg);
        String [] params = ProtocolCONFIG.getParamsFromData(dataFrom);
        String dataTo = "";
        
        switch(ProtocolCONFIG.getActionFromMessage(msg)){
            case "returnmessagetochat": 
                Platform.runLater(() -> {
                    switch(params[1]){
                        case "PLAYER_BLUE":
                            addMessageBlue(params[0]);
                            break;
                        case "PLAYER_YELLOW":
                            addMessageYellow(params[0]);
                            break;
                    }
                });
                break;
            case "returnwinningplayer":
                Platform.runLater(() -> { 
                    switch(params[0]){
                        case "PLAYER_BLUE":
                            alertWinner("O jogador AZUL ganhou a partida. "+params[1]);
                            break;
                        case "PLAYER_YELLOW":
                            alertWinner("O jogador AMARELO ganhou a partida. "+params[1]);
                            break;
                    }
                });
                break;
            case "returnmovimentcontrol":
                whoDidLastMove = params[1].equals("0") ? "PLAYER_BLUE" : "PLAYER_YELLOW";
                System.out.println("\nFez o ultimo movimento: "+whoDidLastMove);
                
                Platform.runLater(() -> { 
                    renderStatus();
                    movePieceControl(params[0]);
                });
                break;
                
            case "returncangamestart":
                if(params[0].equals("TRUE")){
                    Platform.runLater(() -> { 
                        dialogAlert.close();
                        dialogStackPane.setVisible(false);
                    });
                }
                break;
                
            default: 
                break;
        }
        String msgResp = ProtocolCONFIG.prepareResponse(ProtocolCONFIG.RESULT_OK, "ok");
        //server.sendMessage(0, msgResp);*/
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
        Text text = new Text(msg+"\n");
        text.setFill(value);
        text.setFont(Font.font("Helvetica", FontPosture.REGULAR, 20));    
        String message = msg+"&amp;"+value;
        msgTextFlow.getChildren().addAll(text);
    }
    
    
    private void alertWinner(String msg){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("FIM DE JOGO"));
        content.setBody(new Text(msg));
        
        dialogAlert = new JFXDialog(dialogStackPane, content, JFXDialog.DialogTransition.CENTER);
        btnDialogOK = new JFXButton("Reiniciar partida");
        
        btnDialogOK.setOnAction((e)->{
            restartGame();
            dialogAlert.close();
            dialogStackPane.setVisible(false);
        });
        
        dialogStackPane.setOnMouseClicked((e)->{
            alertWinner(msg);
        });
        
        content.setActions(btnDialogOK);
        dialogStackPane.setVisible(true);
        dialogAlert.show();
    }
    
    
    private void alertGameStatus(String msg){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Por favor aguarde!"));
        content.setBody(new Text(msg));
        
        dialogAlert = new JFXDialog(dialogStackPane, content, JFXDialog.DialogTransition.CENTER);
        btnDialogOK = new JFXButton("Atualizar");
        
        btnDialogOK.setOnAction((e)->{
            dialogAlert.close();
            alertGameStatus(msg);
        });
        
        dialogStackPane.setOnMouseClicked((e)->{
            dialogAlert.close();
            alertGameStatus(msg);
        });
        
        content.setActions(btnDialogOK);
        dialogStackPane.setVisible(true);
        dialogAlert.show();
    }
    
    
    private void getGameConfigs() {
        
    }
    
    private void quitGame() {
        
    }
    
    public void sendMovimentToServer(String pieceName){
        
    }
    
    public void movePieceControl(String pieceName){
        switch(pieceName){
            case "BLUE_A":
                pieceMap.moveBlueA();
                break;
            case "BLUE_B":
                pieceMap.moveBlueB();
                break;
            case "YELLOW_A":
                pieceMap.moveYellowA();
                break;
            case "YELLOW_B":
                pieceMap.moveYellowB();
                break;
        }
    }
    
    public void renderStatus(){
        switch(whoDidLastMove){
            case "PLAYER_BLUE":
                labelGameStatus.setText("Amarelo joga agora");
                break;
            case "PLAYER_YELLOW":
                labelGameStatus.setText("Azul joga agora");
                break;
        }
    }
}
