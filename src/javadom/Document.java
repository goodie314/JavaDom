package javadom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
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

        SearchUtils.traverseDom(root, (elem) -> {
            if (elem.getTag().getTagName().equals(tagName)) {
                found.add(elem);
            }
        });

        return found;
    }

    public List<HtmlElement> getElementsByClass(String className) {
        List<HtmlElement> found = new ArrayList<>();

        SearchUtils.traverseDom(root, (elem) -> {
            String c = elem.getProperty("class");
            if (c != null && c.equals(className)) {
                found.add(elem);
            }
        });

        return found;
    }

    public List<HtmlElement> find(String selector) {
        String tag = null;
        String className = null;
        String id = null;
        String methodSelector = null;
        String attributeKey = null;
        String attributeValue = null;
        Map<String, String> attributeMap = new HashMap<>();
        List<HtmlElement> found = new ArrayList<>();
        boolean foundPattern = false;
        boolean subSearch = false;
        Pattern queryPattern = Pattern.compile("([a-zA-Z*]*)\\.?([a-zA-Z*]*)#?([a-zA-Z*]*):?([a-zA-Z*]*)");
        Pattern attributePattern = Pattern.compile("\\[([a-zA-Z]*?)=(.*?)\\]|\\[([a-zA-Z]*?)\\]");

        Matcher matcher = queryPattern.matcher(selector);

        if (matcher.find()) {
            foundPattern = true;
            tag = matcher.group(1);
            className = matcher.group(2);
            id = matcher.group(3);
            methodSelector = matcher.group(4);
        }

        matcher = attributePattern.matcher(selector);
        while (matcher.find()) {
            attributeKey = matcher.group(1);
            attributeValue = matcher.group(2);
            if (attributeKey != null && attributeValue != null) {
                foundPattern = true;
                attributeMap.put(attributeKey, attributeValue);
            }
        }

        if (!foundPattern) {
            return Collections.emptyList();
        }

        if (tag != null && !tag.equals("")) {
            if (subSearch) {
                found = SearchUtils.tagSearch(tag, subSearch, found, root);
            }
            else {
                found.addAll(SearchUtils.tagSearch(tag, subSearch, found, root));
            }
            subSearch = true;
        }

        if (className != null && !className.equals("")) {
            if (subSearch) {
                found = SearchUtils.classNameSubSearch(className, found);
            }
            else {
                found.addAll(SearchUtils.classNameSearch(className, this.root));
            }
            subSearch = true;
        }

        return found;
    }
}
