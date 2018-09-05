/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javafx.application.Platform;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author Nonato Dias
 */
public class GameClient extends UnicastRemoteObject implements GameRemoteInterface{
    private Player player = null;    
    private PieceMap pieceMap;
    private TextFlow textflow = null;
    
    
    public GameClient(TextFlow textflow) throws RemoteException {
        super();
        this.textflow = textflow;
        log("Servidor chat criado!");
    }
    
    public void setPlayer(Player whoDidIt) throws RemoteException {
        this.player = whoDidIt;
    }
    
    public void setPieceMap(PieceMap pieceMap) throws RemoteException {
        this.pieceMap = pieceMap;
    }
    
    @Override
    public void connect(String idPlayer) throws RemoteException {
        log("CLIENT "+idPlayer+" connectado SERVIDOR ");
    }

    @Override
    public void writeChatMessage(String idPlayer, String msg) throws RemoteException {
        Platform.runLater(()->{
            addMessageToTheView( msg, player.getChatColor());
        });
    }
    
    @Override
    public void movePieceControl(String idPlayer, String pieceName) throws RemoteException {
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
        });
    }
    
    public void addMessageToTheView(String msg, Paint value){
        Text text = new Text(msg+"\n");
        text.setFill(value);
        text.setFont(Font.font("Helvetica", FontPosture.REGULAR, 20));    
        String message = msg+"&amp;"+value;
        this.textflow.getChildren().addAll(text);
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
