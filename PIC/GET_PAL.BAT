@echo off
if "%1" == ""            goto NeedFilename
if not exist %1.p56      goto NoFileFound
if exist %1.pal          goto FoundPalette
if not exist get_pal.dbg goto NeedDebugScript

:WhatTheHellElseCouldPossiblyGoWrong
copy  %1.p56 %1.pal > NUL
debug %1.pal < get_pal.dbg > NUL
goto END

:FoundPalette
echo  There's already a %1.PAL file.  Erase it first.
goto END

:NoFileFound
echo  Can't find picture "%1.P56" 
goto END

:NeedDebugScript
echo  Can't find the debug script "GET_PAL.DBG" 
goto END

:NeedFilename
echo  Usage:  GET_PAL ### [where ### is the number of the .P56 
echo                       file you want to steal a palette from]
echo  .
echo  This batch file requires DEBUG in your path.
:End
