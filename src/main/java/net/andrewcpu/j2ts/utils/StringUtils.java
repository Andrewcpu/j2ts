package net.andrewcpu.j2ts.utils;

public class StringUtils {
    public static final String STANDARD_SPACING = "  ";

    public static String getSpacing(int indentation) {
        String spacing = "";
        for(int i= 0 ; i<indentation; i++) {
            spacing += STANDARD_SPACING;
        }
        return spacing;
    }

    public static String toKebabCase(String className) {
        return className.replaceAll("([a-z])([A-Z]+)", "$1-$2").toLowerCase();
    }
}
