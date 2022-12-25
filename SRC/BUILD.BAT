@echo off

call setsys
call settools
inc1

copy s:classdef
copy s:selectors

del ..\script\*.scr
del ..\script\*.tex
del  ..\script\*.voc
del *.scr
del *.tex
del *.voc
del build.err

rem x:sc @sys.lst -aO %1 %2 %3 %4 %5 %6 %7 %8 %9 >> build.err
x:sc @build.lst   -aO -o..\script %1 %2 %3 %4 %5 %6 %7 %8 %9 >> build.err
x:tc *.tc                                                    

del *.th

move text.719 ..\script\719.tex 
move text.141 ..\script\141.tex 
move text.721 ..\script\721.tex 
move text.722 ..\script\722.tex 
move text.723 ..\script\723.tex 
move text.724 ..\script\724.tex 
move text.725 ..\script\725.tex 
move text.726 ..\script\726.tex 
move text.727 ..\script\727.tex 
move text.728 ..\script\728.tex 
move text.729 ..\script\729.tex 
move text.730 ..\script\730.tex 
move text.731 ..\script\731.tex 
move text.732 ..\script\732.tex 
move text.733 ..\script\733.tex 
move text.734 ..\script\734.tex 
move text.735 ..\script\735.tex 
move text.736 ..\script\736.tex 
move text.737 ..\script\737.tex 
move text.738 ..\script\738.tex 
move text.739 ..\script\739.tex 
move text.740 ..\script\740.tex 
move text.741 ..\script\741.tex 
move text.742 ..\script\742.tex 
move text.743 ..\script\743.tex 
move text.744 ..\script\744.tex 
move text.745 ..\script\745.tex 
move text.746 ..\script\746.tex 
move text.747 ..\script\747.tex 
move text.748 ..\script\748.tex 
move text.749 ..\script\749.tex 
move text.750 ..\script\750.tex 
move text.751 ..\script\751.tex 
move text.752 ..\script\752.tex 

x:sc 000.sc       -aO -o..\script %1 %2 %3 %4 %5 %6 %7 %8 %9 >> build.err

