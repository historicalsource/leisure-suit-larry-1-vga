;**
;**	Logics for Room 310 -- Casino Interior
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford &
;**
;**	Last Update:  June 16, 1991
;**


;						look in view 311 and discard the stuff around the ceiling

; add features at bottom of this file


(script# 310)

(public
	rm310		0
)

(local
	conversationTimer
)

(define CMAX 400)
(define CMIN 200)

;(define	mCasino					310)
;(define	sGreedSqueals			311)
(define	sConversation			312)

(define	pCasinoInterior		310)
(define	vCasinoInterior		310)
(define	vLarryPlayingSlots	311)
(define	vCasinoExtras			312)
	(define lOldLady				0)
	(define lOldLadyEyes			1)
	(define lMan1Push				2)
	(define lMan1Kick				3)
	(define lMan1Eyes				4)
	(define lShortMan				5)
	(define lShortManEyes		6)
	(define lBabePush				7)
	(define lBabeJump				8)
	(define lJane					9)
	(define lRodPush				10)
	(define lRodShrug				11)

(instance rm310 of LLRoom
	(properties
		picture	pCasinoInterior
		horizon	93
		north		330
		south 	300
	)
	(method (init &tmp i)
		(LoadMany SOUND
			mCasino
			sConversation
		)
		(LoadMany VIEW
			vCasinoExtras
			vCasinoInterior
			vLarryPlayingSlots
;			vLarryGetting
		)
;		(curRoom addObstacle:
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	0 189 0 0 116 0 116 78 90 108 26 108 2 131 2 189,
;				yourself
;			)
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	317 189 317 156 300 156 225 125 204 112 178 102 178 0 319 0
;						319 189,
;				yourself
;			)
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	25 158 43 156 112 157 112 171 74 175 51 174 29 174,
;				yourself
;			)
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	28 127 59 126 112 126 112 135 108 139 67 144 47 145 28 138,
;				yourself
;			)
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	26 113 106 113 106 119 25 119,
;				yourself
;			)
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	302 178 259 178 247 180 202 177 202 166 220 156 284 156
;						302 164,
;				yourself
;			)
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	193 118 239 141 239 148 209 148 179 154 153 154 136 141
;						136 131,
;				yourself
;			)
;		)
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	0 189 0 0 116 0 116 78 90 108 26 108 2 131 2 189,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	317 189 317 156 300 156 225 125 204 112 178 102 178 0 319 0
						319 189,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	26 157 112 157 112 174 26 174,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	28 127 59 126 112 126 112 135 108 139 67 144 47 145 28 138,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	26 113 106 113 106 119 25 119,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	302 178 259 178 247 180 202 177 202 166 220 156 284 156
						302 164,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	193 118 239 141 239 148 209 148 179 154 153 154 136 141
						136 131,
				yourself
			)
		)
		(switch prevRoomNum
			(300
				(ego setPri: RELEASE)
 			)
			(330
				(if (< (ego x?) 110)
					(ego x: 110)
				)
				(if (> (ego x?) 170)
					(ego x: 170)
				)
			)
			(250	;blackjack
				NULL
			)
			(260	;slots
				(ego
					z: 	0,
					show:
				)
			)
			(else	;TP
				(ego posn: 160 160)
			)
		)
		(ego init:)
		(super init:)
		(if (!= prevRoomNum 330)
			(theMusic
				number:		mCasino,
				loop:			-1,
				flags:		1,
				play:			90,
			)
		)
		(theMusic3
			number:		sConversation,
			loop:			-1,
			flags:		1,
			play:
		)
		(= conversationTimer (Random CMAX CMIN))
		(oldlady
			cycleSpeed:	howFast,
			init:,
			setScript: 	sGamble NULL FALSE
		)
;		(oldladyeyes
;			cycleSpeed:	howFast,
;			init:,
;			setScript:	sBlink
;		)
		(man1
			cycleSpeed:	howFast,
			init:,
			setScript: (sGamble new:) NULL 2
		)
		(man1eyes
			cycleSpeed:	howFast,
			init:,
			setScript:	(sBlink new:)
		)
		(shortman
			cycleSpeed:	howFast,
			init:,
			setScript: (sGamble new:) NULL FALSE
		)
