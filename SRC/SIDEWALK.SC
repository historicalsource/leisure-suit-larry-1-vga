;**
;**	Logics for SIDEWALK -- 100, 300, 400, 500, 600
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford &
;**
;**	Last Update:  June 16, 1991
;**
;**	contains:
;**
;**	dog walking and pissing,
;**	ego run over by traffic,
;**	taxi picking up and dropping off Larry,
;**	cabbie beating up and running over Larry
;**


(script# SIDEWALK)

(public
	sidewalk		0
	dog 			1
	taxi			2
	sTaxiScript 3
)

(local
	flatY
	taxiPts = [0 0 0 0 0 0 0 0]
	dogPts  = [0 0 0 0 0 0 0 0]
	dogTimer
	taxiY
	carView
	beenPissedOn
)

(enum
	DROPLEG
	TURN
	WALKOFF
	TRYAGAIN
)

(procedure
	PolyTaxi
	PolyDog
)

(define ABOVETAXI 			8)				;add this value to (taxi brTop?) to get top edge of taxiPoly
(define waitX					150) 			;where taxi goes to to wait for Larry
(define CYCLES_BEFORE_DOG	1000)			;# of cycles in same place
(define dogY					155)			;currently dog's starting y
(define flattenedX			0)				;\
(define flattenedY			0)				;-reposition flattened ego to look good

;(define	sDogPissing				102)		;**	reused in 130
(define	mDog						103)
(define	sYellTaxi				200)		;**	200 	Taxi
;(define	sCabComing				201)
(define	sCabTires				202)
(define	sCabDoorOpen			203)
(define	sCabDoorClose			204)
(define	sCabDeparting			205)
;(define	mCab						206)
;(define	sFight					171)
;(define sCitySounds				800)
;(define	sCarHonk					810)
;(define	sCarThump				811)
;(define	sCarLeaving				812)

;(define	vLarryFlattened		806)	;queried in 500 to fix bug in street
	(define lFlattened			0)
		(define cLookout			0)
		(define cSmash				1)
	(define lFlat					1)
		(define cFlat				1)
	(define lTire					2)

;(define  vLarryHailing			810)
	(define lHailing				0)
(define  vLarryShakingLeg		811)
(define	vDogWalk					820)
(define  vDogPiss					821)
	(define lTurn					0)
		(define cTurnLast			2)
	(define lLift					2)
		(define cLiftLast			2)
	(define lPiss					4)
(define 	vGenericCar1			822)
(define 	vGenericCar2			823)
(define 	vGenericCar3			824)
(define 	vGenericCar4			825)
(define 	vGenericCar5			826)
(define 	vGenericCar6			827)
(define  vTaxi						200)
	(define lTaxi					0)
;(define	vLarryMugged			171)
;	(define lLarryMugged 		0)
;	(define lLarryEscape 		1)
;	(define lLarryCroaks			2)
;	(define lTaxiBully			3)

(instance sidewalk of Region
	(method (init)
		(= carView (Random 822 827))
		(LoadMany VIEW
			carView
			vLarryFlattened
			vLarryHailing
			vLarryShakingLeg
			vDogWalk
			vDogPiss
			vTaxi
		)
		(LoadMany SOUND
			sCitySounds
;			sNeonBuzz
			sCarHonk
			sCarThump
			sCarLeaving
			mDog
			sDogPissing
			sYellTaxi		;**	200 	Taxi
			sCabTires
			sCabDoorOpen
			sCabDoorClose
			sCabDeparting
			mCab
		)
		(if
			(and
				(or
					(!= (theMusic2 number?) sCitySounds)
					(== (theMusic2 prevSignal?) -1)
				)
				(not (Btst fStiffedCabbie))
			)
			(theMusic2
				number:		sCitySounds,
				loop:			-1,
				vol:			127,
				play:
			)
		)
		(super init:)
		(streetF init:)
;		(ego actions: taxiActions)
;		(eHead actions: taxiActions)
		(= flatY
			(switch curRoomNum
				(100 169)
				(300 165)
				(400 159) 	;was 158
				(500 170)
				(600 167)
			)
		)
		(= taxiY flatY) 						;may change
		(if
			(<= 710 prevRoomNum 720)
			(theMusic fade: 0 30 1 1)
		)
		(= dogTimer CYCLES_BEFORE_DOG)
		(if (== prevRoomNum 200)
			(HandsOff)
			(if (Btst fStiffedCabbie)
				(Load VIEW
					vLarryMugged
				)
			)
			(curRoom setScript: sDropoff)
		)
		(if
			(> (LarryHours) 7)	;it's now after six o'clock in the morning
			(self setScript: virginScript)
		)
		(if
			(!= curRoomNum 100)
			(taxiSignProp
				x:					(switch curRoomNum (300 74) (400 294) (500 200) (600 246)),
				y:					(switch curRoomNum (300 178) (400 181) (500 175) (600 172)),
				approachX:		(ego x?),
				approachY:		(- flatY 10),
				approachVerbs:	verbDo verbTalk,
				init:				,
				setPri:			14,
				stopUpd:
			)
		)
	)
	(method (newRoom n)
		(= keep
			(OneOf n SIDEWALKROOMS)
		)
		(= initialized FALSE)
		(if (and (not (OneOf n SIDEWALKROOMS))
					(!= n 160))
			(theMusic2
				fade:,
			)
		)
		(if (and (!= n 200)
					(== (theMusic3 number?) sCabDeparting))
			(theMusic3 stop:)
		)
		(if
			(and
				(== n 160)
				(== (theMusic number?) mThemeSong)
			)
			(theMusic fade:)
		)
		(Cls)
		(super newRoom: n &rest)
	)
	(method (doit)
		(super doit: &rest)
		;so ego will walk straight down screen to curb
		(taxiSignProp approachX: (ego x?))
		(if
			(and
				dogTimer
				(not (dog script?))
			)
			(-- dogTimer)
		)
		(if (ego mover?)
			(= dogTimer CYCLES_BEFORE_DOG)
		)
		(cond
			((== (curRoom script?) sFlattenLarry)
				(ego setMotion: 0)
			)
			((> (ego y?) flatY)
				(if (not (cast contains: taxi))
					(HandsOff)
					(curRoom setScript: sFlattenLarry)
				else
					(if (> (ego y?) (+ (taxi y?) 5))
						(HandsOff)
						(curRoom setScript: sFlattenLarry)
					)
				)
			)
			((and (not dogTimer)
					(not beenPissedOn)
				 	(!= curRoomNum 500)
					(!= (curRoom curPic?) 245))	;**	He's on the phone.
				(= dogTimer -1)
				(dog
					init:			,
					cycleSpeed:	(ego cycleSpeed?),
					moveSpeed:	(ego moveSpeed?),
					setCycle:	Walk,
					z:				0,
					setScript:	sFindLarry
				)
			)
		)
	)
);END sidewalk region instance

;(class taxiActions of Code
;	(methods
;		doVerb
;	)
;	(method (doVerb theVerb invItem)
;		(return
;			(switch theVerb
;				(verbTalk
;					(HandsOff)
;					(curRoom setScript: sTaxiScript)
;					(return TRUE)
;				)
;			)
;		)
;	)
;)

(instance virginScript of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= seconds 5)
			)
			(
				(if (curRoom script?)
					(= cycles 60)				;**	try again soon
					(-- state)
				else
					(HandsOff)
					(ego setHeading: 180 self)
				)
			)
			(
				(= cycles 3)
			)
			(
				(LoadMany FONT giantFont)
				(Print "\n\nOh, no! What's that? In the east! It's... it's...
					it's the sun!\n\n\n"
					WIDE
				)
				(Print "\nGlancing at your watch, you realize the terrible truth.
					Your night in Lost Wages is over,
 					and (technically speaking) you're still...\n\n"
					WIDE
				)
				(Print "\n\n\na\n\n\n\n"
					WIDE
				)
				(Print "\n\nVIRGIN!!!\n\n\n"
					WIDE
					#font		giantFont
				)
				(= seconds 3)
			)
			(
				(ego
					egoSpeed:,
					normal:		FALSE,
					view:			vLarryShakingLeg,
					setLoop:		(if register 0 else 1),
					setCel:		8,
					setCycle: 	EndLoop self
				)
			)
			(
				(= seconds 3)
			)
			(
				(Death vLarryKillingHimself lLarryMatchHead 		)
				(Format @deathTitleStr "Larry-cakes anyone?")
				(GameOver "Life is no longer worth living!")
			)
 		)
	)
)
(instance sDropoff of Script
	(method (doit)
		(super doit: &rest)
		(if (or (== state 1)
					(== state 5))
			(taxi x: (- waitX register))
			(theMusic3 send: 9 mPITCHBEND (+ (* (- waitX (taxi x?)) 10) 3000))
			(if (< (taxi x?) -800)
				(= cycles 1)
			)
			(= register (* register 2))
			(if (== register 4)
				(soundFX
					number:		sCabTires,
					setLoop:		1,
					flags:		1,
					play:
				)
			)
		)
	)
	(method (changeState newState)
		(switchto (= state newState)
			(
				(taxi
					init:				,
					setCel:			1,
					cycleSpeed:		howFast,
					moveSpeed:		howFast,
					x:					waitX,
					y:					taxiY,
				)
				(ego
					x:			waitX,
					y:			(- flatY 10),
					z:			0,
				)
				(if (Btst fStiffedCabbie)
					(ego
						egoSpeed:,
						view:			vLarryMugged,
						loop:			lLarryMugged,
						setCycle:	Forward,
					)
					(++ state)
					(= cycles 1)
				else
					(ego
						setHeading:	180 self
					)
				)
			)
			(;1 is current state
				(= register 1)
			)
			(
				(if (Btst fStiffedCabbie)
					(theMusic
						number:		sFight,
						loop:			-1,
						vol:			127,
						flags:		1,
						play:,
					)
					(ego
						setCycle:	Forward
					)
					(= seconds 5)
				else
					(taxi
						dispose:
					)
					(theMusic3 fade:)
					(HandsOn)
					(self dispose:)
				)
			)
			(
				(ego
					setLoop:		1,
				)
				(= cycles 3)
			)
			(
				(ego
					setLoop:		0,
					setCycle:	Forward
				)
				(= seconds 4)
			)
			(;5 is current state
				(theMusic stop:)
				(= register 1)
				(Cls)
				(Print "\"You'll never ride with me again!\"")
				(ego
					view:			vLarryFlattened,
					loop:			lFlat,
					x:				(- (ego x?) flattenedX),
					y:				(+ (ego y?) flattenedY 5),
					setCycle:	0,
				)
				(mCar2 play:)
			)
			(
				(theMusic3 fade:)
				(Death vLarryMugged lTaxiBully 1)
				(Format @deathTitleStr "Larry-cakes anyone?")
				(GameOver "It's never a good idea to \"stiff a cabbie!\"")
			)
		)
	)
)

