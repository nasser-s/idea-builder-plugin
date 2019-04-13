package com.ns.plugin.util;

/**
 * Created by Sadeghi on 4/13/19.
 *
 * @author Sadeghi
 */
public class Util {
    public Util() {
    }

    public static String setterName(String name){
        return "set"+Character.toUpperCase(name.charAt(0))+name.substring(1);
    }
    public static String getterName(String name){
        return "get"+Character.toUpperCase(name.charAt(0))+name.substring(1);
    }
}
