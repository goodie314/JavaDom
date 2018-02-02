package javadom.page;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by u6062536 on 2/1/2018.
 */
public class Document {

    private Node root;
    private List<Node> explodedDom;

    private static final Pattern querySelectorPattern = Pattern.compile("([a-zA-Z]+)*(?:#(\\w+))?(?:\\.(\\w+))?",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern attributeSelectorPattern = Pattern.compile("\\[([^=]+)=?(?:[\"'](.*?)[\"'])?\\]",
            Pattern.CASE_INSENSITIVE);

    private Document(Node root) {
        this.root = root;
        this.explodedDom = this.explodeDOM(root);
        System.out.println("DOM size: " + this.explodedDom.size());
        this.explodedDom.forEach(System.out::println);
    }

    public Node getRoot() {
        return this.root;
    }

    public List<Node> querySelector(String query) {
        Matcher matcher;
        List<Node> searchList;
        List<Node> resultList;
        String attributeName;
        String attributeValue;
        String attributeQuery;

        matcher = attributeSelectorPattern.matcher(query);
        if (matcher.find()) {
            attributeQuery = query.substring(matcher.start());
            query = query.substring(0, matcher.start());
        }
        else {
            attributeQuery = "";
        }

        matcher = querySelectorPattern.matcher(query);
        searchList = this.explodedDom;
        resultList = new ArrayList<>();


        while(matcher.find()) {
            if (matcher.group(1) != null) {
                resultList = SearchUtil.getElementsByTagName(matcher.group(1), searchList);
                searchList = resultList;
            }
            if (matcher.group(2) != null) {
                resultList = SearchUtil.getElementById(matcher.group(2), searchList);
                searchList = resultList;
            }
            if (matcher.group(3) != null) {
                resultList = SearchUtil.getElementsByClassName(matcher.group(3), searchList);
                searchList = resultList;
            }
        }

        matcher = attributeSelectorPattern.matcher(attributeQuery);
        while (matcher.find()) {
            attributeName = matcher.group(1);
            attributeValue = matcher.group(2);
            resultList = SearchUtil.getElementsByAttribute(attributeName, attributeValue, searchList);
            searchList = resultList;
        }

        return resultList;
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
                .collect(Collectors.toList());
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

    private static final Pattern tagsPattern = Pattern.compile("<(\\/)?(.*?)(\\/)?>");
    private static final Pattern scriptTagsPattern = Pattern.compile("<script.*?>(.*?)<\\/script.*?>", Pattern.CASE_INSENSITIVE);
    private static final Pattern htmlVersionPattern = Pattern.compile("<\\s*!.*?>");

    public static Document parseHtml(String html) throws Exception {
        Matcher matcher;
        Node rootNode;
        Node currentNode;
        Node tempNode;
        boolean insideScriptTag;
        String innerText;
        StringBuilder builder;

        rootNode = null;
        currentNode = null;
        insideScriptTag = false;

        // remove <!DOCTYPE> node from html
        matcher = htmlVersionPattern.matcher(html);
        if (matcher.find()) {
            html = html.substring(matcher.end());
        }

        // pad script tags with new lines so that text inside scripts is not mistaken for html
        matcher = scriptTagsPattern.matcher(html);
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                innerText = "\n" + html.substring(matcher.start(1), matcher.end(1)) + "\n";
                builder = new StringBuilder(html.substring(0, matcher.start(1)));
                builder.append(innerText);
                builder.append(html.substring(matcher.end(1)));
                html = builder.toString();
                matcher = scriptTagsPattern.matcher(html);
            }
        }

        matcher = tagsPattern.matcher(html);
        while(matcher.find()) {
            // end tag
            if (matcher.group(1) != null) {
                tempNode = new Node(matcher.group(2));
                if (tempNode.getName().equalsIgnoreCase("script")) {
                    insideScriptTag = false;
                }
                if (insideScriptTag) {
                    continue;
                }
                if (currentNode == null) {
                    throw new Exception("End tag before start tag");
                }
                currentNode = currentNode.getParent();
            }
            // self closing tag
            else if (matcher.group(3) != null) {
                if (insideScriptTag) {
                    continue;
                }
                tempNode = new Node(matcher.group(2));
                if (currentNode != null) {
                    currentNode.addChild(tempNode);
                }
            }
            // open tag
            else {
                tempNode = new Node(matcher.group(2));
                if (insideScriptTag) {
                    continue;
                }
                if (tempNode.getName().equalsIgnoreCase("script")) {
                    insideScriptTag = true;
                }
                if (tempNode.isSelfClosing()) {
                    if (currentNode != null) {
                        currentNode.addChild(tempNode);
                        tempNode.setParent(currentNode);
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
