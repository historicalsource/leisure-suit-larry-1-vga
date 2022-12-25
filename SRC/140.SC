;**
;**	Logics for Room 140 -- Lefty's Storeroom, TV & Pimp
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford &
;**
;**	Last Update:  June 16, 1991
;**


(script# 140)

(public
	rm140		0
)

(local
	climbing			;flag, on stairs
	gaveWarning		;flag
	madeComment		;flag
	remoteOnPimp	;toggle, for which message
	channels = [0 0 0 0 0 0 0 0]	;program text
	program	= [0 0 0 0 0 0 0 0]	;program name
	moveTimer 		;if ego doesn't get outta da way
)

(enum
	SOAP
	COMMERCIAL
	COMEDY
	DOCUMENTARY
	ROGERS
	SPORTS
	EDUCATIONAL
	NASTY
)

(define MOVETIME 		300)
(define inWay 194 146 261 159)
(define nearPimp 180 116 270 136)
(define inFront 148)	;so ego will be in front of pimp when pimp steps forward
(define stairX 183)
(define stairY 115)
(define tvX		228)	;222
(define tvY		155)
(define stairPimpX 197)
(define stairPimpY 124)
(define tvPimpX 221)
(define tvPimpY 148)
(define pimpWatchX 235)
(define pimpWatchY 146)
(define stairTopX	66)
(define midStairX 113)

;**	Bar music fades upon entry.  No sound, until TV turned on
;(define	sChannelChanging		140)
(define	mChannel1				141)		;**	Soap opera-Lush romantic
(define	mChannel2				142)		;**	Tom Bidet for Motel-69
(define	mChannel3				143)		;**	Software developer's
(define	mChannel4				144)		;**	Wild Kingdom Rhino fight
(define	mChannel5				145)		;**	Mr. Rogers theme
(define	mChannel6				146)		;**	High power sports
(define	mChannel7				147)		;**	Masterpiece Theater music, Handel
;(define	mChannel8				148)		;**	Slow grooving porn funk
;(define	mBarInterior			110)

(define	pPimpRoom				140)
(define	vPimpRoom				140)
	(define  lTail					0)
	(define  lTV					1)
	(define  lDoor					2)
(define	vLarryInPimpRoom		141)
	(define  lUseRemote			0)
		(define cClick				6)
(define	vPimp						142)
	(define  lFlipCoin			0)
	(define  lGesture				1)
	(define	lStand				2)	;still unused
	(define  lTalk					3)
	(define  lLook					4)
	(define  lMoveTv				5)
	(define  lWatchTv				6)
	(define  lAside				7)
		(define cAside				4)

(define clearObstacles
	((Polygon new:)
		type: PBarredAccess,
		init:	10 145 10 187 266 187 266 148 208 148 208 138 266 138 258 116
				205 116 205 122 199 122 67 0 319 0 319 189 0 189 0 145,
		yourself
	)
	((Polygon new:)
		type: PBarredAccess,
		init:	153 156 226 156 226 179 203 179 153 175,
		yourself
	)
	((Polygon new:)
		type: PBarredAccess,
		init:	48 164 92 164 92 180 48 180,
		yourself
	)
	((Polygon new:)
		type: PBarredAccess,
		init:	0 0 62 0 194 122 194 128 164 140 0 140,
		yourself
	)
)


(define blockObstacles
	((Polygon new:)
		type: PBarredAccess,
		init:	10 145 10 187 91 187 267 187 267 135 259 124 197 124 169 140
				0 140 0 0 319 0 319 189 0 189 0 145,
		yourself
	)
	((Polygon new:)
		type: PBarredAccess,
		init:	153 156 226 156 226 179 203 179 153 175,
		yourself
	)
	((Polygon new:)
		type: PBarredAccess,
		init:	48 164 92 164 92 180 48 180,
		yourself
	)
)

(instance rm140 of LLRoom
	(properties
		picture	140
		north		150
	)
	(method (init)
		(LoadMany VIEW
			vPimpRoom
			vPimp
		)
		(LoadMany SOUND
			sChannelChanging
			mChannel8
			;different channel music are loaded when they're needed
		)
		(theMusic3
			loop:			1,
			vol:			127,
			flags:		1,
		)
		(if (and (!= tvChannel NASTY)
					(ego has: iRemoteControl))
			(Load VIEW vLarryInPimpRoom)
			(Load TEXT 141)
		)

		(pimp init:)

		(switch prevRoomNum
			(110
				(Bclr fPimpStandingAside)
				(ego posn: 20 155)
			)
			(150
				(HandsOff)
				(curRoom
					addObstacle: 	clearObstacles,
					setScript: 		sFromHooker
				)
				(if (Btst fPimpStandingAside)
					(pimp
						setLoop:			lAside,
						setCel:			cAside,
						setPri:			pLCYAN,
						approachX:		tvPimpX,
						approachY:		tvPimpY,
					)
				)
				(= gaveWarning TRUE)
			)
			(else		;TP
				(ego posn: 40 160)
			)
		)
		(ego
			init:				,
			illegalBits:	$8000
		)

		(super init:)
		(if (Btst fTVOn)
			(theMusic
				loop:			-1,
				flags:		1,
				number:
				(switch tvChannel
					(SOAP mChannel1)
					(COMMERCIAL mChannel2)
					(COMEDY mChannel3)
					(DOCUMENTARY mChannel4)
					(ROGERS mChannel5)
					(SPORTS mChannel6)
					(EDUCATIONAL mChannel7)
					(NASTY mChannel8)
				),
			)
			(if
				(or
					(!= tvChannel NASTY)
					(and
						(== tvChannel NASTY)
						(== prevRoomNum 110)
					)
				)
				(theMusic play:)
			)
		else
			(theMusic fade: 90 15 5 0)
		)
		(pimp
			cycleSpeed:		howFast,
			approachVerbs:	actorVerbs 
		)
		(if (!= (pimp loop?) lAside:)
			(pimp setCycle: Forward)
		)

		(tv
			init:,
			approachVerbs:	objectVerbs verbLook
		)
		(mooseTail
			cycleSpeed:	howFast,
			init:,
			setScript:	sTail
		)
		(moose init:)
		(boxes		init:)
		(stairs
			init:,
			approachVerbs:	objectVerbs
		)
		(theDoorFeature
			init:,
			approachVerbs:	objectVerbs,
		)
		(thePeephole
			init:,
			approachVerbs:	objectVerbs,
		)
		(barrel1 init:)
		(barrel2 init:)
		(if (Btst fTVOn)
			(tvLights
				cycleSpeed:	howFast,
				init:				,
				setCel:			255,
				approachVerbs:	objectVerbs verbLook
			)
		)
		(if (== tvChannel NASTY)
			(pimp
				x:					pimpWatchX,
				y:					pimpWatchY,
				setLoop:			lWatchTv,
				setPri:			pLCYAN,
				approachX:		tvPimpX,
				approachY:		tvPimpY,
				setCycle:		RandCycle,
			)
			(curRoom addObstacle: clearObstacles)
		else
			(if (not (Btst fPimpStandingAside))
				(if (not (pimp script?))
					(pimp setScript: sPimp)
				)
				(curRoom addObstacle: blockObstacles)
			)
		)
	)
	(method (doit)
		(super doit: &rest)
		(if
			(and
				(< (ego y?) 116)
				(!= script sFromHooker)
			)
			(ego setPri: 8)
			(if
				(not climbing)
				(= climbing TRUE)
				(HandsOff)
				(ego setMotion: PolyPath stairTopX 0)
			)
		else
			(if (== (ego priority?) 8)
				(ego setPri: RELEASE)
			)
			(= climbing FALSE)
		)
		(cond
			(script)
			((< (ego y?) 45)
				(HandsOff)
 				(curRoom setScript: sToHooker)
			)
			((and (== (pimp loop?) lWatchTv)
				   (ego inRect: inWay)
			 )
			 	(if
					(not moveTimer)
					(Print "\"Hey, dude! Outta da way ah da teevee!!\"" HIGH)
					(= moveTimer MOVETIME)
				else
					(-- moveTimer)
				)
			)
			((and
				(== gaveWarning FALSE)
				(!= tvChannel NASTY)
			 	(ego inRect: nearPimp)
			 )
			 	(= gaveWarning TRUE)
				(Print "The pimp growls,
					\"You gots no bizness goin' up dese stairs, little man!\"" HIGH)
			)
			((and
				(== madeComment FALSE)
				(== prevRoomNum 150)
				(== (pimp loop?) lAside)
				(> (ego y?) 138)
			 )
			 	(= madeComment TRUE)
				(Print "\"Pretty sweet stuff, eh, boy?\" laughs the pimp." HIGH)
				(Bclr fPimpStandingAside)
				(pimp
					setLoop:		lAside,
					setCel:		cAside,
					setCycle:	BegLoop pimp
				)
				((curRoom obstacles?) dispose:)
				(curRoom addObstacle: blockObstacles)
			)
		)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Printf "The bar's storage room is almost empty.
					Its most significant feature is that big, mean-looking dude
					%s."
					(if (Btst fPimpWatchingTV)
						{watching TV}
					else
						{blocking the stairs}
					)
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
	(method (newRoom n)
		(ego
			setPri: 			RELEASE,
		)
		(UnLoad TEXT 141)
		(super newRoom: n)
	)
)

;
;	SCRIPTS
;

(instance sPimp of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= cycles (Random 5 25))
			)
			(
				(pimp
					setLoop:		lFlipCoin,
					setCel:		0,
					setCycle:	Forward,
				)
				(= seconds (Random 3 8))
			)
			(
				(if
					(== (pimp loop?) lAside)
					(pimp setLoop: lGesture)
				)
				(pimp
					setCel:			0,
					setCycle:		EndLoop self
				)
			)
			(
				(pimp
					setCel:		0
				)
				(= seconds (Random 3 8))
			)
			(
				(pimp
					setLoop:		lGesture,
					setCel:		0,
					setCycle:	EndLoop
				)
				(= seconds (Random 3 8))
			)
			(
				(self
					start:		(if (Random 0 1) 0 else 2),
					init:,
				)
			)
		)
	)
)

