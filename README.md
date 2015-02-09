# DESCRIPTION #

This Authentication module provides 3rd party authentication for RTMP publishers

### Architecture ###
![alt text](https://cloud.githubusercontent.com/assets/2519781/6103029/4856b564-b065-11e4-9abc-30879c380345.jpg "Architecture")
### How do I get set up? ###
* Prerequisites : Wowza Streaming Engine 4
* Deployment instructions

#### Step : 1  Open `[wowza-install-dir]/conf/[application]/Application.xml` in a text editor and add the following <Module> definition as the last entry in the <Modules> list:

```
<Module>
	<Name>ModuleRTMPAuthenticate</Name>
	<Description>ModuleRTMPAuthenticate</Description>
	<Class>com.wowza.wms.security.ModuleRTMPAuthenticate</Class>
</Module>
```

and comment following module

```
<!-- 	<Module>
	 <Name>ModuleCoreSecurity</Name>
	 <Description>Core Security Module for Applications</Description>
	 <Class>com.wowza.wms.security.ModuleCoreSecurity</Class>
</Module> -->
```

####  Step 2 :  Open `[wowza-install-dir]/conf/[application]/Application.xml` in a text editor and add the following <Property> definition as the last entries in the <Properties> list. Make sure this is the last Properties section of the Application.xml file

```
<Property>
	<Name>usernamePasswordProviderClass</Name>
	<Value>com.aboveinc.authmod.AuthenticationMod</Value>
</Property> 
<!-- secureTokenSharedSecret  must be atleast 16 alphanumberic/special chars -->
<Property>
	<Name>secureTokenSharedSecret</Name>
	<Value><![CDATA[sharedsecret]]></Value>
</Property> 
<Property>
	<Name>serverTokenValidatorURL</Name>
	<Value>http://<authentication server>/TokenProvider/authentication/token</Value>
</Property> 
```

#### Step 3 : Copy `aboveinc-authenticationmod.jar` from dist folder and paste it inside  `[wowza-install-dir]/lib/` directory

#### Step 4 : Restart Wowza Server
