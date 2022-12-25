;**
;**	Logics for Room 600 -- Disco Exterior
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Mike Larsen
;**
;**	Last Update:  June 20, 1991
;**


(script# 600)

(public
	rm600		0
)

(local
	bouncerTalks
	seenBouncer
	gaveRing
	MBRPOINTS = [
		1 1 184 136
		1 2 180 136
		1 3 173 136
		1 4 173 136
		1 5 173 136
		1 6 173 136
		1 7 173 136
		1 8 173 136
		PATHEND	]

	MBLPOINTS = [
		1 7 173 136
		1 6 173 136
		1 5 173 136
		1 4 173 136
		1 3 173 136
		1 2 180 136
		1 1 184 136
		PATHEND	]

)

(define	mDiscoExterior				600)

(define pDiscoExterior				600)
	(define vDiscoExterior			600)

	(define vLarryShowingID 		601)
		(define lLarryShowingID 	0)

	(define vBouncer 602)
		(define	lBounceShakeHead		0)
		(define	lBouncerMoves			1)
		(define	lBounceHeadTalk		2)

(instance rm600 of LLRoom
	(properties
		picture	600
		north		610
		west		500
		east		170
	)

	(method (init)
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	22 146 55 146 55 155 22 155,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	241 143 265 143 265 147 241 147,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	0 0 168 0 168 122 188 127 200 141 112 141 94 137 60 137
						57 132 0 132,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	192 122 192 0 319 0 319 117 277 117 280 140 208 140,
				yourself
			)
		)
		(switch prevRoomNum
			(500
				(self style: SCROLLLEFT)
				(if
					(< (ego y?) 135)
					(ego y: 135)
				)
			)
			(610
				(HandsOff)
				(theMusic3 fade:)
				(ego
					init:,
					posn:	190 64
				)
				(self setScript: sFromDisco)
			)
			(170
				(ego y: 145)
			)
			(200
				NULL
			)
			(else		;TP
				(ego
					posn:		160 160
				)
			)
		)
		(ego
			init:	,
		)
		(discoThump	play:)
		(bouncer
			cycleSpeed:		howFast,
			init:				,
			approachVerbs: actorVerbs verbSmellTaste verbZipper
		)
		(firePlug	init:)
		(disco		init:)
		(darkAlley	init:)
		(artGallery	init:)
		(lights		init:)
		(super		init:)
		(self setRegions: SIDEWALK)
	)
	(method (doit)
		(super doit: &rest)
		(cond
			(script)
			((< (ego y?) 122)
				(if
					(> (ego x?) 240)
					(curRoom newRoom: 170)
				else
					(HandsOff)
					(self setScript: sToDisco)
				)
			)
		)
		(if
			(and
				(== seenBouncer 0)
				(ego inRect: 140 137 225 145)
				(== (curRoom script?) 0)
			)
			(= seenBouncer 1)
			(curRoom setScript: sBouncer)
		)
		(if
			(and
				seenBouncer
				(not
					(ego inRect: 140 137 225 145)
				)
				(== (curRoom script?) 0)
			)
			(=	seenBouncer 0)
		)
	)

	(method (dispose)
		(theMusic fade:)
		(super dispose:)
	)

	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "Gazing up at the storefronts before you,
					a deep, strange, mystical feeling comes over you.
					Surely every one of the windows looking down on the city
					has a wonderful, lonely story hidden behind it.
					There are millions of stories here in Lost Wages,
					but you'll never know any of them.
					Such is the fate of those who are morally-weak,
					but financially-strong.")
				(Print "(What the hell was THAT all about?!)" LOW)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

;
;	SCRIPTS
;

(instance sFromDisco of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(bouncer
					setLoop:		lBouncerMoves,
					setCel:		8,
					x:				173,
					y:				136,
				)
				(= cycles 1)
			)
			(
				(ego
					ignoreActors:	TRUE,
					posn:				190 64,
					setMotion:		PolyPath 190 150 self
 				)
			)
			(
				(HandsOn)
				(ego ignoreActors: FALSE)
				(bouncer
					setCycle:	MoveCycle @MBLPOINTS self,

				)
			)
			(
				(bouncer
					setLoop:		lBounceShakeHead,
					x:				174,
					y:				136,
					setCel:		0
				)
				(self dispose:)
			)
		)
	)
)

