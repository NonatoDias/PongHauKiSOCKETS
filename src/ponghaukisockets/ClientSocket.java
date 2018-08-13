/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


/**
 *
 * @author Nonato Dias
 */
public class ClientSocket{
    DataOutputStream out = null;
    DataInputStream in = null; 		      
    Socket soc = null; 
    static String host = "";
    static int port = 9090;
    String msg = "";
    
    ClientSocket(){
       
    }
    
    public void run(){
        try{
            soc = new Socket(host,port);
            out = new DataOutputStream(soc.getOutputStream());
            out.writeUTF("teste");   

            in = new DataInputStream(soc.getInputStream());
            msg = in.readUTF();		     
            System.out.println("Stream Recebida: " + msg);

        }catch(Exception e){}
    }
}
