;**
;**	Logics for Room 170 -- Dark Alley
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Mike Larsen
;**
;**	Last Update:  June 16, 1991
;**


(script# 170)

(public
	rm170		0
)

(define pDarkAlley				170)

	(define vThug					172)
		(define lThugRight		0)
		(define lThugLeft			1)

;	(define vLarryUnderGround 812)
;		(define lLarryGoesUnder	1)

(define	mDarkAlley			170)
;(define	sGroundOpens		191)
;(define	sFight				171)
(define	sDizzy			  	172)
(define	sBounce				173)


(instance rm170 of LLRoom
	(properties
		picture	170
	)
	(method (init &tmp foo)
		(Load SCRIPT CHASE)
		(LoadMany VIEW
			vThug vLarryMugged vLarryUnderGround
		)
		(LoadMany SOUND
			mDarkAlley sElevator sFight	sDizzy sBounce
		)
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	0 174 0 0 319 0 319 171 253 138 221 146 188 133 67 134 0 175,
				yourself
			)
		)

		(theMusic
			number:	mDarkAlley,
			vol:		127,
			loop:		-1,
			flags:  	1
		)
		(switch prevRoomNum
			(100
				(if (> (ego x?) 160)
					(self west: prevRoomNum)
				else
					(self east: prevRoomNum)
				)
			)
			(500
				(self east: prevRoomNum)
			)
			(600
				(self west: prevRoomNum)
			)
			(300
				(self east: prevRoomNum)
			)
			(400
				(self west: prevRoomNum)
			)
			(else	;TP
				(self east: 100)
			)
		)
		(sfxFight init:)
		(sfxDizzy init:)
		(sfxBounce init:)
		(aThug
			init:				,
			cycleSpeed:		(theGame egoMoveSpeed?),
			moveSpeed:		(theGame egoMoveSpeed?),
			setScript: 		sChased,
		)
		(ego
			init:,
			y:		180
		)
		(super init:)
		(dumpster init:)
		(fence init:)
	)
	(method (newRoom n)
		(theMusic fade:)
		(super newRoom: n)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "A sense of foreboding washes over your heart when your
					eyes slowly examine this dark alley that seems to be just
					outside every public area in Lost Wages.
					Is that just another kindly citizen or a vicious mugger quickly
					approaching you?")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)


;  Scripts

(instance sChased of Script
	(method (doit)
		(super doit: &rest)
		(if
			(!= (aThug moveSpeed?) (theGame egoMoveSpeed))
			(aThug
				cycleSpeed:		(theGame egoMoveSpeed?),
				moveSpeed:		(theGame egoMoveSpeed?),
			)
		)
	)
	(method (changeState newState)
		(switchto (= state newState)
			(
				(theMusic play:)
				(aThug
					setPri:			1,
					loop:				lThugLeft,

					setMotion:		MoveTo 223 131 self
				)
			)
			(
				(aThug
					setPri:			3,
					setCycle:		Walk,
					setMotion:		MoveTo 167 131	self
				)
			)
			(
				(aThug
					setCycle:		Walk,
					setPri:			RELEASE,
					setMotion:		Chase ego 20 self
				)
			)
			(
				(curRoom setScript: sMugged)
				(self dispose:)
			)
		)
	)
)
(instance sMugged of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(HandsOff)
				(ego hide:)
				(= speed (+ 4 howFast))
				(sfxFight play:)
				(aThug
					view:				vLarryMugged,
					setLoop:			lLarryMugged,
					cycleSpeed:		0,
					moveSpeed:		0,
					setCycle:		Forward,
					setMotion:		MoveTo 175 160	self
				)
			)
			(
				(aThug
					setLoop:			lLarryEscape,
					setMotion:		FALSE,
				)
				(= cycles 3)
			)
			(
				(aThug
					observeBlocks:	punkCircle,
					setLoop:			lLarryMugged,
					setCycle:		Forward,
					setMotion:		Wander
				)
				(= seconds 5)
			)
			(
				(aThug
					setLoop:			lLarryEscape,
					setMotion:		FALSE,
				)
				(= cycles 3)
			)
			(
				(aThug
					observeBlocks:	punkCircle,
					setLoop:			lLarryMugged,
					setMotion:		Wander
				)
				(= seconds 5)
			)
			(
				(aThug
					setLoop:			lLarryEscape,
					setMotion:		FALSE,
				)
				(= cycles 3)
			)
			(
				(aThug
					observeBlocks:	punkCircle,
					setLoop:			lLarryMugged,
					setMotion:		MoveTo 116 165 self
				)
			)
			(
				(aThug2 				;Larry fighting with himself
					init:,
					setCycle:		Walk,
					setMotion:		MoveTo 184 249	self
				)
			)
			(
				(theMusic stop:)
				(sfxFight stop:)
				(aThug
					hide:				,
					z:					1000
				)
				(sfxDizzy play:)
				(ego
					show:,
					view:				vLarryMugged,
					setLoop:			lLarryCroaks,
					x:					104,
					y:					163,
					cycleSpeed:		1,
					setCycle:		CycleTo 9 1 self
				)
			)
			(
				(sfxDizzy stop:)
				(sfxBounce play:)
				(ego
					setCycle:		EndLoop self
				)
			)
			(
				(sfxBounce stop:)
				(= seconds 4)
			)
			(
				(Print "Larry, when are you gonna learn to stay
					out of these dark alleys!!"
				)
				(= seconds 4)
			)
			(
				(Print "\"Hey, guys!! He's screwed up again!
					Dump this one down the tubes, and get him another body!\""
				)
				(= seconds 2)
			)
			(
				(theMusic2
					number:	sElevator,
					setLoop:	-1,
					flags:	1,
					play:
				)
				(sfxGroundOpens play:)
				(ego
					view:			vLarryUnderGround,
					setLoop:		lLarryGoesUnder,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(Cls)
				(curRoom newRoom: 190)
			)
		)
	)
)

