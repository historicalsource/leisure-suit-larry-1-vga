@echo off
:Begin
if exist %1.sc goto GotValidFile
echo  No such file as "%1.sc"
quit
:GotValidFile
pcopy %1.sc ..\src /x/nw/ex
:DoAnother
shift
if not "%1" == "" goto Begin