(instance sFindLarry of Script
	(method (doit)
		(super doit: &rest)
		(if (ego mover?)
			(sPiss stop:)
			(dog setScript: sRunOff NULL register)
		)
	)
	(method (changeState newState)
		(switchto (= state newState)
			(;0 is current state
				(theMusic
					number:		mDog,
					loop:			1,
					vol:			127,
					flags:		1,
					play:,
				)
				(dog
					x:				(if (= register (Random 0 1)) 330 else -10),
					y:				dogY,
					setCycle:	Walk,
					setMotion:	PolyPath (if register (+ (ego x?) 17) else (- (ego x?) 15))
												(+ (ego y?) 1) self
				)
			)
			(
				(PolyDog)
				(dog
					view:			vDogPiss,
					loop:			(+ lTurn register),
					cel:			0,
					setCycle:	EndLoop self
				)
			)
			(
				(dog
					loop:			(+ lLift register),
					cel:			0,
					setCycle:	EndLoop self
				)
			)
			(
				(sPiss play:)
				(dog
					loop:			(+ lPiss register),
					setCycle:	Forward,
				)
				(Bset fSmellsLikeDogPiss)
				(= beenPissedOn TRUE)
				(= seconds 3)
			)
			(
				(sPiss stop:)
				(if
					(or (== (ego view?) vLarryWalking)
						(== (ego view?) vLarryStopping))
					(HandsOff)
					(curRoom setScript: sShakeLeg NULL register)
				)
				(dog
					setScript:	sRunOff NULL register
				)
			)
		)
	)
)

