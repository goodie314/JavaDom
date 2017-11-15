package javadom;

import javadom.enums.TagType;

/**
 * Created by u6062536 on 11/15/2017.
 */
public class HtmlTag {

    private String tag;
    private TagType tagType;
    private String tagName;
    private String tagProperties;
    private int pageLocation;

    protected HtmlTag(String tag, TagType tagType, String tagName, int pageLocation) {
        this.tag = tag;
        this.tagType = tagType;
        this.tagName = tagName;
        this.pageLocation = pageLocation;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public TagType getTagType() {
        return tagType;
    }

    public void setTagType(TagType tagType) {
        this.tagType = tagType;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagProperties() {
        return tagProperties;
    }

    public void setTagProperties(String tagProperties) {
        this.tagProperties = tagProperties;
    }

    public int getPageLocation() {
        return pageLocation;
    }

    public void setPageLocation(int pageLocation) {
        this.pageLocation = pageLocation;
    }

    public String toString() {
        return this.tagName + "," + this.tagType + "," + this.pageLocation;
    }

    public boolean equals(HtmlTag tag) {
        return this.tagName.equals(tag.getTagName());
    }
}