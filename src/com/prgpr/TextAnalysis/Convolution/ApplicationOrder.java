package com.prgpr.TextAnalysis.Convolution;

import com.prgpr.TextAnalysis.Evaluators.TraitEvaluator;

/**
 * Created by strange on 1/12/17.
 */
public class ApplicationOrder {
    public final TraitEvaluator[] evaluators;

    public ApplicationOrder(TraitEvaluator[] evaluators) {
        this.evaluators = evaluators;
    }
}
