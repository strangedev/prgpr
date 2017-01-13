package com.prgpr.TextAnalysis.Evaluators;

import com.prgpr.TextAnalysis.Data.FormattedWord;

import java.util.function.Function;

/**
 * Created by strange on 1/12/17.
 */
@FunctionalInterface
public interface TraitEvaluator extends Function<FormattedWord, Float> {
    default float eval(FormattedWord w) {
        return this.apply(w);
    }
}
