@echo off
pkzip tmp.zip todo *.fea *.txt *.s?
uniqname tmp.zip -D:MD -P..\doco
echo		Ready to move *.FEA & *.TXT to net...
pause
for    %%f    in    (      *.fea      ) do call append.bat %%f
for    %%f    in    (      *.txt      ) do call append.bat %%f
xcopy *.sc  M:\games\LL1\src
xcopy *.sc  ..\src
echo	Ready to del FEA, TXT and SC files...
pause 	
del *.fea
del *.txt
del *.sc

