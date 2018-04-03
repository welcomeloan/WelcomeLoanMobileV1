package com.welcome.scraping;

import android.util.Log;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class iftPdfProcess {

    public static int convertInt(String s) {
        try
        {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getJusoIndex(String s) {
        try
        {
            s = s+ " ";
            String[] tmpArr = s.split(" ");
            return Integer.parseInt(tmpArr[0]);
        } catch (Exception e) {
            return 0;
        }
    }

    public static byte[] hexToByte(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String parsePdf(String pdf) throws IOException {

        byte[] bytePdf = hexToByte(pdf);

        PdfReader reader = new PdfReader(bytePdf);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        String ret = "";
        TextExtractionStrategy strategy;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
            ret += strategy.getResultantText();
        }
        reader.close();
        return ret;
    }

    public static JSONObject makeText(final String hexPdf) throws JSONException {
        String src  = hexPdf;
        String data = hexPdf;

        try {
            data = parsePdf(hexPdf);
            src  = data;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject root = new JSONObject();
        JSONArray jusoList  = new JSONArray();

        String tmpMsg = data.split("== 공 란 ==")[1];
        String[] tmpArr = tmpMsg.split("\n");
        String changeMsg = "";
        for (int ci = 0; ci < tmpArr.length; ++ci) {
            Log.i("allscrap", "change msg "+ ci + "::: " + tmpArr[ci]);
            Log.i("allscrap", "change msg "+ ci + "::: " + tmpArr[ci].contains("번호"));
            if (!tmpArr[ci].contains("번호 주 소")) {
                changeMsg += tmpArr[ci];
            } else {
                break;
            }
        }
        Log.i("allscrap", "string change msg ::: " + changeMsg);
        root.put("CHANGE_MSG", changeMsg.replace("\"", ""));

        int start = 0;
        int end = 0;
        String[] tt = data.split("역종");
        if (tt.length >= 2) {
            tmpMsg = "역종" + data.split("역종")[1];
            tmpArr = tmpMsg.split("\n");
            boolean isEmptyServiceType = false;
            for (int i = 0; i < tmpArr.length; ++i) {
                Log.i("allscrap", "army str "+ i + "::: " + tmpArr[i]);
                String tmpLineStr = tmpArr[i];
                if (tmpLineStr.contains("역종")) {
                    start = tmpLineStr.indexOf("[");
                    end = tmpLineStr.indexOf("]");
                    String val = tmpLineStr.substring(start+1, end);
                    if (val.equals("")) {
                        isEmptyServiceType = true;
                    } else {
                        root.put("ARMY_SERVICE_TYPE", tmpLineStr.substring(start+1, end));
                        if (tmpLineStr.contains("군별")) {
                            tmpLineStr = tmpLineStr.substring(tmpLineStr.indexOf("군별"));
                            start = tmpLineStr.indexOf("[");
                            end = tmpLineStr.indexOf("]");
                            if (tmpLineStr.substring(start+1, end).equals("")) {
                                root.put("ARMY_TYPE", "");
                            } else {
                                root.put("ARMY_TYPE", tmpLineStr.substring(start+1, end));
                            }

                        }
                    }
                }
                if (isEmptyServiceType == false) {
                    if (tmpLineStr.contains("입영")) {
                        tmpLineStr = tmpLineStr.substring(tmpLineStr.indexOf("입영"));
                        start = tmpLineStr.indexOf("[");
                        end = tmpLineStr.indexOf("]");
                        if (tmpLineStr.substring(start+1, end).equals("")) {
                            root.put("ARMY_JOIN_DT", "");
                        } else {
                            root.put("ARMY_JOIN_DT", tmpLineStr.substring(start+1, end));
                        }

                    }
                    if (tmpLineStr.contains("전역일자")) {
                        tmpLineStr = tmpLineStr.substring(tmpLineStr.indexOf("전역일자"));
                        start = tmpLineStr.indexOf("[");
                        end = tmpLineStr.indexOf("]");
                        if (tmpLineStr.substring(start+1, end).equals("")) {
                            root.put("ARMY_RETIRE_DT", "");
                        } else {
                            root.put("ARMY_RETIRE_DT", tmpLineStr.substring(start+1, end));
                        }
                        if (tmpLineStr.contains("계급")) {
                            tmpLineStr = tmpLineStr.substring(tmpLineStr.indexOf("계급"))
;                            start = tmpLineStr.indexOf("[");
                            end = tmpLineStr.indexOf("]");
                            if (tmpLineStr.substring(start+1, end).equals("")) {
                                root.put("ARMY_CLASS", "");
                            } else {
                                root.put("ARMY_CLASS", tmpLineStr.substring(start+1, end));
                            }

                        }
                    }
                    if (tmpLineStr.contains("전역사유")) {
                        tmpLineStr = tmpLineStr.substring(tmpLineStr.indexOf("전역사유"));
                        start = tmpLineStr.indexOf("[");
                        end = tmpLineStr.indexOf("]");
                        if (tmpLineStr.substring(start+1, end).equals("")) {
                            root.put("ARMY_RETIRE_REASON", "");
                        } else {
                            root.put("ARMY_RETIRE_REASON", tmpLineStr.substring(start+1, end));
                        }

                        break;
                    }
                } else {
                    if (tmpLineStr.contains("처분사항")) {
                        tmpLineStr = tmpLineStr.substring(tmpLineStr.indexOf("처분사항"));
                        start = tmpLineStr.indexOf("[");
                        end = tmpLineStr.indexOf("]");
                        root.put("ARMY_SERVICE_TYPE", "");
                        root.put("ARMY_TYPE", "");
                        root.put("ARMY_JOIN_DT", "");
                        root.put("ARMY_RETIRE_DT", "");
                        root.put("ARMY_CLASS", "");
                        root.put("ARMY_RETIRE_REASON", tmpLineStr.substring(start+1, end));
                        break;
                    }
                }
            }
        } else {
            root.put("ARMY_SERVICE_TYPE", "");
            root.put("ARMY_TYPE", "");
            root.put("ARMY_JOIN_DT", "");
            root.put("ARMY_RETIRE_DT", "");
            root.put("ARMY_CLASS", "");
            root.put("ARMY_RETIRE_REASON", "");
        }


        String pageArr[] = data.split("변 동 사 유 등록상태");

        String pageContent = "";
        for(int p=1; p < pageArr.length; p++) {
            pageContent += pageArr[p].split("※ 본인이나")[0];
            //System.out.println( data + "[@@@@@]");
        }

        //System.out.println("pageContent::" + pageContent);
        //data = data.split("변 동 사 유 등록상태")[1].split("※ 본인이나")[0];
//        pageContent = pageContent.replace("의 자녀", "의자녀");
//        pageContent = pageContent.replace("의 본인", "의본인");
//        pageContent = pageContent.replace("의 손", "의손");
//        pageContent = pageContent.replace("의 배우자", "의배우자");
//        pageContent = pageContent.replace("의 조카", "의조카");
//        pageContent = pageContent.replace("의 처남", "의처남");
        String arr[] = pageContent.split("\n");
        String str_list[] = new String[100];
        String regex = ".*(((19|20)\\d\\d).(0[1-9]|1[012]).(0[1-9]|[12][0-9]|3[01])).*";   //날짜 형식

        for(int j=1; j<=100; j++) {
            boolean b = false;

            for(int i=0; i<arr.length; i++) {
                //System.out.println("....."+arr[i].substring(0, 2) + ":" + "\n");
                if (arr[i].indexOf("== 이하여백 ==") > -1) break;
                if (arr[i].indexOf("※ 본인이나 세대원은 정부24(gov.kr") > -1) continue;
                int ii = getJusoIndex(arr[i]);
                if(ii == j) {
                    b = true;
                    str_list[j-1] = "<"+j+">";
                } else if(arr[i].length()>1 && ii == j) {
                    b = true;
                    str_list[j-1] = "<"+j+">";
                }
                else if(ii == (j+1)) {
                    b = false;
                    break;
                }
                else if(arr[i].length()>1 && ii == (j+1)) {
                    b = false;
                    break;
                }
                if(b == true) {
                    str_list[j-1] += arr[i] + "$";
                    str_list[j-1] = str_list[j-1].replace(" [", "$[");
                }
            }
            System.out.println(str_list[j-1]);
        }

        int count[]= new int[100]; // 날짜형식 개수
        for(int i=0; i<str_list.length; i++) {
            if (str_list[i] == null) break;
            JSONObject juso = new JSONObject();
            String text[] = str_list[i].split("\\$");
            String addr = "";
            for(int j=0; j<text.length; j++) {
                if(text.length==4) { // 주소가 한 줄인 경우
                    addr = text[1];
                } else if(text.length==5) { // 주소가 두 줄인 경우
                    addr = text[1] + " " +text[2];
                }
            }
//            System.out.println("length::" + str_list[i].split("\\$").length);
            if(!str_list[i].matches(regex) || str_list[i].split("\\$").length <= 2) { // 날짜형식이 없을때 또는 주소만 있는 경우
                str_list[i] = "주소 : " + text[1];
                count[i] = 0;
                System.out.println(str_list[i]);
                juso.put("주소",text[1]);
                juso.put("전입일","");
                juso.put("변동일","");
                juso.put("세대주및관계","");
                juso.put("변동사유","");
            }else {
                for(int j=0; j<text.length; j++) {
                    if(text[j].matches(regex)) { // 날짜형식이 있을때
                        String date[] = text[j].split(" ");

                        for(int k=0; k<date.length; k++) {  // 날짜형식 개수세기
                            if(date[k].matches(regex)) {
                                count[i] += 1;
                            }
                        }

                        for(int k=0; k<date.length; k++) {
                            if(date[k].matches(regex) && count[i]==2) { // 날짜형식이 2개있을 때
                                if (date.length == 2) {
                                    text[j] = "주소 : " + addr;
                                    text[j] += " / 전입일 : " + date[0] + " ";
                                    text[j] += "/ 변동일 : " + date[1] + " ";
                                    text[j] += "/ 세대주및관계 : " + " ";
                                    juso.put("주소",addr);
                                    juso.put("전입일",date[0]);
                                    juso.put("변동일",date[1]);
                                    juso.put("세대주및관계","");
                                } else {
                                    text[j] = "주소 : " + addr;
                                    text[j] += " / 전입일 : " + date[0] + " ";
                                    text[j] += "/ 변동일 : " + date[1] + " ";
                                    text[j] += "/ 세대주및관계 : " + date[2] + " " + date[3];
                                    juso.put("주소",addr);
                                    juso.put("전입일",date[0]);
                                    juso.put("변동일",date[1]);
                                    juso.put("세대주및관계",date[2] + " " + date[3]);
                                }
                                break;
                            } else if(date[k].matches(regex) && count[i]==1) {
                                if (date.length == 2 || date.length == 3) {
                                    text[j] = "주소 : " + addr;
                                    text[j] += " / 전입일 : " + date[0] + " ";
                                    text[j] += "/ 변동일 : " + date[0] + " ";
                                    text[j] += "/ 세대주및관계 : " + date[1] + " " + date[2];
                                    juso.put("주소",addr);
                                    juso.put("전입일",date[0]);
                                    juso.put("변동일",date[0]);
                                    juso.put("세대주및관계",date[1] + " " + date[2]);
                                } else {
                                    text[j] = "주소 : " + addr;
                                    text[j] += " / 전입일 : " + date[0] + " ";
                                    text[j] += "/ 변동일 : " + date[1] + " ";
                                    text[j] += "/ 세대주및관계 : " + date[2] + " " + date[3];
                                    juso.put("주소",addr);
                                    juso.put("전입일",date[0]);
                                    juso.put("변동일",date[1]);
                                    juso.put("세대주및관계",date[2] + " " + date[3]);
                                }
                                break;
                            }
                        }

                    }
                }
            }

            for(int j=0; j<text.length; j++) {
                text[j] += " / 변동사유 : " + text[text.length-1];
                if(text[j].matches(regex)) {
                    System.out.println(text[j]);
                    juso.put("변동사유",text[text.length-1]);
                }
            }
            jusoList.put(juso);
        }
//        root.put("data",src);
        root.put("jusoList",jusoList);
        return root;
    }


}