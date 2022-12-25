;**
;**	Logics for Room 120 -- Lefty's Back Room
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford &
;**
;**	Last Update:  June 20, 1991
;**


(script# 120)

(public
	rm120		0
)


(local
	drunkMessage
	seenTPmsg
	nutsMessage
	whichMessage
	RATPTS = [
	2 0 249 128
	2 1 225 119
	2 2 202 130
	2 3 196 136
	2 4 188 134
	2 5 167 117
	2 6 142 87
	2 7 121 63
	PATHEND]
)

(define tableX 118)
(define tableY	142)
(define doorX 216)
(define doorY 130)
(define drunkX	229)
(define drunkY 143)
(define drunkVerbs verbDo verbTalk verbUse)

(define pStoreRoom				120)
(define vStoreRoom				120)
	(define lRose					0)
	(define lRatEat				1)
	(define lRatJump				2)
	(define lDoor					3)
(define vDrunk						122)
	(define lBody					0)
	(define lHeadUp				1)
	(define lHeadTalk				2)
	(define lHeadDrink			3)
	(define lArmWhiskey			4)
	(define lArmRemote			5)
	(define lLeg					6)

;(define	sDrinking				111)
(define	mDrunk					120)
;(define	sDoorOpen				801)
;(define	sDoorClose				802)
(define sHicUp						121)

(instance rm120 of LLRoom
	(properties
		picture	pStoreRoom
		south		110
		east		130
	)
	(method (init)
		(LoadMany SOUND
			sDrinking
;			mDrunk
			mBarInterior
			sDoorOpen
			sDoorClose
			sHicUp
		)
		(LoadMany VIEW
			vStoreRoom
			vDrunk
			vLarryGetting
		)
		(theMusic3
			loop:			1,
			vol:			127,
			flags:		1,
		)
;		(curRoom addObstacle:
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	0 189 0 0 319 0 319 189 313 189 259 152 213 149 210 145
;						242 143 240 140 213 140 211 136 242 135 227 128 215 128
;						192 118 140 118 137 133 100 146 85 146 85 153 107 158 107 163
;						77 174 81 182 81 189,
;				yourself
;			)
;		)
		; Polygon Editor 1.1
		; Dynamic Obstacles : Picture 120
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	0 189 0 0 319 0 319 189 313 189 259 152 213 149 210 145
						240 142 240 140 213 140 211 136 242 135 227 128 215 128
						192 118 140 118 137 133 100 146 85 146 85 153 107 158 107 163
						77 174 81 182 81 189,
				yourself
			)
		)
		(ego init:)
		(switch prevRoomNum
			(110
				(if (Random 0 1)
					(aRat
						cycleSpeed:	howFast,
						init:			,
						setScript:	sRatScript
					)
				)
			)
			(130
				(aDoor
					setCel:		255
				)
				(HandsOff)
				(self setScript: sFromToilet)
				(theMusic
					number:		mBarInterior,
					loop:			-1,
					flags:		1,
					play:			90,
				)
			)
			(else	;TP
				(ego
					posn:		160 160,
				)
			)
		)

		(if (== ((Inventory at: iRose) owner?) 120)
			(aRose
				init:			,
				approachVerbs:	objectVerbs,
				stopUpd:
			)
			(table
				lookStr:		"There is a single, lovely, long-stemmed, red rose
					in a delicate bud vase standing incongruously on the table."
			)
		else
			(table lookStr:
				"Isn't it funny how you can see the top of that table
					even though you're looking at it from the side?"
			)
		)
		(aDoor
			cycleSpeed:		howFast,
			init:				,
			stopUpd:			,
			approachVerbs:	verbDo
		)
		(aDrunk
			cycleSpeed:		howFast,
			init:				,
			stopUpd:			,
			approachVerbs:	drunkVerbs
		)
		(drunkHead
			cycleSpeed:		howFast,
			init:				,
			approachVerbs:	drunkVerbs
		)
		(drunkLeg
			init:				,
			stopUpd:			,
			approachVerbs:	drunkVerbs
		)
		(drunkArm
			cycleSpeed:		howFast,
			init:				,
			approachVerbs:	drunkVerbs
		)

		;add features
		(boxes 	init:)
		(barrels init:)
		(table
			init:		,
			approachVerbs:	verbDo
		)
		(transom init:)
		(theFan init:)
		(theLight init:)
		(super 	init:)
	)
	(method (doit)
		(super doit: &rest)
		(cond
			(script)

			((and (Btst fToiletPaperOnShoe)
					( > (ego y?) 140)
					(not drunkMessage)
					(not seenTPmsg)
					(== prevRoomNum 130))
				(= drunkMessage 4)
				(= seenTPmsg TRUE)
				(aDrunk setScript: sDrunkTalks)
			)

			((SteppedOn ego cGREEN)
				(if (> nutsMessage -1)
					(if whichMessage
						(Print "\"Wadda ya tryin' to do; make me a soprano??\"")
						(=	whichMessage 0)
					else
						(Print "\"Geeze! Them pointy-toed shoes hurt!\"")
						(= whichMessage 1)
					)
				)
				(= nutsMessage -1)
			)
			((SteppedOn ego cCYAN)
				(if (< nutsMessage 0)
					(= nutsMessage TRUE)
				)
			)
		)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "You are in a dimly lit hallway.
					The paint peeling off the walls gives the cockroaches
					something to watch.
					An old table is pushed against the west wall.
					A filthy drunk wearing filthy clothes sits on the filthy floor,
					leaning his filthy back against the filthy wall.")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

;
;	SCRIPTS
;

(instance sRatScript of Script
	(method (doit)
		(super doit: &rest)
		(if (and	(< (ego distanceTo: aRat) 80)
					(< state 2))
			(self state: 1)
			(= cycles 1)
		)
	)
	(method (changeState newState)
		(switchto (= state newState)
			(
				(aRat
					setCycle: EndLoop
				)
				(= seconds (Random 3 6))
			)
			(
				(self init:)
			)
			(
				(aRat
					setLoop:		lRatJump,
					setCel:		0,
					setPri:		(- (ego priority?) 2),
					setCycle:	MoveCycle @RATPTS self,
				)
			)
			(
				(aRat
					dispose:		,
				)
				(self dispose:)
			)
		)
	)
)


(instance sFromToilet of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					x:				262,
					y:				125,
					setPri:		(- (ego priority?) 2),
					setMotion: 	PolyPath doorX doorY self
				)
			)
			(
				(theMusic3
					number:		sDoorClose,
					loop:			1,
					play:
				)
				(aDoor
					setCycle:	BegLoop self
 				)
				(ego setPri: RELEASE)
			)
			(
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sToToilet of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(theMusic3
					number:		sDoorOpen,
					loop:			1,
					play:
				)
				(aDoor setCycle: EndLoop)
				(ego
					egoSpeed:,
					setPri:		(- (ego priority?) 1),
					setMotion: 	MoveTo 262 (ego y?) self
				)
			)
			(
				(ego
					userSpeed:,
					setPri: 		RELEASE,
				)
				(theMusic fade:)
				(curRoom newRoom: 130)
			)
		)
	)
)

