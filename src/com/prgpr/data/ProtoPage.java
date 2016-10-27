package com.prgpr.data;

/**
 * Created by strange on 10/26/16.
 */
public class ProtoPage {

    private Page instance;
    private StringBuilder htmlData;

    public ProtoPage(Page instance, StringBuilder htmlData) {
        this.instance = instance;
        this.htmlData = htmlData;
    }

    public ProtoPage(Page instance) {
        this.instance = instance;
        this.htmlData = new StringBuilder();
    }

    public Page getInstance() {
        return instance;
    }

    public StringBuilder getHtmlData() {
        return htmlData;
    }

    public void setInstance(Page instance) {
        this.instance = instance;
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

        return htmlData.equals(protoPage.htmlData);

    }

    @Override
    public int hashCode() {
        return htmlData.hashCode();
    }

}
