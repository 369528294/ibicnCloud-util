package com.ibicnCloud.util.http;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpStatus;

import com.ibicnCloud.util.StringUtil;

public class HttpResponse {
    private int statusCode = 0;
    private boolean success = false;
    private String message = null;
    private byte[] body;
    private Map header = null;
    private Map cookie = null;
    private String options = null;
    private String charset = null;

    protected HttpResponse() {
        this.charset = "UTF-8";
    }

    protected HttpResponse(String charset) {
        this.charset = charset;
    }

    public String getBody() throws UnsupportedEncodingException {
        return getBody(this.charset);
    }

    public String getBody(String charset) throws UnsupportedEncodingException {
        if (this.body == null)
            return null;

        if (StringUtil.isEmpty(charset))
            return new String(this.body);

        return new String(this.body, charset);
    }

    public byte[] getBodyInByte() {
        return this.body;
    }

    protected void setBody(byte[] body) {
        this.body = body;
    }

    public boolean isSuccess() {
        return this.success;
    }

    protected void setSuccess(boolean success) {
        this.success = success;
        this.statusCode = 200;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getStatusText() {
        return HttpStatus.getStatusText(this.statusCode);
    }

    protected void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return this.message;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

    protected HttpResponse addHeader(String name, String value) {
        if (StringUtil.isEmpty(name))
            return this;

        if (this.header == null)
            this.header = new HashMap();

        this.header.put(name, value);
        return this;
    }

    public String getHeader(String name) {
        if (this.header == null)
            return null;

        return ((String) this.header.get(name));
    }

    public String getAllHeader() {
        if (this.header == null)
            return null;

        Iterator iterator = this.header.keySet().iterator();
        StringBuffer sb = new StringBuffer();
        while (iterator.hasNext()) {
            String name = (String) iterator.next();
            String value = (String) this.header.get(name);
            sb.append(name + ":" + value + "\n");
        }
        return sb.toString();
    }

    protected HttpResponse addCookie(String name, Cookie c) {
        if (this.cookie == null)
            this.cookie = new HashMap();

        this.cookie.put(name, c);
        return this;
    }

    public Cookie getCookie(String name) {
        if (this.cookie == null)
            return null;

        return ((Cookie) this.cookie.get(name));
    }

    public String getCookieInString(String name) {
        if (this.cookie == null)
            return null;

        Cookie c = (Cookie) this.cookie.get(name);
        if (c == null)
            return null;

        return c.getValue();
    }

    public Map getAllCookie() {
        return this.cookie;
    }

    public String getAllCookieInString() {
        if (this.cookie == null)
            return null;

        Iterator iterator = this.cookie.keySet().iterator();
        StringBuffer sb = new StringBuffer();
        while (iterator.hasNext()) {
            String name = (String) iterator.next();
            Cookie c = (Cookie) this.cookie.get(name);
            sb.append(name + ":" + c.getValue() + "\n");
        }
        return sb.toString();
    }

    public String getOptions() {
        return this.options;
    }

    protected void addOption(String option) {
        if (StringUtil.isEmpty(this.options)) {
            this.options = option;
        } else {
            HttpResponse tmp19_18 = this;
            tmp19_18.options = tmp19_18.options + "," + option;
        }
    }

    public String getCharset() {
        return this.charset;
    }

    protected void setCharset(String charset) {
        this.charset = charset;
    }
}