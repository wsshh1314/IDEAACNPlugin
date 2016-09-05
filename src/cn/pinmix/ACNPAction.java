package cn.pinmix;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

/**
 * Created by icity on 16/9/2.
 */
public class ACNPAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent event) {

        final  PsiFile psiFile = (PsiFile)event.getData(PlatformDataKeys.PSI_FILE);

        //ver 0.1 only for java
        if(!psiFile.getName().endsWith(".java")){
            return;
        }

        final Project project = (Project) event.getData(PlatformDataKeys.PROJECT);
        final Editor editor = (Editor) event.getData(PlatformDataKeys.EDITOR);
        final Document document = editor.getDocument();

        String text = document.getText();

        final CaretModel caretModel = editor.getCaretModel();
        LogicalPosition position = caretModel.getLogicalPosition();

        //currentline
        int line = position.line;

        //get line text
        int offset_start = document.getLineStartOffset(line);
        int offset_end = document.getLineEndOffset(line);
        StringBuilder builder = new StringBuilder();

        for (int i = offset_start; i <= offset_end; i++) {
            builder.append(text.charAt(i));
        }
        //get Class Name
        //final static public private const
        String[] split = builder.toString().split("\\s+");

        String first = "";

        for (String str : split) {

            if (str.length() > 0 && !SimpleUtils.isContaintKeyWorkds(str)) {
                first = str;
                break;
            }
        }
        final String resultString = first;

        final Runnable writeRunner = new Runnable() {
            @Override
            public void run() {
                if (!resultString.isEmpty()) {

                    document.insertString(caretModel.getOffset(), resultString);
                    caretModel.moveToOffset(caretModel.getOffset() + resultString.length());
                }
            }
        };

        //write Action

        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                CommandProcessor.getInstance().executeCommand(project, new Runnable() {
                    @Override
                    public void run() {
                        ApplicationManager.getApplication().runWriteAction(writeRunner);
                    }
                }, "ACNPAction", null);
            }
        });


    }


}
