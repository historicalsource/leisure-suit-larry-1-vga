;**
;**	Logics for Room 350 -- Eighth Floor
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford &
;**
;**	Last Update:  June 11, 1991
;**


;[R350 V809 XY226/167] ART make Faith walk faster; increase
;[R350 V809 XY226/167] step size; she's supposed to be RUNNING

; pressing 7 accesses disk if prevRoom 340


(script# 350)

(public
	rm350		0
)

(local
	lookCount
	talkCount
	breathLineTimer
;	closePts = [319 0 319 142 245 142 213 133 81 133 3 153 3 184 315 184
;				315 158 302 154 242 154 238 146 319 146 319 189 0 189 0 0]
	closePts = [319 0 319 189 314 189 314 159 296 154 253 154 234 148 234 139
				213 133 81 133 3 153 3 189 0 189 0 0]

;	openPts  = [262 0 262 151 242 151 242 146 251 146 251 143 213 133 81 133
;				3 153 3 184 315 184 314 158 293 152 273 151 273 0 319 0
;				319 189 0 189 0 0]
	openPts  = [262 0 262 151 237 151 237 139 213 133 81 133 3 153 3 184
						315 184 314 158 293 152 273 151 273 0 319 0 319 189 0 189 0 0]
	middlePts = [85 144 108 137 149 137 149 146 161 146 161 137 205 137
						222 145 213 155 229 157 224 172 181 172 179 160 85 160]

)

(define startZ 84)
(define startX 240)
(define zGap 18)
(define xGap 43)
(define elevatorX 156)
(define elevatorY	137)
(define doorsX 267)
(define doorsY 154)
(define deskX	226)
(define deskY	167)
(define inElevator (and (== (ego x?) elevatorX) (== (ego y?) elevatorY)))
(define pentDoorsOpen (== (doors cel?) cOpen))

(define	pEighthFloor			350)
(define	vEighthFloor			350)
	(define lDoors					0)
		(define cClosed			0)
		(define cOpen				5)
	(define lLarryDoors			1)
		(define cLarryOpen		0)
		(define cLarryClosed		5)
(define	vFaithGuarding			352)
	(define lWalkLeft				0)
	(define lWalkRight			1)
	(define lStandDesk			2)
	(define lSitDesk				3)
;(define	vLarryGetting			808)
;	(define lHighRight			0)
;	(define lHighLeft				1)
;	(define lLowRight				2)
;	(define lLowLeft				3)
;(define	vLarryInElevator		331)
;(define	vElevator				332)
;(define	vNumbers					333)


;(define	sElevatorFloorDing	340)
;(define	sElevatorDoors			350)
;(define	mFloor8					351)
(define	mFaithLeaving			353)

(class ElevatorNumbers of View
	(properties
		view			vNumbers
		loop			lNumbers
		y				180
		priority		15
		signal		(| ignrAct fixPriOn)
		description "the button"
;		sightAngle  90
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(self setLoop: (+ (self loop?) 1))
				(Animate (cast elements?) FALSE)
				(= floorTo (self cel?))
				(if (== floorTo 8)
					(HandsOff)
					(self setLoop: (- (self loop?) 1))
					(Animate (cast elements?) FALSE)
					(curRoom setScript: sNoRide)
				else
					(HandsOff)
					(curRoom setScript: sToElevator)
				)
			)
			(verbTalk
				(curRoom doVerb: verbTalk)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance rm350 of LLRoom
	(properties
		picture	pEighthFloor
	)
	(method (init)
		(if debugging
			(Bset fFaithGone)
		)
		(LoadMany VIEW
			vEighthFloor
			vLarryGetting
			vNumbers
			vLarryInElevator
			vElevator
		)
		(LoadMany SOUND
			sElevatorDoors
			sElevatorFloorDing
			mFloor8
			mFaithLeaving
		)
		(theMusic3
			loop:			1,
			vol:			127,
			flags:		1,
		)
		(closeObstacle
			points:		@closePts,
			size:			14,
		)
		(openObstacle
			points:		@openPts,
			size:			19,
		)
		(middleObstacle
			points:		@middlePts,
			size:			14,
		)
		(closeObstacles
			add: middleObstacle closeObstacle
		)
		(openObstacles
			add: middleObstacle openObstacle
		)
		(curRoom obstacles: closeObstacles)
		(ego
			init:,
		)
		(super init:)
		(if (!= prevRoomNum 355)
			(theMusic
				number:		mFloor8,
				loop:			-1,
				vol:			127,
				flags:		1,
				play:
			)
		)
		(doors
			cycleSpeed:		(+ howFast 1),
			init:,
			approachVerbs:	objectVerbs,
		)
		(elevator
			cycleSpeed:		(+ howFast 1),
			moveSpeed:		(+ howFast 1),
			init:,
			approachVerbs: objectVerbs,
		)
		(deskF
			init:,
			approachVerbs: objectVerbs
		)
		(if (not (Btst fFaithGone))
			(LoadMany VIEW
				vFaithGuarding
			)
			(faith
				cycleSpeed:		howFast,
				moveSpeed:		howFast,
				init:,
				stopUpd:,
				approachVerbs: actorVerbs verbLook
			)
		else
			(deskF
				lookStr:
				"Now that you're no longer staring at Faith's
				ample weapons, you notice an unlabeled button on her desk."
			)
		)
		(switch prevRoomNum
			(360
				(elevator stopUpd:)
				(ego x: 400)
				(HandsOff)
				(curRoom setScript: sFromPenthouse)
			)
			(340
				(doors stopUpd:)
				(HandsOff)
				(curRoom setScript: sFromElevator)
			)
			(355
				(elevator stopUpd:)
				(ego
					loop:			1,
					show:,
					normal: 		TRUE,
					setMotion:	0,
				)
				(if (ObjInRoom iPills)
					(LoadMany SOUND
						mFaithLeaving
					)
					(curRoom setScript: sFaithLeaves)
				)
			)
			(else
				(ego posn: 160 170)
			)
		)
		(plants init:)
		(door1 init:)
		(door2 init:)
		(door3 init:)
		(door4 init:)
		(sculpture init:)
	)
	(method (doit)
		(super doit: &rest)
		(if
			(ego mover?)
			(if (cast contains: numberPad)
				(numberPad dispose:)
				(ego illegalBits: $8000)
				(cast eachElementDo: #perform disposeNumbers)
				(if
					(== (curRoom script?) sElevatorScript)
					(curRoom setScript: 0)
				)
			)
		)
		(cond
			(script)
			((SteppedOn ego cBLUE)
				(HandsOff)
				(curRoom setScript: sToPenthouse)
			)
		)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if (Btst fFaithGone)
					(Print "You are at the top floor of the hotel.
						That desk is where you lost your Faith, remember?")
					(Print "(Al's so proud he finally got to use THAT line!)"
						LOW)
				else
					(Print "You are at the top floor of the hotel.
						A beautiful security guard sits behind a desk.")
				)
			)
			(verbTalk
				(Print "\"strangers in the night, da da da dee da...\"")
				(Print "(How you love Muzak!)" LOW)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
	(method (dispose)
		(closeObstacles dispose:)
		(openObstacles dispose:)
		(super dispose:)
	)
)


(instance sFromElevator of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(elevator
					view:			vLarryInElevator,
					y:				190,
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

(instance sToElevator of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= cycles 15)
			)
			(
				(numberPad dispose:)
				(cast eachElementDo: #perform disposeNumbers)
				(= cycles 30)
			)
			(;2 is current state
				(ego
					z: 		1000,
					hide:		,
					normal:	FALSE,
				)
				(elevator
					view:			vLarryInElevator,
					cel:			0,
					setCycle:	Reverse,
					setMotion:	MoveTo (elevator x?) 190 self
				)
			)
			(
				(theMusic fade:)
				(curRoom newRoom: 340)
			)
		)
	)
)

(instance sElevatorScript of Script
	(method (doit)
		(super doit: &rest)
		(if (ego mover?)
			(numberPad dispose:)
			(cast eachElementDo: #perform disposeNumbers)
			(self dispose:)
		)
	)
	(method (changeState newState &tmp i j)
		(switchto (= state newState)
			(
;				(ego setHeading: 180)	;don't do without hands off!
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
	)
)

(instance sFaithLeaves of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= cycles 10)
			)
			(
				(theMusic
					number:		mFaithLeaving,
					loop:			1,
					play:,
				)
				(employeeExit init:)
				(Print "\"I'm outta here!\" cries Faith.
					\"I've gotta get home to my boyfriend before this
					stuff wears off!\"")
				(faith
					cycleSpeed:	0,
					moveSpeed:	0,
					setCycle:	Walk,
					setMotion: 	MoveTo 5 185 self
				)
			)
			(
				(Print "\"Hey, thanks a lot, buddy!\" shouts Faith across the
					hotel. \"We'll do it once for you!\"")
				(Print "Twisting her key in the lock,
					she disappears down the unmarked employee staircase.")
				(faith
					setMotion: MoveTo -40 185 self
				)
			)
			(
				(theMusic
					number:		mFloor8,
					loop:			-1,
					play:,
				)
				(Print "Sorry, Larry. It appears Faith is true to her name.")
				(Bset fFaithGone)
				(deskF lookStr: "Now that you're no longer staring at Faith's
						ample weapons, you notice an unlabeled button on her desk."
				)
				(HandsOn)
				(self dispose:)
				(faith dispose:)
			)
		)
	)
)

(instance sToPenthouse of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					illegalBits:	$8000,
					setMotion:		MoveTo 267 149 self
				)
			)
			(
				(ego
					setHeading:		270 self
				)
			)
			(
				(theMusic3
					number:			sElevatorDoors,
					play:
				)
				(ego
					illegalBits:	$0000,
					normal:			FALSE,
					hide:
				)
				(doors
					setLoop:			lLarryDoors,
					setCel:			cLarryOpen,
					setCycle:		EndLoop self
				)
			)
			(
				(Cls)
				(ego
					illegalBits:	$8000
				)
				(Print "Oh, oh. Where are we goin' now, Larry?")
				(Points pBeenInPenthouse ptsBeenInPenthouse)
				(curRoom newRoom: 360)
			)
		)
	)
)

