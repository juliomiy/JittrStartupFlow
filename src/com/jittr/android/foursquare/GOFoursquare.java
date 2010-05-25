package com.jittr.android.foursquare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
	
	public String getFoursquareScreenName(AccessToken accessToken) {
		String strUrl = "http://api.foursquare.com/v1/user.xml";
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
		} 
		return "9173702880";
	}
}
