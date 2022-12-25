;**
;**	Logics for Room 370 -- Blow-Up Doll Closeup
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford
;**
;**	Last Update:  July 1, 1991
;**


(script# 370)

(public
	rm370		0
)

(procedure
	InitRoomFeas
)

(define	pPenthouseBedroom		370)

(define	vPenthouseBedroom		370)
	(define 	lBedroomDoor 	0)
	(define	lEmptyCloset	1)
	(define	lE&LfuckedOut	2)

(define	vLarryInBed				371)
	(define	lClimbingOntoBed	0)
	(define	lDoesTake&Winks	1)
	(define	lUnderCovers		2)
	(define	lTowelOnBed			3)

(define	vEveOnBed				372)
	(define	lEvePattingBed	0)


(define	vLarryInTub				381)
	(define	lLarryInTowel	5)

(define	vKenWilliams			722)
	(define	lKenWalking		0)
	(define	lKenWalkingL	1)
	(define	lKenGlasses		2)
	(define	lKenTalking		3)
	(define	lKenBlinking	4)

(define	vBlowUpDoll				373)
	(define	lBlowUpDoll		0)


(define	sInflateDoll			370)
(define	sDollPops				371)
;(define	sDollDeflating			372)
;(define	mThemeSong				700)


(instance rm370 of LLRoom
	(properties
		picture	370
		lookStr	"The Casino Hotel's Penthouse Suite's bedroom is lavishly
 					appointed with the latest in Bill Skirvin art."
	)
	(method (init)
		(if (== debugging 1)
			(Bset fEveHasApple)
		else
			(if (== debugging 2)
				(Bset fOpenedDoor)
			)
		)
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	0 128 79 128 14 166 306 166 306 156 283 136 276 136 256 120
						232 120 243 134 126 134 126 120 110 120 110 124 80 124 80 120
						0 120 0 0 319 0 319 189 0 189,
				yourself
			)
		)
		(switch prevRoomNum
			(360
				(if (Btst fEveHasApple)
					(ego
						egoSpeed:	(+ 3 howFast),
						init:			,
						x:				102,
						y:				124,
						normal:		FALSE,
						view:			vLarryInTub,
						setLoop:		lLarryInTowel,
					)
					(LoadMany VIEW vEveOnBed vLarryInBed )
					(aEve init:)
					(HandsOff)
					(curRoom setScript: sEveDoesLarry)
				else
					(Load VIEW
						vPenthouseBedroom
					)
					(LoadMany SOUND
						sInflateDoll
						sDoorOpen
					)
					(HandsOff)
					(ego init:)
					(curRoom setScript:	sFromLivingroom)
				)
			)
			(375
				(LoadMany VIEW vKenWilliams )
				(LoadMany SOUND 
					mThemeSong
					sDoorOpen
				)
				(ego
					init:		,
					normal:	FALSE,
					view:		vPenthouseBedroom,
					setLoop:	lE&LfuckedOut,
					setPri:	9,
					x:			182,
					y:			116,
					stopUpd:	,
				)
				(HandsOff)
				(curRoom setScript: sKenSpeaks)
			)
			(else
				(Load VIEW
					vPenthouseBedroom
				)
				(LoadMany SOUND
					sInflateDoll
					sDollDeflating
					sDollPops
					sDoorOpen
				)
				(HandsOff)
				(ego
					init:,
				)
				(curRoom setScript: sLeakyDoll)
			)
		)
		(fDoor
			init:,
			approachVerbs:
			objectVerbs verbLook
		)
		(InitRoomFeas)
		(cond
			((and	 ;closet door is open and empty
					(Btst fOpenedDoor)
					(Btst fPuncturedDoll)
				)
				(door
					init:			,
					setLoop:		1,
					setCel:		0,
					stopUpd:		,
				)
			)
			((and	 ;closet door is open and doll is inside
					(Btst fOpenedDoor)
					(not (Btst fPuncturedDoll))
				)
				(flatDoll
					init:	,
					approachVerbs:
					objectVerbs
				)
				(door
					init:			,
					setLoop:		0,
					setCel:		3,
					stopUpd:		,
				)
			)
			((and	 ; door has never been opened
					(not (Btst fOpenedDoor))
					(not (Btst fPuncturedDoll))
				)
				(door
					cycleSpeed:	(+ 3 howFast),
					init:
				)
			)
		)
		(super init:)
	)
	(method (doit)
		(super doit: &rest)
		(cond
			(script)
			((SteppedOn ego cBLUE)
				(HandsOff)
				(self setScript: sToLivingroom)
			)
		)
	)
)

