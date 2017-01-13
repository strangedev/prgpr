package com.prgpr.TextAnalysis.Convolution;

import com.prgpr.TextAnalysis.Data.Document;
import com.prgpr.TextAnalysis.Data.WordTraitVector;

import java.util.Iterator;
import java.util.List;

/**
 * Created by strange on 1/12/17.
 */
public class ConvolutionKernel implements Iterator<Document> {
    private final int breadth;
    public final int size;
    private final float[][] weights;
    private final Document doc;
    private final float[] ZERO_VEC;
    private int current;

    public ConvolutionKernel(int breadth, int size, float[][] weights, Document doc) {
        assert breadth > 0;
        assert weights.length == breadth;
        assert weights[0].length == size;
        this.breadth = breadth;
        this.size = size;
        this.weights = weights;
        this.current = 0;
        this.doc = doc;
        this.ZERO_VEC = new float[size];

        for (int i = 0; i < size; i++) {
            this.ZERO_VEC[i] = 0;
        }
    }

    @Override
    public boolean hasNext() {
        return current < this.doc.words.size();
    }

    @Override
    public Document next() {
        int offset = (this.breadth/2);
        float usefulness = 0;

        for (int j = 0; j < this.breadth; j++) {
            int at = this.current + j - offset;
            float[] traitVector = getTraitVector(doc, at);
            usefulness += applyVector(traitVector, this.weights[j], size);
        }
        this.current++;
        Document ret = Document.fromWordTraitVectors(getFrame(this.current), doc.applicationOrder);
        ret.usefulness = usefulness / breadth;
        return ret;
    }

    private static float applyVector(float[] v, float[] w, int size) {
        float acc = 0;
        for (int i = 0; i < size; i++) {
            acc += v[i] * w[i];
        }
        return acc / size;
    }

    private List<WordTraitVector> getFrame(int at) {
        at--;
        int offset = this.breadth / 2;
        int lower = at - offset > 0? at - offset : 0;
        int upper = at + offset < (this.doc.words.size() - 1)? at + offset : (this.doc.words.size() - 1);
        return this.doc.words.subList(lower, upper);
    }

    private float[] getTraitVector(Document doc, int at) {
        if (at > (doc.words.size() - 1)) return ZERO_VEC;
        if (at < 0) return ZERO_VEC;
        return doc.words.get(at).properties;
    }

}
