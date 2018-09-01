/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


/**
 *
 * @author Nonato Dias
 */
public class ServerChat extends UnicastRemoteObject implements ChatRemoteInterface{

    public ServerChat() throws RemoteException {
        super();
        System.out.println("Servidor chat criado!");
    }

    @Override
    public void writeMessage(String msg) throws RemoteException {
         System.out.println("====================== Recebido: "+msg);
        
    }
    
}
