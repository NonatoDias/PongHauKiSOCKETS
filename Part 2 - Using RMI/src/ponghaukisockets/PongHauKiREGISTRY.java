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
    public void init() throws RemoteException, MalformedURLException{
        this.registry = LocateRegistry.createRegistry(this.port);
        ServerChat serverChat = new ServerChat();
        
        Naming.rebind("//localhost/chatMethodsRef",serverChat);
    }
    
    /*public void initThreadServer1(){
        Task task = new Task<Void>() {
            @Override public Void call() throws IOException {
                server.acceptAndConnect();
                initThreadClient1();
                
                initThreadServer2();
                
                while(true){
                    String msg = server.receiveMessage(client_ONE_index);
                    resolveMessages(client_ONE_index, msg);
                }
            }
        };
        Thread threadSocket = new Thread(task);
        threadSocket.setDaemon(true);//Mata a thread qdo fecha a janela
        threadSocket.start();
    }*/
    
}
