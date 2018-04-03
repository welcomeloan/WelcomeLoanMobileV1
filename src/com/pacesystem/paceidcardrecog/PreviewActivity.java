package com.pacesystem.paceidcardrecog;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.welcomeloan.mobile.R;
import com.cabsoft.rainbowbr.activity.BaseApplication;
import com.pacesystem.lib.PaceCameraPreview;
import com.pacesystem.lib.PaceDetectionListener;
import com.pacesystem.lib.PaceIdCardRecognition;
import com.pacesystem.lib.RecognitionResult;



public class PreviewActivity extends Activity
{
	
	
	public static final int REQ_MANUAL_DETECTION = 0;
	
	public static final String RES_CARD_TYPE = "res_card_type";
	public static final String RES_CARD_IMG = "res_card_img";
	public static final String RES_NAME = "res_name";
	public static final String RES_ID_NUMBER = "res_id_number";
	public static final String RES_RECOGNITION_RESULT = "res_recognition_result";

	private static final int CNT_JUMIN_CHECK = 2;
	
	private int mManualDelay = 0;
	
	private Timer mManualTimer;
	
    private PaceCameraPreview m_cameraPreview;
    private TextView m_textview;

    private ProgressBar m_progressBar;

    private boolean m_bFinish = false;

    private Button m_btnFlash;
    private Button m_btnShotter;

    private String m_documentPath;
    
    private boolean mCameraManual = false;
    
    private int mJuminFailCount = 0;
    private int mShotterCount = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        Bundle extra = getIntent().getExtras();

        if(extra != null) {
        	mManualDelay = extra.getInt("menualDelay", 30000);
        }

        String cachePath = getCacheDir().getAbsolutePath();
        m_documentPath = cachePath + File.separator + "RecogImage";
        File fDocument = new File(m_documentPath);
        if(fDocument.exists() == false)
        {
        	fDocument.mkdirs();
        }
        
        m_cameraPreview = (PaceCameraPreview)findViewById(R.id.id_preview_view_camerapreview);
        m_textview = (TextView)findViewById(R.id.id_preview_textview_error);
        m_progressBar = (ProgressBar)findViewById(R.id.id_preview_progressbar);

        m_btnFlash = (Button)findViewById(R.id.id_preview_button_flash);
        m_btnShotter = (Button)findViewById(R.id.id_preview_button_shutter);
        
        m_btnShotter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
        	
            	if(m_cameraPreview.manualTakePicture()){
            		mShotterCount++;
            	}
            	
        	}
        });
        
        m_btnShotter.setVisibility(View.INVISIBLE);

       
        m_cameraPreview.setOnDetectedResult(new PaceDetectionListener.DetectedResult()
        {
            @Override
            public void onStart()
            {
                m_progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish(boolean success)
            {

            }

            @Override
            public void onCompleted(boolean success, String detectedImagePathName)
            {

            	if(success) {	// 영역 인식 성공
            		
            		RecognitionResult recognitionResult = recognition(detectedImagePathName);

            		Log.w("PreviewActivity", recognitionResult.result + ", " + recognitionResult.type + ", " + recognitionResult.name + ", " + recognitionResult.idNumber
            			+ ", " + recognitionResult.publish	+ ", " + recognitionResult.licenseNumber + ", " + recognitionResult.licenseCheckDigit);
  
                    if(recognitionResult.result && (recognitionResult.type == 1 || recognitionResult.type == 3)) {
                    	
                    	m_progressBar.setVisibility(View.GONE);

                    	BaseApplication baseApp = (BaseApplication) getApplication();
        				baseApp.setRecognitionResult(recognitionResult);
        				 
                        setResult(RESULT_OK);
                        
                        finish();
   
                    } else {
                    	
                    	if(mCameraManual && mShotterCount > 0) {
                    		setResult(RESULT_CANCELED);
                        	finish();
                    		
                    	} else {
                    		Toast.makeText(getApplicationContext(), "주민번호 인식에 실패 하여 재촬영 합니다.", Toast.LENGTH_SHORT).show();
                        	m_progressBar.setVisibility(View.GONE);
                        	m_cameraPreview.startCamera();
                    	}

                    	/*
                    	if(CNT_JUMIN_CHECK > mJuminFailCount) {
                    		mJuminFailCount++;
                    		
	                    	Toast.makeText(getApplicationContext(), "주민번호 인식에 실패 하여 재촬영 합니다.", Toast.LENGTH_SHORT).show();
	                    	m_progressBar.setVisibility(View.GONE);
	                    	m_cameraPreview.startCamera();
                    	
                    	} else {
                    		Toast.makeText(getApplicationContext(), "주민번호 인식에 " + CNT_JUMIN_CHECK + "번 실패 하여 종료 합니다.", Toast.LENGTH_LONG).show();
                    		setResult(RESULT_CANCELED);
                        	finish();
                    	}
                    	*/

                    }

            	} else if (mCameraManual && mShotterCount > 0) {
            		
            		m_progressBar.setVisibility(View.GONE);
                	Intent intent =  new Intent(PreviewActivity.this, ManualDetectionActivity.class);
                	intent.putExtra("src_image_path", detectedImagePathName);

                	startActivityForResult(intent, REQ_MANUAL_DETECTION);

            	} else {
            		
            		Toast.makeText(getApplicationContext(), "신분증 인식에 실패 하여 재촬영 합니다.", Toast.LENGTH_SHORT).show();
                	m_progressBar.setVisibility(View.GONE);
                	m_cameraPreview.startCamera();

            	}

                
            }
        });

        m_btnFlash.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (m_cameraPreview != null)
                {

                    if (m_btnFlash.isSelected() == false)
                    {
                        m_btnFlash.setSelected(true);
                        m_cameraPreview.enableFlash(true);
                        m_btnFlash.setBackgroundResource(R.drawable.ic_flash_on);
                    }
                    else
                    {
                        m_btnFlash.setSelected(false);
                        m_cameraPreview.enableFlash(false);
                        m_btnFlash.setBackgroundResource(R.drawable.ic_flash_off);
                       
                    }
                }
            }
        });

