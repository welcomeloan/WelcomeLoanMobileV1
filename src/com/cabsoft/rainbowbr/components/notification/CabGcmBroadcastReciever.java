package com.cabsoft.rainbowbr.components.notification;

import com.cabsoft.convenience.CSLog;
//import com.callgate.launcher.LauncherLinker;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

public class CabGcmBroadcastReciever extends WakefulBroadcastReceiver {

	//private LauncherLinker launcherLinker; // LauncherLinker 선언
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		CSLog.w("GcmBroadcastReceiver.java | onReceive", "|" + "================="+"|");

		/*
		launcherLinker = new LauncherLinker(context.getApplicationContext());
		
		boolean isFCC = launcherLinker.isCloudMessageForFCC(intent);
		
		CSLog.w("GcmBroadcastReceiver.java | isFCC", "|" + isFCC +"|");
		
		if(isFCC) {
			return;
		}*/

		
		Bundle bundle = intent.getExtras();
	    for (String key : bundle.keySet())
	    {
	       Object value = bundle.get(key);
	       CSLog.i("GcmBroadcastReceiver.java | onReceive", "|" + String.format("%s : %s (%s)", key, value.toString(), value.getClass().getName()) + "|");
	    }
	    CSLog.i("GcmBroadcastReceiver.java | onReceive", "|" + "================="+"|");
	 
	    ComponentName comp = new ComponentName(context.getPackageName(), CabGcmIntentService.class.getName());
	    startWakefulService(context, intent.setComponent(comp));
	    setResultCode(Activity.RESULT_OK);
	}

}