(instance sRunOff of Script
	(method (changeState newState)
		(switch (= state newState)
			(DROPLEG
				(cond
					((or	(== (dog view?) vDogWalk)
							(== (dog loop?) lTurn))
						(= state (- WALKOFF 1))
						(= cycles 1)
					)
					((== (dog loop?) lLift)
						(= cycles 1)
					)
					(else
						(dog
							loop:			(+ lLift register),
							cel:			cLiftLast,
							setCycle:	BegLoop self
						)
					)
				)
			)
			(TURN
				(dog
					loop:			(if register lTurn else (+ lTurn 1)),
					cel:			cTurnLast,
					setCycle:	BegLoop self
				)
			)
			(WALKOFF
				((curRoom obstacles?) delete: dogPoly)
				(dog
					view:			vDogWalk,
					setCycle:	Walk,
					setMotion:	PolyPath (if (> (dog x?) (ego x?)) 350 else -30) dogY self
				)
			)
			(TRYAGAIN
				(theMusic fade:)
				(dog
					z:				1000,
					dispose:
				)
				(self dispose:)
			)
		)
	)
)

(instance sTaxiScript of Script
	(method (doit)
		(super doit: &rest)
		(if (== state 4)
			(theMusic3 send: 9 mPITCHBEND (* (- (taxi x?) waitX) 50))
		)
		(if (== state 5)
			(taxi x: (+ waitX register))
			(theMusic3 send: 9 mPITCHBEND (* (- (taxi x?) waitX) 30))
			(if (== register 0)
				(theMusic3 send: 9 mPITCHBEND 0)
				(= cycles 1)
			)
			(if (== register 16)
				(soundFX
					number:		sCabTires,
					setLoop:		1,
					flags:		1,
					play:
				)
			)
			(= register (/ register 2))
		)
	)
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego setHeading: 180 self)
			)
			(
;				(Print "\"Yo, taxi!\"" #dispose:)
				(ego
					egoSpeed:,
					view:			vLarryHailing,
					setLoop:		lHailing,
					setCel:		0,
					cycleSpeed:	1,
					setCycle:	EndLoop self
				)
			)
			(
				(soundFX
					number:		sYellTaxi,
					loop:			1,
					flags:		0,
					vol:			127,
					play:
				)
				(= seconds 3)
			)
			(
;				(Cls)
				(= seconds (Random 1 3))
			)
			(;4 is current state
				(NormalEgo 2)
				(if (not (cast contains: taxi))
					(taxi
						approachVerbs:	verbDo,
						cycleSpeed:		howFast,
						moveSpeed:		howFast,
						init:				,
						x:					400,
						y:					taxiY,
						setMotion:		MoveTo (+ waitX 64) taxiY self
					)
					(= register 64) 	;for pitchBending
					(theMusic3
						number:			sCabDeparting,
						loop:				-1,
						vol:				127,
						flags:			1,
						play:,
					)
				else
					(cond
						((== (taxi x?) waitX)
							(Cls)
							(Print "\"Stop whistling and get in the cab!\"")
						)
						((< (taxi x?) waitX)
							(Cls)
							(Print "\"Too late now, Ace-(in-the)-hole!\"
								shouts the rude cabbie, \"I'm outta here!\"")
						)
						(else
							(Cls)
					 		(Print "Hold your horses, he's on his way!")
						)
					)
					(HandsOn)
					(self dispose:)
				)
			)
			(;5 is current state
				NULL
			)
			(
				(taxi
					setCycle:	EndLoop self
				)
			)
			(
				(HandsOn)
				(PolyTaxi)
				(taxi
					setScript: 		sTaxiWait,
					approachX:		(+ (taxi x?) 10),
					approachY:		(- (taxi brTop?) ABOVETAXI),
;					stopUpd:,
				)
				(self dispose:)
			)
		)
	)
)

