package javadom;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class JavaDom {

    private String html;
    private String protocol;
    private String url;
    private String baseUrl;
    private String relativeUrl;

    public JavaDom() {

    }

    public JavaDom(String html) {
        this.html = html;
    }

    public void getPage(String url) throws IOException {
        String getRequest = "GET %s %s/1.0 \r\n\r\n";
        int httpPort = 80;

        Socket sock;
        BufferedReader reader;
        DataOutputStream writer;
        String line;
        StringBuilder responseBuilder;
        String response;

        this.parseUrl(url);
        sock = new Socket(this.baseUrl, httpPort);
        reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        writer = new DataOutputStream(sock.getOutputStream());
        getRequest = String.format(getRequest, this.relativeUrl, this.protocol);
        writer.write(getRequest.getBytes());
        writer.flush();
        responseBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
            responseBuilder.append("\n");
        }
        response = responseBuilder.toString();
        this.html = this.stripResponseHeaders(response);
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

        this.baseUrl = url;
    }

    private String stripResponseHeaders(String response) {
        int index;
        String html;

        index = response.indexOf("\n\n");
        html = response.substring(index);
        html = html.trim();
        return html;
    }

    public String getHtml() {
        return this.html;
    }
}
