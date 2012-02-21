package com.openmeap.thinclient;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.openmeap.protocol.ApplicationManagementService;
import com.openmeap.protocol.WebServiceException;
import com.openmeap.protocol.dto.Application;
import com.openmeap.protocol.dto.ApplicationInstallation;
import com.openmeap.protocol.dto.ConnectionOpenRequest;
import com.openmeap.protocol.dto.ConnectionOpenResponse;
import com.openmeap.protocol.dto.HashAlgorithm;
import com.openmeap.protocol.dto.SLIC;
import com.openmeap.util.HttpRequestExecuterFactory;
import com.openmeap.util.MockHttpRequestExecuter;
import com.openmeap.util.Utils;

public class AppMgmtClientTest {

	@Test public void testRESTOpenConnection() throws Exception {
		AppMgmtClientFactory.setDefaultType(RESTAppMgmtClient.class);
		String[] templates = {
			"xml/connectionResponse-rest-update.xml",
			"xml/connectionResponse-rest-noupdate.xml",
			"xml/connectionResponse.404.xml"
		};
		this.testOpenConnection(templates);
	}
	
	public void testOpenConnection(String[] templates) throws Exception {
		
		HttpRequestExecuterFactory.setDefaultType(MockHttpRequestExecuter.class);
		ApplicationManagementService client = AppMgmtClientFactory.newDefault("/nowhere/");
		
		ConnectionOpenRequest request = new ConnectionOpenRequest();
		request.setApplication(new Application());
		request.getApplication().setInstallation(new ApplicationInstallation());
		request.setSlic(new SLIC());
		
		// setup the response xml that we'll spoof as though it's from the server
		Map<String,String> parms = new HashMap<String,String>();
		parms.put("AUTH_TOKEN", "authToken");
		parms.put("UPDATE_TYPE", "required");
		parms.put("UPDATE_URL", "file://none");
		parms.put("STORAGE_NEEDS", String.valueOf(15));
		parms.put("INSTALL_NEEDS", String.valueOf(15));
		parms.put("HASH", "asdf");
		parms.put("HASH_ALG", "MD5");
		parms.put("VERSION_ID", "versionId");
		MockHttpRequestExecuter.setResponseText( Utils.replaceFields(parms, 
				Utils.readInputStream(AppMgmtClientTest.class.getResourceAsStream(templates[0]),"UTF-8") ) );
		
		// setup our request
		SLIC slic = request.getSlic();
		slic.setVersionId("slicVersion");
		Application app = request.getApplication();
		app.setName("appName");
		app.setVersionId("appVersionId");
		ApplicationInstallation appInst = request.getApplication().getInstallation();
		appInst.setUuid("appInstUuid");

		//////////////
		// Verify that a well-formed request will result in a correctly formed response object
		ConnectionOpenResponse response = client.connectionOpen(request);
		Assert.assertTrue(response.getAuthToken().equals("authToken"));
		Assert.assertTrue(response.getUpdate().getUpdateUrl().equals("file://none"));
		Assert.assertTrue(response.getUpdate().getInstallNeeds().equals(15L));
		Assert.assertTrue(response.getUpdate().getStorageNeeds().equals(15L));
		Assert.assertTrue(response.getUpdate().getVersionIdentifier().equals("versionId"));
		Assert.assertTrue(response.getUpdate().getHash().getValue().equals("asdf"));
		Assert.assertTrue(response.getUpdate().getHash().getAlgorithm().equals(HashAlgorithm.MD5));
		
		//////////////
		// Verify that the xml, sans the update header, will generate a response with no update
		MockHttpRequestExecuter.setResponseText( Utils.replaceFields(parms, 
				Utils.readInputStream(AppMgmtClientTest.class.getResourceAsStream(templates[1]),"UTF-8") ) );
		response = client.connectionOpen(request);
		Assert.assertTrue(response.getAuthToken().equals("authToken"));
		Assert.assertTrue(response.getUpdate()==null);
		
		//////////////
		// Verify that a non-200 will result in a WebServiceException
		Boolean thrown = false;
		Exception e = null;
		MockHttpRequestExecuter.setResponseCode(304); // 304 is not 200
		try {
			response = client.connectionOpen(request);
		} catch( Exception wse ) {
			e = wse;
			thrown = true;
		}
		Assert.assertTrue(thrown && e instanceof WebServiceException);
		
		//////////////
		// Verify that invalid response content will throw an exception
		thrown = false; e=null;
		MockHttpRequestExecuter.setResponseText( Utils.replaceFields(parms, 
				Utils.readInputStream(AppMgmtClientTest.class.getResourceAsStream(templates[2]),"UTF-8") ) );
		MockHttpRequestExecuter.setResponseCode(200);
		try {
			response = client.connectionOpen(request);
		} catch( Exception wse ) {
			e = wse;
			thrown = true;
		}
		Assert.assertTrue(thrown && e instanceof WebServiceException);
	}
}