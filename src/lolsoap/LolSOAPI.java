/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolsoap;

/**
 *
 * @author Tobias and Mads Hornbeck
 */

import brugerautorisation.data.Bruger;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import javax.jws.WebMethod;
import javax.jws.WebService;


@WebService
public interface LolSOAPI {
    
		@WebMethod UUID createNewGame(Player p);
		@WebMethod Set<UUID> findGames();
		@WebMethod Set<Player> getPlayers(UUID gameId);
		@WebMethod void joinGame(UUID gameId, Player p);
		@WebMethod void startGame(UUID gameId);
		@WebMethod boolean isGameDone(UUID gameId);
		@WebMethod boolean didIWin(UUID gameId, Player p);
		
		@WebMethod Champion getCurrentChampion(UUID gameId, Player p);
		
		@WebMethod boolean guessChampion(UUID gameId, Player p, String guess);
		
		@WebMethod void skipChampion(UUID gameId, Player p);
		
        @WebMethod Player hentBruger(String user, String pass) throws Exception;
        
         @WebMethod Bruger hentBruger1(String user, String pass) throws Exception;
    
}
