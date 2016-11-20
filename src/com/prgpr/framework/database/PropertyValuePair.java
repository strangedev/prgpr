package com.prgpr.framework.database;

/**
 * Created by strange on 11/20/16.
 */
public class PropertyValuePair<V> {

    public final Property property;
    public final V value;

    public PropertyValuePair(Property property, V value){
        this.property = property;
        this.value = value;
    }

}
