package com.rise.autotest.robot.selenium.locator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocatorContext {
    private String criteria;
    private ElementTag tag;
    private boolean required;
}
