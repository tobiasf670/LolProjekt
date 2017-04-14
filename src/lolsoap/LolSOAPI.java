/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolsoap;

/**
 *
 * @author Tobias
 */

import brugerautorisation.data.Bruger;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.jws.WebMethod;
import javax.jws.WebService;

;
@WebService
public interface LolSOAPI {
    
        @WebMethod void startGame(int gameID);
	@WebMethod String getNameOfCham(int gameID);
        @WebMethod String[] getGameData(int gameID);
        @WebMethod void reset(int gameID);
        @WebMethod String joinGame(int gameID, Player p);
        @WebMethod boolean submitGuess(String guess, Player p);
        @WebMethod GameInstance[] getGames();
        @WebMethod Bruger hentBruger(String user, String pass) throws Exception;
    
}
