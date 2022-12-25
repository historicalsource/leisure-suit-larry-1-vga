;**
;**	Logics for Room 150 -- Hooker's Bedroom
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford &
;**
;**	Last Update:  June 17, 1991
;**

(script# 150)

(public
	rm150		0
)

(local
	oldSpeed
	popping
	screwPts = [
	2 0 100 141
	2 1 100 141
	2 2 100 141
	2 3 100 125
	2 4 104 96
	2 5 98  94
	PATHEND
	]
)

(procedure
	RoomFeats
	CloseupFeats
)

;**	Funk porn music continues from TV below, or begins if you paid pimp $$

;(define frontOfBed 10 133 135 160)
(define naked 		(== (ego view?) vLarryUndressing))
(define halfNaked (and (== (ego view?) vLarryUndressing)
							  (== (ego loop?) lRemoveCoat)))
(define bedX 100)
(define bedY 141)
(define rightX 228)
(define rightY 132)
(define underwearX 207)
(define underwearY 148)
(define hookerZ 6)
(define headZ 14)
(define eyesZ 11)

(define	mHooker					150)
(define	sWindowUp				151)
(define  sGumPopping				155)

(define	pHookersRoom			150)

(define	vHookersRoom			150)
	(define lWindow				0)
		(define cOpen				2)
	(define lCandy					1)
(define	vLarryCensored			151)
	(define lCensored				0)
	(define lCensoredFlipped	1)
	(define lOpenWindow			2)
	(define lExitWindow			3)
		(define cOutWindow		6)
(define	vHooker					152)
	(define lBody					0)
	(define lTalkFront			1)
	(define lChewFront			2)
	(define lBlowFront			3)
	(define lTalkRight			4)
	(define lChewRight			5)
	(define lBlowRight			6)
	(define lSmoke					7)
		(define cSmokeLastcel	14)
(define	pHookerCloseup			155)
(define	vHookerCloseup			155)
	(define lEyes					0)
		(define cEyesClosed		3)
	(define lMouth					1)
		(define cMouthLeft		1)
		(define cMouthHang		2)
		(define cMouthShut		5)
;(define	vLarryUndressing		803)
;	(define lRemoveCoat			0)
;	(define lRemovePants			1)
;	(define lJumpInBed			2)

(instance rm150 of LLRoom
	(properties
		picture	150
	)
	(method (init)
		(LoadMany VIEW
			vHookersRoom
			vLarryCensored
			vHooker
			vLarryUndressing
		)
		(Load PICTURE
			pHookerCloseup
		)
		(LoadMany SOUND
			mHooker
			sWindowUp
			sGumPopping
			mChannel8

		)
		(if (or (!= (theMusic number?) mChannel8)
				  (== (theMusic prevSignal?) -1))
			(theMusic
				number:		mChannel8,
				loop:			-1,
				vol:			127,
				flags:		1,
				play:,
			)
		)
		(theMusic3
			loop:			1,
			vol:			127,
			flags:		1,
		)
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	144 104 114 104 86 121 149 121 134 139 65 142 65 148 241 148
						237 139 218 139 218 132 228 132 212 115 125 115 125 109
						149 109 149 -1 319 0 319 189 0 189 0 0 144 0,
				yourself
			)
		)
		(switch prevRoomNum
			(140
				(HandsOff)
				(ego init:)
				(self setScript: sFromStairs)
			)
			(160
				(HandsOff)
				(curRoom setScript: sFromWindow)
			)
			(else
				(ego
					init:,
					posn: 170 160
				)
			)
		)
		(ego
			setPri:	RELEASE,
			actions: egoActions
		)
		(eHead actions: egoActions)
		(super init:)
		(theHooker
			cycleSpeed:		howFast,
			init:,
			approachVerbs:	actorVerbs verbLook,
		)
		(if
			(Btst fScoredHooker)
			(theHooker
				z:				hookerZ,
				setCel:		0,
				setLoop:		lSmoke,
				setScript:	sSmoke,
			)
		else
			(theHooker
				stopUpd:
			)
			(hookerHead
				cycleSpeed:		howFast,
				init:,
				approachVerbs:	actorVerbs,
			)
		)
 		(if (== ((inventory at: iCandy) owner?) 150)
			(candy
				init:,
				approachVerbs:	objectVerbs,
			)
		)
		(if (Btst fHookerWindowOpen)
			(aWindow cel: cOpen)
		)
		(aWindow
			cycleSpeed:		howFast,
			approachVerbs:	objectVerbs,
			init:
		)
		(RoomFeats)
	)
	(method (doit)
		(super doit: &rest)
		(if (and (ego mover?)
					(== (curRoom curPic?) pHookerCloseup))
			(ego setMotion: 0)
			(sCloseup
				state:	6,
				cue:
			)
		)
		(cond
			(script)
			((SteppedOn ego cYELLOW)
				(HandsOff)
				(self setScript: sDownstairs)
			)
		)
	)
	(method (doVerb theVerb invItem)
		(if (!= pHookersRoom (curRoom curPic?))
			(switch theVerb
				(verbLook
					(Print "Until now, you were unsure of how a \"professional,\"
						would look.")
					(Print "Now you know.")
					(Print "(You were happier before!)" LOW)
				)
				(verbDo
					(Print "\"This is your favorite part, ain' it, Honey?\"")
				)
				(verbTalk
					(Print "\"Nice mandibles,\" you offer.")
					(Print "\"You don' think I'm saggy?\" she replies,
						no Rhodes scholar.")
				)
				(verbZipper
					(Print "She says, \"Sorry, Mister,
						but all you gets is `Straight Ahead' wit' me.\"")
				)
				(verbSmellTaste
					(Print "Sniffing the air, you're suddenly hungry for tuna.")
				)
				(else
					(super doVerb: theVerb invItem)
				)
			)
		else
			(switch theVerb
				(verbLook
					(Print "You are in the hooker's seedy bedroom.
						The room's a mess, ditto the hooker!")
				)
				(verbSmellTaste
					(Print "Sniffing the air, you're suddenly hungry for tuna.")
				)
				(verbUse
					(switch invItem
						(iRibbon
							(Print "Your ribbon is not long enough to reach outside.")
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
				(verbLook
					(cond
						((and naked
								(not halfNaked))
						 	(Print "\"Come on, my big stud-muffin!
								Come to Mama!\"")
						)
						(naked
							(Print "You're really proud of your man-like chest.")
						)
					)
				)
				(verbDo
					(cond
						((and naked
								(not halfNaked))
							(curRoom setScript: sGetDressed)
							(return TRUE)
						)
						((Btst fWearingRubber)
							(Bclr fWearingRubber)
							(ego put: iLubber 0)
							(Points pRemovedRubber ptsRemovedRubber)
							(Print "Good idea. You carefully remove your lubber and
								dispose of it in a way that will not be described here.")
						)
						((Btst fScoredHooker)
							(Print "Not again! A guy can only take so much!!")
						)
						(else
							(curRoom setScript: sGetUndressed)
							(return TRUE)
						)
					)
				)
				(verbZipper
					(cond
						((and naked
								(not halfNaked))
							(curRoom setScript: sScrew)
							(return TRUE)
						)
						((Btst fScoredHooker)
							(Print "Not again! A guy can only take so much!!")
						)
						(else
							(curRoom setScript: sGetUndressed)
							(return TRUE)
						)
					)
				)
				(verbUse
					(switch invItem
						(iLubber
							(cond
								((Btst fWearingRubber)
									(Print "You already are wearing your \"lubber.\"")
								)
								((Btst fScoredHooker)
									(Print "Now's a great time to think of that!
										A little late now, isn't it, Larry?")
								)
								(halfNaked
									(Print "With your pants still on?")
								)
								((not naked)
									(Print "With all your clothes on?")
								)
								(else
									(Print "Yep! This would be a perfect time!
										Your Surgeon General is proud!")
									(Bset fWearingRubber)
									((Inventory at: iLubber) state: 1)
									(Points pWornRubber ptsWornRubber)
									(return TRUE)
								)
							)
						)
					)
				)
			)
		)
	)
)

;
;  	Scripts
;

(instance sGetUndressed of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
			 	(HandsOff)
				(if halfNaked
					(++ state)
					(= cycles 1)
				else
					(ego setMotion: PolyPath bedX bedY self)
				)
			)
			(
				(ego
					egoSpeed:,
					setHeading: 	0 self
				)
			)
			(
				(ego
					view:				vLarryUndressing,
					setLoop:			(if halfNaked lRemovePants else lRemoveCoat),
					setCel:			0,
					cycleSpeed:		howFast,
					setCycle:		EndLoop self,
				)
			)
			(
				(HandsOn)
				(User canControl: FALSE)
				(theIconBar disable: walkIcon)
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
					cycleSpeed:	(* 2 howFast),
					setCycle: 	BegLoop self,
				)
			)
			(
				(ego
					setLoop:		lRemoveCoat,
					setCel:		LastCel,
					setCycle: 	BegLoop self,
				)
			)
			(
				(= cycles 60)
			)
			(
				(Print "Ahh. That feels better.")
				(HandsOn)
				(NormalEgo facingNorth)
				(self dispose:)
			)
		)
	)
)

(instance sFromStairs of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					posn:			141 111,
					setMotion:	MoveTo 124 106 self
				)
			)
			(
				(ego
					setMotion:	MoveTo 118 112 self
				)
			)
			(
				(ego setHeading: 180 self)
			)
			(
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sDownstairs of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					setMotion: MoveTo 145 111 self
				)
			)
			(
				(curRoom newRoom: 140)
			)
		)
	)
)

(instance sCloseup of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(eyesProp
					setScript:  sBlink
				)
				(herMouth
					setCycle:	Forward
				)
				(= seconds (Random 3 6))
			)
			(
				(herMouth setCycle: EndLoop)
				(= seconds (Random 2 4))
			)
			(
				(herMouth
					setCycle:	Forward
				)
				(= cycles (Random 3 6))
			)
			(
				(herMouth
					setCycle: EndLoop
				)
				(= seconds 2)
			)
			(
				(herMouth
					setCycle:	Forward
				)
				(= cycles (Random 3 6))
			)
			(
				(if (Btst fMouthSmellsBad)
					(Print "\"Jeez, Mister!\" cries the hooker,
						\"What've you been eating? Buffalo chips???\"")
				else
					(Print "She certainly seems to enjoy that gum.")
				)
				(= seconds 2)
			)
			(;this has to be state 6 for room doit check
				(herMouth
					setCycle:	EndLoop self
				)
			)
			(
				(HandsOff)
				(= cycles 1)
			)
			(
				(eyesProp
					z:				1000,
					setScript:	0,
					dispose:
				)
				(herMouth
					z:				1000,
					dispose:
				)
				(RoomFeats)
				(curRoom drawPic: pHookersRoom)
				(cast eachElementDo: #show)
				((ScriptID 0 25)
					loop: 	lWalkerIcon,
					cursor:	WALK_CURSOR,
				)
				(Animate (cast elements?) FALSE)	;so drawpic will finish
				(NormalEgo)
				(InitEgoHead)
				(= cycles 1)
			)
			(
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sBlink of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= cycles (Random 25 250))
			)
			(
				(eyesProp setCycle: BegLoop self)
			)
			(
				(self init:)
			)
		)
	)
)

(instance sScrew of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
			 	(HandsOff)
				(theMusic
					number:		mHooker,
					play:
				)
				(ego
					setLoop:		lJumpInBed,
					setCel:		0,
					setPri:		(+ (theHooker priority?) 1),
					setCycle:	MoveCycle @ screwPts self
				)
			)
			(
				(= oldSpeed speed)
				(= speed 0)						;**	was 1
				(theHooker
					z: 		1000,
					hide:
				)
				(ego
					z: 		1000,
					setPri:	RELEASE
				)
				(hookerHead dispose:)
				(censor
					init:			,
					setCycle:	Forward,
					viewer:		humpCycler,
				)
				(= seconds (Random 7 13))
			)
			(
				(censor
					setLoop:		lCensoredFlipped,
				)
				(= seconds (Random 4 7))
			)
			(
				(censor
					setLoop:		lCensored,
				)
				(= seconds (Random 7 13))
			)
			(
				(censor
					viewer:		FALSE,
					setCycle:	Forward,
					cycleSpeed:	0,
				)
				(= seconds 3)
			)
			(
				(censor
					setCycle:	EndLoop,		;**	"OOOh, I'm coming..."
					cycleSpeed:	3,
				)
				(= seconds 3)
			)
			(
				(censor
					setCycle:	CycleTo 4 1,	;**	"Ahhhh...."
					cycleSpeed:	3,
				)
				(= seconds 3)
			)
			(
				(= speed oldSpeed)
				(censor
					z:				1000,
					dispose:
				)
				(theHooker
					z:				hookerZ,
					show:			,
					setCel:		0,
					setLoop:		lSmoke,
				)
				(ego
					egoSpeed:,
					x:				bedX,
					y:				bedY,
					z:				0,
					cycleSpeed:	4,
					setCel:		LastCel,
					setCycle: 	BegLoop self
				)
			)
			(
				(theMusic
					number:		mChannel8,
					play:
				)
				(ego
					setLoop:		lRemovePants,
					setCel:		LastCel,
					setCycle:	BegLoop
				)
				(= seconds 3)
			)
			(
				(ego
					setLoop:		lRemoveCoat,
					setCel:		LastCel,
					setCycle:	BegLoop
				)
				(= seconds 3)
			)
			(
				(Bset fScoredHooker)
				(Points pScoredHooker ptsScoredHooker)
				(NormalEgo 3)
				(theHooker
					setScript:	sSmoke,
				)
				(= cycles 60)
			)
			(
				(Print "Although successful, you feel less than satisfied.
					Technically speaking, you're no longer a virgin,
					but for some reason, the thrill just wasn't there.
					You vow to continue your quest until you please your heart,
					not just your other organs!")
				(if (Btst fWearingRubber)
					(Print "It's a good thing you were wearing your raincoat!")
				else
					(Bset fHasClap)
				)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sExitWindow of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					egoSpeed:,
					x:				241,
					y:				124,
					view:			vLarryCensored,
					setLoop:		lExitWindow,
					setCel:		0,
					cycleSpeed:	larrySpeed,
					setCycle:	EndLoop self
				)
			)
			(
				(theMusic fade:)
				(ego userSpeed:)
				(curRoom newRoom: 160)
			)
		)
	)
)

(instance sFromWindow of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(fakeEgo
					cycleSpeed:	larrySpeed,
					init:			,
					setCycle:	BegLoop self
				)
			)
			(
				(fakeEgo
					z:			1000,
					dispose:
				)
				(ego
					init:,
					x:				rightX,
					y:				rightY,
					loop:			0,
					setHeading:	270,
				)
				(InitEgoHead)
				(= cycles 10)
			)
			(
				(NormalEgo 1)
 				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sGetCandy of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					egoSpeed:,
					view:			vLarryGetting,
					loop:			lHighRight,
					cel:			0,
					setCycle:	EndLoop self
				)
			)
			(
				(candy dispose:)
				(Points pGotCandy ptsGotCandy)
				(ego
					get: 			iCandy,
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

(instance sOpenWindow of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					egoSpeed:,
					normal:		FALSE,
					view:			vLarryCensored,
					setLoop:		lOpenWindow,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(theMusic3
					number:		sWindowUp,
					play:
				)
				(Bset fHookerWindowOpen)
				(aWindow setCycle: EndLoop)
				(ego setCycle:	BegLoop self)
			)
			(
				(NormalEgo 0)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sSmoke of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= seconds (Random 3 10))
			)
			(
				(client
					setCycle:	EndLoop self
				)
			)
			(
				(self init:)
			)
		)
	)
)

;
;	Props
;

(instance hookerHead of Prop
	(properties
		view			vHooker
		loop			lChewRight
		cel			0
		z				headZ
		priority		9
		signal		(| ignrAct fixPriOn)
		approachX	bedX
		approachY	bedY
	)
	(method (init)
		(self
			x: 			(theHooker x?),
			y: 			(theHooker y?),
			cycleSpeed:	6,
			setCycle: 	ForwardCounter 6 self
		)
		(self cue:)
		(super init:)
	)
	(method (cue)
		(super cue:)
		(if (or (== loop lChewFront)
			 	  (== loop lChewRight))
			(self
				setLoop: 	(+ (self loop?) 1),
				setCel:		0,
				setCycle:	EndLoop self
			)
		else
			(self
				setLoop:
					(if (> (self loop?) 3)
						lChewRight
					else
						lChewFront
					),
				setCycle: ForwardCounter (Random 5 15) self
			)
		)
	)
	(method (doit)
		(super doit: &rest)
		(if (and (< (ego x?) 126)
					(> (ego y?) 136))
			(if (> loop 3)
				(self setLoop: (- loop 3))
			)
		else
			(if (< loop 4)
				(self setLoop: (+ loop 3))
			)
		)
		(if (and (== cel 7)
					(!= (curRoom curPic?) pHookerCloseup)
					(not popping))
			(= popping TRUE)
			(gumSound
				number:		sGumPopping,
				play:
			)
		else
			(if
				(!= cel 7)
				(= popping FALSE)
			)
		)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbZipper
				(Print "She says, \"Sorry, Mister,
					but all you gets is `Straight Ahead' wit' me.\"")
			)
			(else
				(theHooker doVerb: theVerb invItem)
			)
		)
	)
)

(instance theHooker of Person
	(properties
		view			152
		loop			0
		cel			0
		x				69
		y				112
		z				6
		priority		9
		signal		(| fixPriOn ignrAct)
		description "the hooker"
		approachX	bedX
		approachY	bedY
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(cond
					((Btst fScoredHooker)
						(Print "She seems more interested in that cigarette
							than she was in you.")
					)
					(naked
						(Print "NOW!? You've come this far, Larry;
							now's the time to do more than just look!")
					)
					(else
						(ego setMotion: 0)
						(cast eachElementDo: #hide)
						(herMouth
							cycleSpeed:	larrySpeed,
							init:			,
							z:				0,
						)
						(eyesProp
							cycleSpeed:	(+ howFast 1),
							init:,
							z:				eyesZ,
						)
						(CloseupFeats)
						(curRoom
							drawPic: 	pHookerCloseup,
							setScript: 	sCloseup
						)
						((ScriptID 0 25)
							loop: 	lExitIcon,
							cursor:	EXIT_CURSOR,
						)
						(Animate (cast elements?) FALSE)	;so drawpic will finish
					)
				)
			)
			(verbDo
				(cond
					(halfNaked
						(Print "With your pants still on?")
					)
					((not naked)
						(Print "With all your clothes on?")
					)
					(else
						(curRoom setScript: sScrew)
					)
				)
			)
			(verbTalk
				(if (Btst fScoredHooker)
					(Print "\"Was it good for you, baby?\" you ask her.")
					(Print "\"Was what good?\" she replies.")
				else
					(Print "\"So,\" she snarls,
						\"you just gonna stand there, or what?\"")
				)
			)
			(verbSmellTaste
				(Print "From here, her perfume smells cheap and overpowering.")
				(Print "(Also from the next county, you suppose!)" LOW)
			)
			(verbZipper
				(cond
					(halfNaked
						(Print "With your pants still on?")
					)
					((not naked)
						(Print "With all your clothes on?")
					)
					(else
						(curRoom setScript: sScrew)
					)
				)
			)
			(verbUse
				(switch invItem
					(iWallet
						(Print "\"I don' need dat, Honey.
							Didn't ju pay downstairs?\"")
					)
					(iWatch
						(Print "\"Dat's right, Honey,\" she gums,
							\"yore time's runnin' out.\"")
					)
					(iRing
						(Print "\"No commitments, Honey.
							This is a biz'nus deal!\"")
					)
					(iRemoteControl
						(Print "\"You jes' gonna stand there and play with your
							li'l remote?\"")
					)
					(iLubber
						(Print "\"Yeah, them's a good idea.
							Lemme see that'n.
							Well, how 'bout dat!
							I didn't know they came in `Extra Small!'\"")
					)
					(iCandy
						(Print "\"Naw, Nuggie, none for me.
							But I jes' had me a john that gimme
							a box a' candy jus' like that'n.\"")
					)
					(iDiscoPass
						(Print "\"Why waste yore time at the disco?
							We kin do the horizontal bop right here!\"")
					)
					(iWine
						(Print "\"Honey, dinner an' drinks ain' necessary here!\"")
					)
					(iMagazine
						(Print "\"I've seen better!\"")
					)
					(iPills
						(Print "\"I hardly need extrinsic motivation!\"")
					)
					(iRibbon
						(Print "\"No S. and M. here, buddy!\"")
					)
					(iPocketKnife
						(Print "\"Honey, you don' need no knife.
							You can assault me with YORE deadly weapon!\"")
					)
					(iGraffiti
						(Print "\"He did? Ain't he the sweetest?
							He's always sendin' me bizness!\"")
					)
					(else
						(Print "\"Say, Honey-chil',
							whacha come up here for--bartering?\"")
					)
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance candy of View
	(properties
		view			150
		loop			1
		cel			0
		x				239
		y				124
		priority		pLBLUE
		signal		fixPriOn
		description "the box of candy"
		approachX	rightX
		approachY	rightY
	)
)

(instance aWindow of Prop
	(properties
		view			150
		loop			0
		cel			1
		x				250
		y				110
		z				20
		description "the window"
		lookStr		"There is a fire escape outside the window."
		approachX	rightX
		approachY	rightY
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(cond
					(naked
						(Print "NOW!?")
					)
					((Btst fHookerWindowOpen)
						(HandsOff)
						(curRoom setScript: sExitWindow)
					)
					(else
						(HandsOff)
						(curRoom setScript: sOpenWindow)
					)
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance eyesProp of Prop
	(properties
		view			vHookerCloseup
		loop			lEyes
		cel			0
		x				147
		y				75
		z				eyesZ
		description "her eyes"
		signal		(| ignrAct ignrHrz)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "What smoldering eyes!")
			)
			(else
				(herFace doVerb: theVerb invItem)
			)
		)
	)
)

(instance herMouth of Prop
	(properties
		view			vHookerCloseup
		loop			lMouth
		cel			cMouthHang
		x				146
		y				83
		description "her mouth"
		signal		(| ignrAct ignrHrz)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "Look out! It's the \"Jaws of Life!\"")
			)
			(else
				(herFace doVerb: theVerb invItem)
			)
		)
	)
)

(instance censor of Actor
	(properties
		view			vLarryCensored
		loop			lCensored
		x				89
		y				115
		priority		9
		signal		(| fixPriOn ignrAct)
	)
)

(instance fakeEgo of Prop
	(properties
		x				241
		y				124
		view			vLarryCensored
		loop			lExitWindow
		cel			cOutWindow
	)
)

;
;	Features
;

(instance table of Feature
	(properties
		x 239
		y 126
		z 0
		heading 0
		nsLeft 229
		nsTop 116
		nsBottom 136
		nsRight 249
 		description "the table"
		sightAngle 40
		approachX	rightX
		approachY	rightY
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if (not (ObjInRoom iCandy))
					(Print "It's a really ugly table.")
				else
					(Print "There is a box of candy on the hooker's table.
						Probably a gift from some admirer, you suppose.")
				)
			)
			(verbDo
				(cond
					((not (ObjInRoom iCandy))
						(Print "You can't take her table.
							She's nailed it to the floor.")
					)
					(naked
						(Print "NOW!?")
					)
					(else
						(HandsOff)
						(curRoom setScript: sGetCandy)
					)
				)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance light of Feature
	(properties
		x 176
		y 131
		z 68
		heading 0
		nsLeft 160
		nsTop 55
		nsBottom 72
		nsRight 192
 		description "the light"
		lookStr 		"The lamp casts a yellow pallor over the hooker's parlor."
		sightAngle 40
	)
)
(instance underwear of Feature
	(properties
		x 207
		y 160
		z 48
		heading 0
		nsLeft 195
		nsTop 98
		nsBottom 127
		nsRight 220
 		description "her underwear"
		sightAngle	40
		lookStr	  	"The clothesline is barely strong enough to hold its
						present load.
						Poor girl, you'd think at these prices she could
					  	afford a laundromat."
		approachX	underwearX
		approachY	underwearY
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Although you would look precious in that little white
					lace number, you're just not that kind of guy!")
			)
			(verbSmellTaste
				(Print "The hooker's underwear are not of the edible variety.")
			)
			(verbUse
				(switch invItem
					(iPocketKnife
						(clothesline doVerb: verbDo)
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
(instance bed of Feature
	(properties
		x 97
		y 110
		z 0
		heading 0
		nsLeft 57
		nsTop 106
		nsBottom 137
		nsRight 137
 		description "the bed"
		sightAngle 	40
		approachX	bedX
		approachY	bedY
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbSmellTaste
				(Print "The bed smells of stale cigarette smoke.")
			)
			(else
				(theHooker doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance clothesline of Feature
	(properties
		x 				207
		y 				155
		z 				53
;		nsLeft 		179
;		nsTop 		73
;		nsBottom 	108
;		nsRight 		256
		description	"the clothesline"
		onMeCheck	cLRED
		sightAngle 	40
		approachX	underwearX
		approachY	underwearY
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "The clothesline is barely strong enough to hold its
					present light load.
					Besides, if you take it, her underwear would fall down.")
				(Print "(And you know how em-bare-assing THAT would be!)" LOW)
			)
			(verbUse
				(switch invItem
					(iPocketKnife
						(Print "Although the pocket knife is sharp enough,
							you have no need to hassle this poor girl.")
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

(instance theSign of Feature
	(properties
		x 40
		y 79
		z 0
		heading 0
		nsLeft 27
		nsTop 68
		nsBottom 91
		nsRight 53
 		description "the small sign over the hooker's bed"
		lookStr 		"The sign over the hooker's bed reads,
						\"Substantial penalty for early withdrawal!\""
		sightAngle 	40
		approachX 40
		approachY 79
	)
)

(instance windowsill of Feature
	(properties
		x 157
		y 170
		z 0
		heading 0
		nsLeft 11
		nsTop 152
		nsBottom 189
		nsRight 303
 		description "the window sill"
		lookStr 		"Someone has yet to kick the habit!"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You have no use for cigarette butts.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

;features for closeup

(instance herBreast of Feature
	(properties
		x 156
		y 158
		z 0
		heading 0
		nsLeft 105
		nsTop 139
		nsBottom 177
		nsRight 207
 		description "her breast"
		lookStr 		"\"Is that all you came up here to do, Honey? LOOK!?\""
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbZipper
				(Print "Yeah, Larry! You should be so lucky!")
			)
			(verbSmellTaste
				(Print "To you, they taste slightly salty.")
			)
			(verbDo
				(Print "\"Is that all you came up those stairs for, Honey?
					To cop a little feel?\"")
			)
			(verbTalk
				(Print "\"Mmmmmbbbblllemmm,\" you mumble,
					imagining your face buried in that valley.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance herFace of Feature
	(properties
		x 146
		y 74
		z 0
		heading 0
		nsLeft 122
		nsTop 49
		nsBottom 99
		nsRight 171
 		description "her face"
		lookStr 		"\"You think I'm pretty, doncha Honey?\"
						gums the hooker."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
         (verbDo
				(Print "\"Is that all you want to do, Honey?\" she asks.")
			)
			(verbTalk
				(Print "\"Nice mandibles,\" you offer.")
				(Print "\"You don' think I'm saggy?\" she replies,
					no Rhodes scholar.")
			)
			(verbZipper
				(Print "\"Hey, baby,\" she groans, \"none o' dat.
					`Straight ahead' is all you get!\"")
			)
			(verbSmellTaste
				(Print "You recognize that cologne:
					\"Eau de Toilette Cie't!\"")
			)
			(verbUse
				(theHooker doVerb: theVerb invItem &rest)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance herCrack of Feature
	(properties
		x 65
		y 20
		z 0
		heading 0
		nsLeft 29
		nsTop 0
		nsBottom 41
		nsRight 102
 		description "the crack"
		lookStr 		"Is that why you came up here? To look at a crack?"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Get your hand out of the crack!")
			)
			(verbTalk
				(Print "You can't imagine putting your tongue in THAT crack!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance bedpost of Feature
	(properties
		x 301
		y 82
		z 0
		heading 0
		nsLeft 283
		nsTop 0
		nsBottom 165
		nsRight 319
 		description "the bedpost"
		lookStr 		"That's where she leaves her chewing gum overnight."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "No, there's no chewing gum
					losing its flavor on THAT bedpost.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(procedure (RoomFeats)
	(table
		approachVerbs:	objectVerbs verbLook,
		init:
	)
	(light init:)
	(underwear
		approachVerbs:	objectVerbs,
		init:
	)
	(bed
		approachVerbs:	objectVerbs verbLook,
		init:
	)
	(clothesline
		init:,
		approachVerbs:	objectVerbs,
	)
	(theSign init:)
	(windowsill init:)
	(herBreast dispose:)
	(herFace dispose:)
	(herCrack dispose:)
	(bedpost dispose:)
)

(procedure (CloseupFeats)
	(herBreast init:)
	(herFace init:)
	(herCrack init:)
	(bedpost init:)

	(table dispose:)
	(light dispose:)
	(underwear dispose:)
	(bed dispose:)
	(clothesline dispose:)
	(theSign dispose:)
	(windowsill dispose:)
)

(instance gumSound of Sound
	(properties
		flags			1
		prevSignal	-1
	)
)

(instance humpCycler of Code
	(method (doit)
		(if (not (Random 0 30))
			(censor cycleSpeed: (Random 0 5))
		)
	)
)

