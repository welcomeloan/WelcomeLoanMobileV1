package com.cabsoft.rainbowbr.components.notification;

import java.util.HashMap;

import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;

import com.cabsoft.rainbowbr.components.CabBankXComponent;
import com.cabsoft.rainbowbr.components.notification.CabGcmRegister.GcmRegListener;
import com.cabsoft.rainbowbr.property.CabBankAppProperty;
//import com.callgate.launcher.LauncherLinker;
//import com.callgate.launcher.LauncherListener;

public class CabNotiComponent extends CabBankXComponent /*implements LauncherListener*/ {
	public static final String COMPONENT_KEY = "noti";
	
	private String mRegId = "";
	private CabGcmRegister	mRegister;
	
	//private LauncherLinker launcherLinker;
	
	
	public CabNotiComponent(Activity activity, String SenderID){
		mActivity = activity;
		
		//launcherLinker = new LauncherLinker(activity.getApplicationContext(), activity, this);
		//launcherLinker.setPNSSenderID(false, CabBankAppProperty.PROJECT_NUMBER, true);
		
		/* LauncherLinker.initialize()
		 * 3번쨰 Param 고정.
		 * 4번째 Param : Appcation에서 GCM 등록 시엔 false, Launcher에서 처리하도록 하려면 true.
		 * 5번째 Param : GCM 3.0을 사용하려면 false, 이전 버전의 GCM을 사용하려면 true.
		 * 6번째 Param : Application에서 직접 READ_PHONE_STATE 권한을 요청하면 false, Launcher에서 처리하도록 하려면 true.
		 * 7번째 Param : Application에서 직접 SYSTEM_ALERT_WINDOW 권한을 요청하거나 해당 권한을 사용하고 싶지 않을 때 false, Launcher에서 처리하도록 하려면 true.
		 */
		//launcherLinker.initialize(CabBankAppProperty.LAUNCHER_ID, CabBankAppProperty.CONFIG_FILE_URL, true, false, true, true, true);
		
		
		mRegister = new CabGcmRegister(mActivity, SenderID, CabBankAppProperty.CALLGATE_SENDER_ID, new GcmRegListener() {
			public void getRegId(String regId, boolean isNew) {
				mRegId = regId;
				
				if(isNew) {
					//launcherLinker.requestFCCRegistration(CabBankAppProperty.LAUNCHER_ID, regId);
				}
				
			}
		});
		mRegister.getRegId();
	}
	
	/**
	 * 앱의 GCM 등록 아이디를 받아온다.
	 * @param param 웹으로부터 전달받는 파라메터 <br>
	 * 	<ul>
	 * 	<li>instanceName 실행결과를 전달 받을 인스턴스명</li>
	 * 	<li>callbackName 실행결과를 전달 받을 콜백함수명</li>
	 * 	</ul>
	 * @param webview 스키마를 호출한 웹뷰
	 */
	public void getRegId(HashMap<String, Object> param, WebView webview)
	{
		// 결과 callback 용
		String instanceName = (String) param.get("instanceName");
		String callbackName = (String) param.get("callbackName");
		
		int retCode = -1;
		
		HashMap<String, Object> retData = null;
		if(mRegId != null && !mRegId.isEmpty()){
			retCode = 0;
			retData = new HashMap<String, Object>();
			retData.put("regId", mRegId);
		}
		Log.i("TEST", "instanceName:"+instanceName);
		Log.i("TEST", "callbackName:"+callbackName);
		Log.i("TEST", "mRegId:"+mRegId);
		postLoadUrl(webview, instanceName, callbackName, retCode, retData);
	}

	/*
	@Override
	public void recvLauncherResult(int resultCode, String msg) {
		System.out.println("recvLauncherResult!!! : " + resultCode + ", " + msg);
		
	}*/
}
