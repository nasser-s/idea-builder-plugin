package com.ns.plugin.util;

import com.intellij.ide.IdeEventQueue;
import com.intellij.idea.IdeaLogger;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ex.ProjectManagerEx;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.psi.*;
import com.intellij.ui.awt.RelativePoint;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.intellij.openapi.wm.WindowManager.getInstance;

/**
 * User: Nasser Sadeqi
 * Date: 4/20/14
 * Time: 4:06 PM
 */
public class NSIdeaPluginUtil {

    /**
     *
     * @param currentProject
     * @return Current editable document if any, otherwise returns null
     */
    public static Document getCurrentDocument(Project currentProject) {
        Editor editor = FileEditorManager.getInstance(currentProject).getSelectedTextEditor();
        if (editor == null) {
            return null;
        }

        final Document document = editor.getDocument();
        if (document == null) {
            return null;
        }
        return document;
    }

    /**
     *
     * @param actionEvent
     * @return current selected PsiFile if exist, otherwise returns null
     */
    public static PsiFile getCurrentPsiFile(AnActionEvent actionEvent) {
        return DataKeys.PSI_FILE.getData(actionEvent.getDataContext());
    }

    /**
     *
     * @param actionEvent
     * @return current selected PsiJavaFile if exist, otherwise returns null
     */
    public static PsiJavaFile getCurrentPsiJavaFile(AnActionEvent actionEvent) {
        PsiFile psiFile = DataKeys.PSI_FILE.getData(actionEvent.getDataContext());
        if(psiFile==null || ! (psiFile instanceof PsiJavaFile)) return null;
        else
            return (PsiJavaFile)psiFile;
    }

    /**
     *
     * @param actionEvent
     * @return
     */
    public static VirtualFile getCurrentFile(AnActionEvent actionEvent) {
        return DataKeys.VIRTUAL_FILE.getData(actionEvent.getDataContext());
    }

