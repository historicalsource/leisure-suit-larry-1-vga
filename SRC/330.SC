;**
;**	Logics for Room 330 -- At the Base of the Elevator
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford
;**
;**	Last Update:  October 2, 1991
;**


(script# 330)

(public
	rm330		0
)

(define startZ 84)
(define startX 240)
(define zGap 18)
(define xGap 43)
(define elevatorX 156)
(define elevatorY	137)
(define ashtrayX 175)
(define ashtrayY 162)
(define phoneX 54)
(define phoneY 101)

;(define	vLarryInElevator		331)
;(define	vElevator				332)
;(define	vNumbers					333)
;(define vLarryGetting			808)

;(define	sElevatorFloorDing	340)
;(define	mCasino 310)

(class ElevatorNumbers of View
	(properties
		view			vNumbers
		loop			lNumbers
		y				180
		priority		15
		signal		(| ignrAct fixPriOn)
		description "the button"
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(self setLoop: (+ (self loop?) 1))
				(Animate (cast elements?) FALSE)
				(= floorTo (self cel?))
				(if (== floorTo 1)
					(HandsOff)
					(self setLoop: (- (self loop?) 1))
					(Animate (cast elements?) FALSE)
					(curRoom setScript: sNoRide)
				else
					(HandsOff)
					(curRoom setScript: sToElevator)
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance rm330 of LLRoom
	(properties
		picture	330
		north		340
		east		320
		south		310
		west		310
	)
	(method (init)
		(Load SOUND
			mCasino
			sElevatorFloorDing
		)
		(LoadMany VIEW
			vLarryInElevator
			vElevator
			vLarryGetting
			vNumbers
		)
;		(curRoom addObstacle:
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	0 0 319 0 319 112 150 112 135 102 117 99 82 97 46 99 15 108
;						18 126 32 126 32 139 0 139,
;				yourself
;			)
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	250 189 250 184 271 176 319 176 319 189,
;				yourself
;			)
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	83 145 117 135 141 135 152 147 161 147 164 140 164 135
;						189 135 222 143 222 152 187 162 175 162 175 169 150 169
;						136 163 100 162 83 150,
;				yourself
;			)
;		)
		; Polygon Editor 1.11
		; Dynamic Obstacles : Picture 330
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	0 0 319 0 319 112 150 112 135 102 117 99 82 97 46 99 15 108
						18 126 36 126 36 139 0 139,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	250 189 250 184 271 176 319 176 319 189,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	83 145 117 135 141 135 152 147 161 147 164 140 164 135
						189 135 222 143 222 152 187 162 175 162 175 169 150 169
						136 163 100 162 83 150,
				yourself
			)
		)
		(theMusic3
			loop:			1,
			vol:			127,
			flags:		1,
		)
		(switch prevRoomNum
			(310
				NULL
			)
			(320
				(theMusic
					number:		mCasino,
					loop:			-1,
					flags:		1,
					play:			90,
				)
				(HandsOff)
				(self setScript: sFromLounge)
			)
			(340
				(HandsOff)
				(curRoom setScript: sFromElevator)
			)
			(else
				(ego posn: 160 170)
			)
		)
		(ego init:)
		(super init:)
		(elevator
			cycleSpeed:		(+ 1 howFast),
			moveSpeed:		(+ 1 howFast),
			init:,
			approachVerbs:	objectVerbs
		)
		(if (!= prevRoomNum 340)
			(elevator stopUpd:)
		)
		(ashTray
			init:,
			approachVerbs:	objectVerbs
		)
		(thePhone
			init:,
			approachVerbs:	objectVerbs verbLook
		)
		(plants
			init:,
		)
		(elevatorShaft
			init:,
			approachVerbs:	objectVerbs
		)
		(theWindow init:)
		(cabaretSign init:)
		(if (== ((inventory at: iDiscoPass) owner?) 330)
			(discoPass
				init:,
				approachVerbs: objectVerbs,
			)
		)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if
					(SteppedOn ego cCYAN)
					(Print "You are inside the glass elevator.
						Your favorite kind of music is playing.
						You hum along, insipidly.")
				else
					(Print "The entrance to the Casino's Cabaret Lounge is to
						the right. A glass elevator awaits your entrance.
						An ashtray sits in front of that planter full of
						plastic plants.")
				)
			)
			(verbTalk
				(Print "\"...strangers in the night, da da da dee da...\"")
				(Print "(How you love Muzak!)" LOW)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
	(method (doit)
		(super doit: &rest)
		(if
			(ego mover?)
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
			(script)
			((> (ego x?) 278)
				(HandsOff)
				(self setScript: sGoLounge)
			)
			((SteppedOn ego cCYAN)
				(theMusic fade: 127 5 5 0)
			)
			((SteppedOn ego cRED)
				(theMusic fade: 90 5 5 0)
			)
		)
	)
)

;
;	SCRIPTS
;

(instance sGoLounge of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego setMotion: MoveTo 288 (ego y?) self)
			)
			(
				(theMusic fade:)
				(curRoom newRoom: (curRoom east?))
			)
		)
	)
)

