@echo off

call settools

pushd ..\pic

if "%1" == "" goto JustLoad

attrib -r %1.p16
x:pe %1.p16
attrib +r %1.p16
goto Done

:JustLoad
x:pe

:Done
popd

