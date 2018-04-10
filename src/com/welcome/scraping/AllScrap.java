package com.welcome.scraping;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.cabsoft.rainbowbr.components.nfilter.CabNFilterComponent;
import com.infotech.IFTCrypto.iftCoreEnV2;
import com.nshc.nfilter.NFilter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

/**
 * Created by welcomeloan on 2018. 2. 1..
 */

//infoTech 은행 스크래핑을 위한 enum
enum BankCode {
    BANK_KDB        {
        public String getCode() {return "002";}
        public String getName() {return "산업은행";}
    },
    BANK_IBK        {
        public String getCode() {return "003";}
        public String getName() {return "기업은행";}
    },
    BANK_KB         {
        public String getCode() {return "004";}
        public String getName() {return "국민은행";}
    },
    BANK_SUHYUP     {
        public String getCode() {return "007";}
        public String getName() {return "수협은행";}
    },
    BANK_NH         {
        public String getCode() {return "011";}
        public String getName() {return "농협은행";}
    },
    BANK_WOORI      {
        public String getCode() {return "020";}
        public String getName() {return "우리은행";}
    },
    BANK_SC         {
        public String getCode() {return "023";}
        public String getName() {return "SC은행";}
    },
    BANK_CITY       {
        public String getCode() {return "027";}
        public String getName() {return "씨티은행";}
    },
    BANK_DGB        {
        public String getCode() {return "031";}
        public String getName() {return "대구은행";}
    },
    BANK_BUSAN      {
        public String getCode() {return "032";}
        public String getName() {return "부산은행";}
    },
    BANK_KJ         {
        public String getCode() {return "034";}
        public String getName() {return "광주은행";}
    },
    BANK_JEJU      {
        public String getCode() {return "035";}
        public String getName() {return "제은행";}
    },
    BANK_JB         {
        public String getCode() {return "037";}
        public String getName() {return "전북은행";}
    },
    BANK_KN         {
        public String getCode() {return "039";}
        public String getName() {return "경남은행";}
    },
    BANK_MG         {
        public String getCode() {return "045";}
        public String getName() {return "새마을금고";}
    },
    BANK_SINHYUP    {
        public String getCode() {return "048";}
        public String getName() {return "신협";}
    },
    BANK_EPOST      {
        public String getCode() {return "079";}
        public String getName() {return "우체국";}
    },
    BANK_KEB        {
        public String getCode() {return "081";}
        public String getName() {return "하나은행";}
    },
    BANK_SHINHAN    {
        public String getCode() {return "088";}
        public String getName() {return "신한은행";}
    },
    BANK_KBANK      {
        public String getCode() {return "089";}
        public String getName() {return "K뱅크";}
    },
    //스크래핑 지원불가라 함..
//    BANK_KAKAO      {
//        public String getCode() {return "090";}
//        public String getName() {return "카카오뱅크";}
//    },
//    BANK_DAISHIN    {
//        public String getCode() {return "267";}
//        public String getName() {return "대신증권";}
//    },
//    BANK_NHQV       {
//        public String getCode() {return "289";}
//        public String getName() {return "NH투자증권";}
//    },
    BANK_SMPOP      {
        public String getCode() {return "240";}
        public String getName() {return "삼성증권";}
    },
    BANK_MERITZ     {
        public String getCode() {return "287";}
        public String getName() {return "메리츠증권";}
    },
    BANK_SHINVEST   {
        public String getCode() {return "278";}
        public String getName() {return "신한투자증권";}
    };

    public String getCode() {
        return this.getCode();
    }

    public String getName() {
        return this.getName();
    }
}

public class AllScrap {
    private final String decUrl;
    private CabNFilterComponent nfilter;
    private ArrayList<String> scrapTypes;

    public HashMap<String, String> mapScrapOutput = new HashMap<String, String>();

    public List<Future<?>> futures = new ArrayList<Future<?>>();
    public List<ScrapRunnable> runnables = new ArrayList<ScrapRunnable>();

    public String inspNo;

    private String fromDate;    //조회 시작일
    private String toDate;      //조회 종료일
    private String certNm;      //인증서 이름
    private String certPw;      //인증서 패스워드
    private String bizEndNo;    //암호화된 주번
    private String bizNo;       //복호화된 주번
    private String sido;        //시도
    private String sigg;        //시군구
    private String roadNm;      //나머지 도로명 주소
    private String telNo;       //전화번호
    private String custNm;      //고객이름
    private String bankSDate;   //은행조회 시작일
    private String faxNum;
    public boolean isEstate = false;

    private ExecutorService executeService;

    private iftCoreEnV2 iftEncPw = null;

    private Activity activity;

    private int THREAD_CNT = 15;

    private String addr1;
    private String addr2;

    public AllScrap(String decUrl, CabNFilterComponent nfilter, ArrayList<String> scrapTypes, String telNo, Activity activity) {
        this.decUrl = decUrl;
        this.nfilter = nfilter;
        this.scrapTypes = scrapTypes;
        this.activity = activity;
        this.telNo = telNo;
    }

    public void setIsEstate(boolean isEstate) {
        this.isEstate = isEstate;
    }