(instance sFromPenthouse of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= cycles 3)
			)
			(
				(theMusic3
					number:		sElevatorDoors,
					play:
				)
				(doors
					setLoop:		lLarryDoors,
					setCel:		cLarryClosed,
					setCycle:	BegLoop self
				)
			)
			(
				(ego
					x:					267,
					y:					149,
					illegalBits: 	$0000,
				)
				(= cycles 5)
			)
			(

				(doors
					setLoop:		lDoors,
					setCel:		cOpen,
				)
				(ego
					egoSpeed:,
					show:			,
					normal:		TRUE,
					setMotion:	MoveTo (- (ego x?) 10) (+ (ego y?) 5),
				)
				(= cycles 5)
			)
			(
				(theMusic3
					number:		sElevatorDoors,
					play:
				)
				(doors
					setCycle:	BegLoop doors
				)
				(ego
					setMotion:	MoveTo (- (ego x?) 10) (+ (ego y?) 5) self,
				)
			)
			(
				(NormalEgo 1)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sPushButton of Script
	(method (changeState newState &tmp [string 100])
		(switchto (= state newState)
			(
				(ego
					egoSpeed:,
					view:			vLarryGetting,
					loop:			lHighLeft,
					cel:			0,
					setPri:		(+ (ego priority?) 1),
					setCycle:	EndLoop self
				)
			)
			(
				(theMusic3
					number:		sElevatorDoors,
					play:
				)
				(Cls)
				(Print
					(Format @string "The penthouse elevator doors glide %s."
						(if register {open} else {close})
					)
					#dispose:
					#time: 	5
					HIGH
				)
				(if register
					(curRoom obstacles: openObstacles)
				else
					(curRoom obstacles: closeObstacles)
				)
				(doors
					setCycle: (if (doors cel?) BegLoop else EndLoop) doors
				)
				(ego setCycle: BegLoop self)
			)
			(
				(NormalEgo 1)
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
				(Print "The rumors were right.
					You are quick on the trigger, Larry!")
				(HandsOn)
				(self dispose:)
			)
		)
	)
)


;
;	Room Props
;

(instance aNumber of ElevatorNumbers)

(instance disposeNumbers of Code
	(method (doit obj)
		(if (obj isKindOf: ElevatorNumbers)
			(obj dispose:)
		)
	)
)

(instance faith of Person
	(properties
		view		 	vFaithGuarding
		loop			lSitDesk
		x				198
		xStep			4
		y				166
		description "the security guard"
		sightAngle  40
		approachX	deskX
		approachY	deskY
		signal		ignrAct
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(curRoom newRoom: 355)
			)
			(else
				(Print "She might listen to you,
					if you can establish eye contact.")
			)
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
	)
	(method (init)
		(self approachX: (- elevatorX 1))
		(super init:)
	)
	(method (cue)
		(super cue:)
		(if inElevator
			(Print "There are buttons labeled \"one\" through \"eight.\"")
		else
			(Print "The glass elevator only goes down from here.
				How about you?")
		)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				turnEgo
			)
			(verbDo
				;check and make sure no weird script with no handson is running
				(curRoom setScript: sElevatorScript)
			)
			(verbTalk
				(curRoom doVerb: verbTalk)
			)
			(else
				(super doVerb: theVerb invItem)
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
;		sightAngle	90
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(curRoom doVerb: verbLook)
			)
			(verbTalk
				(curRoom doVerb: verbTalk)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance doors of Prop
	(properties
		view			vEighthFloor
		loop			lDoors
		cel			cClosed
		x 				267
		y 				154
 		description "the doors"
		sightAngle 	40
		approachX	doorsX
		approachY	doorsY
		priority		10
		signal		(| ignrAct fixPriOn)
	)
	(method (cue)
		(super cue:)
		(self stopUpd:)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if (not pentDoorsOpen)
					(Print "The doors are securely closed.
						A brass plate near the elevator says: \"Private.\"
						There is no button nearby.")
				else
					(Print "The doors are open. Go on in.")
				)
			)
			(verbDo
				(if (not pentDoorsOpen)
					(Print "The doors are securely closed.
						A brass plate near the elevator says: \"Private.\"
						There is no button nearby.")
				else
					(HandsOff)
					(curRoom setScript: sToPenthouse)
 				)
			)
			(verbTalk
				(curRoom doVerb: verbTalk)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)


;
; 	Room Features
;

(instance deskF of Feature
	(properties
		x 203
		y 168
		z 0
		heading 0
		nsLeft 193
		nsTop 138
		nsBottom 167
		nsRight 214
 		description "the security desk"
		sightAngle 	40
		approachX	deskX
		approachY	deskY
		lookStr
					"A beautiful security guard sits at the desk.
					A pair of large pistols are in a holster around her
					slim waist.
					She looks friendly, but quick on the trigger."
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(cond
					((not (Btst fFaithGone))
						(Print "\"Touch that button, and you're dead meat,
							White-Suit!\"")
					)
					((not (doors cel?))
						(HandsOff)
						(curRoom setScript: sPushButton NULL TRUE)
					)
					(else
						(HandsOff)
						(curRoom setScript: sPushButton NULL FALSE)
					)
				)
			)
			(verbTalk
				(curRoom doVerb: verbTalk)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance door1 of Feature
	(properties
		x 15
		y 116
		z 0
		heading 0
		nsLeft 3
		nsTop 97
		nsBottom 138
		nsRight 27
 		description "the door"
		lookStr "It's a door just like all the others."
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbTalk
				(Print "\"Hello? Hello?\" you cry.
					There is no answer from within.")
			)
			(verbDo
				(Print "Evidently the rooms on this floor are all unoccupied.")
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
(instance door2 of Feature
	(properties
		x 50
		y 116
		z 0
		heading 0
		nsLeft 40
		nsTop 98
		nsBottom 134
		nsRight 60
 		description "the door"
	)
	(method (doVerb theVerb invItem)
		(door1 doVerb: theVerb invItem)
	)
)
(instance door3 of Feature
	(properties
		x 102
		y 112
		z 0
		heading 0
		nsLeft 90
		nsTop 94
		nsBottom 130
		nsRight 115
 		description "the door"
	)
	(method (doVerb theVerb invItem)
		(door1 doVerb: theVerb invItem)
	)
)
(instance door4 of Feature
	(properties
		x 189
		y 120
		z 0
		heading 0
		nsLeft 175
		nsTop 95
		nsBottom 126
		nsRight 203
 		description "the door"
	)
	(method (doVerb theVerb invItem)
		(door1 doVerb: theVerb invItem)
	)
)
(instance plants of Feature
	(properties
		x 159
		y 143
		z 0
		heading 0
		nsLeft 111
		nsTop 130
		nsBottom 156
		nsRight 207
 		description "the plants"
		sightAngle 	40
		lookStr		"They almost look real, don't they?"
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbTalk
				(curRoom doVerb: theVerb invItem)
			)
			(verbSmellTaste
				(Print "To you, the plants have no distinctive smell or taste.")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance sculpture of Feature
	(properties
		x 155
		y 155
		z 120
		heading 0
		nsLeft 110
		nsTop 4
		nsBottom 67
		nsRight 201
 		description "the sculpture"
		lookStr 		"Atop the elevator shaft,
						a modern woman runs to meet her alien lover."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbTalk
				(curRoom doVerb: theVerb invItem)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance employeeExit of Feature
;init this after Faith leaves
	(properties
		x 				10
		y 				166
		z 				0
		heading 		0
		nsLeft 		0
		nsTop 		144
		nsBottom 	189
		nsRight 		21
 		description "the employee exit"
		sightAngle 	40
		lookStr		"Evidently, this exit is for employees only.
						You'd better use the elevator."
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(self doVerb: verbLook)
			)
			(verbTalk
				(curRoom doVerb: theVerb invItem)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance closeObstacles of List)

(instance openObstacles of List)

(instance middleObstacle of Polygon
	(properties
		type 			PBarredAccess
	)
)

(instance closeObstacle of Polygon
	(properties
		type			PBarredAccess
	)
)

(instance openObstacle of Polygon
	(properties
		type			PBarredAccess
	)
)


