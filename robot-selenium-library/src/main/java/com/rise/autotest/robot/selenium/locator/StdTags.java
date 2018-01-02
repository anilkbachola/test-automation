package com.rise.autotest.robot.selenium.locator;

public enum StdTags {

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

    private final String tagName;
    private final String type;

    StdTags(String tagName, String type) {
        this.tagName = tagName;
        this.type = type;
    }

    public String getTagName() {
        return tagName;
    }

    public String getType() {
        return type;
    }
}
