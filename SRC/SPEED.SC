;**
;**	Logics for Script 803 -- Speed Test
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford & Al Lowe
;**
;**	Last Update:  June 18, 1991
;**


(script#	SPEEDTEST)

(public
	speedTest	0
)

(local
	doneTime
	machineSpeed
	cfgHandle
	fastThreshold
	mediumThreshold
)

(instance fred of Actor)

(instance speedTest of Room
	(properties
		picture		0
		style			PLAIN
	)

	(method (init)
		; read in version number from disk
		(= cfgHandle (FileIO fileOpen {version} fRead))
		(FileIO fileFGets version 6 cfgHandle)
		(FileIO fileClose cfgHandle)

		(super init:)

		(sounds eachElementDo: #stop)

		(while (u> (GetTime) $fc00))
		(fred
			view:				vSpeedTest,
			x:					0,
			y:					99,
			setStep:			1 1,
			setMotion:		MoveTo 319 99,
			setCycle:		Walk,
			init:
		)

		(= speed 0)
		(= machineSpeed 0)
		(= fastThreshold   (if (Btst isVga) 90 else 60))
		(= mediumThreshold (if (Btst isVga) 59 else 30))
	)

	(method (doit)
		(super doit:)

		(++ machineSpeed)
		(if (== machineSpeed 1)
			(= doneTime (+ 60 (GetTime)))	;**	when another second is over
		)

		(if (u< doneTime (GetTime))	;**	when the second is over
			(cond
				(( > machineSpeed fastThreshold)
					(= howFast fast)
					(theGame detailLevel: 3)
					(= larrySpeed (+ 1 howFast))
					(= speed 1)
				)
				(( > machineSpeed mediumThreshold)
					(= howFast medium)
					(theGame detailLevel: 2)
					(= larrySpeed (+ 1 howFast))
					(= speed 1)
				)
				(else
					(= howFast slow)
					(theGame detailLevel: 1)
					(= larrySpeed (+ 1 howFast))
					(= speed 0)
				)
			)

			; set up initial move speed & put the cursor down in right corner
			(theGame
				egoMoveSpeed:  larrySpeed,
			)
			(ego
				moveSpeed:		larrySpeed,
				cycleSpeed:		larrySpeed,
			)
;**				(Printf "Congratulations!
;**					You are running at a machineSpeed of %d,
;**					with a %s howFast multiplier.
;**					Your fastThreshold is %d and
;**					your mediumThreshold is %d
;**					all because you're %srunning a VGA card.\n\n
;**					Please write all this down on your first bug report
;**					(assuming, of course, that you find a bug in this version!)"
;**
;**					machineSpeed
;**					(switch howFast
;**						(0 {slow})
;**						(1 {medium})
;**						(2 {fast})
;**					)
;**					fastThreshold
;**					mediumThreshold
;**					(if (Btst isVga) {} else {not })
;**				)
			(curRoom newRoom: restartRoom)
		)
	)
)
