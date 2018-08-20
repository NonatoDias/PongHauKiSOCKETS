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

    public SocketClient() {
        this.socket = null;
        this.output = null;
        this.input = null;
        
        host = "127.0.0.1";
        port = 8080;
    }
    
    public void bindAndConnect() throws IOException{
        this.socket = new Socket(host,port);
        
        this.output = new DataOutputStream(this.socket.getOutputStream());
        this.input = new DataInputStream(this.socket.getInputStream());
        
        String msgToServer = protocolCONFIG.prepareRequest(protocolCONFIG.CONNECT, "posso conectar?");
        sendMessage(msgToServer);
        
        String msgFromServer = this.receiveMessage();
        String action = protocolCONFIG.getActionFromMessage(msgFromServer);
        String data = protocolCONFIG.getDataFromMessage(msgFromServer);
        
        
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
        String msg = "*** CLIENTSOCKET *** "+text;
        System.out.println(msg);
    }
}
