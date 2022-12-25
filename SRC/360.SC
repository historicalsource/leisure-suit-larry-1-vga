;**
;**	Logics for Room 360 -- Penthouse Living Room
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford &
;**
;**	Last Update:  June 11, 1991
;**


(script# 360)

(public
	rm360		0
)

(define	vBlowUpDoll				373)
	(define	lBlowUpDoll			0)

(define	vLarryInTub				381)
	(define	lLarryInTowel		5)

(define	vEveInTub				382)
	(define	lEveWalks			3)

;(define	sElevatorDoors			350)
;(define	sBubbles					380)		;**	softly upon entering room
;(define	sDollDeflating			372)



(instance rm360 of LLRoom
	(properties
		picture	360
		east		370
	)
	(method (init)
;		(curRoom addObstacle:
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	9 165 306 165 270 148 232 148 216 134 209 134 209 126 319 126
;						319 189 0 189 0 140 23 140,
;				yourself
;			)
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	0 0 319 1 319 116 187 116 187 106 48 111 17 123 0 123,
;				yourself
;			)
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	54 114 163 111 163 116 94 116 87 120 53 120,
;				yourself
;			)
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	100 122 203 122 203 137 210 137 210 144 163 145 109 142,
;				yourself
;			)
;		)
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	9 165 306 165 270 148 232 148 216 134 209 134 209 126 319 126
						319 189 0 189 0 137 24 137,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	0 0 319 1 319 116 187 116 187 106 48 111 17 123 0 123,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	54 114 163 111 163 116 94 116 87 120 53 120,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	100 122 203 122 203 137 210 137 210 144 163 145 109 142,
				yourself
			)
		)
		(switch prevRoomNum
			(380
				(if (Btst fEveHasApple)
					(HandsOff)
;					(LoadMany VIEW vLarryInTub vEveInTub)
					(aEve
						cycleSpeed:	(ego cycleSpeed?),
						moveSpeed:	(ego moveSpeed?),
						init:
					)
					(ego
						normal:	FALSE,
						view:		vLarryInTub,
						x:			70,
						y:			130,
						setLoop:	lLarryInTowel
					)
					(curRoom setScript:	sEveIsReady)
				else
					(HandsOff)
					(self setScript: sFromTub)
				)
				(theMusic2 fade:)
			)
			(370
				(HandsOff)
				(if
					(Btst fDollFlies)
					(Load VIEW vBlowUpDoll)
					(Load SOUND sDollDeflating)
					(aDoll
						cycleSpeed:	(+ 3 howFast),
						moveSpeed:	(+ 3 howFast),
						init:
					)
					(ego
						x: 235,
						y:	119
					)
					(curRoom setScript: sFlyingDoll)
				else
					(self setScript: sFromBedroom)
				)
			)
			(350
			 	(HandsOff)
				(LoadMany SOUND sElevatorDoors mHotTub)
				(theMusic
					number:	mHotTub,
					vol:		127,
					loop:		-1,
					flags:	1,
					play:		,
					send:		4 mMUTE TRUE,
					send:		6 mMUTE TRUE,
					send:		7 mMUTE TRUE,
					send:		9 mMUTE TRUE,
					send:		10 mMUTE TRUE,
				)
				(ego
					loop:		1,
					x:			1000
				)																					
				(self setScript: sFromElevator)
			)
			(else	;TP
				(ego posn: 160 160)
				(theMusic
					number:	mHotTub,
					vol:		127,
					loop:		-1,
					flags:	1,
					play:		,
					send:		4 mMUTE TRUE,
					send:		6 mMUTE TRUE,
					send:		7 mMUTE TRUE,
					send:		9 mMUTE TRUE,
					send:		10 mMUTE TRUE,
				)
			)
		)
		(ego init:)
		(planter init:)
		(sofa init:)
		(painting2 init:)
		(painting1 init:)
		(sculpture init:)
		(fTable init:)
		(fShelf init:)
		(fSkylight init:)
		(fDoorway init:)
		(fDoorwayWest init:)
		(super init:)
		(elevator
			cycleSpeed:	(+ 1 howFast),
			init:
		)
		(elevatorF init:)
	)
	(method (doit)
		(super doit: &rest)
		(cond
			(script)
			((SteppedOn ego cBLUE)
				(HandsOff)
				(self setScript: sToTub)
			)
			((SteppedOn ego cGREEN)
				(HandsOff)
				(self setScript: sToBedroom)
			)
		)
	)
)

