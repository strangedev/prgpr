TODO
====

WHAT GOES WHERE?
================

To keep this project structure organized, there are some practices
we will be using. These practices concern the placement of files
into the project structure.

Contents
--------

1. Source code

2. Libraries

3. Configuration

4. Resources

5. Code examples

6. General info

7. Toolchain

8. External Files

1 Source code
-------------

Source code goes into src/.

Different parts of the Program go into different modules.
For example: GUI -> com.prgpr.gui, heuristics -> com.prgpr.heuristics

2 Libraries
-----------

Libraries are located in lib/.

New libraries are NOT installed by hand, but imported as maven
dependencies. They should always include source and javadocs.

3 Supporting source code
------------------------

Supporting source code goes into support/

Sometimes, supporting source code is needed, for example in the
case of log4j's config file. This directory should NEVER
include code with actual functionality.

4 Resources
-----------

Resources go into res/

Resources like image files, configuration files, etc. should
reside in their own directory.

5 General info
--------------

General info goes into info/

Things like todo lists, general info for other people involved in
the project or notes should be placed here.



