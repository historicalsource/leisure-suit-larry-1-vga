;**
;**	Logics for Room 610 -- Disco Interior
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Mike Larsen
;**
;**	Last Update:  June 24, 1991
;**

(script# 610)

(public
	rm610		0
)

(local
	oldSpeed
	egoX
	fawnX
	larryX
	cueNum
	sharkTimer
	fish1Timer
	fish2Timer
	fish3Timer
	clickX
	clickY
)

(procedure
	ReviveActors
	DumpActors
)

(define TMPPAL $8b)

;(define	mDiscoInterior				610)
(define	mDanceNumber 				611)
(define	sCorrect						711)

(define	pDiscoInterior				610)
	(define	vDiscoInterior			610)
		(define	lTunaRight 			0)
		(define	lTunaLeft 			1)
		(define	lSharkRight			2)
		(define	lSharkLeft			3)
		(define	lFish1Right			4)
		(define	lFish1Left			5)
		(define	lFish2Right			6)
		(define	lFish2Left			7)
		(define	lFish3Right			8)
		(define	lFish3Left			9)
		(define	lTunaTongue			10)
		(define	lLarrySitting		11)
		(define	lFloorLights		12)

	(define	vLarryDancing			611)
		(define	lDiscoLarry			0)
		(define	lRussianRight		1)
		(define	lRussianLeft		2)
		(define	lLarrySpinning		3)
		(define	lTwoStepLarry		4)
		(define	l1FootHeyRight		5)
		(define	l1FootHeyLeft		6)
		(define	l2FootHeyRight		7)
		(define	l2FootHeyLeft		8)
		(define	lLarryDanceRight	9)
		(define	lLarryDanceLeft	10)

;(define	vFawnWalking			612)
		(define	lFawnSitting 		4)

	(define	vFawnDancing 			613)
		(define	lSpinFawnLeft		0)
		(define	lSpinFawnRight		1)
		(define	lDipFawnRight		2)
		(define	lDipFawnLeft		3)
		(define	lTossFawn			4)
		(define	lFawnFlies			5)
		(define	lFawnDanceRight	6)
		(define	lFawnDanceLeft		7)

	(define	vDiscoExtras			614)
		(define	lAlEyes				0)
		(define	lRightGuyEyes		1)
		(define	lRogerHead			2)

(instance rm610 of LLRoom
	(properties
		picture	610
		south		600
		lookStr	"The disco is filled with remarkably similar guys,
					all looking for remarkably similar girls."
	)
	(method (init &tmp foo)
		(= foo Oscillate)
		(= foo ForwardCounter)
		(LoadMany VIEW
			vDiscoInterior
			vFawnWalking
			vDiscoExtras
			vLarryDancing
			vFawnDancing
		)
		(LoadMany SOUND
			mDiscoInterior
			mDanceNumber
			sDrumFill1
			sCorrect
		)
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	0 189 0 0 319 0 319 189 283 189 290 180 219 176 121 179
						115 172 118 159 216 136 276 136 278 116 300 110 249 107
						239 100 249 84 271 75 198 75 177 80 148 77 107 78 89 84 71 84
						37 95 40 109 12 125 29 139 102 139 113 157 103 164 41 167
						28 186 95 186 96 189,
				yourself
			)
		)
		(= sharkTimer (Random 400 700))
		(= fish1Timer (Random 100 300))
		(= fish2Timer (Random 150 350))
		(= fish3Timer (Random 200 400))
		(switch prevRoomNum
			(600
				(theMusic fade:)
				(theMusic3
					number:		mDiscoInterior,
					vol:			127,
					loop:			-1,
					flags:		1,
					play:
				)
				(self south: prevRoomNum)
				(ego init:)
			)
			(615
				(Bset fLookedAtFawn)
				(ego
					normal:	FALSE,
					init:		,
					view:		vDiscoInterior,
					setLoop:	lLarrySitting,
					setCel:	3,
					x:			220,
					y:			73,
				)
				(if
					(Btst fReadyToDance)
					(theMusic2 prevSignal: 0)
					(Load TMPPAL 610)
					(HandsOff)
					(if
						(not (Btst isVga))
						(floorLight1 init:)
						(floorLight2 init:)
					)
					(curRoom setScript:	sDance)
				else
					(if
						(Btst fFawnLeaves)
 						(curRoom setScript: sFawnIsHistory)
					else
						(HandsOn)
					)
				)
			)
			(else
				(theMusic3
					number:		mDiscoInterior,
					vol:			127,
					loop:			-1,
					flags:		1,
					play:
				)
				(ego
					init:,
					x:		165,
					y:		100,
				)
			)
		)
		(if
			(not (Btst fReadyToDance))
			(ReviveActors)
		)
		(louZerr init:)
		(man2 init:)
		(man3 init:)
		(man4 init:)
		(rogerMan init:)
		(coral1 init:)
		(coral2 init:)
		(coral3 init:)
		(coral5 init:)
		(boat init:)
		(lowe 
			init:	,
			approachVerbs: actorVerbs verbLook
		)
		(skirvin init:)
		(aquarium init:)
		(chair
			init:,
			approachVerbs:
			objectVerbs
		)
		(if
			(not (Btst fFawnIsGone))
			(aFawn
				cycleSpeed:	howFast,
				moveSpeed:	howFast,
				init:			,
				stopUpd:		,
				approachVerbs:
				objectVerbs
			)
		)
		(super init:)
	)
	(method (doit)
		(super doit: &rest)
		(if
			(and
				(ego mover?)
				(== (ego view?) vDiscoInterior)
				(== (ego loop?) lLarrySitting)
			)
			(= clickX 
				((User curEvent?) x?)
			)
			(= clickY
				((User curEvent) y?)
			)
			(HandsOff)
			(curRoom setScript: sStandUp)
		)
		(cond
			(	(and
					(!= (curRoom script?) sDance)
					(== sharkTimer	0)
					(!= (aShark script?) sSharkChase)
				)
				(aShark setScript: sSharkChase)
			)
			(	(and
					(!= (curRoom script?) sDance)
					(!= sharkTimer 0)
					(!= (aShark script?) sSharkChase)
				)
				(-- sharkTimer)
			)
			(	(== sharkTimer 0)
				(= sharkTimer (Random 200 500))
			)
		)
		(if
			(and
				(!= (curRoom script?) sDance)
				(== fish1Timer 0)
				(!= (aFish1 script?) sFish1)
			)
			(= fish1Timer (Random 300 500))
			(aFish1 setScript: sFish1)
		else
			(-- fish1Timer)
		)
		(if
			(and
				(!= (curRoom script?) sDance)
				(== fish2Timer 0)
				(!= (aFish2 script?) sFish2)
			)
			(= fish2Timer (Random 300 500))
			(aFish2 setScript: sFish2)
		else
			(-- fish2Timer)
		)
		(if
			(and
				(!= (curRoom script?) sDance)
				(== fish3Timer 0)
				(!= (aFish3 script?) sFish3)
			)
			(= fish3Timer (Random 300 500))
			(aFish3 setScript: sFish3)
		else
			(-- fish3Timer)
		)
	)
	(method (dispose)
		(super dispose:)
	)
)
(instance sSitDown of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(HandsOff)
				(ego
					setMotion:	PolyPath 220 75 self
				)
			)
			(
				(ego
					view:			vDiscoInterior,
					setLoop:		lLarrySitting,
					setCel:		0,
				)
				(= cycles 12)
			)
			(
				(if (and (== (aFawn view?) vFawnWalking)
							(== (aFawn loop?) lFawnSitting)
							(not (Btst pAskedToSit)))
					(Points pAskedToSit ptsAskedToSit)
					(Print "You smile at the girl and say, \"May I join you?\"")
					(Print "And, of course she replies,
						\"Why, am I coming apart?\"")
					(drumFillSFX play:)
					(Print "(Drum fill)" LOW)
				)
				(= cycles 11)
			)
			(
				(ego
					view:			vDiscoInterior,
					setLoop:		lLarrySitting,
					setCycle:	EndLoop self
				)
			)
			(
				(HandsOn)
				(self dispose:)
			)
		)
	)
)
(instance sStandUp of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					setCycle:	BegLoop self
				)
			)
			(
				(NormalEgo 2)
				(ego
					setMotion:	MoveTo (ego x?) (+ (ego y?) 5) self
				)
			)
			(
				(HandsOn)
				(if (and (IsObject (CueObj	client?))
							(!= (CueObj client?) chair)
;							(!= (CueObj client?) aFawn)
							((CueObj client?) approachX?))
					(ego setMotion: PolyPath
					((CueObj client?) approachX?)
					((CueObj client?) approachY?)
					CueObj)
				else
					(ego setMotion: PolyPath clickX clickY)
				)
				(self dispose:)
			)
		)
	)
)
(instance sFawnIsHistory of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(HandsOff)
				(aFawn startUpd:)
				(= cycles 1)
			)
			(
				(aFawn
					setCycle:	BegLoop self
				)
			)
			(
				(aFawn
					view:			vFawnWalking,
					loop:		 	2,
					cel:			1,
					x:				174,
					y:				79,
					setCycle:	Walk,
					setMotion:	PolyPath 166 249 self
				)
			)
			(
				(Bset fFawnIsGone)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)


