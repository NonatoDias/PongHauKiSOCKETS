/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author Nonato Dias
 */
public class LogText {
    private TextFlow logTextFlow;

    public LogText(TextFlow logTextFlow) {
        this.logTextFlow = logTextFlow;
    }
    
    public void log(String text){
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Text dth = new Text(df.format(new Date()).toString() +" -- ");
        dth.setFont(Font.font("Helvetica", FontPosture.REGULAR, 14));
        
        Text text1 = new Text(text+"\n");
        text1.setFont(Font.font("Helvetica", FontPosture.REGULAR, 16));
        logTextFlow.getChildren().addAll(dth, text1);
    }
}