;
; 	SCRIPTS
;

(instance sToTub of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(if (< (ego y?) 116)
					(ego
						setMotion: PolyPath 87 112 self
					)
				else
					(= cycles 1)
				)
			)
			(
				(curRoom newRoom: 380)
			)
		)
	)
)

(instance sFromTub of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(if
					(< (ego y?) 106)
					(ego
						posn:			84 112,
						setMotion:	PolyPath 137 110 self
					)
				else
					(ego
						posn:			65 130,
						setMotion:	PolyPath 95 130 self
					)
				)
			)
			(
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sToBedroom of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego setMotion: PolyPath 235 (ego y?) self)
			)
			(
				(curRoom newRoom: 370)
			)
		)
	)
)
(instance sFlyingDoll of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
;				(theMusic3
;					number:		sDollDeflating,
;					setLoop:		-1,
;					flags:		1,
;					play:
;				)
				(aDoll
					setCycle:	Forward,
					setMotion:	MoveTo -20 96
				)
				(= cycles 10)
			)
			(
 				(ego setMotion:	PolyPath	0 132 self)
			)
			(
				(curRoom newRoom: 380)
			)
		)
	)
)

(instance sFromBedroom of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					posn:		227 118,
					setMotion:	PolyPath 209 122 self
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
			(
				(= cycles 3)
			)
			(
				(sfxElevator play:)
				(elevator
					setCycle:	EndLoop self
				)
			)
			(
				(elevator
					setLoop:			1,
					setCel:			4,
				)
				(= cycles 1)
			)
			(
				(ego
					setLoop:		1,
					setCel:		0,
					x:		 		256,
					y:				136,
					setPri:		14,
				)
				(= cycles 1)
			)
			(
				(ego
					setLoop:		-1,
					setCel:		-1,
					setCycle:	Walk,
					setMotion: 	MoveTo 237 144
				)
				(= seconds 3)
			)
			(
				(sfxElevator play:)
				(elevator
					setCycle:	BegLoop self
				)
			)
			(
				(elevator init:)
				(NormalEgo 1)
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
				(sfxElevator play:)
				(elevator
					loop:			1,
					cel:			0,
					setCycle:	EndLoop self
				)
			)
			(
				(ego
					setPri:		14,
					setMotion: 	MoveTo 255 142 self
				)
			)
			(
				(ego setHeading: 180 self)
			)
			(
				(sfxElevator play:)
				(ego
					hide:,
					normal:		FALSE,
					setPri:		RELEASE,
				)
				(elevator
					setLoop:		0,
					setCel:		4,
					setCycle:	BegLoop self
				)
			)
			(
				(= seconds 3)
			)
			(
				(theMusic fade:)
				(curRoom newRoom: 350)
			)
		)
	)
)
(instance sEveIsReady of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(aEve
					setLoop:		lEveWalks,
					setCycle:	Walk,
					setMotion:	PolyPath 236 118
				)
				(ego
					setMotion:	PolyPath 230 119 self
				)
			)
			(
				(curRoom newRoom:	370)
			)
		)
	)
)

;**************
;*** SOUNDS	***
;**************

(instance sfxElevator of Sound
	(properties
		number	sElevatorDoors
		vol		127
		loop		1
		flags		0
	)
)


;**************
;*** ACTORS	***
;**************

(instance aDoll of Actor
	(properties
		view		vBlowUpDoll
		loop		lBlowUpDoll
		x			220
		y			91
		xStep		7
		yStep		5
		priority 8
		signal 	fixPriOn
		lookStr	"Hurry, Larry! You're losing your new buddy!"
	)
)
(instance aEve of Actor
	(properties
		view		vEveInTub
		loop		lEveWalks
		x			100
		y			130
		signal	ignrAct
		lookStr	"So far; so good!"
	)
)


