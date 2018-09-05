/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Nonato Dias
 */
public class ServerChat extends UnicastRemoteObject implements ChatRemoteInterface{
    private ChatRemoteInterface chatInterface = null;

    
    public ServerChat() throws RemoteException {
        super();
        System.out.println("Servidor chat criado!");
    }

    @Override
    public void writeMessage(String msg) throws RemoteException {
        if(chatInterface == null){
            try {
                chatInterface =  (ChatRemoteInterface)Naming.lookup("//localhost/clientChatRef");
            } catch (Exception ex) {
                
            } 
        }
        chatInterface.writeMessage(msg);
    }
}
