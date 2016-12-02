package com.prgpr.commands.arguments;

import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.framework.command.CommandArgument;

/**
 * Created by kito on 02.12.16.
 */
public class PageTitleArgument extends CommandArgument {
    @Override
    public String getName() {
        return "page-title";
    }

    @Override
    public void test(String arg) throws InvalidArgument {

    }
}
