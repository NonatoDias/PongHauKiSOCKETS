/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets.rmi;

import java.rmi.Remote; 
import java.rmi.RemoteException;

/**
 *
 * @author Nonato Dias
 */
public interface GameRemoteInterface extends Remote{
    void connect(String idPlayer) throws  RemoteException;
    void writeChatMessage(String idPlayer, String msg) throws  RemoteException;
    void movePieceControl(String idPlayer, String pieceName)  throws  RemoteException;
}
