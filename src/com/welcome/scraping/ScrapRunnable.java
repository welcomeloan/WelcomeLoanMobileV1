package com.welcome.scraping;

import android.app.Activity;

import com.infotech.IFTCrypto.InfoTecCoreCompelete;
import com.infotech.IFTCrypto.iftCoreEnV2;

import org.json.JSONObject;

/**
 * Created by welcomeloan on 2018. 2. 1..
 */

public class ScrapRunnable implements Runnable {

    private JSONObject inputObj;
    private String scrapType;
    private Activity cabWebActivity;
    private String strScrapResult;

    public String getStrScrapResult() {
        return this.strScrapResult;
    }

    public void setStrScrapResult(String result) {
        this.strScrapResult = result;
    }

    public String getScrapType() {
        return this.scrapType;
    }

    InfoTecCoreCompelete infoTecListener =  new InfoTecCoreCompelete() {
        @Override
        public void onRequestComplete(boolean b, String s) {

        }

        @Override
        public void onRequestProgress(int i, String progressMsg) {

        }
    };

    ScrapRunnable(JSONObject inputObj, String type, Activity activity) {
        this.inputObj = inputObj;
        this.scrapType = type;
        this.cabWebActivity = activity;
    }

    @Override
    public void run() {
        iftCoreEnV2 iftCore = new iftCoreEnV2(this.cabWebActivity, infoTecListener);
        //FIXME for test
        this.strScrapResult = iftCore.startEngine(this.inputObj.toString());
//        if (!this.scrapType.equals("KRAS")) {
//            this.strScrapResult = iftCore.startEngine(this.inputObj.toString());
//        } else {
//            this.strScrapResult = "{\"errYn\":\"Y\"}";
//        }

    }
}
