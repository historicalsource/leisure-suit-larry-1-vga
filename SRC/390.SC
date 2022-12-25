;**
;**	Logics for Room 390 -- Honeymoon Suite
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford &
;**
;**	Last Update:  June 25, 1991
;**


;**		No music, until radio turned on, then swing jazz.
;**	Pno,Bass,Drums to Mac the Knife.  Interupted for liquor delivery ad
;**	jingle music, then back to jazz music.  Piano solo resumes.
;**	Big Band shout section during censored section.
;**	When she leaves, we return to the melody and groove changes
;**	till room exit.

(script# 390)

(public
	rm390		0
)

(local
	wearingRubber
	pouredWine
	doneForeplay
	knifeTimer
	radioTimer
	fawnDollars 
	DIVEPTS = [
	3 0 45 92
	3 1 45 92
	3 2 50 85
	3 3 53 83
	3 4 59 74
	3 5 70 79
	3 6 76 81
	3 7 98 99
	PATHEND]
)

(define RADIOMIN 		200)
(define RADIOMAX 		600)
(define KNIFETIME 	(* (+ 1 howFast) 300))
(define tiedToBed (== (ego view?) vLarryTiedup))
(define fawnX 153)
(define fawnY 132)
(define radioX 85)
(define radioY 96)
(define wineX 54)
(define wineY 160)
(define doorX 230)
(define doorY 96)
(define ribbonX 116)
(define ribbonY 143)
(define newRibbonX 89)
(define newRibbonY 100)
(define flowerX 235)
(define flowerY 132)

;(define	sFight					171)
;(define	mMacTheKnife			390)
(define	sKnifeFlip				393)
(define	sCuttingRibbon			394)
(define	sCommercial				395)
(define  mHoneyMoon2				396)
;(define	sGlassBreaking			167)
;(define	sChannelChanging		140)
;(define	sDoorOpen				801)
;(define	sDoorClose				802)


(define	pHoneymoonSuite		390)
(define	vHoneymoonSuite		390)
	(define lRibbon				0)
		(define cCut				1)
	(define lWine					1)
	(define lDoor					2)
(define	vLarryTiedup			391)
	(define lCloud					0)
	(define lThrust				1)
	(define lCutLoose				2)
	(define lJumpOff				3)
	(define lKnife					4)
	(define lKnifeFlipping		5)
	(define lCensored				6)
	(define lTiedup				7)
(define	vEveInBedroom			392)
	(define lSitDown				0)
		(define cStanding			0)
		(define cSitting			3)
	(define lLegRight				1)
	(define lFidget				2)
	(define lFawnGlass			3)
		(define cFawnGlassHold	5)
(define vPourWine					393)
	(define lWineGetAndTurn		0)
		(define cHasWine			1)
		(define cTurn				5)
	(define lWineWalk				1)
	(define lMirror				2)
	(define lWineGlasses			3)
	(define lWinePour				4)
	(define lWineFlow				5)
	(define lWinePoured			6)
;	(define skip 				7)
;	(define skip				8)
	(define lGlassHand			9)
	(define lGlassLift			10)
	(define lGlassDrink			11)
	(define lGlassFinish			12)
	(define lGlassToss			13)
	(define lGlass					14)
;(define	vLarryUndressing		803)
;	(define lRemoveCoat			0)
;	(define lRemovePants			1)
;	(define lJumpInBed			2)

(instance rm390 of LLRoom
	(properties
		picture		pHoneymoonSuite
	)
	(method (init &tmp foo)
		(= foo JUMP)	;also used in 375
		(if debugging
			(Bset fFawnInRoom)
			(ego get: iPocketKnife)
		)
		(if (> debugging 1)
			(Bset fOrderedWine)
		)
		(if (> debugging 2)
			(= pouredWine TRUE)
		)
;		(curRoom addObstacle:
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	235 96 85 96 85 100 157 117 157 128 137 138 57 156 38 177
;						70 182 210 182 210 171 221 162 254 154 240 136 214 114
;						241 114 248 95 248 0 319 0 319 189 0 189 0 0 235 0,
;				yourself
;			)
;		)
		; Polygon Editor 1.11
		; Dynamic Obstacles : Picture 390
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	235 96 85 96 85 109 157 109 157 128 137 138 57 156 38 177
						70 182 210 182 210 171 221 162 254 154 240 136 214 114
						241 114 248 95 248 0 319 0 319 189 0 189 0 0 235 0,
				yourself
			)
		)
		(LoadMany VIEW
			vHoneymoonSuite
			vLarryTiedup
			vEveInBedroom
			vLarryGetting
		)
		(LoadMany SOUND
			mMacTheKnife
			sCommercial
			sChannelChanging
			sDoorOpen
			sDoorClose
		)
		(theMusic
			loop:			-1,
			vol:			127,
			flags:		1,
		)
		(theMusic2
			number:		sCommercial,
			loop:			1,
			vol:			127,
			flags:		1,
		)
		(theMusic3
			setLoop:		1,
			flags:		1,
		)
		(ego
			x:				241,
			y:				88,
			init:			,
			illegalBits: 0,
			setHeading:	180,
			actions:		egoActions,
		)
		(eHead actions: egoActions)
		(HandsOff)
		(curRoom setScript: sEnter)
		(super init:)
		(door
			init:,
			cycleSpeed:		howFast,
			approachVerbs:	objectVerbs,
		)
		(fawn
			init:,
			cycleSpeed:		howFast,
			moveSpeed:		howFast,
			approachVerbs: verbLook actorVerbs
		)
		(ribbon
			init:,
			approachVerbs:	objectVerbs verbLook
		)
		(if (Btst fTurnedOnRadio)
			(theMusic
				number:			mMacTheKnife,
				play:				127,
			)
			(if (not (Btst fOrderedWine))
				(= radioTimer (Random (* (+ 1 howFast) 200) (* (+ 1 howFast) 300)))
			)
		)
		(if (Btst fOrderedWine)
			(Load VIEW
				vPourWine
				vLarryUndressing
			)
			(LoadMany SOUND
				sFight
				sKnifeFlip
				sCuttingRibbon
				mHoneyMoon2
				sGlassBreaking
				sDrinking
			)
			(wine
				init:,
				approachVerbs:	verbLook objectVerbs
			)
		)
		(theBed init:)
		(theWindow init:)
		(theRadio
			init:,
			approachVerbs:	objectVerbs
		)
		(painting init:)
		(iceBucket
			init:,
			approachVerbs:	verbLook objectVerbs
		)
		(theChair init:)
		(painting2 init:)
		(sculpture init:)
		(flower
			init:,
			approachVerbs:	objectVerbs verbLook
		)
	)
	(method (doit)
		(super doit: &rest)
		(cond
			(script)
			(knifeTimer
				(-- knifeTimer)
				(if (== knifeTimer 1)
					(Print "Since you have no way of cutting the ribbon and it's
						far too tightly tied for you to loosen,
						it looks like this is the end for you, Larry.")
					(Death vLarryTiedup lTiedup	)
					(Format @deathTitleStr "\"...he's currently tied up\"")
					(GameOver "Probably the housekeeper will find you when
						she makes her usual rounds... in a few weeks!")
				)
			)
			(radioTimer
				(if
					(> radioTimer 1)
					(-- radioTimer)
				)
				(if
					(and
						(== radioTimer 1)
						(not (curRoom script?))
					)
					(= radioTimer (Random (* (+ howFast 1) RADIOMAX) (* (+ howFast 2) RADIOMAX)))
					(HandsOff)
					(curRoom setScript: sDoCommercial)
				)
			)
		)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if (Btst fFawnInRoom)
					(Print "You are in the casino hotel's honeymoon suite.
						The heart-shaped bed is just waiting for you and Fawn.
						There is a radio by the bed.
						This place is perfect for your wedding night!")
					(Print "The dim lighting sets the perfect atmosphere.
						What a lovely room!")
					(Print "(You've always been a fan of the
						\"Early American Gaudy\" school of decorating.)" LOW)
				else
					(Print "This is the honeymoon suite.
						The heart-shaped bed seemed perfect for you and Fawn.
						Too bad things worked out like this!")
				)
			)
			(verbTalk
				(if tiedToBed
					(Print "\"FAWN!! You scum-sucking gutterslug,
						get back in here!\"")
					(Print "Unfortunately, your touching plea goes unheeded.")
				else
					(super doVerb: theVerb invItem)
				)
			)
			(else
				(if tiedToBed
					(Print "You can't. You're tied to a bed!")
				else
					(super doVerb: theVerb invItem)
				)
			)
		)
	)
	(method (dispose)
		(DisposeScript: JUMP)
		(super dispose:)
	)
)

(class egoActions of Code
	(methods
		doVerb
	)
	(method (doVerb theVerb invItem)
		(if (not tiedToBed)
			(return
				(switch theVerb
					(verbUse
						(switch invItem
							(iLubber
								(if wearingRubber
									(Print "You already are wearing your \"lubber!\"")
								else
									(Print "O.K. Good idea.")
									(= wearingRubber TRUE)
								)
							)
						)
					)
					(verbZipper
						(cond
							((not (Btst fOrderedWine))
								(Print "\"Hey, don't rush it, buster!
									Can't a gal even have a drink first?\" 
									she \"wines.\"
									\"Geez, what a romantic!\"")
							)
							((not pouredWine)
								(Print "\"Aren't you going to pour the wine?\"
									she says, her eyes flashing.")
							)
							((not doneForeplay)
								(Print "\"Ain'cha never heard of `foreplay,' 
									Larry?\" says Fawn.
									\"I ain' no rabbit, you know!\"")
							)
							((and
								(== (ego x?) fawnX)
								(== (ego y?) fawnY)
							 )
								(HandsOff)
								(curRoom setScript: sGetHim)
								(return TRUE)
							)
						)
					)
				)
			)
		else
			(return
				(switch theVerb
					(verbDo
						(Print "You can't. She fastened you quite securely!")
					)
					(verbTalk
						(Print "\"FAWN!! You scum-sucking gutterslug,
							get back in here!\"")
						(Print "Unfortunately, your touching plea goes unheeded.")
					)
					(verbUse
						(switch invItem
							(iPocketKnife
								(HandsOff)
								(curRoom setScript: sCutLoose)
							)
							(else
								(Print "You can't. You're tied to a bed!")
							)
						)
					)
					(else
						(Print "You can't. You're tied to a bed!")
					)
				)
			)
		)
	)
)

;
;	Scripts
;

(instance sEnter of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= cycles 1)
			)
			(
				(door
					setCycle:	EndLoop self
				)
				(theMusic3
					number:		sDoorOpen,
					play:
				)
			)
			(
				(ego
					setMotion:	PolyPath 238 103 self
				)
			)
			(
				(door
					setCycle:	BegLoop
				)
				(theMusic3
					number:		sDoorClose,
					play:
				)
				(ego
 					illegalBits:	$8000,
					setMotion:		PolyPath 211 106 self
				)
			)
			(
				(= cycles 10)
			)
			(
				(if (and (Btst fOrderedWine)
							(not (Btst fSeenDeliveryBoyGag)))
					(Bset fSeenDeliveryBoyGag)
					(Print "\"Oh, Larry, it's you!\" Fawn says.
						\"I thought it was that cute, young delivery boy again.\""
						HIGH)
					(Print "Hmm. What does she mean by that? Oh, well.
						You decide to let it pass.
						After all, this IS your wedding night!")
				else
					(Print "\"Oh, Larry!\" oozes Fawn,
						\"I've been waiting patiently right here on the bed
						just for you.\"" HIGH)
				)
				(fawn setScript: sFawn)
				(door stopUpd:)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sGetHim of Script
	(method (doit)
		(super doit: &rest)
		(if (and (== (theMusic prevSignal?) -1)
					state
					(Btst fTurnedOnRadio))
			(theMusic
				number:	mMacTheKnife,
				loop:		-1,
				vol:		127,
				play:,
			)
		)
	)
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= radioTimer 0)
				(Print "She says, \"Lie down, Larry.
					I have a special little surprise for you!\"")
				(ego
					egoSpeed:,
					setMotion:	PolyPath newRibbonX newRibbonY self
				)
				(fawn
					setScript:	0,
					cycleSpeed:	larrySpeed:,
					moveSpeed:	larrySpeed:,
					setLoop:		lSitDown,
					setCel:		cSitting,
					setCycle:	BegLoop fawn,
				)
			)
			(
				(ego
					setHeading:	180 self
				)
			)
			(
				(Print "Then she says, \"Close your eyes,\"
					and you do because you're sure that, finally,
					success is at hand." BIG)
				(theMusic
					pause:		FALSE,
					number:		mHoneyMoon2,
					loop:			1,
					play:,
				)
				(fawn
					hide:,
					setMotion:	0,
				)
				(ego
					egoSpeed:,
					view:			vLarryTiedup,
					setLoop:		lCensored,
					setCel:		0,
					x:				72,
					y:				116,
					setPri:		9,
				)
				(= seconds 4)
			)
			(
				(Print "\"Oh, Fawn!\" you moan." HIGH)
				(= seconds 4)
			)
			(
				(Print "\"Ooh, Baby! That feels so good! Ooooooooooh!!\"" HIGH)
				(= seconds 4)
			)
			(
				(Print "\"Say, Fawn, when are you going to get undressed?\"" HIGH)
				(= seconds 4)
			)
			(
				(Print "\"Hey, what are you doing with the ribbon?\"" HIGH)
				(= seconds 4)
			)
			(
				(Print "\"Oh, I see! We're gonna get kinky our first time?\"" HIGH)
				(= seconds 2)
			)
			(
				(theMusic3
					number:		sFight,
					loop:			-1,
					play:
				)
				(ego
					egoSpeed:	0,
					setLoop:		lCloud,
					setCel:		0,
					setCycle:	RandCycle,
				)
				(= seconds 6)
			)
			(
				(theMusic3 stop:)
				(if (not (Btst fTurnedOnRadio))
					(theMusic fade:)
				)
				(if 
					(> larryDollars 10)
					(= fawnDollars (- larryDollars 10))
					(= larryDollars 10)
				else
					(= fawnDollars (- larryDollars 1))
					(= larryDollars 1)
				)
				(ego
					egoSpeed:,
					x:				45,
					y:				92,
					setLoop:		lThrust,
					setCel:		0,
					setCycle:	0
				)
				(fawn
					show:,
					view:			vFawnWalking,
					setCycle:	Walk,
					setLoop:		RELEASE,
				)
				(= seconds 3)
			)
			(
				(Print "When Fawn has you securely tied to the bed,
					she leaps from the bed, grabs your wallet, and says,
					\"You forgot to pay me for the wine!\"")
				(Printf "\"It came to $%d.00, including the tip.\"" fawnDollars)
				(= cycles (* (+ 1 howFast) 5))
			)
			(
				(fawn
					setMotion:	PolyPath doorX doorY self
				)
			)
			(
				(Print "\"Wait, Fawn!\" you shout, \"Come back!
					This is our wedding night!\"")
				(door
					setCycle:	EndLoop self
				)
				(theMusic3
					number:		sDoorOpen,
					setLoop:		1,
					play:
				)
			)
			(
				(Print "\"So long, sucker!\"")
				(= seconds 3)
			)
			(
				(Print "\"FAWN! Come back!\" you shout. \"This isn't funny!!\"")
				(fawn
					setMotion:	PolyPath 241 88 self
				)
			)
			(
				(Bclr fFawnInRoom)
				(theMusic3
					number:		sDoorClose,
					play:
				)
				(door
					setCycle:	BegLoop self
				)
			)
			(
				(= seconds 2)
			)
			(
				(fawn dispose:)
				(Print "Hmmm. Does it count if a guy \"consummates\" by himself?")
				(= seconds 2)
			)
			(
				(Print "Larry, it seems \"the honeymoon's over.\"")
				(if (not (ego has: iPocketKnife))
					(= knifeTimer KNIFETIME)
				)
				(HandsOn)
				(User canControl: FALSE)
				(theIconBar disable: walkIcon)
				(self dispose:)
			)
		)
	)
)

