package com.cabsoft.rainbowbr.property;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.cabsoft.convenience.CabR;
import com.cabsoft.rainbowbr.components.CabBankXComponent;
import com.cabsoft.rainbowbr.components.appprotect.CabAppProtectComponent;
import com.cabsoft.rainbowbr.components.basic.CabBasicComponent;
import com.cabsoft.rainbowbr.components.draftcontract.CabDContractComponent;
import com.cabsoft.rainbowbr.components.droidx.CabDroidXComponent;
import com.cabsoft.rainbowbr.components.inisafilter.CabInisaFilterComponents;
import com.cabsoft.rainbowbr.components.nfilter.CabNFilterComponent;
import com.cabsoft.rainbowbr.components.notification.CabNotiComponent;
import com.cabsoft.rainbowbr.components.photoswipe.CabPhotoSwipeComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * 어플리케이션의 기본설정 및 컴포넌트설정을 제어하는 클래스
 * @author pys
 *
 */
public class CabBankAppProperty {
	
	public final static String mScheme = "cab";	// 현재앱에서 사용할 스키마 (rainbowBr의 js 단의 scheme)

	String mHomeURL = "http://211.232.100.60:8092/WM_IN_000.do";	//개발 첫페이지 URL
//	String mHomeURL = "https://app.welcomeloan.co.kr/WM_IN_000.do";	//운영 첫페이지 URL
	//String mHomeURL = "https://appa.welcomeloan.co.kr/WM_IN_000.do";	//애니원론 개발 첫페이지 URL
	//String mHomeURL = "https://app.anyonecapital.co.kr/WM_IN_000.do"; //애니원론 운영 첫페이지 URL
	String mErrorURL = "file:///android_asset/errorPage/html/networkError.html";

	public final static String scrapSaveUrl = "http://211.232.100.60:8092/WM_BZ_701_SAVE_SCRAP.do";
//	public final static String scrapSaveUrl = "https://app.welcomeloan.co.kr/WM_BZ_701_SAVE_SCRAP.do";

