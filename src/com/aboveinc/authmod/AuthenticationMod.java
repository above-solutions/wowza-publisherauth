package com.aboveinc.authmod;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;

import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.authentication.AuthenticateUsernamePasswordProviderBase;
import com.wowza.wms.security.encryption.TEA;
 
 

public class AuthenticationMod extends AuthenticateUsernamePasswordProviderBase {

	@Override
  public String getPassword(String username) { 
		WMSProperties localWMSProperties = this.client.getAppInstance().getProperties();
		 
		String sharedSecret = localWMSProperties.getPropertyStr("secureTokenSharedSecret");
		String serverTokenValidatorURL = localWMSProperties.getPropertyStr("serverTokenValidatorURL");
		
		URL url;
    try {
	    url = new URL(serverTokenValidatorURL);
   
			URLConnection connection;
	    connection = url.openConnection();
	    
			connection.setDoOutput(true);
	
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			 
			username = TEA.encrypt( username, sharedSecret );
			out.write("clientid=" + username);
			out.close(); 
	    
     
	    ObjectMapper mapper = new ObjectMapper();
	    JsonNode node = mapper.readTree(connection.getInputStream()); 
	    node = node.get("publisherToken") ;
	    String token = node.asText();
	 
	    return TEA.decrypt(token, sharedSecret); 
    } catch (JsonParseException e) { 
	    e.printStackTrace();
    } catch (IOException e) { 
	    e.printStackTrace();
    }  
    return null;
  } 
	@Override
  public boolean userExists(String username) { 
	  return false;
  } 
}