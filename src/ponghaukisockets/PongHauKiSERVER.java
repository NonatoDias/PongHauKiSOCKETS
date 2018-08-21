package ponghaukisockets;


import java.io.IOException;
import javafx.concurrent.Task;
import ponghaukisockets.ProtocolCONFIG;
import ponghaukisockets.SocketClient;
import ponghaukisockets.SocketServer;


/**
 *
 * @author Nonato Dias
 */
public class PongHauKiSERVER {
    private SocketServer server;
    private SocketClient client;
    
    int portClient = 8080;
    int portServer = 8000;
    
    
    public void initThreadServer(){
        //Cria o socket e inicializa a thread
        server = new SocketServer(portServer);
        Task task = new Task<Void>() {
            @Override public Void call() throws IOException {
                server.acceptAndConnect();
                initThreadClient();
                
                while(true){
                    String msg = server.receiveMessage();
                    String msgResp = ProtocolCONFIG.prepareResponse(ProtocolCONFIG.RESULT_OK, msg+" RECEBI A MENSAGEM");
                    server.sendMessage(msgResp);
                }
            }
        };
        Thread threadSocket = new Thread(task);
        threadSocket.setDaemon(true);//Mata a thread qdo fecha a janela
        threadSocket.start();
    }
    
    public void initThreadClient(){
        //Cria o socket e inicializa a thread
        client = new SocketClient(portClient);
        Task task = new Task<Void>() {
            @Override public Void call() throws IOException {
                client.bindAndConnect();
                
                while(true){
                    String msg = client.receiveMessage();
                    String msgResp = ProtocolCONFIG.prepareResponse(ProtocolCONFIG.RESULT_OK, msg+" RECEBI A MENSAGEM");
                    client.sendMessage(msgResp);
                }
            }
        };
        Thread threadSocket = new Thread(task);
        threadSocket.setDaemon(true);//Mata a thread qdo fecha a janela
        threadSocket.start();
    }
}
