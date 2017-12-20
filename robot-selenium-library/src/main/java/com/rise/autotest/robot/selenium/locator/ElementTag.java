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
        try {
            Tag tag =  Tag.valueOf(tagName.toUpperCase());
            return new ElementTag(tag.tagName, "type", tag.type);
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

    public enum Tag {
        LINK("a", null),
        IMAGE("img", null),
        CHECKBOX("input", "checkbox"),
        RADIO("input", "radio"),
        TEXT("input", "text"),
        PASSWORD("input","password"),
        FILEUPLOAD("input", "file"),
        SUBMIT("input", "submit"),
        RESET("input", "reset"),
        BUTTON("button", null),
        INPUTBUTTON("input", "button"),
        TEXTAREA("textarea", null),
        LIST("select", null),
        FORM("form", null);

        Tag(String tagName, String type) {
            this.tagName = tagName;
            this.type = type;
        }
        private final String tagName;
        private final String type;
    }

}
