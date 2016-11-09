#!/usr/bin/env bash
FILE_PATH=${1:-"."}
JAVA_FILE="${FILE_PATH}/CapnWikicrunch.jar"
VM_PARAMS="-Xmx500m"
java ${VM_PARAMS} -jar ./${JAVA_FILE} $@