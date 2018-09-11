/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets.model;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 *
 * @author Nonato Dias
 */
public class ModalAlert {

    private final StackPane dialogStackPane;
    private final EventHandler<ActionEvent> onShow;

    public ModalAlert(StackPane dialogStackPane, EventHandler<ActionEvent> onShow) {
        this.dialogStackPane = dialogStackPane;
        this.onShow = onShow;
    }
    
    
    public void show(String msg){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("FIM DE JOGO"));
        content.setBody(new Text(msg));
        
        JFXDialog dialogAlert = new JFXDialog(dialogStackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton btnDialogOK = new JFXButton("Reiniciar partida");
        
        btnDialogOK.setOnAction((e)->{
            //restartGame();
            onShow.handle(e);
            dialogAlert.close();
            dialogStackPane.setVisible(false);
        });
        
        dialogStackPane.setOnMouseClicked((e)->{
            this.show(msg);
        });
        
        content.setActions(btnDialogOK);
        dialogStackPane.setVisible(true);
        dialogAlert.show();
    }
}
