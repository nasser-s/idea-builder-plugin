package com.ns.plugin.builder;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.ns.action.GenerateStandaloneBuilder;
import com.ns.plugin.util.NSIdeaPluginUtil;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Sadeghi on 4/16/19.
 *
 * @author Sadeghi
 */
public class TestBuild extends LightCodeInsightFixtureTestCase {

    @Test
    public void testSimpleGenerate() throws IOException {

        PsiClass psiClass = myFixture.addClass(IOUtils.toString(getClass().getResourceAsStream("/sample-class1.txt")));
        System.out.println(psiClass);
        GenerateStandaloneBuilder generateStandaloneBuilder = new GenerateStandaloneBuilder();
        String packageName = "com.ns.plugin.builder.";
        PsiClass aClass = myFixture.getJavaFacade().findClass(packageName+"SampleBean");
        String generateName = generateStandaloneBuilder.generate((PsiJavaFile) psiClass.getContainingFile(), myFixture.getJavaFacade().getProject());
        PsiClass generatedClass = myFixture.findClass(packageName+generateName);
        Assert.assertNotNull(generatedClass);
        System.out.println("Done.");

    }
}
