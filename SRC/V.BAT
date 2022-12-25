@echo off

call settools

pushd ..\view

if "%1" == "" goto JustLoad

x:ve256 %1.v56
goto Done

:JustLoad
x:ve256

:Done
popd
