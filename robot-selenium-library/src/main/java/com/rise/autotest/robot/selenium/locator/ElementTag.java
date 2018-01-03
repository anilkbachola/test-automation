package com.rise.autotest.robot.selenium.locator;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ElementTag {
    private final String tagName;
    private final Map<String, String> attributes = new HashMap<>();

    private ElementTag(String tagName) {
        this.tagName = tagName;
    }

    private ElementTag(String tagName, String attrName, String attrVal) {
        this.tagName = tagName;
        if(attrName != null && attrVal != null) {
            this.attributes.put(attrName, attrVal);
        }
    }

    public static ElementTag fromName(String tagName) {
        if(tagName == null || tagName.isEmpty()) {
            return  null;
        }
        try {
            StdTags tag =  StdTags.valueOf(tagName.toUpperCase());
            return new ElementTag(tag.getTagName(), "type", tag.getType());
        } catch (IllegalArgumentException ex) {
            //ignore
        }
        return new ElementTag(tagName);
    }

    public void addAttribute(String attrName, String attrVal) {
        this.attributes.put(attrName, attrVal);
    }

    public void addAttributes(Map<String, String> attributes) {
        if(attributes != null && !attributes.isEmpty()) {
            this.attributes.putAll(attributes);
        }
    }

    public void removeAttribute(String attrName) {
        this.attributes.remove(attrName);
    }


}
