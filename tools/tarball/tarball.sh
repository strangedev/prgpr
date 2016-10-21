#!/bin/bash

projectName="PrgPr_Milestone1_"
authors="Noah Hummel 5966916 _ Da Lis0r _ Kyle Rinfreschi"
includes="PRGPR_Project.iml .idea/ lib/ src/"

authorNames="$(echo -e "${authors}" | tr -d '[[:space:]]')"
outfile=${projectName}${authorNames}

tar -vczf ./out/"${outfile}".tar.gz