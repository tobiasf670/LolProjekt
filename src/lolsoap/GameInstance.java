/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolsoap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Magnus
 */
public class GameInstance {
    
    ArrayList<Player> players;
    int gameID;
    String nameOfChamp = null;
    String lolVersion = null;
    String url = null;
    boolean joinable = true;

    public GameInstance() {
        
        players = new ArrayList<Player>();
        
        
    }
    
    public void startGame(){
        this.joinable = false;
        
        
        
    }
    
    public void addPlayer(Player p){
        
        this.players.add(p);
        
    }
    
    public boolean guessChamp(Player p, String guess){
        if(guess.equals(nameOfChamp)){
            for (int i = 0; i < players.size(); i++){
                if (players.get(i).ID == p.ID){
                    players.get(i).hasWon = true;
                    return true;
                }
            }
            
        }
        return false;
    }
    
    public void resetGame(){
        
        this.lolVersion = null;
        this.nameOfChamp = null;
        this.url = null;
    }
    
    private ArrayList<Champion> makeChampionArray(){
        
        
        
        
        JSONArray jsonArray = null ;
        Client client = ClientBuilder.newClient();
        Response res = client.target("https://global.api.riotgames.com/api/lol/static-data/EUW"
                + "/v1.2/champion?champData=info&api_key=RGAPI-0120396b-3f2b-4cb3-bbc9-4f27240f6d45")
                .request(MediaType.APPLICATION_JSON).get();

        String svar = res.readEntity(String.class);

        try {

            JSONObject json = new JSONObject(svar);
            //JSONArray k = json.getJSONArray("data");

            JSONObject songs = json.getJSONObject("data");
            Iterator x = songs.keys();
            jsonArray = new JSONArray();

            while (x.hasNext()) {
                String key = (String) x.next();
                jsonArray.put(songs.get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        Response version = client.target("https://global.api.riotgames.com/api/lol/static-data/EUW"
                + "/v1.2/versions?api_key=RGAPI-0120396b-3f2b-4cb3-bbc9-4f27240f6d45")
                .request(MediaType.APPLICATION_JSON).get();

        String versionNr = version.readEntity(String.class);

        try {
            JSONArray versionArray = new JSONArray(versionNr);
            lolVersion = versionArray.get(0).toString();
            url = "http://ddragon.leagueoflegends.com/cdn/" + lolVersion + "/img/champion/";
          
        } catch (Exception e) {
            e.printStackTrace();

        }
        
        
        ArrayList<Champion> newArray = new ArrayList<> ();
        
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                newArray.add(new Champion(jsonArray.getJSONObject(i),url));
            } catch (JSONException ex) {
                Logger.getLogger(GameInstance.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return newArray;
    }
  
    
}
  
