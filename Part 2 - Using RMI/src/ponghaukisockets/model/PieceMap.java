/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets.model;

import javafx.scene.shape.Circle;

/**
 *
 * @author Nonato Dias
 */
public class PieceMap {
    public Piece pieceblueA;
    public Piece pieceblueB;
    public Piece pieceYellowA;
    public Piece pieceYellowB;
    public PieceMovement pm = new PieceMovement();
    
    private boolean positionsStatus [];
    
    
    public PieceMap() {
        this.positionsStatus = new boolean[6];
        for (int i = 0; i <= 5; i++) {
            this.positionsStatus[i] = false;
        }
    }

    public void setPieceblueA(Piece pieceblueA) {
        this.pieceblueA = pieceblueA;
        this.positionsStatus[pieceblueA.position] = true;
    }

    public void setPieceblueB(Piece pieceblueB) {
        this.pieceblueB = pieceblueB;
        this.positionsStatus[pieceblueB.position] = true;
    }

    public void setPieceYellowA(Piece pieceYellowA) {
        this.pieceYellowA = pieceYellowA;
        this.positionsStatus[pieceYellowA.position] = true;
    }

    public void setPieceYellowB(Piece pieceYellowB) {
        this.pieceYellowB = pieceYellowB;
        this.positionsStatus[pieceYellowB.position] = true;
    }
    
    public void moveBlueA() {
        move(this.pieceblueA);
    }

    public void moveBlueB() {
        move(this.pieceblueB);
    }

    public void moveYellowA() {
        move(this.pieceYellowA);
    }

    public void moveYellowB() {
        move(this.pieceYellowB);
    }
    
    private void move(Piece p){
        switch(p.position){
            case 1: 
                moveFromPosition1(p);
                break;
            case 2: 
                moveFromPosition2(p);
                break;
            case 3: 
                moveFromPosition3(p);
                break;
            case 4: 
                moveFromPosition4(p);
                break;
            case 5: 
                moveFromPosition5(p);
                break;
        }
    }

    private void moveFromPosition1(Piece p) {
        if(this.positionsStatus[3] == false){
            togglePosition(p, 3);
            pm.moveVertically(p.circle);
            
       }else if(this.positionsStatus[5] == false){
           togglePosition(p, 5);
           pm.moveToCenter(p.circle);
       }
    }
    
    private void moveFromPosition2(Piece p) {
        if(this.positionsStatus[4] == false){
            togglePosition(p, 4);
            pm.moveVertically(p.circle);
            
       }else if(this.positionsStatus[5] == false){
           togglePosition(p, 5);
           pm.moveToCenter(p.circle);
       }
    }
    
    private void moveFromPosition3(Piece p) {
        if(this.positionsStatus[1] == false){
            togglePosition(p, 1);
            pm.moveVertically(p.circle);
            
       }else if(this.positionsStatus[5] == false){
           togglePosition(p, 5);
           pm.moveToCenter(p.circle);
           
       }else if(this.positionsStatus[4] == false){
           togglePosition(p, 4);
           pm.moveHorizontally(p.circle);
       }
    }
    
    
    private void moveFromPosition4(Piece p) {
        if(this.positionsStatus[2] == false){
            togglePosition(p, 2);
            pm.moveVertically(p.circle);
            
       }else if(this.positionsStatus[5] == false){
           togglePosition(p, 5);
           pm.moveToCenter(p.circle);
           
       }else if(this.positionsStatus[3] == false){
           togglePosition(p, 3);
           pm.moveHorizontally(p.circle);
       }
    }
    
    private void moveFromPosition5(Piece p) {
        if(this.positionsStatus[1] == false){
            togglePosition(p, 1);
            pm.moveToTopLeft(p.circle);
            
       }else if(this.positionsStatus[2] == false){
            togglePosition(p, 2);
            pm.moveToTopRight(p.circle);
            
       }else if(this.positionsStatus[3] == false){
            togglePosition(p, 3);
            pm.moveToBottomLeft(p.circle);
           
       }else if(this.positionsStatus[4] == false){
            togglePosition(p, 4);
            pm.moveToBottomRight(p.circle);
       }
    }
    
    private void togglePosition(Piece p, int pos){
        this.positionsStatus[p.position] = false;
        p.position = pos;
        this.positionsStatus[p.position] = true;
    }
    
}
