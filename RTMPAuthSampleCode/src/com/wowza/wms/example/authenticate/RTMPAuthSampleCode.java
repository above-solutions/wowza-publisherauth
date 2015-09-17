/*
 * @author Altanai
 * 17 Sep 2015
 */
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

public class RTMPAuthSampleCode extends AuthenticateUsernamePasswordProviderBase
{
	
	public String getPassword(String username)
	{
		
		if(username=="test"){
			
			return "1234";
		}
		
		else{
		
			return "null";
		}
	}
	
	public boolean userExists(String username)
	{
		return false;
	}
}