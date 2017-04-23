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
		@WebMethod void startGame(String username);
		@WebMethod boolean isGameDone(String username);
		@WebMethod boolean didIWin(UUID gameId, String username);
		
		@WebMethod String getChampionImgUrl(String username);
		@WebMethod String getChampionTitle(String username);    
		
		@WebMethod boolean guessChampion(String username, String guess);
		
		@WebMethod void skipChampion(String username);
		
        @WebMethod String hentBruger(String user, String pass);
}
