package com.prgpr.data;


/**
 * Created by lissie on 1/5/17.
 */
public abstract class EntityBase {

    /**
     * @return hashcode for uniqueness
     */
    public abstract int hashCode();

    /**
     * @param o Object to compare to
     * @return boolean of the comparison
     */
    public abstract boolean equals(Object o);

}
