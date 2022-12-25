;**
;**	Logics for Room 130 -- Lefty's Bathroom
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford
;**
;**	Last Update:  June 17, 1991
;**


(script# 130)

(public
	rm130		0
)

;**	Music of bar fades upon entry.  Water drips throughout.
;**	Bar music resumes soft upon opening door to leave.


(define	sDripping				130)
;(define	sWashHands				131)
(define	sNewspaperShuffle		132)
;(define	sFart						133)
(define	sToiletFlush			134)
(define	sToiletGusher			135)
;(define	sDogPissing				102)
;(define	sDoorOpen				801)
;(define	sDoorClose				802)
(define  sDrip						136)


(local
	graffitiCount
	choice
	paperCycles
	lookedSink
)

(define pBathroom					130)
(define vBathroom					130)
	(define lDoor					0)
	(define lFlow					1)
	(define lFlow2					2)
	(define lDrip					3)
(define vLarryShitting			131)
	(define lReadPaper			0)
	(define lLookSink				1)
	(define lReachSink			2)
	(define lDropPants			3)
	(define lTakePiss				4)
	(define lDrown					5)
	(define lSparkle				6)
	(define lDeath					7)
(define vOverflow					132)

(define sitting (and (== (ego view?) vLarryShitting) (or (== (ego loop?) 0) (== (ego loop?) 1))))
(define toiletX 	171)
(define toiletY 	111)
(define sinkX		190)
(define sinkY		144)
(define doorX		141)
(define doorY		112)
(define handleX	187)
(define handleY	113)
(instance rm130 of LLRoom
	(properties
		picture	130
	)
	(method (init &tmp foo)
		(= foo ForwardCounter)
		(LoadMany SOUND
			sDripping
;			sWashHands
			sNewspaperShuffle
			sFart
			sToiletFlush
			sToiletGusher
			sDogPissing
			sDoorOpen
			sDoorClose
			sDrip
		)
		(theMusic3
			loop:			1,
			vol:			127,
			flags:		1,
		)
		(LoadMany VIEW
			vBathroom
			vLarryShitting
			vOverflow
		)
;		(curRoom addObstacle:
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	125 113 95 120 117 165 190 148 190 143 182 143 182 137
;						223 137 224 109 194 108 184 114 160 114 155 108 130 111 130 0
;						319 0 319 189 0 189 0 0 125 0,
;				yourself
;			)
;		)
		; Polygon Editor 1.11
		; Dynamic Obstacles : Picture 130
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	125 113 95 120 117 165 190 148 190 143 182 143 182 137
						223 137 224 109 194 108 184 114 160 114 155 110 130 111 130 0
						319 0 319 189 0 189 0 0 125 0,
				yourself
			)
		)
		(ego
			posn:					138 113,
			observeControl:	cWHITE,
			init:					,
			actions:				egoActions,
		)
		(eHead actions: egoActions)
		(if (ObjInRoom iRing)
			(theSparkle
				cycleSpeed:		howFast,
				init:,
				approachVerbs:	objectVerbs verbLook,
			)
		)

		(toilet
			init:,
			approachVerbs:	objectVerbs,
		)
		(sink
			init:,
			approachVerbs:	objectVerbs verbLook,
		)
		(graffiti 	init:)

		(super init:)
		(theMusic2
			number:		sDrip,
			loop:			1,
			vol:			127,
			flags:		1,
		)
		(door
			init:				,
			setCel:			6,
			setScript:		sCloseDoor,
			approachVerbs:	objectVerbs,
		)
		(theHandle
			init:,
			approachVerbs:	objectVerbs
		)
		(faucet
			init:,
			setScript:		sDoDrips
		)
	)
	(method (doit)
		(super doit: &rest)
		(if (and sitting
					(ego mover?))
			(curRoom setScript: 0)
			(Print "Carefully finishing your duties, you wipe and toss the
				newspaper behind the toilet for the next user." HIGH)
			(Bset fToiletPaperOnShoe)
			(NormalEgo 2)
			(if (!= (CueObj client?) toilet)
				(ego setMotion: PolyPath
					((CueObj client?) approachX?)
					(+ (ego z?) ((CueObj client?) approachY))
					CueObj
				)
			else
				(ego setMotion: PolyPath
					((User curEvent?) x?)
					((User curEvent) y?)
				)
			)
		)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "You are in Lefty's one and only restroom.
	 				The stench is overpowering, there is graffiti on the walls,
					and you doubt that the sink was clean even when it was
					installed. Even roaches can't survive in this place!")
				(if (ObjInRoom iRing)
					(Print "Water drips merrily down the drain in Lefty's
						lovely sink.")
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
	(method (newRoom n)
		(theMusic2 fade:)
		(super newRoom: n)
	)
)
;
;	Classes
;

(class egoActions of Code
	(methods
		doVerb
	)
	(method (doVerb theVerb invItem)
		(return
			(switch theVerb
				(verbZipper
					(toilet doVerb: verbZipper)
				)
			)
		)
	)
)

;
;   Scripts
;

(instance sTakeCrap of Script
	(method (doit)
		(super doit: &rest)
		(if register
			(-- register)
		else
			(= register (Random 30 50))
			(ego
				setCel:		0,
				setCycle: 	ForwardCounter (Random 1 3)
			)
		)
		(if (== (ego cel?) 1)
			(theMusic3
				number:		sNewspaperShuffle,
				play:,
			)
		)
	)
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					egoSpeed:,
					view:			vLarryShitting,
					normal:		FALSE,
					setLoop:		0,
					setCel:		2,
				)
				(= register 5)
				(= cycles 3)
			)
			(
				(Print "You grab an ancient copy of the \"Ball Street Journal,\"
					and settle in to \"sit and think!\"" HIGH)
				(ego setCycle: EndLoop)
				(= register (Random 20 40))
				(Points pPoopedInCan ptsPoopedInCan)
				(= seconds 8)
			)
			(
				(Print "You feel a great relief!" HIGH)
				(= seconds 5)
			)
			(
				(fartNoise play:)
				(= seconds 4)
			)
			(
				(Print "What's that aroma?" HIGH)
			)
		)
	)
)

