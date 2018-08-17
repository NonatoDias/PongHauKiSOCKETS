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
import java.util.concurrent.Callable;


/**
 *
 * @author Nonato Dias
 */
public class ClientSocketThread extends Thread{	      
    
    static String host = "127.0.0.1";
    static int port = 8080;
    String msg = "";
    
    
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input; 	
    
    private Callable<Integer> onconnect;
    
    ClientSocketThread(){
        this.socket = null;
        this.output = null;
        this.input = null; 
    }
    
    public void run() {
        try{
            this.socket = new Socket(host,port);
            log("Conectado....");
            onconnect.call();
            log("Cliente conectado");
            
            this.output = new DataOutputStream(this.socket.getOutputStream());
            this.input = new DataInputStream(this.socket.getInputStream());
            
            log(getMessage());
            sendMessage("MENSAGEM DO CLIENTE");
          
        }catch(Exception e){
            log("Erro ao conectar: "+ e.toString());
        }
        
    }
    
    public void connect(){
        this.start();
    }
    
    /**
     *
     * @param myFunc
     */
    public void setOnConnect(Callable<Integer> func){
       onconnect = func;
   }
            
    private void sendMessage(String text){
        try{
            this.output.writeUTF(text);
            this.output.flush();
        }catch (IOException ex) {
            System.out.println("ERROR "+ex.toString());
        }
    }
    
    private String getMessage(){
        String msg = null;
        try{
            msg = this.input.readUTF();
        }catch (IOException ex) {
            System.out.println("ERROR "+ex.toString());
        }
        return msg;
    }
    
    private void log(String text){
        String msg = "*** CLIENT *** "+text;
        System.out.println(msg);
    }

}
