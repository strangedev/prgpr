#!/usr/bin/env bash
JAVA_FILE=${JAVA_FILE:-"CapnWikicrunch.jar"}
VM_PARAMS="-Xmx500m"
java ${VM_PARAMS} -jar ./${JAVA_FILE} $@