(instance sTaxiWait of Script
	(enum
		WAIT
		STARTTAXI
		COMPLAIN
		BEEP
		PAUSE
		BEEP1
		PAUSE2
		LEAVE
		DISPOSE
	)
	(method (doit)
		(super doit: &rest)
		(if (== state LEAVE)
			(taxi
				startUpd:,
				x: (- waitX (/ register 4)),
			)
			(theMusic3 send: 9 mPITCHBEND (* (- waitX (taxi x?)) 10))
			(if (< (taxi x?) -120)
				(= cycles 1)
			)
			(= register (* register 2))
		)
	)
	(method (changeState newState)
		(switch (= state newState)
			(WAIT
				(= seconds 14)
			)
			(STARTTAXI
				(taxi startUpd:)
				(= cycles 1)		;to avoid modeless dialog bug
			)
			(COMPLAIN
				(Cls)
				(Print
					"\"I ain' gonna wait all day, bub!\" yells the cabbie."
					#dispose:
					#time:		4
				)
				(= cycles 9)
			)
			(BEEP
				(if (and (< (ego x?) (- (taxi x?) 20))
							(> (ego y?) (- (taxi brTop?) ABOVETAXI)))
					(soundFX
						number:	sCarHonk,
						play:
					)
					(= cycles 12)
				else
					(= state PAUSE2)
					(= cycles 1)
				)
			)
			(PAUSE
				(soundFX stop:)
				(= cycles 1)
			)
			(BEEP1
				(soundFX play:)
				(= cycles 12)
			)
			(PAUSE2
				(soundFX stop:)
				(= state COMPLAIN)
				(= seconds 5)
			)
			(LEAVE
				(= register 1)
				(if (!= (curRoom script?) sEnterTaxi)
					((curRoom obstacles?) delete: taxiPoly)
					(soundFX
						number:		sCabTires,
						setLoop:		1,
						flags:		1,
						play:
					)
				else
					(self dispose:)
				)
			)
			(DISPOSE
				(if 
					(< (taxi x?) -80)
					(taxi
						dispose:
					)
					(theMusic3 fade:)
					(self dispose:)
				else
					(-= state 1)
					(= cycles 1)
				)
			)
		)
	)
)

