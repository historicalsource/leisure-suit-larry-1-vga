;**
;**	Logics for Room 110 -- Lefty's Bar interior
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford
;**
;**	Last Update:  June 21, 1991
;**


(script# 110)

(public
	rm110		0
)

(local
	tipCount
	ordered
	tookADrink
	beenToldPrice
	jokeCycles
	jokeNumber
	selection
	pitch
	theta
	saidHello
	clickX
	clickY
)

(procedure
	PickAJoke
)

(enum
	0
	WHISKEY
	WINE
	BEER
	CHAMPAGNE
	LIGHTBEER
	ROUND
)

(enum
	KNOCK
	SLOTOPEN
	QUERY
	SCRAM
	SLOTCLOSE
	DOOROPEN
	WALKOUT
	SHUTDOOR
	NEWROOM
)

(define peepholeZ 32)
(define TOOK_WHISKEY	999)
(define doorX	290)
(define doorY	151)
(define stoolX 208)
(define stoolY 150)
(define babeX	141)
(define babeY	154)
(define jukeX	58)
(define jukeY	120)
(define sitting (== (ego view?) vLarrySitting))
(define getupMsg (Print "How can you do that from there?"))
(define pimpTalksAt #at: 180 55)
(define kenTalksAt #at:	5 5 #mode: teJustLeft)
(define leftyTalksAt #at: 73 42)

(define handZ 17)
(define kenHeadZ 23)

(define pBarInterior				110)
(define vBarInterior				110)
	(define lJute					0)
	(define lSlot					1)
	(define lEyes					2)
	(define lFan					3)
	(define lDoor					4)
(define vLefty						112)
	(define lLeftyRight			0)
	(define lPantsFallRight		1)
	(define lLeftPantsFall		2)
	(define lRaiseDrink			3)
	(define lShakeDrink			4)
	(define lLeftyTalk			5)
	(define lHandDrink			6)
		(define cReachOut			0)
		(define cReachIn			3)
	(define lLeftyToLarry		7)
(define vBarFlys					113)
	(define lBlondTalk			0)
	(define lBlondLiftArm		1)
		(define cLifted			3)
	(define lBlondDrinkArm		2)
	(define lFatsoPick			3)
	(define lFatsoDrink			4)
		(define cFDrink			4)

	(define lKenTalkLeft			5)
		(define cMouthShut		0)
	(define lKenTalkRight		6)
	(define lKenTurnRight		7)
		(define cBodLeft			0)
		(define cBodFront			1)
		(define cBodRight			2)
		(define cKenBottom		3)
	(define lKenTopPunchLine	8)
	(define lKenLaugh				9)

	(define lDudeDrink			10)
	(define lBabeLeg				11)
	(define lBabeHair				12)
	(define lBods					13)
		(define cBlondBod			0)
		(define cBlondHand		1)
		(define cDickBod			2)
;(define	vLarrySitting			801)

;(define	mBarInterior			110)
;(define	sDrinking				111)
;(define	sMugOnBar				112)
(define	sBartenderSwoosh		113)
(define	sPantsDown				114)
(define	sPantsUp					115)
(define	sRecordStart			116)
(define	sRecordNoise			117)
(define	sRecordDone				118)
(define	sPeepholeSlider		119)

(instance rm110 of LLRoom
	(properties
		picture	110
		south		100
		north		120
	)
	(method (init)
		(LoadMany SOUND
			mBarInterior
			sBartenderSwoosh
			sPantsDown
			sPantsUp
			sRecordStart
			sRecordNoise
			sRecordDone
			sPeepholeSlider
		)
		(LoadMany VIEW
			vBarInterior
			vLefty
			vBarFlys
			vLarrySitting
		)
;		(curRoom addObstacle:
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	0 189 0 0 319 0 319 189 310 189 310 149 287 142 270 154
;						116 154 89 130 99 106 129 85 128 77 87 104 72 107 51 126
;						27 130 12 151 12 189,
;				yourself
;			)
;		)
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	0 189 0 0 319 0 319 189 310 189 310 149 287 142 270 154
						116 154 89 130 99 106 129 85 128 77 82 107 72 107 51 126
						27 130 12 151 12 189,
				yourself
			)
		)
		(switch prevRoomNum
			(120
				(self setScript: sFromStoreroom)
				(if
					(!= (theMusic number?) mBarInterior)
					(theMusic
						setLoop:		1,
					)
				)
				(theMusic fade: 127 15 5 0)
			)
			(100
				(theMusic
					number:		mBarInterior,
					loop:			-1,
					vol:			127,
					flags:		1,
					play:,
				)
				(ego setPri:	RELEASE,)
			)
			(140
				(HandsOff)
				(ego
					normal:		FALSE,
					z:				1000,
					hide:,
				)
				(curRoom setScript: sFromPimp)
			)
			(else
				(ego posn: 160 170)
			)
		)
		(ego
			init:		,
			actions: egoActions
		)
		(eHead actions: egoActions)
		(super init:)
		(jukebox
			cycleSpeed:		howFast,
			init:,
			approachVerbs: objectVerbs,
		)
		(if (!= (theMusic number?) mBarInterior)
			(jukebox setCycle: Forward)
		else
			(jukebox stopUpd:)
		)
		(blondGuy
			init:,
			stopUpd:
		)
		(blondHand
			init:,
			stopUpd:
		)
		(blondHead
			cycleSpeed:		(+ howFast 2),
			init:,
			setScript:	sGuyScript
		)
		(babe
			cycleSpeed:		(+ howFast 1),
			init:				,
			approachVerbs:	actorVerbs,
			setScript:		sBabeScript
		)
		(babeTop
			cycleSpeed:		(+ howFast 1),
			init:,
			approachVerbs:	actorVerbs,
		)
		(ken
			cycleSpeed:		(+ howFast 1),
			init:,
			stopUpd:
		)
		(kenHead
			cycleSpeed:		(+ howFast 1),
			init:,
			setScript:		sKenTalksGirl
		)
		(dude
			init:,
			stopUpd:
		)
		(skinnyMan
			init:,
			cycleSpeed:	howFast,
			setScript:	sDudeScript,
		)
		(fatso
			cycleSpeed:	(+ howFast 1),
			init:			,
			setScript:	sFatsoScript
		)
		(lefty
			cycleSpeed:	howFast,
			init:,
			stopUpd:,
		)
		(fan
			cycleSpeed:	howFast,
			setCycle:	Forward,
			init:			,
		)
		(mooseEyes
			cycleSpeed:		howFast,
			init:,
			setScript:	(sDudeScript new:),
		)
		(door
			cycleSpeed:	(+ howFast 1),
			init:,
			stopUpd:,
			approachVerbs: objectVerbs,
		)
		(if (!= prevRoomNum 140)
			(door stopUpd:)
		)
		(peephole
			cycleSpeed:		howFast,
			stopUpd:,
			init:,
			approachVerbs: objectVerbs,
		)
		(stool
			init:,
			approachVerbs: objectVerbs,
		)
		(moose init:)
		(painting init:)
	)
	(method (doit)
		(super doit: &rest)
		(if
			(and
				(!= (theMusic number?) mBarInterior)
				(== (theMusic prevSignal?) -1)
				(not (jukebox script?))
			)
			(sPlaysong start: 5)
			(jukebox setScript: sPlaysong)
		)
		(if (and
				(not jokeCycles)
				sitting
				(!= (kenHead script?) sTellJoke)
			)
			(kenHead setScript: sTellJoke)
		else
			(-- jokeCycles)
		)
		(cond
			(script)
			((< (ego y?) 104)
				(HandsOff)
				(curRoom setScript: sToStoreroom)
			)

			((and (ego mover?)
					(== (ego view?) vLarrySitting)
		 	 )
		 		(if
					(== (lefty script?) sLeftyServes)
					(ego 
						setMotion: 		0,
						x:					stoolX,
						y:					stoolY,
					)
					(Print "Don't be so impatient, Larry.
						Wait until Lefty brings your order.")
				else
					(= clickX
						((User curEvent?) x?)
					)
					(= clickY
						((User curEvent?) y?)
					)
		 			(HandsOff)
					(self setScript: sGetUp)
				)
			)
		)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "This is the sleaziest bar you've ever been in.")
				(Print "(And you've been in quite a few.)" LOW)
			)
			(verbTalk
				(Print "Talk to someone in particular.")
			)
			(verbSmellTaste
				(Print "This place HAS no taste!")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
	(method (newRoom n)
		(switch n
			(140
				(theMusic fade: 90 15 5 0)
			)
			(100
				(theMusic fade:)
			)
			(120
				(theMusic
					setLoop: -1,
					fade: 	90 15 5 0
				)
			)
			(160
				(theMusic stop:)
			)
		)
		(Cls)
		(super newRoom: n)
	)
)

;
;	Scripts
;

(instance sFromPimp of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(peephole z: 1000)
				(= cycles 1)
			)
			(
				(if (!= (theMusic number?) mBarInterior)
					(theMusic
						number:		mBarInterior,
						loop:			-1,
						vol:			127,
						flags:		1,
						play:,
					)
				else
					(theMusic
						fade:			127 10 5 0
					)
				)
				(door
					setCycle:	EndLoop self
				)
			)
			(
				(ego
					egoSpeed:,
					show:,
					z:					0,
					normal:			TRUE,
					illegalBits:	$8000,
					x:					319,
					y:					145,
					setMotion:		MoveTo doorX doorY self
				)
			)
			(
				(door setCycle: BegLoop self)
			)
			(
				(peephole z: peepholeZ)
				(NormalEgo 1)
				(door stopUpd:)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sGetUp of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					egoSpeed:,
					setCycle:	EndLoop self
				)
			)
			(
				(NormalEgo 2)
				(ego
					setMotion:	MoveTo (ego x?) (+ stoolY 4) self
				)
			)
			(
				(Cls) ;in case Ken is telling a joke
				(kenHead
					z:				kenHeadZ,
					setLoop:		lKenTalkLeft,
					setScript: 	sKenTalksGirl
				)
				(ken setCel: cBodLeft)
				(if (not tookADrink)
					(HandsOn)
					(if 
						(and 
							(!= (CueObj client?) stool)
							((CueObj client?) approachX?)
							(!= (theIconBar curIcon?) (ScriptID 0 25))
						)
						(ego setMotion: PolyPath
						((CueObj client?) approachX?)
						(+ (ego z?) ((CueObj client?) approachY))
						CueObj)
					else
						(ego setMotion: PolyPath clickX clickY)
					)
					(self dispose:)
				else
					(Print "You feel a little light-headed.
						You are unsure if you'll be able to walk straight.")
					(if (> tookADrink 8)
						(= tookADrink 8)
					)
					(curRoom setScript: sWalkDrunk)
				)
			)
		)
	)
)

