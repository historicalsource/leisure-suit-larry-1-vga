@echo off
pcopy *.sc classes /U /CD:$ /CF:make.lst
type make.lst
sc -ao..\script @make.lst > sc.err
del make.lst
