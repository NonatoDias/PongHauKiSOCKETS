/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;

import javafx.scene.shape.Circle;

/**
 *
 * @author Nonato Dias
 */
public class PieceMap {
    /*private boolean position1;//superior esquerdo
    private boolean position2;//superior direito
    private boolean position3;//inferior esquerdo
    private boolean position4;//inferior direito
    private boolean position5;//posicao do meio*/
    
    public Piece pieceblueA;
    public Piece pieceblueB;
    public Piece pieceYellowA;
    public Piece pieceYellowB;
    
    public PieceMap() {
        
    }

    public void setPieceblueA(Piece pieceblueA) {
        this.pieceblueA = pieceblueA;
    }

    public void setPieceblueB(Piece pieceblueB) {
        this.pieceblueB = pieceblueB;
    }

    public void setPieceYellowA(Piece pieceYellowA) {
        this.pieceYellowA = pieceYellowA;
    }

    public void setPieceYellowB(Piece pieceYellowB) {
        this.pieceYellowB = pieceYellowB;
    }
    
    public void moveBlueA() {
        PieceMovement.moveCircle(this.pieceblueA.circle);
    }
    
}
