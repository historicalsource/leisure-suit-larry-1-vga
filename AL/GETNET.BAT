@echo off
cd M:\games\ll1\src
:Begin
if exist M:%1.sc goto GotValidFile
echo  No such file as "%1.sc"
quit
:GotValidFile
pcopy M:%1.sc /x/nw/ex
 copy %1.sc ..\src
:DoAnother
shift
if not "%1" == "" goto Begin

