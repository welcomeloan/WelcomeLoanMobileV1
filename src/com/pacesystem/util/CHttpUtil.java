package com.pacesystem.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import android.os.AsyncTask;

//String strCheck = new CHttpUtil().execute(URL_CHECK, strCheckParam).get().trim();
public class CHttpUtil extends AsyncTask<String, Void, String> {

	public String DownloadHtml(String addr, String strPostParam, String strEnc) {
		StringBuilder html = new StringBuilder(); 
		
		HttpURLConnection conn = null; 
		BufferedReader br = null;
		BufferedWriter bw = null;

		try {
			URL url = new URL(addr);
            
            if (url.getProtocol().toLowerCase().equals("https")) { 
                trustAllHosts(); 
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection(); 
                https.setHostnameVerifier(DO_NOT_VERIFY); 
                conn = https; 
            } else { 
            	conn = (HttpURLConnection) url.openConnection(); 
            } 
			
			if (conn != null) {

				conn.setConnectTimeout(3000);
				conn.setReadTimeout(10000);
				conn.setUseCaches(false);

				// POST
				if(strPostParam != null) {
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					try {
						bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
						bw.write(strPostParam);
						
						bw.flush();
					} finally {
						if(bw != null) bw.close();
					}
				}

				int resultcode = conn.getResponseCode();
				
				if (resultcode == HttpURLConnection.HTTP_OK) {
					
					try {
						br = new BufferedReader(new InputStreamReader(conn.getInputStream(), strEnc));
						for (;;) {
							String line = br.readLine();
							if (line == null) break;
							html.append(line + '\n'); 
						}				
					} finally {
						if(br != null) br.close();
					}
				}
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			
			return ex.getMessage();
		} finally {
			if(conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		
		return html.toString();
	}
    
    private void trustAllHosts() { 
        // Create a trust manager that does not validate certificate chains 
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() { 
                public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
                        return new java.security.cert.X509Certificate[] {}; 
                } 
 
                @Override 
                public void checkClientTrusted( 
                        java.security.cert.X509Certificate[] chain, 
                        String authType) 
                        throws java.security.cert.CertificateException { 
                    // TODO Auto-generated method stub 
                     
                } 
 
                @Override 
                public void checkServerTrusted( 
                        java.security.cert.X509Certificate[] chain, 
                        String authType) 
                        throws java.security.cert.CertificateException { 
                    // TODO Auto-generated method stub 
                     
                } 
        } }; 
 
        // Install the all-trusting trust manager 
        try { 
                SSLContext sc = SSLContext.getInstance("TLS"); 
                sc.init(null, trustAllCerts, new java.security.SecureRandom()); 
                HttpsURLConnection 
                                .setDefaultSSLSocketFactory(sc.getSocketFactory()); 
        } catch (Exception e) { 
                e.printStackTrace(); 
        } 
    } 
     
    final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() { 
        
		@Override
		public boolean verify(String arg0, SSLSession arg1) {
			// TODO Auto-generated method stub
			return true;
		} 
    };

	@Override
	protected String doInBackground(String... paramArrayOfParams) {
		
		String strUrl = null;
		String strParam = null;
		String strEnc = "utf-8";
		
		strUrl = paramArrayOfParams[0];
		if(paramArrayOfParams.length == 2 )  strParam = paramArrayOfParams[1];
		if(paramArrayOfParams.length == 3 )  strEnc = paramArrayOfParams[2];
		
		return this.DownloadHtml(strUrl, strParam, strEnc);
	} 
	
}

