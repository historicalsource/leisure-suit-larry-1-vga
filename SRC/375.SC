;**
;**	Logics for Room 375 -- End of Game Sequence
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Mike Larsen
;**
;**	Last Update:  June 25, 1991
;**


(script# 375)

(public
	rm375		0
)

(procedure
	KillFireWorks
	FindSize
	Credits
)

(local
	fireworkCounter = 0
	colorCycle
	sizeElem = [ 0 0 0 0 ]
	fireworkNum
	sparkX
	sparkY
)

(define	pFireworks				376)
(define 	MAX_EXPLOSIONS			30)
(define	TMP_PALETTE				$8b)

	(define	vFireWorks			375)
		(define	lDetonate		0)
		(define	lFireWorks		1)
		(define	lGlitter			2)

(define	sFWLaunch		373)
(define	sFWExplode		374)
;(define	mThemeSong		700)


(instance rm375 of LLRoom
;	(properties
;		picture		pBlackScreen
;	)
	(method (init &tmp foo)
		(= foo JumpTo)
		(theGame
			setCursor:		theCursor TRUE 310 190,
		)
		(if
			(Btst fCredits)
			(curRoom drawPic: pBlackScreen)
			(curRoom setScript: sCredits)
		else
			(curRoom drawPic: pFireworks)
			(theMusic2
				number:	sFWExplode,
				loop:		1,
				flags:	1,
			)
			(theMusic3
				number:	sFWExplode,
				loop:		1,
				flags:	1,
			)
			(LoadMany SOUND sFWLaunch sFWExplode mThemeSong)
			(HandsOff)
			(curRoom setScript: sEcstasy)
			(ego
				init:,
				x:			100,
				y:			1000,
				stopUpd: ,
			)
		)
		(super init:)
		(theGame
			setCursor:		theCursor TRUE 310 190,
		)
	)
)

;***************
;*** SCRIPTS ***
;***************	

(instance sEcstasy of Script
	(method (doit)
		(super doit:)
		(cond
			((== colorCycle 2)
				(= colorCycle 0)
				(Palette PALLoad pFireworks PAL_REPLACE)
			)
			(colorCycle
				(Palette PALCycle 64 190 1)
			)
		)
	)
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= seconds 2)
			)
			(
				(= sparkX (Random 40 280))
				(= sparkY (Random 20 100))
				(= register (Random 140 180))
				(sfxFWLaunch play:)
				(launchFirework
					init:,
					x:				register,
					setCel:		0,
					setCycle:	EndLoop
				)
				(sparky
					init:			,
					show:			,
					x:				register,
					y:				185,	
					moveSpeed:	(+ 1 howFast),
					setMotion:	sparkJump sparkX sparkY self
 				)
			)
			(
				(sparky hide:)
				(switch fireworkNum
					(0
						(firework1
							cycleSpeed:		(* 3 howFast),
							init:				,
							x:					sparkX,
							y:					sparkY,
							setCel:			0,
							setCycle:		EndLoop,
						)
						(sfxFWExplode play:)
					)
					(1
						(firework2
							cycleSpeed:		(* 3 howFast),
							init:				,
							x:					sparkX,
							y:					sparkY,
							setCel:			0,
							setCycle:		EndLoop,
						)
						(theMusic2 play:)
					)
					(2
						(firework3
							cycleSpeed:		(* 3 howFast),
							init:				,
							x:					sparkX,
							y:					sparkY,
							setCel:			0,
							setCycle:		EndLoop,
						)
						(theMusic3 play:)
					)
					(3
						(firework4
							cycleSpeed:		(* 3 howFast),
							init:				,
							x:					sparkX,
							y:					sparkY,
							setCel:			0,
							setCycle:		EndLoop,
						)
						(sfxFWExplode play:)
					)
					(4
						(firework5
							cycleSpeed:		(* 3 howFast),
							init:				,
							x:					sparkX,
							y:					sparkY,
							setCel:			0,
							setCycle:		EndLoop,
						)
						(theMusic2 play:)
					)
					(5
						(firework6
							cycleSpeed:		(* 3 howFast),
							init:				,
							x:					sparkX,
							y:					sparkY,
							setCel:			0,
							setCycle:		EndLoop,
						)
						(theMusic3 play:)
					)
				)
				(= cycles (Random 1 4))
			)
			(
				(= colorCycle TRUE)
				(if (< fireworkNum 6)
					(++ fireworkNum)
				else
					(= fireworkNum 0)
				)
				(= cycles 1)
			)
			(
				(if ( > fireworkCounter MAX_EXPLOSIONS)
					(= colorCycle 2)
					(= cycles 1)
				else
					(++ fireworkCounter)
					(self 
						start:	1,
						init:		,
					)
				)
			)
			(
				(sfxFWExplode dispose:)
				(= seconds 5)
			)
			(
				(KillFireWorks)
				(self dispose:)
				(curRoom newRoom: 370)
			)
		)
	)
)

