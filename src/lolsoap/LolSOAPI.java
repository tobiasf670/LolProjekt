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
    
		@WebMethod UUID createNewGame(String username);
		@WebMethod Set<UUID> findGames();
		@WebMethod Set<Player> getPlayers(UUID gameId);
		@WebMethod void joinGame(UUID gameId, String username);
		@WebMethod void startGame(UUID gameId);
		@WebMethod boolean isGameDone(UUID gameId);
		@WebMethod boolean didIWin(UUID gameId, String username);
		
		@WebMethod String getChampionImgUrl(UUID gameId, String username);
		@WebMethod String getChampionTitle(UUID gameId, String username);    
		
		@WebMethod boolean guessChampion(UUID gameId, String username, String guess);
		
		@WebMethod void skipChampion(UUID gameId, String username);
		
        @WebMethod String hentBruger(String user, String pass) throws Exception;
}
