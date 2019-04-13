package com.ns.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;
import com.ns.plugin.util.NSIdeaPluginUtil;

/**
 * Created by Sadeghi on 4/10/19.
 *
 * @author Sadeghi
 */
public class GenerateNestedBuilder extends BaseBuilder {
    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiJavaFile currentPsiJavaFile = NSIdeaPluginUtil.getCurrentPsiJavaFile(e);
        PsiImportList importList = currentPsiJavaFile.getImportList();
        PsiImportStatement[] importStatements = importList.getImportStatements();
        String name = getBuilderName(currentPsiJavaFile);
        String templateName = "/simple-builder.vm";
        final String finalGenerated = NSIdeaPluginUtil.formatCode(generate(currentPsiJavaFile,templateName));

        new WriteCommandAction.Simple(e.getProject(), currentPsiJavaFile.getParent().getContainingFile()) {
            public void run() {
                WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
                    String text = currentPsiJavaFile.getContainingFile().getText();
                    int classEnds = text.lastIndexOf("}");
                });
            }
        }.run();
    }


}
