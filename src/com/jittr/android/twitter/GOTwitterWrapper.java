package com.jittr.android.twitter;

import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;

/* Wrapper for the Twitter4J class
 * 
 */
public class GOTwitterWrapper {

	public final static String GAMEON_TWITTER_CONSUMER_KEY="ulBahZRGnn584YAgGMIQ";
    public final static String GAMEON_TWITTER_CONSUMER_SECRET="a5uugdaRSrXrYlBmb6AbR09ezfsPF34SqI3akvIVh4";
	public final static String TWITTER_REQUEST_TOKEN_ENDPOINT_URL="http://twitter.com/oauth/request_token";
	public final static String TWITTER_ACCESS_TOKEN_ENDPOINT_URL="http://twitter.com/oauth/access_token";
    public final static String TWITTER_AUTHORIZE_WEBSITE_URL="http://twitter.com/oauth/authorize";
	private static final String GAMEON_TWITTER_CALLBACK_URL = "gameon://oauth";//"http://jittr.com/jittr/confirm.php";
	private static final String TAG = "GOTwitterWraper";
	private String strAccessToken;
	private String strAccessTokenSecret;
	private AccessToken accessToken;
	private DefaultOAuthConsumer consumer;
	private DefaultOAuthProvider provider;
	
	public GOTwitterWrapper(String at, String ats) {
		strAccessToken = at;
		strAccessTokenSecret = ats;
		accessToken = new AccessToken(strAccessToken,strAccessTokenSecret);
	}

	public GOTwitterWrapper(AccessToken at) {
		strAccessToken = at.getToken();
		strAccessTokenSecret = at.getTokenSecret();
		accessToken = at;
	}
	
	public GOTwitterWrapper() {
		
	}

	public String getAccessTokenString() {
       return strAccessToken;
	}
	public String getAccessTokenSecretString() {
		return strAccessTokenSecret;
	}
	
	/* return a user's twitter screenNAme - an authenticated request
	 *  
	 */
	@SuppressWarnings("deprecation")
	public String getTwitterScreenName(AccessToken accessToken) {
        String screenName = null;
		
        Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(GAMEON_TWITTER_CONSUMER_KEY, GAMEON_TWITTER_CONSUMER_SECRET);
		twitter.setOAuthAccessToken(accessToken);
        try {
			screenName = twitter.getScreenName();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		return screenName;
	}  //getTwitterScreenName
	
	public String getTwitterRequestToken() {
		/**
		 * Open the browser and asks the user to authorize the app.
		 * Afterwards, we redirect the user back here!
		 */
           String strAuthUrl = null;
		   try {
  	    	    consumer = new DefaultOAuthConsumer(GAMEON_TWITTER_CONSUMER_KEY,GAMEON_TWITTER_CONSUMER_SECRET);
				provider = new DefaultOAuthProvider(TWITTER_REQUEST_TOKEN_ENDPOINT_URL,
						                            TWITTER_ACCESS_TOKEN_ENDPOINT_URL,
						                            TWITTER_AUTHORIZE_WEBSITE_URL);
				strAuthUrl = provider.retrieveRequestToken(consumer, GAMEON_TWITTER_CALLBACK_URL);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
            return strAuthUrl;
	} 
	public void getTwitterAccessToken(String verifier) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException {
		provider.retrieveAccessToken(consumer, verifier);
		//need to reset Access Token objects
		strAccessToken = consumer.getToken();
		strAccessTokenSecret = consumer.getTokenSecret();
		accessToken = new AccessToken(strAccessToken,strAccessTokenSecret);
	}

	public String getTwitterScreenName() {
		return getTwitterScreenName(accessToken);
	}
}
