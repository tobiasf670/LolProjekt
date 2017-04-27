/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolsoap;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import lolsoap.LolSOAPI;

/**
 *
 * @author Magnus
 */
public class LolSoapImplementation {
    
    private URL url;
    private QName qname;
    private Service service;
    LolSOAPI soapHandler;
    
    
    public LolSoapImplementation(){
        
          try {
             url = new URL("http://ec2-35-165-42-120.us-west-2.compute.amazonaws.com:9901/LolSOAP?wsdl");
        } catch (MalformedURLException e) {
        }

	  
	 qname = new QName("http://lolsoap/", "GameHandlerService");
	   service = Service.create(url, qname);
		 soapHandler = service.getPort(LolSOAPI.class);
        
    }
    
        
    
}
