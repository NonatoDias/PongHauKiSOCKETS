/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;


/**
 *
 * @author Nonato Dias
 */
public class ClientSocket{	      
    
    static String host = "127.0.0.1?";
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
    
    public void connect(){
        try{
            this.socket = new Socket(host,port);
            System.out.println("Conectado....");
            onconnect.call();
            System.out.println("Conectado");
            
            this.output = new DataOutputStream(this.socket.getOutputStream());
            this.input = new DataInputStream(this.socket.getInputStream());
          
        }catch(Exception e){
            System.out.println("Erro ao conectar: "+ e.toString());
        }
    }
    
    /**
     *
     * @param myFunc
     */
    public void setOnConnect(Callable<Integer> func){
       onconnect = func;
   }
               

}
