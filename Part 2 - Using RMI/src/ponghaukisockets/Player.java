/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;

import javafx.scene.paint.Paint;

/**
 *
 * @author Nonato Dias
 */
public class Player {
    public String name;
    private Paint chatColor;

    public Player(String name) {
        this.name = name;
    }

    public void setChatColor(Paint chatColor) {
        this.chatColor = chatColor;
    }

    public Paint getChatColor() {
        return chatColor;
    }
    
    
}
