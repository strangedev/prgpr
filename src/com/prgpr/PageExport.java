package com.prgpr;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Set;

public class PageExport {

    /**
     * @param pages
     * @param path
     */
    public static void exportToXml(Set<Page> pages, String path){
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();

            Element root = doc.createElement("pages");
            doc.appendChild(root);

            pages.parallelStream().forEach((page) -> addPage(doc, root, page));

            // output DOM XML to console
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            //StreamResult console = new StreamResult(System.out);
            StreamResult output = new StreamResult(new File(path));
            transformer.transform(source, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param doc
     * @param parent
     * @param p
     */
    private static void addPage(Document doc, Element parent, Page p){
        Element page = doc.createElement("page");

        page.setAttribute("namespaceID", Integer.toString(p.getNamespaceID()));
        page.setAttribute("pageId", Long.toString(p.getId()));
        page.setAttribute("title", p.getTitle());

        Element categories = doc.createElement("categories");
        p.getCategories().forEach((name) -> {
            Element category = doc.createElement("category");
            category.setAttribute("name", name);
            categories.appendChild(category);
        });
        page.appendChild(categories);

        parent.appendChild(page);
    }
}
