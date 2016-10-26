package com.prgpr;

/**
 * Created by strange on 10/26/16.
 */
public class ProtoPage {

    private Page instance;
    private String htmlData;

    public ProtoPage(Page instance, String htmlData) {
        this.instance = instance;
        this.htmlData = htmlData;
    }

    public ProtoPage(Page instance) {
        this.instance = instance;
        this.htmlData = "";
    }

    public Page getInstance() {
        return instance;
    }

    public String getHtmlData() {
        return htmlData;
    }

    public void setInstance(Page instance) {
        this.instance = instance;
    }

    public void setHtmlData(String htmlData) {
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
