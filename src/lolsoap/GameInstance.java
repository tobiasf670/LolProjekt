/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolsoap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *	This class handles the individual games and their state. 
 *	This consists of a gameState object per player that contains a list of champions.
 * @author Mads Hornbeck
 */
public class GameInstance {
    
	
	private UUID id;
	private boolean gameStarted = false;
	private long startTime;
	private Player winner = null;

	private Set<Player> players;
	private HashMap<Player, GameState> playerGameStates = new HashMap<>();

	public GameInstance() {
		// This creates a UUID which is a semi-random identifier of 128 bits;
		// read more here : https://en.wikipedia.org/wiki/Universally_unique_identifier.
        id = UUID.randomUUID();
    }
	
	public UUID getGameId(){
		return id;
	}
    
    public void startGame(){
    	if (!gameStarted) {
    		gameStarted = true;
    		startTime = System.currentTimeMillis();
    	}
    }
    
    public void addPlayer(Player p){
        this.players.add(p);
		p.joinGame(id);
        // TODO add player champion array pair to hashmap
    }
    
    public boolean guessChamp(Player p, String guess){
    	
    	GameState gameState = playerGameStates.get(p);
    	if (gameState.guessChampion(guess)) {
    		// TODO we need to figure out when the player knows the game is over.
    		if (gameState.doneGuessing() && winner == null) {
    			winner = p;
    		}
    		return true;
    	}
        return false;
    }

	public boolean isDone() {
		return winner != null;
	}

	public boolean hasWon(Player p) {
		return p.equals(winner);
	}
	
	public Set<Player> getPlayers(){
		return players;
	}
	
	public void skip(Player p) {
		GameState gameState = playerGameStates.get(p);
		gameState.skip();
	}

	public Champion getCurrentChampion(Player p) {
		GameState gameState = playerGameStates.get(p);
		return gameState.getCurrentChampion();
	}
 
	public long getTimeTaken(Player p) {
		GameState gameState = playerGameStates.get(p);
		return gameState.getEndTime() - startTime;
	}
    
}
  
