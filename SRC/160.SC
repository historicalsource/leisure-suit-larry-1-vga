;**
;**	Logics for Room 160 -- Trash Bin Alley
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford &
;**
;**	Last Update:  June 20, 1991
;**

(script# 160)

(public
	rm160		0
)

(local
	lookedInTrash
	clickX
	clickY

	FALLPATH = [
	4 0 129  72
	4 1 126 116
	4 2 127 135
	PATHEND]

	DIE_PATH = [
		0 0 197 66
		0 1 201 68
		0 2 206 74
		0 3 194 77
		0 4 192 75
		0 5 196 70
		1 0 223 67
		1 1 217 71
		1 2 224 71
	PATHEND]
)

(procedure
	FallAndDie
)

(define binX 			117)
(define binY 			171)
(define binZ			34)
(define onEscape  	(< (ego y?) 100))
(define tiePosn		185 61)
(define tieX			185)
(define tieY			61)
(define jumpRailPosn	198 64)
(define reachOutPosn	226 60)
(define smashPosn		230 63)
(define swingPosn		230 63)
(define climbPosn		197 63)
(define HWindowX		162)
(define HWindowY		62)
(define inBin (== (ego view?) vLarryInAlley))
(define ribbonOn  (and 	(== (ego view?) vLarryGettingBottle)
							 	(== (ego loop?) lTiesToSelf)))
(define onRailing	(and	(== (ego view?) vLarryGettingBottle)
								(== (ego loop?) lTiesToRail)))
(define hangingOn	(and	(== (ego view?) vLarryGettingBottle)
								(> (ego loop?) lTiesToRail)))
(define FALLY 66)

(define	sFalling					160)
(define	sLandInTrash			161)
(define	sOutOfTrash				163)
(define	sSplatOnPavement		164)
(define	mTrapeze					165)
;(define	sGlassBreaking			167)
;(define	sGroundOpens			191)

(define	pTrashbinAlley			160)
(define	vTrashbinAlley			160)
	(define lPillBottle			0)
	(define lHotelSign			1)
	(define lWindowOpen			2)
	(define lWindowSmashed		3)
		(define cBrokenWindow	4)

(define	vLarryInAlley			161)
	(define lJumpIn			0)
		(define cJumpOut			3)
	(define lGetHammer			1)
		(define cInBin			0)
		(define cGone			2)

(define	vLarryGettingBottle	162)
	(define lTiesToSelf			0)
	(define lTiesToRail			1)
	(define lJumpsRail			2)
	(define lLeansBack			3)
	(define lUsesHammer			4)
		(define cHangOut			0)
	(define lSwingsBack			5)
	(define lClimbsBack			6)
	(define lWiggle				7)

(define vLarryFalls				164)
	(define lClimbOver			0)
	(define lLeanout				1)
	(define lLoseGrip				2)
	(define lBounce				3)
	(define lLarryFallLeft		4)

;	(define vLarryUnderGround 	812)
;		(define lLarryGoesUnder	1)

(instance rm160 of LLRoom
	(properties
		picture	160
		west		100
	)
	(method (init)
		(LoadMany SOUND
			sFalling
			sLandInTrash
			sOutOfTrash
			sSplatOnPavement
			mTrapeze
		)
		(LoadMany VIEW
			vTrashbinAlley
			vLarryInAlley
			vLarryGettingBottle
			vLarryFalls
			vLarryUnderGround
		)
		(theMusic3
			loop:			1,
			vol:			127,
			flags:		1,
		)
;		(curRoom addObstacle:
;			((Polygon new:)
;				type: PBarredAccess,
;				init:	62 171 58 162 41 162 30 136 43 81 319 81 319 189 0 189 0 186
;						254 186 225 160 191 160 197 171,
;				yourself
;			)
;		)
		(if
			(!= prevRoomNum 150)
			(curRoom addObstacle:
				((Polygon new:)
					type: PBarredAccess,
					init:	62 171 58 162 41 162 30 136 58 0 319 0 319 189 0 189 0 186
							254 186 225 160 191 160 197 171,
					yourself
				)
			)
		)
		(ego
			init:			,
			actions: 	egoActions,
		)
		(if debugging
			(ego
				get:			iRibbon,
				get:			iHammer,
			)
		)
		(eHead actions: egoActions)
		(switch prevRoomNum
			(100
				(ego
					y:			167,
				)
			)
			(110
				(HandsOff)
				(ego
					hide:			,
					normal:		FALSE,
				)
				(self
					style:		IRISOUT,
					setScript: 	sComeTo,
				)
			)
			(150
				(NormalEgo 2)
				(ego
					x:					HWindowX,
					y:					HWindowY,
					illegalBits:	$8000,
				)
			)
			(else 	;TP
				(ego
					x:				160,
					y:				160,
				)
			)
		)
		(super init:)

		(if 
			(or
				(!= (theMusic2 number?) sCitySounds)
				(and
					(== (theMusic2 number?) sCitySounds)
					(or
						(== (theMusic2 signal?) -1)
						(< (theMusic2 vol?) 127)
					)
				)
			)
			(theMusic2
				number:		sCitySounds,
				loop:			-1,
				flags:		1,
				play:			127,
			)
		)
		;init props
		(if (and	(Btst fAlleyWindowBroken)
					(ObjInRoom iPills))
			(bottle init:)
		else
			(if
				(and
					(ego has: iRibbon)
					(ego has: iHammer)
				)
				(Load SOUND sGlassBreaking)
			)
		)
		(eastWindow
			setCel:	(if (Btst fAlleyWindowBroken) cBrokenWindow),
			init:		,
		)
		(hotelSign
			cycleSpeed:	30,
			init: 		,
			setCycle:	Forward
		)
		(if
			(Btst fHookerWindowOpen)
			(hookerWindow init:)
		)

		;init features
		(binF
			init:				,
		)
		(balconyF init:)
		(hookerWindowF init:)
		(hotelSignF init:)
		(eastWindowF init:)
		(fenceF
			init:				,
		)
		(if (!= prevRoomNum 150)
			(fenceF approachVerbs: objectVerbs)
			(binF approachVerbs:	objectVerbs)
		)

		(lidF init:)
	)
	(method (doit)
		(super doit: &rest)
 		(cond
			(script)
			((SteppedOn ego cBLUE)
				(curRoom newRoom: 100)
			)
			((SteppedOn ego cGREEN)
				(HandsOff)
				(curRoom setScript: sFall)
			)
			((ego mover?)
				(ego setScript: 0)
				(cond
					(inBin
						(HandsOff)
						(binF approachVerbs: objectVerbs)
						(curRoom setScript: sClimbOut)
					)
					(hangingOn
						(HandsOff)
						(curRoom setScript: sClimbBack)
					)
					(onRailing
						(HandsOff)
						(curRoom setScript: sUntieRailing)
					)
					(ribbonOn
						(HandsOff)
						(curRoom setScript: sUnTie)
					)
				)
			)
		)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if (== ((inventory at: iPills) owner?) 160)
					(Print "It's a nice little alley, with a trash bin,
						two windows, a fire escape, a fence, and whoa, look there!
						There's something sitting in that window to the right!")
				else
					(Print "It's a nice little alley, with a trash bin,
						two windows, a fire escape and a fence.")
 				)
			)
			(verbUse
				(switch invItem
					(iRibbon
						(Print "It's not long enough to reach anywhere,
							but you might want to use it for safety purposes.")
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

;
;  	Ego actions
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
						(iRibbon
							(if (== (ego view?) vLarryGettingBottle)
								(Print "You're already all tied up!")
							else
								(if onEscape
									(HandsOff)
									(curRoom setScript: sTie)
								)
							)
						)
						(iPocketKnife
							(cond
								(hangingOn
									(FallAndDie)
								)
								(onRailing
									(HandsOff)
									(curRoom setScript: sUntieRailing)
									(return TRUE)
								)
								(ribbonOn
									(HandsOff)
									(curRoom setScript: sUnTie)
									(return TRUE)
								)
							)
						)
					)
				)
				(verbDo
					(if ribbonOn
						(HandsOff)
						(curRoom setScript: sUnTie)
						(return TRUE)
					)
				)
			)
		)
	)
)

;
; 	Scripts
;

(instance sFall of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					egoSpeed:		(+ larrySpeed howFast),
					normal:			FALSE,
					view:				vLarryFalls,
					setLoop:			lLarryFallLeft,
					setCel:			0,
					setCycle:		MoveCycle @FALLPATH self,
					illegalBits:	$0000,
					setPri:			(ego priority?),
				)
				(theMusic3
					number:			sFalling,
					setVol:			127,
					play:,
				)
			)
			(
				(ego
					hide:,
					setPri:			RELEASE,
				)
				(theMusic3
					number:			sLandInTrash,
					play:,
				)
				(curRoom
					setScript: 		sComeTo,
					addObstacle:
					((Polygon new:)
						type: PBarredAccess,
						init:	62 171 58 162 41 162 30 136 58 0 319 0 319 189 0 189 0 186
								254 186 225 160 191 160 197 171,
						yourself
					)
				)
			)
		)
	)
)

