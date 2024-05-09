#!/bin/bash

even(){
        echo `expr $1 % 2`
}

if [ $# -ne 1 ]
then
        echo "usage: $0 dirName"
        exit -1
fi

if [ ! -d "$1/even" ]
then
        mkdir "$1/even"
fi

if [ ! -d "$1/odd" ]
then
        mkdir "$1/odd"
fi

for f in `ls $1`
do
        if [ -d $f ] || [ -x $f ]
        then
                continue
        fi

        chars=`cat $f | wc -c`
        res=`even $chars`

        if [ "$res" = "1" ]
        then
                mv $f "$1/even"
        else    mv $f "$1/odd"
        fi

done
