package com.prgpr;

import com.prgpr.data.Page;
import com.prgpr.framework.Consumer;
import com.prgpr.framework.Producer;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Kyle Rinfreschi
 *
 * An Export Class for creating xml-syntax and writing it into the export file
 */
public class PageExport implements Consumer<Page> {

    private Set<Page> pages;
    private static final int batchSize = 100;
    private FileOutputStream outputFile;
    private String path;

    PageExport(String path){
        this.path = path;
        this.pages = new LinkedHashSet<>();
    }

    /**
     * creates a new file if the old one is deleted
     */
    private void createOutputFile(){
        try {
            File f = new File(this.path);

            if (f.exists() && !f.delete()) {
                throw new Exception("Could not delete file!");
            }

            this.outputFile = new FileOutputStream(f, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * builds Documents with xml-Pages and outputs them in the console
     *
     * @param pages Set of pages which were created by the PageFactory
     */
    public void exportToXml(Set<Page> pages){
        if(this.outputFile == null){
            this.createOutputFile();
        }

        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();

            Element root = doc.createElement("pages");
            doc.appendChild(root);

            pages.forEach((page) -> this.addPage(doc, root, page));

            // output DOM XML to console
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            //StreamResult console = new StreamResult(System.out);
            StreamResult output = new StreamResult(this.outputFile);
            transformer.transform(source, output);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.pages = new LinkedHashSet<>();
        }
    }

    /**
     * creates xml-Elements of Pages
     *
     * @param doc Document with xml-syntax with Pages
     * @param parent parent of the Page
     * @param p a Page
     */
    private void addPage(Document doc, Element parent, Page p){
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

    /**
     * writes into the output file as soon as the batchSize is reached
     *
     * @param consumable Page from PageFactory
     */
    @Override
    public void consume(Page consumable) {
        this.pages.add(consumable);

        if(this.pages.size() % batchSize == 0) {
            this.exportToXml(this.pages);
        }
    }

    /**
     * finishes the method by unsubscribing and writes the last pages into the output file
     *
     * @param producer The producer this was subscribed to.
     */
    @Override
    public void onUnsubscribed(Producer<Page> producer) {
        this.exportToXml(this.pages);
    }
}