(instance sDrunkTalks of Script		;messages 1 & 4
	(method (changeState newState)
		(switchto (= state newState)
			(
				(if (== (drunkHead loop?) lHeadUp)
					(drunkHead setCycle: EndLoop self)
				else
					(= cycles 1)
				)
			)
			(
				(drunkHead
					loop:			lHeadTalk,
					cel:			0,
					setCycle:	EndLoop self
				)
			)
			(
				(switch drunkMessage
					(1
						(Print "\"Hiya shhhhonny.
							How's 'bout you 'n' me havin' a lil' drink?\"")
					)
					(4
						(Print "\"Jeeezz! Somethin' die in there??\""							)
					)
				)
				(= cycles 1)
			)
			(
				(self dispose:)
			)
		)
	)
)

(instance sDrunkDrinks of Script		;messages 2 & 3
	(method (doit)
		(super doit: &rest)
		(if (and (== state 5)
					(== (theMusic3 prevSignal?) -1))
			(= cycles 1)
		)
	)
	(method (changeState newState)
		(switchto (= state newState)
			(
				(if (== (drunkHead loop?) lHeadUp)
					(drunkHead setCycle: EndLoop self)
				else
					(= cycles 1)
				)
			)
			(
				(ego
					egoSpeed:,
					view:			vLarryGetting,
					loop:			lLowRight,
					cel:			0,
					setCycle:	EndLoop self
				)
			)
			(
				(drunkArm
					loop:			lArmWhiskey,
					cel:			0,
					setCycle:	EndLoop self
				)
			)
			(
				(ego
					setCycle:	BegLoop self
				)
				(drunkArm
					setCycle:	BegLoop
				)
			)
			(
				(drunkHead
					loop:			lHeadDrink,
					cel:			0,
					setCycle:	EndLoop self
				)
			)
			(;5 is current state
				(theMusic3
					number:		sDrinking,
					loop:			1,
					play:
				)
				(Print "Glug, glug, glug, glug, glug." HIGH #dispose:)
			)
			(
				(NormalEgo 0)
				(HandsOn)
				(Cls)
				(drunkHead
					setCycle:	BegLoop self
				)
			)
			(
				(drunkHead
					loop:			lHeadTalk,
					cel:			0,
					setCycle:	EndLoop self
				)
			)
			(
				(switch drunkMessage
					(2
						(Print "\"Shhaay, thanksh buddy! How 'bout sshhom'ore?\"")
					)
					(3
						(Print "Slurp! \"Ahhhh! That did hit the ol' spot!\"")
						(Print "\"Ya know, ya mus' be ma only frien' in the whole
							world! Shhoooo, I'm gonna give ya my only posshhess...
							hhic!")
						(theMusic3
							number:		sHicUp,
							setLoop:		1,
							play:
						)
						(Print "\"...my only posshhess... hhic!")
						(theMusic3
							play:
						)
						(Print "\"...my only posshhess... hhic!")
						(theMusic3
							play:
						)
						(Print "\"...my only posshhess... hhic!")
						(theMusic3
							play:
						)
						(if (or (ego mover?)
								  (!= (ego x?) drunkX)
								  (!= (ego y?) drunkY))
							;he moved, so don't give him remote
							(Print  "\"Oh well, since you're leavin' anyway,
								jes' forgit it.\"")
							(= drunkMessage 2)
						else
							(HandsOff)
							(Print "\"...all I got in the world.\"")
							(Print "\"Besides, what'sha guy like me need witta
								television remote control!?\"")
						)
					)
				)
				(if (== drunkMessage 3)
					(+= state 1)
				)
				(= cycles 1)
			)
			(
				(self dispose:)
			)
			(
				(drunkArm
					loop:			lArmRemote,
					cel:			0,
					setCycle:	EndLoop self
				)
			)
			(
				(Points pGotRemote ptsGotRemote)
				(ego
					egoSpeed:,
					get: 			iRemoteControl,
					view:			vLarryGetting,
					loop:			lLowRight,
					cel:			0,
					setCycle:	EndLoop self
				)
			)
			(
				(ego
					setCycle:	BegLoop self
				)
			)
			(
				(drunkArm
					loop:			lArmRemote,
					cel:			0,
					setCycle:	BegLoop self
				)
			)
			(
				(NormalEgo 0)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sGetRose of Script
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
				(aRose
					z:			1000,
					dispose:
				)
				(Points pGotRose ptsGotRose)
				(ego
					setCycle:	BegLoop self
				)
			)
			(
				(table lookStr:
					"Isn't it funny how you can see the top of that table
						even though you're looking at it from the side?"
				)
				(NormalEgo 1)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)


; 	Props

(instance aRose of View
	(properties
		view			vStoreRoom
		loop			lRose
		cel			0
		x				107
		y				107
		description "the rose"
		signal		ignrAct
		lookStr		"There is a single, lovely, long-stemmed, red rose
						in a delicate bud vase standing incongruously on the table."
		approachX	tableX
		approachY	tableY
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbSmellTaste
				(Print "Thoughts of romance are awakened as the scent of the red
					rose reaches your nostrils.")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance drunkHead of Prop
	(properties
		view			vDrunk
		loop			lHeadUp
		cel			0
		x				256
		y				146
		z				10
		priority		10
		signal		(| fixPriOn ignrAct)
		approachX	drunkX
		approachY	drunkY
	)
	(method (doVerb theVerb invItem)
		(aDrunk doVerb: theVerb invItem)
	)
)

(instance drunkArm of Actor
	(properties
		view			vDrunk
		loop			lArmWhiskey
		cel			0
		x				251
		y				133
		priority		10
		signal		(| ignrAct fixPriOn)
		approachX	drunkX
		approachY	drunkY
	)
	(method (doVerb theVerb invItem)
		(aDrunk doVerb: theVerb invItem)
	)
)

(instance drunkLeg of View
	(properties
		view			vDrunk
		loop			lLeg
		cel			0
		x				246
		y				149
		priority		10
		signal		(| fixPriOn ignrAct)
		approachX	drunkX
		approachY	drunkY
	)
	(method (doVerb theVerb invItem)
		(aDrunk doVerb: theVerb invItem)
	)
)

(instance aDrunk of Prop
	(properties
		view			vDrunk
		loop			lBody
		x				251
		y				141
		description "Robin"
		priority		9
		signal		(| fixPriOn ignrAct)
		approachX	drunkX
		approachY	drunkY
	)
	(method (cue)
		(super cue:)
		(Print "He's a poor little lamb, who has lost his way...")
		(Print "...and also his bladder control!")
	)
	(method (doVerb theVerb invItem &tmp choice)
		(switch theVerb
			(verbLook
				turnEgo
			)
			(verbDo
				(Print "You take pity on the poor drunk and want to help him,
					but his odor convinces you otherwise.")
			)
			(verbTalk
				(= drunkMessage 1)
				(self setScript: sDrunkTalks)
			)
			(verbSmellTaste
				(Print "You reel back as the putrid stench of the unfortunate
					assails your senses.")
			)
			(verbUse
				(switch invItem
					(iWallet
						(-- larryDollars)
						(Print "He responds, \"Shhay, buddy, thanx for da dollar.
							Butcha know, whad I could really usshh is a good,
							sshhtiff belt!\"")
					)
					(iWhiskey
						(ego put: iWhiskey)
						(if (ego has: iRemoteControl)
							(= drunkMessage 2)
						else
							(= drunkMessage 3)
						)
						(HandsOff)
						(self setScript: sDrunkDrinks)
					)
					(iWine
						(Print "\"Hey, waddaya think, I got no class?\" mumbles
							the drunk. \"I'm a whiskey drinker, pure and simple!\"")
					)
					(iWatch
						(Print "\"Know what time it is?\" you ask the drunk.")
						(Print "\"Time for you t' buy me a drink!\" he responds.")
					)
					(iBreathSpray
						(Print "\"Here, have a shot of this!\" you tell the drunk.")
						(Print "\"Whadt?\" he replies,
							\"and ruin my edge?\"")
					)
					(iRing
						(Print "\"It'ssh lovely, but I don't know you
							well enough to get engagshed!\"")
					)
					(else
						(Print "He's a drunk, Larry;
							there's only one thing he wants.")
					)
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance aRat of Prop
	(properties
		view			vStoreRoom
		loop			lRatEat
		x				249
		y				128
		priority		14
		description "the rat"
		signal		(| ignrAct fixPriOn)
	)
)

(instance aDoor of Prop
	(properties
		view			vStoreRoom
		loop			lDoor
		x				251
		y				136
		description "the bathroom door"
		approachX	doorX
		approachY	doorY
		priority		8
		signal		(| ignrAct fixPriOn)
		lookStr		"The door bears the scars of too many late night brawls."
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(HandsOff)
				(curRoom setScript: sToToilet)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

;**	Features

(instance boxes of Feature
	(properties
		x 51
		y 45
		z 0
		heading 0
		nsLeft 0
		nsTop -1
		nsBottom 91
		nsRight 102
 		description "the stack of boxes"
		lookStr "You didn't know Lefty was into boxing!"
		sightAngle 40
	)
)
(instance barrels of Feature
	(properties
		x 165
		y 49
		z 0
		heading 0
		nsLeft 128
		nsTop 0
		nsBottom 99
		nsRight 202
 		description "the barrels"
		lookStr "Those barrels are stacked better than any date you've ever had!
					But they do look as though they might fall over on top of
					you at any moment."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You can't drink what's in THOSE barrels!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance table of Feature
   (properties
      x 				94
      y 				118
      z 				0
      heading 		0
      nsLeft 		86
      nsTop 		95
      nsBottom 	142
      nsRight 		112
      description "the table"
      sightAngle 	40
		approachX	tableX
		approachY	tableY
   )
   (method (doVerb theVerb invItem)
      (switch theVerb
         (verbDo
				(if (not (ObjInRoom iRose))
            	(Print "You have no use for such a lovely table.")
				else
					(ego get: iRose)
					(HandsOff)
					(curRoom setScript: sGetRose)
				)
         )
         (else
            (super doVerb: theVerb invItem &rest)
         )
      )
   )
)
(instance transom of Feature
	(properties
		x 238
		y 135
		z 80
		heading 0
		nsLeft 222
		nsTop 45
		nsBottom 65
		nsRight 254
 		description "the transom"
		lookStr 		"A rancid glow leaks from the next room."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You can't reach that high.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance theFan of Feature
	(properties
		x 26
		y 189
		z 158
		heading 0
		nsLeft 0
		nsTop 0
		nsBottom 63
		nsRight 53
 		description "the fan"
		lookStr 		"This is Lefty's biggest fan!"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You can't reach that high.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance theLight of Feature
	(properties
		x 278
		y 140
		z 120
		heading 0
		nsLeft 257
		nsTop 0
		nsBottom 41
		nsRight 300
 		description "the light"
		lookStr 		"Lefty thought he ordered a \"Bud Light!\""
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You can't reach that high.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

