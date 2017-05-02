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
    
	private UUID id;
	private Bruger bruger;
    private UUID currentGameId;
    private HashSet<UUID> games;
    
    public Player(Bruger bruger){
        this.id = UUID.randomUUID();
        this.games = new HashSet<UUID>();
    	this.bruger = bruger;
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

    public String getBrugernavn(){
        return this.bruger.brugernavn;
    }
    
    public void endGame() {
    	this.currentGameId = null;
    }
}
