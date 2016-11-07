package com.prgpr;

import com.prgpr.data.Page;
import com.prgpr.framework.Consumer;
import com.prgpr.framework.Producer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
 * An Export Class for creating an output file in xml format
 * and appending data to it in batches.
 */
public class PageExport implements Consumer<Page> {

    private static final Logger log = LogManager.getFormatterLogger(PageExport.class);

    private Set<Page> pages;
    private static final int batchSize = 100;
    private FileOutputStream outputFile;
    private String path;

    /**
     * Constructor.
     *
     * @param path The path to the output file.
     */
    PageExport(String path){
        this.path = path;
        this.pages = new LinkedHashSet<>();
    }

    /**
     * Creates a new output file.
     * If an output file already exists, it will be overwritten.
     */
    private void createOutputFile(){
        try {
            File f = new File(this.path);

            if (f.exists() && !f.delete()) {
                throw new Exception("Could not delete file!");
            }

            this.outputFile = new FileOutputStream(f, true);

        } catch (Exception e) {
            log.error(e.getMessage());

        }
    }

    /**
     * Appends a set of Pages to the output file as xml.
     *
     * @param pages Set of pages to append.
     */
    private void exportToXml(Set<Page> pages){
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
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");  // enable indentation in output file

            DOMSource source = new DOMSource(doc);
            StreamResult output = new StreamResult(this.outputFile);
            transformer.transform(source, output);

        } catch (Exception e) {
            log.error("Couldn't append page to document: " + e.getMessage());

        } finally {
            this.pages = new LinkedHashSet<>();  // clear current batch

        }
    }

    /**
     * Appends an xml-Element of a single Page object to
     * an xml document.
     *
     * @param doc The document to append to.
     * @param parent Parent element within the document to append to.
     * @param p The Page to append.
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
     * Aggregates Page object until batchSize is reached,
     * then appends the aggregated pages to the output file.
     *
     * @param consumable A Page from PageFactory.
     */
    @Override
    public void consume(Page consumable) {
        this.pages.add(consumable);

        if(this.pages.size() % batchSize == 0) {
            this.exportToXml(this.pages);
        }
    }

    /**
     * Finishes the export when the Producer has no more output
     * and writes the remaining pages into the output file.
     *
     * @param producer The producer this was subscribed to.
     */
    @Override
    public void onUnsubscribed(Producer<Page> producer) {
        this.exportToXml(this.pages);
    }
}
