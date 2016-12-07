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
import com.prgpr.framework.database.neo4j.Neo4jEmbeddedDatabase;
import com.prgpr.framework.database.EmbeddedDatabaseFactory;

/**
 * Created by kito on 02.12.16.
 */
public class PageInfoCommand extends Command {

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
        Neo4jEmbeddedDatabase graphDb = EmbeddedDatabaseFactory.newEmbeddedDatabase(arguments[0].get());
        PageFinder.setDatabase(graphDb);
        Page page = PageFinder.findByNamespaceAndTitle(Integer.valueOf(arguments[1].get()), arguments[2].get());
        assert page != null;

        AsciiTable t = new AsciiTable();
        t.setColumns("Current Page", "NamespaceID", "ArticleID");
        t.addRow(page.getTitle(), page.getNamespaceID(), page.getArticleID());
        t.print();

        t.setColumns("Direct Categories", "NamespaceID");
        page.getCategories().stream().map(cat -> new Object[]{cat.getTitle(), cat.getNamespaceID()}).forEach(t::addRow);
        t.print();

        t.setColumns("Related Categories", "NamespaceID");
        page.getAllRelatedCategories().stream().map(cat -> new Object[]{cat.getTitle(), cat.getNamespaceID()}).forEach(t::addRow);
        t.print();

        t.setColumns("Linked Articles", "NamespaceID");
        page.getLinkedArticles().stream().map(article -> new Object[]{article.getTitle(), article.getNamespaceID()}).forEach(t::addRow);
        t.print();

        t.setColumns("Linking Articles", "NamespaceID");
        page.getLinkingArticles().stream().map(article -> new Object[]{article.getTitle(), article.getNamespaceID()}).forEach(t::addRow);
        t.print();

    }
}
