package com.jittr.android.JittrStartupFlow;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FSCredentialsActivity extends JittrBaseActivity {
	private static final String TAG = "FSCredentialsActivity";
	private Button loginButton;
	//END Temporary
	private Button cancelButton;
	private EditText fspassword_editText;
	private EditText fsusername_editText;
    private String fsUserName;
    private String fsPassword;
    private Intent callingIntent;
    
	public void setFsUserName(String fsUserName) {
		this.fsUserName = fsUserName;
		if (isFsUserName() && isFsPassword())
			loginButton.setEnabled(true);
		else
			loginButton.setEnabled(false);
	}
	public boolean isFsUserName() {
        if (fsUserName != null && fsUserName.length() > 0) return true;
        return false;
	}
	public boolean isFsPassword() {
        if (fsPassword != null && fsPassword.length() > 0) return true;
        return false;
	}
	public void setFsPassword(String fsPassword) {
		this.fsPassword = fsPassword;
		if (isFsUserName() && isFsPassword())
			loginButton.setEnabled(true);
		else
			loginButton.setEnabled(false);
	}
		/** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.foursquaresignin);
	        setUpViews();
	    }
		private void setUpViews() {
	        loginButton = (Button) findViewById(R.id.login);
	        cancelButton = (Button)findViewById(R.id.cancel);
	        fsusername_editText = (EditText)findViewById(R.id.fsusername);
	        fspassword_editText = (EditText)findViewById(R.id.fspassword);
	        
	        cancelButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
 					cancelSignUp(v);
				}
			});    
	        loginButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
 					signUp(v);
				}
			});    
	    	fsusername_editText.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				  setFsUserName(s.toString());	
				}
				public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
				public void afterTextChanged(Editable s) { }
			});
	    	fspassword_editText.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				  setFsPassword(s.toString());	
				}
				public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
				public void afterTextChanged(Editable s) { }
			});

		}
		protected void signUp(View v) {
              Bundle credentials = new Bundle();
              credentials.putString("username", fsUserName);
              credentials.putString("password", fsPassword);
             // callingIntent.putExtras(credentials);
              callingIntent.putExtra(callingIntent.getPackage(),credentials);
			  setResult(RESULT_OK,callingIntent);
		      finish();	
		}
		protected void cancelSignUp(View v) {
			  setResult(RESULT_CANCELED,callingIntent);
		      finish();	
		}
		@Override
		protected void onResume() {
			super.onResume();
			callingIntent = super.getIntent();
		}
}
