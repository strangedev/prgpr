package com.prgpr.framework.command;

import com.prgpr.exceptions.InvalidArgument;

/**
 * Created by kito on 20.11.16.
 *
 * An abstract superclass for command Arguments.
 * Used by a Command object to specify it's required Arguments.
 * Arguments have a name, value and description and are responsible for checking whether
 * a given argument value is valid.
 *
 * @author Kyle Rinfreschi
 */
public abstract class CommandArgument {

    private String value;

    /**
     * @return The name of the argument.
     */
    public abstract String getName();

    /**
     * @return A human readable description of the argument. Used for displaying help messages.
     */
    public abstract String getDescription();

    /**
     * @return A human readable description of the argument including name and description. Used for displaying help messages.
     */
    public String getFullDescription(){
        String name = getName();
        String desc = getDescription();

        if(desc == null){
            return name;
        }

        return String.format("%s : %s", name, desc);
    }

    /**
     * Tests whether a given argument as string can be interpreted as this argument type.
     * Used by set() to determine whether a valid argument has been passed.
     *
     * @param arg The argument to be tested as string
     * @throws InvalidArgument If the passed argument can't be interpreted as this argument type.
     */
    public abstract void test(String arg) throws InvalidArgument;

    /**
     * Sets the value of the argument from a string.
     *
     * @param arg The new value for this argument object as string.
     * @throws InvalidArgument If test() fails on the given argument.
     */
    public void set(String arg) throws InvalidArgument {
        this.test(arg);
        this.value = arg;
    }

    /**
     * Getter.
     *
     * @return The value of the argument.
     */
    public String get() {
        return value;
    }

    @Override
    public int hashCode(){
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || getClass() ==  o.getClass();
    }

    @Override
    public String toString() {
        return String.format("<%s>", getName());
    }
}
