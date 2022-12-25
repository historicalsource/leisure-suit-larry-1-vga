;**
;**	Logics for Room 410 -- QuikiWed Interior
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Mike Larsen
;**
;**	Last Update:  June 25, 1991
;**

(script# 410)

(public
	rm410		0
)

;
; defines for pics views loops
;

(define	pQuikiwedInterior		410)

	(define	vQuikiwedInterior	410)
		(define	lLeftCandle		0)
		(define	lRightCandle	1)

	(define	vLarryMarrying		411)
; 	(define	vEveMarrying		412)
	(define	vMinister			413)
		(define lPreacherTalks	0)
		(define lPreacherHead	1)
		(define lTakesDrink		2)
		(define lRaisesArm		3)


;
; defines for sounds
;

;(define	mSyrupOrgan				410)
;(define	mLohingren				411)


(enum												;**	sGetMarried state values
	sayHello
	lookatHim
	willyMarry
	dearly
	rolling
	nip
	digressing
	talkMore
	gottaRing
	wearingIT
	gotBux
	testForMoney
	startCeremoney
	kissTheBitch
	wait4me
	ceremonyOver
	fawnsGone
	hesBroke
	nopeNoBucks
	cashFlow
	SOL
	sierra
	waitHere
	changeRooms
)


(instance rm410 of LLRoom
	(properties
		picture		410
		south			400
		lookStr 		"You are in a quickie marriage parlor.
						A flashing neon sign on the rear wall asks:
						\"Why wait? Marry the girl of your dreams today!
						You provide the girl, we'll do the rest for only $100!!\""
	)
	(method (init)
		(LoadMany VIEW vLarryMarrying vMinister vQuikiwedInterior)
		(LoadMany SOUND mSyrupOrgan mLohingren sDrumFill1)
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	0 189 0 0 319 0 319 189 172 189 140 133 183 128 171 122
						148 127 120 130 71 127 59 132 108 135 110 189,
				yourself
			)
		)
		(window1 init:)
		(window2 init:)
		(window3 init:)
		(pew1 	init:)
		(pew2 	init:)
		(bouquet init:)
		(candelabra
			init:,
			approachVerbs:	actorVerbs verbLook
		)
		(candelabra2
			init:,
			approachVerbs: actorVerbs verbLook
		)
		(fSign 	init:)
		(rightCandle
			cycleSpeed:		howFast,
			init:,
			setCycle:		RandCycle,
			approachVerbs: actorVerbs verbLook
		)
		(leftCandle
			cycleSpeed:		howFast,
			init:,
			setCycle:		RandCycle,
			approachVerbs:	actorVerbs verbLook
		)
		(if debugging
			(Bset fReadyToMarry)
		)
		(if (Btst fReadyToMarry)
			(fawn
				init:,
				setLoop:	3,
				setCel:	0,
				x:			117,
				y:			135
			)

			(preacherHead
				cycleSpeed:	howFast,
				init:
			)
			(aPreacher
				cycleSpeed:	howFast,
				moveSpeed:	howFast,
				init:
			)
			(ego
				x:			137,
				init:
			)
		else
			(ego
				x:			137,
				y:			170,
				init:
			)
		)
		(theMusic
			number:	mSyrupOrgan,
			vol:		127,
			loop:		-1,
			flags:	1,
			play:
		)
		(theMusic3
			loop:			1,
			flags:		1,
		)
		(super init:)
	)
	(method (doit)
		(super doit: &rest)
		(if (and	(not script?)
					(Btst fReadyToMarry))
			(curRoom setScript: sGetMarried)
		)
	)
	(method (dispose)
		(super dispose:)
		(theMusic fade:)
	)
)

;*************
;** Scripts	**
;*************

