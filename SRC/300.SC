;**
;**	Logics for Room 300 -- Casino Exterior
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Mike Larsen
;**
;**	Last Update:  June 17, 1991
;**


(script# 300)

(public
	rm300		0
)

(local
	aAppleManPolyPts = [0 0 0 0 0 0 0 0]
	lookedOnce = 0
	appleManTimer
	paidWith
	doorState
)
(enum
	dClosed
	dOpening
	dOpen
	dClosing
)


(define	sDoorsCasino				300)
;(define	sFountain					301)
(define	mAppleMan					302)

(define	appleCost					10)

(define	doorLeftX					141)
(define	doorRightX					235)
(define	doorXcenter 				185)
(define	doorY 						124)
(define	doorMat 		143 123 230 135)

(define	pOutsideCasino				300)
	(define	vOutsideCasino			300)

	(define	vAppleMan		 		302)
;	(define	lWalkingLoops		0-3)
		(define	lHeadFront			4)
		(define	lHeadRight			5)
		(define	lAppleGesture		6)
		(define	lGrabApple			7)
		(define	lGiveApple			8)
		(define	lHoldApple			9)
		(define	lGrabBarrel			10)

(instance rm300 of LLRoom
	(properties
		picture	300
		north		310
		west		170
		east		400
		lookStr	"You are outside the lovely Lost Wages Casino and Hotel.
					You hear the sounds of money changing hands within.
					A tasteful modern sculpture decorates a fountain."
	)
	(method (init)
;		(Load VIEW vOutsideCasino)
		(LoadMany SOUND sDoorsCasino sFountain)
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	128 128 115 135 100 137 105 142 117 142 117 147 35 147 11 143
						9 136 38 130 35 119 0 124 0 0 319 0 319 141 282 141 282 132
						301 132 301 128 210 128 196 108 179 108 164 128,
				yourself
			)
		)
;		(curRoom addObstacle:
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	128 128 115 135 100 137 105 142 117 142 117 147 35 147 24 136
;						15 104 0 104 0 0 319 0 319 141 282 141 282 132 301 132
;						301 128 210 128 196 108 179 108 164 128,
;				yourself
;			)
;		)
		(aDoorLeft
			init:,
			stopUpd:,
			approachVerbs: verbDo verbZipper verbSmellTaste
		)
		(aDoorRight
			init:,
			stopUpd:,
			approachVerbs: verbDo verbZipper verbSmellTaste
		)
		(switch prevRoomNum
			(400
				(self style: SCROLLRIGHT)
				(ego y: 143)
			)
			(310
				(ego
					setPri:	6,
					posn:		doorXcenter 120,
				)
;**			(self setScript: sFromCasino) moved down to end of init
			)
			(170
				(ego y: 154)
			)
			(200
				NULL
			)
			(else									;TP
				(ego
					posn:		160 150
				)
			)
		)
		(ego
			init:,
		)
		(tit
			init:,
			cycleSpeed:	howFast,
			setCycle:	Forward
		)
		(knee
			init:,
			cycleSpeed:	howFast,
			setCycle:	Forward
		)
		(hip
			init:,
			cycleSpeed:	howFast,
			setCycle:	Forward
		)
		(fountain
			init:,
			cycleSpeed:	howFast,
			setCycle:	Forward
		)
		(fPlant 	init:)
		(fSign 	init:)
		(fLights init:)
		(statue	init:)
		(super	init:)

		(if (or	debugging
					(ObjInRoom iApple))
			(if (Btst fApplemanTgl)
				(= appleManTimer (Random 100 300))
				(LoadMany VIEW
					vAppleMan vLarryGetting
				)
				(Load SOUND mAppleMan)
				(aAppleMan
					cycleSpeed:	howFast,
					moveSpeed:	howFast,
					init:
				)
				(appleHead
					cycleSpeed:	howFast,
					init:
				)
			)
			(Btgl fApplemanTgl)
		)
		(self setRegions: SIDEWALK)
		(fountainSFX play:)
		(if (== prevRoomNum 310)
			(self setScript: sFromCasino)
		)
	)
	(method (doit)
		(super doit: &rest)
		(cond
			((and	(<= (ego x?) 50)
				(<= (ego y?) 130))
				(curRoom newRoom: 170)
			)
			((== script sFromCasino))
			((== script sToCasino))
			((and (ego inRect: doorMat)
					(or	(== doorState dClosed)
							(== doorState dClosing)))
				(if (aDoorLeft script?)
					((aDoorLeft script?) dispose:)
				)
				(aDoorLeft setScript: sAutoDoorOpen)
			)
			((< (ego y?) doorY)
				(curRoom setScript: sToCasino)
			)
			((and (not (ego inRect: doorMat))
					(or	(== doorState dOpening)
							(== doorState dOpen)))
				(if (aDoorLeft script?)
					((aDoorLeft script?) dispose:)
				)
				(aDoorLeft setScript: sAutoDoorClose)
			)
			(script)
			((and appleManTimer				;**	if he's coming this time AND
					( > (ego x?) 50))			;**	Larry's not at room's left edge
				(if (or	(== 1 (-- appleManTimer))	;**	if time's up, OR
							( > (ego x?) 260))			;**	ego's leaving room
					(= appleManTimer 0)
					(aAppleMan setScript: sAppleMan)
				)
			)
		)
	)
)

