@echo off

call settools

pushd ..\pic

if "%1" == "" goto JustLoad

attrib -r %1.p56
x:pe256 %1.p56
attrib +r %1.p56
goto Done

:JustLoad
x:pe256

:Done
popd
