package com.prgpr.commands.arguments;

import com.prgpr.data.WikiNamespaces;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.framework.command.CommandArgument;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by kito on 02.12.16.
 *
 * A command Argument specifying a namespaceID of a wiki article.
 *
 * @author Kyle Rinfreschi
 */
public class NamespaceIDArgument extends CommandArgument {
    @Override
    public String getName() {
        return "namespaceID";
    }

    @Override
    public String getDescription() {
        return "The namespace id of a page or article.";
    }

    @Override
    public void test(String arg) throws InvalidArgument {
        try {
            Integer.parseInt(arg);
        }catch (NumberFormatException e){
            throw new InvalidArgument("Invalid namespace id.");
        }
    }
}
