package com.prgpr.TextAnalysis.ModifierTree;

import com.prgpr.TextAnalysis.Data.FormattedWord;

import java.util.*;

/**
 * Created by strange on 1/9/17.
 */
public class ModifierTree {

    private ModifierNode root;
    public ModifierNode current;

    public ModifierTree() {
        this.root = new ModifierNode(null);
        this.current = root;
    }

    public List<FormattedWord> fold() {
        List<FormattedWord> formattedWords = new LinkedList<>();
        Stack<ModifierNode> discoveredNodes = new Stack<>();
        discoveredNodes.push(this.root);

        while (!discoveredNodes.isEmpty()) {
            
            ModifierNode node = discoveredNodes.pop();

            for (int i = node.children.size() - 1; i >= 0; i--) {
                ModifierNode child = node.children.get(i);

                for (FormattedWord.TextModifier mod : node.modifiers) {
                    child.modifiers.add(mod);
                }

                discoveredNodes.push(node.children.get(i));
            }

            node.applyModifiers();
            formattedWords.addAll(node.formattedWords);
            
        }

        return formattedWords;
    }

    public void addNode(ModifierNode node) {
        this.current.children.add(node);
    }

    public boolean descend() {
        if(current.children.isEmpty()) return false;
        this.current = this.current.children.get(this.current.children.size() - 1);
        return true;
    }

    public void ascend() {
        this.current = this.current.parent;
    }

}