	 public final static String CACert ="-----BEGIN CERTIFICATE-----\nMIIDpjCCAo6gAwIBAgIQR0Y5lIvy5YeVP8Z62c3xsTANBgkqhkiG9w0BAQsFADBT\nMQswCQYDVQQGEwJLUjEQMA4GA1UEChMHSU5JVEVDSDERMA8GA1UECxMIUGx1Z2lu\nQ0ExHzAdBgNVBAMTFklOSVRFQ0ggUGx1Z2luIFJvb3QgQ0EwHhcNMDkwNjE1MDgz\nNjM2WhcNMzAwNjE1MDgzNjM2WjBTMQswCQYDVQQGEwJLUjEQMA4GA1UEChMHSU5J\nVEVDSDERMA8GA1UECxMIUGx1Z2luQ0ExHzAdBgNVBAMTFklOSVRFQ0ggUGx1Z2lu\nIFJvb3QgQ0EwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCk0vGFf5xr\nVt1EQPVNqRPuQ7QjZr2kqd0q0f92r7id/LoTIk+U3f5BgH6ZCk0XLqocB9YsApTx\nod6nLuguXdBatd+R9EinrZHOUClYAWVPJqfJ+HM1wazxopWCJ2abG+ijGMzECcJu\nW4fkpkb1YRyiAUfb5S4zdOw//SUwSHe3uXYpGy5AKMVHznBCHn5GXX3ZJAcROZw1\nOq+sZapwbGdmkgfEKrvJWBVY2ia/1Y4223JxEkMD88XoGZOfoSET078w8XMBJUVM\n6ftqtmWkRNCXzQsDY8HYcMYK+iAhBkCKEywqjaN/AzaWaUVs9IOvfa1jhvjIGcab\nbX3ufgt9b8BFAgMBAAGjdjB0MBEGA1UdIAQKMAgwBgYEVR0gADAPBgNVHRMBAf8E\nBTADAQH/MA4GA1UdDwEB/wQEAwIBxjAdBgNVHQ4EFgQUdZHynOrUueejpV93hNuh\nGg0Yi6UwHwYDVR0jBBgwFoAUdZHynOrUueejpV93hNuhGg0Yi6UwDQYJKoZIhvcN\nAQELBQADggEBAFjbyOEtkWVG8x6iDP/kK3PxOiOWoOyODDIv8sRbHVUChtYqpV53\ndaZixPMAXXeO3hrlpwZabKWNtUwbQaEsRsRW1mh58YbmM/atiYjho8aeg+ksIqv7\nUVOXnZ3Uv7a+7RPGqJICVoL/PPZgv1PlyQgjmR1qKSGlAJ1GAgYyXVaZd3cnZDHe\ngOm1NliNMYo8YCNGEE4ya8WxWNbznHYlAbure/G+T6x5BG+N4oOynlKT+y1eXfbc\nJx1vO1MIb861TzMJs6PY4NyKIGVVE1ZEQ+iKT7TlPaC7TZUHXZVc6QGrw7qA96PO\npA32EMaJIeHSlZmNGqH9WvMBjFjfRHKFjOc=\n-----END CERTIFICATE-----\n";                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
	 // 웰컴 론 공개키
	 public final static String SCert  ="-----BEGIN CERTIFICATE-----\nMIIDtTCCAp2gAwIBAgIDAg+BMA0GCSqGSIb3DQEBCwUAMFMxCzAJBgNVBAYTAktS\nMRAwDgYDVQQKEwdJTklURUNIMREwDwYDVQQLEwhQbHVnaW5DQTEfMB0GA1UEAxMW\nSU5JVEVDSCBQbHVnaW4gUm9vdCBDQTAeFw0xNDA0MTEwMjEyMjRaFw0yNDA0MTEw\nMjEyMjNaMEExCzAJBgNVBAYTAktSMRIwEAYDVQQKEwlJTklURUNIQ0ExHjAcBgNV\nBAMTFXd3dy53ZWxjb21lbG9hbi5jby5rcjCCASIwDQYJKoZIhvcNAQEBBQADggEP\nADCCAQoCggEBAMjP0tptfZBXw269HdiigChxnJAhZQmP4KHBxKMlU+Y3K/G6Y4I2\n/Mr0UYYmQKUZa5CCovyDVAmYM/wCQ53zBEaVInGSZIMxgJrKNyaSXMBhGN1qiVKZ\n2STQnHZpSAKECOiFPe7XD7fDz1o+8hgPj3fi42+ry0+o7e+hroYM0eGP6XIswMrZ\n883M3iFqDgY0wGTmd483oFCP/m/pnHB29v8I403ZpUKy/UeMj4byPMoopKIgg7Dh\n6V7p5hM7NcEVCcP1+kT5Kij9wuVSAljK/B/S6exKNq2Tpe47FgmNruz/GbVYDzry\nHiT7t/O6dmwYQLCiEDRRZxPFgicsYwJOmMcCAwEAAaOBozCBoDAfBgNVHSMEGDAW\ngBR1kfKc6tS556OlX3eE26EaDRiLpTAdBgNVHQ4EFgQUoEg7YDv5Gi7xKhM+9477\nlpCroCUwDgYDVR0PAQH/BAQDAgH+MAwGA1UdEwEB/wQCMAAwQAYDVR0fBDkwNzA1\noDOgMYYvaHR0cDovLzExOC4yMTkuNTUuMTM5OjQ4MjAwL0NSTC9JTklURUNIQ0Ex\nMy5jcmwwDQYJKoZIhvcNAQELBQADggEBACUix299qiLlQIkLhQRidaMVEnAMJxdV\nBpA3QkutMaXpDHCpvbVNlNFsRQ0iYNQvKSn+DP1slEf0p7konNCyCBqBwn4c3U6q\nlg+09RpIQAkik6TeqJ1uhb2bp/FkPKAF+rtUvFrJleVp5ffgv3I7MYpSdlQcXHqb\n8fNY+iGHsNd6hiDSYXL8XB/US+kUohh7M1neYr2PTUoPsEI97xVWNUVimdV9iDA4\nqPr133gzvS68GfKvTxgU10yMOGPfdk+pBwTvG/BWobBsRB/bnU04TXIeCjI1BCo0\nkwi0rERgebv4zdYSRBU7v1tdZHvgM0+7rbM/WA86h0OSyh5ZVbOcCfM=\n-----END CERTIFICATE-----\n";

	public final static String PROJECT_NUMBER = "320210788890";	// GCM의 project number

	public final static Boolean enableScreenShot = false;	// 스크린샷 가능 여부. 운영시 false!!!
	public final static Boolean usingDirectCall = false; 	// 전화 링크시에 바로 전화를 걸 것인지 여부. false면 다이얼창이 뜬다.
	public final static Boolean useShortcut	= false; 		// 최초구동시 바로가기 생성여부.
	
	/* CALLGATE 설정 */
	public final static String LAUNCHER_ID = "[welloan]";	// Launcher ID 사전 정의
	public final static String CONFIG_FILE_URL = "https://mfcc.co.kr/cfg/saas.cfg";
	public final static String CALLGATE_SENDER_ID = "364152695002";
	
	
	
	private HashMap <String, CabBankXComponent> mComponents; //컴포넌트를 저장해 놓을 HashMap
	private Activity mActivity = null; //해당 설정을 사용하는 액티비티
	
	public CabBankAppProperty(Activity activity)
	{
		mActivity = activity;
		mComponents = new HashMap<String, CabBankXComponent>();
		this.initializeComponents();
		
		checkShortcut();
	}

	public CabBankAppProperty(Activity activity, String homeURL)
	{
		mHomeURL = homeURL;
		mActivity = activity;
		mComponents = new HashMap<String, CabBankXComponent>();
		this.initializeComponents();
		
		checkShortcut();
	}
	
	/***
	 * home url을 설정한다. 
	 * @param url home url
	 */
	public void setHomeURL(String url) {
		this.mHomeURL = url;
	}
	
	/***
	 * 설정한 home url을 반환한다.
	 * @return home url
	 */
	public String getHomeURL()
	{
		return mHomeURL;
	}

