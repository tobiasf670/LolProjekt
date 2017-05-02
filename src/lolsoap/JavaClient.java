/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolsoap;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 *
 * @author Tobias
 */
public class JavaClient {
static LolSOAPI spil;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, Exception {
        // TODO code application logic 
        
        System.out.println("Velkommen til LOLQUIZ");
	  Scanner scan = new Scanner(System.in);
	  System.out.println("Indtast Brugernavn");
	  String user = scan.next();
	  System.out.println("Indtast password!");
	  String pass = scan.next();
	  
	  boolean legitUser = false;
	  
	  //URL url = new URL("http://ec2-35-165-42-120.us-west-2.compute.amazonaws.com:9901/galgeSOAP?wsdl");
         URL url = new URL("http://localhost:9901/LolSOAP?wsdl");
	  
	  QName qname = new QName("http://lolsoap/", "GameHandlerService");
	  Service service = Service.create(url, qname);
		LolSOAPI ba = service.getPort(LolSOAPI.class);
                String bruger = null;
                
                
             try {
		  bruger = ba.hentBruger(user,  pass);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		
	}     
               
              
		if (bruger != null){           
			legitUser = true;
		}
	
                
	  
	  if(legitUser){
		  System.out.println("Du er nu logget ind!");
		  
	  }
	  else{
		  System.out.println("Login fejlet");
		 legitUser = false;
	  }
         boolean igen = true;
	  if (legitUser){
              
          
	  while (igen){
	  try {
		 igen = spilSpillet(ba, bruger);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
          }
	  
	  }
    }

    private static boolean spilSpillet(LolSOAPI face,String p) {
    spil = face;
     UUID id = null;
    
//    System.out.println("Vil du starte et ny Spil ?");
//    Scanner scan = new Scanner(System.in);
//    System.out.println("Y = Ja , N = nej");
//	  String svar = scan.next().toLowerCase(); 
//          if(svar.equals("y")){
//               id = spil.createNewGame(p);
//          }
//          else{
//              return false;
//          }
    
      //System. out.println("Nyt Spil Startet");
      
     // Valg variablen som styrer spillet.
     int valg = 0;
     
     while(true){
     Scanner scan = new Scanner(System.in);
      UUID[] setOfGames ;
      System.out.println("**********-MENU-*****************");
       System.out.println("*********************************");
      System.out.println("Klik 1 ......... Find et spil");
      System.out.println("Klik 2 ......... Start spil");
      System.out.println("Klik 3 ......... Lav et spil");
      System.out.println("Klik 4 ......... Exit");
        System.out.println("*********************************");
        System.out.println("*********************************");
        System.out.println();
        System.out.print("Dit valg : ");
     
      //
      try {
           valg = scan.nextInt();
      } catch (InputMismatchException e) {
              System.out.println("");
              System.out.println("\u001B[31m" + "Det er kun muligt at skrive til fra 1-4" + "\u001B[0m");
      }
        
      switch(valg){
          case 1: 
              
            setOfGames = spil.findGames();
            if(setOfGames.length == 0){
                
                System.out.println("\u001B[31m"+"Ikke nogle aktive spil fundet"+"\u001B[0m");
               
                break;
            }
            System.out.format("%-15s%-15s\n","Game nr","Spiller");
           
            for(int i = 0; setOfGames.length>i; i++){
                int gameNr = i+1;
               // System.out.println("          Game nr.          Spiller");
               // System.out.println("          "+gameNr+"          "+spil.getUsernames(setOfGames[i]));
                //System.out.println("Game nr."+gameNr+" Med spiller : "+ spil.getUsernames(setOfGames[i]));
               // System.out.format("%10s%10s%10s%10s\n", "Game nr", gameNr, "Spiller",spil.getUsernames(setOfGames[i]));
             System.out.format("%-15s%-15s\n",gameNr,spil.getUsernames(setOfGames[i]));
            }
            
            System.out.println("Indtast nr, på det game du vil joine ( gå tilbage klik -1)");
            System.out.print("Indtast nr : ");
            boolean getGame = true;
            do{
            int gameValg = scan.nextInt();
           if(gameValg == -1){
               System.out.println("\u001B[31m"+"Du valgte at gå tilbage"+"\u001B[0m");
               break;
           }
            if(gameValg <= setOfGames.length){
                spil.joinGame(setOfGames[gameValg-1], p);
                startGame(p, setOfGames[gameValg-1], scan);
                break;
                
            }
            } while (getGame);
            
            break;
            
          case 2:
              if(id == null){
                  System.out.println("\u001B[31m"+"DU SKAL FØRST LAVE ET SPIL"+"\u001B[0m");
                  break;
              }
              startGame(p, id, scan);
    break;
      
      
          case 3 :
             id = spil.createNewGame(p);
             System.out.println("\u001B[32m"+"Der er nu oprettet et spil, spil det fra menu ved at klikke 2"+"\u001B[0m");
             
      break;
          case 4 :
              
              return false;
              //break;
              //return false;
              
          default : 
              System.out.println("\u001B[31m"+"Dit valgte input er ikke i menuen"+"\u001B[0m");
                  
      }
      
      
      
     
      
  
    
           
   //return true;
    }
    
    }
    
    
    public static  String startGame(String p, UUID id, Scanner scan){
        spil.startGame(p);  
             
             while(!spil.isGameDone(p)){
         System.out.println(spil.isGameDone(p));
        System.out.println("Hvem har titlen : " + spil.getChampionTitle(p));
   
      
      
      boolean tryAgain= true;
          do {      
             
              //String guess = scan.next();
                String guess = "skip";
              if(guess.equals("done")){
                  return "";
              }
              if(guess.equals("skip")){
                  spil.skipChampion(p);
                  tryAgain = false;
                  break;
              //    break;
              }
              
              if( spil.guessChampion(p, guess)){
                  
                  tryAgain = false;
    }
              else  {
                
                  System.out.println("Forkert, prøv igen.(Hvis du ikke kan gætte det, skriv skip");
                   System.out.println("Hvem har titlen : " + spil.getChampionTitle(p));
                  tryAgain = true;
                  }
              
              
              
          } while (tryAgain);
          
          
    }
             System.out.println("FØR IF og ID ER"+id);
        if(spil.isGameDone(p)){
            System.out.println("Efter IF og ID ER"+id);
            System.out.println("Vinderen er : " + spil.getWinner(id));
        }
        return spil.getWinner(id);
}
}
