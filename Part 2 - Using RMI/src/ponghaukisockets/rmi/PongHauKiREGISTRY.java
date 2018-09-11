package ponghaukisockets.rmi;


import ponghaukisockets.model.PieceMap;
import ponghaukisockets.model.Player;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javafx.concurrent.Task;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;

/**
 *
 * @author Nonato Dias
 */
public class PongHauKiREGISTRY {
    private int port;
    private Registry registry;
    
    public PongHauKiREGISTRY(){
        this.port = 1099;
    }
    
    
    /**
     * Método principal para inciar 
     * @throws IOException 
     */
    public void initRMIRegistry(int port) throws RemoteException, MalformedURLException{
        /** @TODO destruir thread ao fechar programa */
        this.port = port;
        this.registry = LocateRegistry.createRegistry(port);
        log("RMIRegistry iniciado");
    }
    
    public void createAndRegisterGameServer() throws RemoteException, MalformedURLException{
        GameServer server = new GameServer();
        Naming.rebind("//localhost/gameServerRef",server);
        log("gameServerRef bind");
    }    
     
    /**
     * 
     * @param p
     * @param PieceMap
     * @param textflow
     * @param labelGameStatus
     * @throws RemoteException
     * @throws MalformedURLException 
     */
    public void createAndRegisterGameClient(Player p, PieceMap PieceMap, TextFlow textflow, Label labelGameStatus) throws RemoteException, MalformedURLException{
        GameClient client = new GameClient(textflow);
        client.setPlayer(p);
        client.setPieceMap(PieceMap);
        client.setlabelGameStatus(labelGameStatus);
        String name = "//localhost/gameClientRef"+p.getIdPlayer();
        Naming.rebind(name,client);
        log("gameClientRef bind");
    }    
    
    private void log(String msg){
        System.out.println("*** REGISTRY ***" + msg);
    }
    
    public String getHost(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            
        }
        return "ERROR";
    }

    public int getPort() {
        return 1099;
    }

}
