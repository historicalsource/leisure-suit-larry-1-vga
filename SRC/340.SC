;**
;**	Logics for Room 340 -- Generic Floors
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford 
;**
;**	Last Update:  June 21, 1991
;**
;**	UpElevator > start up, then call GoUp or ArriveUp
;**	DownElevator > start down, then call GoDown or ArriveDown
;**	from 330 > call GoUp or ArriveUp
;**	from 350 > call GoDown or ArriveDown
;**	GoUp > call GoUp or ArriveUp
;**	GoDown > call GoDown or ArriveDown
;**	ArriveUp
;**	ArriveDown
;**

(script# 340)

(public
	rm340		0
)

;(define	sElevatorFloorDing	340)
;(define	sKnocking				341)
;(define	mCasino 					310)

(define startZ 84)
(define startX 240)
(define zGap 18)
(define xGap 43)
(define elevatorX 	156)
(define elevatorY		133)	;137
(define yJump			16)
(define floorX			160)
(define floorY			86)
(define suiteDoorX 	40)
(define suiteDoorY 	138)
(define inElevator	(and (== (ego x?) elevatorX) (== (ego y?) elevatorY)))


(local
	floorWant
)

(procedure
	FadeIt
)

(class ElevatorNumbers of View
	(properties
		view			vNumbers
		loop			lNumbers
		y				180
		priority		15
		signal		(| ignrAct fixPriOn)
		description "a button"
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(self setLoop: (+ (self loop?) 1))
				(Animate (cast elements?) FALSE)
				(= floorWant (self cel?))
				(if (== floorTo floorWant)
					(HandsOff)
					(self setLoop: (- (self loop?) 1))
					(Animate (cast elements?) FALSE)
					(curRoom setScript: sNoRide)
				else
					(HandsOff)
					(ego illegalBits: $0000)
					(if (> floorWant floorTo)
						(HandsOff)
						(curRoom setScript: sUpElevator)
					else
						(HandsOff)
						(curRoom setScript: sDownElevator)
					)
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance rm340 of LLRoom
	(properties
		picture	340
		style		FADEOUT
	)
	(method (init)
		(if debugging
			(Bset fFawnInRoom)
			(ego get: iPocketKnife)
		)
		(if (> debugging 1)
			(Bset fOrderedWine)
		)
		(LoadMany VIEW
			vGenericFloor
			vElevator
			vNumbers
			vLarryInElevator
			vLarryGetting
		)
		(LoadMany SOUND
			sElevatorFloorDing
			mCasino
			sKnocking
		)
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	0 0 319 0 319 189 316 189 316 154 222 130 90 128 3 144 3 189
						0 189,
				yourself
			)
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	35 158 72 158 72 164 35 164,
;				yourself
;			)
			((Polygon new:)
				type: PBarredAccess,
				init:	150 135 150 146 164 146 164 135 211 135 231 140 206 161
						109 161 95 145 95 138 109 136,
				yourself
			)
		)
		(theMusic3
			loop:			1,
			vol:			127,
			flags:		1,
		)
		(switch prevRoomNum
			(330
				(floorNumber cel: 2)
				(if (== floorTo 2)
					(HandsOff)
					(self setScript: sArriveUp)
				else
					(= floorWant floorTo)
					(= floorTo 2)
					(HandsOff)
					(self setScript: sGoUp)
				)
			)
			(350
				(theMusic
					number:		mCasino,
					loop:			-1,
					flags:		1,
					play:,
				)
				(floorNumber cel: 7)
				(if (== floorTo 7)
					(HandsOff)
					(self setScript: sArriveDown)
				else
					(= floorWant floorTo)
					(= floorTo 7)
					(HandsOff)
					(self setScript: sGoDown)
				)
			)
			(390
				(theMusic
					number:		mCasino,
					loop:			-1,
					flags:		1,
					play:			90,
				)
				(floorNumber cel: 4)
				(ego
					x: 		suiteDoorX,
					y:			suiteDoorY,
					loop:		0,
					normal: 	FALSE,
				)
				(HandsOff)
				(elevator stopUpd:)
				(curRoom setScript: sFrom390)
			)
			(else		;TP
				(ego posn: 160 175)
				(= floorTo 4)
				(suiteDoor
					init:,
					approachVerbs:	objectVerbs,
				)
				(theHeart z: 0)
			)
		)
		(ego init:)
		(super init:)
		(if (== prevRoomNum 390)
			(suiteDoor
				init:,
				approachVerbs:	objectVerbs,
			)
		else
			(door1
				init:,
				approachVerbs:	objectVerbs
			)
			(theHeart z: 1000)
		)
		(elevator
			cycleSpeed:		(+ howFast 1),
			moveSpeed:		(+ howFast 1),
			init:,
			approachVerbs:	objectVerbs,
		)
		(floorNumber init:)
		(theHeart
			init:,
			approachVerbs:	objectVerbs
		)
		(door2
			init:,
			approachVerbs:	objectVerbs
		)
		(door3
			init:,
			approachVerbs:	objectVerbs
		)
		(door4
			init:,
			approachVerbs:	objectVerbs
		)
		(door5
			init:,
			approachVerbs:	objectVerbs
		)
		(door6
			init:,
			approachVerbs:	objectVerbs
		)
;		(ashtray init:)
		(plants init:)
		(elevatorShaft
			init:,
			approachVerbs:	objectVerbs
		)
	)
	(method (doit)
		(super doit: &rest)
		(if
			(ego mover?)
			(ego illegalBits: $8000)
			(if (cast contains: numberPad)
				(numberPad dispose:)
				(cast eachElementDo: #perform disposeNumbers)
				(if
					(== (curRoom script?) sElevatorScript)
					(curRoom setScript: 0)
				)
			)
		)
		(cond
			((SteppedOn ego cCYAN)
				(theMusic fade: 127 5 5 0)
			)
			((SteppedOn ego cRED)
				(theMusic fade: 90 5 5 0)
			)
		)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "You are in the Casino's hotel.
					Doors line every wall, many with \"Do Not Disturb\" signs
					hanging from their doorknobs.")
				(if (== floorTo 4)
					(Print "There's a door with a heart on it to the west.")
				else
					(if inElevator
						(Print "There are buttons labeled \"one\" through \"eight.\"")
					else
						(return TRUE)
					)
				)
			)
			(verbTalk
				(Print "\"strangers in the night, da da da dee da...\"")
				(Print "(How you love Muzak.)" LOW)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
	(method (newRoom n)
		(if (== n 350)
			(theMusic fade:)
		)
		(super newRoom: n)
	)
)

;
;	SCRIPTS
;

(instance sUpElevator of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= cycles 15)
			)
			(
				(numberPad dispose:)
				(cast eachElementDo: #perform: disposeNumbers)
				(= cycles 30)
			)
			(;2 is current state
				(ego
					z: 		1000,
					hide:		,
					normal:	FALSE
				)
				(elevator
					view:			vLarryInElevator,
					cel:			0,
					setCycle:	Forward,
					setMotion:	MoveTo (elevator x?) -20 self
				)
			)
			(
				(if
					(== (++ floorTo) floorWant)
					(if (== floorTo 8)
						(curRoom newRoom: 350)
					else
						(FadeIt)
						(curRoom setScript: sArriveUp)
					)
				else
					(FadeIt)
					(curRoom setScript: sGoUp)
				)
			)
		)
	)
)

