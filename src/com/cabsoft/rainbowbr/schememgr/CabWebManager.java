package com.cabsoft.rainbowbr.schememgr;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

import com.cabsoft.convenience.CabR;
import com.cabsoft.rainbowbr.components.CabBankXComponent;
import com.cabsoft.rainbowbr.property.CabBankAppProperty;
import com.google.gson.Gson;


/**
 * 웹의 스키마 제어 및 컴포넌트 호출을 맡는다.
 * @author pys
 *
 */
public class CabWebManager {
	
	final static int ERROR_CODE_UNKNOWN_COMPONENT = -9000;
	final static int ERROR_CODE_UNKNOWN_FUNCTION = -9001;
	
	CabBankAppProperty mProperty = null;
	Activity mActivity = null;
	CabParserURLAdapter mAdapter = null;
	
	public CabWebManager(Activity acitivty)
	{
		mActivity = acitivty;
		mProperty = new CabBankAppProperty(acitivty);
		mAdapter = new CabParserURLAdapter();
	}
	
	public CabWebManager(Activity acitivty, String homeURL)
	{
		mActivity = acitivty;
		
		if (homeURL == null) {
			mProperty = new CabBankAppProperty(acitivty);
		} else {
			mProperty = new CabBankAppProperty(acitivty, homeURL);
		}
		
		mAdapter = new CabParserURLAdapter();
	}
	
	public CabWebManager(Activity acitivty, String homeURL, Map <String, Object> param)
	{
		mActivity = acitivty;
		
		if (homeURL == null) {
			mProperty = new CabBankAppProperty(acitivty);
		} else {
			mProperty = new CabBankAppProperty(acitivty, homeURL);
		}
		
		mAdapter = new CabParserURLAdapter();

		/*
//		하단의 로직은 저축은행 중앙회용. 
//		 - 각 저축은행이 메인 인트로를 가지며, 네트워크 오류 시 각 저축은행 메인으로 가야 하므로 param을 같이 
//	     homeURL로 물려놔야 한다.
		if (param != null) {
			
			StringBuilder strb = new StringBuilder();
			boolean isStart = true;
			for (String key : param.keySet()) {
				if(!isStart){
					strb.append("&");
				}
				strb.append(key).append('=').append(param.get(key));
				isStart = false;
			}
			
			if (strb.length() != 0) {
				mProperty.setHomeURL(mProperty.getHomeURL() + "?" + strb.toString());
			}
			
			strb = null;
			
		}*/
		
	}
	
	public void callHomeURL(WebView targetWebview)
	{
		this.webPageWillChage(mProperty.getHomeURL());
		targetWebview.loadUrl(mProperty.getHomeURL());
	}
	
	public void callHomeURL(WebView targetWebview, Map <String, Object> param) {
		
		this.webPageWillChage(mProperty.getHomeURL());
		
		// 저축은행 중앙회는 아래 로직을 실행하지 않고 
		// targetWebview.loadUrl(mProperty.getHomeURL()); 만 실행할 것!
		
		StringBuilder strb = new StringBuilder();
		
		if (param != null) {
			
			boolean isStart = true;
			for (String key : param.keySet()) {
				if(!isStart){
					strb.append("&");
				}
				strb.append(key).append('=').append(param.get(key));
				isStart = false;
			}
					
		}

		if (strb.length() != 0) {
			targetWebview.loadUrl(mProperty.getHomeURL() + "?" + strb.toString());
		} else {
			targetWebview.loadUrl(mProperty.getHomeURL());
		}
		
		strb = null;
		
	}
	
	public void callErrorURL(WebView targetWebview)
	{
		this.webPageWillChage(mProperty.getErrorURL());
		targetWebview.loadUrl(mProperty.getErrorURL());
	}
	
