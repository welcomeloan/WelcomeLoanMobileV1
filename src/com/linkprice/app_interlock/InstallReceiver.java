package com.linkprice.app_interlock;

// adb shell am broadcast -a com.android.vending.INSTALL_REFERRER -n com.linkprice.app_interlock/.InstallReceiver --es "referrer" "LPINFO%3DA100511865%7C2397216200014E%7C0000%7CB%7C1%26rd%3D30"

import java.io.UnsupportedEncodingException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class InstallReceiver extends BroadcastReceiver {
	public InstallReceiver() {
	}

	// LPINFO를 저장합니다.
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Lpfront.setReceiver(context, intent);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
