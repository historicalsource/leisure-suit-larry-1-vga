;**
;**	Logics for Room 380 -- Penthouse Hot Tub
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Mike Larsen
;**
;**	Last Update:  June 19, 1991
;**


(script# 380)

(public
	rm380		0
)


(local
	looked	= 0
	talked 	= 0
	cycleInterval = 1
	larrySplash
	palDelay

	INTO_TUB_PATH	= [
		0 0 159 136
		0 1 159 136
		0 2 159 136
		0 3 154 114
		0 4 150 105
		0 5 144 94
		0 6 138 92
		0 6 129 111
		0 7 141 133
		0 8 133 134
		PATHEND ]

	EXIT_TUB_PATH	= [
		4 1 117 153
		4 2 121 150
		4 3 125 147
		4 4 129 144
		4 5 133 141
		4 6 137 138
		4 7 144 134
		PATHEND ]

	EXIT_TUB_AND_DRESS_PATH = [
		6 0 118 154
		6 1 124 151
		6 2 130 149
		6 3 136 146
		6 4 142 144
		6 5 148 141
		6 6 154 139
		6 7 159 136
		PATHEND ]
)

(procedure
	JetsOn
	JetsOff
)

(define	pPenthouseTub			380)
(define	vPenthouseTub			380)
	(define	lJets					0)
	(define	lRimJet1				1)
	(define	lRimJet2				2)
	(define	lRimJet3				3)

(define	vLarryInTub				381)
	(define 	lJumping			0)
	(define	lInTub			1)
	(define	lStandsUp		2)
	(define	lGrabsTowel		3)
	(define	lExitTubTowel	4)
	(define	lWalkInTowel	5)
	(define	lExitTubNaked	6)

(define	vEveInTub				382)
	(define	lEveInTub			0)
	(define	lEveGetsOut			1)
	(define	lEveTits				2)
	(define	lEveWalks			3)
	(define	lEvesTowel			5)

(define	vBlowUpDoll				373)
	(define	lBlowUpDoll			0)

;(define	vLarryUndressing		803)
;	(define lRemoveCoat			0)
;	(define lRemovePants			1)
;	(define lJumpInBed			2)


(define	sBubbles					380)
(define	sSplash					384)
;(define	mHotTub					381)
;(define	sCitySounds				800)


(instance rm380 of LLRoom
	(properties
		picture	380
	)
	(method (init)
		(LoadMany VIEW
			vLarryInTub
			vEveInTub
			vPenthouseTub
		)
		(LoadMany SOUND
			mHotTub
			sBubbles
		)
		(if (not (Btst fEveHasApple))
			(Load SOUND sSplash)
			(Load VIEW vLarryUndressing)
		)
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	319 189 0 189 0 0 203 -3 203 81 68 81 48 95 54 114 129 114
						165 141 166 158 163 163 319 111,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	319 108 172 108 148 91 319 73,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	88 92 122 92 122 97 88 97,
				yourself
			)
		)
		(ego
			actions: 		egoActions,
		)
		(eHead actions: egoActions)
		(aEve
			cycleSpeed:		howFast,
			moveSpeed:		howFast,
			setCycle:		Forward,
			approachVerbs:	actorVerbs verbLook,
			init:,
		)
		(if (== prevRoomNum 385)			;**	back from close-up
			(larryClothes
				init:		,
				stopUpd:
			)
			(ego
				view:			vLarryInTub,
				egoSpeed:	(* 2 howFast),
				setLoop:		lInTub,
				z:				0,
				posn:			120 139,
				init:			,
			)
			(if (Btst fJetsOff)
				(JetsOff)
			else
				(JetsOn)
			)
			(if (Btst fEveHasApple)
				(aEve
					cycleSpeed:	(* 2 howFast),
					moveSpeed:	(* 2 howFast),
					setLoop:		lEveGetsOut,
					setCel:		0,
					posn:			84 126,
				)

				(theMusic
					send:			3 mMUTE TRUE,
					send:			7 mMUTE FALSE,
					send:			10 mMUTE FALSE,
				)
			 	(HandsOff)
				(curRoom setScript: sEveLeaves)
			else
				(theMusic
					send:	4 mMUTE TRUE,
					send:	9 mMUTE TRUE,
					send:	5 mMUTE FALSE,
				)
			)
		else										;**	from living room
			(if (Btst fJetsOff)
				(JetsOff)
			else
				(JetsOn)
			)
			(if (Btst fDollFlies)
				(aDoll
					cycleSpeed:	howFast,
					moveSpeed:	howFast,
					init:
				)
				(ego
					posn:			231 113,
				)
				(curRoom setScript: sFlyingDoll)
			else
				(self setScript: sFromLivingroom)
			)
		)

		(towel 
			init:	,
			stopUpd:	,
			approachVerbs: actorVerbs 
		)
		(fStatue
			init:	,
			approachVerbs:	actorVerbs verbLook
		)
		(fSpaButton
			init:	,
			approachVerbs: actorVerbs
		)
		(fHotels
			init:,
			approachVerbs: actorVerbs verbLook
		)
		(fHotTub init:)
		(fLivingRoom init:)
		(fWaterfall init:)
		(ego init:)
		(super init:)
	)

	(method (doit)
		(super doit: &rest)
		(if (== palDelay cycleInterval)
			(= palDelay 0)
		 	(Palette PALCycle 64 71 -1)
		else
			(++ palDelay)
		)
		(cond
			(script)
			((and (ego mover?)
					(== (ego view?) vLarryInTub))
				(HandsOff)
				(curRoom setScript: sGetDressed)
			)
			((SteppedOn ego cBLUE)
				(if (< (ego y?) 100)
					(HandsOff)
					(self setScript: sToLivingroom)
				else
					(curRoom newRoom: 360)
				)
			)
		)
	)

	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "An expansive rooftop garden bathes in the moonlight,
					while a luscious woman bathes in the nude!")
				(Print "You can't believe your eyes.
					She's the girl of your dreams.")
				(Print "(She's even wet!)" LOW)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(class egoActions of Code
	(methods
		doVerb
	)
	(method (doVerb theVerb invItem)
		(if (or	(== theVerb verbDo)
					(== theVerb verbZipper))
			(cond
				((not (Btst fComeOnIn))
					(Print "As much as you'd like to get undressed and join
						that young lady in her hot tub,
						you haven't been invited yet.")
					(Print "(And you know about hotel security...)" LOW)
				)
				((not (Btst fEveKnowsLarry))
					(Print "Perhaps if you introduced yourself to her first...")
				)
				((!= (ego view?) vLarryInTub)
					(curRoom setScript: sUndress)
					(return TRUE)
				)
				(else
					(curRoom setScript: sGetDressed)
					(return TRUE)
				)
			)
		)
	)
)

;
;	SCRIPTS
;

(instance sUndress of Script
	(method (doit)
		(super doit: &rest)
		(if
			(and
				(== larrySplash TRUE)
				(== (ego cel?) 7)
			)
			(= larrySplash FALSE)
			(sfxSplash play:)
		)
	)
	(method (changeState newState)
		(switchto (= state newState)
			(
				(HandsOff)
				(ego
					setMotion:	PolyPath 159 136 self
				)
			)
			(
				(Print "Glancing around, you notice no changing facilities nearby.
					Oh, what the hell....")
				(ego
					egoSpeed:	(+ 1 howFast),
					view:			vLarryUndressing,
					setLoop:		lRemoveCoat,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(ego
					setLoop:		lRemovePants,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(larryClothes init:)
				(= larrySplash TRUE)
				(ego
					view:			vLarryInTub,
					setLoop:		lJumping,
					setPri:		10,
					setCel:		0,
					setCycle:	MoveCycle @INTO_TUB_PATH self
				)
			)
			(
				(ego
					view:			vLarryInTub,
					setPri:		RELEASE,
					x:				120,
					y:				139,
				)
				(if (Btst fJetsOff)
					(ego
						setLoop:		lStandsUp,
						setCel:		0,
						setCycle:	0,
					)
				else
					(ego
						setLoop:		lInTub,
						setCycle:	Forward
					)
				)
				(= seconds 3)
			)
			(
				(Print "Wasting no time, you execute your previously only
					pre-visualized spa hop, landing in the warm water.
					Oooh, does it feel good!
					It's nearly as stimulating as the beautiful naked woman
					next to you.
					With the warmth of the water soaking into your body,
					you quickly become quite relaxed.")
				(fHotels approachVerbs: verbNone)
				(fStatue approachVerbs: verbNone)
				(fSpaButton approachVerbs: verbNone)
				(aEve approachVerbs: verbNone)
				(towel approachVerbs: verbNone)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)
(instance sGetDressed of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(HandsOff)
				(ego
					view:			vLarryInTub,
					setLoop:		lStandsUp,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(ego
					egoSpeed:	(+ 1 howFast),
					view:			vLarryInTub,
					setCycle:	MoveCycle @EXIT_TUB_AND_DRESS_PATH self
				)
			)
			(
				(larryClothes dispose:)
				(ego
					view:			vLarryUndressing,
					setLoop:		lRemovePants,
					setCel:		LastCel,
					posn:			159 136,
					setCycle:	BegLoop self
				)
			)
			(
				(ego
					setLoop:		lRemoveCoat,
					setCel:		LastCel,
					setCycle:	BegLoop self
				)
			)
			(
				(HandsOn)
				(NormalEgo 3)
				(fSpaButton	approachVerbs:	actorVerbs)
				(fStatue	approachVerbs:	actorVerbs verbLook)
				(aEve	approachVerbs:	actorVerbs verbLook)
				(fHotels	approachVerbs:	actorVerbs verbLook)
				(towel approachVerbs: actorVerbs)
				(self dispose:)
			)
		)
	)
)

(instance sEveLeaves of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(aEve
					setLoop:		lEveGetsOut,
					setCel:		0,
					posn:			84 126,
					setCycle:	CycleTo 3 1 self,
				)
			)
			(
				(towel dispose:)
				(aEve setCycle: EndLoop self)
			)
			(
				(aEve
					setLoop:		lEveWalks,
					setCel:		0,
					cycleSpeed:	howFast,
					moveSpeed:	howFast,
					posn:			96 121,
					setCycle:	Walk,
					setMotion:	PolyPath 166 118 self,
				)
				(ego
					setLoop:		lStandsUp,
					setCel:		0,
					setCycle:	EndLoop self,
				)
			)
			(
				(ego
					setLoop:		lGrabsTowel,
					setCel:		0,
					setCycle:	EndLoop self,
				)
			)
			(
				(ego
					egoSpeed:	,
					cycleSpeed:	howFast,
					moveSpeed:	howFast,
					setLoop:		lExitTubTowel,
					setCel:		0,
					posn:			117 153,
					setCycle:	MoveCycle @EXIT_TUB_PATH self,
				)
			)
			(
				NULL
			)
			(
				(Print "\"Say, Larry!\" smiles Eve,
					\"You really ARE glad to see me!\"" HIGH)
				(aEve
					setMotion:	PolyPath 241 105
				)
				(= cycles 8)
			)
			(
				(ego
					egoSpeed:	,
					setLoop:		lWalkInTowel,
					setCycle:	Walk,
					setMotion:	PolyPath 230 113 self
				)
			)
			(
				(curRoom newRoom: 360)
			)
		)
	)
)


(instance sToLivingroom of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(if
					(< (ego y?) 106)
					(ego
						setMotion:	PolyPath 186 86 self
					)
				else
					(ego
						setMotion:	PolyPath 240 110 self
					)
				)

			)
			(
				(curRoom newRoom:	360)
			)
		)
	)
)

(instance sFromLivingroom of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(HandsOff)
				(if (< (ego y?) 116)
					(ego
						posn:			186 84,
						setMotion:	PolyPath 140 95 self
 					)
				else
					(ego
						posn:			231 113,
						setMotion:	PolyPath 205 120 self
					)
				)
			)
			(
				(HandsOn)
				(NormalEgo)
				(self dispose:)
			)
		)
	)
)
(instance sFlyingDoll of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(HandsOff)
				(aDoll
					setCycle:	Forward,
					setMotion:	MoveTo -20 90 
				)
 				(ego
					egoSpeed:	howFast,
					setMotion:	PolyPath	170 131 self
				)
			)
			(
				(aDoll
					setMotion:	MoveTo -35 90 
				)
				(theMusic3 fade: self)
			)
			(
				(NormalEgo 1)
				(HandsOn)
				(UnLoad VIEW vBlowUpDoll)
				(UnLoad SOUND sDollDeflating)
				(Bclr fDollFlies)
				(aDoll dispose:)
				(self dispose:)
			)
		)
	)
)

