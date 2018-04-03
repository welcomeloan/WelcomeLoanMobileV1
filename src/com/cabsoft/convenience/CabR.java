package com.cabsoft.convenience;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * 본래 android 리소스 관리 클래스는 어플리케이션의 패키지에 종속되므로,
 * 어플리케이션의 패키지명을 바꿀 때 마다 리소스에 관련된 패키지명을 전부 바꿔줘야 된다.
 * 리소스를 사용하는 패키지가 늘어날 수록 수정되어야 될 부분이 많아지기 때문에,
 * 패키지 관련 종속을 끊기 위해 이 클래스를 이용해서 리소스를 호출하도록 한다.
 * @author yspark
 *
 */
public class CabR {
	
	public static String getAppPackageName(Context context){
		return context.getApplicationContext().getPackageName();
	}
	
	public static Resources getResources(Context context){
		return context.getResources();
	}
	
	public static int getLayoutId(Context context, String resourceName){
		String packageName = CabR.getAppPackageName(context);
		return CabR.getResources(context).getIdentifier(resourceName, "layout", packageName);
	}
	
	public static int getId(Context context, String resourceName){
		String packageName = CabR.getAppPackageName(context);
		return CabR.getResources(context).getIdentifier(resourceName, "id", packageName);
	}
	
	public static int getDrawableId(Context context, String resourceName) {
		String packageName = CabR.getAppPackageName(context);
		return CabR.getResources(context).getIdentifier(resourceName, "drawable", packageName);
	}
	
	public static int getAnimId(Context context, String resourceName) {
		String packageName = CabR.getAppPackageName(context);
		return CabR.getResources(context).getIdentifier(resourceName, "anim", packageName);
	}
	
	public static int getStringId(Context context, String resourceName) {
		String packageName = CabR.getAppPackageName(context);
		return CabR.getResources(context).getIdentifier(resourceName, "string", packageName);
	}
	
	public static Drawable getDrawable(Context context, String resourceName) {
		int resourceId = CabR.getDrawableId(context, resourceName);
		return CabR.getResources(context).getDrawable(resourceId);
	}
	
	public static String getString(Context context, String resourceName) {
		int resourceId = CabR.getStringId(context, resourceName);
		return CabR.getResources(context).getString(resourceId);
	}
}
