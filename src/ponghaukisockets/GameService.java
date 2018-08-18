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
public class GameService extends Service<String>{

    private String action;
    private ClientSocket clientSocket;
    private Object data;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ClientSocket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }
    
    
    
    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            
            protected String call() throws Exception {
                clientSocket.sendMessage(action + "==>"+ data);
                return clientSocket.getMessage();
            }
        };
        
    }

    void setData(Object message) {
        data = message;
    }
}
