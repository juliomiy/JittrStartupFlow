package com.jittr.android.JittrStartupFlow;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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

import org.apache.http.client.methods.HttpGet;

import com.jittr.android.foursquare.GOFoursquare;
import com.jittr.android.twitter.GOTwitterWrapper;

import android.app.Activity;
import android.os.Bundle;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.http.*;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.OAuthConsumer;
import twitter4j.http.AccessToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

		private String foursquareUserName;
		private String foursquarePassword;
		
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
		private static final String GAMEON_FOURSQUARE_CALLBACK_URL = "gameon://fsoauth";//"http://jittr.com/jittr/confirm.php";
        private static final String FOURSQUARE_BASIC_AUTHORIZE_WEBSITE_URL="http://api.foursquare.com/v1/authexchange?";
		private static final String TAG = "JittrStartupFlowActivity";
	//END Temporary
		private boolean hasFoursquareCredentials() {
			if (foursquareUserName != null && foursquarePassword != null) {
				return true;
			}
			return false;
		}
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
		    	  // getFoursquareCredentials(v);
		    	  authorizeFoursquare();
		          break;
		    } //switch
		} //continueSignUp

		/* instantiate an activity to obtain username/password*/
		private void getFoursquareCredentials(View v) {
			Intent intent = new Intent(JittrStartupFlowActivity.this, FSCredentialsActivity.class);
          	startActivityForResult(intent, SIGNUP_FOURSQUARE);
		}
		// Listen for results.
		public void onActivityResult(int requestCode, int resultCode, Intent data){
		    // See which child activity is calling us back.
			Log.d(TAG, "OnActivityResult - requestCode " + requestCode + " ResultCode " +  resultCode + " Data " + data );
		    switch (requestCode) {
		        case SIGNUP_FOURSQUARE:
		    	   if (resultCode == RESULT_OK) {
		              Bundle credentials = data.getBundleExtra(data.getPackage());	
		              foursquareUserName = (String)credentials.get("username");
		              foursquarePassword = (String)credentials.get("password");
                      if (hasFoursquareCredentials()) {
          //          	  authorizeFoursquare( foursquareUserName, foursquarePassword);
                      }
		              break;
		    	   }  
		    	   
		    	 default:
		    		 super.onActivityResult(requestCode,resultCode,data);
		    		 break;
		    } //switch
		} //
		
		private void authorizeFoursquare() {
			/**
			 * Open the browser and asks the user to authorize the app.
			 * Afterwards, we redirect the user back here!
			 */
			    //String url = FOURSQUARE_BASIC_AUTHORIZE_WEBSITE_URL +"fs_username=" + userName +"&fs_password=" + password;
                 try {
	  	    	    consumer = new DefaultOAuthConsumer(GAMEON_FOURSQUARE_CONSUMER_KEY,GAMEON_FOURSQUARE_CONSUMER_SECRET);
	  	    	    provider = new DefaultOAuthProvider(FOURSQUARE_REQUEST_TOKEN_ENDPOINT_URL,
							                            FOURSQUARE_ACCESS_TOKEN_ENDPOINT_URL,
							                            FOURSQUARE_AUTHORIZE_WEBSITE_URL);
					String authUrl = provider.retrieveRequestToken(consumer,OAuth.OUT_OF_BAND);// GAMEON_FOURSQUARE_CALLBACK_URL);
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
            String verifier=null;
            
			super.onNewIntent(intent);
            AccessToken accessToken;
			Uri uri = intent.getData();
			if (uri != null && uri.toString().startsWith(GAMEON_TWITTER_CALLBACK_URL)) {
				verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
			} else if (uri != null && uri.toString().startsWith(GAMEON_FOURSQUARE_CALLBACK_URL)) {
				verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_TOKEN);
			}	
			try {
					provider.retrieveAccessToken(consumer, verifier);
					accessToken = new AccessToken(consumer.getToken(), consumer.getTokenSecret());
					String userName = null;
					if (socialNetworkSelected == SIGNUP_TWITTER) {
						GOTwitterWrapper twitter = new GOTwitterWrapper();
						userName = twitter.getTwitterScreenName(accessToken);
					} else if (socialNetworkSelected == SIGNUP_FOURSQUARE) {
                        GOFoursquare foursquare = new GOFoursquare(accessToken);
						userName = foursquare.getFoursquareScreenName(accessToken);
					}
 					saveUserCredentials(accessToken.getToken(),accessToken.getTokenSecret(),userName);		
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
				} //try 
		}   //onNewIntent
		
	
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
			    String strUrl = "http://jittr.com/jittr/gameon/go_postnewuser.php?primarynetworkname="+socialNetwork;
		        long userID = -1;
			    try {
					URL url = new URL(strUrl);
					HttpURLConnection request = (HttpURLConnection) url.openConnection();
					request.setRequestMethod("GET");
					//consumer.sign(request);
					request.connect();
					InputStream is = request.getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
	                String inputLine, response=null;
					while ((inputLine = br.readLine()) != null ) {
						response += inputLine;
					}
					is.close();
			        Log.d(TAG,response);
			        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
			        DocumentBuilder builder = factory.newDocumentBuilder();
                    InputSource inputsource = new InputSource(new StringReader(response));
			        Document document = builder.parse(inputsource);
			        
			        Element element = document.getDocumentElement();
			        NodeList nodelist = element.getElementsByTagName("userid");
			        Element elementUserid = (Element) nodelist.item(0);
			        NodeList nlUserID = elementUserid.getChildNodes();
			        
			        Long longUserID = new Long((nlUserID.item(0)).getNodeValue());
			        userID = longUserID.longValue();
			        Log.d(TAG , "UserID " + userID );
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return userID ; //userID;
		}
}  //class