package com.linkprice.app_interlock;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class Lpfront {
	public static boolean LP_DEBUG = true;
	
	// receiver에서 LPINFO를 저장합니다.
	public static void setReceiver(Context context, Intent intent) throws UnsupportedEncodingException {
		
		SharedPreferences mPref = getPref(context);
		SharedPreferences.Editor prefEditor = mPref.edit();
		String referrer = null;
		
		Map<String, String> referrerParse = new LinkedHashMap<String, String>();
		
		if (LP_DEBUG) {
			Log.e("linkprice", "Referrer is - " + referrer);
		}

		Bundle extras = intent.getExtras();		
		if ((extras != null)) {
			referrer = extras.getString("referrer");
			if (LP_DEBUG) {
				Log.d("linkprice", "Referrer is - " + referrer);
			}

			referrerParse = parseQuery(referrer);
			
			prefEditor.putString( "referrer", referrer );
			prefEditor.putString( "LPINFO", referrerParse.get("LPINFO") );

			//rd값이 숫자로 안들어올때 앱이 죽는 현상이 있어 수정.jjc
			String rd = referrerParse.get("rd") == null?"-1":referrerParse.get("rd");
			prefEditor.putInt( "rd", Integer.parseInt(rd) );

	        prefEditor.apply();

	        if (LP_DEBUG) {
	        	Log.d("linkprice", "Referrer is - " + referrer);
	        	Log.d("linkprice", "LPINFO is - " + referrerParse.get("LPINFO"));
	        	Log.d("linkprice", "rd is - " + referrerParse.get("rd"));
	        }
		} else {
			return;
		}
	}
	
	public static Map<String, String> parseQuery(String query)
			throws UnsupportedEncodingException {
		
		String queryDecoded = URLDecoder.decode(query, "UTF-8");

		Map<String, String> query_pairs = new LinkedHashMap<String, String>();

		String[] pairs = queryDecoded.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			query_pairs.put(pair.substring(0, idx), pair.substring(idx + 1));
		}
		
		return query_pairs;
	}
	
	// activity에서 LPINFO를 저장합니다.
	public static void setActivity(Context context, Intent intent) {
		
		SharedPreferences mPref = getPref(context);
		SharedPreferences.Editor prefEditor = mPref.edit();
		
		Uri data = intent.getData();
		if (null != data) {
			String lpinfo = data.getQueryParameter("LPINFO");
			if (LP_DEBUG) {
				Log.e("linkprice", "LPINFO - " + lpinfo);
			}
			if (lpinfo != null) {
				prefEditor.putString( "LPINFO", lpinfo );
			}
			
			int rd;
			
			if (data.getQueryParameter("rd") != null) {
				rd = Integer.parseInt(data.getQueryParameter("rd"));
				prefEditor.putInt( "rd", rd );
			} else {
				rd = 0;
			}
			if (LP_DEBUG) {
				Log.d("linkprice", "rd - " + rd);
			}
			
	        prefEditor.apply();
		}
	}
	
	public static SharedPreferences getPref(Context context) {
		SharedPreferences mPref = context.getSharedPreferences( "Linkprice", Context.MODE_PRIVATE );
		
		return mPref;
	}
}