	/***
	 * 에러 페이지에 대한 url을 반환한다. (네트워크가 끊겼을 때 보여주는 page. 위치가 native에 있음.)
	 * @return
	 */
	public String getErrorURL()
	{
		return mErrorURL;
	}
	
	
	/**
	 * 컴포넌트를 생성하여 HashMap에 초기화하여 넣어놓는다.
	 */
	private void initializeComponents()
	{
		// 기본 컴포넌트
		mComponents.put(CabBasicComponent.COMPONENT_KEY, new CabBasicComponent(mActivity, mHomeURL));
		
		// 앱.위변조 방지 컴포넌트 (AppProtect)
		mComponents.put(CabAppProtectComponent.COMPONENT_KEY, new CabAppProtectComponent(mActivity));
		
		// 모바일 백신 (DroidX)
		mComponents.put(CabDroidXComponent.COMPONENT_KEY, new CabDroidXComponent(mActivity));
		
		// 보안 키패드
		mComponents.put(CabNFilterComponent.COMPONENT_KEY, new CabNFilterComponent(mActivity));
		
		//기존의 inisafe 대신에 nFilter가 연계된 컴포넌트를 연결한다.
		//mComponents.put(CabInisafeComponent.COMPONENT_KEY, new CabInisafeComponent(mActivity));
		CabInisaFilterComponents component = null;
		component = new CabInisaFilterComponents(mActivity, 
				(CabNFilterComponent) getComponents(CabNFilterComponent.COMPONENT_KEY));
		component.setCACert(CACert);
		component.setSCert(SCert);
		
		mComponents.put(CabInisaFilterComponents.COMPONENT_KEY, component);
		
		// Photo swipe 연동용 컴포넌트.
		mComponents.put(CabPhotoSwipeComponent.COMPONENT_KEY, new CabPhotoSwipeComponent(mActivity));
		
		
		// GCM 컴포넌트
		mComponents.put(CabNotiComponent.COMPONENT_KEY, new CabNotiComponent(mActivity, PROJECT_NUMBER));
		

		// 가계약 컴포넌트
		mComponents.put(CabDContractComponent.COMPONENT_KEY, new CabDContractComponent(mActivity));
	}
	
	/**
	 * compName에 해당되는 컴포넌트를 불러온다.
	 * @param compName 불러올 컴포넌트의 키
	 * @return compName에 해당되는 컴포넌트
	 */
	public CabBankXComponent getComponents(String compName)
	{
		return mComponents.get(compName);
	}
	
	/**
	 * 컴포넌트 목록을 ArrayList로 반환한다.
	 * @return ArrayList로 변환된 컴포넌트 목록
	 */
	public ArrayList<CabBankXComponent> getComponentsList()
	{
		ArrayList<CabBankXComponent> list = new ArrayList<CabBankXComponent>();
		Set<String> keys = mComponents.keySet();
		for(String key : keys)
		{
			list.add(mComponents.get(key));
		}
		
		return list;
	}
	
	/**
	 * 바로가기가 생성되었는지 여부를 체크한 뒤 생성 안 되었으면 Alert을 띄워준다.
	 */
	private void checkShortcut()
	{
		if(CabBankAppProperty.useShortcut){
			String packageName = mActivity.getApplicationContext().getPackageName();
			SharedPreferences pref = mActivity.getSharedPreferences(packageName, Context.MODE_PRIVATE);
			boolean createShortcut = pref.getBoolean("shortcut", false);
			if(!createShortcut){
				showCreateShortcutAlert(pref);
			}
		}
	}
	
	/**
	 * 바로가기 생성여부를 묻는 Alert을 띄워준다.
	 * @param pref 바로가기 생성여부가 저장된 SharedPreference
	 */
	private void showCreateShortcutAlert(final SharedPreferences pref){
		new AlertDialog.Builder(mActivity).setTitle(CabR.getStringId(mActivity, "app_name"))
		.setMessage(CabR.getStringId(mActivity, "shortcut_create"))
		.setNegativeButton(CabR.getStringId(mActivity, "no"), null)
		.setPositiveButton(CabR.getStringId(mActivity, "yes"), new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent shortcutIntent = new Intent(mActivity.getApplicationContext(), mActivity.getClass()); //바로가기 실행 Intent
				shortcutIntent.setAction(Intent.ACTION_MAIN);
				
				Intent addIntent = new Intent();
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, mActivity.getString(CabR.getStringId(mActivity, "app_name")));	//바로가기 이름은 app이름과 동일하게 맞춘다.
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, 
						Intent.ShortcutIconResource.fromContext(mActivity.getApplicationContext(), CabR.getDrawableId(mActivity, "ic_launcher"))); //app아이콘을 사용한다.
				addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
				mActivity.getApplicationContext().sendBroadcast(addIntent); //생성.
			}
		}).show();
		
		//바로가기를 생성했든 안 했든 일단은 생성시도는 한 것으로 처리해서 다음부터는 뜨지 않도록 해준다.
		Editor editor = pref.edit();
		editor.putBoolean("shortcut", true);
		editor.commit();
	}
}
