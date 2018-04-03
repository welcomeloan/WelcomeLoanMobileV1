package com.cabsoft.rainbowbr.activity;

import android.app.Application;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.pacesystem.lib.RecognitionResult;

public class BaseApplication extends MultiDexApplication {

	private RecognitionResult recognitionResult;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d("lenddo", "BaseApplication on create call");
		super.onCreate();
	}

	public RecognitionResult getRecognitionResult() {
		return recognitionResult;
	}

	public void setRecognitionResult(RecognitionResult recognitionResult) {
		this.recognitionResult = recognitionResult;
	}
}
