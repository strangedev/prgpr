#!/bin/bash

COMMIT_MSG=$1
INFILE=`realpath $2`
ROOT_DIR=`realpath ${3:-external_files/personal_stash/svn_checkout}`

GIT_DIR="git"
GIT_REPO="ssh://git@github.com/strangedev/prgpr.git"

SVN_DIR="svn"

JAVA_FILE="CapnWikicrunch.jar"

if ! [ -d "$ROOT_DIR" ]; then
    mkdir -p ${ROOT_DIR}
fi

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
cp -R ${GIT_DIR}/config ${SVN_DIR}/config
cp -R ${GIT_DIR}/build/docs/javadoc ${SVN_DIR}/javadoc
cp ${GIT_DIR}/build/libs/${JAVA_FILE} ${SVN_DIR}/

cd ${SVN_DIR}
mkdir example_output
echo "Running CapnWikicrunch Jar"
./CapnWikicrunch.sh ${INFILE} ./example_output/out.xml true >> /dev/null
tail -2 events.log
mv events.log ./example_output/events.log

zip example_output.zip ./example_output/*
rm -rf ./example_output

svn add ./*
svn commit -m ${COMMIT_MSG}
svn update