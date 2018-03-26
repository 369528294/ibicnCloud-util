package com.ibicnCloud.util.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.OptionsMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.TraceMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.ibicnCloud.util.CollectionUtil;
import com.ibicnCloud.util.StringUtil;

public class HttpManager {
    private static final int DefaultMaxThreadCount = 50;

    public static final HttpResponse send(HttpRequest request) throws IOException {
        HttpClient client = new HttpClient();
        return send(client, request);
    }

    private static final HttpResponse send(HttpClient client, HttpRequest request) throws IOException {
        HttpResponse response = new HttpResponse(request.getCharset());

        if ("1".equals(request.getMethod()))
            doGet(client, request, response);
        else if ("2".equals(request.getMethod()))
            doPost(client, request, response);
        else if ("3".equals(request.getMethod()))
            doMultipartPost(client, request, response);
        else if ("8".equals(request.getMethod()))
            doHead(client, request, response);
        else if ("4".equals(request.getMethod()))
            doTrace(client, request, response);
        else if ("7".equals(request.getMethod()))
            doOptions(client, request, response);
        else if ("6".equals(request.getMethod()))
            doDelete(client, request, response);
        else if ("5".equals(request.getMethod())) {
            doPut(client, request, response);
        }

        return response;
    }

    public static final Map<String, HttpResponse> send(String[] url) {
        return send(url, 0, 0);
    }

    public static final Map<String, HttpResponse> send(String[] url, int maxConnectionCount) {
        return send(url, maxConnectionCount, 0);
    }

    public static final Map<String, HttpResponse> send(String[] url, int maxConnectionCount, int maxThreadCount) {
        int size = StringUtil.size(url);
        HttpRequest[] request = new HttpRequest[size];
        for (int i = 0; i < size; ++i)
            request[i] = new HttpRequest(url[i]);

        return send(request, maxConnectionCount, maxThreadCount);
    }

    public static final Map<String, HttpResponse> send(HttpRequest[] request) {
        return send(request, 0, 0);
    }

    public static final Map<String, HttpResponse> send(HttpRequest[] request, int maxConnectionCount) {
        return send(request, maxConnectionCount, 0);
    }

