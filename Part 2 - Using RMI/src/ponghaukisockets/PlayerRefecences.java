/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nonato Dias
 */
public class PlayerRefecences {
    public List<String> allIds;
    public List<GameRemoteInterface> allIntrf;

    public PlayerRefecences() {
        allIds = new ArrayList<String>();
        allIntrf = new ArrayList<GameRemoteInterface>();
    }
    
    public void add(String id, GameRemoteInterface intrf){
        allIds.add(id);
        allIntrf.add(intrf);
    }

    public List<GameRemoteInterface> getAllGameInterface() {
        return allIntrf;
    }
    
    /**
     * 
     * @param id
     * @return 
     */
    public GameRemoteInterface getReference(String id){
        int index = allIds.indexOf(id);
        if(index != -1){
            return allIntrf.get(index);
        }
        return null;
    }
    
    public int size(){
        return allIds.size();
    }
    
}
