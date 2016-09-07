package cn.pinmix;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

/**
 * Created by icity on 16/9/5.
 */
public class LineBatAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        final PsiFile psiFile = (PsiFile) event.getData(PlatformDataKeys.PSI_FILE);

        //ver 0.1 only for java
        if (!psiFile.getName().endsWith(".java")) {
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
        final int offset_start = document.getLineStartOffset(line);
        final int offset_end = document.getLineEndOffset(line);

        StringBuilder builder = new StringBuilder();

        for (int i = offset_start; i <= offset_end; i++) {
            builder.append(text.charAt(i));
        }

        final String resultString = SimpleUtils.replaceLine(builder.toString());

        if(resultString.length() <5){
            return;
        }

        final Runnable writeRunner = new Runnable() {
            @Override
            public void run() {
                if (!resultString.isEmpty()) {
                    document.replaceString(offset_start, offset_end, resultString);
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
                }, "LineACNPAction", null);
            }
        });


    }
}