    /**
     *
     * @param actionEvent
     * @return
     */
    public static Project getCurrentProject(AnActionEvent actionEvent) {
     /*   if(actionEvent!=null){
            return DataKeys.PROJECT.getData(actionEvent.getDataContext());
        }else{*/
        return ProjectManagerEx.getInstanceEx().getOpenProjects()[0];

    }
    public static Project getCurrentProject() {
        return getCurrentProject(null);
    }
    /**
     * Shows messages ballon
     * @param msg
     * @param actionEvent
     */
    public static void showMessageBalon(final String msg, AnActionEvent actionEvent) {
        try {
            IdeaLogger.getInstance("NCG").info(msg);
            //StatusBar statusBar = WindowManager.getInstance().getStatusBar(actionEvent != null ? getCurrentProject(actionEvent) : ProjectManagerEx.getInstance().getOpenProjects()[0]);
            IdeEventQueue.getInstance().doWhenReady(new Runnable() {
                @Override
                public void run() {
                    //JBPopupFactory.getInstance().createMessage(msg).showInFocusCenter();
                    StatusBar statusBar = getInstance()
                            .getStatusBar(getCurrentProject());
                    JBPopupFactory.getInstance()
                            .createHtmlTextBalloonBuilder(msg, MessageType.INFO, null)
                            .setFadeoutTime(9000)
                            .createBalloon()
                            .show(RelativePoint.getCenterOf(statusBar.getComponent()),
                                    Balloon.Position.atRight);

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void showMessageBalon(String msg) {
        showMessageBalon(msg,null);
    }


   public static void showError(final String msg, final AnActionEvent actionEvent) {
       IdeaLogger.getInstance("NCG").error(msg);
       IdeEventQueue.getInstance().doWhenReady(new Runnable() {
           @Override
           public void run() {
               StatusBar statusBar = getInstance()
                       .getStatusBar(getCurrentProject(actionEvent));
               JBPopupFactory.getInstance()
                       .createHtmlTextBalloonBuilder(msg, MessageType.ERROR, null)
                       .setFadeoutTime(9000)
                       .createBalloon()
                       .show(RelativePoint.getCenterOf(statusBar.getComponent()),
                               Balloon.Position.atRight);
           }
       });

    }
    public static void showError(String msg) {
        showError(msg,null);
    }
    /**
     * Run write operations on opened documents
     * @param writeTask
     */
    public static void runWriteOperation(Runnable writeTask){
        ApplicationManager.getApplication().runWriteAction(writeTask);
    }

    public static boolean confirm(String title, String msg) {
        return true;
    }

    public static String formatCode(String contentStr) {
        return contentStr;

    }

    public static void writeTextFile(String formatCode, File fileToBeWritten) throws IOException {
        FileWriter output = new FileWriter(fileToBeWritten);
        IOUtils.write(formatCode, output);
        output.close();
    }

    public static void writeToFile(File file, byte[] content) throws IOException {
        FileOutputStream output = new FileOutputStream(file);
        IOUtils.write(content, output);
        output.close();

    }

    /**
     *
     * @param psiJavaFile
     * @return
     */
    public static List<PsiMethod> getMethods(PsiJavaFile psiJavaFile){
        List<PsiMethod> methods  = new ArrayList<PsiMethod>();
        PsiMethod[] psiMethods = psiJavaFile.getClasses()[0].getMethods();
        for (int i = 0; i < psiMethods.length; i++) {
            PsiMethod psiMethod = psiMethods[i];
            methods.add(psiMethod);
        }
        return methods;
    }

  /*  *//**
     *
     * @param psiMethod
     * @return Method annotations
     *//*
    public static List<PsiAnnotation> getAnnotatons(PsiMethod psiMethod){
        List<PsiAnnotation> annots = new ArrayList<PsiAnnotation>();
        PsiModifierList modifierList = psiMethod.getModifierList();
        PsiElement[] children1 = modifierList.getChildren();
        for (int j = 0; j < children1.length; j++) {
            PsiElement psiElement = children1[j];
            if(psiElement instanceof  PsiAnnotation){
                PsiAnnotation annot = (PsiAnnotation) psiElement;
                annots.add(annot);
            }
            //System.out.println(psiElement.getClass().getName()+" = "+psiElement.toString());
        }
        return annots;
    }*/

    public static List<PsiAnnotation> getAnnotatons(PsiModifierListOwner psiMethod){
        List<PsiAnnotation> annots = new ArrayList<PsiAnnotation>();
        PsiModifierList modifierList = psiMethod.getModifierList();
        PsiElement[] children1 = modifierList.getChildren();
        for (int j = 0; j < children1.length; j++) {
            PsiElement psiElement = children1[j];
            if(psiElement instanceof  PsiAnnotation){
                PsiAnnotation annot = (PsiAnnotation) psiElement;
                annots.add(annot);
            }
            //System.out.println(psiElement.getClass().getName()+" = "+psiElement.toString());
        }
        return annots;
    }

    /**
     *
     * @param psiMethod
     * @param annotaName
     * @return
     */
    public static PsiAnnotation getAnnotatoion(PsiMethod psiMethod,String annotaName){
        List<PsiAnnotation> annotatons = getAnnotatons(psiMethod);
        for (Iterator<PsiAnnotation> iterator = annotatons.iterator(); iterator.hasNext(); ) {
            PsiAnnotation next = iterator.next();
            if(next.getQualifiedName().equals(annotaName)){
                return next;
            }
        }
        return null;
    }

    /**
     *
     *
     * @param annotaName
     * @return
     */
    public static List<PsiMethod> getAnnotatedMethods(PsiClass psiClass,String annotaName){
        List<PsiMethod> methods  = new ArrayList<PsiMethod>();
        //PsiClass psiClass = psiJavaFile.getClasses()[0];
        PsiMethod[] psiMethods = psiClass.getMethods();
        for (int i = 0; i < psiMethods.length; i++) {
            PsiMethod psiMethod = psiMethods[i];
            List<PsiAnnotation> annotatons = getAnnotatons(psiMethod);
            for (Iterator<PsiAnnotation> iterator = annotatons.iterator(); iterator.hasNext(); ) {
                PsiAnnotation annot = iterator.next();
                if(annot.getQualifiedName().equals(annotaName)) {
                    methods.add(psiMethod);
                }
            }
        }
        return methods;
    }

    /**
     * Puts text into clipboard
     * @param text
     */
    public static void putInClipBoard(String text){
        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection,null);
    }

}
