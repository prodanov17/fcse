#!/bin/bash

if [ $# -lt 1 ]; then
    echo "Usage: $0 <file>"
    exit 1
fi

home_demo='.'
dir_name="trash"

if [ ! -d $home_demo/$dir_name ]; then
    mkdir $home_demo/$dir_name
fi

find $home_demo/$dir_name -type f -mtime +1 -exec rm -rf {} \;

for file in $@; do
    if [ ! -e $file ]; then
        echo "$file not exists."
        continue
    else
        mv $file $home_demo/$dir_name
    fi
done
