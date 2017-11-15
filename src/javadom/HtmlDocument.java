package javadom;

public class HtmlDocument {

    private String documentType;
    private String html;

    public HtmlDocument() {
    }

    public HtmlDocument parseDocument(String html) {
        String html5 = "<!doctype html>";

        html = html.trim().toLowerCase();
        if (html.startsWith(html)) {
            this.documentType = "html5";
            html = html.substring(html5.length());
        }

        
        return this;
    }
}
