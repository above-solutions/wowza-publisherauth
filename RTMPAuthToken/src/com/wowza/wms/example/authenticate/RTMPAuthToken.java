package com.wowza.wms.example.authenticate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.authentication.*;
import com.wowza.wms.logging.WMSLoggerFactory; 
import com.wowza.wms.security.encryption.TEA;

import java.io.IOException;
import java.io.OutputStreamWriter;

import java.net.URL;
import java.net.URLConnection;

import java.sql.*;


public class RTMPAuthToken extends AuthenticateUsernamePasswordProviderBase
{
	
	
	public String getPassword(String username)
	{
		
		WMSLoggerFactory.getLogger(null).info("Authenticate getPassword username: " + username);
		
		WMSProperties localWMSProperties = this.client.getAppInstance().getProperties();
		 
		String sharedSecret = localWMSProperties.getPropertyStr("secureTokenSharedSecret");
		String serverTokenValidatorURL = localWMSProperties.getPropertyStr("serverTokenValidatorURL");
		
		
		System.out.println(" Shared Secret -"+ sharedSecret);
		System.out.println(" server Token Validator URL -"+ serverTokenValidatorURL);
		
		
		URL url;
		
		try {
			url = new URL(serverTokenValidatorURL);
			
			URLConnection connection;
	        connection = url.openConnection();
			connection.setDoOutput(true);
	
			username = TEA.encrypt( username, sharedSecret );
			System.out.println(" username -"+ username);
			
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			out.write("clientid=" + username);
			out.close(); 
			
			System.out.println(connection.getInputStream());
			
			
		    ObjectMapper mapper = new ObjectMapper();
		    JsonNode node = mapper.readTree(connection.getInputStream()); 
		    node = node.get("publisherToken") ;
		    String token = node.asText();
		 
		    System.out.println("Token -"+token);
		    
		    String token2 =TEA.decrypt(token, sharedSecret);
		    
		    System.out.println("Token Decrypted -"+ token2);
		    
		    //return TEA.decrypt(token, sharedSecret); 
    
		    return token2.toString();
		    
		}catch (IOException e) { 
		    
	    	e.printStackTrace();
	    }  
		
		return "abcdefgh";
	}
	
	public boolean userExists(String username)
	{
		System.out.println(" User Exists "+username);
		// return true is user exists
		return false;
	}
}