(instance sGoUp of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(elevator
					view:			vLarryInElevator,
					y:				190,		;180,
					setCycle:	Forward,
					setMotion:	MoveTo (elevator x?) -20 self
				)
			)
			(
				(if
					(== (++ floorTo) floorWant)
					(if (== floorTo 8)
						(curRoom newRoom: 350)
					else
						(FadeIt)
						(curRoom setScript: sArriveUp)
					)
				else
					(FadeIt)
					(self init:)
				)
			)
		)
	)
)

(instance sDownElevator of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= cycles 15)
			)
			(
				(numberPad dispose:)
				(cast eachElementDo: #perform: disposeNumbers)
				(= cycles 30)
			)
			(;2 is current state
				(= register yJump)
				(ego
					z: 		1000,
					hide:,
					normal:	FALSE
				)
				(elevator
					view:			vLarryInElevator,
					cel:			0,
					setCycle:	Reverse,
					setMotion:	MoveTo (elevator x?) 190 self
				)
			)
			(
				(if
					(== (-- floorTo) floorWant)
					(if (== floorTo 1)
						(curRoom newRoom: 330)
					else
						(FadeIt)
						(curRoom setScript: sArriveDown)
					)
				else
					(FadeIt)
					(curRoom setScript: sGoDown)
				)
			)
		)
	)
)

(instance sGoDown of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(elevator
					view:			vLarryInElevator,
					y:				-20,
					setCycle:	Reverse,
					setMotion:	MoveTo (elevator x?) 190 self
				)
			)
			(
				(if
					(== (-- floorTo) floorWant)
					(if (== floorTo 1)
						(curRoom newRoom: 330)
					else
						(FadeIt)
						(curRoom setScript: sArriveDown)
					)
				else
					(FadeIt)
					(self init:)
				)
			)
		)
	)
)

