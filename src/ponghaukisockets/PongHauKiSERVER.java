package ponghaukisockets;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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
    String host = "localhost";
    int client_ONE_index = 0;
    int client_TWO_index = 1;
    
    int countPlaying = 0;

    public PongHauKiSERVER(int portServer, int portClient1, int portClient2) {
        this.portServer = portServer;
        this.portClient_ONE = portClient1;
        this.portClient_TWO = portClient2;
        
        this.socketClients = new ArrayList<>();
        server = new SocketServer();
    }
    
    
    public void initServer() throws IOException{
        //Cria o socket e inicializa a thread
        server.init(portServer);
        initThreadServer1(); 
    }
    
    public void initThreadServer1(){
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
    }
    
    public void initThreadServer2(){
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
        socketClients.add(new SocketClient(host, portClient_ONE));
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
        socketClients.add(new SocketClient(host, portClient_TWO));
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
        String msgResp = "";
        
        switch(ProtocolCONFIG.getActionFromMessage(msg)){
            case "addmessage": 
                msgResp = ProtocolCONFIG.prepareResponse(ProtocolCONFIG.RESULT_OK, "ok message");
                if(countPlaying < 2){
                    returnMessageToChat(clientNum, dataFrom);
                    server.sendMessage(clientNum, msgResp);
                }else{
                    server.sendMessage(client_ONE_index, msgResp);
                    returnMessageToChat(client_ONE_index, dataFrom);
                    server.sendMessage(client_TWO_index, msgResp);
                    returnMessageToChat(client_TWO_index, dataFrom);
                }
                break;
                
            case "getgameconfigs":
                countPlaying++;
                String player = countPlaying > 1 ? "PLAYER_YELLOW" : "PLAYER_BLUE";
                msgResp = ProtocolCONFIG.prepareResponse(ProtocolCONFIG.RESULT_OK, player);
                server.sendMessage(clientNum, msgResp);
                break;
            
            case "quitgame":
                String player_ = clientNum == client_ONE_index ? "PLAYER_BLUE" : "PLAYER_YELLOW";
                String winner = clientNum == client_TWO_index ? "PLAYER_BLUE" : "PLAYER_YELLOW";
                msgResp = ProtocolCONFIG.prepareResponse(ProtocolCONFIG.RESULT_OK, "jogador desitiu "+player_);
                
                server.sendMessage(client_ONE_index, msgResp);
                returnWinningPlayer(client_ONE_index, winner+ProtocolCONFIG.and_+"Houve desistência do outro jogador");
                server.sendMessage(client_TWO_index, msgResp);
                returnWinningPlayer(client_TWO_index, winner+ProtocolCONFIG.and_+"Houve desistência do outro jogador");
                
                break;
            case "movepiece":
                msgResp = ProtocolCONFIG.prepareResponse(ProtocolCONFIG.RESULT_OK, "ok movimente "+dataFrom);
                //server.sendMessage(clientNum, msgResp);
                server.sendMessage(client_ONE_index, msgResp);
                returnMovimentControl(client_ONE_index, dataFrom);
                
                server.sendMessage(client_TWO_index, msgResp);
                returnMovimentControl(client_TWO_index, dataFrom);
                
                break;
             
            default: 
                break;
        }
        
    }
    
    public void returnMessageToChat(int clientNum, String msg){
        SocketClientService service = new SocketClientService();
        service.setSocket(socketClients.get(clientNum));
        service.setAction("returnmessagetochat");
        service.setData(msg);
        service.setOnSucceeded((e)->{
            String resp = e.getSource().getValue().toString();
            String code = ProtocolCONFIG.getActionFromMessage(resp);
            if(code.equals(ProtocolCONFIG.RESULT_OK)){
                String data = ProtocolCONFIG.getDataFromMessage(resp);
                System.out.println("Resposta "+data);
            }       
        });
        service.start();
    }
    
    public void returnMovimentControl(int clientNum, String msg){
        SocketClientService service = new SocketClientService();
        service.setSocket(socketClients.get(clientNum));
        service.setAction("returnmovimentcontrol");
        service.setData(msg);
        service.setOnSucceeded((e)->{
            String resp = e.getSource().getValue().toString();
            String code = ProtocolCONFIG.getActionFromMessage(resp);
            if(code.equals(ProtocolCONFIG.RESULT_OK)){
                String data = ProtocolCONFIG.getDataFromMessage(resp);
                System.out.println("Resposta "+data);
            }       
        });
        service.start();
    }
    
    public void returnWinningPlayer(int clientNum, String msg){
        SocketClientService service = new SocketClientService();
        service.setSocket(socketClients.get(clientNum));
        service.setAction("returnwinningplayer");
        service.setData(msg);
        service.setOnSucceeded((e)->{
            String resp = e.getSource().getValue().toString();
            String code = ProtocolCONFIG.getActionFromMessage(resp);
            if(code.equals(ProtocolCONFIG.RESULT_OK)){
                String data = ProtocolCONFIG.getDataFromMessage(resp);
                System.out.println("Resposta "+data);
            }       
        });
        service.start();
    }
    
    public String getLocalIp() throws UnknownHostException{
        return server.getLocalIp();
    }
}
