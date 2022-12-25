;**
;**	Logics for Room 510 -- QuikiMart Interior
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Mike Larsen
;**
;**	Last Update:  June 22, 1991
;**

(script# 510)

(public
	rm510		0
)


(local
	lubberMaterial
	lubberTexture
	lubberColor
	lubberLubrication
	lubberPattern
	lubberFlavor
	lubberWeight
	lubberThickness
	lubberCoating
	lubberSize

	sprayPrice
	lubberPrice
	winePrice
	magazinePrice
	moneyOwed
	askedForBucks
	readyForMoney
	notPaid
	clerkTalk
	scopeRoom
	headCount
	readSign
	lookedAtMagRack
)

(procedure
	OweMoney
)

(define clerkX 50)
(define clerkY 160)
(define magStandX	180)
(define magStandY 185)
(define wineShelveX 49)
(define wineShelveY 121)
(define sprayStandX 87)
(define sprayStandY 165)


;(define	mQuikiMartInterior	510)
	(define	sHeadsOut			511)
	(define	sPervert				512)
	(define	sHeadsIn				513)
	(define	sGunShot				514)

(define	pQuikiMartInterior	510)

(define	vQuikiMartInterior	510)
	(define	lSecureCam0		0)
	(define	lSecureCam1		1)
	(define	lSecureCam2		2)
	(define	lSecureCam3		3)
	(define  lLubberSign		4)

(define	vLarryShot				511)
	(define	lLarryShot		0)
	(define	lLarryGetsWine	1)
	(define	lSecurityCam	2)

(define	vCounterPerson			512)
	(define	lClerkTalk		0)
	(define	lClerkTakeCash	1)
	(define	lClerkShootGun	2)

(define vQuikiMartExtras		513)
	(define	lDudeLeft		0)
	(define	lKid				1)
	(define	lDudeUp			2)
	(define	lGranny			3)
	(define	lRabi				4)


(instance rm510 of LLRoom
	(properties
		picture	510
		south		500
		west		500
		lookStr 	"You are in a 24-hour convenience store.
					There is a magazine rack near the front door,
					with a sign reading \"This no riblaly -- no leeding.\"
					Except for the clerk standing behind the counter,
					the store appears to be deserted.
					A small sign graces the counter."
	)
	(method (init)
		(LoadMany VIEW
			vLarryShot
			vCounterPerson
			vQuikiMartInterior
			vQuikiMartExtras
		)
		(LoadMany SOUND
			mQuikiMartInterior
			sGunShot
			sHeadsIn
			sHeadsOut
			sPervert
		)
		(LoadMany FONT giantFont)
		(theMusic
			number:	mQuikiMartInterior,
			vol:		127,
			loop:		-1,
			flags:	1,
			play:
		)
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	0 0 319 0 319 189 76 189 76 184 311 184 311 154 236 130
						236 128 247 120 194 110 26 121 56 136 56 157 38 164 4 167
						4 184 51 184 51 189 0 189,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	107 173 279 173 279 181 107 181,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	61 150 163 150 163 159 102 159 97 163 61 163,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	165 150 263 150 263 159 165 159,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	62 130 221 130 221 143 62 143,
				yourself
			)
		)
		(switch prevRoomNum
			(500
				(ego
					x:			60,
					init:
				)
			)
			(else
				(ego
					init:,
					x:			60,
					y:			180,
				)
			)
		)
		(cam1
			setCycle:	Oscillate,
			init:
		)
		(cam2
			setCycle:	Oscillate,
			init:
		)
		(cam3
			setCycle:	Oscillate,
			init:
		)
		(cam4
			setCycle:	Oscillate,
			init:
		)
		(cam5
			setCycle:	Oscillate,
			init:
		)
		(cam6
			setCycle:	Oscillate,
			init:
		)
		(cam7
			setCycle:	Oscillate,
			init:
		)
		(cam8
			setCycle:	Oscillate,
			init:
		)
		(cam9
			setCycle:	Oscillate,
			init:
		)
		(cam10
			setCycle:	Oscillate,
			init:
		)
		(cam11
			setCycle:	Oscillate,
			init:
		)
		(cam12
			setCycle:	Oscillate,
			init:
		)
		(lubberSign
			init:,
			stopUpd:,
			approachVerbs:	objectVerbs verbLook
		)
	  	(pClerk
			init:,
			approachVerbs:	actorVerbs verbLook
		)
		(sfxGunShot init:)
		(fShelves init:)
		(fShelves1 init:)
		(fMagazineStand
			init:,
			approachVerbs: objectVerbs verbLook
		)
		(fWineShelves
			init:,
			approachVerbs: objectVerbs verbLook
		)
		(fWineShelves1
			init:,
			approachVerbs: objectVerbs verbLook
		)
		(fWineShelves2
			init:,
			approachVerbs: objectVerbs verbLook
		)
		(fFreezer init:)
		(fBreathSpray
			init:,
			approachVerbs:	objectVerbs verbLook
		)
		(microwave init:)
		(checkoutCounter
			init:,
			approachVerbs:	objectVerbs verbLook
		)
		(super init:)
	)
	(method (doit)
		(super doit: &rest)
		(if
			(and
				readyForMoney
				notPaid
				(ego inRect: 45 180 81 190)
				(== (curRoom script?) 0)
			)
			(curRoom setScript:	sShootLarry)
		)
	)
	(method (dispose)
		(if (and	lubberFlavor
					(== notPaid	FALSE))
			(Cls)
			(Printf "\"Hey, Meester!!\" yells the clerk.
				\"I hope you enjoy your %s-weight, %s-gauge, %s-flavored,
				%s, %s, %s, %s, %s, %s small-sized lubber!!!\""
				lubberWeight
 				lubberThickness
				lubberFlavor

				lubberPattern
				lubberLubrication
				lubberColor
				lubberTexture
				lubberMaterial
				lubberCoating
			)
		)
		(theMusic fade:)
		(super dispose:)
	)
)