(instance sToHooker of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego setMotion: MoveTo stairTopX 0 self)
			)
			(
				(curRoom newRoom: 150)
			)
		)
	)
)

(instance sFromHooker of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					x:				stairTopX,
					y:				2,
					setPri:		8,
					setMotion: 	MoveTo midStairX 46 self
				)
			)
			(
				(ego
					setMotion: 	MoveTo 194 121 self
				)
			)
			(
				(HandsOn)
				(= climbing TRUE)
				(self dispose:)
			)
		)
	)
)

(instance sDoChannel of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					egoSpeed:,
					view:			vLarryInPimpRoom,
					setLoop:		lUseRemote,
					cycleSpeed:	howFast,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(= seconds 3)
			)
			(
				(theMusic3
					number:		sChannelChanging,
					play:
				)
				(if (not (Btst fTVOn))
					(Print "You aim your remote control at the pimp's TV
						and click the power switch."
						HIGH)
					(Points pTurnedOnTV ptsTurnedOnTV)
					(Bset fTVOn)
					(tvLights
						cycleSpeed: howFast,
						init:			,
						setCycle:	EndLoop
					)
				else
					(Print "You aim your remote control at the TV
						and click the channel selector."
						HIGH)
					(++ tvChannel)
				)
				(theMusic
					vol:			,
					loop:			-1,
					number:
					(switch tvChannel
						(SOAP
							mChannel1
						)
						(COMMERCIAL
							mChannel2
						)
						(COMEDY
							mChannel3
						)
						(DOCUMENTARY
							mChannel4
						)
						(ROGERS
							mChannel5
						)
						(SPORTS
							mChannel6
						)
						(EDUCATIONAL
							mChannel7
						)
						(NASTY
							mChannel8
						)
					),
					play:,
				)
				(switch tvChannel
					(COMMERCIAL
						(UnLoad SOUND mChannel1)
					)
					(COMEDY
						(UnLoad SOUND mChannel2)
					)
					(DOCUMENTARY
						(UnLoad SOUND mChannel3)
					)
					(ROGERS
						(UnLoad SOUND mChannel4)
					)
					(SPORTS
						(UnLoad SOUND mChannel5)
					)
					(EDUCATIONAL
						(UnLoad SOUND mChannel6)
					)
					(NASTY
						(UnLoad SOUND mChannel7)
					)
				)
				(ego
					setCycle:	BegLoop
				)
				(= seconds 3)
			)
			(
				(if (< tvChannel NASTY)
					(HandsOn)
				)
				(NormalEgo 2)
				(Print 141 (* 2 tvChannel) HIGH)
				(= seconds 3)
			)
			(
				(cond
					((== tvChannel ROGERS)
						(Print 141 (+ 1 (* 2 tvChannel)) HIGH)
					)
					((== tvChannel NASTY)
						(Bset fPimpWatchingTV)
				 		(+= state 1)
				 	)
					(else
						(Printf "Another boring %s" 141 (+ 1 (* 2 tvChannel)) HIGH)
					)
				)
				(= seconds 3)
			)
			(
				(Printf "The pimp seems vaguely interested in this program%s"
					(if (Btst fPimpStandingAside) {.} else
					{, but not interested enough to leave his post guarding
					the stairs.})
					HIGH)
				(self dispose:)
			)
			(
				(pimp
						setScript:	0,
				)
				(if (Btst fPimpStandingAside)
					(Bclr fPimpStandingAside)
					(++ state)
					(= cycles 1)
				else
					(pimp
						setLoop:		lLook,
						setCel:		0,
						cycleSpeed:	(+ 3 howFast),
						setCycle:	EndLoop self
					)
				)
			)
			(
				(pimp
					setCel:		0,
					setLoop:		lMoveTv,
					cycleSpeed:	(+ 1 howFast),
					setCycle:	EndLoop self,
				)
			)
			(
				(HandsOn)
				(pimp
					x:				pimpWatchX,
					y:				pimpWatchY,
					approachX:	tvPimpX,
					approachY:	tvPimpY,
					setCel:		0,
					setLoop:		lWatchTv,
					setPri:		pLCYAN,
					setCycle:	RandCycle,
					cycleSpeed:	howFast,
				)
				(= seconds 3)
			)
			(
				(Points pGotPimpToWatchTV ptsGotPimpToWatchTV)
				(Print "Well, well, Larry!
					It appears you've finally reached the pimp's cultural level."
					HIGH)
				((curRoom obstacles?) dispose:)
				(curRoom addObstacle: clearObstacles)
				(self dispose:)
			)
		)
	)
)