(instance sGetMarried of Script
	(method (changeState newState)
		(switch (= state newState)
			(sayHello
				(HandsOff)
				(if (not (Btst fFailedCeremony))
					(Print "Your heart fills with eager anticipation as you see
						your lovely bride, Fawn, waiting for you at the end of the
						aisle. 
						You've always been a sucker for beautiful weddings and
						you feel sure that this will be your most special wedding 
						of all!" BIG)
				)
				(ego
					setMotion:	PolyPath 136 133 self
				)
			)
			(lookatHim
				(Face ego aPreacher)
				(= cycles 4)
			)
			(willyMarry	
				(ego hide:)
				(fawn hide:)
				(egoAtAlter	
					init:			,
					setCycle:	EndLoop ,
				)

				(if (Btst fFailedCeremony)
					(Print "\"So... shall we try again?\"" HIGH)
					(Print "\"Yes, that's why I came back in,\" you reply."
						PRIGHT)
					(Print "To Fawn you whisper, \"Thanks for waiting, Babe!\""
						PRIGHT)
					(Print "Fawn replies, \"It's okay, Larry.
						The preacher entertained me while you were gone!\""
						PLEFT)
				)
				(preacherHead
					setCycle:	RandCycle 35 self
				)
				(aPreacher
					setLoop:		lRaisesArm,
					setCycle:	ForwardCounter 7
				)
			)
			(dearly	
				(egoAtAlter		stopUpd:)
				(Print "\"Dearly beloved, we are gathered together today in the
					sight of, er, ah, um... each other, to enter these two people
					into the eternal bonds of marriage.\"" HIGH)
				(preacherHead
					setCycle:	RandCycle 35 self
				)
				(aPreacher
					setCycle:	ForwardCounter 7
				)
			)
			(rolling	
				(Print "\"But before we really get rollin',
					I'd like to say a few words.
					Many's the marriage that I've performed here,
					in this little chapel, wearing this cheap suit,
					before those shiny plastic-covered pews,
					with these electric candles,
					looking at those genuine Plexiglas stained-glass windows,
					for these measly few bucks...\"" BIG)
				(= seconds 3)
			)
			(nip	
				(Print "(He pauses for another nip from his pocket flask.)" LOW)
				(aPreacher
					setLoop:		lTakesDrink,
					setCel:		0,
					setCycle:	BegLoop self
				)
			)
			(digressing	
				(Print "\"Where was I?\" he hiccups." HIGH)
				(Print "\"Digressing,\" you answer." PRIGHT)
				(= seconds 3)
			)
			(talkMore
				(aPreacher
					setLoop:		lPreacherTalks
				)
				(preacherHead
					setCycle:	RandCycle 20 self
				)
			)
			(gottaRing
				(Print "\"Oh, well, have you got a ring?\" he asks." HIGH)
				(= seconds 3)
			)
			(wearingIT
				(Print "\"Yep,\" you reply. \"She's wearing it.\"" PRIGHT)
				(preacherHead
					setCycle:	RandCycle 15 self
				)
			)
			(gotBux	
				(Print "\"Good enough for me! Have you got a hundred bucks?\""
					HIGH)
				(= seconds 3)
			)
			(testForMoney	
				(cond
					((== larryDollars 100)
						(Print "\"Oh, oh. I've got EXACTLY a hundred dollars,\"
							you cry, \"Once we're married, I'm sure Fawn will
							be glad to support us!\"" PRIGHT)
						(Print "\"Not on  your life, Larryberry. 
							Get back out on those streets and bring back some
							dough!\"" PLEFT)
						(Print "\"Kill the music,\" the preacher shouts." HIGH)
						(theMusic stop:)
						(Print "Shrugging your shoulders in defeat,
							you head sadly down the aisle--all alone!")
						(ego
							show:,
							setCycle:	Walk,
							setMotion:	PolyPath 136 175 self
						)
						(fawn
							show:,
							loop:			2,
							cel:			4
						)
						(egoAtAlter dispose:)
			 			(self changeState: waitHere)
					)
					(( < larryDollars 100)
			 			(self changeState: hesBroke)
					)
					(else
						(Bset fFawnInRoom)
						(Print "\"Of course,\" you reply, forking over some of
							your hard-won dough." PRIGHT)
						(-= larryDollars 100)
						(preacherHead
							setCycle:	RandCycle 35 self
						)
					)
				)
			)
			(startCeremoney
				(Print "\"Ok, now where were we? Oh, let's see here...
					let me skip some of this...\"" HIGH)
				(Print "\"Oh well, let's cut to the chase.
					I now pronounce you man and wife, till death do you part,
					or until you get tired of each other,
					per the currently acceptable social standards!\"" HIGH)
				(Points pGotMarried ptsGotMarried)
				(Bclr fReadyToMarry)
				(Bclr fFailedCeremony)
				(preacherHead
					setCycle:	RandCycle 20 self
				)
			)
			(kissTheBitch
				(theMusic
					number:	mLohingren,
					vol:		127,
					loop:		-1,
					flags:	-1,
					play:
				)
				(Print "\"You may kiss the bride,\" says the preacher." HIGH)
				(Print "\"(Oh, boy!)\" you think." PRIGHT)
				(Print "And with that he disappears behind the altar to 
					refill his flask.")
				(aPreacher dispose:)
				(preacherHead dispose:)
				(Print "\"Not now,\" says Fawn. \"That kissing stuff can wait.
					I've got much better things in mind for us, Larry!\"")
				(Print "\"I'll go to our room right now and get everything ready
					for our big night,\" says Fawn knowingly.
					\"You meet me at the Casino Hotel's Honeymoon Suite as soon
					as you can and I'll give you much more than kisses,
					my passionate polyester peacock!!\""
					PLEFT
				)
				(ego
					show:,
					loop:			2
				)
				(fawn
					show:,
					loop:			2,
					setCycle:	Walk,
					cycleSpeed:	(+ 3 howFast),
					moveSpeed:	(+ 3 howFast),
					setMotion:	PolyPath 136 247 self
				)
				(egoAtAlter dispose:)
				(= seconds 3)
			)
			(wait4me
				(Print "\"Hey!\" you shout, \"Wait for me!!\"" PRIGHT)
				(= seconds 3)
			)
	 		(ceremonyOver
				(Print "Somehow this entire ceremony was less than you expected.
					The sacred bonds of marriage certainly get tied loosely in
					this joint!")
			)
			(fawnsGone
				(Bset fWhereDidSheGo)
				(HandsOn)
				(self dispose:)
			)


			;**	This section runs if Larry didn't bring in the $100 

			(hesBroke
				(preacherHead
					setCycle:	RandCycle 10 self
				)
			)
			(nopeNoBucks
				(if (Btst fFailedCeremony)
					(Print "\"So?\" the preacher asks sarcastically,
						\"Did you bring the cash with you THIS time?\"" HIGH)
					(Print "\"Well, er, ah, no, I'm sorry.
						I suppose I forgot,\" you reply." PRIGHT)
					(Print "\"I told you, buddy, 
						come back with 100 cold, hard ones!\"" HIGH)
					(ego
						show:,
						setCycle:	Walk,
						setMotion:	PolyPath 136 175 self
					)
					(fawn
						show:,
						loop:			2,
						cel:			4
					)
					(egoAtAlter dispose:)
					(self changeState: waitHere)
				else
					(Bset fFailedCeremony)
					(Print "\"Not me,\" you reply.
						\"I thought this was a temple of love!\"" PRIGHT)
			 		(Print "\"Sorry, buddy, you two are SOL until you can come back
						here with 100 cold, hard ones!\" he says indignantly." HIGH)
				)
				(Print "\"Kill the music,\" the preacher shouts." HIGH)
				(theMusic stop:)
				(= seconds 3)
			)
			(cashFlow
			 	(Print "Gee, Larry! It looks like you have to leave
					your new bride waiting at the altar,
					while you generate a little positive cash flow!")
			 	(Print "As you leave, Fawn sighs under her breath, 
					\"Hurry back, Larry.
					I don't know how long I can wait before I have you!\"" PLEFT)
				(ego
					show:,
					setCycle:	Walk,
					setMotion:	PolyPath 136 175 self
				)
				(fawn
					show:,
					loop:			2,
					cel:			4
				)
				(egoAtAlter dispose:)
			)
			(SOL
				(Print "\"By the way, Larry,\" she asks.
					\"What does SOL stand for?\"" PLEFT)
				(Face ego fawn)
				(= seconds 3)
			)
			(sierra
			 	(Print "\"Why, Sierra On-Line, I presume!\" you respond." PRIGHT)
				(theMusic3
					number:	sDrumFill1,
					play:
				)
			 	(Print "(Drum fill)" LOW)
				(= seconds 2)
			)
			(waitHere
			 	(Print "\"Wait here, Fawn baby! I'll be back in a flash!!\"" 
					PRIGHT)
				(ego
					setMotion:	PolyPath 136 247 self
				)
			)
			(changeRooms
			 	(curRoom newRoom: 400)
			)
		)
	)
)


(instance preacherHead of Prop
	(properties
		view		vMinister
		loop		lPreacherHead
	)
	(method (doit)
		(super doit:)
		(if
			(OneOf (aPreacher loop?) lPreacherTalks lRaisesArm)
			(preacherHead
				x:		(- (aPreacher x?) 1),
				y:		(aPreacher y?),
				z:		27
			)
		else
			(preacherHead
				z:		1000
			)
		)
	)
	(method (doVerb theVerb invItem)
		(aPreacher doVerb: theVerb invItem)
	)
)

(instance aPreacher of Actor
	(properties
		view		vMinister
		loop		lPreacherTalks
		x			127
		y			118
		description "Reverend Al"
		lookStr		"He looks a lot like his brother,
						that bad comedian in the Lizard Lounge."
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbTalk
				(Print "\"Make up your mind, son. Time is money!\"")
				(Print "(What an old romantic!)" LOW)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance fawn of Actor
	(properties
		view			vFawnWalking
		loop			2
		cel			0
		x				119
		y				134
		description "Fawn"
		signal		ignrAct
		sightAngle	40
	)
	(method (doVerb: theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "Fawn is waiting for you at the altar.
					Go for it, Larry!")
			)
			(verbTalk
				(Print "\"Fawn, I think I love you!\" you cry.")
				(Print "\"Good enough,\" she replies cryptically.")
			)
         (verbDo
				(Print "\"There'll be time enough for that later,\" she smiles.")
			)
			(verbZipper
				(Print "\"Yeah, I know. I can hardy wait too, Larry,\"
					she smiles.")
			)
			(verbSmellTaste
				(Print "Fawn smells of that new perfume, \"Floppies.\"")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance egoAtAlter of Prop
	(properties
		view		vLarryMarrying
		loop		0
		cel		0
		x			123
		y			133
		signal	ignrAct
	)
)
(instance rightCandle of Prop
	(properties
		view			vQuikiwedInterior
		loop			lRightCandle
		cel			0
		x				181
		y				74
		detailLevel 1
		description "the candle"
		lookStr		"You can barely tell they're made from pressed sawdust."
		sightAngle	40
		approachX	160
		approachY	130
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
         (verbDo
				(Print "You hold your hand in the flame.")
				(Print "OUCH!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance leftCandle of Prop
	(properties
		view			vQuikiwedInterior
		loop			lLeftCandle
		cel			0
		x				69
		y				76
		detailLevel 1
		description "the candle"
		lookStr		"You can barely tell they're made from pressed sawdust."
		sightAngle	40
		approachX	95
		approachY	134
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
         (verbDo
				(Print "You hold your hand in the flame.")
				(Print "OUCH!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)


(instance window1 of Feature
	(properties
		x 				274
		y 				84
		z 				0
		heading 		0
		nsLeft 		259
		nsTop 		52
		nsBottom 	117
		nsRight 		289
 		description "the window"
		lookStr 		"You can't see through the \"stained glass\" windows.
						They're fake, like everything else about this place."
		sightAngle 	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance window2 of Feature
	(properties
		x 				217
		y 				82
		z 				0
		heading 		0
		nsLeft 		206
		nsTop 		52
		nsBottom 	112
		nsRight 		228
 		description "the window"
		lookStr 		"You can't see through the \"stained glass\" windows.
						They're fake, like everything else about this place."
		sightAngle 	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance window3 of Feature
	(properties
		x 				29
		y 				82
		z 				0
		heading 		0
		nsLeft 		11
		nsTop 		55
		nsBottom 	109
		nsRight 		48
 		description "the window"
		lookStr 		"You can't see through the \"stained glass\" windows.
						They're fake, like everything else about this place."
		sightAngle 	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance fSign of Feature
	(properties
		x 				112
		y 				30
		z 				0
		heading 		0
		nsLeft 		77
		nsTop 		17
		nsBottom 	44
		nsRight 		148
 		description "the sign"
		lookStr 		"You are in a quickie marriage parlor.
						A flashing neon sign on the rear wall asks:
						\"Why wait? Marry the girl of your dreams today!
						You provide the girl, we'll do the rest for only $100!!\""
		sightAngle 	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance pew1 of Feature
	(properties
		x 				234
		y 				144
		z 				0
		heading 		0
		nsLeft 		152
		nsTop 		117
		nsBottom 	172
		nsRight 		317
 		description "the pew"
		lookStr 		"It seems everything here is covered in plastic."
		sightAngle	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "The plastic covers are so slick,
					you'd probably slide right off.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance pew2 of Feature
	(properties
		x 				50
		y 				156
		z 				0
		heading 		0
		nsLeft 		0
		nsTop 		124
		nsBottom 	189
		nsRight 		100
 		description "the pew"
		lookStr 		"It seems everything here is covered in plastic."
		sightAngle 	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "The plastic covers are so slick,
					you'd probably slide right off.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance bouquet of Feature
	(properties
		x 118
		y 114
		z 58
		heading 0
		nsLeft 83
		nsTop 41
		nsBottom 72
		nsRight 153
 		description "the bouquet"
		lookStr "A lovely artificial bouquet resides about an
					artificial mantle above an artificial fireplace
					/altar."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You don't need more plastic things in your
					life."
				)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance candelabra of Feature
	(properties
		x 72
		y 131
		z 0
		heading 0
		nsLeft 59
		nsTop 71
		nsBottom 126
		nsRight 85
 		description "the candelabra"
		lookStr "If you don't look too closely, you can barely
					see the wires running up the inside of the
					PVC tubing."
		sightAngle 40
		approachX	95
		approachY	134
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Who are you? Liberace?!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance candelabra2 of Feature
	(properties
		x 179
		y 93
		z 0
		heading 0
		nsLeft 170
		nsTop 64
		nsBottom 122
		nsRight 188
 		description "the candelabra"
		lookStr "If you don't look too closely, you can barely
					see the wires running up the inside of the
					PVC tubing."
		sightAngle 40
		approachX	160
		approachY	130
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Who are you? Liberace?!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
