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
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
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
import javafx.scene.Node;
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
    
    private SocketServer server;
    private SocketClient client;
    
    private PieceMap pieceMap;
    private String player = ""; //"PLAYER_BLUE" or "PLAYER_YELLOW"
    
    int portClient = 8000;
    int portServer = 8080;
    private String host;
    
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
            //pieceMap.moveBlueA();
            sendMovimentToServer("BLUE_A");
        });
        circuloAzulB.setOnMouseClicked((e)->{
            //pieceMap.moveBlueB();
            sendMovimentToServer("BLUE_B");
        });
        circuloAmareloA.setOnMouseClicked((e)->{
            //pieceMap.moveYellowA();
            sendMovimentToServer("YELLOW_A");
        });
        circuloAmareloB.setOnMouseClicked((e)->{
            //pieceMap.moveYellowB();
            sendMovimentToServer("YELLOW_B");
        });
        jfxTf_message.setOnKeyPressed((e)->{
            if(e.getCode().equals(KeyCode.ENTER)){
                String colorPlayer = "#1e90ff";
                addMessageToTheServer(jfxTf_message.getText()+"&amp;"+player);
                jfxTf_message.setText("");
            }
        });
        
        dialogStackPane.setOnMouseClicked((e)->{
            showDialogHost();
        });
        
        btnQuit.setOnAction((e)->{
            quitGame();
        });
        
        //run
        showDialogHost();
        //dialogStackPane.setVisible(false);
        //clientSocket.connect();
    }   
    
    public void initThreadServer() throws IOException{
        server = new SocketServer();
        server.init(portServer);
        Task task = new Task<Void>() {
            @Override public Void call() throws IOException {
                server.acceptAndConnect();
                
                while(true){
                    String msg = server.receiveMessage(0);
                    resolveMessages(msg);
                }
            }
        };
        Thread threadSocket = new Thread(task);
        threadSocket.setDaemon(true);//Mata a thread qdo fecha a janela
        threadSocket.start();
    }
    
    boolean isClientConected = false;
    public void initClientAndCloseDialog(){
        client = new SocketClient(host, portClient);
        
        Task task = new Task<Void>() {
            @Override public Void call() throws IOException {
                client.bindAndConnect();
                if(isClientConected){
                    return null;
                }
                isClientConected = true;
                Platform.runLater(() -> {
                    try {
                        initThreadServer();
                        getGameConfigs();
                        dialog.close();
                        dialogStackPane.setVisible(false);
                    } catch (Exception ex) {
                        System.out.println("ERROR "+ex.toString());
                    } 
                }); 
                return null;
            }
        };
        Thread threadSocket = new Thread(task);
        threadSocket.setDaemon(true);//Mata a thread qdo fecha a janela
        threadSocket.start();
    }
    
    public void resolveMessages(String msg){
        String dataFrom = ProtocolCONFIG.getDataFromMessage(msg);
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
                Platform.runLater(() -> { 
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
        server.sendMessage(0, msgResp);
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
    
    private void showDialogPort(){
        JFXDialogLayout content = new JFXDialogLayout();
        JFXTextField portServerField = new JFXTextField("8080");
        //JFXTextField portField = new JFXTextField("Porta do processo servidor");
        content.setHeading(new Text("Porta do GAME"));
        content.setBody(portServerField);
        
        dialog = new JFXDialog(dialogStackPane, content, JFXDialog.DialogTransition.CENTER);
        btnDialogOK = new JFXButton("OK");
        //btn.setButtonType(com.jfoenix.controls.JFXButton.ButtonType.RAISED);
	//btn.setPrefHeight(32);
        
        btnDialogOK.setOnAction((e)->{
            int x = Integer.parseInt(portServerField.getText());
            if(x > 0){
                portServer = x;
                initClientAndCloseDialog();
            }
        });
        content.setActions(btnDialogOK);
        dialog.show();
    }
    
    private void showDialogPortClient(){
        JFXDialogLayout content = new JFXDialogLayout();
        JFXTextField portClientField = new JFXTextField("8000");
        content.setHeading(new Text("Porta do SERVIDOR"));
        content.setBody(portClientField);
        
        dialog = new JFXDialog(dialogStackPane, content, JFXDialog.DialogTransition.CENTER);
        btnDialogOK = new JFXButton("OK");
        btnDialogOK.setOnAction((e)->{
            int x = Integer.parseInt(portClientField.getText());
            if(x > 0){
                portClient = x;
                dialog.close();
                showDialogPort();
                //initClientAndCloseDialog();
            }
        });
        content.setActions(btnDialogOK);
        dialog.show();
    }
    
    private void showDialogHost(){
        JFXDialogLayout content = new JFXDialogLayout();
        JFXTextField hostServerField = new JFXTextField();
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            hostServerField.setText(""+inetAddress.getHostAddress());
            content.setHeading(new Text("Host do servidor(ipadress)"));
            content.setBody(hostServerField);
            
        } catch (UnknownHostException ex) {
            
        }
        dialog = new JFXDialog(dialogStackPane, content, JFXDialog.DialogTransition.CENTER);
        btnDialogOK = new JFXButton("OK");
        btnDialogOK.setOnAction((e)->{
            host = hostServerField.getText();
            dialog.close();
            showDialogPortClient();
            //showDialogPort();
        });
        content.setActions(btnDialogOK);
        dialog.show();
    }
    
    private void alertWinner(String msg){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("FIM DE JOGO"));
        content.setBody(new Text(msg));
        
        dialogAlert = new JFXDialog(dialogStackPane, content, JFXDialog.DialogTransition.CENTER);
        btnDialogOK = new JFXButton("Reiniciar partida");
        
        btnDialogOK.setOnAction((e)->{
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
    
    private void addMessageToTheServer(String message) {
        //Chamada Assicrona
        SocketClientService service = new SocketClientService();
        service.setSocket(client);
        service.setAction("addmessage");
        service.setData(message);
        service.setOnSucceeded((e)->{
            String resp = e.getSource().getValue().toString();
            String code = ProtocolCONFIG.getActionFromMessage(resp);
            if(code.equals(ProtocolCONFIG.RESULT_OK)){
                String data = ProtocolCONFIG.getDataFromMessage(resp);
                System.out.println("Resposta "+data);
            }       
        });
        service.start();
    }
    
    private void getGameConfigs() {
        //Chamada Assicrona
        SocketClientService service = new SocketClientService();
        service.setSocket(client);
        service.setAction("getgameconfigs");
        service.setData("");
        service.setOnSucceeded((e)->{
            String resp = e.getSource().getValue().toString();
            String code = ProtocolCONFIG.getActionFromMessage(resp);
            if(code.equals(ProtocolCONFIG.RESULT_OK)){
                String data = ProtocolCONFIG.getDataFromMessage(resp);
                player = data;
                String title = data.equals("PLAYER_BLUE") ? "Você é o AZUL" : "Você é o AMARELO";
                String color = data.equals("PLAYER_BLUE") ? "#1e90ff" : "#c3c310";
                Platform.runLater(() -> {
                    labelGameTitle.setText(title);
                    labelGameTitle.setTextFill(Paint.valueOf(color));
                    alertGameStatus("Esperando o outro jogador conectar...");
                });
               
            }       
        });
        service.start();
    }
    
    /*
    private void canGameStart() {
        //Chamada Assicrona
        SocketClientService service = new SocketClientService();
        service.setSocket(client);
        service.setAction("cangamestart");
        service.setData("");
        service.setOnSucceeded((e)->{
            String resp = e.getSource().getValue().toString();
            String code = ProtocolCONFIG.getActionFromMessage(resp);
            if(code.equals(ProtocolCONFIG.RESULT_OK)){
                String data = ProtocolCONFIG.getDataFromMessage(resp);
                if(data.equals("TRUE")){
                    Platform.runLater(() -> {
                        dialog.close();
                        dialogStackPane.setVisible(false);
                    });
                }else{
                    alertGameStatus("Esperando o outro jogador conectar...");
                }
            }       
        });
        service.start();
    }*/
    
    private void quitGame() {
        //Chamada Assicrona
        SocketClientService service = new SocketClientService();
        service.setSocket(client);
        service.setAction("quitgame");
        service.setData("");
        service.setOnSucceeded((e)->{
            String resp = e.getSource().getValue().toString();
            String code = ProtocolCONFIG.getActionFromMessage(resp);
            if(code.equals(ProtocolCONFIG.RESULT_OK)){
                String data = ProtocolCONFIG.getDataFromMessage(resp);
                /*Stage stage = (Stage) btnQuit.getScene().getWindow();
                stage.close();*/
            }       
        });
        service.start();
    }
    
    public void sendMovimentToServer(String pieceName){
        SocketClientService service = new SocketClientService();
        service.setSocket(client);
        service.setAction("movepiece");
        service.setData(pieceName);
        service.setOnSucceeded((e)->{
            String resp = e.getSource().getValue().toString();
            String code = ProtocolCONFIG.getActionFromMessage(resp);
            if(code.equals(ProtocolCONFIG.RESULT_OK)){
                String data = ProtocolCONFIG.getDataFromMessage(resp);
                /*Stage stage = (Stage) btnQuit.getScene().getWindow();
                stage.close();*/
            }       
        });
        service.start();
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
}