(instance sWalkDrunk of Script
	(method (doit &tmp i)
		(super doit: &rest)
		(= pitch (* (SinMult theta 1000) tookADrink))
		(= theta (mod (+= theta 5) 360))
		(for ((= i 1)) (<= i 15) ((++ i))
			(theMusic
				send: i mPITCHBEND pitch
			)
		)
		(if (and (not register)
					(< state 2))
			(= register (Random 5 25))
			(ego setLoop: (Random 0 3))
		else
			(-- register)
		)
	)
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					egoSpeed:,
					observeBlocks:	drunkCircle,
					setMotion:		Wander,
				)
				(= seconds 6)
			)
			(
				(if (== tookADrink 5)
					(Print "Boy, are you drunk!")
				)
				(-- tookADrink)
				(if tookADrink
					(self init:)
				else
					(ego
						setMotion: 		0,
						setLoop:			RELEASE,
						ignoreBlocks:	drunkCircle
					)
					(Print "Shaking your head,
						you finally clear away the cobwebs and are
						once again able to walk in your standard, studly style.")
					(= cycles 1)
				)
			)
			(;2 is current state
				(NormalEgo)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sKenTalksGirl of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ken
					setLoop:		lKenTurnRight,
					setCel:		cBodLeft,
				)
				(kenHead
					setLoop:		lKenTalkLeft,
					setCel:		0,
				)
				(= cycles (Random 24 32))
			)
			(

				(kenHead
					setCycle:	Forward,
				)
				(= cycles (Random 24 32))
			)
			(
				(kenHead
					setCel:		cMouthShut,
					setCycle:	0,
					stopUpd:
				)
				(self init:)
			)
		)
	)
)