(instance sTakePiss of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					egoSpeed:,
					view:			vLarryShitting,
					setLoop:		lDropPants,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(ego
					setLoop:		lTakePiss,
					setCel:		0,
					setCycle:	Forward,
				)
				(= cycles 8)
			)
			(
				(theMusic3
					number:		sDogPissing,
					setLoop:		1,
					play:			self,
				)
				(Print "Ahhhhhhh." HIGH
					#dispose:
				)
			)
			(
				(= seconds 3)
			)
			(
				(Cls)
				(ego
					setLoop:		lDropPants,
					setCel:		255,
					setCycle:	BegLoop self
				)
			)
			(
				(NormalEgo 3)
				(HandsOn)
				(if
					(> (GetDistance (ego x?) (ego y?) (toilet approachX?) (toilet approachY?)) 5)
					(Print "\"Hey, you missed the toilet!\"" HIGH)
				)
				(self dispose:)
			)
		)
	)
)

(instance sFlood of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(theMusic3
					number:			sToiletFlush,
					loop:				1,
					play:
				)
				(= cycles 20)
			)
			(
				(Print "Realizing your mistake, you quickly jiggle the handle,
					attempting to stem the onrushing tide of water." HIGH)
				(Print "It doesn't work." HIGH)
				(theMusic3
					number:			sToiletGusher,
					loop:				-1,
					play:
				)
				(overflow
					init:,
					cycleSpeed:	(+ 3 howFast),
					setCycle:	EndLoop self
				)
				(ego
					egoSpeed:,
					setHeading:		180
				)
			)
			(
				(overflow
					setLoop:		lFlow2,
					setCycle:	Forward,
				)
				(flood
					init:,
				)
				(= cycles 2)
			)
			(
				(++ register)
				((flood new:)
					init:,
					cel:		register,
					y:			(- (flood y?) (* 7 register)),
					stopUpd:,
				)
				(if (< register 8)
					(-- state)
				)
				(= cycles 5)
			)
			(
				(overflow dispose:)
				(Print "Your life passes before your eyes.")
				(Print "(You doze briefly.)" LOW)
				(ego
					view:			vLarryShitting,
					loop:			lDrown,
					x:				(+ (ego x?) 10),
					y:				(- (ego y?) 20),
					cycleSpeed: 4,
					setCycle:	Forward
				)
				(= cycles 1)
			)
			(
				(++ register)
				((flood new:)
					init:,
					cel:		register,
					y:			(- (flood y?) (* 7 register)),
					stopUpd:,
				)
				(if (< register 16)
					(-- state)
				)
				(= cycles 5)
			)
			(
				(= seconds 3)
			)
			(
				(Death vLarryShitting lDeath)
				(Format @deathTitleStr "You feel flushed!")
				(GameOver "Sometimes good manners are a bad idea, eh, Larry?")
			)
		)
	)
)

