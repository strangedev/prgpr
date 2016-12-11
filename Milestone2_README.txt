 _____             _         _    _ _ _    _                            _
/  __ \           ( )       | |  | (_) |  (_)                          | |
| /  \/ __ _ _ __ |/ _ __   | |  | |_| | ___  ___ _ __ _   _ _ __   ___| |__
| |    / _` | '_ \  | '_ \  | |/\| | | |/ / |/ __| '__| | | | '_ \ / __| '_ \
| \__/\ (_| | |_) | | | | | \  /\  / |   <| | (__| |  | |_| | | | | (__| | | |
 \____/\__,_| .__/  |_| |_|  \/  \/|_|_|\_\_|\___|_|   \__,_|_| |_|\___|_| |_|
            | |
            |_|

The second adventure has started...

                                                    ____
                                         v        _(    )
        _ ^ _                          v         (___(__)
       '_\V/ `
       ' oX`
          X                            v
          X             -HELP!
          X                                                 .
          X        \O/                                      |\
          X.a##a.   M                                       |_\
       .aa########a.>>                                    __|__
    .a################aa.                                 \   /
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------

After a journey full of being late to the code review of the first mission
was over, the passionate coders headed to a whole new island, said to be full
of treasure, nodes and relationships. But when they arrived, panic ensued.
The treasure was not and nobody had shipped even the most basic search
functionality. Lacking their most basic tools, the coders began rebuilding
everything they hoped they would find...
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
         of well-formed Wikidata into a database. It provides several commands
         to analyse the imported data:

         The following information can be extracted:

                Wiki Article:
                    * Page ID
                    * Namespace ID
                    * Page title
                    * Linked categories
                    * Related categories
                    * Linked articles
                    * Linking articles

        The following output is an example of the 'pageinfo' command when all
        extraction commands have been used before:

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

        The following commands exist:

            help          | Displays this information.

            articlelinks  | Extracts links between articles.
                          | required: <DB-Directory>

            categorylinks | Inserts the links of the categories.
                          | required: <DB-Directory>

            importhtml    | Imports the HTML-File into the database.
                          | required: <DB-Directory> <HTML-Input-File>

            pageinfo      | Outputs gathered information about the requested
                          | page.
                          | required: <DB-Directory> <namespaceID> <page-title>

            reset         | Resets the database in the given argument and
                          | generates a new empty database.
                          | required: <DB-Directory>

            version       | Outputs the current version.


        To list all of the commands and arguments, use the help command.


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
    all program progress. DEBUG logs are left in place, this way our beloved
    tutor can gain a better understanding of what is happening as the program
    is running.

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

    The authorship of each class is documented with javadoc.
    Pair programming was used on many classes, but only the original author
    is documented.

    Additional credits go to:

        Kyle Rinfreschi:    For greatly improving the git2svn script used for
                            pushing the release candidates to svn

        Elizaveta Kovalevskaya: For generating a test set which greatly sped up
                                the testing process, for testing the program
                                countless times and writing many of the
                                docstrings.

-------------------------------------------------------------------------------
5. Known issues
-------------------------------------------------------------------------------

    5.1 Required usage of subversion ಠ╭╮ಠ
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


    5.2 Required usage of Neo4j
    ---------------------------

        How does one tell a great framework apart from a bad one?

        Let's say you've got this great looking framework in front of you.
        Massive adoption by all kinds of different people, the professional
        press is praising it like the savior and it makes some really big
        promises about how it's gonna make your life so much easier and bring
        your company more revenue.

        "Great!" You think to yourself, "I finally found the thing I was looking
        for all along!". "Where can I get started?", you ask yourself. The
        browsing ensues. After some time you realize, that while trying to cater
        to the people who make decisions, the developers of this great and one
        of a kind framework simply forgot to tell anyone how to use it.

        You've got everything. Use cases. Success stories. Professional
        opinions. A jazzy looking all new query language. But no documentation.
        This should be a huge red flag right away.

        After some time, you've gotten your hands on a developers' manual.
        It's old and rather short, but you figure "Hey maybe it's just that
        easy to use!" and carry on. The structure is awful, the examples
        convoluted and not in any way descriptive and slowly, you're starting to
        loose your sanity.

        In a last attempt of good will, you find yourself a book on the topic.
        It's even older than that manual you found, but you figure "Hey, it
        can't be that bad if someone once paid money for it". Boy, are you
        wrong. You find out that half of the examples given in the book are no
        longer valid, because large parts of the backend have been cut out,
        flagged as deprecated and no replacement was put into place.

        Now it's dawning on you:
        They're not selling a framework. They're not selling to developers.
        They're trying to sell a bunch of nice sounding slogans to whoever is in
        charge of your company. They're trying to sell the idea that by using
        their framework, you will be able to do all these new things and gene-
        rate all this revenue by it.

        So how does one tell a great framework apart from a bad one?

        If it's selling you ordinary features as the new big thing,
        if it's selling you use cases and professional opinions instead of a
        clear documentation, if it's silently removing functionality to force
        you to obey their rules and pay their support, you've got a bad frame-
        work on your hands.

        Neo4J is such a framework.
        And no, I'm not buying it. Neo4J cannot do a thing another data-
        base cannot. But because it's developers have spent more time appealing
        to management than actually working on their API, working with it is
        a convoluted, badly documented mess and I recommend it to noone.

        ~ strange

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