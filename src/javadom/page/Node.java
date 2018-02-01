package javadom.page;

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
            Pattern.compile("(^[a-zA-Z]+)|(?:([a-zA-Z]+)\\s*=(?:\\s*['\"](.*?)['\"]))|(?:([a-zA-Z]+)=([^\\s]+))",
                    Pattern.DOTALL);

    private static final String[] selfClosingTags = {
            "area", "base", "br", "col", "command", "embed", "hr", "img", "input", "keygen", "link", "menuitem",
            "meta", "param", "source", "track", "wbr"
    };

    private Node parent;
    private List<Node> children;

    private String name;
    private Map<String, String> attributes;

    public Node(String nodeBody) {
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

    public boolean isSelfClosing() {
        for (String tagName : selfClosingTags) {
            if (tagName.equalsIgnoreCase(this.name)) {
                return true;
            }
        }
        return false;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Node: ").append(this.name).append("\n");
        for (String key : this.attributes.keySet()) {
            builder.append(key).append(": ").append(this.attributes.get(key)).append("\n");
        }
        return builder.toString();
    }
}
