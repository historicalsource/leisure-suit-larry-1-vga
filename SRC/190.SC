;**
;**	Logics for Room 190 -- Sierra Underground
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Mike Larsen
;**
;**	Last Update:  June 13, 1991
;**


(script# 190)

(public
	rm190		0
)


;(define	sGroundOpens				191)
;(define	sElevator					192)
(define	sFactory						193)
(define	sEgoOffElevator			194)
(define	sBlender						195)
(define	sGurgle						196)
(define	sMachineGrunts				197)
(define	sSquirt						198)
(define	sConveyor					199)

(define	pUnderGround				190)

	(define	vUnderGround			190)
		(define	lBlenderLid			0)
		(define	lLarryStew1			1)
		(define	lLarryStew2			2)
		(define	lLarryStew3			3)
		(define	lDeadLarry			4)
		(define	lShaft				5)
		(define	lPlatform2			6)
		(define	lPlatform1			7)


	(define	vLarryCollapsing		191)
		(define	lLarryInPipe		0)
		(define	lLarryDrip			1)

	(define	vUnderGroundExtras	192)
		(define	lTypist1				0)
		(define	lSmallScreen		1)
		(define	lTypist2				2)
		(define	lNudeLady			3)


(instance rm190 of LLRoom
	(properties
		picture	190
	)

	(method (init)
		(LoadMany VIEW
			vUnderGround vLarryCollapsing vUnderGroundExtras
		)
		(LoadMany SOUND
			sBlender sSquirt sMachineGrunts sGurgle sGroundOpens
			sElevator sConveyor sFactory sEgoOffElevator
		)
		(theGame
			setCursor:		theCursor FALSE 333 199,
		)
		(typist1
			init:,
			cycleSpeed:		(+ 2 howFast),
			setCycle:		Forward
		)
		(typist2
			init:,
			cycleSpeed:		(+ 3 howFast),
			setCycle:		Forward
		)
		(smallScreen
			init:,
			cycleSpeed:		(+ 4 howFast),
			setCycle:		Forward
		)
		(nudeLady
			init:,
			cycleSpeed:		(+ 3 howFast),
			setCycle:		Oscillate,
		)
		(theGame
			setSpeed:		3,
		)
		(blenderLid
			cycleSpeed:		howFast,
			init:				,
			stopUpd:			,
		)
		(shaft1
			cycleSpeed:		howFast,
			init:
		)
		(platform1
			cycleSpeed:		howFast,
			init:
		)
		(shaft2
			cycleSpeed:		howFast,
			init:				,
			stopUpd:
		)
		(platform2
			cycleSpeed:		howFast,
			init:				,
			stopUpd:
		)
		(deadLarry
			cycleSpeed:		howFast,
			init:
		)
		(curRoom setScript: sRecycle)
		(sfxFactory play:)
		(super init:)
	)
)

;***************
;*** SCRIPTS ***
;***************

(instance sRecycle of Script
	(method (doit)
		(super doit:)
		(if
			(== (platform2 cel?) 3)
			(deadLarry
				setPri:	1
			)
		)
		(if
			(and
				(== state 18)
				(== (sfxNewLarry prevSignal?) -1)
			)
			(= cycles 1)
		)
	)
	(method (changeState newState)
		(switchto (= state newState)
			(
				(HandsOff)
				(shaft1
					illegalBits:0,
					yStep:		1,
					setLoop:		lShaft,
					setCel:		0,
					setMotion:	MoveTo 95 18,
				)
				(platform1
					illegalBits:0,
					yStep:		1,
					setLoop:		lPlatform1,
					setMotion:	MoveTo 95 18,
				)
				(deadLarry
					illegalBits:0,
					yStep:		1,
					setLoop:		lDeadLarry,
					setMotion:	MoveTo 99 38 self
				)
			)
			(
				(theMusic2 stop:)
				(shaft1			stopUpd:,)
				(platform1		stopUpd:,)
				(deadLarry		stopUpd:,)
				(blenderLid
					cycleSpeed:	4,
					setCycle:	EndLoop self,
				)
			)
			(
				(sfxDumpEgo	play:)
				(platform1
					setCel:		1,
				)
				(deadLarry
					setCel:		1,
					posn:			80 28
				)
				(= cycles 6)
			)
			(
				(platform1
					setCel:		2,
				)
				(deadLarry
					setCel:		2,
					posn:			71 41
				)
				(= cycles 6)
			)
			(
				(deadLarry
					setCel:		3,
					posn:			63 54
				)
				(= cycles 6)
			)
			(
				(deadLarry
					setCel:		4,
					posn:			60 64,
					setCycle:	EndLoop self
				)
			)
			(
				(platform1
					cycleSpeed:	3,
					setCycle:	BegLoop self,
				)
			)
			(
				(platform1		stopUpd:)
				(blenderLid
					cycleSpeed:	3,
					setCycle:	BegLoop self,
				)
				(sfxLidCloses play:)
			)
			(
				(blenderLid		stopUpd:)
				(= cycles 30)
			)
			(
				(sfxBlender
					play:			,
					hold:			10
				)
				(deadLarry
					setLoop:		lLarryStew1,
					posn:			63 66,
					cycleSpeed:	4,
					setCycle:	Forward
				)
				(= cycles 30)
			)
			(
				(deadLarry
					cycleSpeed:	3,
				)
				(= cycles 30)
			)
			(
				(deadLarry
					cycleSpeed:	2,
				)
				(= cycles 30)
			)
			(
				(deadLarry
					cycleSpeed:	1,
				)
				(= cycles 30)
			)
			(
				(deadLarry
					cycleSpeed:	0,
				)
				(= cycles 30)
			)
			(
				(deadLarry
					setLoop:		lLarryStew2,
				)
				(= seconds 6)
			)
			(
				(sfxBlender release:)
				(deadLarry
					cycleSpeed:	4,
					setLoop:		lLarryStew3,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(sfxGurgle play:)
				(deadLarry
					view:			vLarryCollapsing,
					setPri:		6,
					posn:			48 92,
					cycleSpeed:	6,
					setLoop:		lLarryInPipe,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(= cycles 5)
			)
			(
				(sfxGurgle		stop:)
				(sfxNewLarry	play:)
				(deadLarry		hide:)

			)
			(
				(sfxNewLarry stop:)
				(sfxSquirt play:)
				(deadLarry
					show:			,
					posn:			232 126,
					setLoop:		lLarryDrip,
					setCel:		0,
					setCycle:	EndLoop self
				)

			)
			(
				(sfxSquirt stop:)
				(sfxConveyor play:)
				(deadLarry
					setStep:		1 1,
					setMotion:	MoveTo 187 137 self
				)
			)
			(
				(sfxConveyor stop:)
				(sfxGroundOpens play:)
				(= seconds 3)
			)
			(
				(sfxGroundOpens stop:)
				(sfxElevator play:)
				(deadLarry
					setPri:		12,
					yStep:		1,
					setMotion:	MoveTo 187 0 ,
				)
				(platform2
					setLoop:		lPlatform2,
					setPri:		8,
					setCycle:	Walk,
					setStep:		1 1,
					cycleSpeed:	20,
					setMotion:	MoveTo 189 0 ,
				)
				(shaft2
					setLoop:		lShaft,
					setStep:		1 1,
					setPri:		4,
					setMotion:	MoveTo 189 0 self ,
				)
			)
			(
				(sfxElevator fade:)
				(= seconds 2)
			)
			(
				(Print "(Now you know what we have to go through every
					time you press \"Restart!\")" LOW)
				(theGame restart:)
			)
		)
	)
)

;****************
;*** SOUND FX ***
;****************

(instance sfxLidCloses of Sound
	(properties
		number	sDoorClose
		vol		127
		loop		1
		flags		0
	)
)
(instance sfxGroundOpens of Sound
	(properties
		number	sGroundOpens
		vol		127
		loop		1
		flags		0
	)
)
(instance sfxElevator of Sound
	(properties
		number	sElevator
		vol		127
		loop		1
		flags		1
	)
)
(instance sfxDumpEgo of Sound
	(properties
		number	sEgoOffElevator
		vol		127
		loop		1
		flags		0
	)
)
(instance sfxBlender of Sound
	(properties
		number	sBlender
		vol		127
		loop		1
		flags		0
	)
)
(instance sfxFactory of Sound
	(properties
		number	sFactory
		vol		127
		loop		-1
		flags		0
	)
)
(instance sfxGurgle of Sound
	(properties
		number	sGurgle
		vol		127
		loop		-1
		flags		0
	)
)
(instance sfxNewLarry of Sound
	(properties
		number	sMachineGrunts
		vol		127
		loop		1
		flags		0
	)
)
(instance sfxSquirt of Sound
	(properties
		number	sSquirt
		vol		127
		loop		-1
		flags		0
	)
)
(instance sfxConveyor of Sound
	(properties
		number	sConveyor
		vol		127
		loop		1
		flags		0
	)
)


;**************
;*** ACTORS	***
;**************

(instance deadLarry of Actor
	(properties
		view		vUnderGround
		loop		lDeadLarry
		cel		0
		x			99
		y			28
		signal	ignrAct
	)
)
(instance shaft1 of Actor
	(properties
		view		vUnderGround
		loop		lShaft
		cel		0
		x			95
		y			8
		yStep		1
		priority	1
		signal 	(| ignrAct fixPriOn)
	)
)
(instance platform1 of Actor
	(properties
		view		vUnderGround
		loop		lPlatform1
		cel		0
		x			95
		y			8
		yStep		1
		priority	5
		signal	(| ignrAct fixPriOn)
	)
)
(instance shaft2 of Actor
	(properties
		view		vUnderGround
		loop		lShaft
		cel		0
		x				189
		y				141
		yStep		1
		priority	1
		signal 	(| ignrAct fixPriOn)
	)
)
(instance platform2 of Actor
	(properties
		view		vUnderGround
		loop		lPlatform2
		cel		0
		x				189
		y				141
		yStep		1
		priority	1
		signal	(| ignrAct fixPriOn)
	)
)
(instance nudeLady of Actor
	(properties
		view 		vUnderGroundExtras
		loop		lNudeLady
		cel		0
		x			45
		y			166
		yStep		8
		detailLevel 1
	)
)

;*************
;*** PROPS ***
;*************



(instance blenderLid of Prop
	(properties
		view		vUnderGround
		loop		lBlenderLid
		cel		0
		x				43
		y				34
	)
)
(instance typist1 of Prop
	(properties
		view		vUnderGroundExtras
		loop		lTypist1
		cel		0
		x			7
		y			143
		detailLevel 1
	)
)
(instance smallScreen of Prop
	(properties
		view		vUnderGroundExtras
		loop		lSmallScreen
		cel		0
		x			77
		y			148
		detailLevel 1
	)
)
(instance typist2 of Prop
	(properties
		view 		vUnderGroundExtras
		loop		lTypist2
		cel		0
		x			72
		y			158
		detailLevel 1
	)
)

