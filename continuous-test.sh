#!/bin/bash

WATCH_DIRS="src/main/java src/test/java"
COMMAND="mvn verify"

echo "Watching directories: $WATCH_DIRS"
echo "Running '$COMMAND' on changes. after first.."

mvn clean verify site
inotifywait -m -r -e modify,create,delete,move --format '%w%f' $WATCH_DIRS | while read FILE
do
    echo "File changed: $FILE"
    $COMMAND
done