;
;	SCRIPTS
;

(instance sToCasino of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(HandsOff)
				(ego
					setPri: 		6,
					setMotion:  MoveTo doorXcenter 121 self,
				)
				(if (aDoorLeft script?)
					((aDoorLeft script?) dispose:)
				)
				(aDoorLeft setScript: sAutoDoorClose)	;**	it'll cue us later
			)
			(
				NULL
			)
			(
				(curRoom newRoom: 310)
			)
		)
	)
)
(instance sFromCasino of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(HandsOff)
				(ego
					setPri:	6,
					posn:		doorXcenter 120,
				)
				(if (aDoorLeft script?)
					((aDoorLeft script?) dispose:)
				)
				(aDoorLeft setScript: sAutoDoorOpen)	;**	it'll cue us later
			)
			(
				(ego
					setPri:		RELEASE,
					setMotion:  MoveTo doorXcenter 136 self
				)
			)
			(
				(HandsOn)
				(self dispose:)
			)
		)
	)
)
(instance sAutoDoorOpen of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(doorSFX play:)
				(aDoorLeft  setMotion: MoveTo 101 doorY self)
				(aDoorRight setMotion: MoveTo 275 doorY self)
				(= doorState dOpening)
			)
			(
				NULL
			)
			(
				(doorSFX stop:)
				(aDoorLeft stopUpd:)
				(aDoorRight stopUpd:)
				(= doorState dOpen)
				(if (== (curRoom script?) sFromCasino)
					(sFromCasino cue:)
				)
				(self dispose:)
			)
		)
	)
)

(instance sAutoDoorClose of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(doorSFX play:)
				(aDoorLeft  setMotion: MoveTo doorLeftX  doorY self)
				(aDoorRight setMotion: MoveTo doorRightX doorY self)
				(= doorState dClosing)
			)
			(
				NULL
			)
			(
				(doorSFX stop:)
				(aDoorLeft stopUpd:)
				(aDoorRight stopUpd:)
				(= doorState dClosed)
				(if (== (curRoom script?) sToCasino)
					(sToCasino cue:)
				)
				(self dispose:)
			)
		)
	)
)

