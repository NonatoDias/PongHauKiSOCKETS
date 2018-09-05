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
    
    public void createAndRegisterServerChat() throws RemoteException, MalformedURLException{
        ServerChat serverChat = new ServerChat();
        Naming.rebind("//localhost/chatMethodsRef",serverChat);
        log("serverChatRef bind");
    }    
    
    
    public void createAndRegisterClientChat(TextFlow textflow) throws RemoteException, MalformedURLException{
        ClientChat clientChat = new ClientChat(textflow);
        Naming.rebind("//localhost/clientChatRef",clientChat);
        log("clientChatRef bind");
    }    
    
    
    private void log(String msg){
        System.out.println(msg);
    }
}
