/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets.rmi;


import ponghaukisockets.model.PlayerRefecences;
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
public class GameServer extends UnicastRemoteObject implements GameRemoteInterface{
    private PlayerRefecences players;
    private String IdPlayerFromLastMove;
    private String serverName;

    public GameServer() throws RemoteException {
        super();
        players =  new PlayerRefecences();
        System.out.println("GameServer criado!");
    }
   
    @Override
    public void connect(String serverName, String idPlayer, String data) throws RemoteException {
        try {
            this.serverName = serverName;
            if(isAllPlayerReady()){
                addPlayer(idPlayer);
                log("SERVIDOR connectado com "+idPlayer);
            }
            
        }catch (Exception ex) {
            log("Error writeChatMessage: "+ex.toString());
        } 
    }

    @Override
    public void writeChatMessage(String idPlayer, String msg, String color) throws RemoteException {
        try {
            if(isAllPlayerReady()){
                for(GameRemoteInterface gameInterface: players.getAllGameInterface()){
                    gameInterface.writeChatMessage(idPlayer, msg, color);
                }
            }
            
        }catch (Exception ex) {
            log("Error writeChatMessage: "+ex.toString());
        } 
    }

    @Override
    public void movePieceControl(String idPlayer, String pieceName) throws RemoteException {
        this.IdPlayerFromLastMove = idPlayer;
        try {
            if(isAllPlayerReady()){
                for(GameRemoteInterface gameInterface: players.getAllGameInterface()){
                    gameInterface.movePieceControl(idPlayer, pieceName);
                }
            }
            
        }catch (Exception ex) {
            log("Error movePieceControl: "+ex.toString());
        } 
    }
    
    @Override
    public void quitGame(String idPlayer) throws RemoteException {
        try {
            if(isAllPlayerReady()){
                for(GameRemoteInterface gameInterface: players.getAllGameInterface()){
                   gameInterface.quitGame(idPlayer);
                }
            }
            
        }catch (Exception ex) {
            log("Error quitGame: "+ex.toString());
        } 
    }
    
    @Override
    public String getIdPlayerFromLastMove() throws RemoteException {
        return this.IdPlayerFromLastMove;
    }
    
    boolean isAllPlayerReady(){
        return players.size() >= 0 && players.size() <= 2;
    }
    
    /**
     * Loga na view 
     * @param text 
     */
    private void log(String text){
        String msg = "*** GameServer *** "+text;
        System.out.println(msg);
    }
    
    /**
     * Adiciona a referencia do jogador
     * @param idPlayer
     * @throws NotBoundException
     * @throws MalformedURLException
     * @throws RemoteException 
     */
    private void addPlayer(String idPlayer) throws NotBoundException, MalformedURLException, RemoteException{
        String baseUrl = "//"+serverName+"/gameClientRef";
        GameRemoteInterface intrf = (GameRemoteInterface)Naming.lookup(baseUrl+idPlayer);
        players.add(idPlayer, intrf);
        intrf.connect(serverName, idPlayer, creatPlayName());
    }
    
    private String creatPlayName(){
        if(players.size()%2 > 0){
            return "PLAYER_BLUE";
        }
        return "PLAYER_YELLOW";
    }
}