(instance sAppleMan of Script
	(method (doit)
		(super doit:)
		(if (and (== state 5)
			 		(ego mover?))
			(= seconds 0)
			(= cycles 1)
		)
	)
	(method (changeState newState)
		(switch (= state newState)
			(0
				(theMusic
					number:		mAppleMan,
					setLoop:		1,
					flags:		1,
					play:
				)
				(aAppleMan
					setCycle:	Walk,
					setMotion:	PolyPath 49 151 self
				)
			)
			(1
				(appleHead
					setCycle:	ForwardCounter 3 self
				)
				(aAppleMan
					setLoop:		lAppleGesture,
					setCel:		0,
					setCycle:	EndLoop
				)
			)
			(2
				(Print "\"Apples. Apples! APPLES for sale!!\"" HIGH)
				(aAppleMan
					setLoop:	  	RELEASE,
					setCycle:	Walk,
					setMotion:	PChase ego 45 self
				)
			)
			(3
;**					(HandsOff)
;**					(Face ego aAppleMan)
				(= cycles 3)
			)
			(4
				(aAppleMan
					setLoop:		lGrabApple,
					setCel:		0,
					setCycle:	EndLoop
				)
				(appleHead
					setCycle:	ForwardCounter 4 self
				)
			)
			(5
				(Printf "\"Hello, sir,\" says the skinny young man in the barrel.
					\"Would you help out a poor unfortunate by purchasing an apple?
					The price is reasonable: only $%d.00.\"" appleCost)
;**					(if
;**						(not (curRoom script?))
;**						(HandsOn)
;**					)
				(= seconds 7)
			)
			(6
				(appleHead
					setCycle:	ForwardCounter 2 self
				)
				(aAppleMan
					setCycle:	BegLoop
				)
			)
			(7
				(if (ego mover?)
					(Print "\"Hey, come back!\" says the man.
						\"Are you sure you don't want to buy an apple?\"")
				else
					(Print "\"I presume that means `No,'\" he says pleasantly.
						\"Very well then, perhaps some other time.\"")
				)
				(= seconds 2)
			)
			(8
				(aAppleMan
					setLoop:		-1,
					setCycle:	Walk,
					setMotion:	PolyPath	-30 155 self
				)
			)
			(9
				(aAppleMan dispose:)
				(appleHead dispose:)
				(self dispose:)
			)
		)
	)
)

