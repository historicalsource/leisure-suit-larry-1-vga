(script# DISP)

(public
	disposeCode	0
)

(instance disposeCode of Code
	(method (doit n)
;--- unload system scripts:
		(LoadMany FALSE
;			AVOIDER
;			ORBIT
;			EXTRA
;			TEXTRA
;			RFEATURE
;			DEMO
;			TIMER
;			QSCRIPT
;			CAT
;			TRACK
;			GOTOSAID
;			LASTLINK
;			TIMEDCUE
;			APPROACH
;			SORTCOPY
			JUMP
;			PATH
;			FOLLOW
;			BELTWAY
;			WIND
			CHASE
			WANDER
			PCHASE
			OSC
			NAMEFIND
			REVERSE
			SORT
			COUNT
			DPATH
			FORCOUNT
;			SIGHT
			MOVEFWD
			RANDCYC
			STOPWALK
			MOVECYC
			FILE
;			POLYPATH
;			POLYGON
;			LLROOM
;			NOSEPICK
;			HINTBOOK
;			RMNSCRIPT
			PRINTD
			WRITEFTR
			BLOCK
		)

;		(if (not (OneOf n NOGRAVROOMS))
;			(DisposeScript INERTIA)
;		)

		(DisposeScript DEBUG)	; Debug Object

		; toss ourselves
		(DisposeScript DISP)
	)
)

