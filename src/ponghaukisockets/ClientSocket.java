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
public class ClientSocket{	      
    
    static String host = "127.0.0.1";
    static int port = 8080;
    String msg = "";
    
    
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input; 	
    
    private Callable<Integer> onconnect;
    
    ClientSocket(){
        this.socket = null;
        this.output = null;
        this.input = null; 
    }
    
    public void run() {
        //caso use thread
        
    }
    
    public void connect(){
        try{
            this.socket = new Socket(host,port);
            log("Conectado....");
            onconnect.call();
            log("Cliente conectado");
            
            this.output = new DataOutputStream(this.socket.getOutputStream());
            this.input = new DataInputStream(this.socket.getInputStream());
            
            //testConect();
          
        }catch(Exception e){
            log("Erro ao conectar: "+ e.toString());
        }
    }
    
    /**
     *
     * @param myFunc
     */
    public void setOnConnect(Callable<Integer> func){
       onconnect = func;
   }
            
    public void sendRequest(String req){
        try{
            log("mensagem --- "+req);
            this.output.writeUTF(req);
            this.output.flush();
        }catch (IOException ex) {
            System.out.println("ERROR "+ex.toString());
        }
    }
    
    public String getResponse(){
        String resp = null;
        try{
            resp = this.input.readUTF();
        }catch (IOException ex) {
            System.out.println("ERROR "+ex.toString());
        }
        return resp;
    }
    
    private void log(String text){
        String msg = "*** CLIENT *** "+text;
        System.out.println(msg);
    }

    private void testConect() {
        log(getResponse());
        sendRequest("MENSAGEM DO CLIENTE");
    }

}
