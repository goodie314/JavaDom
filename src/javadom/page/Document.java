package javadom.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by u6062536 on 2/1/2018.
 */
public class Document {

    private Node root;
    private List<Node> explodedDom;

    private Document(Node root) {
        this.root = root;
        this.explodedDom = this.explodeDOM(root);
    }

    public Node getRoot() {
        return this.root;
    }

    public List<Node> getElementsByClassName(String className) {
        return this.explodedDom.stream()
                .filter(node -> {
                    String val = node.getAttribute("class");
                    if (val == null) {
                        return false;
                    }
                    else {
                        return val.equals(className);
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<Node> explodeDOM(Node root) {
        List<Node> dom;

        dom = new ArrayList<>();
        dom.add(root);
        for (Node child : root.getChildren()) {
            dom.addAll(explodeDOM(child));
        }
        return dom;
    }

    private static final Pattern tagsPattern = Pattern.compile("<(\\/)?(.*?)(\\/)?>", Pattern.CASE_INSENSITIVE & Pattern.DOTALL);
    private static final Pattern htmlVersionPattern = Pattern.compile("<\\s*!.*?>");

    public static Document parseHtml(String html) throws Exception {
        Matcher matcher;
        Node rootNode;
        Node currentNode;
        Node tempNode;

        rootNode = null;
        currentNode = null;

        matcher = htmlVersionPattern.matcher(html);
        if (matcher.find()) {
            html = html.substring(matcher.end());
        }

        matcher = tagsPattern.matcher(html);
        while(matcher.find()) {
            // end tag
            if (matcher.group(1) != null) {
                if (currentNode == null) {
                    throw new Exception("End tag before start tag");
                }
                currentNode = currentNode.getParent();
            }
            // self closing tag
            else if (matcher.group(3) != null) {
                tempNode = new Node(matcher.group(2));
                if (currentNode != null) {
                    currentNode.addChild(tempNode);
                }
            }
            // open tag
            else {
                tempNode = new Node(matcher.group(2));
                if (tempNode.isSelfClosing()) {
                    if (currentNode != null) {
                        currentNode.addChild(tempNode);
                        continue;
                    }
                    else {
                        throw new Exception("root cannot be a self closing tag");
                    }
                }
                if (currentNode != null) {
                    tempNode.setParent(currentNode);
                    currentNode.addChild(tempNode);
                    currentNode = tempNode;
                }
                else {
                    rootNode = tempNode;
                    currentNode = tempNode;
                }
            }
        }

        return new Document(rootNode);
    }
}