    public void init() {
        this.inspNo = getPaceUrlParam(this.decUrl, "inspNo");

        this.iftEncPw = new iftCoreEnV2(null, null);
        this.bizEndNo = iftEncPw.iftEncPrarm(iftEncPw.iftDecryptExt("welcomeloan_kwic", getPaceUrlParam(decUrl, "jumin")));
        this.bizNo = iftEncPw.iftDecryptExt("welcomeloan_kwic", getPaceUrlParam(decUrl, "jumin"));

        final String nfilterPublicKey = nfilter.getPublishKey();
        final String nfilterCoworkKey = NFilter.COWORKER_CODE;
        String aesenc = nfilter.aesencDataForId("certificatePass");
        this.certPw = iftEncPw.iftEncPrarm(iftEncPw.nFilterPassword(aesenc, nfilterPublicKey, nfilterCoworkKey));
        Log.i("scrap for iroTest", "for irostest certPW ::: " + iftEncPw.nFilterPassword(aesenc, nfilterPublicKey, nfilterCoworkKey));
//        this.certPw = iftEncPw.nFilterPassword(aesenc, nfilterPublicKey, nfilterCoworkKey);

        this.certNm = Base64.encodeToString(getPaceUrlParam(decUrl, "certnm").getBytes(), Base64.DEFAULT);
        Log.i("scrap for iroTest", "for irostest certPW ::: " + getPaceUrlParam(decUrl, "certnm"));

        this.fromDate = getPaceUrlParam(this.decUrl, "startdate");
        this.toDate = getPaceUrlParam(this.decUrl, "enddate");
        this.bankSDate = getPaceUrlParam(this.decUrl, "banksdate");

        this.sido = getPaceUrlParam(this.decUrl, "sido");
        this.sigg = getPaceUrlParam(this.decUrl, "sigg");
        this.roadNm = getPaceUrlParam(this.decUrl, "roadNm");

        this.custNm = getPaceUrlParam(this.decUrl, "custnm");

        this.faxNum = getPaceUrlParam(decUrl, "faxnum");

        this.inspNo = getPaceUrlParam(decUrl, "inspNo");

        ThreadFactory tf = new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread thread = new Thread(r);
                return thread;
            }
        };

        executeService = Executors.newFixedThreadPool(THREAD_CNT, tf);
    }

    public void runScrap() throws Exception {
        for (String scrapType : scrapTypes) {
            JSONObject inputObj;
            if (!scrapType.equals("BANK") & !scrapType.equals("NHIS")) {
                inputObj = this.getJsonObjectForScrap(scrapType, "");
                Log.i("AllScrap", "Scrap Object ::: " + inputObj.toString());
                ScrapRunnable sr = new ScrapRunnable(inputObj, scrapType, this.activity);
                runnables.add(sr);
                futures.add(executeService.submit(sr));
            }
        }
    }

    public void runNhisScrap() throws Exception {
        JSONObject inputObj;
        String scrapType = "NHIS";
        inputObj = this.getJsonObjectForScrap(scrapType, "");
        Log.i("AllScrap", "Scrap Object ::: " + inputObj.toString());
        ScrapRunnable sr = new ScrapRunnable(inputObj, scrapType, this.activity);
        runnables.add(sr);
        futures.add(executeService.submit(sr));
    }

    public void runBankScrap() throws Exception {
        JSONObject inputObj;
        String scrapType = "BANK";
        for (final BankCode bc : BankCode.values()) {
            inputObj = this.getJsonObjectForScrap(scrapType, bc.getCode());
            Log.i("AllScrap", "Scrap Object ::: " + inputObj.toString());
            ScrapRunnable sr = new ScrapRunnable(inputObj, scrapType+bc.getCode(), this.activity);
            //ScrapRunnable sr = new ScrapRunnable(inputObj, scrapType, this.activity);
            runnables.add(sr);
            futures.add(executeService.submit(sr));
        }
    }

    //3자 발급 신청번호를 받아와 PDF 파일을 읽어옴..
    public void runSecondGov(ScrapRunnable sr, int firstGovIndex) {
        String firstGovResult = sr.getStrScrapResult();
        Log.i("allScrap", "firstGovResult ::: " + firstGovResult);
        String type = "GOV";
        try {
            JSONObject inputObj = new JSONObject();
            JSONObject jo = new JSONObject(firstGovResult);
            if (jo.get("errYn").equals("N")) {
                runnables.remove(firstGovIndex);
                futures.remove(firstGovIndex);
                String cappReqNo = (String) jo.get("cappReqNo");
                //String cappReqNo = "2018022766033765";
                inputObj.put("svcCd", "P0001");
                inputObj.put("applNo", cappReqNo);
                inputObj.put("orgCd", "gov");
                inputObj.put("devMode", "R");
                inputObj.put("userId", "wel02");
                inputObj.put("loginMethod", "ID");
                // 3자 발급 수신처 정보 비번 (웰컴 회사 정보로 변경)
                inputObj.put("userEncPw", iftEncPw.iftEncPrarm("welcome1!"));
                Log.i("AllScrap", "Scrap Object second gov::: " + inputObj.toString());
                ScrapRunnable secondGovSr = new ScrapRunnable(inputObj, type, this.activity);
                runnables.add(secondGovSr);
                futures.add(executeService.submit(secondGovSr));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //InfoTech Scraping을 위해 Input Json Object를 생성.
    private JSONObject getJsonObjectForScrap(String scrapType, String bankCode) throws Exception {
        //default options
        final JSONObject obj = new JSONObject();
        obj.put("appCd", "com.welcomeloan.mobile");
        obj.put("bizEncNo", this.bizEndNo);
//        obj.put("bizNo", this.bizNo);

        obj.put("certNm", this.certNm);
        obj.put("signEncPw",this.certPw);
        //개발용일 경우 T로 값 변경
//        obj.put("devMode","T");
        obj.put("devMode","R");
        switch (scrapType) {
            case "NHIS":    //건강보험
                obj.put("svcCd", "B0001,B0002");
                obj.put("orgCd", "nhic");
                obj.put("fromDate", this.fromDate);
                obj.put("toDate", this.toDate);
                obj.put("faxNum", this.faxNum);
                break;
            case "BANK":    //은행내역
                obj.put("svcCd", "B0001,B0002");
                obj.put("orgCd", "bank");
                obj.put("bankCd", bankCode);
                //obj.put("useChannel", "0");
                obj.put("loginMethod", "CERT");
                obj.put("curCd", "KRW");
                obj.put("sdate", this.bankSDate);
                obj.put("edate", this.toDate);
                break;
            case "IROS":    //확정일자
                obj.put("orgCd", "iros");
                obj.put("svcCd", "B2001");
                obj.put("svcDivCd", "VIEW1");
                obj.put("loginMethod", "CERT");
                obj.put("userName", this.custNm);
                obj.put("fdCls", "1");
                obj.put("reqCls", "2");
                if (this.isEstate) {
                    obj.put("inqYn", "N");
                    obj.put("payDiv", "0");
                    obj.put("payNo", "I22048260655");
                    obj.put("payPw", "8878");
                    obj.put("hp1", "010");
                    obj.put("hp2", "6796");
                    obj.put("hp3", "8878");
                    obj.put("hpLoginPw", "8878");
                } else {
                    obj.put("inqYn", "Y");
                }
                break;
            case "ONNARA":  //온나라 토지찾기
                obj.put("orgCd", "onnara");
                obj.put("svcCd", "B0001");
                obj.put("userName", this.custNm);
                obj.put("logLevel", "D");
                break;
            case "GOV":  //민원24 초본 열람
                obj.put("orgCd", "gov");
                obj.put("svcCd", "B0003");
                obj.put("nonMemberYn", "Y"); // 비회원 로그인 연람 신청
                obj.put("hpNo", this.telNo);
                obj.put("userName", this.custNm);
                obj.put("reqSMS_YN", "N");
                obj.put("roadNm", this.roadNm);
                obj.put("sido", this.sido);
                obj.put("sigg", this.sigg);
                obj.put("req2Opt1", "01");
                obj.put("req2Opt2", "01");
                obj.put("req2Opt3", "01");
                obj.put("req2Opt4", "01");
                obj.put("req2Opt5", "01");
                obj.put("req2Opt6", "01");
                obj.put("req2Opt7", "01");
                obj.put("req2Opt8", "01");
                // 3자 발급 수신처 정보 (웰컴 회사 정보로 변경)
                obj.put("recvNm","웰컴크레디라인대부");
                obj.put("recvId","wel02");
                obj.put("recvTel1","010");
                obj.put("recvTel2", "6796");
                obj.put("recvTel3","8878");
                break;
            case "INSURE":
                obj.put("orgCd", "cont.insure");
                obj.put("svcCd", "B0001");
                obj.put("authType", "cert");
                obj.put("userName", this.custNm);
                obj.put("mobileNo", telNo);
                break;
            //일사편리는 스크래핑 안하기로 함
//            case "KRAS":
//                obj.put("orgCd", "kras");
//                obj.put("svcCd", "B0001");
//                obj.put("loginMethod", "CERT");
//                obj.put("userName", this.custNm);
//                obj.put("addrOption", "2");
//                obj.put("addr1", this.addr1);
//                obj.put("addr2", this.addr2);
//                obj.put("pub_type", "종합형");
//                obj.put("captcha_type", "auto");
//                break;
            case "EFINE":
                obj.put("orgCd", "efine");
                obj.put("svcCd", "B0001,B0002,B1001,B1002");
                obj.put("userName", this.custNm);
                break;

        }
        return obj;
    }

    private String getPaceUrlParam(String url, String key) {
        String value = "";

        int paramStart = url.indexOf("?");

        if (paramStart == -1) {
            return value;
        }

        int keyIdx = url.indexOf(key + "=", paramStart);

        if (keyIdx > -1) {

            int start = keyIdx + key.length() + 1;
            int end = url.indexOf("&", start);

            if (end == -1) {
                value = url.substring(start, url.length());
            } else {
                value = url.substring(start, end);
            }
        }

        return value;
    }
}
