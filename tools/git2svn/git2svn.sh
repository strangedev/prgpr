#!/bin/bash
SVN_USER=$1
COMMIT_MSG=$2
ROOT_DIR=`realpath ${3:-external_files/personal_stash/svn_checkout}`
DRY_RUN=1 # set to 1 for testing the script.

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
    (cd ${GIT_DIR} && git reset --hard HEAD && git pull)
else
    git clone ${GIT_REPO} ${GIT_DIR}
fi

if ! [ -d "$SVN_DIR" ]; then
    mkdir ${SVN_DIR}
    svn checkout --depth empty --username ${SVN_USER} ${SVN_REPO} ${SVN_DIR}
fi

rm -rf ${SVN_DIR}/*

rm -f ${GIT_DIR}/build/libs/*

cd ${GIT_DIR}
sed -i "s/${PROJECT_NAME_REAL}/${PROJECT_NAME_ALIAS}/g" build.gradle
sed -i "s/version '\(.*\)-\(.*\)\(\:\?-\(.*\)\)\?'/version '\1-\2-release'/g" build.gradle
sed -i "s/${PROJECT_NAME_REAL}/${PROJECT_NAME_ALIAS}/g" settings.gradle
./gradlew build
./gradlew javadoc

cd ${ROOT_DIR}

cp ${GIT_DIR}/README.txt ${SVN_DIR}/README.txt
cp ${GIT_DIR}/settings.gradle ${SVN_DIR}/settings.gradle
cp ${GIT_DIR}/build.gradle ${SVN_DIR}/build.gradle
cp ${GIT_DIR}/gradlew ${SVN_DIR}/gradlew
cp ${GIT_DIR}/gradlew.bat ${SVN_DIR}/gradlew.bat
cp -R ${GIT_DIR}/gradle ${SVN_DIR}/gradle
cp ${GIT_DIR}/CapnWikicrunch.sh ${SVN_DIR}/${PROJECT_NAME_ALIAS}
cp -R ${GIT_DIR}/src ${SVN_DIR}/src
cp -R ${GIT_DIR}/config ${SVN_DIR}/config
cp -R ${GIT_DIR}/build/docs/javadoc ${SVN_DIR}/javadoc
cp ${GIT_DIR}/build/libs/${JAVA_FILE} ${SVN_DIR}/

cd ${SVN_DIR}
sed -i "s/${PROJECT_NAME_REAL}/${PROJECT_NAME_ALIAS}/g" ${PROJECT_NAME_ALIAS}

if [ "$DRY_RUN" == 1 ]; then
    echo "Dry run with success message '${COMMIT_MSG}'"
    exit
fi

svn add --force ./*
svn commit -m ${COMMIT_MSG}
svn update