(instance sBuyApple of Script
	(method (changeState newState)
		(switchto (= state newState)
			(0
				(HandsOff)
				(aAppleMan
					setLoop:			lAppleGesture,
					setCel:			0,
					setMotion:		0
				)
				(= cycles 1)
			)
			(1
;				(MakeApplePoly)
				(= cycles 1)
			)
			(2
				(if (> (ego distanceTo: aAppleMan) 30)
					(ego
						setCycle:	Walk,
						setMotion:	PolyPath (+ (aAppleMan x?) 29) (aAppleMan y?) self
					)
				else
					(= cycles 1)
				)
			)
			(3
;				((curRoom obstacles?) delete: aAppleManPolyPts)
				(Face ego aAppleMan)
				(appleHead
					setCycle:	ForwardCounter 2
				)
				(aAppleMan
					setCycle:	EndLoop self
				)
			)
			(4
				(cond
					((== paidWith iRing)
						(Print "\"Wow! Thanks a lot, Mister!\" shouts the
							poor misfortunate, \"I'll gladly trade you an apple
							for a diamond ring!\"")
						(Print "\"But don't you think you might have needed this
							later?\"")
						(ego put: iRing 0)
					)
					((== ((inventory at: iApple) owner?) ego)
						(Printf "\"You know,\" says the man,
							peering deep into his barrel, \"I'm all out!
							But thank you for your continued support.
							And for the $%d.00!\"" appleCost)
						(= state 7)
					)
					(else
						(Printf "\"Thank you, sir,\"
							says the skinny young man in the barrel.
							\"For helping out a poor unfortunate by
							purchasing an apple at a reasonable $%d.00.\"" appleCost)
						(Points pBoughtApple ptsBoughtApple)
					)
				)
				(= cycles 1)
			)
			(5
				(ego
					view:			vLarryGetting,
					setLoop:		(if (< (ego x?) (aAppleMan x?)) 0 else 1),
					setCel:		0,
				)
				(aAppleMan
					setLoop:		lGrabApple,
					setCycle:	EndLoop self
				)
			)
			(6
				(ego
					setCel:		1,
					setCycle:	EndLoop
				)
				(aAppleMan
					setLoop:		lGiveApple,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(7
				(ego
					setCycle:	BegLoop self
				)
			)
			(8
				(cond
					((== paidWith iRing)
						(Print "\"If you have any other jewelry you'd like to
							swap, be sure to let me know!\"")
						(ego get: iApple)
					)
					((>= larryDollars appleCost)
						(-= larryDollars appleCost)
						(Print "\"Please call again,
							whenever you are in the neighborhood,\"
							says the man.
							And with that, he walks away into the night.")
						(ego get: iApple)
					)
					(else
						(Print "\"You know, I'm a little short of funds myself,\"
							you say to the man in the barrel.
							\"Perhaps next time, ok?\"")
						(Print "He says nothing, but walks away dejectedly
							into the Lost Wages night.")
					)
				)
				(= cycles 30)
			)
			(9
				(if
					(not (curRoom script?))
					(HandsOn)
					(NormalEgo)
				)
				(aAppleMan
					setLoop:		-1,
					setCycle:	Walk,
					setMotion:	PolyPath	-30 155 self
				)
			)
			(11
				(self dispose:)
			)
		)
	)
)
(instance sLookInBarrel of Script
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(aAppleMan
					setMotion:		0,
					setLoop:			lAppleGesture,
					setCel:			0,
				)
				(ego
					setMotion:	PChase aAppleMan 25 self
				)
			)
			(1
				(Face ego aAppleMan)
				(= cycles 5)
			)
			(2
				(if (not register)
					(Print "Peering down inside the poor man's barrel you say,
						\"Wow! You really did lose everything!\"")
					(if
						(not (curRoom script?))
						(HandsOn)
					)
					(= state 3)
					(= cycles 1)
				else
					(appleHead
						setCycle:	ForwardCounter 2 self
					)
					(aAppleMan
						setLoop:	lGrabBarrel,
						setCel:	0,
					)
				)
			)
			(3
				(Print "\"Oh no, you don't!\" exclaims the man.
					\"Keep your hands out of my barrel!\"")
				(= cycles 10)
			)
			(4
				(if
					(not (curRoom script?))
					(NormalEgo 1)
					(HandsOn)
				)
				(aAppleMan
					setLoop:		-1,
					setCycle:	Walk,
					setMotion:	PolyPath	-30 155 self
				)
			)
			(5
				(aAppleMan dispose:)
				(self dispose:)
			)
		)
	)
)

;*************
;*** SOUND ***
;*************

(instance fountainSFX of Sound
	(properties
		number	sFountain
		vol		127
		loop		-1
		flags		0
	)
)
(instance doorSFX of Sound
 	(properties
		number	sDoorsCasino
		vol		127
		loop		1
		flags		0
	)
)

;**************
;*** ACTORS	***
;**************

(instance aAppleMan of Person
	(properties
		view			vAppleMan
		loop			0
		cel			0
		x				-15
		y				150
		priority		13
		description "the apple salesman"
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbUse
				(switch invItem
					(iWallet
						(if (!= (aAppleMan script?) sBuyApple)
							(aAppleMan setScript: sBuyApple)
							(= paidWith iWallet)
						else
							(Print "Why don't you catch him next time around, Larry?")
						)
					)
					(iRing
						(if (!= (aAppleMan script?) sBuyApple)
							(aAppleMan setScript: sBuyApple)
							(= paidWith iRing)
						else
							(Print "Why don't you catch him next time around, Larry?")
						)
					)
					(iBreathSpray
						(Print "\"No, thanks,\" he says,
							\"I'm broke, not odiferous!\"")
					)
					(iApple
						(Print "\"I don't want that thing back!\"")
					)
					(iWhiskey
						(Print "\"No, thanks, I don't drink!\"")
					)
					(iRose
						(Print "\"Sweet, but I'm busted!
							What do I need with a rose?\"")
					)
					(iLubber
						(Print "\"I'm selling apples, not cucumbers!\"")
					)
					(iDiscoPass
						(Print "\"I can't afford to buy a drink in THAT place!\"")
					)
					(iPocketKnife
						(Print "\"What do you think? I want to slash my wrists?\"")
						(Print "\"No, thanks, Mister. I deal in cash only.\"")
					)
					(iWine
						(Print "\"No, thanks, I don't drink!\"")
					)
					(iHammer
						(Print "You take a swing at the apple salesman, but miss.")
						(Print "(You never were any
							good at hack-and-slash games!)" LOW)
					)
					(else
						(Print "\"Sorry,\" replies the apple salesman to your
							generous offer, \"but I'm broke, not stupid!\"")
					)
				)
			)
			(verbLook
				(if lookedOnce
					(aAppleMan setScript: sLookInBarrel NULL 0)
				else
					(Print "He's yet another poor soul who donated his everything
						so the shrine of Lost Wages could continue to flourish.")
					(= lookedOnce 1)
				)
			)
			(verbTalk
				(Print "\"Hey, Buddy,\" you joke. \"How's your Pippin?\"")
				(Print "He mutters under his breath,
					\"(10,000 comedians out of work in this town...)\"")
			)
			(verbDo
				(aAppleMan setScript: sLookInBarrel NULL 1)
			)
			(verbZipper
				(Print "\"Sorry, buddy,
					but this is MY night in the barrel!\"")
			)
			(verbSmellTaste
				(Print "He smells of decomposed apple cores and fresh pork rind.")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)
(instance aDoorLeft of Actor
	(properties
		view			300
		loop			4
		cel			0
		x				doorLeftX
		y				doorY
		approachX	189
		approachY	133
		priority		7
		description "an automatic, motion-sensing door"
		signal		(| fixPriOn fixedLoop)
	)
	(method (doVerb theVerb invItem)
		(aDoorRight doVerb: theVerb invItem)
	)
)
(instance aDoorRight of Actor
	(properties
		view			300
		loop			4
		cel			1
		x				doorRightX
		y				doorY
		approachX	189
		approachY	133
		priority		7
		description "an automatic, motion-sensing door"
		signal		(| fixPriOn fixedLoop)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Isn't it funny how every time you reach out to touch
					these doors, they open?")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

;******************
;*** PROCEDURES ***
;******************

(instance aAppleManPoly of Polygon
	(properties
		type: 	PBarredAccess
	)
)

;**	(procedure (MakeApplePoly &tmp rL rT rR rB)
;**		(= rL (- (aAppleMan brLeft?) 5))
;**		(= rT (- (aAppleMan brTop?) 5))
;**		(= rR (+ (aAppleMan brRight?) 6))
;**		(= rB (aAppleMan brBottom?))
;**
;**		(= [aAppleManPolyPts 0] (= [aAppleManPolyPts 6] rL))
;**		(= [aAppleManPolyPts 1] (= [aAppleManPolyPts 3] rT))
;**		(= [aAppleManPolyPts 2] (= [aAppleManPolyPts 4] rR))
;**		(= [aAppleManPolyPts 5] (= [aAppleManPolyPts 7] rB))
;**		(aAppleManPoly points: @aAppleManPolyPts, size: 4)
;**		(curRoom addObstacle: aAppleManPoly)
;**	)


;*****************
;****	 PROPS  ****
;*****************

(instance appleHead of Prop
	(properties
		view	vAppleMan
		loop	lHeadFront
		description "the apple salesman"
	)
	(method (doit)
		(super doit:)
		(if
			(OneOf (aAppleMan loop?) lAppleGesture lGrabApple lHoldApple lGiveApple lGrabBarrel)
			(appleHead
				x:		(+ (aAppleMan x?) 2),
				y:		(aAppleMan y?),
				z:		35
			)
		else
			(appleHead
				z:		1000
			)
		)
	)
	(method (doVerb theVerb invItem)
		(aAppleMan doVerb: theVerb invItem)
	)
)

(instance tit of Prop
	(properties
		view	vOutsideCasino
		loop	1
		cel	0
		x		310
		y		34
		priority 15
		detailLevel 1
		signal 	(| ignrAct fixPriOn)
 		description	 "the sculpture"
		sightAngle	 40
	)
	(method (doVerb theVerb invItem)
		(statue doVerb: theVerb invItem)
   )
)
(instance hip of Prop
	(properties
		view	vOutsideCasino
		loop	3
		cel	0
		x	  	294
		y		90
		priority 15
		detailLevel 1
		signal (| ignrAct fixPriOn)
 		description	 "the sculpture"
		sightAngle	 40
	)
	(method (doVerb theVerb invItem)
		(statue doVerb: theVerb invItem)
   )
)
(instance knee of Prop
	(properties
		view	vOutsideCasino
		loop	2
		cel	0
		x		276
		y		129
		priority 15
		detailLevel 1
		signal (| ignrAct fixPriOn)
 		description	 "the sculpture"
		sightAngle	 40
	)
	(method (doVerb theVerb invItem)
		(statue doVerb: theVerb invItem)
   )
)
(instance fountain of Prop
	(properties
		view	vOutsideCasino
		loop	0
		cel	0
		x	  	291
		y		165
		priority 15
		detailLevel	2
		signal (| ignrAct fixPriOn)
		description "the fountain"
		lookStr
		"That water sure looks cold."
	)
	(method (doVerb theVerb invItem)
      (switch theVerb
         (verbDo
				(Print "That water sure is deep.")
			)
			(verbZipper
				(Print "Use the restroom at Lefty's instead.")
			)
			(verbSmellTaste
				(Print "The water tastes rather metallic and a little acidic.")
			)
			(else
            (super doVerb: theVerb invItem &rest)
			)
      )
   )
)

;****************
;*** FEATURES ***
;****************

(instance fPlant of Feature
	(properties
		x 64
		y 116
		z 0
		heading 0
		nsLeft 23
		nsTop 90
		nsBottom 143
		nsRight 105
 		description "the plant"
		lookStr "Which is more lifelike, the plant or the pot?"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "The plant feels like someone spit apple juice
					on it earlier!")
			)
			(verbTalk
				(Print "Do you think talking to artificial plants
					will make them grow?")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance fSign of Feature
	(properties
		x 151
		y 17
		z 100
		heading 0
		nsLeft 42
		nsTop 1
		nsBottom 33
		nsRight 260
 		description "the sign"
		lookStr 		"The sign above the door says \"CASINO,\" 
						in large, uneven letters."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You can't reach that sign from down here!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance fLights of Feature
	(properties
		x 143
		y 146
		z 100
		heading 0
		nsLeft 28
		nsTop 34
		nsBottom 58
		nsRight 259
 		description "the lights over the door"
		lookStr 		"These are only a few of the millions of lights that
						surround this casino, you just can't see the others!"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You can't reach the lights.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance statue of Feature
	(properties
		x				 249
		y				 94
		z				 0
		heading		 0
		nsLeft		 179
		nsTop			 0
		nsBottom		 188
		nsRight		 319
 		description	 "the sculpture"
		sightAngle	 40
		onMeCheck	 cLRED
		lookStr		"From the David Niven, Myrna Loy School of Design."
	)
	(method (doVerb theVerb invItem)
      (switch theVerb
			(verbTalk
				(Print "You and the sculpture have absolutely no common
					interests."
				)
			)
         (verbDo
				(Print "It feels like metal.")
			)
			(verbZipper
				(Print "Jeez, Larry; take a cold shower or something!")
			)
			(verbSmellTaste
				(Print "It tastes rather metallic.")
			)
			(else
            (super doVerb: theVerb invItem &rest)
			)
      )
   )
)

