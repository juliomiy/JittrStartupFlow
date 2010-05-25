package com.jittr.android.twitter;

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
	private String strAccessToken;
	private String strAccessTokenSecret;
	private AccessToken accessToken;
	
	public GOTwitterWrapper(String at, String ats) {
		strAccessToken = at;
		strAccessTokenSecret = ats;
	}

	public GOTwitterWrapper(AccessToken at) {
		strAccessToken = at.getToken();
		strAccessTokenSecret = at.getTokenSecret();
		accessToken = at;
	}
	
	public GOTwitterWrapper() {
		
	}

	/* return a user's twitter screenNAme - an authenticated request
	 *  
	 */
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
	}
}
