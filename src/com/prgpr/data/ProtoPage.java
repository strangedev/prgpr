package com.prgpr.data;

/**
 * Created by lisa on 10/26/16.
 * @author Elizaveta Kovalevskaya
 *
 * ProtoPage Class.
 * Used to tie an incomplete Page object to it's original html data
 * for later processing.
 */
public class ProtoPage {

    private Page page;
    private StringBuilder htmlData;

    /**
     *
     * @param instance A page object.
     * @param htmlData A StringBuilder containing the pages html data.
     */
    public ProtoPage(Page instance, StringBuilder htmlData) {
        this.page = instance;
        this.htmlData = htmlData;
    }

    /**
     *
     * @param instance A page object.
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProtoPage protoPage = (ProtoPage) o;

        return page.equals(protoPage.page);
    }

    @Override
    public int hashCode() {
        return page.hashCode();
    }

}
