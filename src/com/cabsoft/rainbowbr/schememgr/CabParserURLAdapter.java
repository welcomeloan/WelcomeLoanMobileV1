package com.cabsoft.rainbowbr.schememgr;

import java.util.HashMap;

import com.cabsoft.parser.CabParserTypeURL;

public class CabParserURLAdapter {
	CabParserTypeURL mURLParser;
	HashMap<String, Object> mURLParsedData;
	
	public CabParserURLAdapter() {
		mURLParser = new CabParserTypeURL();
	}
	
	@SuppressWarnings("unchecked")
	public void parseURL(String url){
		mURLParsedData = (HashMap<String, Object>) mURLParser.excute(url);
	}
	
	public String getScheme()
	{
		if(mURLParsedData == null){
			return null;
		}
		return (String) mURLParsedData.get("scheme");
	}
	
	public String getDomain()
	{
		if(mURLParsedData == null){
			return null;
		}
		return (String) mURLParsedData.get("domain");
	}
	
	public String getPath()
	{
		if(mURLParsedData == null){
			return null;
		}
		return (String) mURLParsedData.get("path");
	}
	
	public Object getParam()
	{
		if(mURLParsedData == null){
			return null;
		}
		return mURLParsedData.get("param");
	}
}
