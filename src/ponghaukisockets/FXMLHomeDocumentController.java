/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Nonato Dias
 */
public class FXMLHomeDocumentController implements Initializable {
    
    private ServerSocketThread serverSocket;
    private LogText logText;
    
    @FXML
    private JFXButton btnServer;

    @FXML
    private JFXButton btnClient;

    @FXML
    private TextFlow logTextFlow;
    
    @FXML
    private StackPane dialogStackPane;
    
    
    
    private int maxAllowedClients = 2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logText = new LogText(logTextFlow);
        
        //Conexao
        serverSocket = new ServerSocketThread();
        /*serverSocket.setMethodLogText(()->{
            
            return null;
        });*/
        serverSocket.setLogText(logText);
        
        // TODO
        btnClient.setOnAction((e)->{
            if(maxAllowedClients == 0){
                return;
            }
            logText.log("Inicializando cliente game");
            Parent game = null;
            try {
                game = FXMLLoader.load(getClass().getResource("FXMLGameDocument.fxml"));
            } catch (IOException ex) {
                logText.log("ERROR: "+ex.toString());
            }
            Stage stage2 = new Stage();
            Scene scene2 = new Scene(game);
            stage2.setScene(scene2);
            stage2.show();
            maxAllowedClients--;
            if(maxAllowedClients == 0){
                btnClient.setDisable(true);
            }
        
        });
        
        btnServer.setOnAction((e)->{
            logText.log("Inicializando SERVIDOR");
            serverSocket.start();
            btnServer.setDisable(true);
        });
    }    
    
    public void alertText(String text){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Atenção"));
        content.setBody(new Text(text));
        JFXDialog dialog = new JFXDialog(dialogStackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton btn = new JFXButton("OK");
        
        btn.setOnAction((e)->{
            dialogStackPane.setVisible(false);
            dialog.close();
        });
        content.setActions(btn);
        dialogStackPane.setVisible(true);
        dialog.show();
    }
}