(instance sTellJoke of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= cycles (Random 50 100))
			)
			(
				(if (== (kenHead loop?) lKenTalkLeft)
					(ken
						setCel:		1,
					)
					(kenHead z: 1000)
				else
					(++ state)
				)
				(= cycles 1)
			)
			(
				(ken
					setCel:			2,
				)
				(kenHead
					z: 				kenHeadZ,
					setLoop:			lKenTalkRight
				)
				(= cycles 1)
			)
			(
				(kenHead
					setLoop:			lKenTalkRight,
					setCycle:		Forward,
				)
				(= cycles (Random 43 90))
			)
			(
				(Print "\"...blah, blah, blah, blah, blah...\""
					kenTalksAt
					#dispose:)
				(= seconds 5)
			)
			(
				(Cls)
				(PickAJoke)
				(kenHead
					setLoop:			lKenTopPunchLine,
					setCel:			0,
					setCycle:		EndLoop,
				)
				(ken
					setCel:			cKenBottom
				)
				(= seconds 7)
			)
			(
				(Cls)
				(Print "\"Har, har!!\""
					kenTalksAt
					#dispose:)
				(ken
					setLoop:			lKenLaugh,
					setCycle:		Forward,
				)
				(kenHead
					z:		1000
				)
				(= cycles 20)
			)
			(
				(Cls)
				(ken
					setLoop:			lKenTurnRight,
					setCel:			cBodRight
				)
				(kenHead
					z:					kenHeadZ,
					setLoop:			lKenTalkRight,
					setCel:			0,
					setCycle:		0,
				)
				(= cycles 20)
			)
			(
				(Print "(So what was the setup to THAT punch line?)" LOW)
				(= jokeCycles (Random 150 250))
				(self dispose:)
			)
		)
	)
)

(instance sSulk of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(kenHead
					z:			1000
				)
				(ken
					setLoop:		lKenTurnRight,
					setCel:		cBodFront,
				)
				(= jokeCycles (Random 400 800))
				(= cycles (Random 123 234))
			)
			(
				(ken
					setCel:		cBodRight
				)
				(kenHead
					z:				kenHeadZ,
					setScript: 	sKenTalksGirl
				)
			)
		)
	)
)

(instance sSitDown of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego setMotion: MoveTo stoolX stoolY self)
			)
			(
				(ego
					egoSpeed:,
					view:		vLarrySitting,
					setLoop:	0,
					cel:		0,
					setCycle:EndLoop self
				)
			)
			(
				(ego
					setLoop:	1,
					setCel:	0
				)
				(= cycles 1)
			)
			(
				(HandsOn)
				(ego userSpeed:)
				(self dispose:)
			)
		)
	)
)


(instance sToStoreroom of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					setMotion: PolyPath 126 82 self
				)
			)
			(
				(curRoom newRoom: 120)
			 	(self dispose:)
			)
		)
	)
)

(instance sFromStoreroom of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					posn:			94 100,
					setMotion:	PolyPath 91 110 self
				)
			)
			(
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sDoor of Script
	(method (changeState newState)
		(switch (= state newState)
			(KNOCK
				(Print "You rap loudly on the Naugahyde door.")
				(= seconds 4)
			)
			(SLOTOPEN
				(theMusic3
					number:		sPeepholeSlider,
					loop:			1,
					vol:			127,
					flags:		1,
					play:,
				)
				(peephole setCycle: EndLoop self)
			)
			(QUERY
				(Print "\"Yeah. Whatsda\n
					passwoid?\""
					pimpTalksAt)
				(= seconds 15)
			)
			(SCRAM
				(Print "\"Scram, dog breath!\"" pimpTalksAt)
				(peephole setCycle: BegLoop)
				(theMusic3
					number:		sPeepholeSlider,
					loop:			1,
					vol:			127,
					flags:		1,
					play:,
				)
				(self dispose:)
				;**	this is the end of the failure to give password portion
			)


			;**	This is if he tells the pimp the password while the peephole
			;**	is open:
			(SLOTCLOSE
				(Print "\"Come on in!\""
					#at:		220 55
					#dispose
				)
				(theMusic3
					number:		sPeepholeSlider,
					loop:			1,
					vol:			127,
					flags:		1,
					play:,
				)
				(peephole setCycle: BegLoop self)
			)
			(DOOROPEN
				(peephole
					z:			1000,
					dispose:
				)
				(door
					setCycle: EndLoop self
				)
			)
			(WALKOUT
				(Cls)
				(curRoom setScript: sGoPimp)
				(self dispose:)
			)
		)
	)
)

(instance sGoPimp of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					setMotion: MoveTo 340 141 self
				)
			)
			(
				(door
					setCycle: BegLoop self
				)
			)
			(
				(curRoom newRoom: 140)
			)
		)
	)
)

