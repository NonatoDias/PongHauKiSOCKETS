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
public class ClientChat extends UnicastRemoteObject implements ChatRemoteInterface{
    private String lastMessage = "";
    private String player = "PLAYER_BLUE";
    private TextFlow textflow = null;
    //private Runnable callbackMessage;
    
    public ClientChat(TextFlow textflow) throws RemoteException {
        super();
        this.textflow = textflow;
        System.out.println("Servidor chat criado!");
    }

    @Override
    public void writeMessage(String msg) throws RemoteException {
        this.lastMessage = msg;
        Platform.runLater(()->{
            switch(player){
                case "PLAYER_BLUE":
                    addMessageBlue(msg);
                    break;
                case "PLAYER_YELLOW":
                    addMessageYellow(msg);
                    break;
            }
        });
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
        this.textflow.getChildren().addAll(text);
    }
    
    
    /*public void onWriteMessage(Runnable runnable){
        callbackMessage = runnable;
    }*/
    
    
}
