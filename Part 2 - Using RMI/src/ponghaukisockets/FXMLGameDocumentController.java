/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;

import ponghaukisockets.rmi.GameRemoteInterface;
import ponghaukisockets.rmi.PongHauKiREGISTRY;
import ponghaukisockets.model.PieceMap;
import ponghaukisockets.model.Piece;
import ponghaukisockets.model.Player;
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
import javafx.scene.input.KeyEvent;
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
    private Player player  = null; //"PLAYER_BLUE" or "PLAYER_YELLOW"
    private String hostServerName = "localhost";//DEfinido no modal ao iniciar o jogo
    
    private PongHauKiREGISTRY pongHauKiREGISTRY;
    private GameRemoteInterface gameControl;
    
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
        //Jogador
        player = new Player();
                
        //Peças do jogo
        createPieceMap();
        //Inicia PongHauKiREGISTRY
        createRegistries();
        
        //Eventos
        addEventsToTheView();
        startGame();
    }   
    
    /**
     * Cria instacia cliente local 
     * com referencias aos elementos da view
     */
    private void createRegistries() {
        pongHauKiREGISTRY = new PongHauKiREGISTRY();
        try {
            pongHauKiREGISTRY.createAndRegisterGameClient(player, pieceMap, msgTextFlow, labelGameStatus);
        } catch (Exception ex) {
            
        }
    }
    
    /**
     * Conecta com processo servidor
     * Pega referencia rmi
     */
    private void connect() {
        try {
            gameControl =  (GameRemoteInterface)Naming.lookup("//"+hostServerName+"/gameServerRef");
            gameControl.connect(player.getIdPlayer(), null);
            
            String title = player.getName().equals("PLAYER_BLUE") ? "Você é o AZUL" : "Você é o AMARELO";
            labelGameTitle.setText(title);
            labelGameTitle.setTextFill(player.getChatColor());
            //alertGameStatus("Esperando o outro jogador conectar...");
            
            
        } catch (Exception ex){
            
        }
    }
    
    /**
     * Inicia o jogo
     */
    public void startGame(){
        msgTextFlow.getChildren().setAll(new Text(""));
        //dialogStackPane.setVisible(false);
        dialogStackPane.setOnMouseClicked((e)->{
            showDialogHost();
        });
        showDialogHost();
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
    
    
    public void circleClick(){
        
    }
    
    /**
     * Modal para definicao de host 
     */
    private void showDialogHost(){
        JFXDialogLayout content = new JFXDialogLayout();
        JFXTextField hostServerField = new JFXTextField();
        try {
            hostServerField.setText(""+InetAddress.getLocalHost().getHostAddress());
            content.setHeading(new Text("Host do servidor de nomes(ipadress)"));
            content.setBody(hostServerField);
            
        } catch (UnknownHostException ex) {
            
        }
        dialog = new JFXDialog(dialogStackPane, content, JFXDialog.DialogTransition.CENTER);
        btnDialogOK = new JFXButton("OK");
        btnDialogOK.setOnAction((e)->{
            hostServerName = hostServerField.getText();
            
            //RMI
            connect();
            dialog.close();
            dialogStackPane.setVisible(false);
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
    
    private void addEventsToTheView() {
        //Events
        circuloAzulA.setOnMouseClicked((e)->{
            if(player.getName().equals("PLAYER_BLUE")){
                movePiece("BLUE_A");
            }else{
                
            }
        });
        circuloAzulB.setOnMouseClicked((e)->{
            if(player.getName().equals("PLAYER_BLUE")){
                movePiece("BLUE_B");
            }else{
                
            }
        });
        circuloAmareloA.setOnMouseClicked((e)->{
            if(player.getName().equals("PLAYER_YELLOW")){
                movePiece("YELLOW_A");
            }else{
                
            }
        });
        circuloAmareloB.setOnMouseClicked((e)->{
            if(player.getName().equals("PLAYER_YELLOW")){
                movePiece("YELLOW_B");
            }else{
                
            }
        });
        jfxTf_message.setOnKeyPressed((KeyEvent e)->{
            if(e.getCode().equals(KeyCode.ENTER)){
                String colorPlayer = "#1e90ff";
                try {
                    gameControl.writeChatMessage(player.getIdPlayer(), jfxTf_message.getText(), player.getChatColor().toString());
                    
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
    }

    private void createPieceMap() {
        pieceMap = new PieceMap();
        pieceMap.setPieceblueA(new Piece(circuloAzulA, 1));
        pieceMap.setPieceblueB(new Piece(circuloAzulB, 2));
        pieceMap.setPieceYellowA(new Piece(circuloAmareloA, 3));
        pieceMap.setPieceYellowB(new Piece(circuloAmareloB, 4));
        
    }

    private void movePiece(String movement) {
        try {
            String idPlayer = player.getIdPlayer();
            String idLast = gameControl.getIdPlayerFromLastMove();
            
            if(idLast == null){//Primeira jogada é do blue
                if(player.getName().equals("PLAYER_BLUE")){
                    gameControl.movePieceControl(player.getIdPlayer(), movement);
                }
            }else if(idPlayer.equals(idLast) == false){
                gameControl.movePieceControl(player.getIdPlayer(), movement);
            }
        } catch (RemoteException ex) {
            
        }
    }
}
