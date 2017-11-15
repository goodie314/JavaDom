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
    private int tagIndex;

    private HtmlElement(HtmlElement parent, HtmlTag tag, int tagIndex) {
        this.parent = parent;
        this.tag = tag;
        this.tagIndex = tagIndex;
    }

    public static HtmlElement parseHtml(String html) {
        HtmlElement root;
        int closingIndex;

        HtmlElement.html = html;

        populateTagLists(html);

        if (tags.size() == 0) {
            return null;
        }

        root = new HtmlElement(null, tags.get(0), 0);
        closingIndex = getClosingTag(0);
        if (closingIndex < 0) {
            return null;
        }

        parseChildren(root, 0);
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

    private static int getClosingTag(int tagIndex) {
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

    private static void parseChildren(HtmlElement parent, int tagIndex) {
        int endIndex;
        int temp;
        int subEndIndex = tagIndex;
        HtmlElement elem;

        endIndex = getClosingTag(tagIndex);
        while (subEndIndex < (endIndex - 1)) {
            subEndIndex++;
            elem = new HtmlElement(parent, tags.get(subEndIndex), subEndIndex);
            parent.children.add(elem);

            temp = getClosingTag(subEndIndex);
            if (temp > 0) {
                subEndIndex = temp;
            }
        }

        for (HtmlElement child : parent.children) {
            parseChildren(child, child.tagIndex);
        }
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