;***************
;*** SCRIPTS ***
;***************

(instance sEveDoesLarry of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(aEve
					cycleSpeed:	(+ 3 howFast),
					setCycle:	ForwardCounter 7
				)
				(ego
					setCycle:	Walk,
					setMotion:	PolyPath 150 134 self
				)
			)
			(
				(aEve	stopUpd:)
				(ego
					egoSpeed:	(+ 2 larrySpeed),
					setPri:		12,
					posn:			166 100,
					view:			vLarryInBed,
					setLoop:		lClimbingOntoBed,
					setCel:		0,
					setCycle:	EndLoop self,
				)
			)
			(
				(aTowel			init:)
				(ego
					setLoop:		lDoesTake&Winks,
					setCel:		0,
					setCycle:	EndLoop self,
				)
			)
			(
				(= seconds 3)
			)
			(
				(ego
					setLoop:		lUnderCovers,
					setCel:		0,
					setCycle:	EndLoop self,
				)
			)
			(
				(= seconds 4)
			)
			(
				(Points pLaidEve ptsLaidEve)
				(curRoom newRoom: 375)
			)
		)
	)
)

(instance sFromLivingroom of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					posn:			53 122,
					setMotion:	PolyPath 79 125 self
				)
			)
			(
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sToLivingroom of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego setMotion: PolyPath 53 122 self)
			)
			(
				(curRoom newRoom: 360)
			)
		)
	)
)

(instance sClosetDoor of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(if
					(and
						(== (door cel?) 0)
						(not (Btst fOpenedDoor))
					)
					(= cycles 1)
				else
					(Print "The closet door is already open.")
					(self dispose:)
				)
			)
			(
				(ego
					view:			vLarryGetting,
					setLoop:		2,
					setCel:		0,
					setCycle:	BegLoop self
				)
				(Bset fOpenedDoor)
			)
			(
				(theMusic3
					number:		sDoorOpen,
					loop:			1,
					flags:		1,
					play:
				)
				(door 
					init:			,
					setLoop:		0,
					setCel:		0,
					setCycle:  	EndLoop self
				)
				(Points pGotDoll ptsGotDoll)
				(= cycles 1)
			)
			(
				(if
					(not (Btst fPuncturedDoll))
					(flatDoll
						init:	,
						approachVerbs:
						objectVerbs
					)
				)
				(= cycles 1)
			)
			(
				(NormalEgo 0)
				(= cycles 1)
			)
			(
				(self dispose:)
			)
		)
	)
)

(instance sLeakyDoll of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(if 
					(not (Btst pDidDoll))
					(Print "Walking out of the closet,
						you clumsily catch your new latex friend on a protruding nail!
						There's a loud, flatulent sound, and suddenly..."
					)
				)
				(ego 
					z: 	0,
					show:
				)
				(NormalEgo 1)
				(= cycles 1)
			)
			(
				(sfxPop play:)
				(door
					setLoop:	1,
					setCel:	0
				)
				(= cycles 1)
			)
			(
				(theMusic3
					number:		sDollDeflating,
					loop:			-1,
					flags:		1,
					play:
				)
				(aDoll
					cycleSpeed:		(+ 1 howFast),
					moveSpeed:		(+ 1 howFast),
					init:				,
					setCycle:		Forward,
					setMotion:		MoveTo 33 85
				)
				(ego
					egoSpeed:,
					setMotion:		PolyPath 56 119 self
				)
			)
			(
				(Bset fDollFlies)
				(Bset fPuncturedDoll)
				(curRoom newRoom: 360)
			)
		)
	)
)

(instance sInflate of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					view:			vLarryGetting,
					setLoop:		lHighRight,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(door
					setLoop:		1,
					setCel:		0,
					stopUpd:		,
				)
				(ego
					view:			vLarryGetting,
					setLoop:		lHighRight,
					setCycle:	BegLoop self
				)
			)
			(
				(Print "You huff, and you puff, and you blow your doll up.")
				(sfxInflate
					flags:		1,
					play:
				)
				(NormalEgo)
				(= seconds 3)
			)
			(
				(Print "Holding her out at arm's length,
					you pause to admire your handiwork...")
				(Points pInflatedDoll ptsInflatedDoll)
				(curRoom newRoom 371)
			)
		)
	)
)

