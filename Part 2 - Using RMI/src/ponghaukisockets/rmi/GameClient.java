/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets.rmi;

import com.jfoenix.controls.JFXDialog;
import ponghaukisockets.model.PieceMap;
import ponghaukisockets.model.Player;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import ponghaukisockets.model.ModalAlert;

/**
 *
 * @author Nonato Dias
 */
public class GameClient extends UnicastRemoteObject implements GameRemoteInterface{
    private Player player = null;   
    private String IdPlayerFromLastMove;
    private PieceMap pieceMap;
    private TextFlow textflow = null;
    private Label labelGameStatus;
    private ModalAlert modal;
    
    
    public GameClient(TextFlow textflow) throws RemoteException {
        super();
        this.textflow = textflow;
        log("Servidor chat criado!");
    }
    
    public void setPlayer(Player whoDidIt){
        this.player = whoDidIt;
    }
    
    public void setlabelGameStatus(Label labelGameStatus){
        this.labelGameStatus = labelGameStatus;
    }
    
    public void setPieceMap(PieceMap pieceMap){
        this.pieceMap = pieceMap;
    }
    
    
    void setModalAlert(ModalAlert modal) {
        this.modal = modal;
    }
    
    @Override
    public void connect(String idPlayer, String data) throws RemoteException {
        switch(data){
            case "PLAYER_BLUE":
                player.setChatColor(Paint.valueOf("#1e90ff"));
                break;
            case "PLAYER_YELLOW":
                player.setChatColor(Paint.valueOf("#c3c310"));
                break;
        }
        player.setName(data);
        log("CLIENT "+idPlayer+" connectado SERVIDOR ");
        Platform.runLater(()->{
            renderStatus();
        });
        
    }

    @Override
    public void writeChatMessage(String idPlayerSender, String msg, String color) throws RemoteException {
        Platform.runLater(()->{
            addMessageToTheView( msg, Paint.valueOf(color));
        });
    }
    
    @Override
    public String getIdPlayerFromLastMove() throws RemoteException {
        return this.IdPlayerFromLastMove;
    }
    
    @Override
    public void quitGame(String idPlayer) throws RemoteException {
        Platform.runLater(()->{ 
            String msg = "";
            if(idPlayer.equals(player.getIdPlayer())){
                msg = "Você desistiu e perdeu!";
                
            }else{
                msg += player.getName().equals("PLAYER_YELLOW") ? "Azul desistiu. " : "Amarelo desistiu. ";
                msg += "Parabéns você ganhou!";
            }
            modal.show(msg);
        });
    }
    
    @Override
    public void movePieceControl(String idPlayer, String pieceName) throws RemoteException {
        this.IdPlayerFromLastMove = idPlayer;
        
        Platform.runLater(()->{
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
            renderStatus();
        });
    }
    
    public void addMessageToTheView(String msg, Paint value){
        Text text = new Text(msg+"\n");
        text.setFill(value);
        text.setFont(Font.font("Helvetica", FontPosture.REGULAR, 20));    
        String message = msg+"&amp;"+value;
        this.textflow.getChildren().addAll(text);
    }
    
    public void renderStatus(){
        try {
            String idPlayer = player.getIdPlayer();
            String idLast = getIdPlayerFromLastMove();
            if(idLast == null){//Primeira jogada é do blue
                labelGameStatus.setText("Azul joga agora");
                
            }else if(idPlayer.equals(idLast)){
                String title = player.getName().equals("PLAYER_YELLOW") ? "Azul joga agora" : "Amarelo joga agora";
                labelGameStatus.setText(title);
            }else{
                String title = player.getName().equals("PLAYER_BLUE") ? "Azul joga agora" : "Amarelo joga agora";
                labelGameStatus.setText(title);
            }
        } catch (RemoteException ex) {
            
        }
    } 
    
    /**
     * Loga na view 
     * @param text 
     */
    private void log(String text){
        String msg = "*** GameClient *** "+text;
        System.out.println(msg);
    } 
}
