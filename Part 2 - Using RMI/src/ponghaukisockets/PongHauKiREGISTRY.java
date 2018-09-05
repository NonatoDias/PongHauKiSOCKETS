package ponghaukisockets;


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
    public void initRMIRegistry() throws RemoteException, MalformedURLException{
        /** @TODO destruir thread ao fechar programa */
        this.registry = LocateRegistry.createRegistry(this.port);
        log("RMIRegistry iniciado");
    }
    
    public void createAndRegisterGameServer() throws RemoteException, MalformedURLException{
        GameServer server = new GameServer();
        Naming.rebind("//localhost/gameServerRef",server);
        log("gameServerRef bind");
    }    
     
    
    public void createAndRegisterGameClient(Player p, TextFlow textflow) throws RemoteException, MalformedURLException{
        GameClient client = new GameClient(textflow);
        client.setPlayer(p);
        String name = "//localhost/gameClientRef"+p.getIdPlayer();
        Naming.rebind(name,client);
        log("gameClientRef bind");
    }    
    
    private void log(String msg){
        System.out.println(msg);
    }

}
