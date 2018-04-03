package com.cabsoft.rainbowbr.components.notification;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigPictureStyle;

import com.cabsoft.convenience.CabR;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class CabGcmIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 1001;
//	public final Random mRand = new Random();

	public CabGcmIntentService() {
		super("CabGcmIntentService");
//		mRand.setSeed(System.currentTimeMillis());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String msgType = gcm.getMessageType(intent);

		if(!extras.isEmpty()){
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(msgType)){

			}
			else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(msgType)) {

			}
			else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(msgType)) {
				HashMap<String, String> params = new HashMap<String, String>();

				for(String key : extras.keySet()){
					if (!key.startsWith("com.android")) {
						Object objValue = extras.get(key);
						if (objValue instanceof String) {
							params.put(key, (String) objValue);
						}
					}
				}
				
				// GCM을 받은 경우에 처리. 
				// !주의 
				// - 구글 플레이에서 앱 설치 후 열기를 눌렀을 때 들어오는 케이스가 있음.
				//   예상으론 설치 시, 앱 삭제 시 들어오는 듯한 느낌이 듬.
				if (extras.containsKey("msgID") && 
					extras.containsKey("title") && 
					extras.containsKey("message")) {
				
					int    intMsgID		= Integer.valueOf((String) extras.get("msgID"));
					String strTitle		= extras.getString("title");
					String strMessage	= extras.getString("message");
					
					String strImgURL		 = extras.getString("imgURL");
					String strImgTitle		 = extras.getString("imgTitle");
					String strImgSummaryText = extras.getString("imgSummaryText");
					
					params.remove("title");
					params.remove("message");
					params.remove("imgURL");
					params.remove("imgTitle");
					params.remove("imgSummaryText");
	
					if(strTitle == null || strTitle.isEmpty()){
						strTitle = CabR.getString(this, "app_name");
					}
	
					Map <String, Object> mapNotiInfo = new HashMap<String, Object>();
					
					mapNotiInfo.put("msgID",		  intMsgID);
					mapNotiInfo.put("title",		  strTitle);
					mapNotiInfo.put("message",		  strMessage);
					mapNotiInfo.put("imgURL", 		  strImgURL);
					mapNotiInfo.put("imgTitle",		  strImgTitle);
					mapNotiInfo.put("imgSummaryText", strImgSummaryText);
					
					sendNotification(mapNotiInfo, params);
					
				}
				
			}
		}

		CabGcmBroadcastReciever.completeWakefulIntent(intent);
	}

	@TargetApi(16)
	@SuppressLint("NewApi")
	private void sendNotification(Map <String, Object> notiInfo, Map<String, String> params)
	{
		
		int    intMsgID	  = (Integer)notiInfo.get("msgID");
		String strTitle	  = (String) notiInfo.get("title");
		String strMessage = (String) notiInfo.get("message");
		
		String strImgURL		 = (String) notiInfo.get("imgURL");
		String strImgTitle		 = (String) notiInfo.get("imgTitle");
		String strImgSummaryText = (String) notiInfo.get("imgSummaryText");
		
		NotificationManager notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Context context = getApplicationContext();
		String packageName  = context.getPackageName();
		Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		
		Intent intent = launchIntent; //new Intent(context, CabWebActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

		for(String key : params.keySet()){
			intent.putExtra(key, params.get(key));
		}

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		if (Build.VERSION.SDK_INT >= 16) {
			
			Notification.Builder builder = null;

			builder = new Notification.Builder(this)
						.setSmallIcon(CabR.getDrawableId(this, "ic_launcher"))
						.setContentTitle(strTitle)
						.setContentText(strMessage)
						.setTicker(strMessage)
						.setPriority(Notification.PRIORITY_MAX)
						.setWhen(System.currentTimeMillis())
						.setStyle(new Notification.BigTextStyle().bigText(strMessage))
						.setContentIntent(contentIntent)
						.setDefaults(Notification.DEFAULT_VIBRATE)
						.setAutoCancel(true);
		
			if (Build.VERSION.SDK_INT >= 21) {
				builder.setVisibility(Notification.VISIBILITY_PUBLIC)
					   .setCategory(Notification.CATEGORY_PROMO);
			}

			if(strImgURL != null && !strImgURL.isEmpty()){
				
				Bitmap bitmap = getBitmapFromURL(strImgURL);
				
				if(bitmap != null){
					
					Notification.BigPictureStyle bigStyle = null;
					
					bigStyle = new Notification.BigPictureStyle(builder)
								.setBigContentTitle(strImgTitle)
								.setSummaryText(strImgSummaryText)
								.bigPicture(bitmap);
				
					builder.setStyle(bigStyle);
				
				}
			}

			notiManager.notify(NOTIFICATION_ID + intMsgID, builder.build());

		} else {

			NotificationCompat.Builder builder = null;
			
			builder = new NotificationCompat.Builder(this)
						.setSmallIcon(CabR.getDrawableId(this, "ic_launcher"))
						.setContentTitle(strTitle)
						.setContentText(strMessage)
						.setTicker(strMessage)
						.setWhen(System.currentTimeMillis())
						.setStyle(new NotificationCompat.BigTextStyle().bigText(strMessage))
						.setContentIntent(contentIntent)
						.setDefaults(Notification.DEFAULT_VIBRATE)
						.setAutoCancel(true);

			if(strImgURL != null && !strImgURL.isEmpty()){
				Bitmap bitmap = getBitmapFromURL(strImgURL);
				if(bitmap != null){
					NotificationCompat.BigPictureStyle bigStyle = new BigPictureStyle(builder);
					bigStyle.setBigContentTitle(strImgTitle);
					bigStyle.setSummaryText(strImgSummaryText);
					bigStyle.bigPicture(bitmap);
					builder.setStyle(bigStyle);
				}
			}

			notiManager.notify(NOTIFICATION_ID + intMsgID, builder.build());

		}

	}

	public Bitmap getBitmapFromURL(String strUrl)
	{
		try{
			URL url = new URL(strUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(input);
			return bitmap;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
