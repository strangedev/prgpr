package com.prgpr.framework.database;

/**
 * Created by kito on 21.11.16.
 */
public interface Element {

    void addLabel(Label label);

    Object getProperty(Property property);

    <E> void setProperty(Property property, E val);

    <E> Element findNode(Label label, Property property, E val);

    void update(Callback<Element> callback);
}
