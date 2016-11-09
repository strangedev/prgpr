#!/usr/bin/env bash

RAW_DATA=`realpath $1`
XML_FILE=`realpath $2`
LOG_FILE=`realpath $3`

echo "Pages in data file : "  $(cat ${RAW_DATA} | egrep "¤\s[0-9]+\s[0-9]+\s.*" | wc -l)
echo "Pages found: " $(cat ${XML_FILE} | egrep "<page\s" | wc -l)
echo "Categories found: " $(cat ${XML_FILE} | egrep "<category\s" | wc -l)
echo "Total number of lines: " $(cat ${XML_FILE} | wc -l)
echo "Log number of lines: " $(cat ${LOG_FILE} | wc -l)