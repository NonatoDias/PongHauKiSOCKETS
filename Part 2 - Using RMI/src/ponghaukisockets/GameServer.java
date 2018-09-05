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
public class GameServer extends UnicastRemoteObject implements GameRemoteInterface{
    private PlayerRefecences players;

    public GameServer() throws RemoteException {
        super();
        players =  new PlayerRefecences();
        System.out.println("GameServer criado!");
    }
   
    @Override
    public void connect(String idPlayer) throws RemoteException {
        try {
            if(players.size() >= 0 && players.size() <= 2){
                addPlayer(idPlayer);
                log("SERVIDOR connectado com "+idPlayer);
            }
            
        }catch (Exception ex) {
            log("Error writeChatMessage: "+ex.toString());
        } 
    }

    @Override
    public void writeChatMessage(String idPlayer, String msg) throws RemoteException {
        try {
            if(players.size() >= 0 && players.size() <= 2){
                for(GameRemoteInterface gameInterface: players.getAllGameInterface()){
                    gameInterface.writeChatMessage(idPlayer, msg);
                }
            }
            
        }catch (Exception ex) {
            log("Error writeChatMessage: "+ex.toString());
        } 
    }

    @Override
    public void movePieceControl(String idPlayer, String pieceName) throws RemoteException {
        
    }
    
    /**
     * Loga na view 
     * @param text 
     */
    private void log(String text){
        String msg = "*** GameServer *** "+text;
        System.out.println(msg);
    }
    
    private void addPlayer(String idPlayer) throws NotBoundException, MalformedURLException, RemoteException{
        String baseUrl = "//localhost/gameClientRef";
        GameRemoteInterface intrf = (GameRemoteInterface)Naming.lookup(baseUrl+idPlayer);
        players.add(idPlayer, intrf);
        intrf.connect(idPlayer);
    }
}
