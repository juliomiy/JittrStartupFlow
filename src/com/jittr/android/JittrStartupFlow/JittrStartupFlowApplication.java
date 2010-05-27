package com.jittr.android.JittrStartupFlow;


import android.app.Application;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;


public class JittrStartupFlowApplication extends Application {
	private JittrStartupFlowApplication appContext;
	private SQLiteDatabase database;
   
	@Override
	public void onCreate() {
		super.onCreate();
		appContext = this;
		JittrSQLiteOpenHelper helper = new JittrSQLiteOpenHelper(this);
		database = helper.getWritableDatabase();
	}  //onCreate

	@Override
	public void onTerminate() {
		database.close();
		super.onTerminate();
	}

	public long createLocalUser(String OAuthToken, String OAuthTokenSecret, String userName, String socialNetwork,long userID) {
		//insert to go_user
		ContentValues values = new ContentValues();
		values.put(JittrSQLiteOpenHelper.GAME_USER_ID, userID);
		values.put(JittrSQLiteOpenHelper.GAME_USERNAME, userName);
		values.put(JittrSQLiteOpenHelper.GAME_BANKBALANCE, 2000);
		values.put(JittrSQLiteOpenHelper.GAME_USERDEFAULTNETWORK, socialNetwork);

		long id = database.insert(JittrSQLiteOpenHelper.GAME_USER_TABLE, null, values);
		
		//insert to go_userSettings
		values=new ContentValues();
		values.put(JittrSQLiteOpenHelper.GAME_USER_ID, userID);
		//values.put(JittrSQLiteOpenHelper.GAME_USERNAME, userName);
		if (socialNetwork=="twitter") {
		   values.put(JittrSQLiteOpenHelper.GAME_TWITTER, userName);
		   values.put(JittrSQLiteOpenHelper.GAME_TWITTER_OAUTH_TOKEN,OAuthToken);
		   values.put(JittrSQLiteOpenHelper.GAME_TWITTER_OAUTH_TOKEN_SECRET,OAuthTokenSecret);
		} else if (socialNetwork=="foursquare") {
		   values.put(JittrSQLiteOpenHelper.GAME_FOURSQUARE, userName);
		   values.put(JittrSQLiteOpenHelper.GAME_FOURSQUARE_OAUTH_TOKEN,OAuthToken);
		   values.put(JittrSQLiteOpenHelper.GAME_FOURSQUARE_OAUTH_TOKEN_SECRET,OAuthTokenSecret);		   
		}
		database.insert(JittrSQLiteOpenHelper.GAME_USER_SETTINGS_TABLE, null, values);
        return id;
	}
}  //class
