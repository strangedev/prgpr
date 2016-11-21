package com.prgpr.data;

import com.prgpr.framework.database.Label;

/**
 * Created by strange on 11/20/16.
 */
public class WikiNamespaces {

    public enum PageLabel implements Label {
        Page,
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

    public static Label fromID(int namespaceID) {
        switch (namespaceID){
            case -1: return PageLabel.Page;
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
}
