#!/bin/bash

cd $1
rm -rf ./*
git clone "https://github.com/strangedev/prgpr.git"
cd ./prgpr
git checkout Production
cd ../
mv ./prgpr/* ./
rm -rf ./prgpr
svn add ./*
svn commit -m $2
svn update
