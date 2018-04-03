package com.pacesystem.paceidcardrecog;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.welcomeloan.mobile.R;
import com.pacesystem.lib.PaceManualDetectionView;

import java.io.File;


public class ManualDetectionActivity extends Activity
{
    private PaceManualDetectionView m_ivManualView;

    private Button m_btnRecognition;

    private String m_detectedImagePathName;

    private String m_documentPath;

    private ProgressBar m_progressBar;

    private SendMessageHandler m_msgHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_detection);

        m_ivManualView = (PaceManualDetectionView)findViewById(R.id.id_result_imageview_manual);
        
        m_btnRecognition = (Button)findViewById(R.id.id_result_button);

        m_progressBar = (ProgressBar)findViewById(R.id.id_result_progressbar);

        m_msgHandler = new SendMessageHandler();

        Intent intent = getIntent();
        String src_image_path = intent.getStringExtra("src_image_path");

        m_ivManualView.loadImage(src_image_path);
        

        String cachePath = getCacheDir().getAbsolutePath(); //Environment.getExternalStorageDirectory().getAbsolutePath();
        m_documentPath = cachePath + File.separator + "RecogImage";
        File fDocument = new File(m_documentPath);
        if(fDocument.exists() == false)
        {
            fDocument.mkdirs();
        }


        m_btnRecognition.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            	boolean result = warpTransform(m_documentPath + "/detectedResult.jpg");
                m_detectedImagePathName = m_documentPath + "/detectedResult.jpg";
                
                
                if(result) {
                	
                	Intent intent = new Intent();
                	intent.putExtra("detectedImagePath", m_detectedImagePathName);
                	setResult(RESULT_OK, intent);
                } else {
                	setResult(RESULT_CANCELED);
                }
                
                finish();
            }
        });

    }



    private class SendMessageHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);


            switch (msg.what)
            {
                case 1:
                    m_progressBar.setVisibility(View.VISIBLE);
                    break;

                case 2:
                    m_progressBar.setVisibility(View.INVISIBLE);
                    break;

                case 3:
                    break;

                default:
                    break;
            }
        }
    }

    private boolean warpTransform(String saveResultImagePathName)
    {
        if(m_ivManualView != null)
        {
            return m_ivManualView.doWarpTransform(saveResultImagePathName);
        }

        return false;
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
}
