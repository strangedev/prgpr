package com.prgpr.commands.arguments;

import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.framework.command.CommandArgument;

/**
 * Created by kito on 02.12.16.
 *
 * A command Argument specifying a title of a wiki page.
 *
 * @author Kyle Rinfreschi
 */
public class ArticleTitleArgument extends CommandArgument {
    @Override
    public String getName() {
        return "Artikel-Title";
    }

    @Override
    public String getDescription() {
        return "The article title.";
    }

    @Override
    public void test(String arg) throws InvalidArgument {

    }
}
