 _____             _         _    _ _ _    _                            _
/  __ \           ( )       | |  | (_) |  (_)                          | |
| /  \/ __ _ _ __ |/ _ __   | |  | |_| | ___  ___ _ __ _   _ _ __   ___| |__
| |    / _` | '_ \  | '_ \  | |/\| | | |/ / |/ __| '__| | | | '_ \ / __| '_ \
| \__/\ (_| | |_) | | | | | \  /\  / |   <| | (__| |  | |_| | | | | (__| | | |
 \____/\__,_| .__/  |_| |_|  \/  \/|_|_|\_\_|\___|_|   \__,_|_| |_|\___|_| |_|
            | |
            |_|

As the death of motivation came by, the coders kept fighting.

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
So the break came clearly. The coders were at an eventing Chaos Congress of
Communication. They learned, how code should not work and slept over it.
Afterwards, the journal asked for exactly the tasks they learned not to use.
Irony of life. Using not readable data to read information in between the
words is definitely a good way to have some fun.
They fought and fought and denied the traditional way.
Clearly in between the lines, the MateData was found. So asking other wise
and "fast" working DatabaseKings helped.
Now they were bound to a bureaucracy called "Internet"...
What adventures should it else bring?

-------------------------------------------------------------------------------
1. How to use
-------------------------------------------------------------------------------

    1.1 Invocation
    --------------

        1.1.1 Invocation by script
        --------------------------

            Execute the script 'WikiXtractor' from within bash or similar:

            "./WikiXtractor COMMAND ARGUMENT"
            "sh ./WikiXtractor COMMAND ARGUMENT"


        1.1.2 Manual invocation
        -----------------------

            Execute the following command from the command line of your choice:

            "java -jar WikiXtractor.jar COMMAND ARGUMENT"


    1.2 Description
    ---------------

         WikiXtractor extracts and imports contained articles from an infile
         of well-formed Wikidata into a database. It provides several analyses
         of the imported data:

         When the article ist about a person, a city or a monument, the
         following information will be extracted:

                Person:
                    * Name of the Wikiarticle
                    * First name
                    * Last name
                    * Birth name
                    * Date of birth
                    * Date of death
                    * Place of birth
                    * Place of death
                    * Linked Persons, Cities, Monuments

                City:
                    * Name of the city
                    * Country
                    * Population
                    * Earliest mention
                    * Linked Persons, Cities, Monuments

                Monument:
                    * Name of the monument
                    * Creation date
                    * Inauguration date
                    * Honored Persons
                    * Linked Persons, Cities, Monuments

         Example output:

+-----------+-----------+----------------+------------+---------------+----------------+---------------+----------------+
| Raw Name  | Last Name | First Name     | Birth Name | Date of Birth | Place of Birth | Date of Death | Place of Death |
+-----------+-----------+----------------+------------+---------------+----------------+---------------+----------------+
| Timm, Uwe | Timm      | Uwe Hans Heinz |            | 30. März 1940 | Hamburg        |               |                |
+-----------+-----------+----------------+------------+---------------+----------------+---------------+----------------+

        Afterwards, the rate of correct extracted attributes can be evaluated.
        Example output:

        +---------------------+---------+-------+
        | Attribute           | Correct | Ratio |
        +---------------------+---------+-------+
        | Name                | 2       | 0.1   |
        | Nearest city        | 0       | 0     |
        | Inauguration date   | 2       | 0.1   |
        | Commemorated person | 0       | 0     |
        +---------------------+---------+-------+


    1.3 Database-Directory
    ----------------------

        Directory where the database will store its files.
        You should have enough memory.


    1.4 Commands
    ------------

        The following commands exist:

            help          | Gives information about the program.

            createdb      | Creates a database given an html input file.
                          | required: <Database-Directory> <HTML-Input-File>

            executetasks  | Executes the tasks, taken from the taskfile.
                          | required: <Database-Directory> <Task-File>

            queryentity   | Returns all extracted information, if entity exists.
                          | required: <Database-Directory> <Article-Title>

            evaluation    | Evaluates the articles to test.
                          | required: <Database-Directory>

            version       | Outputs the current version.

        To list all of the commands and arguments, use the help command.


        * Also supports the command syntax given by the Milestone

          For Example:
             <Database-Directory> createdb <HTML-Input-File>

          This is done thanks to checking all arguments for the first instance of a command name.


    1.5 Command Arguments
    ---------------------

        1.5.1 Option ordering
        ---------------------

            ARGUMENT :=   <Database-Directory> <HTML-Input-File>
                        | <Database-Directory> <Task-File>
                        | <Database-Directory> <Article-Title>


        1.5.2 HTML-Input-File
        ---------------------

            The path to the input file.
            This file should be readable.
            The provided file should contain Wikidata.

        1.5.3 Task-File
        ---------------

            The path to the task file.
            The file should be readable.
            The provided file should contain possible tasks.
            The order should be executable.

        1.5.4 Article-Title
        -------------------

            The article title.
            Should exist in the database.

    1.6 Tasks
    ---------


        HTMLDumpImport <HTML-Input-File>
                | Imports the HTML-File into the database.
                | required: <HTML-Input-File>
                | depends on: independent

        CategoryLinkExtraction
                | Inserts the links of the categories.
                | depends on: HTMLDumpImport

        ArticleLinkExtraction
                | Extracts links between articles.
                | depends on: HTMLDumpImport

        EntityBaseExtraction
                | Extracts entities from articles.
                | depends on: CategoryLinkExtraction, ArticleLinkExtraction

        PersonExtraction
                | Extracts person information from wikidata.
                | depends on: EntityBaseExtraction

        CityExtraction
                | Extracts city information from wikidata.
                | depends on: EntityBaseExtraction

        MonumentExtraction
                | Extracts monument information from wikidata.
                | depends on: EntityBaseExtraction


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
        Arch Linux (latest)        | amd64 | jdk1.8
        Linux Mint 18.1 "Serena"   | amd64 | jdk1.8
        Linux Mint 18 "Sarah"      | amd64 | jdk1.8

    I THOUGHT THIS WAS JAVA! (ノಠ益ಠ)ノ彡┻━┻


-------------------------------------------------------------------------------
4. Authorship
-------------------------------------------------------------------------------

    (ヘ･_･)ヘ┳━┳

    The authorship of each class is documented with javadoc.
    Pair programming was used on many classes, but only the original author
    is documented.

    Additional credits go to:

        Noah Hummel:    For finding DatabaseKing providing the Matedata.

        Kyle Rinfreschi:    For greatly improving the Internet.

        Elizaveta Kovalevskaya: For generating a test set.


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

    5.3 The Style of Wikipedia
    --------------------------

        As somebody once tried to create data, which at the end should be
        readable by machine, did definitely never thought of making it readable
        by a human mind.
        So to be able to write a program extracting inconsistent formed data,
        the data should be at least unambiguously understandable for a person.

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