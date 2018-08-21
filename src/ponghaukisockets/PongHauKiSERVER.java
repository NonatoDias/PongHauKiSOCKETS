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
                while(true){
                    String msg = server.receiveMessage(client_ONE_index);
                    resolveMessages(client_ONE_index, msg);
                }
            }
        };
        Thread threadSocket = new Thread(task);
        threadSocket.setDaemon(true);//Mata a thread qdo fecha a janela
        threadSocket.start();
        
        
        Task task2 = new Task<Void>() {
            @Override public Void call() throws IOException { 
                server.acceptAndConnect();
                initThreadClient2();
                while(true){
                    String msg = server.receiveMessage(client_TWO_index);
                    resolveMessages(client_TWO_index, msg);
                }
            }
        };
        Thread threadSocket2 = new Thread(task2);
        threadSocket2.setDaemon(true);//Mata a thread qdo fecha a janela
        threadSocket2.start();
    }
    
    public void initThreadClient1(){
        int indexClient = client_ONE_index;
        socketClients.add(new SocketClient(portClient_ONE));
        Task task = new Task<Void>() {
            @Override public Void call() throws IOException {
                socketClients.get(indexClient).bindAndConnect();
                return null;
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
                return null;
            }
        };
        Thread threadSocket = new Thread(task);
        threadSocket.setDaemon(true);//Mata a thread qdo fecha a janela
        threadSocket.start();
    }
    
    public void resolveMessages(int clientNum, String msg){
        String dataFrom = ProtocolCONFIG.getDataFromMessage(msg);
        String dataTo = "";
        
        switch(ProtocolCONFIG.getActionFromMessage(msg)){
            case "addmessage": 
                System.out.println("Messagem recebida aqui "+dataFrom);
                returnMessageToChat(client_ONE_index, dataFrom);
                //returnMessageToChat(client_TWO_index, dataFrom);
                break;
            default: 
                break;
        }
        
        String msgResp = ProtocolCONFIG.prepareResponse(ProtocolCONFIG.RESULT_OK, "ok");
        server.sendMessage(clientNum, msgResp);
    }
    
    public void returnMessageToChat(int clientNum, String msg){
        SocketClientService service = new SocketClientService();
        service.setSocket(socketClients.get(clientNum));
        service.setAction("returnmessagetochat");
        service.setData(msg);
        service.setOnSucceeded((e)->{
            String resp = e.getSource().getValue().toString();
            String code = ProtocolCONFIG.getCodeFromResponse(resp);
            if(code.equals(ProtocolCONFIG.RESULT_OK)){
                String data = ProtocolCONFIG.getDataFromResponse(resp);
                System.out.println("Resposta "+data);
            }       
        });
        service.start();
    }
}
