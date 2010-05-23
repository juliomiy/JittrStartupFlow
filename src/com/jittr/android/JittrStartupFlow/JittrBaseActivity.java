package com.jittr.android.JittrStartupFlow;


import android.app.Activity;

public class JittrBaseActivity extends Activity {
	public static JittrStartupFlowApplication appObject;
	
	protected JittrStartupFlowApplication getStuffApplication()
	{
		return (JittrStartupFlowApplication)getApplication();
	}
}
