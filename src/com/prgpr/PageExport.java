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
import java.util.Set;

public class PageExport {

    /*
     <?xml version="1.0" encoding="UTF-8"?>
     <pages>
     <page pageID="17724" namespaceID="0" title="Amiga 500">
     <categories>
     <category name="Amiga-Computer"/>
     <category name="Heimcomputer"/>
     </categories>
     </page>
     <page>
     <!-- ... -->
     </page>
     </pages>
     */

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

            for (Page p : pages) {
                addPage(doc, root, p);
            }

            //@TODO: write to file instead of System.out
            // output DOM XML to console
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult console = new StreamResult(System.out);
            transformer.transform(source, console);
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
        page.setAttribute("pageId", Long.toString(p.getId()));
        page.setAttribute("namespaceID", Integer.toString(p.getNamespaceID()));
        page.setAttribute("title", p.getTitle());

        for(String name : p.getCategories()){
            Element category = doc.createElement("category");
            category.setAttribute("name", name);
            page.appendChild(category);
        }

        parent.appendChild(page);
    }
}