    public static final Map<String, HttpResponse> send(HttpRequest[] request, int maxConnectionCount, int maxThreadCount) {
        Hashtable table = new Hashtable();
        MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
        if (maxThreadCount == 0)
            maxThreadCount = 50;

        if (maxConnectionCount > 0)
            manager.getParams().setMaxTotalConnections(maxConnectionCount);

        HttpClient client = new HttpClient(manager);

        int total = StringUtil.size(request);
        HttpWorker[] worker = new HttpWorker[total];
        int beginIndex = 0;
        int closeIndex = Math.min(beginIndex + maxThreadCount, total);
        while (beginIndex < total) {
            for (int i = beginIndex; i < closeIndex; ++i)
                worker[i] = new HttpWorker(client, request[i], table);

            for (int i = beginIndex; i < closeIndex; ++i)
                worker[i].start();

            for (int i = beginIndex; i < closeIndex;) {
                try {
                    worker[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ++i;
            }

            beginIndex += maxThreadCount;
            closeIndex = Math.min(beginIndex + maxThreadCount, total);
        }

        return table;
    }

    private static final void doGet(HttpClient client, HttpRequest request, HttpResponse response) throws IOException {
        ByteArrayOutputStream buffer;
        GetMethod get = new GetMethod(request.getUrl());
        get.setFollowRedirects(request.getFollowRedirect());
        get.getParams().setParameter("http.protocol.content-charset", request.getCharset());
        if (request.getRetryCount() != 3) {
            get.getParams().setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler(request.getRetryCount(), false));
        }

        if (!(CollectionUtil.isEmpty(request.getParameter()))) {
            String query = StringUtil.join(request.getParameter(), "&");
            get.setQueryString(query);
        }

        HttpState state = null;
        if (CollectionUtil.size(request.getCookie()) > 0) {
            state = new HttpState();
            List cookie = request.getCookie();
            for (int i = 0; i < CollectionUtil.size(cookie); ++i) {
                Cookie c = (Cookie) cookie.get(i);
                state.addCookie(c);
            }

        }

        if (CollectionUtil.size(request.getHeader()) > 0) {
            List headerList = request.getHeader();
            for (int i = 0; i < CollectionUtil.size(headerList); ++i) {
                Header header = (Header) headerList.get(i);
                get.addRequestHeader(header);
            }
        }

        try {
            if (request.getTimeout() > 0)
                client.getParams().setConnectionManagerTimeout(request.getTimeout());

            if (state != null)
                client.setState(state);

            if (request.getAuthentication()) {
                client.getState().setCredentials(request.getAuthScope(), request.getCredentials());
                get.setDoAuthentication(true);
            }

            int statusCode = client.executeMethod(get);
            Header[] header = get.getResponseHeaders();
            for (int i = 0; i < StringUtil.size(header); ++i) {
                response.addHeader(header[i].getName(), header[i].getValue());
            }

            Cookie[] cookie = client.getState().getCookies();
            for (int i = 0; i < StringUtil.size(cookie); ++i)
                response.addCookie(cookie[i].getName(), cookie[i]);

            if (statusCode != 200) {
                response.setSuccess(false);
                response.setStatusCode(statusCode);
            } else {
                response.setSuccess(true);
                if (!(request.getReceiveContent())) {
                    return;
                }
                byte[] temp = new byte[1024];
                BufferedInputStream bis = new BufferedInputStream(get.getResponseBodyAsStream());
                int byteRead = 0;
                buffer = new ByteArrayOutputStream(1024);
                while ((byteRead = bis.read(temp)) != -1)
                    buffer.write(temp, 0, byteRead);
                response.setBody(buffer.toByteArray());
            }
        } finally {
            get.releaseConnection();
        }
    }

    private static final void doPost(HttpClient client, HttpRequest request, HttpResponse response) throws IOException {
        int i;
        ByteArrayOutputStream baos;
        PostMethod post = new PostMethod(request.getUrl());

        post.getParams().setParameter("http.protocol.content-charset", request.getCharset());
        if (request.getRetryCount() != 3) {
            post.getParams().setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler(request.getRetryCount(), false));
        }

        List list = request.getParameter();
        for (i = 0; i < CollectionUtil.size(list); ++i) {
            NameValuePair parameter = (NameValuePair) list.get(i);
            post.addParameter(parameter);
        }

        HttpState status = null;
        if (CollectionUtil.size(request.getCookie()) > 0) {
            status = new HttpState();
            List cookie = request.getCookie();
            for (i = 0; i < CollectionUtil.size(cookie); ++i) {
                Cookie c = (Cookie) cookie.get(i);
                status.addCookie(c);
            }

        }

        if (CollectionUtil.size(request.getHeader()) > 0) {
            List headerList = request.getHeader();
            for (i = 0; i < CollectionUtil.size(headerList); ++i) {
                Header header = (Header) headerList.get(i);
                post.addRequestHeader(header);
            }
        }

        try {
            if (request.getTimeout() > 0)
                client.getParams().setConnectionManagerTimeout(request.getTimeout());

            if (status != null)
                client.setState(status);

            if (request.getAuthentication()) {
                client.getState().setCredentials(request.getAuthScope(), request.getCredentials());
                post.setDoAuthentication(true);
            }

            int statusCode = client.executeMethod(post);
            Header[] header = post.getResponseHeaders();
            for (i = 0; i < StringUtil.size(header); ++i)
                response.addHeader(header[i].getName(), header[i].getValue());

            Cookie[] cookie = client.getState().getCookies();
            for (i = 0; i < StringUtil.size(cookie); ++i)
                response.addCookie(cookie[i].getName(), cookie[i]);

            if (statusCode != 200) {
                response.setSuccess(false);
                response.setStatusCode(statusCode);
            } else {
                response.setSuccess(true);

                if (!(request.getReceiveContent())) {
                    return;
                }

                byte[] buffer = new byte[1024];
                BufferedInputStream bis = new BufferedInputStream(post.getResponseBodyAsStream());
                int byteRead = 0;
                baos = new ByteArrayOutputStream(1024);
                while ((byteRead = bis.read(buffer)) != -1)
                    baos.write(buffer, 0, byteRead);
                response.setBody(baos.toByteArray());
            }
        } finally {
            post.releaseConnection();
        }
    }

    private static final void doMultipartPost(HttpClient client, HttpRequest request, HttpResponse response) throws IOException {
        int i;
        ByteArrayOutputStream buffer;
        PostMethod post = new PostMethod(request.getUrl());

        if ((!(CollectionUtil.isEmpty(request.getParameter()))) || (!(CollectionUtil.isEmpty(request.getFile())))) {
            List parameter = request.getParameter();
            List file = request.getFile();
            Part[] parts = new Part[CollectionUtil.size(parameter) + CollectionUtil.size(file)];
            i = 0;
            for (; i < CollectionUtil.size(parameter); ++i)
                parts[i] = ((StringPart) parameter.get(i));

            for (int j = 0; j < CollectionUtil.size(file);) {
                parts[i] = ((FilePart) file.get(j));

                ++j;
                ++i;
            }

            RequestEntity entity = new MultipartRequestEntity(parts, post.getParams());
            post.setRequestEntity(entity);
        }

        HttpState status = null;
        if (CollectionUtil.size(request.getCookie()) > 0) {
            status = new HttpState();
            List cookie = request.getCookie();
            for (i = 0; i < CollectionUtil.size(cookie); ++i) {
                Cookie c = (Cookie) cookie.get(i);
                status.addCookie(c);
            }

        }

        if (CollectionUtil.size(request.getHeader()) > 0) {
            List headerList = request.getHeader();
            for (i = 0; i < CollectionUtil.size(headerList); ++i) {
                Header header = (Header) headerList.get(i);
                post.addRequestHeader(header);
            }
        }

        try {
            if (request.getTimeout() > 0)
                client.getParams().setConnectionManagerTimeout(request.getTimeout());

            if (status != null)
                client.setState(status);

            if (request.getAuthentication()) {
                client.getState().setCredentials(request.getAuthScope(), request.getCredentials());
                post.setDoAuthentication(true);
            }

            int statusCode = 0;
            try {
                statusCode = client.executeMethod(post);
            } catch (SocketException e) {
                throw new IOException(e.getMessage());
            }
            Header[] header = post.getResponseHeaders();
            for (i = 0; i < StringUtil.size(header); ++i) {
                response.addHeader(header[i].getName(), header[i].getValue());
            }

            Cookie[] cookie = client.getState().getCookies();
            for (i = 0; i < StringUtil.size(cookie); ++i) {
                response.addCookie(cookie[i].getName(), cookie[i]);
            }

            if (statusCode != 200) {
                response.setSuccess(false);
                response.setStatusCode(statusCode);
            } else {
                response.setSuccess(true);

                if (!(request.getReceiveContent())) {
                    return;
                }

                byte[] temp = new byte[1024];
                BufferedInputStream bis = new BufferedInputStream(post.getResponseBodyAsStream());
                int byteRead = 0;
                buffer = new ByteArrayOutputStream(1024);
                while ((byteRead = bis.read(temp)) != -1)
                    buffer.write(temp, 0, byteRead);
                response.setBody(buffer.toByteArray());
            }
        } finally {
            post.releaseConnection();
        }
    }