;**************
;*** SOUNDS	***
;**************

(instance sfxGroundOpens of Sound
	(properties
		number	sGroundOpens
		loop		1
		vol		127
		flags		1
	)
)
(instance sfxFight of Sound
	(properties
		number	sFight
		loop		-1
		vol		127
		flags		1
	)
)
(instance sfxDizzy of Sound
	(properties
		number	sDizzy
		loop		1
		vol		127
		flags		1
	)
)
(instance sfxBounce of Sound
	(properties
		number	sBounce
		loop		1
		vol		127
		flags		1
	)
)

; cage
;

(instance punkCircle of Cage
	(properties
		top			137
		left			65
		bottom		189
		right			265
	)
)

;**************
;*** ACTORS	***
;**************

(instance aThug2 of Actor
	(properties
		view	 		vThug
		loop			lThugRight
		x				124
		y				174
	)
)
(instance aThug of Actor
	(properties
		view	vThug
		loop	lThugLeft
		cel	0
		x		223
		y		150
		xStep	5
		yStep	3
		priority 2
		description	"the mugger"
		lookStr 		"It appears he wants something -- YOU!
						Quick! Get out of here, Larry!"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
      (switch theVerb
			(verbTalk
				(Print "\"Pardon me, buddy,\" you say,
					\"do you have any spare change?\"")
				(Print "(He seems unamused!)" LOW)
			)
         (verbDo
				(Print "\"Hey, you!\" you challenge,
					\"come over here and fight like a man!\"")
				(Print "(Dumb idea, Larry!)" LOW)
			)
			(verbZipper
				(Print "Your attempt to frighten the mugger fails.")
			)
			(verbSmellTaste
				(Print "Phew!")
			)
			(verbUse
				(Print "There's only one thing he wants, Larry:
					everything!")
			)
         (else
            (super doVerb: theVerb invItem &rest)
         )
      )
   )
)

;Features

(instance dumpster of Feature
	(properties
		x				 222
		y				 107
		z				 0
		heading		 0
		nsLeft		 191
		nsTop			 84
		nsBottom		 131
		nsRight		 254
 		description	 "the dumpster"
		lookStr		 "You think you've seen this dumpster before!"
		sightAngle	 40
	)
)
(instance fence of Feature
	(properties
		x				 144
		y				 71
		z				 0
		heading		 0
		nsLeft		 84
		nsTop			 41
		nsBottom		 102
		nsRight		 205
 		description	"the fence"
		lookStr		 "You think you've seen this fence before!"
		sightAngle	 40
	)
)

