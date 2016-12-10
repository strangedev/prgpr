#!/usr/bin/env sh
JAVA_FILE="CapnWikicrunch.jar"
VM_PARAMS="-Xmx2g"
java ${VM_PARAMS} -jar ./${JAVA_FILE} $@