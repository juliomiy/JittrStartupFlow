package com.jittr.android.JittrStartupFlow;

import android.app.Activity;
import android.os.Bundle;

	import oauth.signpost.exception.OAuthCommunicationException;
	import oauth.signpost.exception.OAuthExpectationFailedException;
	import oauth.signpost.exception.OAuthMessageSignerException;
	import oauth.signpost.exception.OAuthNotAuthorizedException;
	import oauth.signpost.http.*;
	import oauth.signpost.OAuthProvider;
	import oauth.signpost.basic.DefaultOAuthConsumer;
	import oauth.signpost.basic.DefaultOAuthProvider;
	import oauth.signpost.OAuthConsumer;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
	//import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
	import android.app.Activity;
	import android.content.Intent;
	import android.net.Uri;
	import android.os.Bundle;
	import android.util.Log;
	import android.view.View;
	import android.widget.Button;
	import android.widget.ImageView;
	import android.widget.RadioButton;
	import android.widget.RadioGroup;
	import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

	public class JittrStartupFlowActivity extends JittrBaseActivity {
	   
		private RadioGroup radiogroup;
		private ImageView imageview;
		private RadioButton select_facebook;
		private RadioButton select_twitter;
		private RadioButton select_foursquare;
		private Button cancelButton;
		private Button continueButton;
		private OnCheckedChangeListener onCheckedChangeListener;
		private int socialNetworkSelected = 0;
		private int facebook_radioID;
		private int twitter_radioID;
		private int foursquare_radioID;

		private final int SIGNUP_FACEBOOK=1;
		private final int SIGNUP_TWITTER=2;
		private final int SIGNUP_FOURSQUARE=3;
		private DefaultOAuthProvider provider;
		private OAuthConsumer consumer;

	//Temporary
	    public final static String GAMEON_TWITTER_CONSUMER_KEY="ulBahZRGnn584YAgGMIQ";
	    public final static String GAMEON_TWITTER_CONSUMER_SECRET="a5uugdaRSrXrYlBmb6AbR09ezfsPF34SqI3akvIVh4";
		public final static String TWITTER_REQUEST_TOKEN_ENDPOINT_URL="http://twitter.com/oauth/request_token";
		public final static String TWITTER_ACCESS_TOKEN_ENDPOINT_URL="http://twitter.com/oauth/access_token";
	    public final static String TWITTER_AUTHORIZE_WEBSITE_URL="http://twitter.com/oauth/authorize";
		private static final String GAMEON_TWITTER_CALLBACK_URL = "gameon://oauth";//"http://jittr.com/jittr/confirm.php";

		public final static String GAMEON_FOURSQUARE_CONSUMER_KEY="AROV4OCBZMPAMSHLCF3LLYWQQ0W0F2WH1K1BDZDGBW1OFJSM";
	    public final static String GAMEON_FOURSQUARE_CONSUMER_SECRET="IT5NN1BSTKCORZ3YVC32BRAJD5O4201TCCAGAK2KAMUYHOQD";
		public final static String FOURSQUARE_REQUEST_TOKEN_ENDPOINT_URL="http://foursquare.com/oauth/request_token";
		public final static String FOURSQUARE_ACCESS_TOKEN_ENDPOINT_URL="http://foursquare.com/oauth/access_token";
	    public final static String FOURSQUARE_AUTHORIZE_WEBSITE_URL="http://foursquare.com/oauth/authorize";
		private static final String GAMEON_FOURSQUARE_CALLBACK_URL = "gameon://oauth";//"http://jittr.com/jittr/confirm.php";

		private static final String TAG = "JittrStartupFlowActivity";
	//END Temporary

		/** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	        setUpViews();
	    }
		@Override
		protected void onResume() {
			super.onResume();
		}

        private String getSocialNetwork() {
        	if (socialNetworkSelected == SIGNUP_FACEBOOK) return "facebook";
        	else if (socialNetworkSelected == SIGNUP_TWITTER) return "twitter";
        	else if (socialNetworkSelected == SIGNUP_FOURSQUARE) return "foursquare";
        	return "";
        }
		private void setUpViews() {
	        imageview = (ImageView)findViewById(R.id.logo);
	        radiogroup = (RadioGroup)findViewById(R.id.socialnetworkselection);
	        select_facebook = (RadioButton)findViewById(R.id.select_facebook);
	        select_twitter = (RadioButton)findViewById(R.id.select_twitter);
	        select_foursquare = (RadioButton)findViewById(R.id.select_foursquare);
	        continueButton = (Button) findViewById(R.id.continue_button);
	        cancelButton = (Button)findViewById(R.id.cancel);
	        
	        facebook_radioID = select_facebook.getId();
	        twitter_radioID = select_twitter.getId();
	        foursquare_radioID = select_foursquare.getId();
	        
	        onCheckedChangeListener = new OnCheckedChangeListener() {
	            public void onCheckedChanged(RadioGroup group, int checkedId) {
	               continueButton.setEnabled(true);
	               if (checkedId ==facebook_radioID)
	                  socialNetworkSelected=SIGNUP_FACEBOOK;
	               else if (checkedId == twitter_radioID)
	                  socialNetworkSelected=SIGNUP_TWITTER;
	               else if (checkedId == foursquare_radioID)
	                  socialNetworkSelected = SIGNUP_FOURSQUARE;
	            	//	group.
	            	Log.i("Test", "onCheckedChanged() id:" + checkedId + " social network selected = " + socialNetworkSelected);
	            }
	        };
	        radiogroup.setOnCheckedChangeListener(onCheckedChangeListener); 
	        
	        continueButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					continueSignUp(v);
				}
			});
	        
	        cancelButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
	                cancelSignUp(v);
				}
			});
		}  //setUpViews

		private void continueSignUp(View v) {
		    switch (socialNetworkSelected) {
		       case SIGNUP_FACEBOOK:
		    	  authorizeFacebook(v);  
		          break;
		       case SIGNUP_TWITTER:
		    	  authorizeTwitter(v);
		          break;
		       case SIGNUP_FOURSQUARE:
		    	   authorizeFoursquare(v);
		          break;
		    } //switch
		} //continueSignUp

		private void authorizeFoursquare(View v) {
			/**
			 * Open the browser and asks the user to authorize the app.
			 * Afterwards, we redirect the user back here!
			 */
				try {
	  	    	    consumer = new DefaultOAuthConsumer(GAMEON_FOURSQUARE_CONSUMER_KEY,GAMEON_FOURSQUARE_CONSUMER_SECRET);
					provider = new DefaultOAuthProvider(FOURSQUARE_REQUEST_TOKEN_ENDPOINT_URL,
							                            FOURSQUARE_ACCESS_TOKEN_ENDPOINT_URL,
							                            FOURSQUARE_AUTHORIZE_WEBSITE_URL);
					String authUrl = provider.retrieveRequestToken(consumer, GAMEON_FOURSQUARE_CALLBACK_URL);
					Toast.makeText(this, "Please authorize GameON app!", Toast.LENGTH_LONG).show();
					this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
					Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				}

		}
		private void authorizeTwitter(View v) {
			/**
			 * Open the browser and asks the user to authorize the app.
			 * Afterwards, we redirect the user back here!
			 */
				try {
	  	    	    consumer = new DefaultOAuthConsumer(GAMEON_TWITTER_CONSUMER_KEY,GAMEON_TWITTER_CONSUMER_SECRET);
					provider = new DefaultOAuthProvider(TWITTER_REQUEST_TOKEN_ENDPOINT_URL,
							                            TWITTER_ACCESS_TOKEN_ENDPOINT_URL,
							                            TWITTER_AUTHORIZE_WEBSITE_URL);
					String authUrl = provider.retrieveRequestToken(consumer, GAMEON_TWITTER_CALLBACK_URL);
					Toast.makeText(this, "Please authorize GameON app!", Toast.LENGTH_LONG).show();
					this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
					Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				}

		}
		/**
		 * As soon as the user successfully authorized the app, we are notified
		 * here. Now we need to get the verifier from the callback URL, retrieve
		 * token and token_secret and feed them to twitter4j (as well as
		 * consumer key and secret).
		 */
		@Override
		protected void onNewIntent(Intent intent) {

			super.onNewIntent(intent);
            AccessToken a;
			Uri uri = intent.getData();
			if (uri != null && uri.toString().startsWith(GAMEON_TWITTER_CALLBACK_URL)) {

				String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);

				try {
					provider.retrieveAccessToken(consumer, verifier);
					a = new AccessToken(consumer.getToken(), consumer.getTokenSecret());
					String screenName = null;
					if (socialNetworkSelected == SIGNUP_TWITTER) {
						screenName = getTwitterScreenName(a);
					}
					saveUserCredentials(a.getToken(),a.getTokenSecret(),screenName);		
				} catch (OAuthMessageSignerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthNotAuthorizedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthExpectationFailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OAuthCommunicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}

		private void authorizeFacebook(View v) {
			// TODO Auto-generated method stub

		}

		private void cancelSignUp(View v) {
		    finish();	
		}

		/* save user credentials (oauth tokens) to handset datastore and to reomote host
		 * 
		 */
		private long saveUserCredentials(String OAuthToken,String OAuthTokenSecret, String userName) {
			 long userID = 0; 
			 userID = insertRemoteUser(OAuthToken,OAuthTokenSecret,userName,this.getSocialNetwork());
			 userID = insertLocalUser(OAuthToken,OAuthTokenSecret,userName,this.getSocialNetwork(), userID);
			 
			 return userID;
			//make sure a user with these credentials (username) doesn't already exist
			//create a user record remotely and return the user uniqueID
			//create a user record in the handset device using the id returned by the remote host
		}
		private long  insertLocalUser(String oAuthToken,
				String oAuthTokenSecret, String userName,String socialNetwork,long userID) {
			       JittrStartupFlowApplication appObject = this.getStuffApplication();
			       appObject.createLocalUser(oAuthToken, oAuthTokenSecret, userName, socialNetwork,userID);
                return userID;
		}
		private long insertRemoteUser(String oAuthToken,
				String oAuthTokenSecret, String userName,String socialNetwork) {
		
			long userID=0;
			return 2 ; //userID;
		}
		
		private String getTwitterScreenName(AccessToken accessToken) {
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