(instance sTail of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(mooseTail
					setCycle:	RandCycle
				)
				(= seconds 3)
			)
			(
				(mooseTail
					setCycle:	0,
					stopUpd:
				)
				(= seconds (Random 7 20))
			)
			(
				(self init:)
			)
		)
	)
)

(instance sToBar of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					illegalBits:	$0000,
					setMotion:		MoveTo 20 (ego y?) self
				)
			)
			(
				(curRoom newRoom: 110)
			)
		)
	)
)

;
; 	PROPS
;

(instance pimp of Person
	(properties
		view			vPimp
		loop			lFlipCoin
		cel			0
		x				208
		y				122
		description "the pimp"
		lookStr		"You can tell by his glazed stare,
						he's no rocket scientist!"
		signal		(| fixedLoop ignrAct)
		approachX	stairPimpX
		approachY	stairPimpY
		sightAngle	40
	)
	(method (cue)
		(super cue:)
		(sPimp
			start:		(if (Random 0 1) 0 else 2),
		)
		(pimp
			setScript:	sPimp,
			setPri:		RELEASE,
			approachX:	stairPimpX,
			approachY:	stairPimpY,
		)
	)
	(method (doVerb theVerb invItem)
		(if (Btst fPimpWatchingTV)
			(Print "The pimp ignores you.
				He's \"totally engrossed\" in something else right now.")
		else
			(switch theVerb
				(verbDo
					(Print "What? You want to pick a fight with the pimp?
						He's far too big, and you're far too wimpy.")
				)
				(verbZipper
					(Print "\"HEY! Waddayou crazy?!\"" HIGH)
				)
				(verbSmellTaste
					(Print "He smells of wet fur.")
				)
				(verbTalk
					(Print "\"If ya wanna go upstairs,\"
						he says in his scholarly voice,
						\"ya gotta pays da price!\"" HIGH)
					(Print "\"$200.00, cash up front, no refunds.\"" HIGH)
					(Print "(He certainly has a pleasing manner about him.)" LOW)
				)
				(verbUse
					(switch invItem
						(iWallet
							(cond
								((Btst fPimpWatchingTV)
									(Print "There is no need to pay the pimp;
										he's pleasantly distracted.
										Help yourself!")
								)
								((< larryDollars 200)
									(Print "You'd love to pay the pimp his $200.00,
										but you just don't have enough money.")
								)
								(else
									(= gaveWarning TRUE)
									(-= larryDollars 200)
									(Print "\"Thanks,\" he growls.
										\"Remember, no refunds!\"" HIGH)
									(if (Btst fScoredHooker)
										(Print "\"You mus' really like that stuff,
											eh, boy?\"" HIGH)
									)
									(if (not (Btst fPimpStandingAside))
										(Bset fPimpStandingAside)
										(pimp
											setLoop:		lAside,
											setCel:		0,
											setCycle:	EndLoop,
											setPri:		pLCYAN,
											approachX:	tvPimpX,
											approachY:	tvPimpY,
											setScript:	0,
										)
									)
									((curRoom obstacles?) dispose:)
									(curRoom addObstacle: clearObstacles)
									(Print "\"Have a nice lay.\"" HIGH)
								)
							)
						)
						(iRemoteControl
							(Print "The remote control's operation is far too
								sophisticated for his mental capabilities.")
						)
						(iRing
							(Print "Save your ring in case you find your
								\"true love,\" Larry!")
						)
						(iRibbon
							(Print "You'd never be able to tie his up with that
								ribbon, Larry.")
						)
						(iHammer
							(Print "The pimp is far too big for you to attack,
								even using your hammer as a weapon.")
							(Print "(Besides,  you're a lover, not a fighter!)" LOW)
						)
						(iPocketKnife
							(Print "The pimp is far too big for you to attack,
								even using that pocket knife for a weapon.")
							(Print "(Besides,  you're a lover, not a fighter!" LOW)
						)
						(else
							(Printf "The pimp isn't interested in %s.
								He prefers money."
								((Inventory at: invItem) description?)
							)
						)
					)
				)
				(else
					(super doVerb: theVerb invItem)
				)
			)
		)
	)
)

(instance moose of Feature
	(properties
		x 97
		y 129
		z 36
		nsLeft 80
		nsTop 78
		nsBottom 109
		nsRight 115
 		description "the moose"
		lookStr 		"And you thought that moosehead over Lefty's bar
						was stuffed! "
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "It is not wise to fondle a moose!")
			)
			(verbZipper
				(Print "Jeez, Larry!
					Who has the bigger antlers, you or the moose?!")
			)
			(verbTalk
				(Print "\"How's Rocky?\" you ask.")
				(Print "(There is no reply.)" LOW)
			)
			(verbSmellTaste
				(Print "It smells like an old moosehide--because it IS an old
					moose's hide!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance mooseTail of Prop
	(properties
		view			vPimpRoom
		loop			0
		cel			0
		x				117
		y				156
		z				71
		description "the moose's tail"
		lookStr		"And all this time you thought that moosehead over
						Lefty's bar was stuffed!"
		signal 		ignrAct
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Sorry, Larry! Wrong kind of tail!")
			)
			(else
				(moose doVerb: theVerb invItem)
			)
		)
	)
)

(instance tvLights of Prop
	(properties
		view			vPimpRoom
		loop			1
		cel			0
		x				180
		y				133
		description "the television set"
		priority		13
		signal		(| fixPriOn ignrAct)
		approachX	tvX
		approachY	tvY
		sightAngle	40
	)
	(method (doVerb theVerb invItem)
		(tv doVerb: theVerb invItem)
	)
)

;
;	Features
;

(instance theDoorFeature of Feature
	(properties
		x				1
		y				146
		z				0
		heading		0
		nsLeft		0
		nsTop			84
		nsBottom		142
		nsRight		34
		description "the door"
		approachX	29
		approachY	146
		lookStr		"It's the door from Lefty's bar.
						Its far side looks even better than this side."
		sightAngle	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(HandsOff)
				(curRoom setScript: sToBar)
			)
			(verbTalk
				(Print "\"Yeah? Wadd's da passwoid?\"
					you say to the peephole in the door.")
				(Print "You love playing the part of the pimp in these little
					fantasies of yours, don't you?")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance tv of Feature
	(properties
		x 190
		y 173
		z 37
		heading 0
		nsLeft 167
		nsTop 122
		nsBottom 151
		nsRight 213
 		description "the television set"
		sightAngle 40
		approachX	tvX
		approachY	tvY
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if (Btst fTVOn)
					(switch tvChannel
						(ROGERS
							(Print 141 (+ 1 (* 2 tvChannel)) HIGH)
						)
						(NASTY
							(Print "Get your mind back on the game, Larry!")
						)
						(else
							(Printf "Another boring %s" 141 (+ 1 (* 2 tvChannel)) HIGH)
						)
					)
				else
					(Print "The television screen is blank;
						much like the pimp's mind.")
				)
			)
			(verbDo
				(Print "The television set's knobs are broken off.
					There's no way to turn it off or on, or even change channels.")
			)
			(verbUse
				(switch invItem
					(iRemoteControl
						(cond
							((or
								(> (ego y?) 162)
								(< (ego x?) 183)
							 )
								(Print "The remote control seems ineffectual
									from this angle.")
							)
							((== tvChannel NASTY)
								(Print "Nah. He's having a good time!")
							)
							(else
								(HandsOff)
								(curRoom setScript: sDoChannel)
							)
						)
					)
					(else
						(super doVerb: theVerb invItem &rest)
					)
				)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance boxes of Feature
	(properties
		x 244
		y 92
		z 0
		heading 0
		nsLeft 194
		nsTop 69
		nsBottom 115
		nsRight 294
		sightAngle 40
 		description "some boxes"
		lookStr 		"They're boxes, all right."
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You briefly consider stacking all the boxes into a
					perilous tower, then dangerously climbing up them inch by
					inch to reach the top of the staircase, avoiding the
					pimp and making your way to the hooker that waits above.")
				(Print "Then you remember how clumsy you really are and decide
					you'd rather find a casino and gamble.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance stairs of Feature
	(properties
		x 				143
		y 				14
		z 				0
		heading 		0
;		nsLeft 		21
;		nsTop 		0
;		nsBottom 	29
;		nsRight 		265
		onMeCheck	cLRED
 		description "the staircase"
		sightAngle 	180
		lookStr		"You wonder what palace of earthly delights
						resides above you."
		approachX	stairPimpX	;stairX
		approachY	stairPimpY	;stairY
	)
)
(instance barrel1 of Feature
	(properties
		x 47
		y 120
		z 0
		heading 0
		nsLeft 34
		nsTop 104
		nsBottom 137
		nsRight 61
 		description "the barrel"
		lookStr 		"That's how Lefty buys his fine wines!"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You can't drink from these barrels; they're empty.")
			)
			(verbSmellTaste
				(Print "It smells of stale malt and barley.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance barrel2 of Feature
	(properties
		x 144
		y 123
		z 0
		heading 0
		nsLeft 126
		nsTop 110
		nsBottom 136
		nsRight 163
 		description "the barrel"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(barrel1 doVerb: theVerb invItem)
	)
)

(instance thePeephole of Feature
	(properties
		x 12
		y 148
		z 38
		heading 0
		nsLeft 6
		nsTop 108
		nsBottom 113
		nsRight 18
 		description "the peephole"
		lookStr 		"How interesting. This door has a small peephole.
						You suspect it may be used to communicate with
						someone on the other side."
		sightAngle 40
		approachX	29
		approachY	146
	)
	(method (doVerb theVerb invItem)
		(theDoorFeature doVerb: theVerb invItem)
	)
)

