/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolsoap;


import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Tobias
 */
public class Champion {
    
   
   private String name;
   private String title;
   private String url;
   
    public Champion(JSONObject obj, String url) throws JSONException{
        
        name = obj.getString("key").replaceAll(" ", "");
        title = obj.getString("title");
        this.url = url;
    }
    
    public String getTitle(){
        return title;
    }
    
    public String getUrl(){
        return url + name + ".png";
    }
    
    public boolean guessName(String userInput){
        return userInput.equalsIgnoreCase(name);
    }
    
}
