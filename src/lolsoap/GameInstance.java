/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolsoap;

import java.util.ArrayList;
import java.util.Iterator;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
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
    
    public String[] retrieveCurrentGameData(){
        String [] data = new String[3];
        if(nameOfChamp == null || lolVersion == null || url == null){
            renewGameData();
        }
        else{
       
        data[0] = nameOfChamp;
        data[1] = lolVersion;
        data[2] = url;
        }
        
        
        return data;
                
        
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
  
    public String[]  renewGameData() {
        String[] data = new String[2];
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
            JSONArray jsonArray = new JSONArray();

            while (x.hasNext()) {
                String key = (String) x.next();
                jsonArray.put(songs.get(key));
            }

            int randomNum = 0 + (int) (Math.random() * 133);
            System.out.println(jsonArray.getJSONObject(randomNum).getString("name"));
            nameOfChamp = jsonArray.getJSONObject(randomNum).getString("name");
            data[0] = nameOfChamp;

        } catch (Exception e) {
            e.printStackTrace();
        }

        Response version = client.target("https://global.api.riotgames.com/api/lol/static-data/EUW"
                + "/v1.2/versions?api_key=RGAPI-0120396b-3f2b-4cb3-bbc9-4f27240f6d45")
                .request(MediaType.APPLICATION_JSON).get();

        String versionNr = version.readEntity(String.class);

        try {

            JSONArray versionArray = new JSONArray(versionNr);

            System.out.println(versionArray.get(0));
            lolVersion = versionArray.get(0).toString();

            url = "http://ddragon.leagueoflegends.com/cdn/" + lolVersion + "/img/champion/" + nameOfChamp + ".png";
            data[1] = url;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return data;

   

    }
 
    
}
  