(instance sCutLoose of Script
	(method (doit)
		(super doit: &rest)
		(if (and (== (theMusic prevSignal?) -1)
					(Btst fTurnedOnRadio))
			(theMusic
				number:	mMacTheKnife,
				loop:		-1,
				vol:		127,
				play:,
			)
		)
	)
	(method (changeState newState)
		(switchto (= state newState)
			(
				(Print "Right! Using the pocket knife you got from the old bum
					by the convenience store, you saw through the ribbon holding
					you to the bed.")
				(ego
					egoSpeed:   (+ larrySpeed 1),
					setCycle:	EndLoop self
				)
			)
			(
				(ego
					setLoop:			lCutLoose,
					setCel:			0,
				)
				(theMusic3
					number:			sKnifeFlip,
					loop:				1,
					play:
				)
				(theObject
					init:,
					cycleSpeed:		larrySpeed,
					moveSpeed:		larrySpeed,
					view:				vLarryTiedup,
					loop:				lKnifeFlipping,
					x:					79,
					y:					90,
					setStep:			15 15,
					setPri:			14,
					setCycle:		Forward,
					setMotion:		MoveTo 74 44 self
				)
			)
			(
				(theObject
					setMotion:		MoveTo 69 90 self
				)
			)
			(
				(theObject
					setLoop:			lKnife,
					x:					73,
					y:					92,
				)
				(= seconds 3)
			)
			(
				(theMusic3
					number:			sCuttingRibbon,
					loop:				1,
					play:,
				)
				(theObject 			dispose:)
				(ego
					setCycle:		CycleTo 2 1 self
				)
			)
			(
				(ribbon
					setCel: 			cCut,
					stopUpd:			,
				)
				(ego setCycle: 	EndLoop self)
			)
			(
				(Points pCutRibbon ptsCutRibbon)
				(= seconds 3)
			)
			(
				(ego
					setLoop:			lJumpOff,
					setCel:			0,
					setCycle:		MoveCycle @DIVEPTS self
				)
			)
			(
				(ego
					setPri:			RELEASE,
					view:				vLarryUndressing,
					setLoop:			lRemovePants,
					setCel:			255,
					setCycle:		BegLoop self
				)
				(if wearingRubber
					(Print "You carefully remove your \"lubber.\"")
				)
			)
			(
				(ego
					setLoop:			lRemoveCoat,
					setCel:			255,
					setCycle:		BegLoop self
				)
			)
			(
				(NormalEgo 0)
				(= seconds 3)
			)
			(
				(Printf "You grab your wallet and look inside.
					Oh, well. At least she didn't find the %d buck%s
					you hid in that secret compartment.
					With your consummate gambling skills,
					you should be back on your feet in no time!"
						larryDollars
						(if (== larryDollars 1) {} else {s})
				)
				(ribbon approachX: newRibbonX)
				(ribbon approachY: newRibbonY)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sExit of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(Cls)
				(door
					setCycle:	EndLoop self
				)
				(theMusic3
					number:		sDoorOpen,
					play:
				)
			)
			(
				(ego
					illegalBits:	0,
					setMotion: 		PolyPath 241 88 self
				)
			)
			(
				(theMusic3
					number:		sDoorClose,
					play:
				)
				(door
					setCycle:	BegLoop self
				)
			)
			(
				(theMusic fade:)
				(curRoom newRoom: 340)
			)
		)
	)
)

(instance sGetRibbon of Script
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
				(Print "Good idea.
					You never know when a nice piece of ribbon will come in handy.")
				(ego get: iRibbon)
				(ribbon dispose:)
				(Points pGotRibbon ptsGotRibbon)
				(ego
					setCycle:	BegLoop self
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

(instance sDoRadio of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(Points pTurnedOnRadio ptsTurnedOnRadio)
				(ego
					egoSpeed:,
					view:			vLarryGetting,
					loop:			lHighLeft,
					cel:			0,
					setCycle:	EndLoop self
				)
			)
			(
				(if register
					(theMusic3
						number:		sChannelChanging,
						loop:			1,
						play:,
					)
					(if (== (theMusic number?) mMacTheKnife)
						(theMusic pause: FALSE)
					else
						(theMusic
							number:			mMacTheKnife,
							play:
						)
					)
					(Print "Sounds like a nice mellow station...")
					(Print "The tuning knob on the radio is stuck.
						Looks like you're stuck with the Easy Listening station.")
					(if (not (Btst fOrderedWine))
						(= radioTimer (Random (* (+ 1 howFast) 200) (* (+ 1 howFast) 300)))
					)
				else
					;stop the music
					(theMusic pause: TRUE)
					(= radioTimer 0)
					(theMusic3
						number:		sChannelChanging,
						loop:			1,
						play:,
					)
					(Print "Click.")
				)
				(ego
					setCycle:	BegLoop self
				)
			)
			(
				(NormalEgo 2)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sFawn of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(fawn
					setCycle:	EndLoop self
				)
				(= seconds (Random 3 5))
			)
			(
				(fawn
					setCel:		0,
					setLoop:		(Random 1 2),
					setCycle:	EndLoop,
				)
				(= seconds (Random 5 10))
			)
			(
				(= start 1)
				(self init:)
			)
		)
	)
)

(instance sPourWine of Script
	(method (changeState newState &tmp foo)
		(switchto (= state newState)
			(
				(= pouredWine TRUE)
				(ego
					egoSpeed:,
					normal:			FALSE,
					view:				vPourWine,
					setLoop:			lWineGetAndTurn,
					setCel:			0,
					setCycle:		CycleTo cHasWine 1 self
				)
			)
			(
				(ego
					setCycle:		EndLoop self
				)
				(wine dispose:)
			)
			(
				(ego
					setLoop:			lWineWalk,
					setCycle:		Walk,
					setMotion:		PolyPath fawnX fawnY self
				)
			)
			(
				(ego
					setLoop:			lWineGlasses,
					setCel:			0,
					setCycle:		EndLoop self
				)
			)
			(
				(ego
					setLoop:			lWinePour,
					setCel:			0,
					setCycle:		EndLoop self
				)
			)
			(
				(Print "You carefully remove the bottle from the ice bucket, 
					unscrew the cap, and pour two glasses.
					She downs her glass immediately, you refill it,
					and she does it again.
					Her eyes meet yours,
					and you're sure luck is with you this time!" BIG)
				(ego
					setLoop:			lWineFlow,
					setCycle:		Forward,
				)
				(= seconds 3)
			)
			(
				(ego
					setLoop:			lWinePoured,
					setCel:			0,
					setCycle:		EndLoop self
				)
			)
			(
				(ego
					setLoop:			lGlassHand,
					setCel:			0,
					setCycle:		EndLoop self
				)
			)
			(
				(= cycles 20)
			)
			(
				(fawn
					setScript:		0,
					setLoop:			lFawnGlass,
					setCel:			0,
					setCycle:		EndLoop self
				)
			)
			(
				(ego
					setLoop:			lGlassLift,
					setCel:			0,
					setCycle:		EndLoop self
				)
			)
			(
				(theMusic3
					number:			sDrinking,
					loop:				-1,
					play:
				)
				(ego
					setLoop:			lGlassDrink,
					setCycle:		Forward,
				)
				(fawn
					setCycle:		CycleTo cFawnGlassHold -1
				)
				(= seconds 3)
			)
			(
				(theMusic3 stop:)
				(ego
					setLoop:			lGlassFinish,
					setCel:			0,
					setCycle:		EndLoop self
				)
			)
			(
				(= cycles 10)
			)
			(
				(= foo JUMP)
				(ego
					setLoop:			lGlassToss,
					setCel:			0,
					setCycle:		EndLoop self
				)
				(fawn
					setCycle:		BegLoop
				)
			)
			(
				(theObject
					init:,
					cycleSpeed:		larrySpeed,
					moveSpeed:		larrySpeed,
					view:				vPourWine,
					loop:				lGlass,
					cel:				1,
					x:					191,
					y:					96,
					yStep:			5,
					setPri:			7,
					setCycle:		Forward,
					setMotion:		JumpTo 228 96 self,
				)
				(ego
					setLoop:			lMirror
				)
			)
			(
				(theMusic3
					number:			sGlassBreaking,
					loop:				1,
					play:,
				)
				(ego
					setCycle:		Walk,
					setMotion:		PolyPath wineX wineY self
				)
			)
			(
				(ego
					setLoop:			lWineGetAndTurn,
					setCel:			cTurn,
					setCycle:		CycleTo cHasWine -1 self
				)
			)
			(
				(wine init:)
				(ego
					setCycle:		BegLoop self
				)
			)
			(
				(theObject dispose:)
				(NormalEgo 1)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sDoCommercial of Script
	(method (doit)
		(super doit: &rest)
		(if (and (== state 1)
					(== (theMusic2 prevSignal?) -1))
			(= cycles 1)
		)
	)
	(method (changeState newState)
		(switchto (= state newState)
			(
				(theMusic
					vol:			0,
					pause: 		TRUE,
					changeState:,
				)
				(Print "The radio interrupts the lovely Frank Sinatra
					medley with an obnoxious singing commercial..."
					HIGH
					#dispose:)
				(= seconds 6)
			)
			(
				(theMusic2
					play:
				)
				(Cls)
				(Print "\"We're Ajax Liquor,\n
					And we're proud to say,\n
					We deliver 'cross\n
					The U.S.A.\n
					\n
					You're out of luck\n
					If you want to dine,\n
					But if you just need booze,\n
					Dial 555-8039!\""
					HIGH
					#dispose:
				)
			)
			(
				(Cls)
				(theMusic
					pause:		FALSE,
					fade:			127 10 5 0,
				)
				(Print "\"And now, back to our music.\""
					HIGH
					#dispose:)
				(= seconds 4)
				(Points pHeardLiquorStoreNumber ptsHeardLiquorStoreNumber)
			)
			(
				(Cls)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

;
;  	Props
;

(instance door of Prop
	(properties
		view			vHoneymoonSuite
		loop			lDoor
		x				257
		y				19
		description "the door"
		sightAngle	40
		priority		6
		signal		(| fixPriOn ignrAct)
		approachX 	doorX
		approachY 	doorY
	)
	(method (doVerb theVerb invItem)
		(if tiedToBed
			(Print "You can't. You're tied to a bed!")
		else
			(switch theVerb
				(verbDo
					(HandsOff)
					(curRoom setScript: sExit)
				)
				(else
					(super doVerb: theVerb invItem)
				)
			)
		)
	)
)

(instance ribbon of View
	(properties
		view			vHoneymoonSuite
		loop			lRibbon
		x				64
		y				102
		priority		8
		description "the ribbon"
		signal		(| fixPriOn stopUpdOn ignrAct)
		sightAngle	40
		approachX	ribbonX
		approachY	ribbonY
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if
					(== cel cCut)
					(Print "The remains of the ribbon are lying on the bed.")
				else
					(if tiedToBed
						 (Print "Isn't it amazing how a simple ribbon can
						 	hold you so securely?")
					else
						(Print "Isn't that cute? The honeymoon suite bed
							is decorated like a giant box of candy--complete
							with a cute red ribbon.")
					)
				)
			)
			(verbDo
				(cond
					(tiedToBed
						(Print "You can't do anything. 
							Fawn has you fastened quite securely!")
					)
					((self cel?)
						(HandsOff)
						(curRoom setScript: sGetRibbon)
					)
					(else
						(super doVerb: theVerb invItem)
					)
				)
			)
			(verbUse
				(switch invItem
					(iPocketKnife
						(if tiedToBed
							(HandsOff)
							(curRoom setScript: sCutLoose)
						else
							(super doVerb: theVerb invItem)
						)
					)
					(else
						(if tiedToBed
							(Print "You can't. You're tied to a bed!")
						else
							(super doVerb: theVerb invItem)
						)
					)
				)
			)
			(verbTalk
				(if tiedToBed
					(Print "\"FAWN!! You scum-sucking gutterslug,
						get back in here!\"")
					(Print "Unfortunately, your pleas go unheeded.")
				else
					(super doVerb: theVerb invItem)
				)
			)
			(verbSmellTaste
				(Print "The ribbon smells Fawny.")
			)
			(else
				(if tiedToBed
					(Print "You can't. You're tied to a bed!")
				else
					(super doVerb: theVerb invItem)
				)
			)
		)
	)
)

(instance wine of View
	(properties
		view			vHoneymoonSuite
		loop			lWine
		x				31
		y				160
		z				31
		description "the wine"
		priority		12
		signal		(| ignrAct fixPriOn)
		sightAngle	40
		approachX 	wineX
		approachY 	wineY
	)
	(method (doVerb theVerb invItem)
		(if tiedToBed
			(Print "You can't. You're tied to a bed!")
		else
			(switch theVerb
				(verbLook
					(Print "Hmmm. Was \"Tuesday\" a good year?")
				)
				(verbDo
					(if (not (Btst fFawnInRoom))
						(Print "You grab the bottle that once held the suggestion
							of a happy marriage but now holds nothing more than
							the bitter dregs of your \"honeymoon-gone-bad.\"
							You unscrew the top and fortify your courage with
							a long, hard belt!
							You screw the lid down tight and quickly replace
							the bottle in the wine bucket.")
					else
						(HandsOff)
						(curRoom setScript: sPourWine)
					)
				)
				(verbUse
					(switch invItem
						(iHammer
							(Print "You can't.
								It's the same brand that people can't break when
								they want to christen a ship!")
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

(instance fawn of Person
	(properties
		view			vEveInBedroom
		loop			lSitDown
		cel			cStanding
		x				137
		y				132
		description "Fawn"
		illegalBits 0
		signal 		ignrAct
		sightAngle	40
		approachX	fawnX
		approachY	fawnY
	)
	(method (cue)
		(super cue:)
		(self
			setLoop:		RELEASE,
			view:			vFawnWalking,
			setCycle:	Walk,
			setMotion:	PolyPath (+ newRibbonX 15) newRibbonY
		)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(cond
					(pouredWine
						(Print "She looks ready, Larry.")
					)
					((Btst fOrderedWine)
						(Print "She looks like she could use some of that wine.")
					)
					(else
						(Print "She is so beautiful you want to do wonderful
							things to, er, ah, FOR her.")
					)
				)
			)
			(verbTalk
				(cond
					((not (Btst fOrderedWine))
						(if (Random 0 1)
							(Print "\"You know, a little wine would help me
								get in the mood, Larry.\"")
						else
							(Print "\"I'd think the least a gentleman would do is
								to buy a lady a bottle of wine.\"")
						)
					)
					((not pouredWine)
						(Print "\"Aren't you going to pour the wine?\"
							she says, her eyes flashing.")
					)
					(else
						(Print "\"I'm ready when you are, honey!\"")
						(Print "(Come on, Larry; what are you waiting for?)" LOW)
			 		)
				)
			)
			(verbDo
				(cond
					((not (Btst fOrderedWine))
						(Print "She doesn't appear to be in the mood yet, Larry.")
					)
					((not pouredWine)
						(Print "\"Aren't you going to pour the wine?\"
							she says, her eyes flashing.")
					)
					(else
						(Print "\"Ahh, that feels nice!\" sighs Fawn.")
						(Print "(You presume a little foreplay never hurts.)" LOW)
						(= doneForeplay TRUE)
					)
				)
			)
			(verbZipper
				(cond
					((not (Btst fOrderedWine))
						(Print "\"Hey, don't rush it, buster!
							Can't a gal even have a drink first?\" she \"wines.\"
							\"Geez, what a romantic!\"")
					)
					((not pouredWine)
						(Print "\"Aren't you going to pour the wine?\"
							she says, her eyes flashing.")
					)
					((not doneForeplay)
						(Print "\"Ain'cha never heard of `foreplay,' Larry?\"
							\"I ain' no rabbit, you know!\"")
					)
					(else
						(HandsOff)
						(curRoom setScript: sGetHim)
					)
				)
			)
			(verbSmellTaste
				(Print "Fawn smells like a dream.")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance theObject of Actor
	(properties
		view	  		vLarryTiedup
		illegalBits	$0000
		xStep 		1
		signal		(| ignrAct fixPriOn fixedLoop)
	)
)



;
;	Features
;

(instance theRadio of Feature
	(properties
		x 65
		y 98
		z 27
		heading 0
		nsLeft 53
		nsTop 62
		nsBottom 81
		nsRight 78
 		description "the radio"
		sightAngle 40
		approachX radioX
		approachY radioY
		lookStr		"It's pretty complicated.
						There's a knob that says \"Power.\""
	)
	(method (doVerb theVerb invItem)
		(if tiedToBed
			(Print "You can't. You're tied to a bed!")
		else
			(switch theVerb
				(verbDo
					(HandsOff)
					(if (Btst fTurnedOnRadio)
						(Bclr fTurnedOnRadio)
						(curRoom setScript: sDoRadio NULL FALSE)
					else
						(Bset fTurnedOnRadio)
						(curRoom setScript: sDoRadio NULL TRUE)
					)
				)
				(else
					(super doVerb: theVerb invItem &rest)
				)
			)
		)
	)
)

(instance theBed of Feature
	(properties
		x 62
		y 100
		z 0
		heading 0
		nsLeft 11
		nsTop 92
		nsBottom 139
		nsRight 113
 		description "the bed"
		sightAngle 40
		lookStr 		"You love the thought of a heart-shaped bed!"
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You have more important things on your mind than sitting.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance theWindow of Feature
	(properties
		x 15
		y 112
		z 59
		heading 0
		nsLeft 0
		nsTop 15
		nsBottom 92
		nsRight 30
 		description "the window"
		sightAngle 40
		lookStr "Out the window the lights of Lost Wages spread before you
					like some cheap trick."
	)
	(method (doVerb theVerb invItem)
		(if tiedToBed
			(Print "You can't. You're tied to a bed!")
		else
			(switch theVerb
				(verbDo
					(Print "This window doesn't open.")
				)
				(verbUse
					(Print "You are unable to break the window.")
				)
				(else
					(super doVerb: theVerb invItem &rest)
				)
			)
		)
	)
)
;more features
(instance painting of Feature
	(properties
		x 145
		y 93
		z 49
		heading 0
		nsLeft 105
		nsTop 18
		nsBottom 71
		nsRight 185
 		description "the painting"
		lookStr "To you, it looks like dozens of spermatozoa
					attacking a Whitman's sampler!"
		sightAngle 40
	)
)
(instance iceBucket of Feature
	(properties
		x 30
		y 166
		z 37
		heading 0
		nsLeft 24
		nsTop 120
		nsBottom 139
		nsRight 37
 		description "the ice bucket"
		sightAngle 40
		approachX 	wineX
		approachY 	wineY
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if
					(cast contains: wine)
					(Print "The ice bucket holds your fancy bottle of wine.")
				else
					(Print "An ice bucket waits beside the bed for that bottle
						of wine you didn't bring!")
				)
			)
			(verbDo
				(if (cast contains: wine)
					(wine doVerb: theVerb invItem)
				else
					(Print "You can't. Room service glued it to the table.")
				)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance vase of Feature
	(properties
		x 123
		y 164
		z 0
		heading 0
		nsLeft 113
		nsTop 140
		nsBottom 189
		nsRight 134
 		description "the vase"
		lookStr "How sweet.  Fawn has placed the rose you gave her in that vase.
					What a romantic she is!"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Oh, just leave the rose in its vase.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance theChair of Feature
	(properties
		x 259
		y 163
		z 0
		heading 0
		nsLeft 232
		nsTop 138
		nsBottom 189
		nsRight 286
 		description "chair"
		lookStr 		"You can just picture Fawn using that chair in the morning."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You're in the Honeymoon Suite and all you want to do
					is sit on a chair?!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance painting2 of Feature
	(properties
		x 288
		y 120
		z 60
		heading 0
		nsLeft 258
		nsTop 20
		nsBottom 101
		nsRight 319
 		description "the painting"
		lookStr "How clever! A painting of the opposite side of the room.
					Many hotels would just install a mirror."
		sightAngle 40
	)
)

(instance sculpture of Feature
	(properties
		x				 258
		y				 180
		z				 0
		heading		 0
		nsLeft		 197
		nsTop			 43
		nsBottom		 189
		nsRight		 319
 		description	 "the sculpture"
		lookStr		 "Wait a minute! What are those sculptures doing?!"
		sightAngle	 40
		onMeCheck	 cLRED
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance flower of Feature
	(properties
		x				263
		y				135
		z				49
		heading		0
		nsLeft		256
		nsTop			75
		nsBottom		97
		nsRight		271
 		description	"the rose"
		approachX	flowerX
		approachY	flowerY
		sightAngle	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if (Btst fFawnInRoom)
					(Print "\"Why, Fawn,\" you say,
						\"you placed my rose in a different vase.\"")
					(Print "\"But, of course, my dear,\" she replies,
						\"but let's not talk about flora at a time like this!\"")
				else
					(Print "You hope at least the rose pricked Fawn.")
				)
			)
			(verbDo
				(if (Btst fFawnInRoom)
					(Print "You only gave it to her a little while ago.
						Don't take it now.")
				else
					(Print "Why take it?
						You don't want to remember Fawn
						(that scum-sucking gutterslug!).")
				)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

