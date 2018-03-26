package com.ibicnCloud.util.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.ibicnCloud.util.StringUtil;

public class HttpUtil {
    public static String File_Encoding = "UTF-8";

    public static final String abstractSiteDomain(String url) {
        if (StringUtil.isEmpty(url))
            return null;
        url = url.replace("http://", "");
        url = (url.indexOf("/") > -1) ? url.substring(0, url.indexOf("/")) : url;
        String[] ary = StringUtil.split(url, "\\.");
        return ((ary.length > 2) ? ary[1] + "." + ary[2] : url);
    }

    public static final String abstractSiteUrl(String url) {
        url = abstractSiteDomain(url);
        if (url != null)
            return "http://" + url;
        return null;
    }

    public static final String encodeUrl(String url) {
        return encodeUrl(url, File_Encoding);
    }

    public static final String encodeUrl(String url, String charset) {
        if (StringUtil.isEmpty(url))
            return "";

        try {
            url = URLEncoder.encode(url, charset).replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
        }
        return url;
    }

    public static final String decodeUrl(String url) {
        return decodeUrl(url, File_Encoding);
    }

    public static final String decodeUrl(String url, String charset) {
        if (StringUtil.isEmpty(url))
            return "";
        try {
            url = URLDecoder.decode(url, charset);
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
        }
        return url;
    }

    public static String encodeUnicode(String input) {
        if (StringUtil.isEmpty(input))
            return "";

        char[] aryChar = input.toCharArray();
        StringBuffer sb = new StringBuffer(aryChar.length);
        for (int i = 0; i < aryChar.length; ++i) {
            String hex = Integer.toHexString(aryChar[i]);
            if (hex.length() <= 2)
                hex = "00" + hex;

            sb.append("\\u" + hex);
        }
        return sb.toString();
    }

    public static String decodeUnicode(String input) {
        if (StringUtil.isEmpty(input))
            return "";

        input = input.toLowerCase();
        int beginIndex = input.indexOf("\\u");
        int closeIndex = 0;
        StringBuffer sb = new StringBuffer();

        while (beginIndex > -1) {
            closeIndex = input.indexOf("\\u", beginIndex + 2);
            String s = "";
            if (closeIndex == -1) {
                s = input.substring(beginIndex + 2, input.length());
                beginIndex = -1;
            } else {
                s = input.substring(beginIndex + 2, closeIndex);
                beginIndex = closeIndex;
            }
            char c = (char) Integer.parseInt(s, 16);
            sb.append(new Character(c).toString());
        }

        return sb.toString();
    }
}
