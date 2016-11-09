#!/bin/bash

ROOT_DIR=`realpath $1`
COMMIT_MSG=$2
INFILE=`realpath $3`

GIT_DIR="git"
GIT_REPO="ssh://git@github.com/strangedev/prgpr.git"

SVN_DIR="svn"

JAVA_FILE="CapnWikicrunch.jar"

cd ${ROOT_DIR}

if [ -d "$GIT_DIR" ]; then
    (cd ${GIT_DIR} && git pull)
else
    git clone ${GIT_REPO} ${GIT_DIR}
fi

cd ${GIT_DIR}
./gradlew jar
./gradlew javadoc

cd ${ROOT_DIR}

if ! [ -d "$SVN_DIR" ]; then
    mkdir ${SVN_DIR}
fi
rm -rf ${SVN_DIR}/*

cp ${GIT_DIR}/README.txt ${SVN_DIR}/README.txt
cp ${GIT_DIR}/settings.gradle ${SVN_DIR}/settings.gradle
cp ${GIT_DIR}/build.gradle ${SVN_DIR}/build.gradle
cp ${GIT_DIR}/gradlew ${SVN_DIR}/gradlew
cp ${GIT_DIR}/gradlew.bat ${SVN_DIR}/gradlew.bat
cp ${GIT_DIR}/CapnWikicrunch.sh ${SVN_DIR}/CapnWikicrunch.sh
cp -R ${GIT_DIR}/src ${SVN_DIR}/src
cp -R ${GIT_DIR}/build/docs ${SVN_DIR}/docs
cp ${GIT_DIR}/build/libs/${JAVA_FILE} ${SVN_DIR}/

cd ${SVN_DIR}

mkdir example_output
java -jar -Xmx500m ${JAVA_FILE} ${INFILE} ./example_output/out.xml true
mv events.log ./example_output/events.log

zip example_output.zip ./example_output/*
rm -rf ./example_output

#svn add ./*
#svn commit -m ${COMMIT_MSG}
#svn update