(instance sToDisco of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego setMotion MoveTo 177 64 self)
			)
			(
				(curRoom newRoom: 610)
			)
		)
	)
)
(instance sBouncer of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(HandsOff)
				(ego setMotion: 0)
				(Face ego bouncer)
				(bouncerHead
					init:,
					cycleSpeed:	howFast,
					setCycle:	RandCycle 20 self
				)
			)
			(
				(Print "\"Halt!!\" says the bouncer." HIGH)
				(Print "\"I'm sorry, sir, but this is a private club.\"" HIGH)
				(= cycles 1)
			)
			(
				(ego
					setMotion:	PolyPath (ego x?)	146 self
				)
			)
			(
				(HandsOn)
				(bouncerHead dispose:)
				(self dispose:)
			)
		)
	)
)
(instance sCard of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(HandsOff)
				(Face ego bouncer)
				(= cycles 10)
			)
			(
				(ego moveHead: FALSE)
				(Points pUsedPass ptsUsedPass)
				(Print "You whip out your \"borrowed\" membership card,
					and lay it on him."
				)
				(= cycles 10)
			)
			(
				(ego
					view:			vLarryShowingID,
					setLoop:		lLarryShowingID,
					setCel:		0,
					cycleSpeed:	1,
					setCycle:	EndLoop self
				)
			)
			(
				(= cycles 10)
			)
			(
				(Print "\"Here ya go, lil' buddy,\" you tell the bouncer,
					\"I'll just go on in now, ok?\""
				)
				(= cycles 10)
			)
			(
				(ego
					setCycle:	BegLoop self
				)
			)
			(
				(bouncerHead
					init:			,
					cycleSpeed:	howFast,
					setCycle:	RandCycle 20 self
				)
				(bouncer
					setLoop:		lBouncerMoves,
					setCel:		0,
					x:				184,
					y:				136,
					setCycle:	CycleTo 1
				)
			)
			(
				(Print "\"Good evening, Sir.
					I'm so sorry I didn't recognize you sooner.
					Please, come right in!!\"" HIGH
				)
				(= cycles 10)
			)
			(
				(bouncerHead dispose:)
				(bouncer
					setCycle:	MoveCycle @MBRPOINTS	self
				)
			)
			(
				(NormalEgo)
				(ego
					ignoreActors:	TRUE,
					setMotion:		PolyPath 190 90 self
				)
			)
			(
				(ego moveHead: TRUE)
				(= cycles 1)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)
(instance sDiamond of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(HandsOff)
				(Face ego bouncer)
				(= cycles 1)
			)
			(
				(Print "\"Here, take this diamond ring,\" you tell the bouncer.")
				(= cycles 1)
			)
			(
				(bouncerHead
					init:			,
					cycleSpeed:	(+ 2 howFast),
					setCycle:	RandCycle 20 self
				)
			)
			(
				(Print "\"Gee, thanks, buddy!\" he says.
							\"Go right on in!\"" HIGH
				)
				(bouncerHead dispose:)
				(= cycles 1)
			)
			(
				(bouncer
					setLoop:		lBouncerMoves,
					setCel:		1,
					x:				184,
					y:				136,
					setCycle:	MoveCycle @MBRPOINTS self
				)
			)
			(
				(ego
					ignoreActors:	TRUE,
					setMotion:		PolyPath 190 90 self
				)
			)
			(
				(self dispose:)
			)
		)
	)
)

(instance bouncerHead of Prop
	(properties
		view		vBouncer
		loop		lBounceHeadTalk
		priority 10
		signal	(| fixPriOn ignrAct)
	)
	(method (init:)
		(super init:)
		(if
			(== (bouncer loop?) lBounceShakeHead)
			(bouncerHead
				x:		(+ (bouncer x?) 2),
				y:		(bouncer y?),
				z:		39
				startUpd:
			)
		else
			(bouncerHead
				x:		(- (bouncer x?) 8),
				y:		(bouncer y?),
				z:		39
				startUpd:
			)
		)
	)
)

