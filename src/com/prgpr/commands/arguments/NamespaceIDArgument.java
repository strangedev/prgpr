package com.prgpr.commands.arguments;

import com.prgpr.data.WikiNamespaces;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.framework.command.CommandArgument;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by kito on 02.12.16.
 */
public class NamespaceIDArgument extends CommandArgument {
    @Override
    public String getName() {
        return "namespaceID";
    }

    @Override
    public String getDescription() {
        List<String> output = new LinkedList<>();
        output.add("The namespace id of a page or article");
        output.add("\tValues:");
        Arrays.stream(WikiNamespaces.PageLabel.values())
                .filter(label -> label != WikiNamespaces.PageLabel.Unknown)
                .map(label -> String.format("\t\t%s: %s", label.name(), WikiNamespaces.fromPageLabel(label)))
                .collect(Collectors.toCollection(() -> output));
        return output.stream().collect(Collectors.joining("\n"));
    }

    @Override
    public void test(String arg) throws InvalidArgument {

    }
}
