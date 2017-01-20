package com.prgpr.export;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by strange on 1/20/17.
 * @author Noah Hummel
 */
public class SimpleXmlDocument {

    private Document document;
    private Element root;

    public SimpleXmlDocument(Document document, Element root) {
        this.document = document;
        this.root = root;
    }

    public Document getDocument() {
        return document;
    }

    public Element createElement(String name) {
        return document.createElement(name);
    }

    public void append(Element element) {
        root.appendChild(element);
    }

}