(instance sToHooker of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					illegalBits:	$0000,
					setMotion: 		PolyPath HWindowX HWindowY self
				)
			)
			(
				(ego setHeading: 0 self)
			)
			(
				(theMusic2 fade:)
				(curRoom newRoom: 150)
			)
		)
	)
)

(instance sUntieRailing of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					setCycle: BegLoop self
				)
			)
			(
				(ego
					setLoop:	lTiesToSelf,
					setCel:	255,
				)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)


(instance sUnTie of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego setCycle:	BegLoop self)
			)
			(
				(ego
					view:				vLarryWalking,
					setLoop:			RELEASE,
					setCycle:		Walk,
					setMotion: 		PolyPath 174 67 self,
				)
			)
			(
				(theMusic fade:)
				(NormalEgo 2)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sClimbOut of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= clickX
					((User curEvent?) x?)
				)
				(= clickY
					((User curEvent) y?)
				)
				(ego
					view:			vLarryInAlley,
					setLoop:		lGetHammer,
					setCel:		1,
					hide:			,
				)
				(= cycles 1)
			)
			(
				(ego
					egoSpeed:	(* 2 larrySpeed),
					show:			,
					setCycle:	BegLoop self
				)
				(theMusic3
					number:		sLandInTrash,
					play:,
				)
			)
			(
				(ego
					view:			vLarryInAlley,
					z:				0,
					setLoop:		lJumpIn,
					setCel:		cJumpOut,
					setCycle:	BegLoop self
				)
			)
			(
				(theMusic3
					number:		sOutOfTrash,
					play:
				)
				(binF approachVerbs: objectVerbs)
				(ego normal: TRUE)
				(NormalEgo 3)
				(HandsOn)
				(if (and (IsObject (CueObj client?))
							((CueObj client?) approachX?)
							(!= (CueObj client?) binF))
						(ego setMotion: PolyPath
						((CueObj client?) approachX?)
						(+ (ego z?) ((CueObj client?) approachY))
						CueObj)
				else
					(ego setMotion: PolyPath clickX clickY)
				)
				(self dispose:)
			)
		)
	)
)

