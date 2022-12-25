@echo off

call setsys
call settools
inc1

if "%1" == ""        goto NOLOAD
if "%1" == "mt32"    goto MT32
if "%1" == "std"     goto STD
if "%1" == "blast"   goto BLAST
if "%1" == "mtblast" goto MTBLAST

:ERROR
   echo  Usage:  "%0 [mt32 std blast mtblast]"
   echo  So next time, type "%0 " and one of the above choices,
   echo  or just "%0".
   goto Done

:NOLOAD
   x:scid -d -c7 noload.16
   goto Done

:MT32
   x:scid -d mt32.16
   goto Done

:STD
   x:scid -d where.16
   goto Done

:BLAST
   x:scid -d -c7 sndblast.16
   goto Done

:MTBLAST
   x:scid -d -c7 mtblast.16
   goto Done

:Done
