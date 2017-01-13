package com.prgpr.TextAnalysis.Data;

import com.prgpr.TextAnalysis.Data.FormattedWord;

/**
 * Created by strange on 1/11/17.
 */
public class WordTraitVector {

    public final FormattedWord formattedWord;
    public final int size;
    public final float[] properties;

    public WordTraitVector(FormattedWord formattedWord, int size, float[] properties) {
        this.formattedWord = formattedWord;
        this.size = size;
        this.properties = properties;
    }

}
