package com.prgpr.framework.database;

/**
 * Created by strange on 11/20/16.
 *
 * A class representing a database Element's property as a tuple of property name and value.
 *
 * @author Noah Hummel
 */
public class PropertyValuePair<V> {

    public final Property property;
    public final V value;

    /**
     * Constructor. Members are final.
     *
     * @param property The name of the property
     * @param value The value of the property
     */
    public PropertyValuePair(Property property, V value){
        this.property = property;
        this.value = value;
    }

}
