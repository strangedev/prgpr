package com.prgpr.data;


import java.util.stream.Stream;

/**
 * Created by lissie on 1/5/17.
 */
public abstract class EntityBase {

    public abstract String getTitle();
    /**
     * @return hashcode for uniqueness
     */
    public abstract int hashCode();

    /**
     * @param o Object to compare to
     * @return boolean of the comparison
     */
    public abstract boolean equals(Object o);

    public abstract Stream<String> insertEntityLinks();

}
