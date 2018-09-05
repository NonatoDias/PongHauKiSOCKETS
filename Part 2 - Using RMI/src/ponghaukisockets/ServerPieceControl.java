/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Nonato Dias
 */
public class ServerPieceControl extends UnicastRemoteObject implements PieceRemoteInterface{
    private PieceMap pieceMap;
    
    public ServerPieceControl(PieceMap pieceMap) throws RemoteException {
        super();
        this.pieceMap = pieceMap;
        System.out.println("ServerPieceControl criado!");
    }
    
    @Override
    public void movePieceControl(String pieceName) throws RemoteException {
        switch(pieceName){
            case "BLUE_A":
                pieceMap.moveBlueA();
                break;
            case "BLUE_B":
                pieceMap.moveBlueB();
                break;
            case "YELLOW_A":
                pieceMap.moveYellowA();
                break;
            case "YELLOW_B":
                pieceMap.moveYellowB();
                break;
        }
    }
    
}