(instance sLeftyServes of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(theMusic3
					number:		sBartenderSwoosh,
					setLoop:		1,
					flags:		1,
					play:,
				)
				(if
					(== (lefty loop?) lLeftyToLarry)
					(lefty
						setCel:	1
					)
				)
				(= cycles 1)
			)
			(
				(lefty
					setLoop:		lLeftyRight,
					setCycle:	EndLoop self
				)
			)
			(
				(lefty
					setLoop:		lPantsFallRight,
					setCel:		0,
					x:				(+ (lefty x?) 30),
					setCycle:	CycleTo 7 1 self
				)
			)
			(
				(theMusic3
					number:		sPantsDown,
					play:
				)
				(lefty
					setCycle:	CycleTo 11 1 self
				)
			)
			(
				(theMusic3
					number:		sPantsUp,
					play:
				)
				(lefty
					setCycle:	EndLoop self
				)
			)
			(
				(theMusic3
					number:		sBartenderSwoosh,
					play:
				)
				(lefty
					setLoop:		lLeftPantsFall,
					setCel:		0,
					x:				(- (lefty x?) 16),
					setCycle:	CycleTo 2 1 self
				)
			)
			(
				(lefty
					x:				(- (lefty x?) 14),
					setCycle:	CycleTo 5 1 self
				)
			)
			(
				(theMusic3
					number:		sPantsDown,
					play:
				)
				(lefty
					setCycle:	CycleTo 11 1 self
				)
			)
			(
				(theMusic3
					number:		sPantsUp,
					play:
				)
				(lefty
					setCycle:	EndLoop self
				)
			)
			(
				(lefty
					setLoop:		lRaiseDrink,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(lefty
					setLoop:		lShakeDrink,
					setCycle:	Forward,
				)
				(= seconds (Random 2 3))
			)
			(
				(theMusic3
					number:		sBartenderSwoosh,
					play:
				)
				(lefty
					setLoop:		lLeftyToLarry,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(lefty
					setLoop:		lHandDrink,
					setCel:		255,
					setCycle:	BegLoop self
				)
			)
			(
				(Print "\"That'll be $5.00, please.\"" leftyTalksAt)
				(lefty
					setLoop:			lLeftyToLarry,
					setCel:			255,
				)
				(if
					(< larryDollars 5)
					(Print "\"I'm presently a little short,\" you stammer.")
					(Print "\"Yeah, and not just in the financial department,
						Dead-Beat!\" he replies,
						\"I'll teach you to try and stiff ol' Lefty!
						Come with me!\"" leftyTalksAt)
					(HandsOff)
					(curRoom setScript: sThrowLarry)
				else
					(Print "You flip five bucks onto the counter.")
					(-= larryDollars 5)
					(switch ordered
						(WHISKEY
							(if (ego has: iWhiskey)
								(++ tookADrink)
								(Print "Since you can only balance one open glass of
									whiskey at a time, you toss this one down
									immediately.")
							else
								(Points pBoughtWhiskey ptsBoughtWhiskey)
								(ego get: iWhiskey)
(= ordered TOOK_WHISKEY)
								(Print "You don't drink the whiskey, but instead,
									decide to carry it with you wherever you go,
									precariously balanced in an open shot glass.")
							)
						)
						(WINE
							(++ tookADrink)
							(Print "You delicately sip the wine until it's
								all gone.")
							(Print "You tell Lefty, \"I find this impudent and sassy,
								with the slightest hint of impertinence.\"")
							(Print "He gazes at you longingly and moistens his
								lips!")
						)
						(BEER
							(++ tookADrink)
							(Print "You down the beer in your pseudo-macho style,
								and slam the mug back on the bar.")
						)
					)

					(if (!= ordered TOOK_WHISKEY)
						(switch tookADrink
							(1
								(Print "Suddenly you feel a little woozy.")
							)
							(else
								(Print "This one tastes even better than the last.
									Don't you think one more seems like a good idea?")
							)
						)
					)
					(= ordered 0)
					(= cycles 1)
				)
			)
			(
				(self dispose:)
			)
		)
	)
)

(instance sBabeScript of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(babe
					setCel:		0,
					stopUpd:
				)
				(babeTop
					setCel:		0,
					stopUpd:
				)
				(= cycles (Random 30 60))
 			)
			(
				(if (Random 0 3)
					(babe
						setCycle: 	Forward
					)
					(= cycles (Random 40 80))
				else
					(babeTop
						setCel:		0,
						setCycle: 	EndLoop self
					)
				)
			)
			(
				(self init:)
			)
		)
	)
)

(instance sGuyScript of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(blondHead
					setLoop:			lBlondTalk,
					setCel:			0,
				)
				(blondHand
					setPri:	10
				)
				(= cycles 2)
			)
			(
				(blondHead stopUpd:)
				(= cycles (Random 75 200))
			)
			(
				(if (Random 0 1)
					(blondHead
						setCycle:	Forward
					)
					(= cycles (Random 15 25))
				else
					(blondHead
						setLoop:		lBlondLiftArm,
						setCel:		0,
						setCycle:	EndLoop self
					)
					(blondHand z: 1000)
				)
			)
			(
				(if (blondHead cycler?)
					(blondHead
						setCel:		0,
						setCycle:	0,
					)
					(self init:)
				else
					(blondHead
						setLoop:			lBlondDrinkArm,
						setCycle:		Forward
					)
					(= seconds (Random 3 5))
				)
			)
			(
				(blondHead
					setLoop:		lBlondLiftArm,
					setCel:		255,
					setCycle:	BegLoop self
				)
			)
			(
				(blondHand
					z:			handZ,
					setPri:	11
				)
				(self init:)
			)
		)
	)
)

(instance sFatsoScript of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(fatso
					setLoop:			lFatsoPick,
					setCel:			0,
				)
				(= cycles 2)
			)
			(
				(fatso stopUpd:)
				(= cycles (Random 75 200))
			)
			(
				(cond
					((<= (theGame detailLevel?) 2)
						(self init:)
					)
					(	(not (Random 0 2))
						(fatso
							setLoop:			lFatsoPick,
							setCycle:	EndLoop self,
						)
					)
					(	else
						(fatso
							setLoop:			lFatsoDrink,
							setCycle:	CycleTo cFDrink 1
						)
						(= cycles (Random 5 10))
					)
				)
			)
			(
				(if (== (fatso loop?) lFatsoDrink)
					(fatso setCycle: EndLoop self)
				else
					(self init:)
				)
			)
			(
				(self init:)
			)
		)
	)
)

