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
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nonato Dias
 */
public class ServerSocketThread extends Thread{
    private LogText logText;
            
    private ServerSocket serversocket;
    private DataOutputStream output;
    private DataInputStream input; 
    
    
    private java.net.Socket clienteSoc1 = null;
    private java.net.Socket clienteSoc2 = null;
    
    static int port = 8080;

    public ServerSocketThread() {
        this.serversocket = null;
        this.output = null;
        this.input = null; 
    }
    
    public void init() throws IOException{
        this.serversocket = new ServerSocket(port);
        
        log("Servidor inicializado");
        log("Aguardando conexão...");
        clienteSoc1 = this.serversocket.accept();
        log("Conexão Estabelecida Com cliente 1");
        
        clienteSoc2 = this.serversocket.accept();
        log("Conexão Estabelecida Com cliente 2");
        
        //this.output = new DataOutputStream(this.serversocket.getOutputStream());
        //this.input = new DataInputStream(this.serversocket.getInputStream());
    }

    @Override
    public void run() {
        try {
            this.init();
        } catch (IOException ex) {
            Logger.getLogger(ServerSocketThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("run java");
        
    }
    
    public void setMethodLogText(Callable<Integer> func){
        
    }

    void setLogText(LogText logText) {
        this.logText = logText;
    }
    
    private void log(String text){
        System.out.println("*** SERVER ***  "+text);
        //logText.log(text);
    }
    
}
