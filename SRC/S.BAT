@echo off

call setsys
call settools
inc1
echo.
x:sc -ao..\script %1 %2 %3 %4 %5 %6 %7 %8 > sc.err

