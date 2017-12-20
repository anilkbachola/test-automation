package com.rise.autotest.robot.selenium.util;

import java.util.Arrays;

public class Util {

    private Util() {

    }

    public static String escapeXpathValue(String value) {
        if (value.contains("\"") && value.contains("'")) {
            String[] partsWoApos = value.split("'");
            return String.format("concat('%s')", String.join("', \"'\", '", Arrays.asList(partsWoApos)));
        }
        if (value.contains("'")) {
            return String.format("\"%s\"", value);
        }
        return String.format("'%s'", value);
    }

    public static void main(String[] args) {
        System.out.println( Util.class.getCanonicalName());
        System.out.println(Util.class.getPackage().getName());
        System.out.println(Util.class.getPackage());
        System.out.println(Util.class.getSimpleName());
        System.out.println(Util.class.getTypeName());
    }

}