//        m_cameraPreview.setOnErrorResult(new PaceDetection.ErrorResult()
//        {
//            @Override
//            public void onMessage(int errorCode, String message)
//            {
//                m_textview.setText(message);
//            }
//        });

        //m_cameraPreview.setGuideMessage("adfasdf");
        
        
        if(mManualDelay == 0) {
        	setManualEnable();
        } else {
        	startManualTimer();
        }
        
        
        
    }

    
    
    private RecognitionResult recognition(String detectedImagePathName){
    	
    	RecognitionResult recognitionResult = new RecognitionResult();
    	
    	PaceIdCardRecognition paceRecog = new PaceIdCardRecognition(PreviewActivity.this);

    	recognitionResult.result = paceRecog.init("asc.wgt", "hang.rec", detectedImagePathName);

        //recognitionResult.cardImgPath = m_documentPath + "/saveRecogImage.jpg";
        
        recognitionResult.result = paceRecog.recognition(recognitionResult);
        paceRecog.release();

        return recognitionResult;

    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	
    	if(requestCode == REQ_MANUAL_DETECTION) {
    		
    		if(resultCode == RESULT_OK) {
    			
    			String detectedImagePath = data.getStringExtra("detectedImagePath");
    			
    			//System.out.println("REQ_MANUAL_DETECTION : " + detectedImagePath + ", " + new File(detectedImagePath).exists());
    			
    			RecognitionResult recognitionResult = recognition(detectedImagePath);
    			
    			//System.out.println(recognitionResult.result + ", " + recognitionResult.type + ", " + recognitionResult.name + ", " + recognitionResult.idNumber + ", " + new File(detectedImagePath).exists());
    			
    			 if(recognitionResult.result && (recognitionResult.type == 1 || recognitionResult.type == 3)) {

    				 BaseApplication baseApp = (BaseApplication) getApplication();
    				 baseApp.setRecognitionResult(recognitionResult);
     				 
                     setResult(RESULT_OK);

                 } else {
                	 setResult(RESULT_CANCELED);
                 }
    			
    		} else {
    			setResult(RESULT_CANCELED);
    		}
    		
    		finish();

    	}
    	
    	
    	
    	
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        if(m_cameraPreview != null)
        {
            m_cameraPreview.onResume();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if(m_cameraPreview != null)
        {
            m_cameraPreview.onPause();
        }

    }
    
    @Override
    protected void onDestroy() {
    	stopManualTimer();
    	
    	super.onDestroy();
    }

    
    private void startManualTimer(){
    	
    	if(mManualTimer == null) {
    	
    		mManualTimer = new Timer();
    		mManualTimer.schedule(new TimerTask() {
				
				@Override
				public void run() {

					runOnUiThread(new Runnable() {
						public void run() {
							stopManualTimer();
							
							setManualEnable();

						}
					});

				}
			}, mManualDelay);
    
    		
    	}
    }
    
    private void stopManualTimer(){
		if(mManualTimer != null) {
			mManualTimer.cancel();
			mManualTimer = null;
		}
		
	}
    
    private void setManualEnable(){

    	m_cameraPreview.showManualMessage();
    	m_cameraPreview.enableMaualTakePictureOnTouch(true);
	    m_cameraPreview.enableAutoFocusTakePicture(false);
	    
	    m_btnShotter.setVisibility(View.VISIBLE);
	    
	    mCameraManual = true;
    	
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class ProgressTask extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            m_progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {

            int i=0;
            while(m_bFinish == false)
            {
                try
                {
                    Thread.sleep(100);
                    m_progressBar.setProgress(++i);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            m_progressBar.setVisibility(View.GONE);
        }
    };
}
