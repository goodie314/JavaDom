package javadom;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Document {

    private static final Pattern htmlWrapper = Pattern.compile("^<\\s*html\\s*>.*<\\s*/html\\s*>$");

    private String documentType;
    private String html;
    private HtmlElement root;

    private Document(String documentType, HtmlElement root, String html) {
        this.documentType = documentType;
        this.root = root;
        this.html = html;
    }

    public static Document parseDocument(String html) {
        String html5 = "<!doctype html>";

        String documentType;
        Matcher matcher;
        HtmlElement root;

        html = html.trim();
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
        
        return new Document(documentType, root, html);
    }

    public HtmlElement getRoot() {
        return this.root;
    }

    public List<HtmlElement> getElementsByTagName(String tagName) {
        List<HtmlElement> found = new ArrayList<>();

        this.traverseDom(root, (elem) -> {
            if (elem.getTag().getTagName().equals(tagName)) {
                found.add(elem);
            }
        });

        return found;
    }

    public List<HtmlElement> getElementsByClass(String className) {
        List<HtmlElement> found = new ArrayList<>();

        this.traverseDom(root, (elem) -> {
            String c = elem.getProperty("class");
            if (c != null && c.equals(className)) {
                found.add(elem);
            }
        });

        return found;
    }

    private void traverseDom(HtmlElement element, Consumer<HtmlElement> callBack) {
        callBack.accept(element);

        element.getChildren().forEach((child) -> traverseDom(child, callBack));
    }
}