(enum
	CHGSPEED				;0
	STARTDANCE			;1	 *10
	LARRYWALKS			;2
	RELMUSIC1			;3	 *20
	FAWNWALKS			;4
	LFSTRIKEPOSE		;5
	RELMUSIC2			;6	 *30
	DIPFAWNRIGHT		;7
	TOSSFAWN				;8
	DISCOLARRY			;9	 *40
	COSSAKRIGHT			;10
	HEY1					;11
	COSSAKLEFT			;12
	HEY2					;13
	COSSAK				;14
	HEY3					;15
	TWOSTEP2				;16 *50
	FAWNFALLS			;17
	CATCHFAWN			;18
	SWINGFAWN			;19
	DANCETOGETHER		;20 *60
	TWOSTEP				;21 *70
	LARRYSPINS			;22
	SPINS1				;23
	SPINS2				;24
	SPINS3				;25
	SPINS4				;26 *80
	FINALE				;27
	LOADPAL				;28
	FAWNSITS				;29
)
(instance sDance of Script
	(method (doit)
		(super doit:)
		(= cueNum (theMusic2 prevSignal?))
		(cond
			((and
					(>= state SPINS4)
					(>= (theMusic2 prevSignal?) 80)
				)
				(self cue:)
			)
			((and
					(== state TWOSTEP)
					(>= (theMusic2 prevSignal?) 70)
				)
				(self cue:)
			)
			((and
					(== state DANCETOGETHER)
					(>= (theMusic2 prevSignal?) 60)
				)
				(self cue:)
			)
			((and
					(== state TWOSTEP2)
					(>= (theMusic2 prevSignal?) 50)
				)
				(self cue:)
			)
			((and
					(== state DISCOLARRY)
					(>= (theMusic2 prevSignal?) 40)
				)
				(self cue:)
			)
			((and
					(== state RELMUSIC2)
					(>= (theMusic2 prevSignal?) 30)
				)
				(self cue:)
			)
			((and
					(== state RELMUSIC1)
					(>= (theMusic2 prevSignal?) 20)
				)
				(self cue:)
			)
			((and
					(== state STARTDANCE)
					(>= (theMusic2 prevSignal?) 10)
				)
				(self cue:)
			)
		)
		(cond
			((and
				(Btst isVga)	;for 256 color play
				(>= state STARTDANCE)
				(<= state FINALE))
			  	(Palette PALCycle 64 79 1)
			)
			((and	 ;for 16 color play
				(not (Btst isVga))
				(>= state STARTDANCE)
				(<= state FINALE))
				(floorLight1
					x:		(Random 110 223),
					y:		(Random 90 135),
				)
				(floorLight2
					x:		(Random 110 223),
					y:		(Random 90 135),
				)
			)
		)
	)
	(method (changeState newState)
		(switch (= state newState)
			(CHGSPEED
				(if  ;for 16 color play
					(not (Btst isVga))
					(floorLight1
						setCycle:	Forward
					)
					(floorLight2
						setCycle:	Forward
					)
				)
				(theMusic3
					fade:		0 12 20 1 self
				)
			)
			(STARTDANCE
				(DumpActors)
				(theMusic2
					number:	mDanceNumber,
					vol:		127,
					loop:		1,
					flags:	0,
					play:		,
					hold:		10,
				)
				(if
					(and
						(== (ego view?) vDiscoInterior)
						(== (ego loop?) lLarrySitting)
					)
					(ego
						startUpd:	,
						setCycle:	BegLoop
					)
				)
				(aFawn
					startUpd:	,
					setCycle:	BegLoop
				)
			)
			(LARRYWALKS
				(NormalEgo 2)
				(ego
					egoSpeed:	larrySpeed,
					setMotion:	MoveTo 151 108	self
				)
			)
			(RELMUSIC1
				(theMusic2 release:)
				(ego setHeading: 180)
			)
			(FAWNWALKS
				(theMusic2 hold: 20)
				(aFawn
					cycleSpeed:	howFast,
					moveSpeed:	howFast,
					view:			vFawnWalking,
					loop:		 	2,
					cel:			1,
					x:				164,
					y:				79,
					setCycle:	Walk,
					setMotion:	PolyPath 183 108 self
				)
			)
			(LFSTRIKEPOSE
				(aFawn x: (+ (aFawn x?) 1000))
				(ego
					normal:		FALSE,
					x:				141,
					y:				108,
					view:			vFawnDancing,
					setLoop:		lSpinFawnLeft,
					setCycle:	EndLoop self
				)
			)
			(RELMUSIC2
				(theMusic2 release:)
			)
			(DIPFAWNRIGHT
				(ego
					setLoop:		lDipFawnRight,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(TOSSFAWN
				(ego
					setLoop:		lTossFawn,
					setCel:		0,
					setCycle:	CycleTo 1 1 self
				)
			)
			(DISCOLARRY
				(ego
					setCycle:	EndLoop self
				)
				(aFawn
					view:				vFawnDancing,
					setLoop:			lFawnFlies,
					setCel:			0,
					x:					137,
					y:					65,
					yStep:		   20,
					setPri:			4,
					setCycle:		Forward,
					setMotion:		MoveTo 139 -40
				)
				(ego
					view:			vLarryDancing,
					setLoop:		lDiscoLarry,
					setCel:		0,
					setCycle:	Forward
				)
			)
			(COSSAKRIGHT
				(ego
					setLoop:		lRussianRight,
					setCel:		0,
					setCycle:	Forward,
					setMotion:	MoveTo 180 108	self
				)
			)
			(HEY1
				(if
					(== (aFawn y?) -40)
					(aFawn stopUpd:)
				)
				(ego
					setLoop:		l1FootHeyRight,
					setCel:		0,
				)
				(= cycles 15)
			)
			(COSSAKLEFT
				(ego
					setLoop:		lRussianLeft,
					setCel:		0,
					setCycle:	Forward,
					setMotion:	MoveTo 120 108 self
				)
			)
			(HEY2
				(ego
					setLoop:		l1FootHeyLeft,
					setCel:		0,
				)
				(= cycles 15)
			)
			(COSSAK
				(ego
					setLoop:		lRussianRight,
					setCel:		0,
					setCycle:	Forward,
					setMotion:	MoveTo  154 108 self
				)
			)
			(HEY3
				(ego
					setLoop:		l2FootHeyRight
				)
				(= cycles 5)
			)
			(TWOSTEP2
				(ego
					setLoop:		lLarryDanceRight,
					setCycle:	Forward
				)
			)
			(FAWNFALLS
				(aFawn
					startUpd:	,
					x:				135,
					setCycle:	Forward,
					setMotion:	MoveTo 135 61 self
				)
			)
			(CATCHFAWN
				(aFawn x: (+ (aFawn x?) 1000))
				(ego
					view:			vFawnDancing,
					setLoop:		lTossFawn,
					x:				139,
					y:				108,
					setCel:		2,
					cycleSpeed:	1,
					setCycle:	BegLoop self
				)
			)
			(SWINGFAWN
				(ego
					view:			vFawnDancing,
					setLoop:		lDipFawnRight,
					setCel:		2,
					setCycle:	BegLoop self
				)
			)
			(DANCETOGETHER
				(ego
					egoSpeed:	larrySpeed,
					view:			vLarryDancing,
					setLoop:		lLarryDanceLeft,
					x:				153,
					y:				108,
					setCycle:	Forward,
				)
				(aFawn
					view:			vFawnDancing,
					setLoop:		lFawnDanceRight,
					x:				122,
					y:				108,
					setCycle:	Forward
				)
			)
			(TWOSTEP
				(ego
					egoSpeed:	(* 2 larrySpeed),
					setLoop:		lTwoStepLarry,
					setCycle:	Forward
				)
				(aFawn
					view:			vFawnWalking,
					setLoop:		0,
					setCel:		2,
					stopUpd:		,
				)
			)
			(LARRYSPINS
				(ego
					view:			vLarryDancing,
					setLoop:		lLarrySpinning,
					cycleSpeed:	4,
					setCycle:	Forward,
				)
				(= seconds 1)
			)
			(SPINS1
				(aFawn
					startUpd:	,
					view:			vFawnWalking,
					setLoop:		-1,
					setCycle:	Walk,
					setMotion:	PolyPath 174 79
				)
				(ego cycleSpeed: 3)
				(= seconds 1)
			)
			(SPINS2
				(ego cycleSpeed: 2)
				(= seconds 1)
			)
			(SPINS3
				(ego cycleSpeed: 1)
				(= seconds 1)
			)
			(SPINS4
				(ego cycleSpeed: 0)
			)
			(FINALE
				(ego
					setLoop:		lDiscoLarry,
					setCel:		4
				)
				(theMusic2 prevSignal: 0)
				(= cycles 2)
			)
			(LOADPAL
				(if
					(Btst isVga)
					(Palette PALLoad 610 PAL_REPLACE)
				else
					(floorLight1 dispose:)
					(floorLight2 dispose:)
				)
				(sfxCheer play:)
				(= seconds 10)
			)
			(FAWNSITS
				(aFawn
					startUpd:	,
					view:			vFawnWalking,
					setLoop:		lFawnSitting,
					setCel:		0,
					setPri:		3,
					setCycle:	EndLoop
				)
				(NormalEgo 2)
				(theMusic3 play:)
				(ReviveActors)
				(Bset fDancedWithFawn)
				(Bclr fReadyToDance)
				(self dispose:)
				(HandsOn)
			)
		)
	)
)
(instance sSharkChase of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(aTuna	  		;slowly swims in from right
					init:,
					x:				334,
					y:				43,
					setLoop:		lTunaLeft,
					cycleSpeed:	howFast,
					moveSpeed:	howFast,
					setCycle:	Forward,
					setMotion:	MoveTo 138 43 self
 				)
				(= cycles 30)
			)
			(
				(aShark			;shark peeks out from behind seaweed
					init:,
					x:				40,
					y:				25,
					setPri:		0,
					cycleSpeed:	howFast,
					moveSpeed:	howFast,
					setLoop:		lSharkRight,
					setCycle:	Forward,
					setMotion:	MoveTo 54 25
				)
			)
			(
				(aShark			;shark chases tuna
					xStep:		25,
					setMotion:	MoveTo 350 43 self
				)
				(aTuna			;tuna swims as fast as it can
					setLoop:		lTunaRight,
					xStep:		25,
					setMotion:	MoveTo 360 43
				)
			)
			(
				(= cycles 60)
			)
			(
				(aTuna	 		;tuna comes in from left after circling the aquarium
					setLoop:		lTunaRight,
					x:				-11,
					y:				43,
					setPri:		1,
					setMotion:	MoveTo 85 32 self
				)
			)
			(						;tuna hides behind seaweed
				(aTuna
					setLoop:		lTunaLeft,
					setPri:		0,
					setMotion:	MoveTo 40 32 self
				)
			)
			(
				(= cycles 5)
			)
			(						;shark slowly searches for tuna
				(aShark
					setLoop:		lSharkRight,
					x:				-17,
					y:				43,
					xStep:		5,
					setPri:		1,
					setMotion:	MoveTo 139 24 self
				)
			)
			(
				(aShark
					setCycle:	Forward
				)
				(= cycles 15)
			)
			(
				(aShark
					setLoop:		lSharkLeft,
					setCycle:	Forward,
				)
				(= cycles 30)
			)
			(
				(aShark
					setLoop:		lSharkRight,
					setCycle:	Forward,
					setMotion:	MoveTo 350 43 self
				)
			)
			(
				(aTuna
					setLoop:		lTunaRight,
					setPri:		0,
					xStep:		3,
					setMotion:	MoveTo 85 32 self
				)
			)
			(
				(aTuna
					setLoop:		lTunaTongue,
					setCycle:	Forward,
				)
				(= cycles 20)
			)
			(						;tuna slowly (smugly) swims away after shark is gone
				(aTuna
					setLoop:		lTunaLeft,
					setPri:		1,
					setMotion:	MoveTo -11 43 self
				)
			)
			(
				(aShark dispose:)
				(aTuna dispose:)
				(self dispose:)
			)
		)
	)
)
(instance sFish1 of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(if
					(>= (aFish1 x?) 100)
					(aFish1
						init:,
						setLoop:		lFish1Left,
						setCycle:	Forward,
						setMotion:	MoveTo -30 (aFish1 y?) self
					)
				else
					(aFish1
						setLoop:		lFish1Right,
						setCycle:	Forward,
						setMotion:	MoveTo 340 (aFish1 y?) self
					)
				)
			)
			(
				(= cycles (Random 200 700))
			)
			(
				(aFish1 dispose:)
				(self init:)
			)
		)
	)
)
(instance sFish2 of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(if
					(>= (aFish2 x?) 100)
					(aFish2
						init:,
						setLoop:		lFish2Left,
						setCycle:	Forward,
						setMotion:	MoveTo -30 (aFish2 y?) self
					)
				else
					(aFish2
						setLoop:		lFish2Right,
						setCycle:	Forward,
						setMotion:	MoveTo 340 (aFish2 y?) self
					)
				)
			)
			(
				(= cycles (Random 200 700))
			)
			(
				(aFish2 dispose:)
				(self init:)
			)
		)
	)
)
(instance sFish3 of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(if
					(>= (aFish3 x?) 100)
					(aFish3
						init:,
						setLoop:		lFish3Left,
						setCycle:	Forward,
						setMotion:	MoveTo -30 (aFish3 y?) self
					)
				else
					(aFish3
						setLoop:		lFish3Right,
						setCycle:	Forward,
						setMotion:	MoveTo 340 (aFish3 y?) self
					)
				)
			)
			(
				(= cycles (Random 200 700))
			)
			(
				(aFish3 dispose:)
				(self init:)
			)
		)
	)
)

