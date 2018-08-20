/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import ponghaukisockets.protocolCONFIG;

/**
 *
 * @author Nonato Dias
 */
public class SocketServer{
    private ServerSocket server;
    private java.net.Socket client = null;
    
    private DataOutputStream outputClient;
    private DataInputStream inputClient; 
    
    private static int port;
    
    /**
     * Constructor
     */
    public SocketServer(int port) {
        this.server = null;
        this.outputClient = null;
        this.inputClient = null; 
        this.port = port;
    }
    
    public void acceptAndConnect() throws IOException{
        this.server = new ServerSocket(port);
        log("inicializado na porta "+port+", aguardando cliente");
        this.client = this.server.accept();
        
        this.outputClient = new DataOutputStream(this.client.getOutputStream());
        this.inputClient = new DataInputStream(this.client.getInputStream());
        
        //recebe primeira mensagem do cliente
        String msgFromClient = this.receiveMessage();
        String action = protocolCONFIG.getActionFromMessage(msgFromClient);
        String data = protocolCONFIG.getDataFromMessage(msgFromClient);
        
        //envia mensagem para o servidor
        String msgToClient = protocolCONFIG.prepareResponse(protocolCONFIG.CONNECTED, "Servidor conectado com cliente");
        sendMessage(msgToClient);
    }
    
    public void sendMessage(String message){
        try{
            log(" send --- "+message);
            this.outputClient.writeUTF(message);
            this.outputClient.flush();
        }catch (IOException ex) {
            System.out.println("ERROR "+ex.toString());
        }
    }
    
    public String receiveMessage(){
        String message = null;
        try{
            message = this.inputClient.readUTF();
            //log(" receive --- "+message);
        }catch (IOException ex) {
            System.out.println("ERROR "+ex.toString());
        }
        return message;
    }
    
    private void log(String text){
        String msg = "*** SERVERSOCKET "+port+" *** "+text;
        System.out.println(msg);
    }
    
}
