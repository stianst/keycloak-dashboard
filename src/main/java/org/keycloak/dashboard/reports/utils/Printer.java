package org.keycloak.dashboard.reports.utils;

public class Printer {

    private static boolean ON_NEW_LINE = true;

    public static void printTableHeader(String label) {
        System.out.println("==========================================================================================");
        System.out.println(label);
        System.out.println("==========================================================================================");
    }

    public static void printColumnHeaders(String... labels) {
        for (String l : labels) {
            print(l);
        }
        println();
    }

    public static void print(Object s) {
        print(s.toString());
    }

    public static void print(String s) {
        print(s, 20);
    }

    public static void print(String s, int l) {
        if (ON_NEW_LINE) {
            System.out.print(s);
            ON_NEW_LINE = false;
        } else {
            System.out.print("," + s);
        }
    }

    public static void println() {
        System.out.println();
        ON_NEW_LINE = true;
    }

}
