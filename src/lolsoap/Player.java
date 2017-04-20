/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolsoap;

import java.util.Set;
import java.util.UUID;

import brugerautorisation.data.Bruger;
import java.util.HashSet;

/**
 *
 * @author Mads Hornbeck
 */
public class Player {
    
	private Bruger bruger;
    private Set<UUID> games;
    private UUID currentGameId;
    String luder = "HAHA GÅR ALDRIG IND I PLAYER";
    public Player(){
        
    }
    
    public Player(Bruger bruger){
        this.games = new HashSet<UUID>();
    	this.bruger = bruger;
        
           luder = "HAHAH DEN ER NULL";
        
        this.currentGameId = null;
    }
    
    public void joinGame(UUID gameId) {
    	this.currentGameId = gameId;
    	games.add(gameId);
    }
    
    public UUID getCurrentGame() {
    	return currentGameId;
    }

	public Set<UUID> getGames() {
		return games;
	}

        public String getNavn(){
            return this.bruger.brugernavn;
        }
        
        public String getBruger(){
            return luder;
        }
    
}
