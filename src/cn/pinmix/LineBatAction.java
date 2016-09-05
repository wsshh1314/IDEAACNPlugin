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

        String statement = builder.toString();

        //too short line ，DONOT Process
        if (statement.length() < 5) {
            return;
        }
        final String resultString = processStatementString(builder.toString());


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

    /**
     * @param statement
     * @return 新的替换语句
     */
    private String processStatementString(String statement) {
        //get Class Name
        //final static public private const

        String[] splits = statement.split("\\s+");

        String className = null;

        int index = 0;

        StringBuilder _afterBuiler = new StringBuilder();

        boolean isFound = false;
        for (int i = 0; i < splits.length; i++) {


            String itemStr = splits[i];

            if (isFound == false) {

                if (itemStr.length() > 0 && !SimpleUtils.isContaintKeyWorkds(itemStr)) {
                    className = itemStr;
                    isFound = true;
                }
                // _beforeBuiler.append(splits[i]);
            } else {
                _afterBuiler.append(splits[i]);
            }
        }

        //NOt FOUND
        if (isFound == false) {
            return null;
        }
        StringBuilder mBuilder = new StringBuilder();

        String[] vars = _afterBuiler.toString().split(",");

        if (vars.length == 0) {
            return null;
        }

        boolean isFirst = true;

        for (int i = 0; i < vars.length; i++) {

            //trim var name
            String _varName = vars[i].trim();

            if (_varName.length() > 0) {
                if (isFirst == true) {
                    isFirst = false;
                }
                if (_varName.endsWith(";")) {

                    if (isFirst) {
                        mBuilder.append(_varName.replaceFirst(";", className + ";"));

                    } else {
                        mBuilder.append("," + _varName.replaceFirst(";", className + ";"));
                    }
                } else {
                    if (isFirst) {
                        mBuilder.append(_varName + className);
                    } else {
                        mBuilder.append("," + _varName + className);
                    }
                }
            }


        }
        int classNameIndex = statement.indexOf(className);

        String before = statement.substring(0, classNameIndex + className.length());

        return before + " " + mBuilder.toString();
    }
}
