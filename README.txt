 _____             _         _    _ _ _    _                            _
/  __ \           ( )       | |  | (_) |  (_)                          | |
| /  \/ __ _ _ __ |/ _ __   | |  | |_| | ___  ___ _ __ _   _ _ __   ___| |__
| |    / _` | '_ \  | '_ \  | |/\| | | |/ / |/ __| '__| | | | '_ \ / __| '_ \
| \__/\ (_| | |_) | | | | | \  /\  / |   <| | (__| |  | |_| | | | | (__| | | |
 \____/\__,_| .__/  |_| |_|  \/  \/|_|_|\_\_|\___|_|   \__,_|_| |_|\___|_| |_|
            | |
            |_|

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------

Listen closely, for this is the tale of a long forgotten programming language!
Once upon a time, three wise coders tried to wield the power of java, an
ancient and long forgotten tongue, which only few know of now.
Fate had chosen them to embark on an epic quest, to conquer the strange land
of WikiData, as described in the most mythical of all writings, the Milestone
of number one. So they saddled their trusty IDEs and rode off, unsure
of the things to come!
Many obstacles did they overcome! They slayed the beast of UTF-8 encoding
and crossed the forest of Malformed HTML, they tricked 500MB, an angry giant
who will crush any amount of RAM, and they forged the sword of XML, the
mightiest of all swords!

-------------------------------------------------------------------------------
1. How to use
-------------------------------------------------------------------------------

    1.1 Invocation
    --------------

        1.1.1 Invocation by script
        --------------------------

            Execute the script CapnWikicrunch.sh from within bash or similar:

            "./CapnWikicrunch.sh INFILE OUTFILE [OPTIONS]"


        1.1.2 Manual invocation
        -----------------------

            Execute the following command from the command line of your choice:

            "java -jar CapnWikicrunch.jar INFILE OUTFILE [OPTIONS]"


    1.2 Description
    ---------------

        CapnWikicrunch takes an infile of well-formed Wikidata and extracts the
        contained articles to an outfile in xml format.
        The following information is extracted:

            Article:
                * Page ID
                * Namespace ID
                * Page title
                * Article categories by title

        The output will be formatted as follows:

            <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <pages>
                    <page namespaceID="0" pageId="162349" title="Shivaji">
                        <categories>
                            <category name="Indischer Herrscher"/>
                            <category name="Person (Pune)"/>
                            <category name="Geboren 1630"/>
                            <category name="Gestorben 1680"/>
                            <category name="Mann"/>
                        </categories>
                    </page>
                    [...]
                </pages>

    1.3 Required Arguments
    ----------------------

        1.3.1 INFILE
        ------------

            The path to the input file.
            This file should be readable.
            The provided file should contain wikidata.


        1.3.2 OUTFILE
        -------------

            The path to the output file.
            The extracted wikidata will be written to this file.
            This file should be writable.
            If this file already exists, it will be overwritten.

    1.4 Options
    -----------

        Options are passed by position.
        Supported options are:


        1.4.1 Option ordering
        ---------------------

            [OPTIONS] := [logAll]


        1.4.2 Detailed description of options
        -------------------------------------

            1.4.2.1 log-all - Log all progress [optional]
            ---------------------------------------------

                Can either be true or false.
                Default: true

                If set to true, every processed article produces a log entry,
                this is useful for logging the programs progress. If set to
                false, only a final performance report will be generated.

                WARNING:
                    Setting this option to true will cause
                    a noticeable slowdown.


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

    This program was developed in 4 main phases:


    4.1 First draft
    ---------------

        During the first phase, the required classes were put in place.

        Kyle:   Page, PageExport
        Lisa:   LinkExtraction
        Noah:   PageFactory

        And a first working version was completed.


    4.2 Optimization & Refactoring
    ------------------------------

        The used structure proved the be not optimal, a consumer-producer
        design pattern was put in place by:

        Kyle:   Idea and later refactoring
        Noah:   First implementation

        Pair programming was then used to refactor all of the classes to use
        the new structure.


    4.3 Finalizing
    --------------

        To complete the assignment, multiple code reviews and pair programming
        sessions were held, where comments and docstrings were put in place,
        configuration files were created and a main class was implemented.

        Kyle:   Logging configuration, docstrings
        Lisa:   Logging configuration, Testing, Main class, docstrings
        Noah:   README.txt, Main class, docstrings


    4.4 Packaging
    -------------

        An artifact was built.
        The final project was pruned of unnecessary data and checked in to
        subversion.


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