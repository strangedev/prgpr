#!/bin/bash
SVN_USER=$1
COMMIT_MSG=$2
INFILE=`realpath $3`
ROOT_DIR=`realpath ${4:-external_files/personal_stash/svn_checkout}`
DRY_RUN=0 # set to 1 for testing the script.

GIT_DIR="git"
GIT_REPO="ssh://git@github.com/strangedev/prgpr.git"

SVN_DIR="svn"
SVN_REPO="https://subversion.hucompute.org/prgpr2016/9C/"

PROJECT_NAME_REAL="CapnWikicrunch"
PROJECT_NAME_ALIAS="WikiXtractor"
JAVA_FILE="${PROJECT_NAME_ALIAS}.jar"

if ! [ -d "$ROOT_DIR" ]; then
    mkdir -p ${ROOT_DIR}
fi

cd ${ROOT_DIR}

if [ -d "$GIT_DIR" ]; then
    (cd ${GIT_DIR} && git pull)
else
    git clone ${GIT_REPO} ${GIT_DIR}
fi

if ! [ -d "$SVN_DIR" ]; then
    mkdir ${SVN_DIR}
    svn checkout --username ${SVN_USER} ${SVN_REPO} ${SVN_DIR}
fi

find ${SVN_DIR} -maxdepth 1 ! -path ${SVN_DIR} ! -path "${SVN_DIR}/.svn" -exec rm -rf {} \;
rm -f ${GIT_DIR}/build/libs/*

cd ${GIT_DIR}
sed -i "s/${PROJECT_NAME_REAL}/${PROJECT_NAME_ALIAS}/g" build.gradle
sed -i "s/${PROJECT_NAME_REAL}/${PROJECT_NAME_ALIAS}/g" settings.gradle
./gradlew jar
./gradlew javadoc
git reset --hard HEAD

cd ${ROOT_DIR}

cp ${GIT_DIR}/README.txt ${SVN_DIR}/README.txt
cp ${GIT_DIR}/settings.gradle ${SVN_DIR}/settings.gradle
cp ${GIT_DIR}/build.gradle ${SVN_DIR}/build.gradle
cp ${GIT_DIR}/gradlew ${SVN_DIR}/gradlew
cp ${GIT_DIR}/gradlew.bat ${SVN_DIR}/gradlew.bat
cp -R ${GIT_DIR}/gradle ${SVN_DIR}/gradle
cp ${GIT_DIR}/CapnWikicrunch.sh ${SVN_DIR}/${PROJECT_NAME_ALIAS}.sh
cp -R ${GIT_DIR}/src ${SVN_DIR}/src
cp -R ${GIT_DIR}/config ${SVN_DIR}/config
cp -R ${GIT_DIR}/build/docs/javadoc ${SVN_DIR}/javadoc
cp ${GIT_DIR}/build/libs/${JAVA_FILE} ${SVN_DIR}/

cd ${SVN_DIR}
mkdir example_output
sed -i "s/${PROJECT_NAME_REAL}/${PROJECT_NAME_ALIAS}/g" ${PROJECT_NAME_ALIAS}.sh
echo "Running ${PROJECT_NAME_REAL} Jar"
./${PROJECT_NAME_ALIAS}.sh ${INFILE} ./example_output/out.xml true >> /dev/null
tail -2 events.log
mv events.log ./example_output/events.log

zip example_output.zip ./example_output/*
rm -rf ./example_output

if [ "$DRY_RUN" == 1 ]; then
    echo "Dry run with commit message '${COMMIT_MSG}'"
    exit
fi

svn add --force ./*
svn commit -m ${COMMIT_MSG}
svn update