#!/bin/bash

if [ "$#" -le 5 ]; then
    echo "More testing is needed"
    exit 1
fi

total_time=0
count=0

for ((i = 1; i <= 3; i++)); do
    # use $(()) to do arithmetic operations
    total_time=$((total_time + $1 * 60))
    # use shift to remove the first argument
    shift
    ((count++))
done

average_time=$((total_time / count))

while [ "$#" -gt 0 ]; do
    total_time=$((total_time + $1 * 60))
    shift
    ((count++))
done

echo "Average execution time: $average_time"
echo "Count of executions: $count"

if [ "$count" -ge 5 ]; then
    echo "The testing is done"
else
    echo "More testing is needed"
fi

