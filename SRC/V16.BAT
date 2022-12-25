@echo off

call settools

pushd ..\view

if "%1" == "" goto JustLoad

x:ve %1.v16
goto Done

:JustLoad
x:ve16

:Done
popd

