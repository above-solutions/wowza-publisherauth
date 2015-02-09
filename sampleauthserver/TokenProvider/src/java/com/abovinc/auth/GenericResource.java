/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abovinc.auth;

import java.io.IOException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author nirbhaykundan
 */
@Path("token")
public class GenericResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }

    /**
     * Retrieves representation of an instance of com.abovinc.auth.GenericResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
       return "{\"Auth Server Version\":\"v1.0\"}";
    }
    
    @POST
    @Produces ("application/json")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String getToken(@FormParam("clientid") String id) throws IOException {
        String sharedSecret = "abcdefghijklmnop"; //must be atleast 16 alphanumberic/special chars
        try {
            String clientID = TEA.decrypt(id, sharedSecret);
            
            //GET TOKEN from TOKEN PROVIDER System using client ID
            String token = "abcdefgh";
            
            //encrypt it using TEA and sent back to wowza
            token = TEA.encrypt(token, sharedSecret); 
            
            return "{\"publisherToken\":\""  + token+ "\"}";
        }catch (Exception ex) {
            return "{\"error\":\"Invalid Client\"}";
        }
    }
}
