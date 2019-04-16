package com.ns.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.ns.plugin.util.NSIdeaPluginUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sadeghi on 4/10/19.
 *
 * @author Sadeghi
 */
public class GenerateStandaloneBuilder extends BaseBuilder {
    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiJavaFile currentPsiJavaFile = NSIdeaPluginUtil.getCurrentPsiJavaFile(e);

        if(currentPsiJavaFile==null){
            return;
        }
        final Project project = e.getProject();
        generate(currentPsiJavaFile, project);
    }

    public String generate(PsiJavaFile currentPsiJavaFile, Project project) {
        String name = getBuilderName(currentPsiJavaFile);
        String templateName = "/simple-builder.vm";
        final String finalGenerated = NSIdeaPluginUtil.formatCode(generate(currentPsiJavaFile,templateName));
        new WriteCommandAction.Simple(project, currentPsiJavaFile.getParent().getContainingFile()) {
            public void run() {
                PsiFile fileFromText = PsiFileFactory.getInstance(project).createFileFromText(name + ".java", finalGenerated).getContainingFile();
                WriteCommandAction.runWriteCommandAction(project, () -> {
                    PsiFile existing = currentPsiJavaFile.getParent().findFile(name + ".java");
                    if (existing != null) {
                        if (existing instanceof PsiJavaFile) {
                            PsiJavaFile ex = ((PsiJavaFile) existing);
                            PsiDocumentManager.getInstance(project).getDocument(ex).setText(finalGenerated);
                        }
                    } else {
                        currentPsiJavaFile.getParent().add(fileFromText);
                    }
                });
            }
        }.run();
        return name;
    }


}
