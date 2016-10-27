package com.prgpr.data;

/**
 * Created by strange on 10/26/16.
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
