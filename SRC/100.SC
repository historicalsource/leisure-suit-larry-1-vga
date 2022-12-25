;**
;**	Logics for Room 100 -- Lefty's Bar Exterior
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford &
;**
;**	Last Update:  June 21, 1991
;**


(script# 100)

(public
	rm100		0
)

(local
	clapTimer
)

(define CLAPTIME 				150)

(define pBarExterior				100)
(define vBarExterior				100)
(define vLarryHotCrotch			101)
	(define lCrotchBlowup		0)
	(define lCrotchMissing		1)
	(define lCrotchEyes			2)
	(define lCondom				3)
;(define vJail						103)
(define vPoliceman				104)

(define	sNuclearHiss			105)		;**	100 Lefty's Exterior
(define	sNuclearBlowup			101)
(define	mPoliceman				104)
;(define	sDoorOpen				801)
;(define	sDoorClose				802)
;(define  vLarryHailing			810)	;not loaded

(define 	canX 305)
(define  canY 140)
(define  matX 89)
(define  matY 142)

(instance rm100 of LLRoom
	(properties
		picture	100
		horizon	120
		north		110
		east		170
	)

	(method (init)
		(if (== debugging 2)
			(Bset fWearingRubber)
		)
		(if (== debugging 1)
			(Bset fHasClap)
		)
		(Load VIEW
			vBarExterior
		)
		(LoadMany SOUND
			sDoorOpen
			sDoorClose
		)
;		(curRoom addObstacle:
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	0 0 319 0 319 140 300 140 300 124 296 124 296 146 98 146
;						99 137 82 137 72 146 9 146 26 116 0 116,
;				yourself
;			)
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	28 150 55 150 55 154 28 154,
;				yourself
;			)
;		)
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	0 0 319 0 319 140 300 140 300 124 296 124 296 146 98 146
						99 137 82 137 72 146 9 146 0 116,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	28 150 55 150 55 154 28 154,
				yourself
			)
		)
		(switch prevRoomNum
			(110
				(HandsOff)
				(self setScript: fromBarScript)
			)
			(160
				(ego
					x:				300,
					y: 			155,
					setHeading:	180
				)
			)
			(170
				(if (> (ego x?) 160)
					(self west: 170)
					(self east: NULL)
				)
				(ego y: 157)
			)
			(200
				NULL
			)
			(else
				;oldstuff: play title music
;				(if (or (== prevRoomNum 710)
;						  (== prevRoomNum 720))
;					(self style: DISSOLVE)
;				else
					(self style: IRISOUT)
;				)
				(HandsOn)
				(theIconBar enable:)
				(ego
					x:				140,
					y:				149,
					setHeading:	180
				)
			)
		)
		(ego
			init:		,
		)

		(super init:)

		(self setRegions: SIDEWALK)

		(door
			cycleSpeed:		howFast,
			init:				,
			approachVerbs:	verbDo,
			stopUpd:
		)

		(if (< (LarryHours) 4)
			(leftySign
				cycleSpeed:	(+ 3 howFast),
				setCycle:	Forward,
				init:,
			)
			(hotelSign
				cycleSpeed:	30,
				setCycle:	Forward,
				init:,
			)
			(glass
				cycleSpeed:	(+ 2 howFast),
				setCycle:	Forward,
				init:,
			)
		)

		(taxiSign
			approachY:		(- 169 10),					;100's flatY in sidewalk
			approachX:		(ego x?),
			approachVerbs:	verbDo verbTalk,
			init:,
		)
		(pole init:)
		(theWindow init:)
		(upperWindows init:)
		(building init:)
		(doormat
			init:,
			approachVerbs:	objectVerbs,
		)
		(cans
			init:,
			approachVerbs:	objectVerbs
		)
		(if
			(Btst fHasClap)
			(Load VIEW vLarryHotCrotch)
			(LoadMany SOUND
				sNuclearHiss
				sNuclearBlowup
			)
			(= clapTimer CLAPTIME)
		)

		(if
			(and
				(not (Btst fStiffedCabbie))
				(Btst fWearingRubber)
			)
			(Load VIEW vPoliceman)
			(Load SOUND mPoliceman)
			(aCop
				init: 		,
;				cycleSpeed:	(theGame egoMoveSpeed?),
;				moveSpeed:	(theGame egoMoveSpeed?),
				setCycle:	Walk,
				x:				(if (== prevRoomNum 160) -20 else 340),
				setScript:	sChaseEgo
			)
		)
		(self east: 170)
		(self west: NULL)

	)

	(method (doit &tmp n)
		;so ego will walk straight down screen to curb
		(taxiSign approachX: (ego x?))
		(if clapTimer
			(if (cast contains: (ScriptID SIDEWALK 2))
				(= clapTimer 1)
			)
			(if (and
					(== clapTimer 1)
					(!= (curRoom script?) sDieOfTheClap)
					(!= (curRoom script?) (ScriptID LLROOM 1))
				)
				(HandsOff)
				(curRoom setScript: sDieOfTheClap)
			)
			(-- clapTimer)
		)
		(super doit:)
		(cond
			(script)
			((< (ego x?) 2)
				(HandsOff)
				(curRoom setScript: sWalkWest)
			)
			((< (ego y?) 135)
				(if
					(< (ego x?) 160)
					(= n 170)
				else
					(= n 160)
				)
				(if (not (Btst fHasClap))
					(super newRoom: n)
				else
					(HandsOff)
					(curRoom setScript: sDieOfTheClap)
				)
			)
			(else
				NULL
			)
		)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "You are outside Lefty's bar.
					Isn't it wonderful what can be done with a little neon?")
				(Print "You notice a sign on that utility pole.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

;
;	SCRIPTS
;

(instance sWalkWest of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					setMotion:	MoveTo -20 (ego y?) self
				)
			)
			(
				(curRoom newRoom: 170)
				(self dispose:)
			)
		)
	)
)

