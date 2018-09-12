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
    /**
     * Estabelece a conexão entre o cliente o servidor
     * @param serverName
     * @param idPlayer
     * @param data
     * @throws RemoteException 
     */
    void connect(String serverName, String idPlayer, String data) throws  RemoteException;
    
    /**
     * idPlayer do jogador que efetuou o ultimo movimento
     * @return idPlayer
     * @throws RemoteException 
     */
    String getIdPlayerFromLastMove() throws  RemoteException;
    
    /**
     * Escreve na tela o texto com uma certa cor
     * @param idPlayerSender
     * @param msg
     * @param color
     * @throws RemoteException 
     */
    void writeChatMessage(String idPlayerSender, String msg, String color) throws  RemoteException;
    
    /**
     * Controlador de movimentos
     * @param idPlayer
     * @param pieceName
     * @throws RemoteException 
     */
    void movePieceControl(String idPlayer, String pieceName)  throws  RemoteException;
    
    /**
     * Informa o jogador que desistiu e o jogador que ganhou
     * @param idPlayer
     * @throws RemoteException 
     */
    void quitGame(String idPlayer)  throws  RemoteException;
}
