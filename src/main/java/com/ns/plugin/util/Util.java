package com.ns.plugin.util;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;

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

    public static String getterNameEx(String name, PsiField field) {

        System.out.println("Getter name for " + name + " " + field.getType().getCanonicalText());
        if (field.getType().getCanonicalText().equals("boolean")) {
            return "is" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        } else {
            return "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        }
    }
}
