package com.jittr.android.JittrStartupFlow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class JittrSQLiteOpenHelper extends SQLiteOpenHelper {
	public static final int VERSION = 1;
	public static final String DB_NAME  = "jittr_gameOn_db.sqlite";
	public static final String GAME_TABLE = "go_games";
    public static final String GAME_USER_SETTINGS_TABLE = "go_userSettings";
	public static final String GAME_USER_TABLE = "go_user";
	public static final String GAME_ID = "id";
	public static final String GAME_NAME="name";
	public static final String GAME_COMPLETE = "complete";
	public static final String GAME_PUBLIC_PRIVATE ="visibility" ;
	public static final String GAME_TYPE = "type";
	public static final String GAME_MODIFIEDDATE ="modifiedDate";
	public static final String GAME_CREATEDDATE = "createdDate";
	public static final String GAME_BANKBALANCE = "bankBalance";
	public static final String GAME_USERNAME = "userName";
	public static final String GAME_TWITTER = "twitterID";
	public static final String GAME_FACEBOOK = "facebookID";
	public static final String GAME_FOURSQUARE = "foursquareID";
	public static final String GAME_USER_ID = "userID";
	public static final String GAME_TWITTER_DEFAULT="twitterDefault";
	public static final String GAME_FACEBOOK_DEFAULT="facebookDefault";
    public static final String GAME_FOURSQUARE_DEFAULT="foursquareDefault";
	public static final String GAME_TWITTER_OAUTH_TOKEN = "twitterOAuthToken";
	public static final String GAME_TWITTER_OAUTH_TOKEN_SECRET = "twitterOAuthTokenSecret";
	public static final String GAME_FOURSQUARE_OAUTH_TOKEN = "foursquareOAuthToken";
	public static final String GAME_FOURSQUARE_OAUTH_TOKEN_SECRET = "foursquareOAuthTokenSecret";
	public static final String GAME_USERDEFAULTNETWORK="defaultNetwork";
	
	
	public JittrSQLiteOpenHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	public JittrSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
         dropAndCreate(db);	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	protected void dropAndCreate(SQLiteDatabase db) {
		dropTables(db);
		createTables(db);
	}

	private void dropTables(SQLiteDatabase db) {
		db.execSQL("drop table if exists " + GAME_TABLE + ";");
		db.execSQL("Drop table if exists " + GAME_USER_TABLE + ";");
		db.execSQL("Drop table if exists " + GAME_USER_SETTINGS_TABLE + ";");
	}

	protected void createTables(SQLiteDatabase db) {
        String sql;
		db.execSQL(
				"create table " + GAME_TABLE +" (" +
				GAME_ID + " integer primary key autoincrement not null," +
				GAME_NAME + " text," +
				GAME_TYPE + " text," +
				GAME_FACEBOOK + " text null," +
				GAME_TWITTER + " text null, " +
				GAME_FOURSQUARE + " text null," +
				GAME_PUBLIC_PRIVATE + " integer not null default 0," +
				GAME_COMPLETE + " text not null default 0," +
				GAME_CREATEDDATE + " timestamp not null default CURRENT_TIMESTAMP," +
				GAME_MODIFIEDDATE + " timestamp" +
				");"
			);
		sql = "create table go_user (userID integer primary key  not null," +
		      "userName text not null default 'anonymous'," +
		      "bankBalance float not null default 0," +
		      "defaultNetwork text," +
		      "createdDate timestamp not null default CURRENT_TIMESTAMP," +
		      "modifiedDate timestamp null" +
				");";
		db.execSQL(sql);
		sql = "create table go_userSettings(userID integer primary key not null," +
                " foursquareID text null," +
		        " twitterID text null," +
		        " facebookID text null," +
		        " aimID text null," +
		        " icqID text null," +
				" twitterDefault text not null default 'false'," +
				" facebookDefault text not null default 'false'," +
				" facebookImageUrl text null," +
				" foursquareDefault text not null default 'false'," +
				" twitterOAuthToken text null," +
				" twitterOAuthTokenSecret text null," +
				" twitterImageUrl text null," +
				" foursquareOAuthToken text null," +
				" foursquareOAuthTokenSecret text null," +
				" foursquareImageUrl text null," +
				" lastSync datetime null," +
				" createdDate timestamp not null default CURRENT_TIMESTAMP," +
				" modifiedDate timestamp null " +
				");";
		db.execSQL(sql);
//        setDefaultSettings(db);
	}  //createTbles
}
