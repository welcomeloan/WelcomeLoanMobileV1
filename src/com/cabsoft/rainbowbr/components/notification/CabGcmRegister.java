package com.cabsoft.rainbowbr.components.notification;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class CabGcmRegister {

	public interface GcmRegListener{
		public void getRegId(String regId, boolean isNew);
	}
	
	private final static String REG_ID_KEY = "GCM_REG_ID";
	
	private String mRegId;
	private Context mContext;
	private GcmRegListener mListener;
	private GoogleCloudMessaging mGcm;
	private String mSenderId;
	private String mCallgateId;
	
	public CabGcmRegister(Context context, String senderId, String callgateId, GcmRegListener listener){
		mContext = context;
		mListener = listener;
		mSenderId = senderId;
		mCallgateId = callgateId;
	}
	
	public void getRegId()
	{
		if (checkPlayService()){
			mGcm = GoogleCloudMessaging.getInstance(mContext.getApplicationContext());
			mRegId = getRegistrationId();
			
			if(mRegId == null || mRegId.isEmpty()){
				registerInBackground();
			}else{
				if(mListener != null){
					mListener.getRegId(mRegId, false);
				}
			}
		}
	}
	
	private boolean checkPlayService()
	{
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
		if (resultCode != ConnectionResult.SUCCESS){
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
				GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) mContext, 9000).show();
			}
			
			return false;
		}
		return true;
	}
	
	private String getRegistrationId()
	{
		String packageName = mContext.getApplicationContext().getPackageName();
		SharedPreferences pref = mContext.getSharedPreferences(packageName, Context.MODE_PRIVATE);
		String registrationId = pref.getString(REG_ID_KEY, "");
		
		return registrationId;
	}
	
	private void registerInBackground()
	{
		new AsyncTask<Void, Void, String>()
	      {
	         @Override
	         protected String doInBackground(Void... params)
	         {
	            String msg = "";
	            try
	            {
	               if (mGcm == null)
	               {
	            	   mGcm = GoogleCloudMessaging.getInstance(mContext.getApplicationContext());
	               }
	               mRegId = mGcm.register(mSenderId, mCallgateId);
	               storeRegistrationId(mRegId);
	            }
	            catch (IOException ex)
	            {
	               ex.printStackTrace();
	            }
	 
	            return msg;
	         }
	 
	         @Override
	         protected void onPostExecute(String msg)
	         {
	        	 if(mListener != null){
					mListener.getRegId(mRegId, true);
	        	 }
	         }
	      }.execute(null, null, null);
	}
	
	private void storeRegistrationId(String regId)
	{
		String packageName = mContext.getApplicationContext().getPackageName();
		SharedPreferences pref = mContext.getSharedPreferences(packageName, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		
		editor.putString(REG_ID_KEY, regId);
		editor.commit();
	}
}
