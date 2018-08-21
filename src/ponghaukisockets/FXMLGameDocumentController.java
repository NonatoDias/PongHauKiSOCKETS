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
import java.net.Socket;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
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
import javafx.util.Duration;

/**
 *
 * @author Nonato Dias
 */
public class FXMLGameDocumentController implements Initializable {
    
    private SocketServer server;
    private SocketClient client;
    
    private PieceMap pieceMap;
    private int PLAYER_BLUE = 1;
    private int PLAYER_YELLOW = 2;
    
    int portClient = 8000;
    int portServer = 8080;
    
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
        //Peças do jogo
        pieceMap = new PieceMap();
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
        jfxTf_message.setOnKeyPressed((e)->{
            if(e.getCode().equals(KeyCode.ENTER)){
                String colorPlayer = "#1e90ff";
                addMessageToTheServer(jfxTf_message.getText()+"&amp;"+colorPlayer+"\n");
                jfxTf_message.setText("");
            }
        });
        
        dialogStackPane.setOnMouseClicked((e)->{
            showDialog();
        });
        
        btnStart.setOnAction((e)->{
            
        });
        
        //run
        showDialog();
        //dialogStackPane.setVisible(false);
        //clientSocket.connect();
        addMessageBlue("TEste");
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
        /*DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Text dth = new Text(df.format(new Date()).toString() +" -- ");
        dth.setFont(Font.font("Helvetica", FontPosture.REGULAR, 16));*/
        
        Text text = new Text(msg+"\n");
        text.setFill(value);
        text.setFont(Font.font("Helvetica", FontPosture.REGULAR, 20));
        
        String message = msg+"&amp;"+value;
        msgTextFlow.getChildren().addAll(text);
    }
    
    private void writeMessages(String data) {
        if(data.length()>0){
            msgTextFlow.getChildren().setAll(new Text(""));
            String[] messagesArray = data.split("\n");
            for (int i = 0; i < messagesArray.length; i++) {
                String [] aux = messagesArray[i].split("&amp;");
                
                Text text = new Text(aux[0]+"\n");
                text.setFill(Paint.valueOf(aux[1]));
                text.setFont(Font.font("Helvetica", FontPosture.REGULAR, 20));
                msgTextFlow.getChildren().addAll(text);
            }
        }
    }
    
    private void showDialog(){
        JFXDialogLayout content = new JFXDialogLayout();
        JFXTextField portServerField = new JFXTextField("8080");
        //JFXTextField portField = new JFXTextField("Porta do processo servidor");
        content.setHeading(new Text("Porta do socket cliente"));
        content.setBody(portServerField);
        
        JFXDialog dialog = new JFXDialog(dialogStackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton btn = new JFXButton("OK");
        //btn.setButtonType(com.jfoenix.controls.JFXButton.ButtonType.RAISED);
	//btn.setPrefHeight(32);
        
        btn.setOnAction((e)->{
            int x = Integer.parseInt(portServerField.getText());
            if(x > 0){
                portServer = x;
                dialogStackPane.setVisible(false);
            }
            initClient();
            dialog.close();
        });
        content.setActions(btn);
        
        dialog.show();
    }
    
    private void addMessageToTheServer(String message) {
        //Chamada Assicrona
        SocketClientService service = new SocketClientService();
        service.setSocket(client);
        service.setAction("addmessage");
        service.setData(message);
        service.setOnSucceeded((e)->{
            String resp = e.getSource().getValue().toString();
            String code = ProtocolCONFIG.getCodeFromResponse(resp);
            if(code.equals(ProtocolCONFIG.RESULT_OK)){
                String data = ProtocolCONFIG.getDataFromResponse(resp);
                System.out.println("Resposta "+data);
            }       
        });
        service.start();
    }
/*
    private void getMessagesAndAddToTheView() {
        GameService service = new GameService();
        service.setClientSocket(clientSocket);
        service.setAction("getallmessages");
        service.setData("");
        service.setOnSucceeded((e)->{
            String resp = e.getSource().getValue().toString();
            String code = ProtocolCONFIG.getCodeFromResponse(resp);
            if(code.equals(ProtocolCONFIG.RESULT_OK)){
                String data = ProtocolCONFIG.getDataFromResponse(resp);
                writeMessages(data);
            }
        });
        service.start();
    }*/
    
    public void initThreadServer() throws IOException{
        //Cria o socket e inicializa a thread    
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
    
    public void initClient(){
        client = new SocketClient(portClient);
        try {
            client.bindAndConnect();
            initThreadServer();

        } catch (IOException ex) {
            
        } 
        
        
        /*Task task = new Task<Void>() {
            @Override public Void call() throws IOException {
                client.bindAndConnect();
                initThreadServer();
                
                while(true){
                    String msg = client.receiveMessage();
                    String msgResp = ProtocolCONFIG.prepareResponse(ProtocolCONFIG.RESULT_OK, msg+"CLIENTE JOGO RECEBI A MENSAGEM");
                    client.sendMessage(msgResp);
                }
            }
        };
        Thread threadSocket = new Thread(task);
        threadSocket.setDaemon(true);//Mata a thread qdo fecha a janela
        threadSocket.start();*/
    }
    
    public void resolveMessages(String msg){
        String dataFrom = ProtocolCONFIG.getDataFromMessage(msg);
        String dataTo = "";
        
        switch(ProtocolCONFIG.getActionFromMessage(msg)){
            case "returnmessagetochat": 
                Platform.runLater(() -> {
                    addMessageBlue(dataFrom);
                });
                break;
            default: 
                break;
        }
        
        String msgResp = ProtocolCONFIG.prepareResponse(ProtocolCONFIG.RESULT_OK, "ok");
        server.sendMessage(0, msgResp);
    }
}