(instance sArriveUp of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= register 8)
				(elevator
					view:			vLarryInElevator,
					y:				190,	;180,
					setCycle:	Forward,
					setMotion:	MoveTo (elevator x?) elevatorY self
				)
			)
			(
				(elevator
					setCycle:	EndLoop self
				)
			)
			(
				(theMusic3
					number:		sElevatorFloorDing,
					play:
				)
				(ego
					x:				elevatorX,
					y:				elevatorY,
					z:				0,
					show:			,
				)
				(NormalEgo 2)
				(elevator
					view:			vElevator,
					stopUpd:
				)
				(= cycles 1)
			)
			(
				(if 
					(not (Btst fGotDizzy))
					(Bset fGotDizzy)
					(Print "Whew! You're dizzy.")
				)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sArriveDown of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= register 16)
				(elevator
					view:			vLarryInElevator,
					y:				-20,
					setCycle:	Reverse,
					setMotion:	MoveTo (elevator x?) elevatorY self
				)
			)
			(
				(elevator
					setCycle:	EndLoop self
				)
			)
			(
				(theMusic3
					number:		sElevatorFloorDing,
					play:
				)
				(ego
					x:				elevatorX,
					y:				elevatorY,
					z:				0,
					show:			,
				)
				(NormalEgo 2)
				(elevator
					view:			vElevator,
					stopUpd:
				)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sElevatorScript of Script
	(method (init &tmp i j)
;		(ego setHeading: 180)	;don't do without hands off!
		(numberPad init:)
		(for ((= i 0)) (< i 4) ((++ i))
			(for ((= j 0)) (< j 2) ((++ j))
				((aNumber new:)
					init:			,
					z:				(+ startZ (* i zGap)),
					x:				(+ startX (* j xGap)),
					cel:			(+ 1 (* i 2) j),
				)
			)
		)
	)
)

(instance sFrom390 of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					egoSpeed:,
					view:			vLarryGetting,
					loop:			lLowLeft,
					cel:			0,
					setCycle:	EndLoop self,
				)
			)
			(
				(Print "You close the door behind you,
					and jiggle the knob to confirm it is locked.")
				(ego
					setCycle:	BegLoop self
				)
			)
			(
				(NormalEgo 1)
				(ego
					setHeading:	90 self
				)
			)
			(
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sNoRide of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= seconds 3)
			)
			(
				(Print "Not one for those long elevator rides, eh, Larry?")
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sDoorMessage of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(Print "You rap your knuckles on the door."
					#dispose
				)
				(ego
					egoSpeed:,
					view:			vLarryGetting,
					setLoop:		(if (< (ego x?) 160) 1 else 0),
					setCel:		0,
					setCycle:	EndLoop self
				)
				(theMusic3
					number:		sKnocking,
					loop:			1,
					play:			self
				)
			)
			(
				(ego setCycle: BegLoop self)
			)
			(
				(= seconds 2)
			)
			(
				(Cls)
				(switch (Random 34 50)
					(34
						(Print "\"Hey, Scott, the girls are here!!\"")
					)
					(35
						(Print "\"It says `Do NOT Disturb!' Can't you read?\"")
					)
					(36
						(Print "You hear some extremely heavy breathing.")
					)
					(37
						(Print "\"Go away! I gave at the office!\"")
					)
					(38
						(Print "\"Oh, Chris, there's someone at the door!\"")
						(Print "\"They're gonna have to wait, Kim Baby!
 							It's too late for me to stop now!!\"")
					)
					(39
						(Print "\"Baaaaah. Baaaaah!\"")
						(Print "Don't even ask, Larry!")
					)
					(40
						(Print "\"Oh, Bruce!\"")
						(Print "\"Oh, Howard!\"")
						(Print "\"Oh, Terri!\"")
						(Print "\"Oh, Keri!\"")
					)
					(41
						(Print "\"Ken! Get off me, Ken!!\"")
					)
					(42
						(Print "\"Oh, gawd, Ed!! Get in the closet. It's my husband!!\"")
					)
					(43
						(Print "\"Bobbit, is that you?\"")
					)
					(44
						(Print "\"Not now; I've got Wessonality!\"")
					)
					(45
						(Print "\"Is that Room Service? I didn't order anchovies.\"")
					)
					(46
						(Print "An eye appears at the peephole.")
						(Print "\"Hey, Mary!
							Isn't it a little early for Trick or Treaters?\"")
					)
					(47
						(Print "\"Oh, gawd, Ed!! Get in the closet. It's my wife!!\"")
					)
					(48
						(Print "Hmmm. Is that the smell of burning rubber?")
					)
					(49
						(Print "\"Oh, Jeff. You really know how to hurt a guy!\"")
					)
					(50
						(Print "\"Get away from him, you bully! He's mine.\"")
					)
				)
				(= seconds 2)
			)
			(
				(Print "This must not be the right door, Larry.")
				(NormalEgo)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sSuiteDoor of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					egoSpeed:,
					view:			vLarryGetting,
					setLoop:		(if (< (ego x?) 160) 1 else 0),
					setCel:		0,
					setCycle:	EndLoop self
				)
				(theMusic3
					number:		sKnocking,
					loop:			1,
					play:			self
				)
			)
			(
				(Print "You loudly rap your knuckles on the door.")
				(ego setCycle: BegLoop self)
			)
			(
				(= seconds 3)
			)
			(
				(if (not (Btst fFawnInRoom))
					(Print "There is no response to your knock, 
						but now your knuckles hurt.")
					(NormalEgo)
					(HandsOn)
					(self dispose:)
				else
					(if (and (Btst fOrderedWine)
							(not (Btst fSeenDeliveryBoyGag)))
						(Print "Fawn's sexy voice oozes through the door,
							\"Oh, baby, it's you! You're back!!\"")
						(Print "There's a click as she unlocks the door, then
							\"Come on in, you wonderful hunk!\"")
						(Print "You can't wait!")
					else
						(Print "Fawn's delicate voice booms out across the hotel,
							\"Just a minute, Larry!\"")
						(Print "There's a click as she unlocks the door,
							then a piercing \"Come on in. The door's open.\"")
					)
					(Print "Subtly, you shout, \"Here I come, baby!\"")
					(theMusic fade:)
					(NormalEgo 1)
					(curRoom newRoom: 390)
				)
			)
		)
	)
)