(instance sDudeScript of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(client stopUpd:)
				(= cycles (Random 50 150))
			)
			(
				(if
					(<= (theGame detailLevel?) (client detailLevel?))
					(self init:)
				else
					(client
						setCycle:	EndLoop
					)
					(= cycles (Random 6 20))
				)
			)
			(
				(client
					setCycle:	BegLoop self
				)
			)
			(
				(self init:)
			)
		)
	)
)

(instance sThrowLarry of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(Cls)
				(cast 
					eachElementDo: #z: 1000,
				)
				(kenHead setScript: 0)
				(curRoom
					style:		IRISIN,
					drawPic:		pBlackScreen
				)
				(= seconds 3)
				(curRoom newRoom: 160)
			)
		)
	)
)

(instance sPlaysong of Script
;	(method (doit)
;		(super doit: &rest)
;		(if (and (== state 0)
;					(== (theMusic prevSignal?) -1))
;			(= cycles 1)
;		)
;	)
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= register selection)
				(theGame setCursor: ARROW_CURSOR TRUE)
				(Animate (cast elements?) FALSE)
				(= selection
					(PrintD {Lefty's proudly presents for your listening
						and drinking pleasure:}
						#new
						#button	{Taxicab from Hell} 						mCab
						#new
						#button	{Air For A \"G\" String}				mSyrupOrgan
						#new
						#button	{I Can Can You}							mCanCan
						#new
						#button	{Mantovani's Revenge}					mCasino
						#new
						#button	{Theme from \"Bo-Larry\"}				mFloor8
						#new
						#button	{\"Wedding March\"\nby Bloughengrin} mLohingren
						#new
						#button	{Prelude to an\nAfternoon of Fawn}	mDiscoInterior
						#new
						#button	{Her Albert Has\nSome Iguana Gas}	mQuikiMartInterior
						#new
						#button	{That's a Sweet Moon, Honey}	mMacTheKnife
					)
				)
				(theGame setCursor: ((theIconBar curIcon?) cursor?) TRUE)
				(theMusic fade: 0 5 5 1 self)
			)
			(
				(if register
					(UnLoad SOUND register)
				else
					(UnLoad SOUND mBarInterior)
				)
				(theMusic2
					number:		sRecordStart,
					loop:			1,
					vol:			127,
					flags:		1,
					play: 		self,
				)
			)
			(
				(sScratchy
					number:		sRecordNoise,
					loop:			-1,
					vol:			127,
					flags:		1,
					priority:	10,
					play:,
				)
				(= seconds 3)
			)
			(
				(client setCycle: Forward)
				(theMusic
					vol:			127,
					loop:			1,
					number:		selection,
					play:			self,
				)
				(HandsOn)
				(= seconds 3)
			)
			(
				(sScratchy fade:)
			)
			(;5 is current state
				(= seconds 3)
			)
			(
				(sScratchy play:)
				(theMusic2
					number:		sRecordDone,
					play:			self,
				)
			)
			(
				(theMusic
					number:	mBarInterior,
					vol:		0,
					loop:		-1,
					play:
				)
				(= cycles 1)
			)
			(
				(client setCycle: BegLoop)
				(sScratchy fade:)
				(theMusic
					fade: 	127 10 5 0,
				)
				(= seconds 3)
			)
			(
				(UnLoad SOUND selection)
				(= selection 0)
				(self
					start:		0,
					dispose:
				)
			)
		)
	)
)

;
;	Classes
;

