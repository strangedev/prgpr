 _____             _         _    _ _ _    _                            _
/  __ \           ( )       | |  | (_) |  (_)                          | |
| /  \/ __ _ _ __ |/ _ __   | |  | |_| | ___  ___ _ __ _   _ _ __   ___| |__
| |    / _` | '_ \  | '_ \  | |/\| | | |/ / |/ __| '__| | | | '_ \ / __| '_ \
| \__/\ (_| | |_) | | | | | \  /\  / |   <| | (__| |  | |_| | | | | (__| | | |
 \____/\__,_| .__/  |_| |_|  \/  \/|_|_|\_\_|\___|_|   \__,_|_| |_|\___|_| |_|
            | |
            |_|

The second adventure has started...
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------

After a journey full of expiring the review of the first mission completed,
the passionate coders headed to a whole new island without shipping the most
basic search functionality. Many dramatic experiences happened since then,
the coders could even imagine ...
The price discovering this mess was high, they even had to give up their name...

-------------------------------------------------------------------------------
1. How to use
-------------------------------------------------------------------------------

    1.1 Invocation
    --------------

        1.1.1 Invocation by script
        --------------------------

            Execute the script 'WikiXtractor' from within bash or similar:

            "./WikiXtractor COMMAND [ARGUMENTS]"
            "sh ./WikiXtractor COMMAND [ARGUMENTS]"


        1.1.2 Manual invocation
        -----------------------

            Execute the following command from the command line of your choice:

            "java -jar WikiXtractor.jar COMMAND [DB-Directory] [OTHER ARGUMENTS]"


    1.2 Description
    ---------------

         WikiXtractor extracts and imports contained articles from an infile
         of well-formed Wikidata into a database. It provides to analyse the
         relations between the articles.
         The following information is extracted:

                Article:
                    * Page ID
                    * Namespace ID
                    * Page title
                    * Article categories
                    * Article articles

        The following output is an example of the 'pageinfo' command when all necessary data (articles and categories)
        are imported.

        +--------------+-------------+-----------+
        | Current Page | NamespaceID | ArticleID |
        +--------------+-------------+-----------+
        | Paderborn    | 0           | 4474649   |
        +--------------+-------------+-----------+
        +----------------------------------------------------+
        | Direct Categories                                  |
        +----------------------------------------------------+
        | Deutsche Universitätsstadt                         |
        | ...                                                |
        | Regiopolregion                                     |
        +----------------------------------------------------+
        +-------------------------------------------------------+
        | Related Categories                                    |
        +-------------------------------------------------------+
        | !Hauptkategorie                                       |
        | Agglomeration                                         |
        | ...                                                   |
        | Öffentlichkeit                                        |
        +-------------------------------------------------------+
        +---------------------------------------------+
        | Linked Articles                             |
        +---------------------------------------------+
        | Aachen                                      |
        | ...                                         |
        | Würzburg                                    |
        +---------------------------------------------+
        +-------------------------------------------------------------+
        | Linking Articles                                            |
        +-------------------------------------------------------------+
        | Aachen                                                      |
        | ...                                                         |
        | Zülpich                                                     |
        +-------------------------------------------------------------+

        (Sadly, the article 'Pirates' was not available.)


    1.3 Commands
    -------------

            What to execute:

            help

            articlelinks  | Extracts links between articles.
                          | required: <DB-Directory>

            categorylinks | Inserts the links of the categories.
                          | required: <DB-Directory>

            importhtml    | Imports the HTML-File into the database.
                          | required: <DB-Directory> <HTML-Input-File>

            pageinfo      | Outputs gathered information about the requested page.
                          | required: <DB-Directory> <namespaceID> <page-title>

            reset         | Resets the database in the given argument and generates a new empty database.
                          | required: <DB-Directory>

            version       | Outputs the current version.


    1.4 Command Arguments
    -------------------

        1.4.1 Option ordering
        ---------------------

            [ARGUMENTS] := <DB-Directory>
                           | <DB-Directory> <HTML-Input-File>
                           | <DB-Directory> <namespaceID> <page-title>

        1.4.1 DB-Directory
        ------------------

            Directory where the database will store its files.
            You should have enough memory.

        1.4.2 HTML-Input-File
        ---------------------

            The path to the input file.
            This file should be readable.
            The provided file should contain wikidata.

        1.4.3 namespaceID
        -----------------

            The namespace id of a page.
            Should match with the namespaceID of the page-title.

        1.4.4 page-title
        ----------------

            The page title.
            NamespaceID of the page-title should match with the denoted namespaceID.


-------------------------------------------------------------------------------
2. Logging
-------------------------------------------------------------------------------

    The program will create a log of any occurring errors, warnings and
    - if set - all program progress.

    The logfile will be written to "events.log".
    The logfile will be written to the current working directory.


-------------------------------------------------------------------------------
3. Tested platforms
-------------------------------------------------------------------------------

    This program was tested on:
        Arch Linux (latest)     | amd64 | jdk1.8
        Debian 8.6 "Jessie"     | amd64 | jdk1.8
        Linux Mint 18 "Sarah"   | amd64 | jdk1.8

    I THOUGHT THIS WAS JAVA! (ノಠ益ಠ)ノ彡┻━┻


-------------------------------------------------------------------------------
4. Authorship
-------------------------------------------------------------------------------

    (ヘ･_･)ヘ┳━┳

    This program was developed in 5 main phases:


    4.1 First draft
    ---------------

        During the first phase, the required functionality was put in place.

        Kyle:   PageProducer, PageFactory, Command-Classes, Output formation
        Lisa:   LinkExtraction, adding Relationships
        Noah:   SearchProvider, PageFinder

        And a first working version was completed.


    4.2 Optimization & Refactoring
    ------------------------------

        The used structure proved the be not optimal (see 5.3 Required usage
        of Neo4j), an abstraction for the database was put in place.

        Kyle:   Idea and implementation

        Pair programming was then used to refactor all of the classes to use
        the new structure.


    4.3 Debugging
    -------------

        After testing was possible, following bugs were found:

        Not all links were found and inserted.
        Exceptions were not thrown.

        Kyle:   Testing, Transaction model
        Lisa:   Testing, LinkExtraction
        Noah:   Testing, Transaction model, LinkExtraction


    4.4 Finalizing
    --------------

        To complete the assignment, multiple code reviews and pair programming
        sessions were held, where comments and docstrings were put in place,
        configuration files were created and a main class was implemented.

        Kyle:   docstrings
        Lisa:   docstrings, Milestone2_README.txt
        Noah:   docstrings


    4.5 Packaging
    -------------

        An artifact was built.
        The final project was pruned of unnecessary data and checked in to
        subversion. By script.


-------------------------------------------------------------------------------
5. Known issues
-------------------------------------------------------------------------------

    5.1 Required usage of JSoup ಠ_ಠ
    -------------------------------

        HTML is messy. Very messy.
        JSoup makes parsing HTML very simple, at a cost. It attempts to build
        the entire document tree first, which is quite useful when doing
        complex operations on it - but entirely useless when it comes to
        extracting just one single div.

        It does the work a regex could do in 2 seconds in 5, just because
        it's dragging along all of this useless information.
        Quite frankly, milestone 1 would do better without it, but for some
        reason, it's obligatory to use it.


    5.2 Required usage of subversion ಠ╭╮ಠ
    -------------------------------------

        From http://harmful.cat-v.org/software/svn/ :

            “Sticking to subversion is like installing Windows 95 on your
            developers’ PCs. If they don’t know anything better
            they won’t complain, though it might not be a bright decision
            from a management viewpoint.”
                – Mike

            “[Svn is] the IE6 of version control”
                – James S.

        http://tinyurl.com/dontusesvn


    5.3 Required usage of Neo4j
    ---------------------------






-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
                                                    _  _
                                                   ' \/ '
   _  _                        <|
    \/              __'__     __'__      __'__
                   /    /    /    /     /    /
                  /\____\    \____\     \____\               _  _
                 / ___!___   ___!___    ___!___               \/
               // (      (  (      (   (      (
             / /   \______\  \______\   \______\
           /  /   ____!_____ ___!______ ____!_____
         /   /   /         //         //         /
      /    /   |         ||         ||         |
     /_____/     \         \\         \\         \
           \      \_________\\_________\\_________\
            \         |          |         |
             \________!__________!_________!________/
              \|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_/|
               \    _______________                /
^^^%%%^%^^^%^%%^\_"/_)/_)_/_)__)/_)/)/)_)_"_'_"_//)/)/)/)%%%^^^%^^%%%%^^%%%^^^!
^!!^^"!%%!^^^!^^^!!^^^%%%%%!!!!^^^%%^^^!!%%%%^^^!!!!!!%%%^^^^%^^%%%^^^!^%%%^^^!