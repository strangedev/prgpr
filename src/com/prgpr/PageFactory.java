package com.prgpr;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.concurrent.ConcurrentHashMap;

// import com.prgpr.Page;
import com.prgpr.mock.Page;

/**
 * Created by strange on 10/21/16.
 */
public class PageFactory {

    private ConcurrentHashMap<Long, Set<String>> categoriesForArticles;

    public Set<Page> extractPages(String infilePath){

        Set<Page> setToReturn = new LinkedHashSet<>();



        setToReturn.add(new Page());

        return setToReturn;
    }

}