;Scripts

(instance sShootLarry of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(HandsOff)
				(ego
					cycleSpeed:	3,
					setMotion: 	0,
					view:			vLarryShot,
					setLoop:		lLarryShot,
					setCel:		0,
				)
				(pClerk
					cycleSpeed:	3,
					setPri:		11,
					setLoop:		lClerkShootGun,
					setCycle:	CycleTo 6 1 self
				)
				(Print "\"STOP! THIEF!\" cries the clerk!"
					#font:	bigFont
					#icon:	vCounterPerson lClerkShootGun 0
					HIGH
				)
			)
			(
				(sfxGunShot play:)
				(pClerk
					setCycle:	EndLoop self
				)
				(Print "\"Where?\" you wonder.")
			)
			(
				(ego
					setCycle:	EndLoop self
				)
			)
			(
				(= seconds 4)
			)
			(
				(theMusic fade:)
				(Death vLarryShot lSecurityCam 1)
				(Format @deathTitleStr "Caught you red-handed!")
				(GameOver "The clerk does not take kindly\n
					to your shoplifting.
					His magnum makes a permanent\n
					impression on you.\n
					(And, IN you!)"
				)
			)
		)
	)
)


(instance sBuyLubber of Script
	(method (doit &tmp c)
		(super doit)

		(if scopeRoom
			(if (== 4 (++ headCount))
				(= headCount 0)
				(= c ((ego _head?) cel?))
				(if (== c facingSW)
					(= cycles 5)				;**	return control to the script
				)
				((ego _head?)
					cel:	(switch c
								(facingEast		facingSE)
								(facingWest		facingNW)
								(facingSouth	facingSW)
								(facingNorth	facingNE)
								(facingSE		facingSouth)
								(facingSW		facingWest)
								(facingNE		facingEast)
								(facingNW		facingNorth)
							)
				)
			)
		)
	)

	(method (changeState newState)
		(switchto (= state newState)
			(
				(if (== ((inventory at: iLubber) owner?) ego)
					(Cls)
					(Print "You haven't used your old \"lubber\" yet!")
					(self dispose:)
				else
					(HandsOff)
					(ego
						setMotion:	PolyPath 50 160 self
					)
				)
			)
			(
				(Face ego pClerk)
				(ego moveHead: FALSE)
				(= seconds 3)
			)
			(
				(= scopeRoom TRUE)
				;**	Park at this state until doit has swiveled his head around
			)
			(
				(= scopeRoom FALSE)
				((ego _head?)
					cel:			facingWest,
				)
				(= seconds 3)
			)
			(
				(Cls)
				(Print "After looking around to make certain you're alone,
					you quietly ask the clerk if he has any protection available.")
				(= seconds 3)
			)
			(
				;**	Even the cameras stare at Larry:
				(cam1		setCel:	6)
				(cam2		setCel:	0)
				(cam3		setCel:	0)
				(cam4		setCel:	1)
				(cam5		setCel:	4)
				(cam6		setCel:	0)
				(cam7		setCel:	2)
				(cam8		setCel:	0)
				(cam9		setCel:	0)
				(cam10	setCel:	2)
				(cam11	setCel:	0)
				(cam12	setCel:	4)
				(pClerk
					cycleSpeed:	(+ 1 howFast),
					setCycle:	RandCycle 20 self
				)
			)
			(
				(Cls)
				(cam1		stopUpd:)
				(cam2		stopUpd:)
				(cam3		stopUpd:)
				(cam4		stopUpd:)
				(cam5		stopUpd:)
				(cam6		stopUpd:)
				(cam7		stopUpd:)
				(cam8		stopUpd:)
				(cam9		stopUpd:)
				(cam10	stopUpd:)
				(cam11	stopUpd:)
				(cam12	stopUpd:)
				(Print "\"Sure, we got lubbers!\"")
				(Print "(Obviously you've hit his area of expertise!)" LOW)
				(pClerk
					setCycle:	RandCycle 18 self
				)
			)
			(
				(theGame setCursor: ARROW_CURSOR TRUE)
				(Animate (cast elements?) FALSE)
				(if
					(Print "\"What kind lubbers you want?\" he asks."
						#button	{Lambskin}	0
						#button  {Latex}	1
					)
					(= lubberMaterial "latex")
				else
					(= lubberMaterial "lambskin")
				)
				(pClerk
					setCycle:	RandCycle 16 self
				)
			)
			(
				(if
					(Print "\"What texture you want?\" he asks."
						#button	{Smooth}	0
						#button  {Ribbed}	1
					)
					(= lubberTexture "libbed")
				else
					(= lubberTexture "smooth")
				)
				(pClerk
					setCycle:	RandCycle 14 self
				)
			)
			(
				(if
					(Print "\"What color you want?\" he asks."
						#button	{Colored}	0
						#button  {Plain}	1
					)
					(= lubberColor "plain")
				else
					(= lubberColor "colored")
				)
				(pClerk
					setCycle:	RandCycle 12 self
				)
			)
			(
				(if
					(Print "\"What surface you want?\" he asks."
						#button	{Lubricated}	0
						#button  {Rough-cut}	1
					)
					(= lubberLubrication "rough-cut")
				else
					(= lubberLubrication "lubricated")
				)
				(pClerk
					setCycle:	RandCycle 10 self
				)
			)
			(
				(if
					(Print "\"What pattern you want?\" he asks."
						#button	{Striped}	0
						#button  {Plaid}	1
					)
					(= lubberPattern "plaid")
				else
					(= lubberPattern "striped")
				)
				(pClerk
					setCycle:	RandCycle 8 self
				)
			)
			(
				(if
					(Print "\"What flavor you want?\" he asks."
						#button	{Peppermint}	0
						#button  {Spearmint}	1
					)
					(= lubberFlavor "spearmint")
				else
					(= lubberFlavor "peppermint")
				)
				(pClerk
					setCycle:	RandCycle 6 self
				)
			)
			(
				(if
					(Print "\"What weight you want?\" he asks."
						#button	{Light}	0
						#button  {Heavy}	1
					)
					(= lubberWeight "heavy")
				else
					(= lubberWeight "light")
				)
				(pClerk
					setCycle:	RandCycle 4 self
				)
			)
			(
				(if
					(Print "\"What thickness you want?\" he asks."
						#button	{Normal}	0
						#button  {Industrial}	1
					)
					(= lubberThickness "industrial")
				else
					(= lubberThickness "normal")
				)
				(pClerk
					setCycle:	RandCycle 4 self
				)
			)
			(
				(if
					(Print "\"What protection you want?\" he asks."
						#button	{Plain}	0
						#button  {Spermicide}	1
					)
					(= lubberCoating "spermicidal")
				else
					(= lubberCoating "non-spermicidal")
				)
				(pClerk
					setCycle:	RandCycle 4 self
				)
			)
			(
				(switch	(Print "\"What size you want?\" he asks."
								#button	{Large}	0
								#button  {Giant}	1
								#button  {Gonzo}	2
							)
					(0	(= lubberSize "large"))
					(1	(= lubberSize "giant"))
					(2	(= lubberSize "gonzo"))
				)
				(theGame setCursor: ((theIconBar curIcon?) cursor?) TRUE)
				(pClerk
					setCycle:	RandCycle 4 self
				)
			)
			(
				(Print "\"Oakie, doakie Meester!\"")
				(pClerk
					setCycle:	RandCycle 4 self
				)
			)
			(
				(Printf "\"Hey, everybody!! This weirdo just bought a
					%s-weight, %s-gauge, %s-flavored,
					%s, %s, %s, %s, %s, %s small-sized lubber!!!\""
					lubberWeight
 					lubberThickness
					lubberFlavor

					lubberPattern
					lubberLubrication
					lubberColor
					lubberTexture
					lubberMaterial
					lubberCoating
					WIDE
				)
				(theMusic stop:)
				(= seconds 2)
			)
			(
				(sfxHeadsOut
					init:,
					play:
				)
				(pDudeLeft	init:)
				(pKid			init:)
				(pDudeUp		init:)
				(pGranny		init:)
				(pRabi		init:)
				(= seconds 2)
			)
			(
				(sfxPervert play:)
				(Print "WHAT A PERVERT!!"
					#font 	giantFont
					#mode		teJustCenter
					#at:		-1 4
					#width:	250
				)
				(sfxHeadsIn
					init:,
					play:
				)
				(ego get: iLubber)
				(pDudeLeft	dispose:)
				(pKid			dispose:)
				(pDudeUp		dispose:)
				(pGranny		dispose:)
				(pRabi		dispose:)
				(cam1		setCycle:	Oscillate)
				(cam2		setCycle:	Oscillate)
				(cam3		setCycle:	Oscillate)
				(cam4		setCycle:	Oscillate)
				(cam5		setCycle:	Oscillate)
				(cam6		setCycle:	Oscillate)
				(cam7		setCycle:	Oscillate)
				(cam8		setCycle:	Oscillate)
				(cam9		setCycle:	Oscillate)
				(cam10	setCycle:	Oscillate)
				(cam11	setCycle:	Oscillate)
				(cam12	setCycle:	Oscillate)
				(= lubberPrice (Random 4 10))
				(OweMoney)
				(pClerk
					setCycle:	RandCycle 4 self
				)
			)
			(
				(Points pGotLubber ptsGotLubber)
				(Printf "He hands you the package,
					saying \"All together, that will be $%d.00, prease.\""
					moneyOwed
				)
				(= askedForBucks TRUE)
				(theMusic play:)
				(HandsOn)
				(NormalEgo facingWest)
				(self dispose:)
			)
		)
	)
)
(instance sGrabWine of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(HandsOff)
				(Face ego fWineShelves)
				(= cycles 7)
			)
			(
				(ego
					view:			vLarryShot,
					setLoop:		lLarryGetsWine,
					setCel:		0,
					cycleSpeed:	(* 2 howFast),
					setCycle:	EndLoop self,
				)
			)
			(
				(ego get: iWine)
				(Points pGotWine ptsGotWine)
				(= cycles 8)
			)
			(
				(Cls)
				(Print "You grab a lovely cardboard box of \"fine\" wine.
					You wonder how they can make a profit on this stuff,
					selling it for only a dollar a gallon!")
				(= cycles 3)
			)
			(
				(HandsOn)
				(NormalEgo facingNorth)
				(self dispose:)
			)
		)
	)
)
(instance sGrabBreathSpray of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(Face ego fBreathSpray)
				(= cycles 1)
			)
			(
				(pClerk
					setLoop:		lClerkTalk,
					setCycle:	RandCycle 10 self
				)
			)
			(
				(Cls)
				(Print "\"Hey, Meester, eet smells like chur brand!\"")
				(ego get: iBreathSpray)
				(= cycles 3)
			)
			(
				(self dispose:)
			)
		)
	)
)