(instance sCredits of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= seconds 3)
			)
			(
				(Credits {Executive Producer\n
							Ken Williams})
				(= seconds 3)
			)
			(
				(Credits {Creative Director\n
							Bill Davis})
				(= seconds 3)
			)
			(
				(Credits {Director\n
							Al Lowe})
				(= seconds 3)
			)
			(
				(Credits {Producer\n
							Stuart Moulder})
				(= seconds 3)
			)
			(
				(Credits {Game Designer\n
							Al Lowe})
				(= seconds 3)
			)
			(
				(Credits {Art Designer\n
							William Skirvin})
				(= seconds 3)
			)
			(
				(Credits {Lead Programmer\n
							Oliver Brelsford})
				(= seconds 3)
			)
			(
				(Credits {Composer\n
							Chris Braymen})
				(= seconds 3)
			)
			(
				(Credits {Animators\n
							Barry Smith\n
							Jennifer Shontz\n
							Roger Hardy Jr.\n
							William D. Skirvin\n
							Eric Apel\n
							Russ Truelove\n
							Desie Hartman})
				(= seconds 3)
			)
			(
				(Credits {Background Artists\n
							William D. Skirvin\n
							Jay Allan Friedmann\n
							Jennifer Shontz\n
							Jane Cardinal\n
							Maurice Morgan})
				(= seconds 3)
			)
			(
				(Credits {Programmers\n
							Oliver Brelsford\n
							Mike Larsen\n
							Al Lowe})
				(= seconds 3)
			)
			(
				(Credits {Witty Text and Documentation\n
							Josh Mandel})
				(= seconds 3)
			)
			(
				(Credits {Music Director\n
							Mark Seibert\n
							\n
							Theme Song Composed by\n
							Al Lowe\n
							\n
							Sound Effects\n
							Chris Braymen\n
							Mark Seibert\n
							and Orpheus Hanley})
				(= seconds 3)
			)
			(
				(Credits {System Programmers\n
							J. Mark Hood\n
							Jeff Stephenson\n
							Bob Heitman\n
							Pablo Ghenis\n
							Dan Foy\n
							Larry Scott\n
							Mark Wilden\n
							Eric Hart\n
							Chris Smith})
				(= seconds 3)
			)
			(
				(Credits {Quality Assurance Team Leaders\n
							Mike Pickhinke and Keri Cooper})
				(= seconds 3)
			)
			(
				(curRoom
					drawPic: pFireworks SCROLLUP
				)
				(= seconds 20)
			)
			(
				(theMusic2 fade: 0 15 15 1 self)
			)
			(
				(curRoom
					drawPic: pFireworks SCROLLUP
				)
				(Display {It's over!\n
							Quit!!\n
							Get a Life!!!}
					#p_color: 	colWhite
					#p_at: 		1 30
					#p_width: 	318
					#p_font: 	giantFont
					#p_mode: 	teJustCenter
				)
				(= seconds 3)
			)
			(
				(= quit TRUE)
			)
		)
	)
)

;**************
;*** SOUNDS ***
;**************
(instance sfxFWExplode of Sound
	(properties
		number		sFWExplode
		loop			1
		flags			1
	)
)
(instance sfxFWLaunch of Sound
	(properties
		number		sFWLaunch
		loop			1
		flags			1
	)
)
;*************
;*** PROPS ***
;*************


(instance sparkJump of JumpTo
	(method (doit)
		(if (>= (++ b-moveCnt) (client moveSpeed?))
			(= b-moveCnt 0)
			(super doit:)
		)
	)
)

(instance sparky of Actor
	(properties
		view		vFireWorks
		loop		0
		cel		0
		y			185
		yStep		5
		xStep		4
		signal	(| ignrAct fixedCel fixedLoop)
		moveSpeed 5
	)
)

(instance launchFirework of Prop
	(properties
		view		vFireWorks
		loop		0
		cel		0
		x			150
		y			189
		signal	(| ignrAct ignrHrz)
	)
)

(instance firework1 of Prop
	(properties
		view		vFireWorks
		loop		1
		cel		0
		x			61
		y			57
		signal	(| ignrAct ignrHrz)
	)
)
(instance firework2 of Prop
	(properties
		view		vFireWorks
		loop		2
		cel		0
		x			61
		y			57
		signal	(| ignrAct ignrHrz)
	)
)
(instance firework3 of Prop
	(properties
		view		vFireWorks
		loop		3
		cel		0
		x			61
		y			57
		signal	(| ignrAct ignrHrz)
	)
)
(instance firework4 of Prop
	(properties
		view		vFireWorks
		loop		4
		cel		0
		x			61
		y			57
		signal	(| ignrAct ignrHrz)
	)
)
(instance firework5 of Prop
	(properties
		view		vFireWorks
		loop		5
		cel		0
		x			61
		y			57
		signal	(| ignrAct ignrHrz)
	)
)
(instance firework6 of Prop
	(properties
		view		vFireWorks
		loop		6
		cel		0
		x			61
		y			57
		signal	(| ignrAct ignrHrz)
	)
)


;*******************************************************
;* procedure for finding margin to center text on screen
;*******************************************************

(procedure (FindSize theText)
	(TextSize @sizeElem theText 0 315)
	(return (/ (- 180 (- [sizeElem 2] [sizeElem 0])) 2))
)

;*********************************
;* procedure for scrolling credits
;*********************************

(procedure (Credits theText &tmp margin c )
	(curRoom
		drawPic: pFireworks SCROLLUP
	)
	(= margin (FindSize theText))

	;**	grab a color from the global vars between 130 and 147
	(= c [colDRed (- (Random 130 147) 130)])

	(Display
		theText
		#p_color: 	c
		#p_at: 		1 margin
		#p_width: 	318
		#p_font: 	bigFont
		#p_mode: 	teJustCenter
	)
)

(procedure (KillFireWorks)
	(theMusic2 stop:)
	(theMusic3 stop:)
	(sfxFWExplode stop:)
	(sfxFWLaunch stop:)
	(firework1 dispose:)
	(firework2 dispose:)
	(firework3 dispose:)
	(firework4 dispose:)
	(firework5 dispose:)
	(firework6 dispose:)
)
