package com.jittr.android.foursquare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import twitter4j.http.AccessToken;
import android.util.Log;

public class GOFoursquare {

	public final static String GAMEON_FOURSQUARE_CONSUMER_KEY="AROV4OCBZMPAMSHLCF3LLYWQQ0W0F2WH1K1BDZDGBW1OFJSM";
    public final static String GAMEON_FOURSQUARE_CONSUMER_SECRET="IT5NN1BSTKCORZ3YVC32BRAJD5O4201TCCAGAK2KAMUYHOQD";
	public final static String FOURSQUARE_REQUEST_TOKEN_ENDPOINT_URL="http://foursquare.com/oauth/request_token";
	public final static String FOURSQUARE_ACCESS_TOKEN_ENDPOINT_URL="http://foursquare.com/oauth/access_token";
    public final static String FOURSQUARE_AUTHORIZE_WEBSITE_URL="http://foursquare.com/oauth/authorize";
	private static final String GAMEON_FOURSQUARE_CALLBACK_URL = "gameon://fsoauth";//"http://jittr.com/jittr/confirm.php";
    private static final String FOURSQUARE_BASIC_AUTHORIZE_WEBSITE_URL="http://api.foursquare.com/v1/authexchange?";
	private static final String FOURSQUARE_API_USER = "http://api.foursquare.com/v1/user.xml";
	private String strAccessToken;
	private String strAccessTokenSecret;
	private AccessToken accessToken;

	public void GOFoursquare() {
		
	}
	public void GOFoursquare(String at, String ats) {
		strAccessToken = at;
		strAccessTokenSecret = ats;
	}

	public GOFoursquare(AccessToken at) {
		strAccessToken = at.getToken();
		strAccessTokenSecret = at.getTokenSecret();
		accessToken = at;
	}
	
	public GOFoursquare() {
		// TODO Auto-generated constructor stub
	}
	public String getFoursquareScreenName(AccessToken accessToken) {
		String strUrl = FOURSQUARE_API_USER; 
		/* XML return for this api
				 * <?xml version="1.0" encoding="UTF-8"?>
		<user>
			<id>33</id>
			<firstname>Naveen</firstname>
			<lastname>Selvadurai</lastname>
			<photo>http://foursquare.com/userpix/33_1235974851.jpg</photo>
		        <gender>male</gender>
		        <phone>2129103995</phone>
		        <email>n@naveen.com</email>
		        <twitter>naveen</twitter>
		        <facebook>29103995</facebook>
		        <friendstatus>friend</friendstatus>
		        <checkin>
		          <id>413421</id>
		          <created>Mon, 29 Jun 09 14:21:06 +0000</created>
		          <venue>
		            <id>45506</id>
		            <name>4SQ HQ - Soho</name>
		            <address>...</address>
		            <crossstreet>btw Grand &amp; Broome</crossstreet>
		            <city>New York</city>
		            <state>NY</state>
		            <zip>10013</zip>
		            ...
		          </venue>
		        </checkin>
		        <badges>
				<badge>
					<name>Newbie</name>
					<icon>http://foursquare.com/img/badge/newbie_on.png</icon>
					<description>Congrats on your first check-in!</description></badge>
		
		Response for 'self' authenticated (truncated):
		
		<?xml version="1.0" encoding="UTF-8"?>
		<user>
		  <id>33</id>
		  ...
		  <settings>
		    <sendtotwitter>false</sendtotwitter>
		    <sendtofacebook>false</sendtofacebook>
		    <pings>on</pings>
		  </settings>
		 * 
		 */
		String name=null;
        HttpURLConnection request;
  	    OAuthConsumer consumer = new DefaultOAuthConsumer(GAMEON_FOURSQUARE_CONSUMER_KEY,GAMEON_FOURSQUARE_CONSUMER_SECRET);
        try {
			URL url = new URL(strUrl);
			request = (HttpURLConnection) url.openConnection();
			request.setRequestMethod("GET");
			//
			consumer.setTokenWithSecret(accessToken.getToken(), accessToken.getTokenSecret());
			consumer.sign(request);
			request.connect();
			InputStream is = request.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine, response=null;
			while ((inputLine = br.readLine()) != null ) {
				response += inputLine;
			}
			is.close();
			/* Temporary - need general purpose template */
		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputsource = new InputSource(new StringReader(response));
	        Document document = builder.parse(inputsource);
	        
	        Element element = document.getDocumentElement();
	        NodeList nodelist = element.getElementsByTagName("lastname");
	        Element elementUserid = (Element) nodelist.item(0);
	        NodeList nlLastName = elementUserid.getChildNodes();
	        name = (nlLastName.item(0)).getNodeValue();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return name;
	}
}
