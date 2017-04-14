/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolsoap;

import java.util.Iterator;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import static javax.ws.rs.client.Entity.json;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.Endpoint;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Tobias
 */
public class LolSOAP {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
      LolLogikImpl k = new LolLogikImpl();
		Endpoint.publish("http://[::]:9901/LolSOAP", k);
		
		System.out.println("Kontotjeneste registreret.");
}
}