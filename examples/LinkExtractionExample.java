import static com.prgpr.LinkExtraction.extractCategories;

/**
 * Created by lisa on 10/24/16.
 */
public class LinkExtractionExample {

    public static void main(String[] args) {
        String test;
        test = "<div id=\"catlinks\" class=\"catlinks\" data-mw=\"interface\"><div id=\"mw-normal-catlinks\" class=\"mw-normal-catlinks\"><a href=\"/wiki/Wikipedia:Kategorien\" title=\"Wikipedia:Kategorien\">Kategorien</a>: <ul><li><a href=\"/wiki/Kategorie:Pr%C3%A4sident_(Sambia)\" title=\"Kategorie:Präsident (Sambia)\">Präsident (Sambia)</a></li><li><a href=\"/wiki/Kategorie:Sambier\" title=\"Kategorie:Sambier\">Sambier</a></li><li><a href=\"/wiki/Kategorie:Geboren_1943\" title=\"Kategorie:Geboren 1943\">Geboren 1943</a></li><li><a href=\"/wiki/Kategorie:Gestorben_2011\" title=\"Kategorie:Gestorben 2011\">Gestorben 2011</a></li><li><a href=\"/wiki/Kategorie:Mann\" title=\"Kategorie:Mann\">Mann</a></li></ul></div></div>";
        extractCategories(test);
    }
}