;		(shortmaneyes
;			cycleSpeed:	howFast,
;			init:,
;			setScript:	(sBlink new:)
;		)
		(babe
			cycleSpeed:	howFast,
			init:,
			setScript: (sGamble new:) NULL 7
		)
		(jane
			cycleSpeed:	howFast,
			init:,
			setScript: (sGamble new:) NULL FALSE
		)
		(rodney
			cycleSpeed:	howFast,
			init:,
			setScript: (sGamble new:) NULL FALSE
		)
		(if (theGame detailLevel?)
			(lightBulb
				cycleSpeed:	howFast,
				setCycle:	Forward,
				init:,
			)
			(flashball
				cycleSpeed:	howFast,
				setCycle:	Forward,
				init:,
			)
			(flashers
				cycleSpeed:	howFast,
				setCycle:	Forward,
				init:,
			)
			(flashingballs2
				cycleSpeed:	howFast,
				setCycle:	Forward,
				init:,
			)
			(flashingsteps
				cycleSpeed:	howFast,
				setCycle:	Forward,
				init:,
			)
			(siren
				cycleSpeed:	howFast,
				setCycle:	Forward,
				init:,
			)
			(chaser1
				cycleSpeed:	howFast,
				setCycle:	Forward,
				init:,
			)
			(chaser2
				cycleSpeed:	howFast,
				setCycle:	Forward,
				init:,
			)
			(chaser3
				cycleSpeed:	howFast,
				setCycle:	Forward,
				init:,
			)
			(chaser4
				cycleSpeed:	howFast,
				setCycle:	Forward,
				init:,
			)
			(screen1
				cycleSpeed:	howFast,
				setCycle:	RandCycle,
				init:,
			)
			(screen2
				cycleSpeed:	howFast,
				setCycle:	RandCycle,
				init:,
			)
			(screen3
				cycleSpeed:	howFast,
				setCycle:	RandCycle,
				init:,
			)
			(screen4
				cycleSpeed:	howFast,
				setCycle:	RandCycle,
				init:,
			)
			(screen5
				cycleSpeed:	howFast,
				setCycle:	RandCycle,
				init:,
			)
			(screen6
				cycleSpeed:	howFast,
				setCycle:	RandCycle,
				init:,
			)
			(screen7
				cycleSpeed:	howFast,
				setCycle:	RandCycle,
				init:,
			)
			(screen8
				cycleSpeed:	howFast,
				setCycle:	RandCycle,
				init:,
			)
			(screen9
				cycleSpeed:	howFast,
				setCycle:	RandCycle,
				init:,
			)
		)
		(blackjack
			approachVerbs:	verbDo verbUse,
			init:
		)
		(BJ4
			approachVerbs:	verbDo verbUse,
			init:
		)
		(BJ2
			approachVerbs:	verbDo verbUse,
			init:
		)
		(slots
			approachVerbs:	verbDo verbUse,
			init:
		)
		(slots2
			approachVerbs:	verbDo verbUse,
			init:
		)
		(paintingLeft
			init:,
		)
		(paintingRight
			init:,
		)
		(farRoom
			init:,
		)
		(planterLeft
			init:,
		)
		(planterRight
			init:,
		)
		(fSlotMachines
			init:,
		)
		(fBlackjackMachines
			init:,
		)
	)
	(method (doit)
		(super doit: &rest)
		(if conversationTimer
			(-- conversationTimer)
			(if
				(== conversationTimer 1)
				(= conversationTimer (Random CMIN CMAX))
				(if
					(== (theMusic3 vol?) 127)
					(theMusic3
						fade: 90 5 1 0
					)
				else
					(theMusic3
						fade: 127 5 1 0
					)
				)
			)
		)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "You are in the casino.
					There are slot machines on the right,
					and video blackjack machines on the left.
					This place is a gambler's paradise!")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
	(method (newRoom n)
		(super newRoom: n)
		(theMusic3 fade:)
		(cond
			((== n 300)
				(ego setPri: 6)
				(theMusic fade:)
			)
			((== n 320)
				(theMusic fade:)
			)
		)
	)
)

;
;	SCRIPTS
;

(instance sBlink of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= cycles (Random 2 20))
			)
			(
				(client
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(self init:)
			)
		)
	)
)


(instance sGamble of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= seconds (Random 3 6))
			)
			(
				(cond
					(register
						(client
							setCel:		0,
							setLoop: 	register,
							setCycle:	EndLoop,
						)
						(= seconds (Random 2 4))
					)
					((== client jane)
						(client
							setCel:			0,
							setCycle:		Forward,
						)
						(= seconds (Random 1 2))
					)
					(else
						(client
							setCel:			0,
							setCycle:		EndLoop self
						)
					)
				)
			)
			(
				(if register
					(client
						setCel:		0,
						setLoop:		(+ register 1),
						setCycle:	(if (== client babe) Forward else EndLoop),
					)
					(= seconds (Random 2 4))
				else
					(client setCel: 0)
					(self init:)
				)
			)
			(
				(self init:)
			)
		)
	)
)