;
;	PROPS
;

(instance aNumber of ElevatorNumbers)

(instance disposeNumbers of Code
	(method (doit obj)
		(if (obj isKindOf: ElevatorNumbers)
			(obj dispose:)
		)
	)
)

(instance elevator of Actor
	(properties
		view			vElevator
		loop			0
		cel			0
		x				elevatorX
		y				elevatorY
		yStep			12
		priority		10
		signal		(| ignrAct fixPriOn ignrHrz)
		sightAngle	40
		description "the elevator"
		approachY	elevatorY
		illegalBits $0000
	)
	(method (init)
		(self approachX: (- elevatorX 1))
		(super init:)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if inElevator
					(Print "There are buttons labeled \"one\" through \"eight.\"")
				else
					(Print "You marvel at the hotel's efficiency.
						The elevator is always waiting for you.")
				)
			)
			(verbDo
				(curRoom setScript: sElevatorScript)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)

)

(instance floorNumber of View
	(properties
		view			vNumbers
		loop			lNumbers
		x				floorX
		y				floorY
		priority		14
		signal		(| ignrAct fixPriOn)
	)
)

(instance theHeart of Prop	;so it has cue method
	(properties
		view			vGenericFloor
		loop			lHeart
		cel			0
		x				20
		y				113
		description "a heart"
		lookStr		"The hotel has stuck a cheap plastic heart to the door
						with a thumbtack.  That's all it takes to create a
						Honeymoon Suite!"
		approachX	suiteDoorX
		approachY	suiteDoorY
	)
	(method (doVerb theVerb invItem)
		(Print "The cheap, plastic heart is not worth taking.")
	)
)

