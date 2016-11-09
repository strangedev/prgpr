#!/bin/bash

SVN_DIR=$1
COMMIT_MSG=$2
INFILE=$3

cd ${SVN_DIR}
rm -rf ./*
git clone "https://github.com/strangedev/prgpr.git"

cd prgpr
sh ./gradlew build
sh ./gradlew javadoc
cd ../

mv prgpr/README.txt ./README.txt
mv prgpr/settings.gradle ./settings.gradle
mv prgpr/build.gradle ./build.gradle
mv prgpr/gradlew ./gradlew
mv prgpr/gradlew.bat ./gradlew.bat
mv prgpr/CapnWikicrunch.sh ./CapnWikicrunch.sh
mv prgpr/src ./src
mv prgpr/build/docs ./docs
mv prgpr/build/libs/CapnWikicrunch* ./

mkdir example_output
sh ./CapnWikicrunch.sh ${INFILE} ./example_output/out.xml true
mv events.log ./example_output/events.log

zip example_output.zip ./example_output/*
rm -rf ./example_output

svn add ./*
svn commit -m ${COMMIT_MSG}
svn update

