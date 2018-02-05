package javadom;

import javadom.http.HttpResponse;
import javadom.http.HttpService;
import javadom.page.Document;

public class JavaDom {

    private JavaDom() {
    }

    public static Document getPage(String url) throws Exception {
        HttpResponse response = HttpService.get(url);
        String html = response.getResponseBody();
        return Document.parseHtml(url, response.getContentType(), html);
    }

    public static Document parseHtml(String url, String html) throws Exception {
        return Document.parseHtml(url, "text/html", html);
    }

}
