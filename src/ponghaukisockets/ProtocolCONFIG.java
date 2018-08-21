/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ponghaukisockets;

/**
 *
 * @author Nonato
 */
public interface ProtocolCONFIG {
   /**
    * Status
    */
    public static String RESULT_OK = "200";
    public static String CONNECT = "CONNECT";
    public static String CONNECTED = "CONNECTED";
    
    /**
    * Separador entre code e data
    */
    public static String separator_ = "==>";
    public static String and_ = "&amp;";
    
    
    public static String getActionFromMessage(String request){
        return request.substring(0, request.indexOf(separator_));
    }
    
    public static String getDataFromMessage(String request){
        return request.substring(request.indexOf(separator_)+separator_.length(), request.length());
    }  
    
    
    public static String prepareRequest(String code, String data){
        return code+separator_+data;
    }
    
    public static String prepareResponse(String code, String data){
        return code+separator_+data;
    }
}
