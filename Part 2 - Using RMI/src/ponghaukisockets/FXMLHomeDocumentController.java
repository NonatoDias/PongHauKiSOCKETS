/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
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

import ponghaukisockets.PongHauKiREGISTRY;

/**
 * FXML Controller class
 *
 * @author Nonato Dias
 */
public class FXMLHomeDocumentController implements Initializable {
    private int maxAllowedClients = 2;
    private PongHauKiREGISTRY pongHauKiREGISTRY;
    
    @FXML
    private JFXButton btnServer;

    @FXML
    private JFXButton btnClient;

    @FXML
    private TextFlow logTextFlow;
    
    @FXML
    private StackPane dialogStackPane;
    
    @FXML
    private JFXTextField jfxTextFieldServerPort;
    
    @FXML
    private JFXTextField jfxTextFieldGamePort1;

    @FXML
    private JFXTextField jfxTextFieldGamePort2;
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pongHauKiREGISTRY = new PongHauKiREGISTRY();
        try {
            pongHauKiREGISTRY.initRMIRegistry();
        } catch (Exception ex) {
            
        } 
        addEventsToTheView();
    }    
    
    
    
    public void createWindowGame(){
        Parent game = null;
        try {
            game = FXMLLoader.load(getClass().getResource("FXMLGameDocument.fxml"));
        } catch (IOException ex) {
            log("ERROR: "+ex.toString());
        }
        Stage stage2 = new Stage();
        Scene scene2 = new Scene(game);
        stage2.setScene(scene2);
        stage2.setResizable(false);
        stage2.show();
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
    
    /**
     * Loga na view 
     * @param text 
     */
    private void log(String text){
        String msg = "*** HOME *** "+text;
        System.out.println(msg);
        
        //Login na view
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Text dth = new Text(df.format(new Date()).toString() +" -- ");
        dth.setFont(Font.font("Helvetica", FontPosture.REGULAR, 14));
        
        Text text1 = new Text(text+"\n");
        text1.setFont(Font.font("Helvetica", FontPosture.REGULAR, 16));
        logTextFlow.getChildren().addAll(dth, text1);
    }

    private void addEventsToTheView() {
         //Cria window game
         btnClient.setOnAction((e)->{
            if(maxAllowedClients == 0){
                return;
            }
            log("GAME inicializado");
            createWindowGame();
            maxAllowedClients--;
            if(maxAllowedClients == 0){
                btnClient.setDisable(true);
            }
        });
        
        //Inicia thread server
        btnServer.setOnAction((e)->{
            try {
                pongHauKiREGISTRY.createAndRegisterServerChat();
            } catch (Exception ex) {
                log("ERROR: "+ex.toString());
            }
            btnServer.setDisable(true);
        });
    }
}
