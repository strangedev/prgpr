package com.prgpr.TextAnalysis.Data;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by strange on 1/9/17.
 */
public class FormattedWord {

    public enum TextModifier {
        bold,  // <b> - Bold
        italic,  // <i> - Italic
        emphasized,  // <em> - Emphasized
        deleted,  // <del> - Deleted text
        anchor,  // <a> - Anchor (link)
        headline,  // <h*> - Headline
        strong,  // <strong> - Important
        marked,  // <mark> - Marked text
        small,  // <small> - Small text
        underlined,  // <ins> - Inserted text
        subscript,  // <sub> - Subscript text
        superscript  // <sup> - Superscript text
    }

    public String content;
    public Set<TextModifier> modifiers;

    public FormattedWord(String content, Set<TextModifier> modifiers) {
        this.content = content;
        this.modifiers = modifiers;
    }

    public FormattedWord(String content) {
        this.content = content;
        this.modifiers = new LinkedHashSet<>();
    }

    public void addModifier(TextModifier modifier) {
        this.modifiers.add(modifier);
    }

}