(instance sFromLounge of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					posn:			288 150,
					setMotion:	MoveTo 260 150 self
				)
			)
			(
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sFromElevator of Script
	(method (changeState newState)
		(switchto (= state newState)
			(;0 is current state
				(= register 16)
				(ego
					z:			1000,
					hide:,
					normal:	FALSE,
				)
				(elevator
					view:			vLarryInElevator,
					y:				-10,
					setCycle:	Reverse,
					setMotion:	MoveTo elevatorX elevatorY self
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
				(elevator
					cel:			0,
					view:			vElevator,
					stopUpd:
				)
				(ego
					x:				elevatorX,
					y:				elevatorY,
					z:				0,
					show:,
				)
				(NormalEgo 2)
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
				(cast eachElementDo: #perform: disposeNumbers)
				(= cycles 30)
			)
			(;2 is current state
				(ego
					z:			1000,
					normal:	FALSE,
					hide:,
				)
				(elevator
					view:			vLarryInElevator,
					cel:			0,
					setCycle:	Forward,
					setMotion:	MoveTo (elevator x?) -20 self
				)
			)
			(
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
				(numberPad init:)
;				(ego setHeading: 180)	;don't do without hands off!
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

(instance sGetPass of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					egoSpeed:,
					view:			vLarryGetting,
					loop:			lHighLeft,
					cel:			0,
					setCycle:	EndLoop self
				)
			)
			(
				(Points pGotPass ptsGotPass)
				(discoPass
					z:				1000,
					dispose:
				)
				(ego
					get:			iDiscoPass,
					setCycle: 	BegLoop self
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

(instance sNoRide of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= seconds 3)
			)
			(
				(Print "Wasn't that a fast ride!")
				(HandsOn)
				(self dispose:)
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
		lookStr 		"That elevator must be made by the DNA Company."
	)
	(method (init)
		(self approachX: (- elevatorX 1))
		(super init:)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(curRoom setScript: sElevatorScript)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance discoPass of View
	(properties
		view			vElevator
		loop			1
		x	 			159
		y				168
		z				20
		sightAngle 	40
 		description "the card"
		approachX 	ashtrayX
		approachY	ashtrayY
		lookStr		"Why, it appears some sort of card is lying in the ashtray."
		signal		ignrAct
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(HandsOff)
				(curRoom setScript: sGetPass)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

;
;	Features
;

(instance ashTray of Feature
	(properties
		x 157
		y 153
		z 0
		heading 0
		nsLeft 149
		nsTop 143
		nsBottom 164
		nsRight 165
 		description "the ash tray"
		sightAngle 40
		approachX 	ashtrayX
		approachY	ashtrayY
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if (== ((inventory at: iDiscoPass) owner?) 330)
					(Print "There's some kind of card in the ashtray.")
				else
					(Print "Yuck!")
				)
			)
			(verbDo
				(if (ObjInRoom iDiscoPass)
					(HandsOff)
					(curRoom setScript: sGetPass)
				else
					(Print "You foolishly run your fingers through the discarded
						cigarette butts until you find one hot enough to burn you.
						Ouch!")
 				)
			)
			(else
				(if (== ((inventory at: iDiscoPass) owner?) 330)
					(Print "There's some kind of card in the ashtray.")
				else
					(Print "The ashtray is of no further interest to you.")
				)
			)
		)
	)
)

(instance thePhone of Feature
	(properties
		x 35
		y 103
		z 23
		heading 0
		nsLeft 27
		nsTop 67
		nsBottom 93
		nsRight 43
 		description "the telephone"
		sightAngle  40
		approachX  	phoneX
		approachY	phoneY
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "The casino thoughtfully provides a courtesy telephone
					here in the lobby.
					Someone thoughtlessly filled its coin slot with chewing gum
					rendering it unusable to you.")
			)
			(verbTalk
				(Print "You'd love to talk on the telephone, but
 					this one is not in working order.")
			)
			(verbDo
				(Print "You attempt to return the telephone to working order
					by prying the chewing gum out of the slot.
					You cannot.
					You'll just have to find another telephone.")
			)
			(verbSmellTaste
				(Print "Yeah, it does smell like a public telephone!")
			)
			(verbUse
				(self doVerb: verbDo)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance plants of Feature
	(properties
		x 151
		y 140
		z 0
		heading 0
		nsLeft 103
		nsTop 123
		nsBottom 158
		nsRight 200
 		description "the plants"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if
					(SteppedOn ego cCYAN)
					(Print "You know,
						the people on those plants look like ants from up here.")
					(Print "Those ARE ants, you fool;
						we haven't left the ground yet!")
				else
					(Print "The plants are a member of your favorite species:
						polyvinyl chloride.
						You notice an ashtray in front of the planter.")
				)
			)
			(verbSmellTaste
				(Print "To you, the plants have no distinctive smell or taste.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance elevatorShaft of Feature
	(properties
		x 154
		y 66
		z 0
		heading 0
		nsLeft 121
		nsTop 0
		nsBottom 132
		nsRight 187
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

(instance theWindow of Feature
	(properties
		x 93
		y 96
		z 47
		heading 0
		nsLeft 67
		nsTop 21
		nsBottom 78
		nsRight 120
 		description "the window"
		lookStr 		"Is that a window or an illusory painting?
						With art like this, who can tell?"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				;**	walk him over first
				(Print "Carefully feeling the panes, you decide this window
					is really a painting.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance cabaretSign of Feature
	(properties
		x 226
		y 110
		z 60
		heading 0
		nsLeft 188
		nsTop 37
		nsBottom 63
		nsRight 265
 		description "the sign"
		lookStr 		"Oh, no! Look! Someone has painted the word
						\"Cabaret\" right there on the wall."
		sightAngle 40
	)
)

