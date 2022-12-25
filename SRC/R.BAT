@echo off

call setsys
call settools
inc1

if "%1" == ""        goto NOLOAD
if "%1" == "mt32"    goto MT32
if "%1" == "std"     goto STD
if "%1" == "blast"   goto BLAST
if "%1" == "mtblast" goto MTBLAST
if "%1" == "cms"     goto CMS
if "%1" == "adl"     goto ADL
if "%1" == "pro"     goto PRO

:ERROR
   echo  Usage:  "%0 [mt32 std blast mtblast cms adl pro]"
   echo  So next time, type "%0 " and one of the above choices,
   echo  or just "%0".
   goto Done

:NOLOAD
   x:scidh -udaU"hunk.use" -c7 noload.256
   goto Done

:MT32
   x:scidh -d mt32.256
   goto Done

:STD
   x:scidh -d where.256
   goto Done

:PRO
   x:scidh -d proaudio.256
   goto Done

:BLAST
   x:scidh -d -c7 sndblast.256
   goto Done

:CMS
   x:scidh -d -c7 cms.256
   goto Done

:ADL
   x:scidh -d -c7 adlib.256
   goto Done

:MTBLAST
   x:scidh -d -c7 mtblast.256
   goto Done

:Done
