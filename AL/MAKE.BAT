@echo off
C:\tools\pcopy *.sc classes /U /CD:$ /CF:make.lst
type make.lst
C:\system\exe\sc -a @make.lst > sc.err
del make.lst
