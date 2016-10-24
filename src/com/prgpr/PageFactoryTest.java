package com.prgpr;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.prgpr.PageFactory;

import java.util.Set;

/**
 * Created by strange on 10/23/16.
 */
public class PageFactoryTest {

    @Test
    public void extractPages() throws Exception {

        // Set<Page> pagesContained = PageFactory.extractPages("res/infile/shortened.txt");
        Set<Page> pagesContained = PageFactory.extractPages("res/infile/wikipedia_de_prgpr_subset.txt");
        System.out.println(pagesContained.size());
        assertTrue(pagesContained.size() == 50560);

        for (int i = 0; i < 10; i++) {

            System.out.println("Run no.: " + i);
            // Set<Page> pagesContained2 = PageFactory.extractPages("res/infile/shortened.txt");
            Set<Page> pagesContained2 = PageFactory.extractPages("res/infile/wikipedia_de_prgpr_subset.txt");
            assertTrue(pagesContained.size() == pagesContained2.size());

        }

    }

}