/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

/**
 *
 * @author Nonato Dias
 */
public class Player {
    private String idPlayer;
    private String name;
    private Paint chatColor;

    public Player() {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String id = "ID"+df.format(new Date()).toString();
           
        this.idPlayer = id;
        //this.name = name;
    }

    public void setChatColor(Paint chatColor) {
        this.chatColor = chatColor;
    }

    public Paint getChatColor() {
        return chatColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdPlayer() {
        return idPlayer;
    }

}
