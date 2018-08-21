package ponghaukisockets;


import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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

    private List<SocketClient> socketClients = null;
    
    int portClient_ONE = 8080;
    int portClient_TWO = 9090;
    int portServer = 8000;
    
    int client_ONE_index = 0;
    int client_TWO_index = 1;

    public PongHauKiSERVER() {
        this.socketClients = new ArrayList<>();
        server = new SocketServer();
    }
    
    
    public void initThreadServer() throws IOException{
        //Cria o socket e inicializa a thread
        server.init(portServer);
        Task task = new Task<Void>() {
            @Override public Void call() throws IOException {
                server.acceptAndConnect();
                initThreadClient1();
                
                server.acceptAndConnect();
                initThreadClient2();
                
                while(true){
                    String msg = server.receiveMessage(client_ONE_index);
                    String msgResp = ProtocolCONFIG.prepareResponse(ProtocolCONFIG.RESULT_OK, msg+" RECEBI A MENSAGEM");
                    server.sendMessage(client_ONE_index, msgResp);
                }
            }
        };
        Thread threadSocket = new Thread(task);
        threadSocket.setDaemon(true);//Mata a thread qdo fecha a janela
        threadSocket.start();
    }
    
    public void initThreadClient1(){
        int indexClient = client_ONE_index;
        socketClients.add(new SocketClient(portClient_ONE));
        Task task = new Task<Void>() {
            @Override public Void call() throws IOException {
                socketClients.get(indexClient).bindAndConnect();
                
                while(true){
                    String msg = socketClients.get(indexClient).receiveMessage();
                    String msgResp = ProtocolCONFIG.prepareResponse(ProtocolCONFIG.RESULT_OK, msg+" RECEBI A MENSAGEM");
                    socketClients.get(indexClient).sendMessage(msgResp);
                }
            }
        };
        Thread threadSocket = new Thread(task);
        threadSocket.setDaemon(true);//Mata a thread qdo fecha a janela
        threadSocket.start();
    }
    
    public void initThreadClient2(){
        int indexClient = client_TWO_index;
        socketClients.add(new SocketClient(portClient_TWO));
        Task task = new Task<Void>() {
            @Override public Void call() throws IOException {
                socketClients.get(indexClient).bindAndConnect();
                
                while(true){
                    String msg = socketClients.get(indexClient).receiveMessage();
                    String msgResp = ProtocolCONFIG.prepareResponse(ProtocolCONFIG.RESULT_OK, msg+" RECEBI A MENSAGEM");
                    socketClients.get(indexClient).sendMessage(msgResp);
                }
            }
        };
        Thread threadSocket = new Thread(task);
        threadSocket.setDaemon(true);//Mata a thread qdo fecha a janela
        threadSocket.start();
    }
}
