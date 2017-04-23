/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolsoap;

import brugerautorisation.data.Bruger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 *
 * @author Tobias
 */
public class NewMain {
static LolSOAPI spil;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, Exception {
        // TODO code application logic 
        
        System.out.println("Velkommen til LOLQUIZ");
	  //Scanner scan = new Scanner(System.in);
	  System.out.println("Indtast Brugernavn");
	  String user = "s153086";
	  //System.out.println("Indtast password!");
	  String pass = "nusnus";
	  
	  boolean legitUser = false;
	  
	  //URL url = new URL("http://ec2-35-165-42-120.us-west-2.compute.amazonaws.com:9901/galgeSOAP?wsdl");
         URL url = new URL("http://localhost:9901/LolSOAP?wsdl");
	  
	  QName qname = new QName("http://lolsoap/", "GameHandlerService");
	  Service service = Service.create(url, qname);
		LolSOAPI ba = service.getPort(LolSOAPI.class);
                
                
               String bruger = ba.hentBruger(user,  pass);
              
		if (bruger != null){           
			legitUser = true;
		}
	
                
	  
	  if(legitUser){
		  System.out.println("Du er nu logget ind!");
		  
	  }
	  else{
		  System.out.println("Login fejlet");
		  
	  }
         boolean igen = true;
	  
	  while (igen){
	  try {
		 igen = spilSpillet(ba, bruger);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
          
	  
	  }
    }

    private static boolean spilSpillet(LolSOAPI face,String p) {
    spil = face;
     UUID id = null;
    
    System.out.println("Vil du starte et ny Spil ?");
    Scanner scan = new Scanner(System.in);
    System.out.println("Y = Ja , N = nej");
	  String svar = scan.next().toLowerCase(); 
          if(svar.equals("y")){
               id = spil.createNewGame(p);
               System.out.println("Game ID for dit nye spil er " + id);
                       
          }
          else{
              return false;
          }
    
      System. out.println("Nyt Spil Startet");
      spil.hentBruger("s153391", "kodetyv");
      
      spil.joinGame(id, "s153391");
      spil.startGame(p);
      Set<Player> players = spil.getPlayers(id);
      if(players.size() > 0){
          
        Player tempP = players.iterator().next();
          System.out.println(players.size());
          if (tempP != null){
              
              System.out.println("han er til at finde");
              System.out.println(tempP.getCurrentGame());
          }
          
      }
     
      while (!spil.didIWin(id, p)){
      System.out.println("Hvem har titlen : " + spil.getChampionTitle(p));
   
      
      boolean tryAgain= true;
          do {      
             
              String guess = scan.next();
              if(guess.equals("skip")){
                  spil.skipChampion(p);
                  tryAgain = false;
                  break;
              }
              if( spil.guessChampion(p, guess)){
                  
                  tryAgain = false;
    }
              else {
                
                  System.out.println("Forkert, prøv igen.(Hvis du ikke kan gætte det, skriv skip");
                   System.out.println("Hvem har titlen : " + spil.getChampionTitle(p));
                  tryAgain = true;
                  }
              
              
              
          } while (tryAgain);
    
      
      }
  // Champion kurt = spil.getCurrentChampion(gg, p);
   
   //System.out.println(kurt.getTitle());
           
   return false;
    }
}
