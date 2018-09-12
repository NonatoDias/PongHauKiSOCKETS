package ponghaukisockets.rmi;


import com.jfoenix.controls.JFXDialog;
import ponghaukisockets.model.PieceMap;
import ponghaukisockets.model.Player;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javafx.concurrent.Task;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;
import ponghaukisockets.model.ModalAlert;

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
        //this.registry = LocateRegistry.getRegistry(port);
        log("RMIRegistry iniciado");
    }
    
    public void closeRMIRegistry() throws NoSuchObjectException{
        //UnicastRemoteObject.unexportObject(this.registry, true);
    }
    
    /**
     * Cria o processo RMIRegistry
     * @param port
     * @return Registry
     * @throws RemoteException
     * @throws MalformedURLException 
     */
    public Registry getRMIRegistry(int port) throws RemoteException, MalformedURLException{
        /** @TODO destruir thread ao fechar programa */
        this.port = port;
        this.registry = LocateRegistry.getRegistry(port);
        log("Referencia RMIRegistry");
        return this.registry;
    }
    
    /**
     * Cria e registra uma instancia GameServer
     * @throws RemoteException
     * @throws MalformedURLException 
     */
    public void createAndRegisterGameServer() throws RemoteException, MalformedURLException{
        GameServer server = new GameServer();
        String name = "//"+this.getHost()+":"+this.port+"/gameServerRef";
        Naming.rebind(name,server);
        log("gameServerRef bind: "+name);
    }    
    
    //public unbind
     
    /**
     * 
     * @param player
     * @param PieceMap
     * @param textflow
     * @param labelGameStatus
     * @param dialogAlert
     * @throws RemoteException
     * @throws MalformedURLException 
     */
    public void createAndRegisterGameClient(String serverName, Player p, PieceMap pieceMap, TextFlow textflow, Label labelGameStatus, ModalAlert modalAlert) throws RemoteException, MalformedURLException{
        GameClient client = new GameClient();
        client.setPlayer(p);
        client.setTextFlow(textflow);
        client.setPieceMap(pieceMap);
        client.setlabelGameStatus(labelGameStatus);
        client.setModalAlert(modalAlert);
        String name = "//"+serverName+"/gameClientRef"+p.getIdPlayer();
        Naming.rebind(name,client);
        log("gameClientRef bind: "+name);
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
    
    public int getPort(){
        return this.port;
    }

    public static int getDefaultPort() {
        return 1099;
    }

}
