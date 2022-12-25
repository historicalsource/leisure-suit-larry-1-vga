@echo off
cd M:\games\ll1\src
:Begin
if exist %1.sc goto GotValidFile
echo  No such file as "%1.sc"
quit
:GotValidFile
 copy %1.sc ..\src
pcopy %1.sc M: /x/nw/ex
:DoAnother
shift
if not "%1" == "" goto Begin

