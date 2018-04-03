package com.cabsoft.convenience;

import android.util.Log;

/**
 * 로그 확장기능.
 * <p>2014.06.30
 * @version 1.0
 * @authorPark Park Youngsu
 */
public class CSLog {
	/**
     * 호출된 클래스명과 함수명을 호출
     * @return [클래스명.함수명(라인수)]\n
     */
	private static String GetClassFuncAndLineString()
	{
		//0:VMStack.getThreadStackTrace
		//1:Thread.getStackTrace
		//2:CSLog.GetClassFuncAndLineString //This Methods
		//3:CSLog. LogMethods
		//4:Target Method
		String fullClassName = Thread.currentThread().getStackTrace()[4].getClassName();            
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = Thread.currentThread().getStackTrace()[4].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[4].getLineNumber();
        
        return "["+className+"."+methodName+"("+lineNumber+")]\n";
	}
	
	/**
     * Send a {@link #VERBOSE} log message.
     * @param tag 태그
     * @param msg 출력할 메세지
     */
	public static void v(String tag, String msg)
	{
		String fullMsg = CSLog.GetClassFuncAndLineString() + msg;
		while(fullMsg.length() > 4000){
			Log.v(tag, fullMsg.substring(0, 4000));
			fullMsg = fullMsg.substring(4000);
		}
		Log.v(tag, fullMsg);
	}
	
	/**
     * Send a {@link #DEBUG} log message.
     * @param tag 태그
     * @param msg 출력할 메세지
     */
	public static void d(String tag, String msg)
	{
		String fullMsg = CSLog.GetClassFuncAndLineString() + msg;
		while(fullMsg.length() > 4000){
			Log.d(tag, fullMsg.substring(0, 4000));
			fullMsg = fullMsg.substring(4000);
		}
		Log.d(tag, fullMsg);
	}
	
	/**
     * Send an {@link #INFO} log message.
     * @param tag 태그
     * @param msg 출력할 메세지
     */
	public static void i(String tag, String msg)
	{
		String fullMsg = CSLog.GetClassFuncAndLineString() + msg;
		while(fullMsg.length() > 4000){
			Log.i(tag, fullMsg.substring(0, 4000));
			fullMsg = fullMsg.substring(4000);
		}
		Log.i(tag, fullMsg);
	}
	
	/**
     * Send an {@link #WARN} log message.
     * @param tag 태그
     * @param msg 출력할 메세지
     */
	public static void w(String tag, String msg)
	{
		String fullMsg = CSLog.GetClassFuncAndLineString() + msg;
		while(fullMsg.length() > 4000){
			Log.w(tag, fullMsg.substring(0, 4000));
			fullMsg = fullMsg.substring(4000);
		}
		Log.w(tag, fullMsg);
	}
	
	/**
     * Send an {@link #ERROR} log message.
     * @param tag 태그
     * @param msg 출력할 메세지
     */
	public static void e(String tag, String msg)
	{
		String fullMsg = CSLog.GetClassFuncAndLineString() + msg;
		while(fullMsg.length() > 4000){
			Log.e(tag, fullMsg.substring(0, 4000));
			fullMsg = fullMsg.substring(4000, fullMsg.length()-1);
		}
		Log.e(tag, fullMsg);
	}
	
	/**
     * Send a {@link #VERBOSE} log message.
     * @param tag 태그
     * @param format 메세지 스트링 포맷
     * @param args 메세지 스트링 포맷 변수
     */
	public static void v(String tag, String format, Object... args)
	{
		String msg = String.format(format, args);
		String fullMsg = CSLog.GetClassFuncAndLineString() + msg;
		while(fullMsg.length() > 4000){
			Log.v(tag, fullMsg.substring(0, 4000));
			fullMsg = fullMsg.substring(4000);
		}
		Log.v(tag, fullMsg);
	}
	
	/**
     * Send a {@link #DEBUG} log message.
     * @param tag 태그
     * @param format 메세지 스트링 포맷
     * @param args 메세지 스트링 포맷 변수
     */
	public static void d(String tag, String format, Object... args)
	{
		String msg = String.format(format, args);
		String fullMsg = CSLog.GetClassFuncAndLineString() + msg;
		while(fullMsg.length() > 4000){
			Log.d(tag, fullMsg.substring(0, 4000));
			fullMsg = fullMsg.substring(4000);
		}
		Log.d(tag, fullMsg);
	}
	
	/**
     * Send a {@link #INFO} log message.
     * @param tag 태그
     * @param format 메세지 스트링 포맷
     * @param args 메세지 스트링 포맷 변수
     */
	public static void i(String tag, String format, Object... args)
	{
		String msg = String.format(format, args);
		String fullMsg = CSLog.GetClassFuncAndLineString() + msg;
		while(fullMsg.length() > 4000){
			Log.i(tag, fullMsg.substring(0, 4000));
			fullMsg = fullMsg.substring(4000);
		}
		Log.i(tag, fullMsg);
	}
	
	/**
     * Send a {@link #WARN} log message.
     * @param tag 태그
     * @param format 메세지 스트링 포맷
     * @param args 메세지 스트링 포맷 변수
     */
	public static void w(String tag, String format, Object... args)
	{
		String msg = String.format(format, args);
		String fullMsg = CSLog.GetClassFuncAndLineString() + msg;
		while(fullMsg.length() > 4000){
			Log.w(tag, fullMsg.substring(0, 4000));
			fullMsg = fullMsg.substring(4000);
		}
		Log.w(tag, fullMsg);
	}
	
	/**
     * Send a {@link #ERROR} log message.
     * @param tag 태그
     * @param format 메세지 스트링 포맷
     * @param args 메세지 스트링 포맷 변수
     */
	public static void e(String tag, String format, Object... args)
	{
		String msg = String.format(format, args);
		String fullMsg = CSLog.GetClassFuncAndLineString() + msg;
		while(fullMsg.length() > 4000){
			Log.e(tag, fullMsg.substring(0, 4000));
			fullMsg = fullMsg.substring(4000);
		}
		Log.e(tag, fullMsg);
	}
}
