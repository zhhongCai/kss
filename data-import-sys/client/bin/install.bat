@echo off
echo [INFO] Install jar to local repository.

cd %~dp0
cd ..
call mvn clean install -Dmaven.test.skip=true -U
cd bin
pause