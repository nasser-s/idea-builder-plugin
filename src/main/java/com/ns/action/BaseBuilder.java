package com.ns.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.ns.plugin.util.Util;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Sadeghi on 4/13/19.
 *
 * @author Sadeghi
 */
public abstract class BaseBuilder extends AnAction {
    protected String getBuilderName(PsiJavaFile currentPsiJavaFile) {
        String selectedClassName = currentPsiJavaFile.getName().substring(0, currentPsiJavaFile.getName().length() - 5);
        return selectedClassName + "Builder";
    }

    protected String generate(PsiJavaFile currentPsiJavaFile, String templateName) {
        String selectedClassName = currentPsiJavaFile.getName().substring(0, currentPsiJavaFile.getName().length() - 5);
        PsiField[] allFields = currentPsiJavaFile.getClasses()[0].getAllFields();
        List<PsiField> oneDimArrayFields = new ArrayList<>(allFields.length);
        List<PsiField> multiDimArrayFields = new ArrayList<>(allFields.length);
        List<PsiField> dateFields = new ArrayList<>(allFields.length);
        List<PsiField> setFields = new ArrayList<>(allFields.length);
        List<PsiField> listFields = new ArrayList<>(allFields.length);
        List<PsiField> mapFields = new ArrayList<>(allFields.length);
        List<PsiField> otherFileds = new ArrayList<>(allFields.length);
        List<PsiField> primitiveFields = new ArrayList<>(allFields.length);
        List<PsiField> booleanFields = new ArrayList<>(allFields.length);
        List<PsiField> fieldsWithSetter = new ArrayList<>(allFields.length);


        for (PsiField field : allFields) {
            PsiType type = field.getType();
            if(!hasSetter(field)){
                continue;
            }
            fieldsWithSetter.add(field);
            if (type instanceof com.intellij.psi.impl.source.PsiClassReferenceType) {
                PsiClassReferenceType referenceType = (PsiClassReferenceType) type;
                String className = referenceType.getCanonicalText();
                if (className.equals("java.util.Date") || className.equals("java.sql.Date")) {
                    dateFields.add(field);
                } else if (className.startsWith("java.util.List")) {
                    listFields.add(field);
                } else if (className.startsWith("java.util.Set")) {
                    setFields.add(field);
                } else if (className.startsWith("java.util.Map")) {
                    mapFields.add(field);
                }else {
                    otherFileds.add(field);
                }
                System.out.println(className);
                System.out.println(referenceType.getCanonicalText());
            } else if (type instanceof com.intellij.psi.PsiArrayType) {
                PsiArrayType array = (PsiArrayType) type;
                System.out.println("dim:" + array.getArrayDimensions());
                System.out.println("text:" + array.getCanonicalText());
                if (array.getArrayDimensions() == 1) {
                    oneDimArrayFields.add(field);
                } else {
                    multiDimArrayFields.add(field);
                }
            } else if (type.getCanonicalText().equals("int") || type.getCanonicalText().equals("long")) {
                primitiveFields.add(field);
            }  else if (type.getCanonicalText().equals("boolean") ) {
                booleanFields.add(field);
            } else {
                otherFileds.add(field);
                System.out.println(field.getName());
                System.out.println(type.getCanonicalText());
                System.out.println(type.getClass().getName());
            }
            System.out.println("------------------------");

        }
        HashMap params = new HashMap();
        params.put("Util", new com.ns.plugin.util.Util());

        params.put("BuilderName", getBuilderName(currentPsiJavaFile));
        params.put("TargetName", selectedClassName);
        params.put("allfields", allFields);
        params.put("fieldsWithSetter", fieldsWithSetter);

        params.put("oneDimArrayFields", oneDimArrayFields);
        params.put("multiDimArrayFields", multiDimArrayFields);
        params.put("dateFields", dateFields);
        params.put("setFields", setFields);
        params.put("listFields", listFields);
        params.put("mapFields", mapFields);
        params.put("otherFileds", otherFileds);
        params.put("primitiveFields", primitiveFields);
        params.put("booleanFields", booleanFields);

        StringWriter out = new StringWriter();

        Velocity.evaluate(new VelocityContext(params), out, "", new InputStreamReader(this.getClass().getResourceAsStream(templateName)));
        return out.toString();
    }

    protected  boolean hasSetter(PsiField field){
        PsiClass containingClass = field.getContainingClass();
        PsiMethod[] allMethods = containingClass.getAllMethods();
        for (int i = 0; i < allMethods.length; i++) {
            PsiMethod method = allMethods[i];
            if(method.getName().equals(Util.setterName(field.getName()))){
                return true;
            }
        }
        return false;

    }
}
