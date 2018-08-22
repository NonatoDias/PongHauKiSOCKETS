/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import ponghaukisockets.ProtocolCONFIG;

/**
 *
 * @author Nonato Dias
 */
public class SocketServer{
    private ServerSocket server;
    private List<Socket> clients = null;
    
    private List<DataOutputStream> outputClient;
    private List<DataInputStream> inputClient;
    
    private static int port;
    private int count_connections = 0;
    
    /**
     * Constructor
     */
    public SocketServer() {
        this.server = null;
        this.outputClient = null;
        this.inputClient = null; 
        
        clients = new ArrayList<>();
        outputClient = new ArrayList<>();
        inputClient = new ArrayList<>();
    }
    
    public void init(int port) throws IOException{
        this.port = port;
        this.server = new ServerSocket(port);
    }
    
    public void acceptAndConnect() throws IOException{
        count_connections++;
        int indexClient = count_connections-1;
        
        log("inicializado na porta "+port+", aguardando cliente");
        this.clients.add(this.server.accept());
        
        this.outputClient.add(new DataOutputStream(this.clients.get(indexClient).getOutputStream()));
        this.inputClient.add(new DataInputStream(this.clients.get(indexClient).getInputStream()));
        
        //recebe primeira mensagem do cliente
        String msgFromClient = this.receiveMessage(indexClient);
        String action = ProtocolCONFIG.getActionFromMessage(msgFromClient);
        String data = ProtocolCONFIG.getDataFromMessage(msgFromClient);
        
        
        //envia mensagem para o servidor
        String msgToClient = ProtocolCONFIG.prepareResponse(ProtocolCONFIG.CONNECTED, "Servidor conectado com cliente");
        sendMessage(indexClient, msgToClient);
    }
    
    public void sendMessage(int indexClient, String message){
        try{
            log(" send --- "+message);
            this.outputClient.get(indexClient).writeUTF(message);
            this.outputClient.get(indexClient).flush();
        }catch (IOException ex) {
            System.out.println("ERROR sendMessage "+ex.toString());
        }
    }
    
    public String receiveMessage(int indexClient){
        String message = null;
        try{
            message = this.inputClient.get(indexClient).readUTF();
            log(indexClient+" receive --- "+message);
        }catch (IOException ex) {
            System.out.println("ERROR receiveMessage "+ex.toString());
        }
        return message;
    }
    
    private void log(String text){
        String msg = "*** SOCKET-SERVER "+port+" *** "+text;
        System.out.println(msg);
    }
    
    
    public String getLocalIp() throws UnknownHostException{
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostAddress();
    }
}
