package javadom.page;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by u6062536 on 2/1/2018.
 */
public class Node {

    private static final Pattern bodyPattern =
            Pattern.compile("(^[\\w]+)|(?:([\\w]+)\\s*=(?:\\s*['\"](.*?)['\"]))|(?:([a-zA-Z]+)=([^\\s]+))",
                    Pattern.DOTALL);

    private static final String[] selfClosingTags = {
            "area", "base", "br", "col", "command", "embed", "hr", "img", "input", "keygen", "link", "menuitem",
            "meta", "param", "source", "track", "wbr"
    };

    private String url;

    private Node parent;
    private List<Node> children;

    private Integer startInner;

    private String name;
    private Map<String, String> attributes;
    private String innerText;

    public Node(String url, String nodeBody) {
        this.url = url;
        this.children = new ArrayList<>();
        this.attributes = new HashMap<>();
        this.parseBody(nodeBody);
    }

    public Node getParent() {
        return this.parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    public List<Node> getChildren() {
        return this.children;
    }

    public String getName() {
        return this.name;
    }

    public String getAttribute(String name) {
        return this.attributes.get(name);
    }

    protected boolean isSelfClosing() {
        for (String tagName : selfClosingTags) {
            if (tagName.equalsIgnoreCase(this.name)) {
                return true;
            }
        }
        return false;
    }

    public String absoluteUrl(String attributeName) {
        Pattern pattern;
        Matcher baseMatcher;
        Matcher attrMatcher;
        String attrUrl;
        StringBuilder absUrl;

        pattern = Pattern.compile("(https?:\\/\\/)?(www\\.)?([^\\/#?]+)?(.*)");
        baseMatcher = pattern.matcher(this.url);
        attrUrl = this.getAttribute(attributeName);
        if (attrUrl == null) {
            return this.url;
        }
        attrMatcher = pattern.matcher(attrUrl);
        if (!baseMatcher.find()) {
            return this.url;
        }
        if (!attrMatcher.find()) {
            return this.url;
        }
        absUrl = new StringBuilder();
        if (attrMatcher.group(1) == null && baseMatcher.group(1) != null) {
            absUrl.append(baseMatcher.group(1));
        }
        else {
            absUrl.append(attrMatcher.group(1));
        }
        if (attrMatcher.group(2) == null && baseMatcher.group(2) != null) {
            absUrl.append(baseMatcher.group(2));
        }
        else {
            absUrl.append(attrMatcher.group(2));
        }
        if (attrMatcher.group(3) == null && baseMatcher.group(3) != null) {
            absUrl.append(baseMatcher.group(3));
        }
        else {
            absUrl.append(attrMatcher.group(3));
        }
        absUrl.append(attrMatcher.group(4));
        return absUrl.toString();
    }

    private void parseBody(String body) {
        Matcher matcher;
        String key;
        String value;

        matcher = bodyPattern.matcher(body);
        while(matcher.find()) {
            if (matcher.group(1) != null) {
                this.name = matcher.group(1);
            }
            else if (matcher.group(2) != null && matcher.group(3) != null) {
                key = matcher.group(2);
                value = matcher.group(3);
                this.attributes.put(key, value);
            }
            else if (matcher.group(4) != null && matcher.group(5) != null) {
                key = matcher.group(4);
                value = matcher.group(5);
                this.attributes.put(key, value);
            }
        }
    }

    protected void setStartInner(Integer startInner) {
        this.startInner = startInner;
    }

    protected void setInnerText(Integer endInner, String html) {
        this.innerText = html.substring(this.startInner, endInner);
    }

    public String getInnerText() {
        return this.innerText;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Node: ").append(this.name).append("\n");
        if (this.parent != null) {
            builder.append("Parent: ").append(this.parent.getName()).append("\n");
        }
        for (String key : this.attributes.keySet()) {
            builder.append(key).append(": ").append(this.attributes.get(key)).append("\n");
        }
        return builder.toString();
    }

    public String toJSON() {
        boolean start = true;
        StringBuilder builder = new StringBuilder("{");

        builder.append("\"name\":\"").append(this.name).append("\",");
        builder.append("\"attributes\":[");
        for (String key : this.attributes.keySet()) {
            if (start) {
                builder.append("{\"").append(key).append("\":\"").append(this.attributes.get(key)).append("\"}");
                start = false;
            }
            else {
                builder.append(",{\"").append(key).append("\":\"").append(this.attributes.get(key)).append("\"}");
            }
        }
        builder.append("],");
        builder.append("\"children\":[");
        start = true;
        for (Node node : this.children) {
            if (start) {
                builder.append(node.toJSON());
                start = false;
            }
            else {
                builder.append(",").append(node.toJSON());
            }
        }
        builder.append("]}");

        return builder.toString();
    }
}
