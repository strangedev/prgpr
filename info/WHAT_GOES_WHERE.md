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

6. Project info

7. Toolchain

8. External Files

9. Mock classes

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

3 Configuration
---------------

Supporting source code goes into support/

Sometimes, supporting source code is needed, for example in the
case of log4j's config file. This directory should NEVER
include code with actual functionality.

4 Resources
-----------

Resources go into res/

Resources like image files, configuration files, etc. should
reside in their own directory.

5 Code Examples
---------------

Code example go into examples/

If there's a good code example you just did/found online,
put it in here for other people to see.

6 Project info
--------------

Project info goes into info/

Things like todo lists, Project info for other people involved in
the project or notes should be placed here.

7 Toolchain
-----------

Tools go into tools/

This directory is for all the little scripts and tools needed for
development, testing, packaging, etc. Which cannot be included with
the IDE.


8 External Files
----------------

Files that don't fit anywhere else go into external_files/

The subdirectory personal_stash is also excluded from git.
Put your "private pics" here.

9 Mock classes
--------------

See MOCK.md



