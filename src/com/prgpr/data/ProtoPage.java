package com.prgpr.data;

/**
 * Created by lisa on 10/26/16.
 * @author Elizaveta Kovalevskaya
 *
 * This ProtoPage Class contains parts of the Wikidatas to collect them before the actual page is finished.
 * This way we can allways know which page belongs to which article.
 */
public class ProtoPage {

    private Page page;
    private StringBuilder htmlData;

    public ProtoPage(Page instance, StringBuilder htmlData) {
        this.page = instance;
        this.htmlData = htmlData;
    }

    public ProtoPage(Page instance) {
        this.page = instance;
        this.htmlData = new StringBuilder();
    }

    public Page getPage() {
        return page;
    }

    public StringBuilder getHtmlData() {
        return htmlData;
    }

    public void setHtmlData(StringBuilder htmlData) {
        this.htmlData = null;
        this.htmlData = htmlData;
    }

    /**
     * Compares this to an object to see if they are equal
     *
     * @param o the other object to compare to
     * @return bool
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProtoPage protoPage = (ProtoPage) o;

        return page.equals(protoPage.page);
    }

    /**
     * Gets the hashCode ot the page
     *
     * @return hashCode
     */
    @Override
    public int hashCode() {
        return page.hashCode();
    }

}