;*************
;*** SOUND ***
;*************

(instance sfxSplash of Sound
	(properties
		number	sSplash
		vol		127
		loop		1
		flags		1
	)
)


;**************
;*** ACTORS	***
;**************

(instance aEve of Actor
	(properties
		view			vEveInTub
		loop			lEveInTub
		x				85
		y				125
		approachX	148
		approachY	128
		description "Eve"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(if (== (ego view?) vLarryInTub)
			(HandsOn)
			(curRoom newRoom: 385)
			(return)
		)
		(switch theVerb
			(verbLook
				(cond
					((Btst fComeOnIn)
						(Print "You ask yourself, \"Why am I standing here
							when I was invited into a spa with a woman like her?\"")
					)
					((Btst fEveKnowsLarry)
						(Print "\"Hey, handsome, why don't you slip into the water
							with me, so we can REALLY get to know each other!\"")
						(Print "(Was she talking to you, Larry?)" LOW)
						(Bset fComeOnIn)
					)
					(else
						(Print "A beautiful black woman relaxes in the penthouse
							suite's rooftop garden's spa.
							You begin to get the feeling that your evening
							in Lost Wages may be successful after all!")
					)
				)
			)
			(verbTalk
				(cond
					((Btst fComeOnIn)
						(Print "\"Yes, of course I'd like to join you,\"
							you stammer.
							\"In fact, I can think of nothing I'd like better!\"")
						(Print "\"(If I could only figure out how to take off
							these clothes),\" you think." LOW)
					)
					((Btst fEveKnowsLarry)
						(Print "\"Well, uh,\" you stutter,
							\"I've always felt it is a look that's right for me.
							`A man has to do what a man has to do,' I always say.\"")
						(Print "She smiles up at you,
							\"I agree completely.
							I'm so tired of men who wear or say anything
							just to gain a woman's favors!\"")
						(Print "\"Gee,\" you think, \"I would never do that!\"")
						(Print "To her, you say with a smile,
							\"And I so love what YOU are wearing!\"")
						(Print "She laughs,
							\"And, a sense of humor!
							Larry, you are one special man.
							Why don't you slip into the water and we'll see if we
							can REALLY get to know each other!\"")
						(Bset fComeOnIn)
					)
					(else
						(Print "\"Hey, Gorgeous! Allow me to introduce myself,\"
							you say. \"My name is Larry; Larry Laffer.\"")
						(Print "\"Hello, yourself, Larry,\" she replies.
							\"What an attractive leisure suit--I so miss them!
							It's refreshing to meet a man with so much
							self-confidence that he's willing to flaunt
							the fickle trends of fashion mores and deeply travel
							the road of his own secure masculinity.\"")
						(Print "(Was she talking to you, Larry?)" LOW)
						(Bset fEveKnowsLarry)
					)
				)
			)
			(verbDo
				(Print "There's not much you can do from out here.")
			)
			(else
				(Print "Perhaps if you establish eye contact first...")
			)
		)
	)
)

(instance aDoll of Actor
	(properties
		view		vBlowUpDoll
		loop		lBlowUpDoll
		x			143
		y			95
		xStep		7
		yStep		5
		priority 8
		signal 	fixPriOn
	)
)

;**************
;***	PROPS	***
;**************


(instance aJet of Prop
	(properties
		view		vPenthouseTub
		loop		lJets
		cel		0
		x			59
		y			147
		detailLevel 1
		signal 		ignrAct
		description "the jet"
		sightAngle 	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "You wonder if there's any way to turn off those
					bubbles!")
			)
			(else
				(Print "Doesn't that water look inviting?")
			)
		)
	)
)
(instance bJet of Prop
	(properties
		view		vPenthouseTub
		loop		lJets
		cel		0
		x			61
		y			166
		signal ignrAct
		detailLevel 1
		description "the bubble"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(aJet doVerb: theVerb invItem &rest)
	)
)
(instance cJet of Prop
	(properties
		view		vPenthouseTub
		loop		lJets
		cel		0
		x			79
		y			156
		signal ignrAct
		detailLevel 1
		description "the bubbles"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(aJet doVerb: theVerb invItem &rest)
	)
)
(instance dJet of Prop
	(properties
		view		vPenthouseTub
		loop		lJets
		cel		0
		x			101
		y			164
		signal ignrAct
		detailLevel 1
		description "the bubbles"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(aJet doVerb: theVerb invItem &rest)
	)
)
(instance eJet of Prop
	(properties
		view		vPenthouseTub
		loop		lJets
		cel		0
		x			99
		y			148
		signal ignrAct
		detailLevel 1
		description "the bubbles"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(aJet doVerb: theVerb invItem &rest)
	)
)
(instance rimJet1 of Prop
	(properties
		view		vPenthouseTub
		loop		lRimJet1
		cel		0
		x			50
		y			127
		priority 7
		signal (| ignrAct fixPriOn)
		detailLevel 2
		description "the bubbles"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(aJet doVerb: theVerb invItem &rest)
	)
)

(instance rimJet2 of Prop
	(properties
		view		vPenthouseTub
		loop		lRimJet2
		cel		0
		x			52
		y			126
		priority 7
		signal (| ignrAct fixPriOn)
		detailLevel 3
		description "the bubbles"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(aJet doVerb: theVerb invItem &rest)
	)
)
(instance rimJet3 of Prop
	(properties
		view		vPenthouseTub
		loop		lRimJet3
		cel		0
		x			117
		y			131
		priority 7
		signal (| ignrAct fixPriOn)
		detailLevel 4
		description "the bubbles"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(aJet doVerb: theVerb invItem &rest)
	)
)

;*************
;*** VIEWS ***
;*************


(instance towel of View
	(properties
		view	 		vEveInTub
		loop			lEvesTowel
		cel			0
		x				78
		y				109
		approachX 	80
		approachY	105
		description "the towel"			
		lookStr		"Behind her rests an expensive fluffy bath towel."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)


(instance larryClothes of View
	(properties
		view		vLarryUndressing
		loop		3
		cel		0
		x			159
		y			136
		signal ignrAct
		description "your leisure suit"
		lookStr 		"It's a good thing polyester doesn't wrinkle."
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(if (== (ego view?) vLarryInTub)
					(curRoom setScript: sGetDressed)
					(return TRUE)
				)
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

(procedure (JetsOn)
	(= cycleInterval 1)
	(= palDelay 1)
	(aEve
		setLoop:		lEveInTub,
		x:				85,
		y:				125,
		setCycle:	Forward
	)
	(if (== (ego view?) vLarryInTub)
		(ego
			setLoop:		lInTub,
			setCel:		0,
			setCycle:	Forward
		)
	)
	(theMusic2
		number:		sBubbles,
		loop:			-1,
		vol:			127,
		flags:		1,
		play:
	)
	(rimJet1
		init:,
		cycleSpeed:	howFast,
		setCycle:	Forward
	)
	(rimJet2
		init:,
		cycleSpeed:	howFast,
		setCycle:	Forward
	)
	(rimJet3
		init:,
		cycleSpeed:	howFast,
		setCycle:	RandCycle
	)
	(aJet
		init:,
		cycleSpeed:	howFast,
		setCycle:	RandCycle
	)
	(bJet
		init:,
		cycleSpeed:	howFast,
		setCycle:	RandCycle
	)
	(cJet
		init:,
		cycleSpeed:	howFast,
		setCycle:	RandCycle
	)
	(dJet
		init:,
		cycleSpeed:	howFast,
		setCycle:	RandCycle
	)
	(eJet
		init:,
		cycleSpeed:	howFast,
		setCycle:	RandCycle
	)
)

(procedure (JetsOff)
	(= cycleInterval 6)
	(= palDelay 6)
	(aEve
		setLoop:	lEveTits,
		x:			85,
		y:			125,
		stopUpd:,
	)
	(theMusic2 stop:)
	(if (== (ego view?) vLarryInTub)
		(ego
			setLoop:		lStandsUp,
			setCel:		0,
			setCycle:	0
		)
	)
	(rimJet1 dispose:)
	(rimJet2 dispose:)
	(rimJet3 dispose:)
	(aJet dispose:)
	(bJet dispose:)
	(cJet dispose:)
	(dJet dispose:)
	(eJet dispose:)
)

;****************
;*** FEATURES ***
;****************

(instance fSpaButton of Feature
	(properties
		x 				105
		y 				189
		z 				71
		heading 		0
		nsLeft 		98
		nsTop 		110
		nsBottom 	123
		nsRight 		113
		approachX 	101
		approachY 	117
 		description "the button"
		lookStr 		"There's some sort of control button built into the
						surface of the spa's decking."
		sightAngle	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(if (Btst fJetsOff)
					(Bclr fJetsOff)
					(JetsOn)
					(Print "Because you are such a wimp,
						you turn the bubblers back on.")
				else
					(Bset fJetsOff)
					(JetsOff)
					(Print "You coyly \"happen\" to step on the spa bubbler control
						switch.")
				)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance fHotels of Feature
	(properties
		x				112
		y				49
		z				90
		heading		0
		nsLeft		59
		nsTop			33
		nsBottom		66
		nsRight		166
		approachX	84
		approachY	81
 		description	"the hotels"
		sightAngle	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "Looking over at the next hotel,
					you see a window with its drapes wide open.")
				(Print "Say, look at that.
					There's an ugly girl getting undressed.")
				(Print "She's not really very pretty.")
				(Print "Well, she's not bad.")
				(Print "You know, she's looking better.")
				(Print "Say, she's pretty good.")
				(Print "Perhaps you could ignore some of her faults.")
				(Print "You may be in love!")
				(Print "You ARE in love!")
				(Print "Well, at least you're in lust!")
				(Print "Pant, pant, pant, pant, pant.")
			)
			(else
				(Print "Say, what's that in that hotel window across the way?")
			)
		)
	)
)
(instance fStatue of Feature
	(properties
		x				112
		y				52
		z				0
		heading		0
		nsLeft		79
		nsTop			7
		nsBottom		97
		nsRight		145
 		description	"the sculpture"
		lookStr	 	"Look! Hooters on a stick!"
		approachX	126
		approachY	106
		sightAngle	40
		onMeCheck	cLRED
	)
)
(instance fHotTub of Feature
	(properties
		x				72
		y				146
		z				0
		heading		0
		nsLeft		0
		nsTop			124
		nsBottom		169
		nsRight		145
 		description	"the hot tub"
		lookStr		"The hot tub looks so warm and inviting."
		sightAngle	40
	)
	(method (doVerb theVerb invItem)
		(if (or	(== theVerb verbDo)
					(== theVerb verbZipper))
			(egoActions doVerb: theVerb invItem &rest)
			(return)
		)
		(switch theVerb
			(verbTalk
				(aEve doVerb: theVerb invItem &rest)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance fLivingRoom of Feature
	(properties
		x				258
		y				77
		z				0
		heading		0
		nsLeft		204
		nsTop			34
		nsBottom		121
		nsRight		312
 		description	"the penthouse living room"
		lookStr		"You were in that very same living room only
						moments ago."
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
(instance fWaterfall of Feature
	(properties
		x				160
		y				192
		z				0
		heading		0
		nsLeft		0
		nsTop			130
		nsBottom		191
		nsRight		319
 		description	"the waterfall"
		lookStr	 	"A lovely waterfall cascades down lava rockwork through
						a mass of exotic flora."
		sightAngle	40
		onMeCheck	cRED
	)
)

