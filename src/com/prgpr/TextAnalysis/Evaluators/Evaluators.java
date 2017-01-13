package com.prgpr.TextAnalysis.Evaluators;

import com.prgpr.TextAnalysis.Data.FormattedWord;

/**
 * Created by strange on 1/13/17.
 */
public class Evaluators {

    public static float isNumeric(FormattedWord w) {
        boolean ret = w.content.matches("^[0-9]+$");
        return ret? 1 : 0;
    }

}