(instance suiteDoor of Feature
	(properties
		x 19
		y 128
		z 15
		heading 0
		nsLeft 7
		nsTop 93
		nsBottom 134
		nsRight 32
 		description "the door with the heart on it"
		sightAngle 40
		approachX	suiteDoorX
		approachY	suiteDoorY
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "It's a door not unlike all the others,
					except this one has a cute, little, trite, cracked,
					plastic heart stuck to it with a thumbtack.")
			)
			(verbDo
				(HandsOff)
				(curRoom setScript: sSuiteDoor)
			)
			(verbUse
				(Print "It's no good. The door resists all of your efforts.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance numberPad of View
	(properties
		view			vNumbers
		loop			lNumberPad
		x				262
		y				170
		z				48
		priority		14
		description "the key pad"
		signal		(| ignrAct fixPriOn)
	)
)

(instance door1 of Feature
	(properties
		x 11		;19
		y 113
		z 0
		heading 0
		nsLeft 7
		nsTop 93
		nsBottom 134
		nsRight 32
 		description "the door"
		sightAngle 40
		approachX	suiteDoorX
		approachY	suiteDoorY
	)
	(method (doVerb theVerb invItem)
		(door2 doVerb: theVerb invItem)
	)
)

(instance door2 of Feature
	(properties
		x 45		;53
		y 112
		z 0
		heading 0
		nsLeft 45
		nsTop 94
		nsBottom 130
		nsRight 62
 		description "the door"
		sightAngle 40
		approachX	71
		approachY	132
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "It's a door just like all the others.")
			)
			(verbDo
				(HandsOff)
				(curRoom setScript: sDoorMessage)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance door3 of Feature
	(properties
		x 100		;108
		y 108
		z 0
		heading 0
		nsLeft 95
		nsTop 91
		nsBottom 125
		nsRight 121
 		description "the door"
		sightAngle 40
		approachX	118
		approachY	129
	)
	(method (doVerb theVerb invItem)
		(door2 doVerb: theVerb invItem)
	)
)
(instance door4 of Feature
	(properties
		x 208		;200
		y 108
		z 0
		heading 0
		nsLeft 181
		nsTop 94
		nsBottom 123
		nsRight 203
 		description "the door"
		sightAngle 40
		approachX	187
		approachY	130
	)
	(method (doVerb theVerb invItem)
		(door2 doVerb: theVerb invItem)
	)
)
(instance door5 of Feature
	(properties
		x 265		;257
		y 114
		z 0
		heading 0
		nsLeft 239
		nsTop 93
		nsBottom 135
		nsRight 262
 		description "the door"
		sightAngle 40
		approachX	240
		approachY	135
	)
	(method (doVerb theVerb invItem)
		(door2 doVerb: theVerb invItem)
	)
)
(instance door6 of Feature
	(properties
		x 308		;301
		y 122
		z 0
		heading 0
		nsLeft 285
		nsTop 99
		nsBottom 145
		nsRight 305
 		description "the door"
		sightAngle 40
		approachX	279
		approachY	145
	)
	(method (doVerb theVerb invItem)
		(door2 doVerb: theVerb invItem)
	)
)
;(instance ashtray of Feature
;	(properties
;		x 52
;		y 149
;		z 0
;		heading 0
;		nsLeft 46
;		nsTop 137
;		nsBottom 161
;		nsRight 59
; 		description "an ashtray"
;		lookStr		"There must be a lot of smokers in this hotel. It stinks!"
;		sightAngle 40
;	)
;)
(instance plants of Feature
	(properties
		x 160
		y 140
		z 0
		heading 0
		nsLeft 114
		nsTop 125
		nsBottom 155
		nsRight 206
 		description "the plants"
		sightAngle 40
		lookStr		"They almost look real, don't they?"
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbSmellTaste
				(Print "To you, the plants have no distinctive smell or taste.")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)
(instance elevatorShaft of Feature
	(properties
		x 				159
		y 				74
		z 				0
		heading 		0
		nsLeft 		128
		nsTop 		-1
		nsBottom 	150
		nsRight 		190
 		description "the elevator shaft"
		lookStr 		"That elevator must be made by the DNA Company."
		sightAngle 	40
		approachX	elevatorX
		approachY	elevatorY
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(elevator doVerb: theVerb invItem)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(procedure (FadeIt)
	(theIconBar disable:)
	(if 
		(not (Btst isVga))
		(curRoom drawPic: pBlackScreen)
		(cast eachElementDo: #hide:)
	)
	(floorNumber cel: floorTo)
	(if (== floorTo 4)
		(suiteDoor
			init:,
			approachVerbs:	objectVerbs,
		)
		(if (features contains: door1)
			(door1 dispose:)
		)
		(theHeart z: 0)
	else
		(theHeart z: 1000)
		(door1 init:)
		(if (features contains: suiteDoor)
			(suiteDoor dispose:)
		)
	)
	(if (Btst isVga)
		(curRoom
			drawPic: 	(curRoom picture?)
		)
		(Animate (cast elements?) FALSE)	;so drawpic will finish
	)
	(if (not (Btst isVga))
		(Animate (cast elements?) FALSE)	;so drawpic will finish
		(curRoom
			drawPic: 	(curRoom picture?)
		)
		(Animate (cast elements?) FALSE)	;so drawpic will finish
		(cast eachElementDo: #show:)
	)
	(theIconBar enable:)
)


