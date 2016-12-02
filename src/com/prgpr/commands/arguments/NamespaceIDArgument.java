package com.prgpr.commands.arguments;

import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.framework.command.CommandArgument;

/**
 * Created by kito on 02.12.16.
 */
public class NamespaceIDArgument extends CommandArgument {
    @Override
    public String getName() {
        return "namespaceID";
    }

    @Override
    public void test(String arg) throws InvalidArgument {}
}
