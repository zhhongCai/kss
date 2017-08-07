#!/bin/bash
set -e
set -x 

pid=$(ps -ef | grep  service-area | grep -v "grep" | awk -F" "  '{ print $2 }')
kill -9 $pid

