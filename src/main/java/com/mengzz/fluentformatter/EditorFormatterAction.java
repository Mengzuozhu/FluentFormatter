package com.mengzz.fluentformatter;

import com.google.common.base.Splitter;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The type Editor formatter action.
 *
 * @author zuozhu.meng
 * @since 2020 /12/17
 */
public class EditorFormatterAction extends PsiElementBaseIntentionAction {
    private static final String FLUENT_FORMAT = "Fluent format";
    private static final int LIMIT = 2;
    private static final String DOT = ".";
    private static final String DOT_REGEX = "\\.";
    private static final String LINE = "\n";
    private static final Splitter LINE_SPLITTER = Splitter.on(LINE);
    private static final String IMPORT = "import";

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
        formatFluentStyle(project, editor);
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        if (editor == null) {
            return false;
        }
        SelectionModel selectionModel = editor.getSelectionModel();
        // more than one dot
        return selectionModel.hasSelection() && countMatches(selectionModel.getSelectedText(), DOT, LIMIT) >= LIMIT;
    }

    @NotNull
    @Override
    public String getText() {
        return FLUENT_FORMAT;
    }

    @Override
    public @NotNull @Nls(capitalization = Nls.Capitalization.Sentence) String getFamilyName() {
        return FLUENT_FORMAT;
    }

    private static int countMatches(String str, String sub, int limit) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(sub, index)) != -1) {
            count++;
            if (count >= limit) {
                return count;
            }
            index += sub.length();
        }
        return count;
    }

    private void formatFluentStyle(@NotNull Project project, Editor editor) {
        Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
        String selectedText = primaryCaret.getSelectedText();
        if (selectedText == null) {
            return;
        }
        String fluentStr = StreamSupport.stream(LINE_SPLITTER.split(selectedText).spliterator(), false)
                .map(this::joinFluent)
                .collect(Collectors.joining(LINE));
        // StringBuilder fluentStr = joinFluent(selectedText);
        int start = primaryCaret.getSelectionStart();
        int end = primaryCaret.getSelectionEnd();
        Document document = editor.getDocument();
        WriteCommandAction.runWriteCommandAction(project, () ->
                document.replaceString(start, end, fluentStr)
        );
        reformat(project, editor);
        primaryCaret.removeSelection();
    }

    private void reformat(@NotNull Project project, Editor editor) {
        PsiFile file = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
        if (file != null) {
            new ReformatCodeProcessor(file, editor.getSelectionModel()).runWithoutProgress();
        }
    }

    private String joinFluent(String selectedText) {
        if (StringUtils.isEmpty(selectedText) || selectedText.startsWith(IMPORT)) {
            return selectedText;
        }
        String[] nodes = selectedText.split(DOT_REGEX);
        int length = nodes.length;
        if (length < LIMIT) {
            return selectedText;
        }
        StringBuilder join = new StringBuilder();
        for (int i = 0; i < length; i++) {
            String node = nodes[i];
            join.append(node);
            if (i == 0) {
                join.append(DOT);
            } else if (i != length - 1) {
                join.append("\n").append(DOT);
            }
        }
        return join.toString();
    }
}
