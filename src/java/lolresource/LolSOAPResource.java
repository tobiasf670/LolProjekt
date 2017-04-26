/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolresource;

import brugerautorisation.data.Bruger;
import java.net.MalformedURLException;
import java.net.URL;
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

/**
 * REST Web Service
 *
 * @author Magnus
 */
@Path("generic")
public class LolSOAPResource {
    
  static LolSoapImplementation soapHandlerService;
   

    @Context
    private UriInfo context;
 

    /**
     * Creates a new instance of LolSOAPResource
     */
    public LolSOAPResource() {
  
                soapHandlerService = new LolSoapImplementation();
                
               
    }

    /**
     * Retrieves representation of an instance of lolresource.LolSOAPResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getXml() {
        //TODO return proper representation object
        return " Hej Jensen";
    }

    /**
     * PUT method for updating or creating an instance of LolSOAPResource
     * @param content representation for the resource
     */
    @POST
    @Path("/{pathparam}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String putXml(@PathParam("pathparam") String input) throws Exception{
        
        String[] s = input.split(" ");
        
        
        return soapHandlerService.soapHandler.hentBruger(s[0], s[1]);
    }
}
