@echo off
call mvn clean install
call mvn jetty:run
exit /b