(class egoActions of Code
	(methods
		doVerb
	)
	(method (doVerb theVerb invItem)
		(return
			(switch theVerb
				(verbUse
					(switch invItem
						(iWhiskey
							(if sitting
								(++ tookADrink)
								(ego put: iWhiskey)
								(Print "The whiskey burns its way down your throat.
									You vow to never again buy a \"well\" brand.")
							else
								(Print "It's not good etiquette to drink standing up.")
							)
						)
						(iBreathSpray
							(if sitting
								(Cls)
								(Print "Ahhhh."
									HIGH
									#time 2
								)
								(++ sprayCounter)
								(= spraySeconds BREATHLIMIT)
								(Bclr fMouthSmellsBad)
								(if (Btst fNearPerson)
									(Bclr fNearPerson)
									(Print "\"It's about time!!\"")
								)
								(if (> sprayCounter SPRAYSINCAN)
									(Print "Since your breath spray is now empty,
										you toss it away.")
									(ego put: iBreathSpray 510)
									(= sprayCounter 0)
								)
								(return TRUE)
							)
						)
					)
				)
			)
		)
	)
)

;
;	Blocks
;

(instance drunkCircle of Cage
	(properties
		top			150
		left			25
		bottom		185
		right			310
	)
)

;
;	Props
;

(instance jukebox of Prop
	(properties
		view			vBarInterior
		loop			lJute
		cel			0
		x				36
		y				122
		description	"the jukebox"
		sightAngle  40
		priority		pRED
		signal		(| ignrAct fixPriOn)
		lookStr		"Gee, Dad. It's a Wurlitzer!"
		approachX	jukeX
		approachY	jukeY
	)
	(method (doVerb theVerb invItem)
		(if 
			sitting
			(if
				(== theVerb verbLook)
				(super doVerb: theVerb)
			else
				getupMsg
			)
		else
			(switch theVerb
				(verbDo
					(Print "True to form, you stick your finger in the coin
						return slot hoping to find some change. You don't.")
				)
				(verbZipper
					(Print "Over the years, a few coin-operated machines have
						screwed you, but this is scarcely the time nor
						the place for you to seek revenge!")
				)
				(verbSmellTaste
					(Print "This place HAS no taste!")
				)
				(verbUse
					(switch invItem
						(iWallet
							(HandsOff)
							(Print "You slip a buck into the juke box,
								and search for a suitably sleazy selection.")
							(-= larryDollars 1)
							(jukebox
								setScript: sPlaysong
							)
						)
						(else
							(super doVerb: theVerb invItem)
						)
					)
				)
				(else
					(super doVerb: theVerb invItem)
				)
			)
		)
	)
)

(instance blondHead of Prop
	(properties
		view			vBarFlys
		loop			lBlondTalk
		x				125
		y				115
		z				17
		priority		10
		signal		(| fixPriOn ignrAct)
		description "Ivan's head"
		sightAngle	40
	)
	(method (doVerb theVerb invItem)
		(blondGuy doVerb: theVerb invItem)
	)
)

(instance blondHand of View
	(properties
		view			vBarFlys
		loop			lBods
		cel			cBlondHand
		x				126
		y				121
		z				handZ
		priority		10
		signal		(| fixPriOn ignrAct)
		description "Ivan's hand"
		sightAngle	40
	)
	(method (doVerb theVerb invItem)
		(blondGuy doVerb: theVerb invItem)
	)
)

(instance blondGuy of Person
	(properties
		view			vBarFlys
		loop			lBods
		cel			cBlondBod
		x				106
		y				115
		priority		10
		signal		(| fixPriOn stopUpdOn ignrAct)
		description "Ivan"
		lookStr		"Obviously, heavy drinking is not just an obsession
						with this guy!"
		sightAngle	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "It's not a good idea to pick a fight in a dive like this,
					Larry!")
			)
			(verbTalk
				(Print "\"Hey, buddy,\" you shout to the man at the end of the
					bar, \"is this the only place in this town to find a little
					action?\"")
				(Print "\"Hardly,\" he snorts between belts,
					\"ain'cha never heard o' taxicabs?\"")
			)
			(verbZipper
				(Print "Larry! That's a good way to get yourself killed in a
					place like this!")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance babeTop of Prop
	(properties
		view			vBarFlys
		loop			lBabeHair
		cel			0
		x				146
		y				125
		z				21
		approachX	babeX
		approachY	babeY
		description "the babe"
		priority		10
		signal		(| fixPriOn ignrAct)
		sightAngle	40
	)
	(method (doVerb theVerb invItem)
		(babe doVerb: theVerb invItem)
	)
)

(instance babe of Person
	(properties
		view			vBarFlys
		loop			lBabeLeg
		cel			0
		x				146
		y				125
		approachX	babeX
		approachY	babeY
		description "the babe"
		priority		10
		signal		(| fixPriOn ignrAct)
		lookStr		"She may not be a great looker,
						but think of the muscles she must have in that leg!"
		sightAngle	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "SLAP!" HIGH)
			)
			(verbSmellTaste
				(self doVerb: verbDo)
			)
			(verbTalk
				(Print "\"Hi there, lovely leg!\" you say casually.
					\"My name's Larry; Larry Laffer.\"")
				(Print "\"Who cares?\" she replies sarcastically.
					\"Stop bugging me, or I'll have my boyfriend take care of
					you when he gets back from the rest room!\"" HIGH)
				(Print "(Looks like you're operating at your standard
					level of charm, Larry.)" LOW)
			)
			(verbZipper
				(Print "Yeah, Larry! Smart thinking.
					Why waste your precious time on small talk?")
				(Print "Larry, this girl's not interested in a manly man like you.
					Keep hunting. You'll find someone sooner or later....")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)
(instance kenHead of Prop
	(properties
		view			vBarFlys
		loop			lKenTalkLeft
		x				178
		y				128
		z				kenHeadZ
		priority		11
		signal		(| fixPriOn ignrAct fixedLoop)
		description	"Ken's head"
		sightAngle  40
	)
	(method (doVerb theVerb invItem)
		(ken doVerb: theVerb invItem)
	)
)

(instance ken of Person
	(properties
		view			vBarFlys
		loop			lKenTurnRight
		cel			cBodLeft
		x				178
		y				128
		priority		10
		signal		(| fixPriOn ignrAct)
		description	"Ken"
		sightAngle  40
		lookStr		"A large man sits beside the only woman in Lefty's.
						He's obviously thrilled with the sound of his own voice."
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "It's not a good idea to pick a fight in a place like this,
					Larry!")
			)
			(verbTalk
				(cond
					((not sitting)
						(Print "He has nothing to say, but says it continuously.")
					)
					((== (ken script?) sKenTalksGirl)
						(Print "He's not bothering you now;
							leave him alone, you big bully!")
					)
					(else
						(Cls) 					;in case he's telling a joke
						(Print "\"Cram it, clown!\" you tell the bore on the stool
							next to you!")
						(kenHead setScript: sSulk)
					)
				)
			)
			(verbZipper
				(Print "Larry! That's a good way to get yourself killed in a
					place like this!")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance dude of View
	(properties
		view			vBarFlys
		loop			lBods
		cel			cDickBod
		x				236
		y				130
		description "James"
		priority		10
		signal		(| fixPriOn ignrAct)
		sightAngle  40
	)
	(method (doVerb theVerb invItem)
		(skinnyMan doVerb: theVerb invItem)
	)
)

(instance skinnyMan of Person
	(properties
		view			vBarFlys
		loop			lDudeDrink
		cel			2
		x				237
		y				130
		z				22
		description "James"
		priority		10
		signal		(| fixPriOn ignrAct)
		sightAngle  40
		lookStr		"A skinny man sits at the bar, talking to his overweight
						friend."
		detailLevel 2
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "It's not a good idea to pick a fight in a place like this,
					Larry!")
			)
			(verbZipper
				(Print "Larry! Put that thing away!")
				(Print "(Besides, he already has a date!)" LOW)
			)
			(verbTalk
				(Print "\"Excuse me,\" you say to the skinny man,
					\"is by any chance your last name Sprat?\"")
				(Print "\"Buzz off, Jerk!\"")
				(Print "He continues to concentrate on his overweight buddy,
					leaving you with nothing to do but find something else to do.")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance fatso of Person
	(properties
		view			vBarFlys
		loop			lFatsoPick
		cel			5
		x				263
		y				132
		priority		10
		signal		(| fixPriOn ignrAct)
		description "Richard"
		lookStr		"An overweight man sits at the bar, talking to his skinny
						friend."
		sightAngle  40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "It's not a good idea to pick a fight in a place like this,
					Larry!")
			)
			(verbTalk
				(Print "\"Excuse me, buddy,\" you say to the overweight man,
					\"but how many stools are under there?\"")
				(Print "\"Jer, er, buzz off, Jerk!\"")
				(Print "He continues to concentrate on his underweight buddy,
					leaving you with nothing to do but find something else to do.")
			)
			(verbZipper
				(Print "Larry! Put that thing away!")
				(Print "(Besides, it looks like he's already found a date!)" LOW)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance lefty of Person
	(properties
		view			vLefty
		loop			0
		cel			0
		x				201
		y				101
		description "Lefty"
		sightAngle  40
	)
	(method (cue)
		(super cue:)
		(Print "He appears to be perverted, twisted and sick!")
		(Print "(You instantly take a liking to him.)" LOW)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if (not sitting)
					(ego setHeading: (GetAngle (ego x?) (ego y?) (self x?) (self y?)) self)
				else
					(self cue:)
				)
			)
			(verbZipper
				(Print "Larry! Put that thing away!
					Lefty isn't interested in playing \"match me for a drink\"
					with you!")
			)
			(verbDo
				(self doVerb: verbTalk)
			)
			(verbTalk
				(cond
					((not sitting)
						(Print "Why not sit down at the bar first?")
					)
					((or
						ordered
						(== (lefty script?) sLeftyServes)
					 )
						(Print "Hold on; you haven't got your last order yet!")
					)
					(else
						(if (not saidHello)
							(= saidHello TRUE)
							(Print "You greet the bartender with a friendly \"Hullo.\"")
						)
						(theGame setCursor: ARROW_CURSOR TRUE)
						(Animate (cast elements?) FALSE)
						(= ordered (PrintD {\"What'll it be?\" Lefty responds.}
								leftyTalksAt
								#new
								#button	{A Round}		ROUND
								#button  {Champagne} 	CHAMPAGNE
								#button	{ Wine }			WINE
								#new
								#button  {Light Beer}	LIGHTBEER
								#button	{ Beer }			BEER
								#button	{ Whiskey }		WHISKEY
							)
						)
						(theGame setCursor: ((theIconBar curIcon?) cursor?) TRUE)
						(switch ordered
							(WHISKEY
								(Print "\"I'll have a glass of your fine,
									well whiskey!\" you tell Lefty.")
								(lefty setScript: sLeftyServes)
							)
							(WINE
								(Print "\"May I please have a glass of your delicate
									white zinfandel, sir?\" you ask Lefty.")
								(Print "Hey, was that your voice?")
								(lefty setScript: sLeftyServes)
							)
							(BEER
								(Print "\"Gimme a mug o' beer!\" you order Lefty.")
								(lefty setScript: sLeftyServes)
							)
							(CHAMPAGNE
								(Print "\"I ain't sellin' no sissy drinks here,\"
									says Lefty. \"Try again.\"" leftyTalksAt)
								(= ordered 0)
							)
							(LIGHTBEER
								(Print "You mean to say you're playing
									\"Leisure Suit Larry,\"
									and all you want is LIGHT beer??" leftyTalksAt)
								(= ordered 0)
							)
							(ROUND
								(= ordered 0)
								(cond
									((not beenToldPrice)
										(= beenToldPrice TRUE)
										(Print "\"Are you sure?\" asks Lefty.
											\"It'll cost ya $90.00!\"" leftyTalksAt)
									)
									((< larryDollars 90)
										(Print "\"I really want to, Mr. Lefty,\"
											you say, \"but I just don't have 90 bucks!\"")
									)
									(else
										(Print "\"Hey, everybody,\" cries Lefty,
											\"Here comes a round from the last of
											the big time spenders!\"" leftyTalksAt)
										(Print "You pop $90.00 out on the bar in
											another unsuccessful attempt to buy
											friendship.")
										(-= larryDollars 90)
									)
								)
							)
						)
					)
				)
			)
			(verbUse
				(switch invItem
					(iWallet
						(cond
							((not sitting)
								(Print "Try sitting at the bar; that usually
									gets Lefty's attention.")
							)
							((< larryDollars 10)
								(Print "You don't have enough money!")
							)
							(else
								(-= larryDollars 10)
								(switch (++ tipCount)
									(1
										(Print "\"Double down on eleven.\""
											leftyTalksAt)
									)
									(2
										(Print "\"If you're short of money, you could
											always try gambling at the casino.\""
											leftyTalksAt)
									)
									(3
										(Print "\"If you're bored hanging around my
											place, try hailing a cab outside.\""
											leftyTalksAt)
									)
									(4
										(Print "\"Have you telephoned Sierra On-Line
											lately?\"" leftyTalksAt)
									)
									(else
										(Print "\"Guys tell me I need to clean my rest
											room walls.\"" leftyTalksAt)
									)
								)
							)
						)
					)
					(iWhiskey
						(if (not sitting)
							(Print "Try sitting at the bar; that usually
								gets Lefty's attention.")
						else
							(Print "\"No, thanks,\" Lefty replies.
								\"I don't drink.\"" leftyTalksAt)
					   )
					)
					(else
						(super doVerb: theVerb invItem)
					)
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance fan of Prop
	(properties
		view			vBarInterior
		loop			lFan
		cel			0
		x				170
		y				0
		description	"the fan"
		sightAngle  40
	)
	(method (cue)
		(super cue:)
			(Print "It's just going around in circles...")
			(Print "...like your life.")
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbZipper
				(Print "Good idea! Sliced meat loaf, anyone?")
			)
			(verbLook
				(if (not sitting)
					(ego setHeading: (GetAngle (ego x?) (ego y?) (self x?) (self y?)) self)
				else
					(self cue:)
				)
			)
			(verbDo
				(Print "When you decided to become a swinging guy,
					this wasn't exactly what you had in mind.")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance mooseEyes of Prop
	(properties
		view			vBarInterior
		loop			lEyes
		cel			3
		x				269
		y				130
		z				86
		description "the moose's eyes"
		sightAngle  40
		detailLevel	2
	)
	(method (doVerb theVerb invItem)
 		(moose doVerb: theVerb invItem)
	)
)

(instance peephole of Prop
	(properties
		view			vBarInterior
		loop			lSlot
		cel			0
		x				303
		y				140
		z				peepholeZ
		priority		10
		signal		(| fixPriOn ignrAct)
		description "the peephole"
		sightAngle	40
		approachX	doorX
		approachY	doorY
	)
	(method (cue)
		(super cue:)
		(if cel
			(Print "An eye peers back at you through the peephole in the door.")
		else
			(Print "The peephole is tightly shut.")
		)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if (not sitting)
					(ego setHeading: (GetAngle (ego x?) (ego y?) (self x?) (self y?)) self)
				else
					(self cue:)
				)
			)
			(verbDo
				(Print "The peephole is locked from the inside.")
			)
			(else
				(door doVerb: theVerb invItem)
			)
		)
	)
)

(instance door of Prop
	(properties
		view			vBarInterior
		loop			lDoor
		cel			0
		x				306	;286
		y				139
		priority		9
		signal		(| ignrAct fixPriOn)
		description "the door"
		approachX	doorX
		approachY	doorY
		sightAngle	40
		lookStr		"You wonder how many naugas had to give
						their all just to decorate this sleazehole."
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(cond
					(sitting
						getupMsg
					)
					((self script?)
						(Print "Patience, Larry; patience!")
					)
					(else
					 	(self setScript: sDoor)
					)
				)
			)
			(verbUse
				(switch invItem
					(iGraffiti
						(if (== (sDoor state?) QUERY)
							(HandsOff)
							(Print "\"The password is: `Ken Sent Me'\" you
								whisper through the open peephole.")
							(sDoor
								seconds:	3,
								state:	SCRAM,
								cue:
							)
						else
							(Print "You carefully whisper \"Ken Sent Me\" to the
								closed door. Why, you do not know.")
						)
					)
					(iHammer
						(Print "The Naugahyde bounces the hammer back,
							grazing your scalp but slightly.")
					)
					(else
						(super doVerb: theVerb invItem)
					)
				)
			)
			(verbTalk
				(if (== (sDoor state?) QUERY)
					(Print "\"Does the password have two syllables?\"
						you ask the man on the other side of the peephole.")
					(Print "\"At least,\" replies the\n
						voice through the door."
						pimpTalksAt)
					(sDoor
						seconds: 	0,
						cue:
					)
				else
					(Print "No one can hear you through the door.")
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance stool of Feature
   (properties
      x 206
      y 150
      z 13
      heading 0
      nsLeft 200
      nsTop 125
      nsBottom 149
      nsRight 212
      description "the stool"
      sightAngle 40
		approachX	stoolX
		approachY	stoolY
		lookStr		"There is a lovely, empty, pink stool just waiting
						for a lovely, empty, pink rump like yours."
   )
   (method (doVerb theVerb invItem)
      (switch theVerb
         (verbDo
				(kenHead setScript: sTellJoke)
				(HandsOff)
				(curRoom setScript: sSitDown)
         )
			(verbSmellTaste
				(Print "Phew!")
			)
         (else
            (super doVerb: theVerb invItem &rest)
         )
      )
   )
)

(instance moose of Feature
   (properties
      x 272
      y 134
      z 86
      heading 0
      nsLeft 246
      nsTop 24
      nsBottom 73
      nsRight 298
      description "the moosehead"
      sightAngle 40
		lookStr		"It's an antique, left over from \"King's Quest III.\""
   )
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbTalk
				(if sitting
					(Print "Geez, Larry, you MUST be drunk!
						Talking to a moose, indeed?!")
					(= tookADrink 4)
				else
					(super doVerb: theVerb invItem)
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance painting of Feature
   (properties
      x 171
      y 110
      z 64
      heading 0
      nsLeft 129
      nsTop 29
      nsBottom 63
      nsRight 214
      description "the painting"
      lookStr "You don't know much about art,
					but you do know what you like!"
      sightAngle 40
   )
)

(instance sScratchy of Sound)

(procedure (PickAJoke)
	(switch (++ jokeNumber)
		(1
			(Print "\"...'cause Thursday's your night in the barrel!\""
				kenTalksAt
				#dispose:)
		)
		(2
			(Print "\"...just think of the money he saved!\""
				kenTalksAt
				#dispose:)
		)
		(3
			(Print "\"...and there stood the pig and the cow!\""
				kenTalksAt
				#dispose:)
		)
		(4
			(Print "\"...dammit Liza, doncha know ya own reflection?\""
				kenTalksAt
				#dispose:)
		)
		(5
			(Print "\"...what's the matter, Honey? Ain't you got a vase?\""
				kenTalksAt
				#dispose:)
		)
		(6
			(Print "\"...You idiot! I said POSSE!!\""
				kenTalksAt
				#dispose:)
		)
		(7
			(Print "\"...nobody can eat THAT much ice cream!\""
				kenTalksAt
				#dispose:)
		)
		(8
			(Print "\"...25 bucks, same as in town!\""
				kenTalksAt
				#dispose:)
		)
		(else
			(Print "\"...a lotta guys just leave her hanging in a tree
				somewhere!\""
				kenTalksAt
				#dispose:)
			(= jokeNumber 0)
		)
	)
)

