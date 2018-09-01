/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;

import java.rmi.Remote; 
import java.rmi.RemoteException;

/**
 *
 * @author Nonato Dias
 */
public interface ChatRemoteInterface extends Remote{
    void writeMessage(String msg) throws  RemoteException;;
}