(instance fromBarScript of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(door
					setCel:		255
				)
				(ego
					posn:			93 125,
					setMotion:	MoveTo 92 148 self
				)
			)
			(
				(soundFX
					number:		sDoorClose,
					loop:			1,
					play:
				)
				(door
					setCycle:	BegLoop self
				)
			)
			(
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sEnterDoor of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
 					setPri:		(+ (door priority?) 1)
				)
				(soundFX
					number:		sDoorOpen,
					loop:			1,
					play:
				)
				(door
					setCycle:	EndLoop self
				)
			)
			(
				(ego
					setMotion:	MoveTo 97 130 self
				)
			)
			(
				(ego setPri:	(- (door priority?) 1))
				(= cycles 3)
			)
			(
				(soundFX
					number:		sDoorClose,
					loop:			1,
					play:
				)
				(ego setMotion: 0)
				(door
					setCycle:	BegLoop self
				)
			)
			(
				(curRoom newRoom: 110)
			)
		)
	)
)

(instance sChaseEgo of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(aCop
					setMotion:	PolyPath (ego x?) (ego y?)
				)
				(= cycles 5)
			)
			(
				(cond
					((and (not register)
						(< (aCop distanceTo: ego) 160))
						(= register 1)
						(soundFX play:)
					)
					((and (== register 1)
						(< (aCop distanceTo: ego) 80))
						(= register 2)
						(Print "\"STOP, PERVERT!!!\" shouts the cop.\n\n
							\"I wonder who he's yelling at,\" you think." HIGH)
					)
				)
				(if (> (aCop distanceTo: ego) 50)
					(self init:)
				else
					(= cycles 1)
				)
			)
			(
				(HandsOff)
				(aCop setMotion: 0)
				(Print "\"Caught you, pervert!\" the cop shouts.
					\"We don't like people exposing themselves around this town,
					especially wearing one of those!\"" HIGH)
				(= seconds 3)
			)
			(
				(Print "Glancing down, you realize what he is talking about.
					Your face flushes." HIGH)
			 	(= seconds 3)
			)
			(
				(Death vJail 0 1)
				(Format @deathTitleStr "What is that?!")
				(GameOver "Next time, if you insist on wearing your \"lubber\" in
					public, you might at least zip your fly!")
			)
		)
	)
)

