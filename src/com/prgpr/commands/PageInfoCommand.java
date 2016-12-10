package com.prgpr.commands;

import com.prgpr.PageFinder;
import com.prgpr.commands.arguments.DatabaseDirectoryArgument;
import com.prgpr.commands.arguments.NamespaceIDArgument;
import com.prgpr.commands.arguments.PageTitleArgument;
import com.prgpr.data.Page;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.AsciiTable;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.EmbeddedDatabaseFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by kito on 02.12.16.
 *
 * Implements the pageinfo command from Milestone 2.
 * From OLAT:
 *
 * "Informationen über angegebene Seite auf der Konsole ausgeben:
 *      namespace, title, pageID
 *      Liste der direkten Kategorien
 *      Liste der direkten und indirekten Kategorien (im Kategorien-Graph)
 *      Liste der Artikel ausgeben auf welche die Seite verweist
 *      Liste der Artikel ausgeben welche auf den betreffenden Artikel verweisen"
 *
 * @author Kyle Rinfreschi, Elizaveta Kovalevskaya
 */
public class PageInfoCommand extends Command {

    private static final Logger log = LogManager.getFormatterLogger(PageInfoCommand.class);

    protected static final CommandArgument[] arguments = new CommandArgument[]{
            new DatabaseDirectoryArgument(),
            new NamespaceIDArgument(),
            new PageTitleArgument()
    };

    @Override
    public String getName() {
        return "pageinfo";
    }

    @Override
    public CommandArgument[] getArguments() {
        return arguments;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void handleArguments(String[] args) throws InvalidNumberOfArguments, InvalidArgument {
        if(args.length < 3){
            throw new InvalidNumberOfArguments();
        }

        arguments[0].set(args[0]);
        arguments[1].set(args[1]);
        arguments[2].set(args[2]);
    }

    @Override
    public void run() {
        EmbeddedDatabase graphDb = EmbeddedDatabaseFactory.newEmbeddedDatabase(arguments[0].get());
        PageFinder.setDatabase(graphDb);
        Page page = PageFinder.findByNamespaceAndTitle(Integer.valueOf(arguments[1].get()), arguments[2].get());

        if (page == null) {
            log.error("The requested page \"" + arguments[2].get() + "\" was not found. Make sure it was imported first!");
            return;
        }

        AsciiTable t = new AsciiTable();
        t.setColumns("Current Page", "NamespaceID", "ArticleID");
        t.addRow(page.getTitle(), page.getNamespaceID(), page.getArticleID());
        t.print();

        t.setColumns("Direct Categories");
        page.getCategories().stream()
                .map(Page::getTitle)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .forEach(t::addRow);
        t.print();

        t.setColumns("Related Categories");
        page.getAllRelatedCategories().stream()
                .map(Page::getTitle)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .forEach(t::addRow);
        t.print();

        t.setColumns("Linked Articles");
        page.getLinkedArticles().stream()
                .map(Page::getTitle)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .forEach(t::addRow);
        t.print();

        t.setColumns("Linking Articles");
        page.getLinkingArticles().stream()
                .map(Page::getTitle)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .forEach(t::addRow);
        t.print();

    }
}
