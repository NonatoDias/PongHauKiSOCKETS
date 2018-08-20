/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author Nonato Dias
 */
public class SocketServerService extends Service<String>{
    private String action;
    private String data;
    
    private SocketServer server;
    

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
    
    public void setData(String message) {
        data = message;
    }

    public SocketServer getSocket() {
        return server;
    }

    public void setSocket(SocketServer server) {
        this.server = server;
    }
    

    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            
            protected String call() throws Exception {
                String message = ProtocolCONFIG.prepareRequest(action, data);
                server.sendMessage(message);
                return server.receiveMessage();
            }
        };
        
    }
}