(instance sGetRing of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					egoSpeed:,
					view:			vLarryShitting,
					setLoop:		lLookSink,
					setCel:		0,
					z:				10,
					setCycle:	EndLoop self
				)
			)
			(
				(ego
					loop:			lReachSink,
					setCycle:	Forward,
				)
				(= seconds 3)
			)
			(
;				(theSparkle
;					z:			1000,
;					dispose:
;				)
				(Points pGotRing ptsGotRing)
				(ego
					get: 			iRing,
					setCel:		255,
					setLoop:		lLookSink,
					setCycle:	BegLoop self
				)
			)
			(
				(Print "Got it!\n
					\n
					You briefly consider an attempt to find the ring's owner,
					but return to your normal self and pocket it instead."
					HIGH
				)
				(ego z: 0)
				(NormalEgo 0)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sSparkle of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(client setCycle: EndLoop)
				(= seconds (Random 5 20))
			)
			(
				(self init:)
			)
		)
	)
)

(instance sCloseDoor of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(door
					setCycle:	CycleTo 5 -1 self
				)
			)
			(
				(theMusic3
					number:		sDoorClose,
					play:
				)
				(door
					setCycle:	BegLoop self
				)
			)
			(
				(self dispose:)
			)
		)
	)
)

(instance sDoDrips of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= seconds (Random 2 4))
			)
			(
				(faucet setCycle: BegLoop self)
			)
			(
				(theMusic2 play:)
				(self init:)
			)
		)
	)
)


;
;	Props
;

(instance door of Prop
	(properties
		view			vBathroom
		loop			0
		cel			0
		x				114
		y				112
		description "the door"
		priority		0
		signal		(| ignrAct fixPriOn)
		approachX	doorX
		approachY	doorY
	)
	(method (cue)
		(super cue:)
		(curRoom newRoom: 120)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(HandsOff)
				(theMusic3
					number:		sDoorOpen,
					play:,
				)
				(self setCycle: EndLoop self)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)

)

(instance theSparkle of Prop
	(properties
		view			vLarryShitting
		loop			lSparkle
		x				206
		y				127
		priority		12
		description	"the ring"
		approachX	sinkX
		approachY	sinkY
		signal		(| fixPriOn ignrAct)
	)
	(method (doVerb theVerb invItem)
		(sink doVerb: theVerb invItem)
	)
)

(instance overflow of Prop
	(properties
		view			vBathroom
		loop			lFlow
		x				171
		y				97
		signal		ignrAct
	)
)

(instance flood of View
	(properties
		view		 	vOverflow
		x				170
		y				146
		priority		1
		signal		(| ignrAct fixPriOn)
	)
)