(instance sComeTo of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= seconds 3)
			)
			(
				(Print "Hey! Where are you?")
				(= seconds 3)
			)
			(
				(Print "After a careful analysis of your surroundings,
					you conclude you are at the bottom of a pile of garbage!")
				(= seconds 3)
			)
			(
				(ego
					show:			,
					view:			vLarryInAlley,
					loop:			lGetHammer,
					cel:			cGone,
					x:				binX,
					y:				binY,
					z:				binZ,
					yStep:		2,
					cycleSpeed:	(+ larrySpeed 4),
					setCycle:	BegLoop self,
					setPri:		RELEASE,
				)
			)
			(
				(Print "\"YUCK!!\"")
				(binF approachVerbs: verbNone)
				(fenceF approachVerbs: objectVerbs)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sJumpIn of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(Print "Willing to do anything to further tonight's goal,
					you dive into the dirty dumpster.")
				(binF approachVerbs:	verbNone)
				(= cycles 2)
			)
			(
				(ego
					view:			vLarryInAlley,
					setLoop:		lJumpIn,
					setCel:		0,
					cycleSpeed:	(* larrySpeed 2),
					setCycle:	CycleTo 2 1 self
				)
			)
			(
				(theMusic3
					number:		sLandInTrash,
					play:
				)
				(ego setCycle: EndLoop self)
			)
			(
				(ego
					z:				binZ,
					setLoop:		lGetHammer,
					setCel:		cInBin,
				)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sGetHammer of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					egoSpeed:,
					setCel:		cInBin,
					setLoop:		lGetHammer,
					setCycle:	EndLoop self

				)
			)
			(
				(ego get: iHammer)
				(Points pGotHammer ptsGotHammer)
				(= cycles 10)
			)
			(
				(Print "Digging past innumerable limp celery stalks from this
					morning's bloody marys, you find Lefty's hammer,
					wipe it off as much as you can, and stuff it into your
					pants pocket.")
				(ego
					loop:			lGetHammer,
					cel:			cInBin,
				)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sTie of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					illegalBits:	0,
					setMotion:		PolyPath tiePosn self
				)
			)
			(
				(Print "You carefully tie the ribbon from your \"nuptial bed\"
					around your waist.")
				(ego
					view:			vLarryGettingBottle,
					setLoop:		lTiesToSelf,
					setCel:		0,
					cycleSpeed:	(+ larrySpeed 6),
					setCycle:	EndLoop self
				)
			)
			(
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sTieToRail of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					egoSpeed:,
					setLoop:		lTiesToRail,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(Print "You carefully tie the ribbon from your \"nuptial bed\"
					around the railing.")
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sJumpRail of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(theMusic
					number:		mTrapeze,
					loop:			-1,
					vol:			127,
					flags:		1,
					play:,
				)
				(ego
					egoSpeed:,
					setLoop:		lJumpsRail,
					setCel:		0,
					posn:			jumpRailPosn,
					setCycle:	EndLoop self,
				)
			)
			(
				(ego
					setLoop:		lLeansBack,
					setCel:		0,
					posn:			reachOutPosn,
					setCycle:	EndLoop self,
				)
			)
			(
				(Print "With the coy pink ribbon rather insecurely holding you
					to the railing, you can reach all the way over to the window.")
				(if (ObjInRoom iPills)
					(Print "NOW, of all times, you notice the window's locked!")
				)
				(ego setScript: sWiggle)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sClimbBack of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					egoSpeed:,
					setScript:	0,
					posn:			swingPosn,
					setLoop:		lSwingsBack,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(ego
					posn:			climbPosn,
					setLoop:		lClimbsBack,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(ego
					x:				tieX,
					y:				tieY,
;					posn:			tiePosn,
					setLoop:		lTiesToRail,
					setCel:		255,
				)
				(theMusic fade:)
				(= cycles 1)
			)
			(
				(Print "Whew! You're glad to be back to the relative safety
					of the fire escape!")
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sSmashWindow of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(Print "Subtly, you smash the window with your left-handed hammer
					in your right hand.
					Your fears were unfounded; it seems to work either way.
					Your future as a second-story man now seems quite plausible!")
				(ego
					egoSpeed:,
					setScript:	0,
					setLoop:		lUsesHammer,
					setCel:		0,
					setCycle: 	EndLoop self
				)
			)
			(
				(theMusic3
					number:		sGlassBreaking,
					play:,
				)
				(eastWindow
					setCycle:	EndLoop eastWindow
				)
				(ego
					setCycle:	BegLoop self
				)
			)
			(
				(ego
					setLoop:		lLeansBack,
					setCel:		255,
				)
				(= cycles 1)
			)
			(
				(ego setScript: sWiggle)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

;Al, this script is used when you click knife on ego too, so don't touch
;unless you want to fix and TEST that too

(instance sFallDie of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					illegalBits:	$0000,
					setMotion: 		MoveTo 187 FALLY self
				)
			)
			(
				(ego
					normal:			FALSE,
					view:				vLarryFalls,
					setLoop:			lClimbOver,
					setCel:			0,
					egoSpeed:		(+ 1 larrySpeed),
					setCycle:		MoveCycle @DIE_PATH self
				)
			)
			(
				(Print "You lean far out over the railing before remembering that
					agility is yet another of your short suits!")
				(= cycles 1)
			)
			(;3
				(theMusic
					loop:				1,
					setVol:			127,
					number:			sFalling,
					play:,
				)
				(ego
					setLoop:			lLoseGrip,
					setCel:			0,
					x:					222,
					setStep:			3 6,
					setCycle:		EndLoop self,					;**  note double cue
					setMotion:		MoveTo (ego x?) 145 self,	;**
				)
			)
			(
			;**	end of loop
			)
			(
				(theMusic
					number:		sSplatOnPavement,
					play:,
				)
				(ego
					setLoop:		lBounce,
					setCel:		0,
					cycleSpeed:	larrySpeed,
					x:				219,
					y:				167,
					setCycle:	EndLoop self
				)
			)
			(
				(= seconds 3)
			)
			(
				(Print "\"Hey, guys!! He's screwed up again!
					Dump this body down the tubes, and get him another one!\""
				)
				(= seconds 3)
			)
			(
				(theMusic2
					number:	sElevator,
					setLoop:	-1,
					flags:	1,
					play:
				)
				(theMusic3
					number:		sGroundOpens,
					setLoop:		1,
					setVol:		127,
					flags:		1,
					play:,
				)
				(ego
					view:			vLarryUnderGround,
					setLoop:		lLarryGoesUnder,
					setCel:		0,
					cycleSpeed:	(* larrySpeed 2),
					setCycle:	EndLoop self
				)
			)
			(
				(curRoom newRoom: 190)
			)
		)
	)
)

(instance sGetPills of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego get: iPills)
				(Points pGotPills ptsGotPills)
				(= cycles 1)
			)
			(
				(Print "You got 'em! But what in the heck are they?")
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sWiggle of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= seconds (Random 2 4))
			)
			(
				(ego
					egoSpeed:,
					setLoop:		lWiggle,
					setCycle:	Forward,
				)
				(= seconds (Random 1 2))
			)
			(
				(ego
					setLoop:		lLeansBack,
					setCel:		255,
					setCycle:	0,
				)
				(self init:)
			)
		)
	)
)

