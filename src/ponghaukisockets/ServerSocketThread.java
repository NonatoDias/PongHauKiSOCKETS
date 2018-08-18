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
import javafx.concurrent.Task;

/**
 *
 * @author Nonato Dias
 */
public class ServerSocketThread extends Thread{
    private static final int CLIENT_ONE = 1;
    private static final int CLIENT_TWO = 2;
    
    private ServerSocket serversocket;
    private DataOutputStream outputClient1;
    private DataInputStream inputClient1; 
    
    private DataOutputStream outputClient2;
    private DataInputStream inputClient2; 
    
    
    private java.net.Socket clientSoc1 = null;
    private java.net.Socket clientSoc2 = null;
    
    static int port = 8080;

    public ServerSocketThread() {
        this.serversocket = null;
        this.outputClient1 = null;
        this.inputClient1 = null; 
        this.outputClient2 = null;
        this.inputClient2 = null; 
    }
    
    public void init() throws IOException{
        this.serversocket = new ServerSocket(port);
        
        log("Servidor inicializado");
        log("Aguardando conexão...");
        
        //Socket Client 1
        clientSoc1 = this.serversocket.accept();
        log("Conexão Estabelecida Com cliente 1");
        this.outputClient1 = new DataOutputStream(this.clientSoc1.getOutputStream());
        this.inputClient1 = new DataInputStream(this.clientSoc1.getInputStream());
        this.sendMessageToClient(CLIENT_ONE, "Conected 1");
        log(getMessageFromClient(CLIENT_ONE));
        //Cliente 1
        Task task = new Task<Void>() {
            @Override public Void call() {
                while(true){
                    String request = getMessageFromClient(CLIENT_ONE);
                    System.out.println("Remoto 111111: "+request);
                    sendMessageToClient(CLIENT_ONE, "Toudo certo");
                }
            }
        };
        new Thread(task).start();
        
        
        
        
        
        
        //Socket Client 2
        clientSoc2 = this.serversocket.accept();
        log("Conexão Estabelecida Com cliente 2");
        this.outputClient2 = new DataOutputStream(this.clientSoc2.getOutputStream());
        this.inputClient2 = new DataInputStream(this.clientSoc2.getInputStream());
        this.sendMessageToClient(CLIENT_TWO, "Conected 2");
        log(getMessageFromClient(CLIENT_TWO));
        
        //Cliente 2
        Task task2 = new Task<Void>() {
            @Override public Void call() {
                while(true){
                    String request = getMessageFromClient(CLIENT_TWO);
                    System.out.println("Remoto 111111: "+request);
                    sendMessageToClient(CLIENT_ONE, "Toudo certo");
                }
            }
        };
        new Thread(task2).start();
        
    }

    @Override
    public void run() {
        try {
            this.init();
        } catch (IOException ex) {
            System.out.println("ERROR "+ex.toString());
        }
        System.out.println("run java");
        
    }

    private void log(String text){
        System.out.println("*** SERVER ***  "+text);
        //logText.log(text);
    }
    
    private String getMessageFromClient(int clientNum){
        String msg = "";
        try {
            switch(clientNum){
                case CLIENT_ONE:
                    msg = this.inputClient1.readUTF();
                    break;
                case CLIENT_TWO:
                    msg = this.inputClient2.readUTF();
                    break;
                default:
                    break;
            }
        }catch (IOException ex) {
            System.out.println("ERROR "+ex.toString());
        }
        return msg;
    }
    
    
    private void sendMessageToClient(int clientNum, String msg){
        try {
            switch(clientNum){
                case CLIENT_ONE:
                    this.outputClient1.writeUTF(msg);
                    this.outputClient1.flush();
                    break;
                case CLIENT_TWO:
                     this.outputClient2.writeUTF(msg);
                     this.outputClient2.flush();
                    break;
                default:
                    break;
            }
        }catch (IOException ex) {
            System.out.println("ERROR "+ex.toString());
        }
    }
   
}
