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

    private JavaDom() {
    }

    public static Document getPage(String url) throws Exception {
        HttpResponse response = HttpService.get(url);
        String html = response.getResponseBody();
        return Document.parseHtml(html);
    }

    public static Document parseHtml(String url, String html) throws Exception {
        return Document.parseHtml(html);
    }

//    private void parseUrl(String url) {
//        int index;
//
//        this.url = url;
//
//        index = url.indexOf("://");
//        if (index != -1) {
//            this.protocol = url.substring(0, index).toUpperCase();
//            url = url.substring(index + 3);
//        }
//        else {
//            this.protocol = "HTTP";
//        }
//
//        index = url.indexOf("/");
//        if (index != -1) {
//            this.relativeUrl = url.substring(index);
//            url = url.substring(0, index);
//        } else {
//            this.relativeUrl = "/";
//        }
//
//        index = url.indexOf(":");
//        if (index != -1) {
//            this.port = Integer.parseInt(url.substring(index + 1));
//            url = url.substring(0, index);
//        }
//        else {
//            this.port = 80;
//        }
//        this.baseUrl = url;
//    }

}
