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


public class RTMPAuthMysql extends AuthenticateUsernamePasswordProviderBase
{
	public String getPassword(String username)
	{
			
		String pwd = null;
		
		WMSLoggerFactory.getLogger(null).info("Authenticate getPassword username: " + username);
		
		Connection conn = null;
		try 
		{
			conn = DriverManager.getConnection("jdbc:mysql://localhost/wowza?user=root&password=mypassword");

			Statement stmt = null;
			ResultSet rs = null;

			try 
			{
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT pwd FROM users where username = '"+username+"'");
				while (rs.next())
				{
					pwd = rs.getString("pwd");
				}

			} 
			catch (SQLException sqlEx) 
			{
				WMSLoggerFactory.getLogger(null).error("sqlexecuteException: " + sqlEx.toString());
			} 
			finally 
			{
				if (rs != null) 
				{
					try 
					{
						rs.close();
					} 
					catch (SQLException sqlEx) 
					{

						rs = null;
					}
				}

				if (stmt != null) 
				{
					try 
					{
						stmt.close();
					} 
					catch (SQLException sqlEx) 
					{
						stmt = null;
					}
				}
			}

			conn.close();
		} 
		catch (SQLException ex) 
		{
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		
		return pwd;
	}
	
	
	
	public boolean userExists(String username)
	{
		System.out.println(" User Exists "+username);
		// return true is user exists
		return false;
	}
}