    private static final void doHead(HttpClient client, HttpRequest request, HttpResponse response) throws IOException {
        HeadMethod head = new HeadMethod(request.getUrl());
        head.setFollowRedirects(request.getFollowRedirect());
        try {
            int statusCode = client.executeMethod(head);
            Header[] headers = head.getResponseHeaders();
            for (int i = 0; i < StringUtil.size(headers); ++i)
                response.addHeader(headers[i].getName(), headers[i].getValue());

            if (statusCode != 200) {
                response.setSuccess(false);
                response.setStatusCode(statusCode);
            } else {
                response.setSuccess(true);
                response.setCharset(head.getResponseCharSet());
            }
        } finally {
            head.releaseConnection();
        }
    }

    private static final void doOptions(HttpClient client, HttpRequest request, HttpResponse response) throws IOException {
        OptionsMethod options = new OptionsMethod(request.getUrl());
        try {
            int statusCode = client.executeMethod(options);
            if (statusCode != 200) {
                response.setSuccess(false);
                response.setStatusCode(statusCode);
            } else {
                response.setSuccess(true);
                response.setCharset(options.getResponseCharSet());

                Enumeration allowedMethods = options.getAllowedMethods();
                while (allowedMethods.hasMoreElements())
                    response.addOption((String) allowedMethods.nextElement());
            }
        } finally {
            options.releaseConnection();
        }
    }

    private static final void doTrace(HttpClient client, HttpRequest request, HttpResponse response) throws IOException {
        TraceMethod trace = new TraceMethod(request.getUrl());
        try {
            int statusCode = client.executeMethod(trace);
            if (statusCode != 200) {
                response.setSuccess(false);
                response.setStatusCode(statusCode);
            } else {
                response.setSuccess(true);
                response.setCharset(trace.getResponseCharSet());
            }
        } finally {
            trace.releaseConnection();
        }
    }

    private static final void doPut(HttpClient client, HttpRequest request, HttpResponse response) throws IOException {
        PutMethod put = new PutMethod(request.getUrl());

        if (!(CollectionUtil.isEmpty(request.getFile()))) {
            List file = request.getFile();
            File f = (File) file.get(0);
            put.setRequestEntity(new InputStreamRequestEntity(new FileInputStream(f)));
        }
        try {
            int statusCode = client.executeMethod(put);
            if (statusCode != 200) {
                response.setSuccess(false);
                response.setStatusCode(statusCode);
            } else {
                response.setSuccess(true);
                response.setCharset(put.getResponseCharSet());
            }
        } finally {
            put.releaseConnection();
        }
    }

    private static final void doDelete(HttpClient client, HttpRequest request, HttpResponse response) throws IOException {
        DeleteMethod delete = new DeleteMethod(request.getUrl());
        try {
            int statusCode = client.executeMethod(delete);
            if (statusCode != 200) {
                response.setSuccess(false);
                response.setStatusCode(statusCode);
            } else {
                response.setSuccess(true);
                response.setCharset(delete.getResponseCharSet());
            }
        } finally {
            delete.releaseConnection();
        }
    }

    private static class HttpWorker extends Thread {
        private HttpClient client;
        private HttpRequest request;
        private Hashtable table;

        public HttpWorker(HttpClient httpClient, HttpRequest request, Hashtable<String, HttpResponse> table) {
            this.client = httpClient;
            this.request = request;
            this.table = table;
        }

        public void run() {
            HttpResponse response;
            try {
                response = HttpManager.send(this.client, this.request);
                this.table.put(this.request.getUrl(), response);
            } catch (Exception e) {
                response = new HttpResponse();
                response.setSuccess(false);
                response.setStatusCode(-1);
                String message = e.getClass().toString().substring(6) + ": " + e.getMessage();
                response.setMessage(message);
                this.table.put(this.request.getUrl(), response);
            }
        }
    }
}