;*************
;*** SOUND ***
;*************

(instance sfxCheer of Sound
	(properties
		number	sCorrect
		vol		127
		loop		1
		flags		1
	)
)

;****************
;***  ACTORS  ***
;****************

(instance aTuna of Actor
	(properties
		view		vDiscoInterior
		loop		lTunaLeft
		cel		0
		x			334
		y			43
		description "the fish"
		lookStr 		"What a great idea! Live fish in an aquarium."
		signal	ignrAct
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance aShark of Actor
	(properties
		view		vDiscoInterior
		loop		lSharkRight
		cel		0
		x			40
		y			25
		signal	ignrAct
		description "Jaws"
		lookStr 		"It's just a small shark!"
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance aFawn of Person
	(properties
		view		vFawnWalking
		loop		lFawnSitting
		cel		4
		x			168
		y			77
		priority 3
		signal	(| ignrHrz ignrAct)
		approachX 199	;193
		approachY 75	;77
		description "the girl"
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbUse
				(Print "She might listen to you,
					if you can establish eye contact.")
			)
			(verbTalk
				(if (and (== (ego view?) vDiscoInterior)
							(== (ego loop?) lLarrySitting))
					(Print "She might listen to you,
						if you can establish eye contact."
					)
				else
					(Print "Perhaps you should sit with her first.")
				)
			)
			(verbDo
				(cond
					((Btst fDancedWithFawn)
						(Print "\"Not again. That trip through the ceiling
							gave me a headache!\"")
					)
					((Btst fLookedAtFawn)
						(Print "You screw up your courage and ask,
							\"What would you say to a spin around the dance
							floor with the nearest thing to John Travolta, baby?\"")
						(Print "\"Well, okay, I guess,\" she replies,
							thinking to herself, \"near as I'm gonna get!\"")
						(Points pDanceWithFawn ptsDanceWithFawn)
						(HandsOff)
						(curRoom setScript: sDance)
					)
					(else
						(Print "\"Hey, Baby, what's a beautiful doll like you
							doing sitting all alone?\" you say to the 
							beautiful doll sitting all alone.
							\"Wanna dance?\"")
						(Print "\"Dance? With you?\" she replies, 
							\"Why I don't even know you.\"")
					)
				)
			)
			(verbZipper
				(Print "Once again, you're a little premature, Larry!")
			)
			(verbSmellTaste
				(Print "The girl smells of expensive perfume and has even
					more expensive tastes.")
			)
			(verbLook
				(cond
					((and (not (Btst fFawnIsGone))
							(== (ego view?) vDiscoInterior)
							(== (ego loop?) lLarrySitting))
						(curRoom newRoom:	615)
					)
					((and (not (Btst fReadyToMarry))
							(Btst fFawnIsGone))
						(Print "Gosh, Larry.
							Maybe you should have loaned her some money!")
					)
					((not (Btst fFawnIsGone))
						(Print "There's a gorgeous blonde sitting at the table,
							and she's all alone!")
						(Print "Perhaps you should sit with her.")
					)
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)
(instance aFish1 of Actor
	(properties
		view			vDiscoInterior
		loop			lFish1Right
		cel			0
		x				-30
		y				41
		xStep			2
		priority		1
		signal		(| ignrAct fixPriOn)
		description "the fish"
		lookStr 		"Look at those fish go!"
		detailLevel	1
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Stop tapping on the glass!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance aFish2 of Actor
	(properties
		view			vDiscoInterior
		loop			lFish2Left
		cel			0
		x				340
		y				30
		xStep			2
		priority		1
		signal		(| ignrAct fixPriOn)
		description "the fish"
		lookStr 		"Look at those fish go!"
		detailLevel	1
	)
 	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Stop tapping on the glass!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance aFish3 of Actor
	(properties
		view			vDiscoInterior
		loop			lFish3Right
		cel			0
		x				-25
		y				41
		xStep			2
		priority		1
		signal		(| ignrAct fixPriOn)
		description "the fish"
		lookStr 		"Look at those fish go!"
		detailLevel	1
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Stop tapping on the glass!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance floorLight1 of Actor
	(properties
		view	vDiscoInterior
		loop	lFloorLights
		cel	0
		x		155
		y		112
		priority 2
		signal (| ignrAct fixPriOn)
	)
)
(instance floorLight2 of Actor
	(properties
		view	vDiscoInterior
		loop	lFloorLights
		cel	0
		x 		155
		y		112
		priority 2
		signal (| ignrAct fixPriOn)
	)
)

;***************
;***  PROPS	 ***
;***************



(instance alEyes of Prop
	(properties
		view		vDiscoExtras
		loop		lAlEyes
		cel		0
		x			13
		y			64
		detailLevel	1
		cycleSpeed	30
	)
	(method (doVerb theVerb invItem)
		(lowe doVerb: theVerb	invItem)
	)
)
(instance rightGuyEyes of Prop
	(properties
		view 		vDiscoExtras
		loop		lRightGuyEyes
		cel		0
		x			289
		y			61
		priority 5
		signal 	fixPriOn
		detailLevel 1
		cycleSpeed	30
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance rogerHead of Prop
	(properties
		view 		vDiscoExtras
		loop		lRogerHead
		cel		0
		x			9
		y			136
		detailLevel 1
		cycleSpeed	50
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)


;******************
;***	FEATURES	 ***
;******************

(instance chair of Feature
	(properties
		x 235
		y 64
		z 0
		heading 0
		nsLeft 211
		nsTop 54
		nsBottom 75
		nsRight 259
 		description "the chair"
		lookStr 		"It looks like the best seat in the house."
		approachX 220
		approachY 73
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(curRoom setScript: sSitDown)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance boat of Feature
	(properties
		x 194
		y 36
		z 0
		heading 0
		nsLeft 167
		nsTop 29
		nsBottom 43
		nsRight 221
 		description "the sunken boat"
		lookStr 		"\"...on a three-hour tour!\""
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance lowe of Feature
	(properties
		x 18
		y 73
		z 0
		heading 0
		nsLeft 0
		nsTop 55
		nsBottom 92
		nsRight 36
		approachX 46
		approachY 97
 		description "Al Lowe"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "My, that Al Lowe is a stunningly handsome fellow!")
				(Print "(The preceding was a paid editorial message.)" LOW)
			)
			(verbDo
				(Print "Trust me; don't touch me!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance skirvin of Feature
	(properties
		x 77
		y 60
		z 0
		heading 0
		nsLeft 65
		nsTop 41
		nsBottom 79
		nsRight 90
 		description "Bill Skirvin"
		lookStr 		"You wonder if Patti knows Bill is hanging around with
						Al Lowe again!"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "\"Hey! Keep your hands off me!\" shouts Skirvin.")
			)
			(verbTalk
				(Print "\"Wanna go somewhere after this, Bill?\"
					you ask William Skirvin, creator of all the
					wonderful graphics in this game.")
				(Print "\"Zzzzzzzzzz,\" replies Bill from behind his shades.")
				(Print "(Bill is the master of the sitting nap!)" LOW)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance aquarium of Feature
	(properties
		x 159
		y 21
		z 0
		heading 0
		nsLeft 0
		nsTop 0
		nsBottom 43
		nsRight 319
 		description "the aquarium"
		lookStr 		"A few fish swim around in the gigantic aquarium."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Don't tap on the glass!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance louZerr of Feature
	(properties
		x 237
		y 167
		z 36
		heading 0
		nsLeft 225
		nsTop 114
		nsBottom 148
		nsRight 250
 		description "Kevin Ray"
		lookStr 		"Kevin is that handsome gent near the divider."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "\"HEY! Stop doing that!\"")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance man2 of Feature
	(properties
		x 300
		y 167
		z 28
		heading 0
		nsLeft 288
		nsTop 122
		nsBottom 157
		nsRight 313
 		description "Barry Smith"
		lookStr 		"Barry Smith thought he could hide out here,
						instead of drawing for a living!"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Barry's clothes are covered with paint.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance man3 of Feature
	(properties
		x 11
		y 137
		z 0
		heading 0
		nsLeft 0
		nsTop 116
		nsBottom 159
		nsRight 23
 		description "Mike"
		lookStr 		"Mike feels good like a programmer should."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Mike feels good, like a programmer should.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance man4 of Feature
	(properties
		x 84
		y 140
		z 0
		heading 0
		nsLeft 73
		nsTop 123
		nsBottom 157
		nsRight 95
 		description "Oliver Brelsford"
		lookStr 		"Oliver Brelsford's neck must be getting stiff by now,
						staring across the room."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Oliver enjoys being touched like that!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance rogerMan of Feature
	(properties
		x 282
		y 76
		z 0
		heading 0
		nsLeft 267
		nsTop 53
		nsBottom 99
		nsRight 298
 		description "Roger Hardy"
		lookStr 		"Have you ever seen Roger in a chair high enough so his
						legs weren't scrambled?"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Roger FEELS life-like!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance coral1 of Feature
	(properties
		x 225
		y 160
		z 0
		heading 0
		nsLeft 131
		nsTop 142
		nsBottom 178
		nsRight 319
 		description "the coral room divider"
		lookStr 		"It's not often you see coral used for a room divider."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Ouch! That coral is sharp!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance coral2 of Feature
	(properties
		x 11
		y 99
		z 0
		heading 0
		nsLeft 0
		nsTop 86
		nsBottom 112
		nsRight 22
 		description "the coral"
		lookStr 		"It's not often you see coral used for a room divider."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Ouch! That coral is sharp!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance coral3 of Feature
	(properties
		x 113
		y 56
		z 0
		heading 0
		nsLeft 94
		nsTop 42
		nsBottom 71
		nsRight 132
 		description "the coral"
		lookStr 		"It's not often you see coral used for a room divider."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Ouch! That coral is sharp!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance coral5 of Feature
	(properties
		x 306
		y 118
		z 0
		heading 0
		nsLeft 294
		nsTop 102
		nsBottom 135
		nsRight 319
 		description "the coral"
		lookStr 		"It's not often you see coral used for a room divider."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Ouch! That coral is sharp!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(procedure (ReviveActors)
	(aTuna	
		init:			,
		cycleSpeed:	howFast,
		moveSpeed:	howFast,
 	)
	(aShark
		init:			,
		cycleSpeed:	howFast,
		moveSpeed:	howFast,
	)
	(aFish1
		init:			,
		cycleSpeed:	howFast,
		moveSpeed:	howFast,
	)
	(aFish2
		init:			,
		cycleSpeed:	howFast,
		moveSpeed:	howFast,
	)
	(aFish3
		init:			,
		cycleSpeed:	howFast,
		moveSpeed:	howFast,
	)
	(alEyes
		init:			,
		cycleSpeed:	(+ 40 howFast),
		setCycle:	RandCycle
	)
	(rightGuyEyes
		init:			,
		cycleSpeed:	(+ 40 howFast),
		setCycle:	RandCycle
	)
	(rogerHead
		init:			,
		cycleSpeed:	(+ 90 howFast),
		setCycle:	RandCycle
	)
	(aShark
		init:			,
		cycleSpeed:	howFast,
		moveSpeed:	howFast,
	)
	(= sharkTimer 300)
	(= fish1Timer 500)
	(= fish2Timer 200)
	(= fish3Timer 100)
)
(procedure (DumpActors)
	(sSharkChase dispose:)
	(sFish1 dispose:)
	(sFish2 dispose:)
	(sFish3 dispose:)
	(aFish1
		dispose:,
		delete:
	)
	(aFish2
		dispose:,
		delete:
	)
	(aFish3
		dispose:,
		delete:
	)
	(aShark
		dispose:,
		delete:
	)
	(aTuna
		dispose:,
		delete:
	)
	(alEyes
		dispose:,
		delete:
	)
	(rightGuyEyes
		dispose:,
		delete:
	)
	(rogerHead
		dispose:,
		delete:
	)
)
(instance drumFillSFX of Sound
	(properties
		number		sDrumFill1
		loop			1
		flags			1
	)
)

