package com.prgpr.TextAnalysis.Parsing;

import com.prgpr.TextAnalysis.Data.FormattedWord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;

import java.util.List;

/**
 * Created by strange on 1/9/17.
 */
public class HtmlParser {

    public List<FormattedWord> asWords(String source) {
        Document doc = Jsoup.parse(source);

        FormattingVisitor formatter = new FormattingVisitor();
        NodeTraversor traversor = new NodeTraversor(formatter);

        Elements matchingDivs = doc.select("body"); // WTF
        if (matchingDivs.size() > 0) traversor.traverse(matchingDivs.get(0));

        return formatter.modifierTree.fold();
    }

}
