package com.rise.autotest.robot.selenium.locator;

public class LocatorFactory {

    private static LocatorFactory locatorFactory;

    //TODO: should be singletons??
    private Locators.IdLocator idLocator = new Locators.IdLocator();
    private Locators.NameLocator nameLocator = new Locators.NameLocator();
    private Locators.XPathLocator xPathLocator = new Locators.XPathLocator();
    private Locators.IdOrNameLocator idOrNameLocator = new Locators.IdOrNameLocator();
    private Locators.CssLocator cssLocator = new Locators.CssLocator();
    private Locators.LinkTextLocator linkTextLocator = new Locators.LinkTextLocator();
    private Locators.TagNameLocator tagNameLocator = new Locators.TagNameLocator();
    private Locators.SizzleLocator sizzleLocator = new Locators.SizzleLocator();
    private Locators.ClassNameLocator classNameLocator = new Locators.ClassNameLocator();

    private LocatorFactory(){}

    /**
     * initialize the instance of this class to be singleton
     * @return this instance {@code WebDriverManager}
     */
    public static synchronized LocatorFactory instance() {
        if(locatorFactory == null) {
            synchronized (LocatorFactory.class) {
                if(locatorFactory == null) {
                    locatorFactory = new LocatorFactory();
                }
            }
        }
        return locatorFactory;
    }

    public Locator parseLocator(String locator) {
        LocatorType locatorType = LocatorType.DEFAULT;

        if(locator.startsWith("//")) {
            locatorType = LocatorType.XPATH;
        } else if(locator.indexOf('=') != -1) {
            String[] locatorParts = locator.split("=", 2);
            if (locatorParts.length == 2) {
                String type = locatorParts[0].trim().toUpperCase();
                locatorType = LocatorType.valueOf(type);
            }
        }

        switch (locatorType) {
            case ID:
                return idLocator;
            case NAME:
                return nameLocator;
            case XPATH:
                return xPathLocator;
            case IDORNAME:
                return idOrNameLocator;
            case CSS:
                return cssLocator;
            case CLASSNAME:
                return classNameLocator;
            case TAG:
                return tagNameLocator;
            case JQUERY:
            case SIZZLE:
                return sizzleLocator;
            case LINKTEXT:
                return linkTextLocator;
            default:
                return idLocator;
        }
    }
}