(instance faucet of Prop
	(properties
		view		  	vBathroom
		loop			lDrip
		x				209
		y				129
		priority		pLGREEN
		signal		(| ignrAct fixPriOn)
		description "the sink"
		sightAngle	40
	)
	(method (cue)
		(super cue:)
		(if (ObjInRoom iRing)
			(= lookedSink TRUE)
			(Print "Why, look!
				Some woman removed her diamond ring to wash her hands,
				and forgot to take it with her." HIGH)
			(theSparkle setCycle: BegLoop)
		else
			(Print "A small, neatly lettered sign reminds you:\n
				\n
				\"Since we can't clean up after every customer,
				please rinse out this sink when you are finished.\"" HIGH)
			(Print "(Evidently, you are the first customer who can read.)" LOW)
		)
	)
)


;**
;**	Features
;**

(instance toilet of Feature
   (properties
      x 					182
      y 					91
      z 					0
      heading 			0
      nsLeft 			164
      nsTop 			76
      nsBottom 		107
      nsRight 			200
      description 	"the toilet"
      lookStr 			"It's not a pretty sight!"
      sightAngle 		40
		approachX		toiletX
		approachY		toiletY
   )
   (method (doVerb theVerb invItem)
      (switch theVerb
         (verbDo
				(if
					(not sitting)
					(curRoom setScript: sTakeCrap)
				else
					(Print "NOW??" HIGH)
				)
         )
         (verbTalk
            (Print  "You briefly consider yodeling in the stagnant water.")
         )
         (verbUse
            (switch invItem
               (iBreathSpray
                  (Print "It would help, but not much.")
               )
               (iWallet
                  (Print "You dip your wallet into the slimy bowl just for the
							feeling you get when you slide it back in your pocket!")
               )
               (else
                  (super doVerb: theVerb invItem &rest)
               )
            )
         )
			(verbZipper
				(HandsOff)
				(curRoom setScript: sTakePiss)
			)
			(verbSmellTaste
				(Print "My, my. Aren't we the little sick-o!")
			)
         (else
            (super doVerb: theVerb invItem &rest)
         )
      )
   )
)
(instance sink of Feature
	(properties
		x 202
 		y 146
		z 20
		heading 0
		nsLeft 185
		nsTop 119
		nsBottom 137
		nsRight 220
 		description "the sink"
		sightAngle 40
		approachX	sinkX
		approachY	sinkY
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if (not sitting)
					(ego setHeading: (GetAngle (ego x?) (ego y?) (self x?) (self y?)) faucet)
				else
					(faucet cue:)
				)
			)
			(verbDo
				(cond
					(sitting
						(Print "NOW??")
					)
					((or	(not (ObjInRoom iRing))
							(and	(ObjInRoom iRing)
									(not lookedSink)))
						(Print "You quickly wash your hands,
							then realize there are no paper towels." HIGH)
					)
					(else
						(HandsOff)
						(curRoom setScript: sGetRing)
					)
				)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance graffiti of Feature
	(properties
		x 193
		y 58
		z 0
		heading 0
		nsLeft 146
		nsTop 27
		nsBottom 90
		nsRight 240
 		description "the walls"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You quickly memorize some of the graffiti." HIGH)
			)
			(verbLook
				(switch graffitiCount
					(0
						(Print "Lefty's restroom wall is filled with clever reading
							material; enough for you to read for a long, long time."
							LOW
						)
					)
					(1
						(Print "\"Scott me up, Beamie!\"" LOW)
					)
					(2
						(Print "\"Attention arcade game players:
							please don't eat the urinal cakes!\"" LOW)
					)
					(3
						(Print "\"It takes leather balls to play rugby!\"" LOW)
					)
					(else
						(Points pKnowsPassword ptsKnowsPassword)
						(Print "Say, here's an interesting one:\n\n
							\"The password is: `Ken sent me.'\"" LOW)
						(if (ObjInRoom iGraffiti)
							(Print "You decide to write it down and take it with
								you wherever you go.
								A guy never knows when a good password will come
								in handy!")
							(ego get: iGraffiti)
						)
					)
				)
				(++ graffitiCount)
			)
			(verbSmellTaste
				(Print "Lefty must be running low on air freshener." HIGH)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance theHandle of Feature
	(properties
		x 193
		y 109
		z 28
		heading 0
		nsLeft 185
		nsTop 78
		nsBottom 85
		nsRight 201
 		description "the flusher"
		sightAngle 40
		lookStr		"This toilet has been provided with a handle for those
						customers of Lefty's sophisticated enough to
						understand its operation."
		approachX	handleX
		approachY	handleY
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(HandsOff)
				(curRoom setScript: sFlood)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance fartNoise of Sound
	(properties
		number		sFart
		loop			1
		vol			127
		flags			1
	)
)