(instance sDieOfTheClap of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(Print "Suddenly,
					you have a warm feeling in the front of your pants." HIGH)
				(if
					(or (== (ego view?) vLarryWalking)
						(== (ego view?) vLarryStopping))
		 			(ego
						egoSpeed:,
						setHeading: 90 self
					)
				else
					(= cycles 1)
				)
			)
			(
				(ego
					view:			vLarryHotCrotch,
					cycleSpeed:	6,
					setLoop:		lCrotchBlowup,
					setCel:		0,
					setCycle:	CycleTo 2 1 self
				)
			)
			(
				(theMusic3
					number:		sNuclearHiss,
					flags:		1,
					setLoop:		1,
					play:			127
				)
				(Print "It appears that the hooker gave you a little more than
					you bargained for!"
					HIGH)
				(ego
					setCycle:	EndLoop self
				)
			)
			(
				(theMusic3
					number:		sNuclearBlowup,
				 	setLoop:		1,
					play:
				)
				(ego
					setLoop:		lCrotchMissing,
					setCel:		0,
				)
				(= seconds 3)
			)
			(
				(ego
					setCycle:	EndLoop self
				)
			)
			(
				(egoEyes
					init:,
					x:				(ego x?),
					y:				(ego y?),
					setPri:		(+ (ego priority?) 1),
					cycleSpeed:	(* 2 howFast),
					setCycle:	Forward,
				)
				(= seconds 3)
			)
			(
				(Death vLarryHotCrotch lCondom)
				(Format @deathTitleStr "You're no Trojan, Larry!")
				(GameOver "While life may be possible, to you
					it is no longer worth living.")
			)
		)
	)
)

;
;	PROPS
;


(instance leftySign of Prop
	(properties
		view			vBarExterior
		loop			0
		cel			1
		x				111
		y				144
		z				95
		priority		14
		description "the neon sign"
		lookStr		"You are outside Lefty's bar.
						Isn't it wonderful what can be done with a little neon?"
		signal		(| fixPriOn ignrAct ignrHrz)
		detailLevel	3
	)
)

(instance hotelSign of Prop
	(properties
		view			vBarExterior
		loop			2
		cel			0
		x				283
		y				144
		z				144
		priority		14
		description "the neon sign"
		lookStr		"The \"L\" in this place must be hot!"
		signal		(| fixPriOn ignrAct)
		detailLevel	2
	)
)

(instance glass of Prop
	(properties
		view			vBarExterior
		loop			1
		cel			1
		x				180
		y				144
		z				106
		priority		14
		description "the neon sign"
		lookStr		"This is the cleanest glass in Lefty's collection."
		detailLevel	1
		signal		(| fixPriOn ignrAct)
	)
)