	/**
	 * URL을 체크하여 내부호출 스키마일 경우 내부 컴포넌트 함수를 호출한다.
	 * @param url 체크할 URL
	 * @param webview 해당 URL을 호출한 웹뷰
	 * @return true-내부호출 스키마 false-일반 링크 URL
	 */
	@SuppressWarnings("unchecked")
	public Boolean checkScheme(String url, WebView webview)
	{
		final WebView web = webview;
		mAdapter.parseURL(url);

		//먼저 스키마가 내부에서 처리하는 스키마인 지를 체크한다.
		if(CabBankAppProperty.mScheme.equals(mAdapter.getScheme()))
		{
			
			// set 관련 action들을 몰아서 처리.
			if ("util".equals(mAdapter.getDomain())) {
				
				if ("setActionsCommit".equals(mAdapter.getPath())) {
					
					Map <String, Object> mapComponents = (Map <String, Object>) mAdapter.getParam();
					
					for (String component : mapComponents.keySet()) {
						
						Map <String, Object> mapActions = (Map <String, Object>) mapComponents.get(component);
						
						for (String action : mapActions.keySet()) {
							
							runClassMethods(component, action, (HashMap<String, String>) mapActions.get(action), web);
							
						}

					}
				}

				return true;
				
			}
			
			
			//처리시간이 늦어지는 것을 막기 위하여, UI쓰레드로 던져버린다.
			mActivity.runOnUiThread(new Runnable() {
				public void run() {
					if(mAdapter.getDomain() != null && mAdapter.getPath() != null)
					{
						runClassMethods(mAdapter.getDomain(), mAdapter.getPath(), (HashMap<String, String>) mAdapter.getParam(), web);
					}
				}
			});
			
			return true;
		}
		/*폰콜 */
		if(url.startsWith("tel:")){
			Intent intent = null;
			if(CabBankAppProperty.usingDirectCall){
				intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
			}else{
				intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
			}
			mActivity.startActivity(intent);
			return true;
		}
		
		/*동영상 재생 처리 부분.*/
		if(url.endsWith(".mp4")){
			try{
				Intent movieIntent = new Intent(Intent.ACTION_VIEW);
				Uri uri = Uri.parse(url);
				movieIntent.setDataAndType(uri, "video/mp4");
				mActivity.startActivity(movieIntent);
			}catch (Exception e){
				new AlertDialog.Builder(mActivity)
				.setTitle(CabR.getStringId(mActivity, "notice"))
	            .setMessage(CabR.getStringId(mActivity, "movie_error"))
	            .setPositiveButton(CabR.getStringId(mActivity, "ok"), null)
	            .create()
	            .show();
			}
			return true;
		}
		/*동영상 재생 처리 부분 완료.*/
		

		// 각 컴포넌트에게 url이 바뀜을 통지한다.
		if ("http".equals(mAdapter.getScheme())  || 
			"https".equals(mAdapter.getScheme()) ||
			"file".equals(mAdapter.getScheme())) {
			
			this.webPageWillChage(url);

		}
		
		return false;
	}
	
	/**
	 * 내부호출 스키마를 받아서 해당 컴포넌트의 메소드를 호출한다.
	 * @param className 호출할 컴포넌트명
	 * @param methods 호출할 메소드명
	 * @param param 전달할 parameter값
	 * @param webview 해당 URL을 호출한 웹뷰
	 */
	private void runClassMethods(String className, String methods, HashMap<String, String> param, WebView webview)
	{
		CabBankXComponent component = mProperty.getComponents(className);
		if(component != null)
		{
			try {
				Method method = component.getClass().getMethod(methods, param.getClass(), webview.getClass());
				method.invoke(component, param, webview);
			} catch (NoSuchMethodException e) {
				sendErrorCallback(param, ERROR_CODE_UNKNOWN_FUNCTION, webview);
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				sendErrorCallback(param, ERROR_CODE_UNKNOWN_FUNCTION, webview);
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				sendErrorCallback(param, ERROR_CODE_UNKNOWN_FUNCTION, webview);
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				sendErrorCallback(param, ERROR_CODE_UNKNOWN_COMPONENT, webview);
				e.printStackTrace();
			}
		}else{
			sendErrorCallback(param, ERROR_CODE_UNKNOWN_COMPONENT, webview);
		}
	}
	
	private void sendErrorCallback(HashMap<String, String> param, final int nErrorCode, final WebView webView){
		final String insName = param.get("instanceName");
		final String callName = param.get("callbackName");
		
		if(insName != null && callName != null){
			webView.post(new Runnable() {
				public void run() {
					String format = "javascript:" + insName + "." + callName + "(%s);";
				
					Map <String, Object> data = new HashMap<String, Object>();
					data.put("ret", nErrorCode);
					
					webView.loadUrl(String.format(format, (new Gson()).toJson(data)));
				}
			});
		}
	}
	
	/**
	 * Activity의 onActivityResult를 각 컴포넌트로 전달한다.
	 * @param requestCode 리퀘스트 코드
	 * @param resultCode 응답코드
	 * @param data Intent데이터
	 * @param webview 웹뷰
	 * @return true-컴포넌트에서 처리, false-해당 메소드를 처리할 컴포넌트가 없음
	 */
	public Boolean onActivityResult(int requestCode, int resultCode, Intent data, WebView webview)
	{
		ArrayList<CabBankXComponent> componentsList = mProperty.getComponentsList();
		Boolean returnValue = false;
		
		for(CabBankXComponent component : componentsList)
		{
			if(component.onActivityResult(requestCode, resultCode, data, webview)){
				returnValue = true;
			}
		}
		
		return returnValue;
	}
	
	/**
	 * Activity에서 백키가 눌렸을때의 이벤트를 각 컴포넌트로 전달한다.
	 * @param webview 웹뷰
	 * @return true-컴포넌트에서 처리, false-해당 메소드를 처리할 컴포넌트가 없음
	 */
	public Boolean onBackPressed(WebView webview) {
		ArrayList<CabBankXComponent> componentsList = mProperty.getComponentsList();
		Boolean returnValue = false;
		for(CabBankXComponent component : componentsList)
		{
			if(component.onBackPressed(webview)){
				returnValue = true;
			}
		}
		
		return returnValue;
	}
	
	private void webPageWillChage(String url) {
		
		for (CabBankXComponent component : mProperty.getComponentsList()) {
			component.changeWebPageUrl(url);
		}
		
	}

	public CabBankXComponent getComponents(String compName)
	{
		return mProperty.getComponents(compName);
	}
	

}