;
; 	PROPS
;

(instance lightBulb of Prop
	(properties
		view			vCasinoInterior
		loop			0
		x				223
		y				132
		priority		pLMAGENTA
		detailLevel 2
		signal		(| fixPriOn ignrAct)
	)
)

(instance chaser1 of Prop
	(properties
		view			vCasinoInterior
		loop			1
		x				273
		y				173
		detailLevel	2
		priority		pLRED
		signal		(| fixPriOn ignrAct)
	)
)

(instance chaser2 of Prop
	(properties
		view			vCasinoInterior
		loop			2
		x				230
		y				181
		detailLevel	2
		priority		pLRED
		signal		(| fixPriOn ignrAct)
	)
)

(instance screen1 of Prop
	(properties
		view			vCasinoInterior
		loop			3
		x				219
		y				149
		priority		pLMAGENTA
		illegalBits	$0000
		signal		(| fixPriOn ignrAct)
		detailLevel	3
		description "the screen"
	)
)

(instance screen2 of Prop
	(properties
		view			vCasinoInterior
		loop			4
		x				251
		y				146
		priority		pLMAGENTA
		signal		(| ignrAct fixPriOn)
		detailLevel	3
		description "the screen"
	)
)

(instance screen3 of Prop
	(properties
		view			vCasinoInterior
		loop			5
		x				274
		y				141
		priority		pLRED
		signal		(| ignrAct fixPriOn)
		detailLevel	3
		description "the screen"
	)
)

(instance flashball of Prop
	(properties
		view			vCasinoInterior
		loop			6
		x				71
		y				127
		priority		pLMAGENTA
		signal		(| fixPriOn ignrAct)
		detailLevel	1
	)
)

(instance flashers of Prop
	(properties
		view			vCasinoInterior
		loop			7
		x				84
		y				166
		priority		pLMAGENTA
		signal		(| ignrAct fixPriOn)
		detailLevel	1
	)
)

(instance chaser3 of Prop
	(properties
		view			vCasinoInterior
		loop			8
		x				73
		y				178
		detailLevel	2
		priority		12
		signal		(| ignrAct fixPriOn)
	)
)

(instance screen4 of Prop
	(properties
		view			vCasinoInterior
		loop			9
		x				46
		y				132
		priority		pLMAGENTA
		illegalBits	$0000
		signal		(| fixPriOn ignrAct)
		detailLevel	3
		description "the screen"
	)
)

(instance screen5 of Prop
	(properties
		view			vCasinoInterior
		loop			10
		x				87
		y				137
		priority		pLMAGENTA
		signal		(| ignrAct fixPriOn)
		detailLevel	3
		description "the screen"
	)
)

(instance flashingballs2 of Prop
	(properties
		view			vCasinoInterior
		loop			11
		x				82
		y				79
		priority		pLGREEN
		signal		(| ignrAct fixPriOn)
		detailLevel	1
	)
)

(instance flashingsteps of Prop
	(properties
		view			vCasinoInterior
		loop			12
		x				46
		y				103
		priority		pLGREEN
		signal		(| fixPriOn ignrAct)
		detailLevel	1
	)
)

(instance screen6 of Prop
	(properties
		view			vCasinoInterior
		loop			13
		x				87
		y				109
		priority		pLGREEN
		signal		(| fixPriOn ignrAct)
		detailLevel	3
		description "the screen"
	)
)

(instance screen7 of Prop
	(properties
		view			vCasinoInterior
		loop			14
		x				54
		y				105
		priority		pLGREEN
		signal		(| fixPriOn ignrAct)
		detailLevel	3
		description "the screen"
	)
)

(instance chaser4 of Prop
	(properties
		view			vLarryPlayingSlots
		loop			0
		x				189
		y				150
		detailLevel	2
		priority		pLGREEN
		signal		(| ignrAct fixPriOn)
	)
)

(instance screen8 of Prop
	(properties
		view			vLarryPlayingSlots
		loop			1
		x				184
		y				122
		priority		pLGREEN
		signal		(| fixPriOn ignrAct)
		detailLevel	3
		description "the screen"
	)
)

(instance screen9 of Prop
	(properties
		view			vLarryPlayingSlots
		loop			2
		x				208
		y				122
		priority		pLGREEN
		signal		(| fixPriOn ignrAct)
		detailLevel	3
		description "the screen"
	)
)

(instance siren of Prop
	(properties
		view			vLarryPlayingSlots
		loop			4
		x				210
		y				107
		priority		pLGREEN
		signal		(| fixPriOn ignrAct)
		detailLevel	1
	)
)