(instance door of Prop
	(properties
		view			vBarExterior
		loop			3
		cel			0
		x				84		;73
		y				120
		z				-15
		approachX	97
		approachY	135	;131
		signal		ignrAct
		description "Lefty's door"
		lookStr		"Nice tuck and roll job, eh?"
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(cond
					((> 2 (LarryHours) 10)
						(Print "You can't. The door is securely locked.")
						(Print "Lefty's closes at 3:00 a.m.")
					)
					((aCop script?)
						(Print "You won't be safe in there, Larry!")
					)
					(else
						(HandsOff)
						(curRoom setScript: sEnterDoor)
					)
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance egoEyes of Prop
	(properties
		view			vLarryHotCrotch
		loop			lCrotchEyes
		z				33
	)
)

(instance theWindow of Feature
	(properties
		x 211
		y 142
		z 57
		heading 0
		nsLeft 155
		nsTop 67
		nsBottom 103
		nsRight 268
		description "Lefty's window"
		sightAngle 40
 		lookStr		"Lefty's windows are too dirty to see through."
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You rub your fingerprints into Lefty's window!")
				(Print "(You hope Lefty doesn't see you!)" LOW)
			)
			(verbUse
				(switch invItem
					(iHammer
						(Print "Nah. Lefty would probably break both your legs!")
					)
					(else
						(super doVerb: theVerb invItem)
					)
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance taxiSign of Feature
	(properties
		x 42
		y 152
		z 72
		heading 0
		nsLeft 36
		nsTop 73
		nsBottom 87
		nsRight 49
      description "the taxi sign"
      sightAngle 40
		lookStr
		"High up that telephone pole sits a lonely sign that reads,
		\"Taxi Stand.\""

   )
	(method (doVerb theVerb invItem)
      (switch theVerb
         (verbDo
            (Print "You whistle loudly for a taxi.")
				(HandsOff)
				(curRoom setScript: (ScriptID SIDEWALK 3))
         )
         (verbTalk
            (Print "You shout, \"YO!  TAXI!!\"")
				(HandsOff)
				(curRoom setScript: (ScriptID SIDEWALK 3))
         )
         (else
            (super doVerb: theVerb invItem &rest)
         )
      )
   )
)


(instance pole of Feature
	(properties
		x 38
		y 150
		z 0
		heading 0
		nsLeft 31
		nsTop 0
		nsBottom 152
		nsRight 45
		description "the utility pole"
		lookStr "It's tall and skinny; built like a pole!"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You wouldn't touch that pole with a 10-foot pole!")
			)
			(verbUse
				(switch invItem
					(iRibbon
						(Print "You COULD tie yourself to the pole,
							but what would that accomplish?")
					)
					(iHammer
						(Print "\"Take that, you mean old pole,\" you shout!")
					)
					(else
						(super doVerb: theVerb invItem)
					)
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)
(instance upperWindows of Feature
	(properties
		x 158
		y 142
		z 126
		heading 0
		nsLeft 48
		nsTop 0
		nsBottom 33
		nsRight 268
		description "the windows"
		lookStr "You cleverly notice that light shines from only one
					of the windows above Lefty's.
					Could there be someone up there?"
		sightAngle 80
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "There's no way to climb up there from here.")
			)
			(verbTalk
				(Print "\"Hark, Juliet! What light from yonder window breaks?
					Be dat you up 'dere?\"")
				(Print "(You were always weak at memorization!)" LOW)
			)
			(verbUse
				(Print "That's far too high for you to hurl anything, Larry!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance building of Feature
   (properties
      x 146
      y 92
      z 0
      heading 0
      nsLeft 13
      nsTop 42
      nsBottom 142
      nsRight 280
      description "Lefty's bar"
      sightAngle 85
   )
   (method (doVerb theVerb invItem)
      (switch theVerb
			(verbLook
				(if (> 2 (LarryHours) 10)
					(Print "Lefty's closes at 3:00 a.m.")
				else
					(Print "Considering the looks of this place,
						you decide that next time you'd be better off to spend
						a little more money on your computer games!")
				)
			)
         (else
            (super doVerb: theVerb invItem &rest)
         )
      )
   )
)


(instance aCop of Actor
	(properties
		view	  		vPoliceman
		loop	  		0
		cel	  		0
		y		  		155
		xStep			6
		signal		ignrAct
		description	"that cop"
		lookStr		"He appears to be chasing someone!"
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbTalk
				(Print "\"STOP, PERVERT!!!\" shouts the cop.\n\n
					\"I wonder who he's yelling at?\" you think.")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)


(instance doormat of Feature
	(properties
		x 				90
		y 				139
		z 				0
		heading 		0
		nsLeft 		73
		nsTop 		134
		nsBottom 	144
		nsRight 		108
 		description "the doormat"
		lookStr 		"\"Welcome\""
		sightAngle 	40
		approachX	matX
		approachY	matY
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Amazed by your own cleverness,
					you lift the door mat and discover...")
				(Print "...absolutely nothing!")
				(Print "(What did you expect? A key?)" LOW)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance cans of Feature
	(properties
		x 307
		y 118
		z 0
		heading 0
		nsLeft 295
		nsTop 99
		nsBottom 137
		nsRight 319
 		description "the trash cans"
		lookStr "Can can. "
		sightAngle 40
		approachX	canX
		approachY	canY
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Now that you're here,
					you decide you don't really want to look inside. ")
			)
			(verbSmellTaste
				(Print "Lefty's wife, Linda, must have made her famous tuna 
					casserole again!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance soundFX of Sound
	(properties
		number			mPoliceman
		flags 			1
	)
)

