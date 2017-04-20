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
                
                
                //Player bruger = ba.hentBruger(user,  pass);
               
                Player b = new Player(ba.hentBruger1(user, pass));
                 System.out.println(b.getBruger());
		if (b != null){           
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
		 igen = spilSpillet(ba, b);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
          
	  
	  }
    }

    private static boolean spilSpillet(LolSOAPI face,Player p) {
    spil = face;
    
    System.out.println(p.getBruger());
    spil.createNewGame(p);
    
  // System.out.println( spil.createNewGame(p));
    
    //List<UUID> list = new ArrayList<UUID>(set);
    System. out.println("Nyt Spil Startet");
      
   //spil.startGame(gg);
   
  // Champion kurt = spil.getCurrentChampion(gg, p);
   
   //System.out.println(kurt.getTitle());
           
   return false;
    }
}
