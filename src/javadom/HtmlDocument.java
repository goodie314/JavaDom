package javadom;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlDocument {

    private static final Pattern htmlWrapper = Pattern.compile("^<\\s*html\\s*>.*<\\s*/html\\s*>$");

    private String documentType;
    private String html;
    private HtmlElement root;

    private HtmlDocument(String documentType, HtmlElement root, String html) {
        this.documentType = documentType;
        this.root = root;
        this.html = html;
    }

    public static HtmlDocument parseDocument(String html) {
        String html5 = "<!doctype html>";

        String documentType;
        String fullHtml;
        Matcher matcher;
        HtmlElement root;

        html = html.trim();
        fullHtml = html;
        if (html.startsWith(html)) {
            documentType = "html5";
            html = html.substring(html5.length());
        } else {
            documentType = "?";
        }

        matcher = htmlWrapper.matcher(html);
        if (!matcher.find()) {
            html = "<html>" + html + "</html>";
        }

        root = HtmlElement.parseHtml(html);
        
        return new HtmlDocument(documentType, root, fullHtml);
    }

    public HtmlElement getRoot() {
        return this.root;
    }
}
