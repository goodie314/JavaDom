package javadom;

import javadom.enums.TagType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HtmlElement {

    private static final Pattern openTagPattern = Pattern.compile("<\\s*([a-zA-Z]+).*?(/)?\\s*>");
    private static final Pattern closeTagPattern = Pattern.compile("<\\s*/\\s*([a-zA-Z]+)\\s*>");

    private static List<HtmlTag> tags = new ArrayList<>();

    private static String html;

    private HtmlElement parent;
    private List<HtmlElement> children = new ArrayList<>();
    private HtmlTag tag;
    private String innerHtml;

    private HtmlElement(HtmlElement parent, HtmlTag tag) {
        this.parent = parent;
        this.tag = tag;
    }

    public static HtmlElement parseHtml(String html) {
        HtmlElement root;
        int closingIndex;

        HtmlElement.html = html;

        populateTagLists(html);

        if (tags.size() == 0) {
            return null;
        }

        root = new HtmlElement(null, tags.get(0));
        closingIndex = getClosingTag(0, tags);
        if (closingIndex < 0) {
            return null;
        }

        parseChildren(root, tags.subList(1, closingIndex));
        return root;
    }

    private static void populateTagLists(String html) {
        Matcher matcher;

        matcher = openTagPattern.matcher(html);
        while (matcher.find()) {
            if (matcher.group(2) == null) {
                tags.add(new HtmlTag(matcher.group(0), TagType.OPEN, matcher.group(1), matcher.end()));
            }
            else {
                tags.add(new HtmlTag(matcher.group(0), TagType.OPEN_CLOSE, matcher.group(1), matcher.start()));
            }
        }

        matcher = closeTagPattern.matcher(html);
        while(matcher.find()) {
            tags.add(new HtmlTag(matcher.group(0), TagType.CLOSE, matcher.group(1), matcher.start()));
        }

        tags = tags.stream()
                .sorted(Comparator.comparingInt(HtmlTag::getPageLocation))
                .collect(Collectors.toList());
    }

    private static int getClosingTag(int tagIndex, List<HtmlTag> tags) {
        int openCount;
        HtmlTag openTag;
        HtmlTag closeTag;
        int currentIndex;

        openTag = tags.get(tagIndex);
        openCount = 1;

        currentIndex = tagIndex;
        while (openCount > 0) {
            currentIndex++;
            if (currentIndex == tags.size()) {
                return -1;
            }
            closeTag = tags.get(currentIndex);
            switch (closeTag.getTagType()) {
                case OPEN:
                    if (openTag.equals(closeTag)) {
                        openCount++;
                    }
                    break;
                case CLOSE:
                    if (openTag.equals(closeTag)) {
                        openCount--;
                    }
                    break;
                default:
                    break;
            }
        }

        return currentIndex;
    }

    private static void parseChildren(HtmlElement parent, List<HtmlTag> remaining) {
        List<HtmlElement> children = new ArrayList<>();

        HtmlElement child;
        String innerHtml;
        HtmlTag openTag;
        HtmlTag closeTag;
        int childEndIndex;

        if (remaining.size() == 0) {
            return;
        }

        openTag = remaining.get(0);
        childEndIndex = getClosingTag(0, remaining);

        child = new HtmlElement(parent, openTag);
        children.add(child);

        if (childEndIndex == -1 || childEndIndex == 0) {
            children.add(child);
            parent.setChildren(children);
        }

        if (childEndIndex == 1) {
            closeTag = tags.get(1);
//            innerHtml = html.substring(openTag.getPageLocation(), closeTag.getPageLocation());
//            child.setInnerHtml(innerHtml);
            children.add(child);
            parent.setChildren(children);
        }

        if (childEndIndex > 0) {
            parseChildren(child, remaining.subList(1, remaining.size()));
        }
        if (childEndIndex < 0) {
            childEndIndex = 1;
        }
        parseChildren(parent, remaining.subList(childEndIndex, remaining.size()));
        parent.setChildren(children);
//        while (remaining.size() > 0) {
//            child.setChildren(parseChildren(child, remaining.subList(0, childEndIndex)));
//
//            remaining = remaining.subList(childEndIndex, remaining.size() - 1);
//            child = new HtmlElement(parent, remaining.get(0));
//        }

    }

    public HtmlElement getParent() {
        return parent;
    }

    public void setParent(HtmlElement parent) {
        this.parent = parent;
    }

    public List<HtmlElement> getChildren() {
        return children;
    }

    public void setChildren(List<HtmlElement> children) {
        this.children = children;
    }

    public HtmlTag getTag() {
        return tag;
    }

    public void setTag(HtmlTag tag) {
        this.tag = tag;
    }

    public String getInnerHtml() {
        return innerHtml;
    }

    public void setInnerHtml(String innerHtml) {
        this.innerHtml = innerHtml;
    }
}
