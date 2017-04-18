package lolsoap;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 *
 * @author Magnus
 */
public class LolClientTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LolSOAPI ba = null;
        
        try {
             URL url = new URL("http://localhost:9901/LolSOAP?wsdl");
             QName qname = new QName("http://lolsoap/", "LolLogikImplService");
	     Service service = Service.create(url, qname);
		 ba = service.getPort(LolSOAPI.class);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(ba != null){
           String [] data = ba.getGameData(1);
           System.out.println(data[0]);
           
            System.out.println(data[1]);
            
        }
        else {
            System.out.println("Could not connect to soap api");
            
                 
        }
         
        // URL url = new URL("http://localhost:9901/galgeSOAP?wsdl");
	  
	
    }
    
}
