/*
 *  Copyright (c) 2017-2019, bruce.ge.
 *    This program is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU General Public License
 *    as published by the Free Software Foundation; version 2 of
 *    the License.
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *    GNU General Public License for more details.
 *    You should have received a copy of the GNU General Public License
 *    along with this program;
 */

package com.mengzz.fluentformatter;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.util.IncorrectOperationException;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * The type Fluent builder action.
 * 生成builder的所有链式调用方法
 *
 * @author bruce ge 2020/9/23
 */
public class FluentBuilderAction extends PsiElementBaseIntentionAction {
    private static final String FLUENT_BUILD = "Fluent build";
    private static final String BUILDER = "builder";

    @NotNull
    @Override
    public String getText() {
        return FLUENT_BUILD;
    }

    @Override
    public @NotNull @Nls(capitalization = Nls.Capitalization.Sentence) String getFamilyName() {
        return FLUENT_BUILD;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
        PsiElement builderElement = getBuilderElement(element);
        if (builderElement == null) {
            return;
        }
        PsiType psiType = ((PsiMethodCallExpressionImpl) builderElement).getType();
        if (psiType == null) {
            return;
        }
        Optional.ofNullable(PsiTypesUtil.getPsiClass(psiType))
                .ifPresent(psiClass -> {
                    PsiFile containingFile = builderElement.getContainingFile();
                    Document document = PsiDocumentManager.getInstance(project).getDocument(containingFile);
                    if (document == null) {
                        return;
                    }
                    StringBuilder builderText = getBuilderText(psiType, psiClass);
                    WriteCommandAction.runWriteCommandAction(project, () -> {
                        TextRange textRange = builderElement.getTextRange();
                        int endOffset = textRange.getEndOffset();
                        TextRange changeRange = TextRange.create(endOffset, endOffset + builderText.length());
                        document.insertString(endOffset, builderText);
                        PsiDocumentManager.getInstance(project).commitDocument(document);
                        new ReformatCodeProcessor(project, containingFile, changeRange, false)
                                .runWithoutProgress();
                    });
                });
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        return isPrevSiblingContainBuilder(element) || isCursorInBuilderName(element);
    }

    private PsiElement getBuilderElement(@NotNull PsiElement element) {
        PsiElement builderElement;
        if (isPrevSiblingContainBuilder(element)) {
            builderElement = getPrevFirstChild(element).orElse(null);
        } else {
            builderElement = getParentOfParent(element).orElse(null);
        }
        return builderElement;
    }

    @NotNull
    private StringBuilder getBuilderText(PsiType psiType, PsiClass psiClass) {
        StringBuilder builder = new StringBuilder("\n");
        for (PsiMethod psiMethod : psiClass.getMethods()) {
            PsiType returnType = psiMethod.getReturnType();
            if (psiType.equals(returnType) || isConvertibleFrom(psiType, returnType)) {
                String methodName = psiMethod.getName();
                builder.append(".").append(methodName).append("()\n");
            }
        }
        builder.append(".build()");
        return builder;
    }

    private boolean isConvertibleFrom(PsiType psiType, PsiType returnType) {
        return returnType != null && psiType.isConvertibleFrom(returnType);
    }

    private Boolean isCursorInBuilderName(@NotNull PsiElement element) {
        return classContainsBuilder(getParentOfParent(element));
    }

    private boolean isPrevSiblingContainBuilder(@NotNull PsiElement element) {
        return classContainsBuilder(getPrevFirstChild(element));
    }

    private Optional<PsiElement> getParentOfParent(@NotNull PsiElement element) {
        return Optional.ofNullable(element.getParent())
                .map(PsiElement::getParent);
    }

    private Optional<PsiElement> getPrevFirstChild(@NotNull PsiElement element) {
        return Optional.ofNullable(element.getPrevSibling())
                .map(PsiElement::getFirstChild);
    }

    private boolean classContainsBuilder(Optional<PsiElement> element) {
        return element.map(this::getPsiType)
                .map(PsiTypesUtil::getPsiClass)
                .map(PsiNamedElement::getName)
                .map(this::containsBuilder)
                .orElse(false);
    }

    @Nullable
    private PsiType getPsiType(PsiElement data) {
        PsiMethodCallExpressionImpl expression = data instanceof PsiMethodCallExpressionImpl ?
                ((PsiMethodCallExpressionImpl) data) : null;
        return expression == null ? null : expression.getType();
    }

    private boolean containsBuilder(String text) {
        return StringUtils.containsIgnoreCase(text, BUILDER);
    }

}
