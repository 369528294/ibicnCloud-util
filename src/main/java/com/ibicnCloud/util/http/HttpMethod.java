package com.ibicnCloud.util.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ibicnCloud.util.TranscodeUtil;

public class HttpMethod {
    public static final String GET = "1";
    public static final String POST = "2";
    public static final String PUT = "5";
    public static final String DELETE = "6";

    public static final String MultipartPost = "3";
    public static final String Trace = "4";
    public static final String Options = "7";
    public static final String Head = "8";

    public static void main2(String[] args) {
        String url;
        int timeOut = 60 * 60; // 一小时
        /*
         * try { url = "http://localhost:8080/CookieService/cookie/userName"; HttpRequest request = new HttpRequest(url); request.addParameter("secretKey", "WELKSDJIR2234L33"); request.addParameter("UUID", "1ivuCtDsECfsBkzOCgoA7r1RDw7o/6D+ZDt8IvXlgzdCtVP7yz8rRAH/oq0PojHX"); HttpResponse response = HttpManager.send(request); if (response.getStatusCode() == 200) { System.out.println(response.getBody("GBK")); } else { System.out.println(response.getStatusCode()); } } catch (Exception e) { e.printStackTrace(); }
         */
        /*
         * try { url = "http://localhost:8080/CookieService/cookie/userName"; HttpRequest request = new HttpRequest(url, "2"); request.addParameter("secretKey", "KNI3HOSHA0JF0W3E"); request.addParameter("value", TranscodeUtil.strToUnicodeStr("测试仪")); HttpResponse response = HttpManager.send(request); if (response.getStatusCode() == 200) { System.out.println(response.getBody("GBK")); } else { System.out.println(response.getStatusCode()); } } catch (Exception e) { e.printStackTrace(); }
         */

        /*
         * try { url = "http://localhost:8080/CookieService/cookie/userName"; HttpRequest request = new HttpRequest(url, "2"); request.addParameter("secretKey", "WELKSDJIR2234L33"); request.addParameter("value", TranscodeUtil.strToUnicodeStr("测试仪--更改")); request.addParameter("UUID", "aSnPL1cJ6kU0cZEk5QxujqcmLhI5QfzGrTCijEngC1mW3XjUQQ9sZtsqr+4i+Vse"); request.addParameter("timeOut", "" + timeOut); HttpResponse response = HttpManager.send(request); if (response.getStatusCode() == 200) { System.out.println(response.getBody("GBK")); } else { System.out.println(response.getStatusCode()); } } catch (Exception e) { e.printStackTrace(); }
         */
    }

    private static String addCookie() {
        String urlResouce = "http://localhost:8080/cloud-storeage-boss/muluSDF/xyz.png"; // create URL
        File localFile = new File("C:\\Users\\maomao\\Pictures\\2.png");
        try {

            HttpURLConnection urlConnection = (HttpURLConnection) (new URL(urlResouce)).openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("PUT");
            OutputStream urlOutputStream = urlConnection.getOutputStream();
            FileInputStream fileInputStream = new FileInputStream(localFile);
            IOUtils.copy(fileInputStream, urlOutputStream);
            fileInputStream.close();
            urlOutputStream.close();
            System.out.println(urlConnection.getResponseCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        addCookie();
    }

}