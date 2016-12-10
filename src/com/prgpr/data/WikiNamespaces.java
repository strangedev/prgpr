package com.prgpr.data;

import com.prgpr.framework.database.Label;

/**
 *
 * A Class to transfer the namespaceID to the appropriate type of label for a Wikipage
 *
 * @author Noah Hummel
 */
public class WikiNamespaces {

    public enum PageLabel implements Label {
        Article,
        ArticleTalk,
        User,
        UserTalk,
        Wikipedia,
        WikipediaTalk,
        File,
        FileTalk,
        MediaWiki,
        MediaWikiTalk,
        Template,
        TemplateTalk,
        Help,
        HelpTalk,
        Category,
        CategoryTalk,
        Portal,
        PortalTalk,
        Book,
        BookTalk,
        Draft,
        DraftTalk,
        EducationProgram,
        EducationProgramTalk,
        TimedText,
        TimedTextTalk,
        Module,
        ModuleTalk,
        Gadget,
        GadgetTalk,
        GadgetDefinition,
        GadgetDefinitionTalk,
        Unknown
    }

    /**
     * A function to transfer from namespaceID to the label
     *
     * @param namespaceID of the Page
     * @return the label
     */
    public static Label fromID(int namespaceID) {
        switch (namespaceID){
            case 0: return PageLabel.Article;
            case 1: return PageLabel.ArticleTalk;
            case 2: return PageLabel.User;
            case 3: return PageLabel.UserTalk;
            case 4: return PageLabel.Wikipedia;
            case 5: return PageLabel.WikipediaTalk;
            case 6: return PageLabel.File;
            case 7: return PageLabel.FileTalk;
            case 8: return PageLabel.MediaWiki;
            case 9: return PageLabel.MediaWikiTalk;
            case 10: return PageLabel.Template;
            case 11: return PageLabel.TemplateTalk;
            case 12: return PageLabel.Help;
            case 13: return PageLabel.HelpTalk;
            case 14: return PageLabel.Category;
            case 15: return PageLabel.CategoryTalk;
            case 100: return PageLabel.Portal;
            case 101: return PageLabel.PortalTalk;
            case 108: return PageLabel.Book;
            case 109: return PageLabel.BookTalk;
            case 118: return PageLabel.Draft;
            case 119: return PageLabel.DraftTalk;
            case 446: return PageLabel.EducationProgram;
            case 447: return PageLabel.EducationProgramTalk;
            case 710: return PageLabel.TimedText;
            case 711: return PageLabel.TimedTextTalk;
            case 828: return PageLabel.Module;
            case 829: return PageLabel.ModuleTalk;
            case 2300: return PageLabel.Gadget;
            case 2301: return PageLabel.GadgetTalk;
            case 2302: return PageLabel.GadgetDefinition;
            case 2303: return PageLabel.GadgetDefinitionTalk;
            default: return PageLabel.Unknown;
        }
    }

    /**
     * A function to transfer from label to the namespaceID
     *
     * @param label of the Page
     * @return the namespaceID
     */
    public static int fromPageLabel(PageLabel label) {
        switch (label.name()){
            case "Article": return 0;
            case "ArticleTalk": return 1;
            case "User": return 2;
            case "UserTalk": return 3;
            case "Wikipedia": return 4;
            case "WikipediaTalk": return 5;
            case "File": return 6;
            case "FileTalk": return 7;
            case "MediaWiki": return 8;
            case "MediaWikiTalk": return 9;
            case "Template": return 10;
            case "TemplateTalk": return 11;
            case "Help": return 12;
            case "HelpTalk": return 13;
            case "Category": return 14;
            case "CategoryTalk": return 15;
            case "Portal": return 100;
            case "PortalTalk": return 101;
            case "Book": return 108;
            case "BookTalk": return 109;
            case "Draft": return 118;
            case "DraftTalk": return 119;
            case "EducationProgram": return 446;
            case "EducationProgramTalk": return 447;
            case "TimedText": return 710;
            case "TimedTextTalk": return 711;
            case "Module": return 828;
            case "ModuleTalk": return 829;
            case "Gadget": return 2300;
            case "GadgetTalk": return 2301;
            case "GadgetDefinition": return 2302;
            case "GadgetDefinitionTalk": return 2303;
            default: throw new IndexOutOfBoundsException();
        }
    }
}
