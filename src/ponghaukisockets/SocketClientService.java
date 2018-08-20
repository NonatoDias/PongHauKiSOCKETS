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
public class SocketClientService extends Service<String>{

    private String action;
    private String data;
    
    private SocketClient client;
    

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
    
    public void setData(String message) {
        data = message;
    }

    public SocketClient getSocket() {
        return client;
    }

    public void setSocket(SocketClient client) {
        this.client = client;
    }
    

    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            
            protected String call() throws Exception {
                String message = protocolCONFIG.prepareRequest(action, data);
                client.sendMessage(message);
                return client.receiveMessage();
            }
        };
        
    }
}
