/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolsoap;

import brugerautorisation.data.Bruger;
import brugerautorisation.transport.rmi.Brugeradmin;
import java.awt.image.BufferedImage;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import javax.jws.WebService;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Tobias
 */
@WebService(endpointInterface = "lolsoap.LolSOAPI")
public class LolLogikImpl implements LolSOAPI {

   
    HashMap<Integer, GameInstance> currentGames;

    public LolLogikImpl() {
        currentGames = new HashMap<Integer, GameInstance>();
         
    }

    @Override
    public void startGame(int gameID) {
        currentGames.get(gameID).startGame();
        
    }

    @Override
    public String getNameOfCham(int gameID) {
        if(currentGames.get(gameID) != null){
            if (currentGames.get(gameID).nameOfChamp == null) {
            return "No Name";
            } else {
            return currentGames.get(gameID).nameOfChamp;
            }
        }
        return "No Name";
    }

    @Override
    public String[] renewData(int gameID) {
        
        
        if(currentGames.get(gameID) != null){
       return this.currentGames.get(gameID).renewGameData();
        }
        else{
            return new String[]{"no data", "no data"};
            
        }

    }

    @Override
    public void reset(int gameID) {
        if(currentGames.get(gameID) != null){
        this.currentGames.get(gameID).resetGame();
        }
    }

    @Override
    public Bruger hentBruger(String user, String pass) throws Exception {
        Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        Bruger b = ba.hentBruger(user, pass);
        return b;

    }

    @Override
    public boolean submitGuess(String guess, Player p) {
        
        boolean gameOver = currentGames.get(p.gameID).guessChamp(p, guess);
        if (gameOver){
            
            currentGames.remove(p.gameID);
        }
        return gameOver;
       
    }



    @Override
    public String joinGame(int gameID, Player p) {
        if(currentGames.get(gameID) != null && currentGames.get(gameID).joinable){
        currentGames.get(gameID).addPlayer(p);
        return "You are now in game number: " + gameID;
        }
        else{
            
            return "No such game id";
        }
       
    }

    @Override
    public GameInstance[] getGames() {
        GameInstance[] games = new GameInstance[currentGames.size()];
        Integer[] keys = (Integer[]) currentGames.keySet().toArray();
        
        
        for (int i = 0; i < currentGames.size(); i++){
            
            
            
            games[i] = currentGames.get(keys[i]);
                    
        }
        return games;
        
    }

    @Override
    public int createGame(Player p) {
        p.ID = new Random().nextInt();
        GameInstance newGame = new GameInstance();
        newGame.gameID = currentGames.size()+1;
        newGame.players.add(p);
        currentGames.put(newGame.gameID, newGame);
        return p.ID;
    }

    @Override
    public String[] getGameData(int gameID) {
        
        return currentGames.get(gameID).retrieveCurrentGameData();
        
    }

}
