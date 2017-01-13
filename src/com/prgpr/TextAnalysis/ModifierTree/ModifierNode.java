package com.prgpr.TextAnalysis.ModifierTree;

import com.prgpr.TextAnalysis.Data.FormattedWord;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by strange on 1/9/17.
 */
public class ModifierNode {

    public Set<FormattedWord.TextModifier> modifiers;
    public List<ModifierNode> children;
    public ModifierNode parent;
    public List<FormattedWord> formattedWords;

    public ModifierNode(ModifierNode parent) {
        this.modifiers = new LinkedHashSet<>();
        this.children = new LinkedList<>();
        this.parent = parent;
        this.formattedWords = new LinkedList<>();
    }

    public void applyModifiers() {
        for (FormattedWord formattedWord : this.formattedWords) {
            formattedWord.modifiers.addAll(this.modifiers);
        }
    }

}
