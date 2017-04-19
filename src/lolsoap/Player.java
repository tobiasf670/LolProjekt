/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolsoap;

import java.util.Set;
import java.util.UUID;

import brugerautorisation.data.Bruger;

/**
 *
 * @author Mads Hornbeck
 */
public class Player {
    
	private Bruger bruger;
    private Set<UUID> games;
    private UUID currentGameId;
    
    public Player(Bruger bruger){
    	this.bruger = bruger;
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

    
}