;
;	Props
;

(instance eastWindow of Prop
	(properties
		view			vTrashbinAlley
		loop			3
		cel			0
		x				259
		y				39
 		description "the east window"
	)
	(method (cue)
		(super cue:)
		(bottle init:)
	)
	(method (doVerb theVerb invItem)
		(eastWindowF doVerb: theVerb invItem)
	)
)

(instance hotelSign of Prop
	(properties
		view			vTrashbinAlley
		loop			1
		cel			0
		x				15
		y				100
		z				100
		priority		10
		signal		fixPriOn
		sightAngle	40
		description "the hotel sign"
	)
	(method (cue)
		(super cue:)
		(Print "There is an old neon sign hanging on the wall that says
			\"Hotel\"...")
		(Print "...approximately.")
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				turnEgo
			)
			(verbDo
				(Print "That's all you need: electricity coursing through
					your hand!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance bottle of View
	(properties
		view			vTrashbinAlley
		loop			lPillBottle
		priority		12
		signal		fixPriOn
	)
	(method (init)
		(super init:)
		(self x: (eastWindow x?))
		(self y: (eastWindow y?))
	)
)

(instance hookerWindow of View
	;make sure y stays smaller than hookerWindowF
	(properties
		view				vTrashbinAlley
		loop				lWindowOpen
		x			 		174
		y					23
		signal			ignrAct
 		description 	"the west window"
		sightAngle		40
	)
	(method (doVerb theVerb invItem)
		(hookerWindowF doVerb: theVerb invItem)
	)
)

;
; 	Features
;

(instance binF of Feature
	(properties
		x 122
		y 149
		z 0
		heading 0
		nsLeft 74
		nsTop 131
		nsBottom 168
		nsRight 170
 		description "the dumpster"
		sightAngle 40
		approachX	binX
		approachY	binY
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if inBin
					(if (not (ego has: iHammer))
						(= lookedInTrash TRUE)
						(Print "Amidst assorted, odoriferous refuse from Lefty's
							clientele, you discover Lefty's old, left-handed hammer
							lying in the bottom of the dumpster, covered with trash.")
					else
						(Print "There's nothing else here worth describing,
							let alone taking.")
					)
				else
					(Print "Unfortunately, you can't see much from out here.")
				)
			)
			(verbDo
				(cond
					(onEscape
						(Print "You reconsider after noticing the trash bin is
							behind where you would fall!")
					)
					((not inBin)
						(HandsOff)
						(curRoom setScript: sJumpIn)
					)
					((ego has: iHammer)
						(Print "There's nothing else here worth describing,
							let alone taking.")
					)
					((not lookedInTrash)
						(= lookedInTrash TRUE)
						(Print "Amidst assorted, odoriferous refuse from Lefty's
							clientele, you discover Lefty's old, left-handed hammer
							lying in the bottom of the dumpster, covered with trash.")
					)
					(else
						(HandsOff)
						(curRoom setScript: sGetHammer)
					)
				)
			)
			(verbUse
				(if onEscape
					(Print "You'd never hit the dumpster from here.
						You can't even hit the barn side of a broad!")
				else
					(switch invItem
						(iHammer
							(Print "That's not the sort of banging you were hoping
								for tonight!")
						)
						(iBreathSpray
							(Print "Now the dumpster smells like garbage in the
								North Woods!")
						)
						(iWallet
							(Print "As much as you love money,
								you can't bear the thought of throwing it away!")
						)
						(iLubber
							(ego put: invItem 0)
							(if (Btst fWearingRubber)
								(Bclr fWearingRubber)
								(Points pRemovedRubber ptsRemovedRubber)
								(Print "Good idea,
									you properly dispose of your \"lubber.\"
									You're mother would be proud.")
							else
								(Print "\"I'll probably never use one of these
									things anyway,\" you shout, tossing it into the
									dumpster.")
								(ego put: invItem 0)
							)
						)
						(else
							(if
								(IsObject (Inventory at: invItem))
								(ego put: invItem 0)
								(Printf "Believing %s will be of no further use,
									you toss it into the dumpster, never to be seen again!"
									((Inventory at: invItem) description?)
								)
							else
								(Print "You cleverly take nothing, and throw nothing away.")
							)
						)
					)
				)
			)
			(verbSmellTaste
				(Print "\"YUCK!!\"")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance balconyF of Feature
	(properties
		x 147
		y 60	;170
		y 77
		z 17
;		z 107
		heading 0
		nsLeft 94
		nsTop 46
		nsBottom 80
		nsRight 201
 		description "the fire escape"
		sightAngle 40
		lookStr
			"A rickety fire escape, complete with safety ladder,
			is loosely attached to the building.
			A mild yellow glow comes from the window behind it."
	)
	(method (doVerb theVerb invItem)
		(if
			(and
				onEscape
				(== theVerb verbLook)
			)
			(Print "You are on a fire escape.
				A rusty ladder is at one end.
				To the right of the fire escape another window waits,
				just barely out of your reach.")
		else
			(switch theVerb
				(verbDo
					(cond
						(hangingOn
							(HandsOff)
							(ego setScript: 0)
							(curRoom setScript: sClimbBack)
						)
						(onRailing
							(HandsOff)
							(curRoom setScript: sUntieRailing)
						)
						(onEscape
							(Print "Try moving to the west end of the fire escape.")
						)
						(else
							(Print "There's no way to reach the ladder from down here.
								It's only accessible from above.")
						)
					)
				)
				(verbUse
					(switch invItem
						(iRibbon
							(cond
								(onRailing
									(Print "You already ARE tied to the railing.")
								)
								(ribbonOn
									(HandsOff)
									(curRoom setScript: sTieToRail)
								)
								(hangingOn
									(Print "You'll be better off if you
										leave the ribbon exactly where it is.")
								)
								(onEscape
									(Print "Good idea. But first,
										tie the ribbon to yourself.")
								)
								(else
									(Print "It's not long enough to reach from here.")
									(Print "(Of course, you've heard THAT line before!)"
										LOW)
								)
							)
						)
						(iPocketKnife
							(cond
								(ribbonOn
									(HandsOff)
									(curRoom setScript: sUnTie)
								)
								(onRailing
									(HandsOff)
									(curRoom setScript: sUntieRailing)
								)
								(hangingOn
									(FallAndDie)
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
				(else
					(super doVerb: theVerb invItem &rest)
				)
			)
		)
	)
)
(instance hookerWindowF of Feature
	(properties
		x 				162
		y 				50							;158
;		z 				136
		heading 		0
		nsLeft 		142
		nsTop 		1
		nsBottom 	43
		nsRight 		183
 		description "the west window"
		sightAngle 	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if onEscape
					(Print "Looking inside the left window,
						you see the room you just left.")
				else
					(Print "There are two windows high above you.
						The left window near the fire escape has a dim yellow
						glow; the other window is dark.
						There may be something on the sill of the dark window,
						but you can't quite make it out from way down here.")
				)
			)
			(verbDo
				(if onEscape
					(if (or onRailing hangingOn ribbonOn)
						(Print "You're currently all tied up!")
					else
						(HandsOff)
						(curRoom setScript: sToHooker)
					)
				else
					(Print "There's just no way to reach the windows from here.
						They're only accessible from above.")
				)
			)
			(verbUse
				(switch invItem
					(iHammer
						(if onEscape
							(Print "The window is already open.
								There's no use in breaking it!")
						else
							(Print "There's no use throwing the hammer up there.")
							(Print "(Besides, you probably just miss the window
								any way!)" LOW)
						)
					)
					(else
						(super doVerb: theVerb invItem)
					)
				)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance hotelSignF of Feature
	(properties
		x 				19
		y 				157
		z 				121
		heading 		0
		nsLeft 		0
		nsTop 		1
		nsBottom 	72
		nsRight 		39
 		description "the hotel sign"
		sightAngle 	40
	)
	(method (doVerb theVerb invItem)
		(hotelSign doVerb: theVerb invItem)
	)
)
(instance eastWindowF of Feature
	(properties
		x 				264
		y 				157
		z 				131
		heading 		0
		nsLeft 		244
		nsTop 		6
		nsBottom 	47
		nsRight 		284
 		description "the east window"
		sightAngle 	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if onEscape
					(if (ObjInRoom iPills)
						(if hangingOn
							(Print "Looking at the dark window,
								you notice a bottle of pills sitting on the window
								ledge.")
						else
							(Print "There may be something in the east window,
								but from here, you're not sure.")
						)
					else
						(Print "You broke it, didn't you?")
					)
				else
					(hookerWindowF doVerb: theVerb invItem)
				)
			)
			(verbDo
				(cond
					((not onEscape)
						(Print "There's no way to reach the window from here.")
					)
					((or	(!= (ego view?) vLarryGettingBottle)
							(== (ego loop?) lTiesToSelf))
						(HandsOff)
						(curRoom setScript: sFallDie)
					)
					((== (ego loop?) lTiesToRail)
						(HandsOff)
						(curRoom setScript: sJumpRail)
					)
					((not (Btst fAlleyWindowBroken))
						(Print "You consider sticking your hand through a solid
							glass window, but realize that if you did,
							you wouldn't be able to play the violin.")
						(Print "(Of course, you can't play the violin
							now, but you get the idea.)" LOW)
				   )
					((!= ((inventory at: iPills) owner?) 160)
						(Print "You already took the pills.
							Why go through all this again?")
					)
					(else
						(HandsOff)
						(curRoom setScript: sGetPills)
						(bottle
							z:			1000,
							dispose:
						)
					)
				)
			)
			(verbUse
				(switch invItem
					(iHammer
						(cond
							(hangingOn
								(if (Btst fAlleyWindowBroken)
									(Print "You already smashed the window.
										Why do it again?")
								else
									(Bset fAlleyWindowBroken)
									(HandsOff)
									(curRoom setScript: sSmashWindow)
								)
							)
							(else
								(Print "You might try reaching over to the window
									first.")
							)
						)
					)
					(iPocketKnife
						(Print "You could never pry that window open with your
							pocket knife.")
					)
					(iRing
						(Print "That's an idea; it's just not a good idea!")
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

(instance fenceF of Feature
	(properties
		x 				275
		y 				143
		z 				0
		heading 		0
		nsLeft 		233
		nsTop 		104
		nsBottom 	182
		nsRight 		318
		sightAngle 	40
		approachX	237
		approachY	172
		description	"the fence"
		lookStr 		"You see alleys stretching off to infinity."
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "The fence is much too tall for you to climb over.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance lidF of Feature
	(properties
		x 				124
		y 				160
		z 				45
		heading 		0
		nsLeft 		84
		nsTop 		101
		nsBottom 	130
		nsRight 		165
 		description	"the lid"
		sightAngle 	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "Unfortunately the lid on the trash bin just will not
					close.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(procedure (FallAndDie)
	(HandsOff)
	(Print "Smart move, Larry!")
	(sFallDie start: 3)
	(ego
		normal:		FALSE,
		y: 			FALLY,
		view:			vLarryFalls,
		cycleSpeed:	(+ larrySpeed 6),
		setScript:	0,
	)
	(theMusic2 stop:)
	(curRoom setScript: sFallDie)
)

