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
import javax.jws.WebMethod;
import javax.jws.WebService;

;
@WebService
public interface LolSOAPI {
    
        @WebMethod void startGame();
	@WebMethod String getNameOfCham();
        @WebMethod String getImage();
        @WebMethod void reset();
        @WebMethod void submitGuess(String guess);
        @WebMethod Bruger hentBruger(String user, String pass) throws Exception;
    
}
