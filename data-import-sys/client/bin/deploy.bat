@echo off
cd %~dp0
cd ..
call mvn clean package install deploy:deploy -U -e -B -Dmaven.test.skip=true
pause