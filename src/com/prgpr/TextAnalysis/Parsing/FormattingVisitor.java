package com.prgpr.TextAnalysis.Parsing;

import com.prgpr.TextAnalysis.Data.FormattedWord;
import com.prgpr.TextAnalysis.ModifierTree.ModifierNode;
import com.prgpr.TextAnalysis.ModifierTree.ModifierTree;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by strange on 1/11/17.
 */
public class FormattingVisitor implements NodeVisitor {

    public ModifierTree modifierTree;

    public FormattingVisitor() {
        this.modifierTree = new ModifierTree();
    }

    private FormattedWord.TextModifier getAssociatedModifier(String tagName) {
        switch (tagName) {
            case "b":
                return FormattedWord.TextModifier.bold;

            case "i":
                return FormattedWord.TextModifier.italic;

            case "em":
                return FormattedWord.TextModifier.emphasized;

            case "del":
                return FormattedWord.TextModifier.deleted;

            case "a":
                return FormattedWord.TextModifier.anchor;

            case "strong":
                return FormattedWord.TextModifier.strong;

            case "mark":
                return FormattedWord.TextModifier.marked;

            case "small":
                return FormattedWord.TextModifier.small;

            case "ins":
                return FormattedWord.TextModifier.underlined;

            case "sub":
                return FormattedWord.TextModifier.subscript;

            case "sup":
                return FormattedWord.TextModifier.superscript;

            default:
                if(tagName.startsWith("h") && tagName.length() > 1) {
                    if(StringUtil.isNumeric(tagName.substring(1))) {
                        return FormattedWord.TextModifier.headline;
                    }
                }
                return null;
        }
    }

    private List<FormattedWord> asWords(String text) {
        String[] terms = text.split("[\\s]+");

        // Remove leading special chars || whitespace
        // Remove trailing special chars || whitespace
        // Remove enclosed dots in numerical characters
        // Remove &nbsp;
        // Remove blank || empty strings
        return Arrays.stream(terms)
                .map(s -> s.replaceAll("^[\\^\\s\\t\\n\\r\\p{Z},\\|\"„“'.();:@%/+-]+", ""))
                .map(s -> s.replaceAll("[\\^\\s\\t\\n\\r\\p{Z},.\\|\"„“';:()@/+-]+$", ""))
                .map(s -> {
                    if (s.matches("^(\\d+\\.\\d+)+")) return s.replaceAll("\\.", "");
                    return s;
                })
                .map(s -> s.replaceAll("\u00a0", ""))
                .filter(s -> !StringUtil.isBlank(s))
                .map(FormattedWord::new)
                .collect(Collectors.toList());
    }

    @Override
    public void head(Node node, int depth) {

        if (node instanceof TextNode) {

            String tagName = ((Element)node.parentNode()).tag().getName();
            FormattedWord.TextModifier mod = getAssociatedModifier(tagName);
            List<FormattedWord> containedFormattedWords = asWords(((TextNode) node).text());

            ModifierNode modNode = new ModifierNode(this.modifierTree.current);
            modNode.formattedWords.addAll(containedFormattedWords);
            if(mod != null) modNode.modifiers.add(mod);

            this.modifierTree.addNode(modNode);
            this.modifierTree.descend();
        }

    }

    @Override
    public void tail(Node node, int depth) {

        if (node instanceof TextNode) {
            this.modifierTree.ascend();
        }

    }
}