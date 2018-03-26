package com.ibicnCloud.util.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import com.ibicnCloud.util.StringUtil;

public class HttpRequest {
    private String url;
    private String domain;
    private String method;
    private String charset;
    private List parameter;
    private List file;
    private List cookie;
    private List header;
    private int timeout;
    private boolean followRedirect;
    private boolean authentication;
    private boolean receiveContent;
    private int retryCount;
    private AuthScope scope;
    private StringRequestEntity body;
    private UsernamePasswordCredentials credentials;

    public HttpRequest(String url) {
        this(url, "1", "UTF-8");
    }

    public HttpRequest(String url, String method) {
        this(url, method, "UTF-8");
    }

    public HttpRequest(String url, String method, String charset) {
        this.url = null;
        this.domain = null;
        this.method = null;
        this.charset = null;
        this.parameter = null;
        this.file = null;
        this.cookie = null;
        this.header = null;
        this.timeout = 5000;
        this.followRedirect = true;
        this.authentication = false;
        this.receiveContent = true;
        this.retryCount = 3;
        this.scope = null;
        this.credentials = null;

        this.url = url;
        this.method = method;
        this.domain = HttpUtil.abstractSiteDomain(this.url);
        this.charset = charset;
        addHeader("charset", this.charset);
        setUserAgent("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; .NET CLR 2.0.50727)");
    }

    protected String getUrl() {
        return this.url;
    }

    protected String getMethod() {
        return this.method;
    }

    protected String getCharset() {
        return this.charset;
    }

    public HttpRequest addParameter(String name, String value) {
        if ((StringUtil.isEmpty(name)) || (StringUtil.isEmpty(value)))
            return this;

        if (this.parameter == null)
            this.parameter = new Vector();

        if ("1".equals(this.method))
            this.parameter.add(name + "=" + HttpUtil.encodeUrl(value, this.charset));
        else if ("2".equals(this.method))
            this.parameter.add(new NameValuePair(name, value));
        else if ("3".equals(this.method))
            this.parameter.add(new StringPart(name, value));

        return this;
    }

    protected List getParameter() {
        return this.parameter;
    }

    public HttpRequest addCookie(String name, String value) {
        return addCookie(name, value, "/");
    }

    public HttpRequest addCookie(Cookie c) {
        if (this.cookie == null)
            this.cookie = new Vector();

        this.cookie.add(c);
        return this;
    }

    public HttpRequest addCookie(Map map) {
        if (this.cookie == null)
            this.cookie = new Vector();

        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String name = (String) iterator.next();
            Cookie c = (Cookie) map.get(name);
            this.cookie.add(c);
        }
        return this;
    }

    public HttpRequest addCookie(String name, String value, String path) {
        if ((StringUtil.isEmpty(name)) || (StringUtil.isEmpty(value)))
            return this;

        if (this.cookie == null)
            this.cookie = new Vector();

        Cookie c = new Cookie(this.domain, name, value);
        c.setPath(StringUtil.format(path, "/"));
        this.cookie.add(c);

        return this;
    }

    protected List getCookie() {
        return this.cookie;
    }

    public HttpRequest addHeader(String name, String value) {
        if ((StringUtil.isEmpty(name)) || (StringUtil.isEmpty(value)))
            return this;

        if (this.header == null)
            this.header = new Vector();

        this.header.add(new Header(name, value));
        return this;
    }

    protected List getHeader() {
        return this.header;
    }

    public HttpRequest setReferer(String referer) {
        addHeader("Referer", referer);
        return this;
    }

    public HttpRequest setUserAgent(String userAgent) {
        addHeader("User-Agent", userAgent);
        return this;
    }

    public HttpRequest addFile(String name, String path) throws FileNotFoundException {
        if ((StringUtil.isEmpty(name)) || (StringUtil.isEmpty(path)))
            return this;

        if (this.file == null)
            this.file = new Vector();

        if ("3".equals(this.method))
            this.file.add(new FilePart(name, new File(path)));
        else if ("5".equals(this.method))
            this.file.add(new File(path));

        return this;
    }

    protected List getFile() {
        return this.file;
    }

    public HttpRequest setTimeout(int timeout) {
        if (timeout > 0)
            this.timeout = timeout;

        return this;
    }

    protected int getTimeout() {
        return this.timeout;
    }

    public HttpRequest setFollowRedirect(boolean flag) {
        this.followRedirect = flag;
        return this;
    }

    public boolean getFollowRedirect() {
        return this.followRedirect;
    }

    public HttpRequest setReceiveContent(boolean flag) {
        this.receiveContent = flag;
        return this;
    }

    public boolean getReceiveContent() {
        return this.receiveContent;
    }

    public int getRetryCount() {
        return this.retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public HttpRequest setAuthentication(String host, int port, String username, String password) {
        this.authentication = true;
        this.scope = new AuthScope(host, port);
        this.credentials = new UsernamePasswordCredentials(username, password);
        return this;
    }

    public boolean getAuthentication() {
        return this.authentication;
    }

    protected AuthScope getAuthScope() {
        return this.scope;
    }

    protected UsernamePasswordCredentials getCredentials() {
        return this.credentials;
    }

    /**
     * 设置当前请求的请求体，仅对 Post 方法有效；
     */
    public HttpRequest setBody(String content, String contentType) throws UnsupportedEncodingException{
        if(!StringUtil.isEmpty(content) && !StringUtil.isEmpty(contentType)){
            this.body = new StringRequestEntity(content, contentType, charset);
        }
        return this;
    }

    /**
     * 设置当前请求的请求体，仅对 Post 方法有效，JSON 格式；
     */
    public HttpRequest setJsonBody(String content) throws UnsupportedEncodingException{
        return setBody(content, "application/json");
    }

    public StringRequestEntity getBody() {
        return body;
    }

    public void setBody(StringRequestEntity body) {
        this.body = body;
    }
}