@echo off
:Begin
if exist ..\src\%1.sc goto GotValidFile
echo  No such file as "%1.sc"
quit
:GotValidFile
pcopy ..\src\%1.sc /x/nw/ex
:DoAnother
shift
if not "%1" == "" goto Begin