(instance sFlattenLarry of Script
	(method (doit &tmp dist)
		(super doit: &rest)
		(= dist (GetDistance (ego x?) (ego y?) (aCar x?) (aCar y?)))
		(cond
			((< dist 0)
				(= dist 0)
			)
			((> dist 300)
				(= dist 300)
			)
		)
		(if (< state 2)
			(mCar
				setVol:			(- 127 (/ dist 4)),
			)
		)
		(if (or	(== state 1)
					(== state 2))
			(++ register)
			(mCar
				send:			2 mPITCHBEND (- (* register 100)),
			)
		)
	)
	(method (changeState newState)
		(switchto (= state newState)
			(
				(mCar
					play:			50
				)
				(ego
					view:			vLarryFlattened,
					loop:			lFlattened,
					cel:			cLookout
				)
				(aCar
					cycleSpeed:	howFast,
					moveSpeed:	howFast,
					init:			,
					view:			carView,
					x:				450,
					setPri:		(+ (ego priority?) 2),
					setMotion:	MoveTo (+ (ego x) 67) (aCar y?) self
				)
			)
			(
				(mCar2 play:)
				(mCar3 play:)
				(ego
					setPri:		(+ (aCar priority?) 1),
					x:				(- (aCar x?) 67),
					setCel:		cSmash,
				)
				(aCar
					setMotion:	MoveTo -50 (aCar y?) aCar
 				)
				(= cycles 5)
			)
			(;2 is current state
				(mCar fade: 0 5 8 1)
				(ego
					setPri:		RELEASE,
					setLoop:		lFlat,
					setCel:		cFlat,
					x:				(- (ego x?) flattenedX),
					y:				(+ (ego y?) flattenedY),
				)
				(= seconds 3)
			)
			(
				(= seconds 3)
			)
			(
				(Death vLarryFlattened lTire)
				(Format @deathTitleStr "That's you all over!")
				(GameOver "Larry, the traffic here is really a killer! 
					You should forget about crossing these streets!")
			)
		)
	)
)

(instance sEnterTaxi of Script
	(method (doit &tmp maxVal)
		(super doit: &rest)
		(if (== state 3)
			(taxi
				startUpd:,
				x: 		(- waitX register),
			)
			(= maxVal (* (- waitX (taxi x?)) 10))
			(theMusic3 send: 9 mPITCHBEND (if (< maxVal 2000) maxVal else 2000))
			(if (< (taxi x?) -80)
				(= cycles 1)
			)
			(= register (* register 2))
		)
	)
	(method (changeState newState)
		(switchto (= state newState)
;			(
;				(ego
;					setMotion:	PolyPath (+ (taxi x?) 10) (- (taxi brTop?) ABOVETAXI) self
;				)
; 			)
			(
				(= cycles 1)
			)
			(
				(soundFX
					number:		sCabDoorOpen,
					loop:			1,
					flags:		0,
					vol:			127,
					play:
				)
				(ego
					setHeading:	180
				)
				(= cycles 10)
			)
			(
				(soundFX
					number:		sCabDoorClose,
					loop:			1,
					flags:		0,
					vol:			127,
					play:
				)
				(ego hide:)
				(= cycles 10)
			)
			(;3 is current state
				(= register 1)
				(soundFX
					number:		sCabTires,
					setLoop:		1,
					flags:		1,
					play:
				)
			)
			(
				(theMusic3
					fade:	90 5 5 0
				)
				(curRoom newRoom: 200)
			)
		)
	)
)

(instance sShakeLeg of Script
	(method (doit)
		(super doit: &rest)
		(if (and (not state)
					(== (sRunOff state?) WALKOFF))
			(= cycles 1)
		)
	)
	(method (changeState newState)
		(switchto (= state newState)
			(
			 	NULL
			)
			(
				(ego
					egoSpeed:,
					normal:		FALSE,
					view:			vLarryShakingLeg,
					loop:			(if register 0 else 1),
					cel:			0,
					cycleSpeed: 1,
					setCycle: 	EndLoop self
				)
			)
			(
				(NormalEgo 2)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

;
;	Props
;

(instance aCar of Actor
	(properties
		view			806
		x				340
		y				170
		xStep			30
		description "the car"
		lookStr		"Crazy driver!"
		signal		(| ignrAct fixedLoop fixedCel)
	)
	(method (cue)
		(super cue:)
		(= z 1000)
		(self dispose:)
	)
)

(instance taxi of Actor
	(properties
		view			vTaxi
		loop			lTaxi
		xStep			20
		signal		(| ignrAct fixedLoop fixedCel)
		description "the taxi"
		sightAngle  40
	)
	(method (cue)
		(super cue:)
		(Cls)
		(Print "It's not very clean.")
		(Print "It's not very safe.")
		(Print "(And those are its good points!)" LOW)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				turnEgo
			)
			(verbSmellTaste
				(Cls)
				(Print "It smells like an old cab.")
			)
			(verbDo
				(if (< (taxi x?) waitX)
					(Cls)
					(Print "It's too late now, Larry!")
				else
					(if (not (Btst fRodeInCab))
						(Bset fRodeInCab)
						(Cls)
						(Print "Vowing to hold your breath forever, you
							enter this miserable excuse for public transportation.")
					)
					(HandsOff)
					(taxi setScript: 0)
					(soundFX stop:)
					(curRoom setScript: sEnterTaxi)
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance dog of Actor
	(properties
		view			vDogWalk
		description "the dog"
		sightAngle	40
		signal		ignrAct
	)
	(method (doVerb theVerb invItem)
		(Cls)
		(switch theVerb
			(verbLook
				(cond
					((== (dog view?) vDogPiss)
						(Print "Obviously, he thinks you're a fire plug.")
					)
					((== (dog script?) sRunOff)
						(Print "You hope he's soon a \"Hmong\" friends.")
					)
					(else
						(Print "Dogs like that make you want to keep moving.")
					)
				)
			)
			(verbDo
				(Print "As you bend over to show that dog who's boss,
					his bare teeth convince you there are worse things
					than damp trousers!")
			)
			(verbSmellTaste
				(Print "The dog smells quite bad, although still better than you.")
			)
			(verbZipper
				(Print "It would seem turn-about would be fair play,
					but you just don't have it in you!")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance taxiSignProp of View
	(properties
		view			vLarryHailing
		loop			1
		z				100
      description "the taxi sign"
      sightAngle 	40
		lookStr		"High up that pole sits a lonely sign that reads,
						\"Taxi Stand.\""
	)
	(method (doVerb theVerb invItem)
      (switch theVerb
         (verbDo
            (Print "You whistle loudly for a taxi.")
				(HandsOff)
				(curRoom setScript: sTaxiScript)
         )
         (verbTalk
				(self doVerb: verbDo)
         )
         (else
            (super doVerb: theVerb invItem &rest)
         )
      )
   )
)

(instance dogPoly of Polygon
	(properties
		type			PBarredAccess
	)
)

(instance taxiPoly of Polygon
	(properties
		type			PBarredAccess
	)
)

;
;	Features
;

(instance streetF of Feature
	(properties
		x 				160
		y 				480
		onMeCheck	cLCYAN
 		description "the street"
		sightAngle 	85
		lookStr		"Isn't it funny how there's so little traffic?"
	)
)


(procedure (PolyTaxi &tmp rL rT rR rB)
	(= rL (- (taxi brLeft?) 8))
	(= rT (- (taxi brTop?) ABOVETAXI))
	(= rR (+ (taxi brRight?) 8))
	(= rB (+ (taxi brBottom?) 5))

	(= [taxiPts 0] (= [taxiPts 6] rL))
	(= [taxiPts 1] (= [taxiPts 3] rT))
	(= [taxiPts 2] (= [taxiPts 4] rR))
	(= [taxiPts 5] (= [taxiPts 7] rB))
	(taxiPoly
		points: 	@taxiPts,
		size: 	4
	)
	(curRoom addObstacle: taxiPoly)
)

(procedure (PolyDog &tmp rL rT rR rB)
	(= rL (- (dog brLeft?) 8))
	(= rT (- (dog brTop?) 4))
	(= rR (+ (dog brRight?) 8))
	(= rB (dog brBottom?))

	(= [dogPts 0] (= [dogPts 6] rL))
	(= [dogPts 1] (= [dogPts 3] rT))
	(= [dogPts 2] (= [dogPts 4] rR))
	(= [dogPts 5] (= [dogPts 7] rB))
	(dogPoly
		points: 	@dogPts,
		size: 	4
	)
	(curRoom addObstacle: dogPoly)
)

(instance mCar of Sound
	(properties
		number		sCarHonk
		loop			1
		vol			50
	)
)

(instance mCar2 of Sound
	(properties
		number		sCarThump
		loop			1
		vol			127
	)
)

(instance mCar3 of Sound
	(properties
		number		sCarLeaving
		loop			1
		vol			127
	)
)

(instance sPiss of Sound
	(properties
		number		sDogPissing
		loop			-1
		vol			127
		flags			1
	)
)
(instance soundFX of Sound
	(properties
		loop			1
		flags			1
	)
)


