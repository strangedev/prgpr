package com.prgpr.TextAnalysis.Data;

import com.prgpr.TextAnalysis.Convolution.ApplicationOrder;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by strange on 1/11/17.
 */
public class Document {

    public List<WordTraitVector> words;
    public ApplicationOrder applicationOrder;
    public float usefulness;

    public static Document fromFormattedWords(List<FormattedWord> words, ApplicationOrder ao) {
        Document doc = new Document();
        doc.usefulness = Float.NEGATIVE_INFINITY;
        doc.applicationOrder = ao;

        int vecSize = ao.evaluators.length;

        doc.words = words.stream()
                .map(w -> new WordTraitVector(w, vecSize, evaluateTraits(ao, w)))
                .collect(Collectors.toCollection(LinkedList::new));

        return doc;
    }

    public static Document fromWordTraitVectors(List<WordTraitVector> words, ApplicationOrder ao) {
        Document doc = new Document();
        doc.usefulness = Float.NEGATIVE_INFINITY;
        doc.applicationOrder = ao;
        doc.words = words;

        return doc;
    }

    private static float[] evaluateTraits(ApplicationOrder ao, FormattedWord w) {
        float[] traits = new float[ao.evaluators.length];

        for (int i = 0; i < ao.evaluators.length; i++) {
            traits[i] = ao.evaluators[i].eval(w);
        }
        return traits;
    }

}