;**************
;*** SOUNDS	***
;**************

(instance sfxPervert of Sound
	(properties
		number	sPervert
		loop		1
		vol 		127
		flags		1
	)
)
(instance sfxGunShot of Sound
	(properties
		number	sGunShot
		loop		1
		vol 		127
	)
)
(instance sfxHeadsIn	of Sound
	(properties
		number	sHeadsIn
		loop		1
		vol 		127
	)
)
(instance sfxHeadsOut of Sound
	(properties
		number	sHeadsOut
		loop		1
		vol 		127
	)
)


;*************
;*** PROPS ***
;*************

(instance pClerk of Person
	(properties
		view			vCounterPerson
		loop			lClerkTalk
		cel			0
		x				8
		y				131
		approachX	clerkX
		approachY	clerkY
		priority		10
		signal		fixPriOn
		description "the store clerk"
		lookStr 		"What law says that convenience store clerks must
						be non-English speaking?!"
	)
	(method (cue)
		(Cls)
		(pClerk setCycle: 0)
	)
	(method (doVerb theVerb invItem)
		(Cls)
		(switch theVerb
			(verbDo
				(Print "You can't stand the idea of running your hand through
					THAT turban!")
			)
			(verbZipper
				(curRoom setScript: sBuyLubber)
			)
			(verbUse
				(switch invItem
					(iWallet
						(if
							(and
								readyForMoney
								notPaid
								(>= larryDollars moneyOwed)
							)
							(if
								(== askedForBucks	FALSE)
								(Printf "\"Oakie, doakie Meester!
									That'll be $%d.00.\""
									moneyOwed
								)
							)
							(-= larryDollars moneyOwed)
							(= readyForMoney FALSE)
							(= notPaid FALSE)
							(= magazinePrice 0)
							(= winePrice 0)
							(= sprayPrice 0)
							(= lubberPrice 0)
							(= moneyOwed 0)
							(Print "You give him the money,
								with a pleasant \"Thanks a lot, Saddam!\"")
							(Print "\"Have a nice day.\"")
						else
							(if
								(< larryDollars moneyOwed)
								(Printf "Unfortunately, you only have $%d.00."
									larryDollars
								)
								(Print "(Perhaps you can sneak out without paying.)"
									LOW)
							else
								(Print "\"Ooooooh, Meester!
									Thank you for free money.
									What wonderful country thees is!!\"")
								(-= larryDollars 10)
							)
						)
					)
					(else
						(super doVerb: theVerb invItem &rest)
					)
				)
			)
			(verbTalk
				(if readSign
					(curRoom setScript: sBuyLubber)
				else
					(pClerk
						setLoop:		lClerkTalk,
						setCycle:	ForwardCounter 5 self
					)
					(switch clerkTalk
						(0
							(Print "\"Fposwqa feinnzx. Ofewass gewyiu qfbva.\""
								#dispose)
						)
						(1
							(Print "\"Poolskd asd woefpo lkjfq feps of foeps.\""
								#dispose)
						)
						(2
							(Print "\"Owf wepa vpifk qpqwf? Qqwpp apifrp qp foiw!\""
								#dispose)
						)
						(3
							(Print "\"Qufe weax zxo zvp zvqiw vpozcvajpbnv wf, apoz.
								Wfipa?\""
								#dispose)
						)
						(4
							(Print "\"Tqp nbq bqopni ba ajpqoi fqyq. HA HA HA!!\""
								#dispose)
						)
						(5
							(Print "\"Pqwwo qfp qwfnbv qozdvie.
								Ppqow poqw fvzxc ioplhjk.\""
								#dispose)
						)
						(6
							(Print "\"Zxzz pofqsoo afpoistr zxcv.\""
								#dispose)
						)
					)
					(if
						(< clerkTalk 6)
						(++ clerkTalk)
					else
						(= clerkTalk 0)
					)
				)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance cam1 of Prop
	(properties
		view			vQuikiMartInterior
		loop			3
		cel			6
		x				8
		y				55
		priority		15
		cycleSpeed	10
		signal		fixPriOn
		detailLevel	1
		description "the security camera"
		lookStr 		"Saddam is watching."
	)
	(method (doVerb theVerb invItem)
		(Cls)
		(switch theVerb
			(verbDo
				(Print "You can't reach the cameras.")
			)
			(verbZipper
				(Print "Larry! Don't expose yourself to the video camera!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance cam2 of Prop
	(properties
		view			vQuikiMartInterior
		loop			1
		cel			0
		x				27
		y				55
		priority		15
		cycleSpeed	15
		detailLevel	2
		signal		fixPriOn
	  	description "the security camera"
		lookStr 		"Saddam is watching."
	)
	(method (doVerb theVerb invItem)
		(cam1 doVerb: theVerb invItem)
	)
)
(instance cam3 of Prop
	(properties
		view			vQuikiMartInterior
		loop			2
		cel			0
		x				46
		y				55
		priority		15
		cycleSpeed	5
		detailLevel	3
		signal		fixPriOn
		description "the security camera"
		lookStr 		"Saddam is watching."
	)
	(method (doVerb theVerb invItem)
		(cam1 doVerb: theVerb invItem)
	)
)
(instance cam4 of Prop
	(properties
		view			vQuikiMartInterior
		loop			1
		cel			1
		x				67
		y				40
		cycleSpeed	10
		detailLevel	1
		description "the security camera"
		lookStr 		"Saddam is watching."
	)
	(method (doVerb theVerb invItem)
		(cam1 doVerb: theVerb invItem)
	)
)
(instance cam5 of Prop
	(properties
		view			vQuikiMartInterior
		loop			2
		cel			4
		x				93
		y				39
		cycleSpeed	25
		detailLevel	2
		description "the security camera"
		lookStr 		"Saddam is watching."
	)
	(method (doVerb theVerb invItem)
		(cam1 doVerb: theVerb invItem)
	)
)
(instance cam6 of Prop
	(properties
		view			vQuikiMartInterior
		loop			3
		cel			0
		x				121
		y				38
		cycleSpeed	20
		detailLevel	2
		description "the security camera"
		lookStr 		"Saddam is watching."
	)
	(method (doVerb theVerb invItem)
		(cam1 doVerb: theVerb invItem)
	)
)
(instance cam7 of Prop
	(properties
		view			vQuikiMartInterior
		loop			1
		cel			2
		x				150
		y				37
		cycleSpeed	30
		detailLevel	3
		description "the security camera"
		lookStr 		"Saddam is watching."
	)
	(method (doVerb theVerb invItem)
		(cam1 doVerb: theVerb invItem)
	)
)
(instance cam8 of Prop
	(properties
		view			vQuikiMartInterior
		loop			3
		cel			0
		x				181
		y				36
		cycleSpeed	15
		detailLevel	1
		description "the security camera"
		lookStr 		"Saddam is watching."
	)
	(method (doVerb theVerb invItem)
		(cam1 doVerb: theVerb invItem)
	)
)
(instance cam9 of Prop
	(properties
		view			vQuikiMartInterior
		loop			0
		cel			0
		x				209
		y				37
		cycleSpeed	10
		detailLevel	2
		description "the security camera"
		lookStr 		"Saddam is watching."
	)
	(method (doVerb theVerb invItem)
		(cam1 doVerb: theVerb invItem)
	)
)
(instance cam10 of Prop
	(properties
		view			vQuikiMartInterior
		loop			1
		cel			2
		x				238
		y				39
		cycleSpeed	20
		detailLevel	3
		description "the security camera"
		lookStr 		"Saddam is watching."
	)
	(method (doVerb theVerb invItem)
		(cam1 doVerb: theVerb invItem)
	)
)
(instance cam11 of Prop
	(properties
		view			vQuikiMartInterior
		loop			3
		cel			0
		x				269
		y				41
		cycleSpeed	15
		detailLevel	1
		description "the security camera"
		lookStr 		"Saddam is watching."
	)
	(method (doVerb theVerb invItem)
		(cam1 doVerb: theVerb invItem)
	)
)
(instance cam12 of Prop
	(properties
		view			vQuikiMartInterior
		loop			2
		cel			4
		x				299
		y				43
		cycleSpeed	10
		detailLevel	2
		description "the security camera"
		lookStr 		"Saddam is watching."
	)
	(method (doVerb theVerb invItem)
		(cam1 doVerb: theVerb invItem)
	)
)
(instance pDudeLeft of Prop
	(properties
		view		vQuikiMartExtras
		loop		lDudeLeft
		cel		0
		x			64
		y			98
		priority 12
		signal	fixPriOn
	)
)
(instance pKid of Prop
	(properties
		view		vQuikiMartExtras
		loop		lKid
		cel		0
		x			106
		y			89
		priority 5
		signal	fixPriOn
	)
)
(instance pDudeUp of Prop
	(properties
		view		vQuikiMartExtras
		loop		lDudeUp
		cel		0
		x			162
		y			145
		priority	10
		signal	fixPriOn
	)
)
(instance pGranny of Prop
	(properties
		view		vQuikiMartExtras
		loop		lGranny
		cel		0
		x			204
		y			142
		priority	12
		signal	fixPriOn
	)
)
(instance pRabi of Prop
	(properties
		view		vQuikiMartExtras
		loop		lRabi
		cel		0
		x			267
		y			159
		priority	14
		signal	fixPriOn
	)
)

;**********************
;***	  FEATURES	  ***
;**********************


(instance fShelves of Feature
	(properties
		x 97
		y 121
		z 0
		heading 0
		nsLeft 74
		nsTop 86
		nsBottom 157
		nsRight 120
 		description "the store shelves"
		lookStr 		"The shelves are filled with all the necessities of life:
						\"Dwinkies,\" \"Wallo-Wars,\" \"Loritos,\" various colas,
						cheap wine, and coffee. You don't see anything you really
						need in this part of the store."
		sightAngle 	40
	)
	(method (doVerb theVerb invItem)
		(Cls)
		(switch theVerb
			(verbDo
				(Print "These shelves don't contain the particular necessities
					you need right now.")
			)
			(verbSmellTaste
				(Print "Ah, the aroma!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance fShelves1 of Feature
	(properties
		x 180
		y 75
		z 0
		heading 0
		nsLeft 120
		nsTop 78
		nsBottom 139
		nsRight 241
 		description "the store shelves"
		lookStr 		"The shelves are filled with all the necessities of life:
						\"Dwinkies,\" \"Wallo-Wars,\" \"Loritos,\" various colas,
						cheap wine, and coffee. You don't see anything you really
						need in this part of the store."
		sightAngle 	40
	)
	(method (doVerb theVerb invItem)
		(fShelves doVerb: theVerb invItem)
	)
)
(instance fMagazineStand of Feature
	(properties
		x 192
		y 160
		z 0
		approachX magStandX
		approachY magStandY
		heading 0
		nsLeft 121
		nsTop 140
		nsBottom 181
		nsRight 263
 		description "the magazine rack"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(= lookedAtMagRack TRUE)
				(Print "There are many magazines on the rack, but your eyes
					immediately go to the ones with the naked girls
					on the cover.")
			)
			(verbDo
				(cond
					((Btst pGotMagazine)
						(Cls)
						(Print "Nah. That's enough reading material for one game.")
					)
					((== lookedAtMagRack TRUE)
						(Cls)
						(= magazinePrice 4)
						(Print "Suiting your discriminating tastes,
							you select the current issue of \"JUGS.\"")
						(OweMoney)
						(ego get: iMagazine)
						(Points pGotMagazine ptsGotMagazine)
					)
					(else
						(Print "There are many magazines on the convenience
							store's spacious magazine rack. 
							How will you ever choose?")
					)
				)
			)
			(else
				(Cls)
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance fWineShelves of Feature
	(properties
		x 81
		y 70
		z 0
		approachX wineShelveX
		approachY wineShelveY
		heading 0
		nsLeft 48
		nsTop 55
		nsBottom 85
		nsRight 114
 		description "the wine shelf"
		lookStr 		"That extra large box of wine looks like a bargain."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(if
					(!= ((inventory at: iWine) owner?) ego)
					(= winePrice 1)
					(OweMoney)
					(curRoom setScript: sGrabWine)
				else
					(Cls)
					(Print "Whew.
						One of those handsome boxes is about all a guy needs, Larry.")
				)
			)
			(else
				(Cls)
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance fWineShelves1 of Feature
	(properties
		x 36
		y 70
		z 0
		approachX wineShelveX
		approachY wineShelveY
		heading 0
		nsLeft 0
		nsTop 92
		nsBottom 115
		nsRight 72
 		description "the wine shelf"
		lookStr 		"That extra large box of wine looks like a bargain."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(fWineShelves doVerb: theVerb invItem)
	)
)
(instance fWineShelves2 of Feature
	(properties
		x 55
		y 70
		z 0
		approachX wineShelveX
		approachY wineShelveY
		heading 0
		nsLeft 39
		nsTop 86
		nsBottom 92
		nsRight 72
 		description "the wine shelf"
		lookStr 		"That extra large box of wine looks like a bargain."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(fWineShelves doVerb: theVerb invItem)
	)
)

(instance fFreezer of Feature
	(properties
		x 284
		y 105
		z 0
		heading 0
		nsLeft 250
		nsTop 72
		nsBottom 139
		nsRight 318
 		description "the food freezer"
		lookStr 		"There's nothing of interest inside the freezer."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(Cls)
		(switch theVerb
			(verbDo
				(Print "There's nothing of interest inside.")
			)
			(verbSmellTaste
				(Print "Chunks of your tongue are still
					frozen to your ice tray at home, Larry."
				)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance fBreathSpray of Feature
	(properties
		x 83
		y 136
		z 0
		approachX sprayStandX
		approachY sprayStandY
		heading 0
		nsLeft 72
		nsTop 119
		nsBottom 153
		nsRight 94
 		description "the rack filled with breath spray"
		lookStr 		"The counter contains a lovely display rack filled with
						breath spray. And, it's your brand!"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(if (!= ((inventory at: iBreathSpray) owner?) ego)
					(= sprayPrice 3)
					(OweMoney)
					(curRoom setScript: sGrabBreathSpray)
				else
					(Cls)
					(Print "Your current supply of breath spray is not yet depleted.")
				)
			)
			(verbSmellTaste
				(Print "The breath spray display is filled with your favorite,
					Uvula Killer Brand,
					`Garlic and Cloves 24-hour Breath Freshener.'"
				)
				(Print "\"Tastes great, and drives the babes crazy too!\"")
			)
			(else
				(Cls)
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance microwave of Feature
	(properties
		x 131
		y 115
		z 47
		heading 0
		nsLeft 115
		nsTop 57
		nsBottom 79
		nsRight 147
 		description "the microwave"
		lookStr		"You may want to warm something in this microwave oven,
						but you can't."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(Cls)
		(switch theVerb
			(verbDo
				(Print "It's broken.")
			)
			(verbUse
				(switch invItem
					(iApple
						(Print "Baked apples? Not really.")
					)
					(iCandy
						(Print "Yeah, good idea! Melted ice cream.")
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

(instance checkoutCounter of Feature
	(properties
		x				 22
		y				 70
		z				 32
		approachX	 clerkX
		approachY	 clerkY
		heading		 0
		nsLeft		 0
		nsTop			 132
		nsBottom		 158
		nsRight		 44
 		description	"the checkout counter"
		sightAngle	 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(= readSign TRUE)
				(Print "The sign reads,\n
					\"Lubbers For Sale\n
					just ask clerk!\"")
			)
			(verbDo
				(curRoom setScript: sBuyLubber)
			)
			(verbZipper
				(curRoom setScript: sBuyLubber)
			)
			(else
				(Cls)
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance lubberSign of View
	(properties
		view			vQuikiMartInterior
		loop			lLubberSign
		x				17
		y				136
		priority		pLGREEN
		signal		fixPriOn
		approachX	clerkX
		approachY	clerkY
		description	"the small, lubber sign"
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(= readSign TRUE)
				(Print "The sign reads, \"Lubbers For Sale --
						just ask clerk!\"")
			)
			(verbDo
				(curRoom setScript: sBuyLubber)
			)
			(verbZipper
				(curRoom setScript: sBuyLubber)
			)
			(else
				(Cls)
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(procedure (OweMoney)
	(+= moneyOwed (+ lubberPrice sprayPrice winePrice magazinePrice))
	(= readyForMoney TRUE)
	(= notPaid TRUE)
)