(instance bouncer of Person
	(properties
		view			vBouncer
		loop			0
		cel			0
		x				174
		y				136
		approachX	190
		approachY	153
		description "Pickhinke"
		sightAngle 20
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if (== prevRoomNum 610)
					(Print "The bouncer seems happy to see you leave.")
				else
					(Print "The bouncer is physically well-developed,
						but you feel sure he's not as wise as you are
						in the ways of women.")
				)
			)
			(verbDo
				(Print "Larry! How could you be so foolish!
					You can't pick a fight with a dude as tough as Pickhinke!")
				(Print "(You know he'd beat you to a pulp!)" LOW)
			)
			(verbTalk
				(if (== prevRoomNum 610)
					(Print "\"On behalf of our entire Disco staff,
						I'd like to express our appreciation for your
						patronage this evening.
						We hope you enjoyed your visit, Sir!\"" HIGH)
					(Print "(Boy, has his tune changed, eh Larry?)" LOW)
				else
					(Print "\"This disco looks like a very swinging place,\"
						you say to Pickhinke, \"how's about letting me in?\"")
					(Print "\"Either show your membership card,
						or get lost, Scum-Breath!\"" HIGH)
					(Print "(It seems he's uninterested in idle chit-chat, Larry.)"
						LOW)
				)
			)
			(verbZipper
				(Print "\"Excuse me, sir,\" you say to the bouncer,
					\"but I really have to use the restroom.
					Would it be okay for me to use the one inside?\"")
				(Print "\"Nope,\" he replies, succinctly." HIGH)
			)
			(verbSmellTaste
				(Print "The bouncer smells of stale gymnasiums and vinyl
					exercise benches.")
			)
			(verbUse
				(switch invItem
					(iWallet
						(Print "\"How's about a little green sliding across
							your palm?\" you ask the bouncer.")
						(Print "\"How's about your little butt sliding across
							the pavement?\" he replies." HIGH)
					)
					(iBreathSpray
						(Print "Psst. Psst.")
						(Print "\"Hey, get that crap away from me!\"" HIGH)
					)
					(iWatch
						(Print "\"Wanna know what time it is?\"
							you ask the bouncer.")
						(Print "\"Time for you to get lost, Creep!\"" HIGH)
					)
					(iApple
						(Print "\"Wanna nibble on my fruit?\" you ask the bouncer.")
						(Print "\"Wanna taste of my knuckles?\" he responds." HIGH)
					)
					(iRing
						(ego put: iRing)
						(curRoom setScript: sDiamond)
					)
					(iWhiskey
						(Print "\"How's about a little drink, buddy?\" you
							ask the bouncer.")
						(Print "\"I never drink when I'm on duty,\"
							Pickhinke replies." HIGH)
					)
					(iRose
						(Print "\"Peace, Love\"")
						(Print "\"Piece this!\"")
					)
					(iLubber
						(Print "\"Have you ever made a balloon out one of these?\"")
						(Print "Pickhinke ignores you completely.")
					)
					(iCandy
						(Print "\"Wanna nibble on my num nums?\"")
						(Print "\"You gotta be kiddin!\"" HIGH)
					)
					(iPocketKnife
						(Print "Your attempt to stab the bouncer is foolish.
							He handles tough guys for a living.")
					)
					(iWine
						(Print "\"How's about a little drink, buddy?\" you
							ask the bouncer.")
						(Print "\"I never drink when I'm on duty,\" he replies."
							HIGH)
					)
					(iMagazine
						(Print "\"Take a look at these!\"")
						(Print "\"Man, I got better than that waiting at home,\"
							he replies." HIGH)
					)
					(iPills
						(Print "\"May I offer you one of these breath mints?\"
							you ask the bouncer.")
						(Print "\"Right. Sure. Do I look stupid?\"")
						(Print "(Don't answer that, Larry!
							It may be a trick question.)" LOW)
					)
					(iDiscoPass
						(curRoom setScript: sCard)
					)
					(iGraffiti
						(Print "\"Who's Ken?\" barks the bouncer.")
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

(instance firePlug of Feature
	(properties
		x 39
		y 141
		z 0
		heading 0
		nsLeft 29
		nsTop 129
		nsBottom 153
		nsRight 50
 		description "the fire plug"
		lookStr 		"Why doesn't that dog use this fireplug instead of you?"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Try as you might, you are unable to \"turn on\"
					the fireplug.")
			)
			(verbZipper
				(Print "Try as you might, you are unable to \"turn on\"
					the fireplug.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance disco of Feature
	(properties
		x 170
		y 30
		z 0
		heading 0
		nsLeft 82
		nsTop 0
		nsBottom 139
		nsRight 258
 		description "the disco sign"
		lookStr 		"You are outside a flashy disco with an unhappy bouncer."
		sightAngle 40
	)
)
(instance lights of Feature
	(properties
		x 176
		y 36
		z 0
		heading 0
		nsLeft 81
		nsTop 16
		nsBottom 56
		nsRight 272
 		description "the lights"
		lookStr 		"Gaudy, aren't they?"
		sightAngle 40
	)
)
(instance artGallery of Feature
	(properties
		x				39
		y				65
		z				0
		heading		0
		nsLeft		0
		nsTop			0
		nsBottom		131
		nsRight		79
 		description	"the art gallery"
		lookStr		"The Lost Wages Art Emporium will satisfy your every need
						for modern art, as long as you're looking for something
						with dead fish!"
		sightAngle	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "The locked gate effectively prevents you from gaining
					the cultural enlightenment you so desire.")
			)
			(verbTalk
				(Print "You have no wish to talk to a bunch of paintings.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance darkAlley of Feature
	(properties
		x				 294
		y				 136
		z				 69
		heading		 0
		nsLeft		 270
		nsTop			 0
		nsBottom		 135
		nsRight		 319
 		description	"the dark alley"
		lookStr		"You're not sure, but you think you hear
						cries of \"Help\" coming from that dark alley over there."
		sightAngle	 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbTalk
				(Print "\"Hello!\" you cry into the darkness,
					\"is there anybody back there?\""
				)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance discoThump of Sound
	(properties
		number		mDiscoExterior
		loop			-1
		flags			1
	)
)
