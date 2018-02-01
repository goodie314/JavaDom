package javadom;

import javadom.http.HttpResponse;
import javadom.http.HttpService;
import javadom.page.Document;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class JavaDom {

    private String html;
    private String protocol;
    private int port;
    private String url;
    private String baseUrl;
    private String relativeUrl;

    public JavaDom() {

    }

    public JavaDom(String html) {
        this.html = html;
    }

    public Document getPage(String url) throws Exception {
//        String getRequest = "GET %s %s/1.1 \r\nHost: %s\r\n\r\nCache-Control: no-cache\r\n";
//
//        Socket sock;
//        BufferedReader reader;
//        DataOutputStream writer;
//        String line;
//        StringBuilder responseBuilder;
//        String response;
//
//        this.parseUrl(url);
//        sock = new Socket(this.baseUrl, this.port);
//        reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//        writer = new DataOutputStream(sock.getOutputStream());
//        getRequest = String.format(getRequest, this.relativeUrl, this.protocol, this.baseUrl);
//        writer.write(getRequest.getBytes());
//        writer.flush();
//        responseBuilder = new StringBuilder();
//        while ((line = reader.readLine()) != null) {
//            responseBuilder.append(line);
//            responseBuilder.append("\n");
//        }
//        response = responseBuilder.toString();
//        this.html = this.stripResponseHeaders(response);
        HttpResponse response = HttpService.get(url);
        this.html = response.getResponseBody();
        return Document.parseHtml(this.html);
    }

    private void parseUrl(String url) {
        int index;

        this.url = url;

        index = url.indexOf("://");
        if (index != -1) {
            this.protocol = url.substring(0, index).toUpperCase();
            url = url.substring(index + 3);
        }
        else {
            this.protocol = "HTTP";
        }

        index = url.indexOf("/");
        if (index != -1) {
            this.relativeUrl = url.substring(index);
            url = url.substring(0, index);
        } else {
            this.relativeUrl = "/";
        }

        index = url.indexOf(":");
        if (index != -1) {
            this.port = Integer.parseInt(url.substring(index + 1));
            url = url.substring(0, index);
        }
        else {
            this.port = 80;
        }
        this.baseUrl = url;
    }

    public String getHtml() {
        return this.html;
    }
}
