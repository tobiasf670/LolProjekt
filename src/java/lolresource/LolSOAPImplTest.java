/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolresource;
import brugerautorisation.data.Bruger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.*;
/**
 *
 * @author Magnus
 */

@Path("lolsoapaccess")
public class LolSOAPImplTest{
    
    
     static LolSoapImplementation soapHandlerService;
     
      @Context
    private UriInfo context;
      
         public LolSOAPImplTest() {
  
                soapHandlerService = new LolSoapImplementation();
                
               
    }
    
   @GET
   @Path("/creategame/{username}")
   @Produces(MediaType.TEXT_PLAIN)
    public UUID createNewGame(@PathParam("username") String username) {
        return soapHandlerService.soapHandler.createNewGame(username);
    }

    @GET
    @Path("/findgames")
    @Produces(MediaType.APPLICATION_JSON)
    public String findGames() {
        UUID[] uuids = soapHandlerService.soapHandler.findGames(); 
        uuids[0].toString();
        Gson g = new Gson();
        String jsonArray = g.toJson(uuids);
        JSONObject jsonObj = new JSONObject();
        
        try {
            
                   for (int i = 0; i < uuids.length; i++) {
                       jsonObj.put("game: " +i, uuids[i]);
            }
    
        } catch (Exception e) {
        }
        System.out.println("Array er : " + uuids.length + " langt");
         return jsonArray;
    }

   @GET
   @Path("/getplayers/{gameID}")
   @Produces(MediaType.APPLICATION_JSON)
    public Set<String> getUsernames(@PathParam("gameID") UUID gameId) {
        return soapHandlerService.soapHandler.getUsernames(gameId);
    }

    @GET
    @Path("/joingame/{gameID}/{username}")
    public void joinGame(@PathParam("gameID") UUID gameId, @PathParam("username")  String username) {
         soapHandlerService.soapHandler.joinGame(gameId, username);
    }

    @GET
   @Path("/startgame/{username}")
    public void startGame(@PathParam("username") String username) {
        soapHandlerService.soapHandler.startGame(username);
    }

   @GET
   @Path("/isgamedone/{username}")
    public boolean isGameDone(@PathParam("username") String username) {
         return soapHandlerService.soapHandler.isGameDone(username);
    }

    @GET
    @Path("didiwin/{gameID}/{username}")
    public boolean didIWin(@PathParam("gameID") UUID gameId, @PathParam("username")  String username) {
         return soapHandlerService.soapHandler.didIWin(gameId, username);
    }

   @GET
   @Path("/getchamppic/{username}")
    public String getChampionImgUrl(@PathParam("username") String username) {
         return soapHandlerService.soapHandler.getChampionImgUrl(username);
    }

    
   @GET
   @Path("/getchamp/{username}")
    public String getChampionTitle(@PathParam("username") String username) {
         return soapHandlerService.soapHandler.getChampionTitle(username);
    }

   @GET
   @Path("/guesschamp/{username}/{guess}")
    public boolean guessChampion(@PathParam("username") String user,@PathParam("guess") String passedGuess) {
         return soapHandlerService.soapHandler.guessChampion(user, passedGuess);
    }

   @POST
   @Path("/skip")
   @Consumes(MediaType.TEXT_PLAIN)
    public void skipChampion(String username) {
          soapHandlerService.soapHandler.skipChampion(username);
    }
    
    @POST
    @Path("/hentbruger/{username}/{password}")
    @Produces(MediaType.TEXT_PLAIN)
    public String hentBruger(@PathParam("username") String username, @PathParam("password") String pass) {
    String requestedUser = soapHandlerService.soapHandler.hentBruger(username, pass);
       if (requestedUser != null){
           
             return requestedUser;
        }
        else{
            
            return "Wrong User Parameters";        }
        
    }

    
    
}