(instance oldlady of Prop
	(properties
		view			vCasinoExtras
		loop			lOldLady
		x				158
		y				142
		description "Bertha"
		detailLevel	1
		signal		ignrAct
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "They appear to be having a great time.
					Perhaps you'd like to join them?")
			)
			(verbTalk
				(Print "Don't interrupt them; they're trying to win.")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

;(instance oldladyeyes of Prop
;	(properties
;		view			vCasinoExtras
;		loop			lOldLadyEyes
;		x				155
;		y				143
;		z				34
;		signal		ignrAct
;		description "Bertha"
;		detailLevel	1
;	)
;	(method (doVerb theVerb invItem)
;		(oldlady doVerb: theVerb invItem)
;	)
;)

(instance man1 of Person
	(properties
		view			vCasinoExtras
		loop			lMan1Push
		x				176
		y				152
		description "Ernie"
		detailLevel	1
		signal		ignrAct
	)
	(method (doVerb theVerb invItem)
		(if (or (== theVerb verbLook)
					(== theVerb verbTalk))
			(oldlady doVerb: theVerb invItem)
		else
			(super doVerb: theVerb invItem)
		)
	)
)

(instance man1eyes of Prop
	(properties
		view			vCasinoExtras
		loop			lMan1Eyes
		x				179
		y				153
		z				33
		signal		ignrAct
		description "Ernie"
		detailLevel	1
	)
	(method (doVerb theVerb invItem)
		(man1 doVerb: theVerb invItem)
	)
)

(instance shortman of Prop
	(properties
		view			vCasinoExtras
		loop			lShortMan
		x				220
		y				145
		description "Bert"
		detailLevel	1
		priority		pLCYAN
		signal		(| ignrAct fixPriOn)
	)
	(method (doVerb theVerb invItem)
		(if (or (== theVerb verbLook)
					(== theVerb verbTalk))
			(oldlady doVerb: theVerb invItem)
		else
			(super doVerb: theVerb invItem)
		)
	)
)

;(instance shortmaneyes of Prop
;	(properties
;		view			vCasinoExtras
;		loop			lShortManEyes
;		x				217
;		y				148
;		z				30
;		signal		ignrAct
;		description "Bert"
;		detailLevel	1
;	)
;	(method (doVerb theVerb invItem)
;		(shortman doVerb: theVerb invItem)
;	)
;)

(instance babe of Person
	(properties
		view			vCasinoExtras
		loop			lBabePush
		x				284
		y				176
		signal		ignrAct
		description "the babe"
		detailLevel	1
	)
	(method (doVerb theVerb invItem)
		(if (or (== theVerb verbLook)
					(== theVerb verbTalk))
			(oldlady doVerb: theVerb invItem)
		else
			(super doVerb: theVerb invItem)
		)
	)
)

(instance jane of Person
	(properties
		view			vCasinoExtras
		loop			lJane
		x				56
		y				137
		z				37
		description "Jane"
		detailLevel	1
		priority		pLGREEN
		signal		(| ignrAct fixPriOn)
	)
	(method (doVerb theVerb invItem)
		(if (or (== theVerb verbLook)
					(== theVerb verbTalk))
			(oldlady doVerb: theVerb invItem)
		else
			(super doVerb: theVerb invItem)
		)
	)
)

(instance rodney of Prop
	(properties
		view			vCasinoExtras
		loop			lRodPush
		x				51
		y				128
		description "Rodney"
		detailLevel	1
		priority		pLBLUE
		signal		(| ignrAct fixPriOn)
	)
	(method (doVerb theVerb invItem)
		(if (or (== theVerb verbLook)
					(== theVerb verbTalk))
			(oldlady doVerb: theVerb invItem)
		else
			(super doVerb: theVerb invItem)
		)
	)
)

;
;  	Features
;

(instance blackjack of Feature
   (properties
      x 84
      y 145
      z 0
      heading 0
      nsLeft 68
      nsTop 122
      nsBottom 169
      nsRight 100
      description "a blackjack machine"
      lookStr "You can remember when blackjack was always played at
					a real table with a real dealer.
					Now all you see are video machines."
      sightAngle 	40
		approachX	95
		approachY	174
   )
   (method (doVerb theVerb invItem)
      (switch theVerb
			(verbDo
				(if (< larryDollars 10000)
					(Printf "You insert your $%d into the machine and brace
						yourself for an invigorating round of
						electronic blackjack."
						larryDollars
					)
					(theMusic fade:)
					(curRoom newRoom: 250)
				else
					(Print "Sorry, Larry. You've already made the big bucks.")
				)
			)
			(verbUse
				(switch invItem
					(iWallet
						(self doVerb: verbDo)
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
(instance slots of Feature
   (properties
      x 223
      y 154
      z 0
      heading 0
      nsLeft 212
      nsTop 139
      nsBottom 169
      nsRight 234
      description "a slot machine"
      lookStr 		"You can remember when slot machines had real wheels and
						gears. Now all you see are video machines."
      sightAngle 	40
		approachX	202
		approachY	174
   )
   (method (doVerb theVerb invItem)
      (switch theVerb
			(verbDo
				(if (< larryDollars 10000)
					(Print "You slip all of your money into the slot machine and
						settle in for a few relaxing minutes with your one-armed
						bandit.")
					(theMusic fade:)
					(curRoom newRoom: 260)
				else
					(Print "Sorry, Larry. You've already made the big bucks.")
				)
			)
			(verbUse
				(switch invItem
					(iWallet
						(self doVerb: verbDo)
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

(instance BJ4 of Feature
	(properties
		x 81
		y 137
		z 0
		heading 0
		nsLeft 67
		nsTop 92
		nsBottom 122
		nsRight 96
		sightAngle 	40
		approachX	94
		approachY	145
	)
	(method (doVerb theVerb invItem)
		(blackjack doVerb: theVerb invItem)
	)
)

(instance BJ2 of Feature
	(properties
		x				 53
		y				 142
		z				 0
		heading		 0
		nsLeft		 41
		nsTop			 125
		nsBottom		 159
		nsRight		 65
		sightAngle	 40
		approachX	 36
		approachY	 174
	)
	(method (doVerb theVerb invItem)
		(blackjack doVerb: theVerb invItem)
	)
)

(instance slots2 of Feature
	(properties
		x 248
		y 150
		z 0
		heading 0
		nsLeft 235
		nsTop 134
		nsBottom 167
		nsRight 		262
		sightAngle 	40
		approachX	256
		approachY	179
	)
	(method (doVerb theVerb invItem)
		(slots doVerb: theVerb invItem)
	)
)

;more features
(instance paintingLeft of Feature
	(properties
		x				 46
		y				 108
		z				 51
		heading		 0
		nsLeft		 17
		nsTop			 46
		nsBottom		 69
		nsRight		 76
 		description	"the painting"
		lookStr		 "What a lovely painting, although it does
					seem strangely familiar."
		sightAngle	 40
	)
)
(instance paintingRight of Feature
	(properties
		x				 268
		y				 141
		z				 56
		heading		 0
		nsLeft		 231
		nsTop			 60
		nsBottom		 110
		nsRight		 306
 		description	"the painting"
		lookStr		 "You never tire of art like this!"
		sightAngle	 40
	)
)
(instance farRoom of Feature
	(properties
		x				 158
		y				 59
		z				 0
		heading		 0
		nsLeft		 92
		nsTop			 37
		nsBottom		 81
		nsRight		 224
 		description	"the next room"
		lookStr		 "Peering through the casino, you can see the next room
						and a lovely planter filled with colorful plants."
		sightAngle	 40
	)
)
(instance planterLeft of Feature
	(properties
		x				 40
		y				 93
		z				 0
		heading		 0
		nsLeft		 0
		nsTop			 72
		nsBottom		 114
		nsRight		 80
 		description	"the planter"
		lookStr		 "You just love these colorful plastic plants."
		sightAngle	 40
	)
)
(instance planterRight of Feature
	(properties
		x				 255
		y				 122
		z				 0
		heading		 0
		nsLeft		 192
		nsTop			 91
		nsBottom		 153
		nsRight		 319
 		description	"the planter"
		lookStr		 "You just love that planter filled with
					plastic plants."
		sightAngle	 40
	)
)
(instance fSlotMachines of Feature
	(properties
		x				 230
		y				 140
		z				 0
		heading		 0
		nsLeft		 170
		nsTop			 101
		nsBottom		 180
		nsRight		 291
 		description	"the slot machines"
		lookStr		 "Exciting video slot machines await
					your money."
		sightAngle	 40
	)
)
(instance fBlackjackMachines of Feature
	(properties
		x				 73
		y				 135
		z				 0
		heading		 0
		nsLeft		 42
		nsTop			 95
		nsBottom		 176
		nsRight		 105
 		description	"the video blackjack machines"
		lookStr		 "The latest state-of-the-gambling-art video
					blackjack machines seem to draw your billfold from your pocket
					directly to their yawning maws!"
		sightAngle	 40
	)
)