;*************
;*** PROPS ***
;*************



(instance elevator of Prop
	(properties
		view			360
		loop			0
		cel			0
		x				258
		y				139
		cycleSpeed	1
		signal		ignrAct
	)
	(method (cue)
		(super cue:)
		(HandsOff)
		(curRoom setScript: sToElevator)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "That's the elevator you rode in on!")
			)
			(verbDo
				(ego setMotion: PolyPath 237 144 self)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

;****************
;*** FEATURES ***
;****************

(instance elevatorF of Feature
	(properties
		x      		254
		y				110
		nsLeft 		236
		nsTop 		79
		nsBottom 	142
		nsRight 		272
		sightAngle 	40
		description	"the elevator"
		lookStr		"That's the elevator you rode in on!"
	)
	(method (doVerb theVerb invItem)
		(elevator doVerb: theVerb invItem)
	)
)
(instance painting1 of Feature
	(properties
		x 231
		y 135
		z 29
		heading 0
		nsLeft 222
		nsTop 91
		nsBottom 121
		nsRight 241
 		description "the painting"
		lookStr 		"The brass nameplate at the bottom says the
						title of this work of art is, \"Right.\""
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Stop smearing the acrylic.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance painting2 of Feature
	(properties
		x 303
		y 152
		z 29
		heading 0
		nsLeft 287
		nsTop 101
		nsBottom 145
		nsRight 319
 		description "the painting"
		lookStr "The brass nameplate at the bottom says the
					title of this work of art is, \"Left.\""
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Stop smearing the acrylic.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance sofa of Feature
	(properties
		x 153
		y 121
		z 0
		heading 0
		nsLeft 113
		nsTop 107
		nsBottom 135
		nsRight 194
 		description "the sofa"
		lookStr "This sofa couldn't possibly be comfortable."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Sit now? Have you looked outside?")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance planter of Feature
	(properties
		x 159
		y 171
		z 0
		heading 0
		nsLeft 0
		nsTop 154
		nsBottom 189
		nsRight 319
 		description "the planter"
		lookStr "Your eyes are immediately attracted to the
					sculptures in the planter."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "These plants aren't the only unreal thing in this
					penthouse!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance sculpture of Feature
	(properties
		x				 82
		y				 145
		z				 50
		heading		 0
		nsLeft		 7
		nsTop			 16
		nsBottom		 175
		nsRight		 157
 		description	"the sculpture"
		lookStr		 "What the hell are those two sculptures planning to do!?"
		sightAngle	 40
		onMeCheck	cLRED
	)
)
(instance fTable of Feature
	(properties
		x				160
		y				162
		z				27
		heading		0
		nsLeft		147
		nsTop			126
		nsBottom		144
		nsRight		173
 		description	"the coffee table"
		lookStr		"Be careful you don't bump your shin
					on THAT table!"
		sightAngle	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance fShelf of Feature
	(properties
		x				267
		y				149
		z				92
		heading		0
		nsLeft		217
		nsTop			37
		nsBottom		77
		nsRight		318
 		description	"the plant shelf"
		lookStr		"A shelf with pots and a plant sit above the
					elevator."
		sightAngle	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance fSkylight of Feature
	(properties
		x				148
		y				124
		z				116
		heading		0
		nsLeft		92
		nsTop			0
		nsBottom		17
		nsRight		204
 		description	"the skylight"
		lookStr		"Looking through the skylight, you see sky."
		sightAngle	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance fDoorway of Feature
	(properties
		x				153
		y				82
		z				0
		heading		0
		nsLeft		100
		nsTop			55
		nsBottom		110
		nsRight		206
 		description	"the far doorway"
		lookStr		"Looking through the doorway, you see
					the skyline of downtown Lost Wages,
					spread out before you like a cheap trick."
		sightAngle	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance fDoorwayWest of Feature
	(properties
		x				38
		y				80
		z				0
		heading		0
		nsLeft		0
		nsTop			21
		nsBottom		139
		nsRight		77
 		description	"the left doorway"
		lookStr		"Outside that doorway is the penthouse's
					rooftop patio."
		sightAngle	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
