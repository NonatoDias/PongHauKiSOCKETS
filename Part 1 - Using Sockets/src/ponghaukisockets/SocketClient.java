/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Nonato Dias
 */
public class SocketClient {
    static String host;
    static int port;
    
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input; 

    public SocketClient(String host, int port) {
        this.socket = null;
        this.output = null;
        this.input = null;
        
        this.host = host;
        this.port = port;
    }
    
    //@todo timeout
    public String bindAndConnect() throws IOException{
        if(this.socket == null || !this.socket.isConnected()){
            this.socket = new Socket(host,port);
        }
        
        log("cliente inicializado na porta "+port);
        
        this.output = new DataOutputStream(this.socket.getOutputStream());
        this.input = new DataInputStream(this.socket.getInputStream());
        
        String msgToServer = ProtocolCONFIG.prepareRequest(ProtocolCONFIG.CONNECT, "cliente conectado com servidor");
        sendMessage(msgToServer);
        /*sendMessage(msgToServer);
        sendMessage(msgToServer);
        sendMessage(msgToServer);
        sendMessage(msgToServer);
        sendMessage(msgToServer);*/
        
        
        
        
        return this.receiveMessage();
    }
    
    public void sendMessage(String message){
        try{
            log(" send --- "+message);
            this.output.writeUTF(message);
            this.output.flush();
        }catch (IOException ex) {
            System.out.println("ERROR "+ex.toString());
        }
    }
    
    public String receiveMessage(){
        String message = null;
        try{
            message = this.input.readUTF();
            log(" receive --- "+message);
        }catch (IOException ex) {
            System.out.println("ERROR "+ex.toString());
        }
        return message;
    }
    
    
    private void log(String text){
        String msg = "*** SOCKETCLIENT "+port+" *** "+text;
        System.out.println(msg);
    }
}