(instance sKenSpeaks of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= seconds 5)
			)
			(
				(Print "Congratulations, Larry!!! You've done it!!
					You've successfully completed your evening in Lost Wages,
					lost your virginity, and emerged victorious!")
				(Print "Of course, your feelings of success are short-lived,
					since this is where Ken Williams has to tell you about all
					the wonderful sequels to this game that you now are required
					to play.")
				(Print "Hey, Ken! Come on out here, and give 'em your pitch...")
				(theMusic
					fade: 0 12 20 1 self
				)
				(= seconds 8)					;**	<---	Prophylactic Programming
			)
			(
				(= seconds 0)					;**	<---	Prophylactic Programming
				(if (not (Btst fOpenedDoor)) 
					(door setCycle: EndLoop self)
					(theMusic3
						number:		sDoorOpen,
						loop:			1,
						flags:		1,
						play:
					)
				else
					(self cue:)
				)
			)
			(
				(theMusic2
					number:		mThemeSong,
					vol:			127,
					loop:			-1,
					flags:		1,
					play:
				)
				(aKen
					init:,
					cycleSpeed:	(+ 4 howFast),
					moveSpeed:	(+ 4 howFast),
					setLoop:		lKenWalkingL,
					setCel:		0,
					setCycle:	Walk,
					setMotion:	MoveTo 178 137 self
				)
			)
			(
				(Print "Ladies and gentlemen... Mr. Ken Williams!" HIGH)
				(aKen
					setLoop:		lKenGlasses,
					setCycle:	EndLoop self
				)
			)								  
			
			(
				(kenHead
					cycleSpeed:	(* 2 howFast),
					init:,
					setLoop:		lKenTalking,
					setCycle:	ForwardCounter 2 self
				)
			)
			(
				(Print "\"Hi, folks! As C.E.O. of a Major Software Publisher
					you might think I'm above pitching products for Al Lowe.\""
					HIGH
				)
				(kenHead
					setCycle:	ForwardCounter 4 self
				)
			)
			(
				(Print "\"Boy, would you be wrong!
					I don't want to be pushy, but in my humble opinion,
					I think you owe it to yourself to run out to your local
					24-hour software store right now and grab yourself a copy of
					EVERY chapter in the (now seemingly never-ending) saga of
					Larry; Larry Laffer, Al Lowe's lovable lounge lizard!\""
					BIG
				)
				(kenHead
					setCycle:	ForwardCounter 4 self
				)
			)
			(
				(Print "\"You would really enjoy `Leisure Suit Larry 2:
					Looking for Love (In Several Wrong Places)'
					in which you help Larry abandon his lizard-like ways
					and find true love on a breathtakingly-beautiful, tropical
					paradise called `Nontoonyt Island,'
					in spite of the efforts of the K.G.B. and the evil
					Dr. Nonookee!\""
					BIG
				)
				(kenHead
					setCycle:	ForwardCounter 4 self
				)
			)
			(
				(Print "\"Or, you may prefer `Leisure Suit Larry 3:
					Passionate Patti in Pursuit of the Pulsating Pectorals'
					in which you play part of the game as Larry,
					but also play as Passionate Patti,
					a woman that is everything Larry is not.
					(And has everything Larry does not, too!)\""
					BIG
				)
				(kenHead
					setCycle:	ForwardCounter 4 self
				)
			)
			(
				(Print "\"But, I really hope you select my personal favorite,
					`Leisure Suit Larry 5: Passionate Patti Does a Little
					Undercover Work' in which Patti truly comes into her own,
					helping the F.B.I. clean up the entertainment industry,
					while Larry must find the sexiest woman in America
					for his employer, that hot new syndicated television program,
					`America's Sexiest Home Videos!'\""
					BIG
				)
				(kenHead
					setCycle:	ForwardCounter 4 self
				)
			)
			(
				(Print "\"You may wonder why I didn't mention
					`Leisure Suit Larry 4: The Missing Floppies!'\""
					HIGH
				)
				(kenHead
					setCycle:	ForwardCounter 4 self
				)
			)
			(
				(Print "\"Me, too.
					The problem is: we don't know where it is!
					You know me; if we COULD find it, I'd sell it!\""
					HIGH
				)
				(kenHead
					setCycle:	ForwardCounter 2 self
				)
			)
			(
				(Print "\"So, if you enjoyed this game (and you must have,
					since you got this far!), give Sierra's Order Desk 
					a call at 800 326-6654 (international customers call 
					209 683-4468).  We'll be happy to sell you any of the
					Leisure Suit Larry series, or even some of our good games!\""
					BIG
				)
				(kenHead
					setCycle:	ForwardCounter 2 self
				)
			)
			(
				(Print "\"Well, enough about me, now let's hear about you...\""
					HIGH
				)
				(aKen
					setLoop:		lKenGlasses,
					setCycle:	CycleTo 2 -1 self
				)
			)
			(
				(aKen
					setLoop:		lKenWalkingL,
					setCel:		3,
					setCycle:	Walk,
					setMotion:	PolyPath 52 121 self
				)
			)
			(
				(Print "\"Oh, wait.
					I almost forgot to tell you how well you played.\""
					HIGH
				)
				(aKen
					setLoop:		lKenWalking,
					setCycle:	Walk,
					setMotion:	PolyPath 80 128 self
				)
			)
			(
				(aKen
					setLoop:		lKenGlasses,
					setCel:		0
				)
				(= seconds 3)
			)
			(
				(aKen
					setLoop:		lKenGlasses,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(kenHead
					setLoop:		lKenTalking,
					setCycle:	ForwardCounter 2 self
				)
			)
			(
				(Print "\"You played ok, I guess!\""
					HIGH
				)
				(kenHead
					setLoop:		lKenTalking,
					setCycle:	ForwardCounter 4 self
				)
			)
			(
				(Print "\"On behalf of Al and Marg, Ken and Roberta,
					Oliver and Lisa, Mike and Laura, Bill and Patti,
					Bob and Jeannine and Ted and Alice...\""
					HIGH
				)
				(Print "\"...thanks for playing,
					and be sure to tell each and every one of your friends
					to buy their own personal copy of this game.\""
					HIGH
				)
				(kenHead	dispose:)
				(aKen
					setLoop:		lKenGlasses,
					setCycle:	CycleTo 2 -1 self
				)
			)
			(
				(aKen
					setLoop:		lKenWalkingL,
					setCel:		3,
					setCycle:	Walk,
					setMotion:	PolyPath 52 121 self
				)
			)
			(
				(aKen dispose:)
				(Print "\"Love ya, baby!\""
					HIGH
				)
				(Bset fCredits)
				(curRoom newRoom: 375)
			)
		)
	)
)
;**************
;*** SOUNDS ***
;**************

(instance sfxInflate of Sound
	(properties
		number		sInflateDoll
		loop			1
		flags			1
	)
)
(instance sfxPop of Sound
	(properties
		number		sDollPops
		loop			1
		flags			1
	)
)

;**************
;*** ACTORS	***
;**************

(instance aKen of Actor
	(properties
		view		vKenWilliams
		loop		lKenWalking
		cel		0
		x			338
		y			152
;		cycleSpeed 2
		signal 	ignrAct
	)
)

(instance aEve of Prop
	(properties
		view		vEveOnBed
		loop		lEvePattingBed
		cel		0
		priority	10
		x			198
		y			108
		signal	fixPriOn
	)
)
(instance aDoll of Actor
	(properties
		view		vBlowUpDoll
		loop		lBlowUpDoll
		x			271
		y			98
		xStep		7
		yStep		5
	)
)

;*************
;*** PROPS ***
;*************

(instance kenHead of Prop
	(properties
		view 		vKenWilliams
		loop		lKenBlinking
		cel		0
		x			139
		y			143
		priority	14
		signal	(| ignrAct fixPriOn)
	)
	(method (doit:)
		(super doit:)
		(if (and
				(== (aKen loop?) lKenGlasses)
				(== (aKen cel?) 4))

			(kenHead
				x:		(- (aKen x?) 2),
				y:		(aKen y?),
				z:		27
				startUpd:
			)
		else
			(if (== (kenHead z?) 27)
				(kenHead
					z:		1000
					stopUpd:
				)
			)
		)
	)
)
(instance door of Prop
	(properties
		view			vPenthouseBedroom
 		loop			lBedroomDoor
		cel			0
		x				311
		y				150
		priority 	4
		signal 		(| fixPriOn ignrAct)
		illegalBits 0
		description "the closet"
		sightAngle 	40
	)
	(method (doVerb theVerb invItem)
		(fDoor doVerb: theVerb invItem)
	)
)



;*********************************
;** feature stuff for flat doll **
;*********************************

(instance flatDoll of Feature
	(properties
		x 306
		y 155
		z 5
		heading 0
		nsLeft 299
		nsTop 92
		nsBottom 132
		nsRight 313
		approachX 287
		approachY 140
 		description "the inflatable doll"
		lookStr 		"It appears to be an inflatable doll."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "This is the flattest babe you've ever felt, Larry!")
			)
			(verbTalk
				(Print "There's only one thing she can do with her mouth, Larry.")
			)
			(verbZipper
				(Print "You may want to inflate her first, Larry.")
			)
			(verbSmellTaste
				(curRoom setScript: sInflate)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

;****************
;*** FEATURES ***
;****************

(instance fDoor of Feature
	(properties
		x 				304
		y 				144
		z 				15
		heading 		0
		nsLeft 		290
		nsTop 		86
		nsBottom 	143
		nsRight 		319
 		description "the closet"
		sightAngle 	40
		approachX	287
		approachY	140
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(if
					(door loop?)
					(Print "The rest of the closet is rather dull compared to
						the inflatable doll."
					)
				else
					(curRoom setScript: sClosetDoor)
				)
			)
			(verbLook
				(cond
					((and
							(door cel?)
							(not (Btst fPuncturedDoll))
						)
				   	(Print  "You've found a closet full of uninteresting stuff,
									plus one inflatable doll."
						)
					)
					((and
							(Btst fPuncturedDoll)
							(door cel?)
						)
						(Print "The rest of the closet is rather dull compared to
							the inflatable doll."
						)
					)
					((and
							(Btst fPuncturedDoll)
							(Btst fOpenedDoor)
						)
						(Print "Peering into the empty closet brings back sad but
									dear memories of your latex friend."
						)
					)
					(else
						(Print "\"What's behind that door?\"
							you wonder to yourself.")
					)
				)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance painting of Feature
	(properties
		x 28
		y 149
		z 56
		heading 0
		nsLeft 3
		nsTop 68
		nsBottom 118
		nsRight 54
 		description "the painting"
		lookStr 		"Oh, look! A portrait of Jesse Helms!"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance painting2 of Feature
	(properties
		x 163
		y 116
		z 55
		heading 0
		nsLeft 152
		nsTop 46
		nsBottom 77
		nsRight 175
 		description "the painting"
		lookStr 		"Oh, that's disgusting. Be glad you don't have a
						higher-resolution screen!"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance lightSwitch of Feature
	(properties
		x 277
		y 129
		z 29
		heading 0
		nsLeft 274
		nsTop 96
		nsBottom 105
		nsRight 281
		approachX 264
		approachY 127
 		description "the electrical switch"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "This switch connects the two legs of a circuit
					causing current to swiftly flow to the lamp socket
					on the ceiling of the closet.")
				(Print "(Of course, nothing happens since the bulb is
					burned out, but you get the idea!)" LOW)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance bed of Feature
	(properties
		x 179
		y 115
		z 0
		heading 0
		nsLeft 136
		nsTop 99
		nsBottom 131
		nsRight 223
 		description "the bed"
		lookStr 		"Play your cards right and you might get to
						try this baby out tonight!"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "It feels soft enough for you.")
			)
			(verbZipper
				(Print "You might get to do that later, Larry.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance pillows of Feature
	(properties
		x 182
		y 91
		z 0
		heading 0
		nsLeft 146
		nsTop 84
		nsBottom 98
		nsRight 218
 		description "the pillows"
		lookStr 		"They look soft and fluffy -- just like their owner!"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "They feel soft and fluffy.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance flowerBox of Feature
	(properties
		x 159
		y 169
		z 0
		heading 0
		nsLeft 0
		nsTop 150
		nsBottom 189
		nsRight 319
 		description "the flower box"
		lookStr 		"What an unusual plant!
						But how can it grow near all those lights?"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Ouch! Are all the plants sharp?")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance tree of Feature
	(properties
		x 119
		y 61
		z 0
		heading 0
		nsLeft 86
		nsTop -1
		nsBottom 123
		nsRight 153
 		description "the ficus tree"
		lookStr 		"This tree is the only normal-looking plant around.
						That's because it's artificial!"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "What did you think?
					You maybe expected to climb this tree?")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(procedure (InitRoomFeas)
	(painting init:)
	(painting2 init:)
	(lightSwitch 
		init:	,
		approachVerbs: actorVerbs verbLook
	)
	(bed init:)
	(pillows init:)
	(flowerBox init:)
	(tree	init:)
)

(instance aTowel of View
	(properties
		view			vLarryInBed
		loop			lTowelOnBed
		x				153
		y				134
		priority		9
		signal 		(| fixPriOn ignrAct)
	)
)
